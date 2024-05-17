package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.MicroorganismAntibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * Microorganismo.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 20/06/2017
 * @see Creación
 */
public interface MicroorganismDao
{

    /**
     * Lista microorganismo desde la base de datos.
     *
     * @return Lista microorganismos.
     * @throws Exception Error en la base de datos.
     */
    default List<Microorganism> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab76.lab76c1, lab76c2, lab76c3, lab76.lab04c1, lab04c2, lab04c3, "
                    + " lab04c4, lab76.lab07c1, lab206.lab77c1, lab77c2, lab77c3, lab77c4 " 
                    + " FROM lab76 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab76.lab04c1 "
                    + " LEFT JOIN lab206 ON lab206.lab76c1 = lab76.lab76c1 AND lab39c1 IS NULL "
                    + " LEFT JOIN lab77 ON lab77.lab77c1 = lab206.lab77c1 ", (ResultSet rs, int i) ->
            {
                Microorganism create = new Microorganism();
                create.setId(rs.getInt("lab76c1"));
                create.setName(rs.getString("lab76c2"));
                
                /*Antibiograma*/
                create.getSensitivity().setId(rs.getInt("lab77c1"));
                create.getSensitivity().setCode(rs.getString("lab77c2"));
                create.getSensitivity().setAbbr(rs.getString("lab77c3"));
                create.getSensitivity().setName(rs.getString("lab77c4"));

                /*Usuario*/
                create.getUser().setId(rs.getInt("lab04c1"));
                create.getUser().setName(rs.getString("lab04c2"));
                create.getUser().setLastName(rs.getString("lab04c3"));
                create.getUser().setUserName(rs.getString("lab04c4"));

                create.setLastTransaction(rs.getTimestamp("lab76c3"));
                create.setState(rs.getInt("lab07c1") == 1);

                return create;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el antibiograma asociado con un microorganismo y examen.
     *
     * @param idMicroorganism id del microorganismo
     * @param idTest Id del examen
     *
     * @return Lista microorganismos.
     * @throws Exception Error en la base de datos.
     */
    default Sensitivity getSensitivity(Integer idMicroorganism, Integer idTest) throws Exception
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            String query = ""
                    + "SELECT lab77.lab77c1, lab77c2, lab77c3, lab77c4, lab76c1, lab39c1 "
                    + "FROM lab206 "
                    + "INNER JOIN lab77 ON lab77.lab77c1 = lab206.lab77c1 ";

            String where = "WHERE lab76c1 = ? ";
            parameters.add(idMicroorganism);

            if (idTest == null || idTest == 0)
            {
                where += "AND lab39c1 IS NULL ";
            } else
            {
                where += "AND lab39c1 = ?";
                parameters.add(idTest);
            }

            return getJdbcTemplate().queryForObject(query + where, parameters.toArray(), (ResultSet rs, int i) ->
            {
                Sensitivity sensitivity = new Sensitivity();
                sensitivity.setId(rs.getInt("lab77c1"));
                sensitivity.setCode(rs.getString("lab77c2"));
                sensitivity.setAbbr(rs.getString("lab77c3"));
                sensitivity.setName(rs.getString("lab77c4"));

                return sensitivity;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista microorganismo desde la base de datos.
     *
     * @param id id del microorganismo
     *
     * @return Lista microorganismos.
     * @throws Exception Error en la base de datos.
     */
    default List<Microorganism> filterByMicroorganism(Integer id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab206.lab76c1, lab76c2, lab76c3, lab76.lab07c1, lab206.lab77c1, lab206.lab39c1 "
                    + "FROM lab206 "
                    + "LEFT JOIN lab76 ON lab76.lab76c1 = lab206.lab76c1 "
                    + "WHERE lab206.lab39c1 IS NOT NULL AND lab206.lab76c1 = ?",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Microorganism create = new Microorganism();
                create.setId(rs.getInt("lab76c1"));
                create.setTest(rs.getString("lab39c1") == null ? null : rs.getInt("lab39c1"));
                create.setName(rs.getString("lab76c2"));
                create.setSelected(id.equals(rs.getInt("lab77c1")));
                create.setLastTransaction(rs.getTimestamp("lab76c3"));
                create.setState(rs.getInt("lab07c1") == 1);
                create.getSensitivity().setId(rs.getInt("lab77c1"));

                return create;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra microorganismo en la base de datos.
     *
     * @param create Instancia microorganismo.
     *
     * @return Instancia microorganismo.
     * @throws Exception Error en la base de datos.
     */
    default Microorganism create(Microorganism create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab76")
                .usingGeneratedKeyColumns("lab76c1");

        HashMap parameters = new HashMap();
        parameters.put("lab76c2", create.getName().trim());
        parameters.put("lab76c3", timestamp);
        parameters.put("lab04c1", create.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);
        create.setState(true);

        return create;
    }

    /**
     * Actualiza microorganismo en la base de datos.
     *
     * @param update Instancia microorganismo.
     *
     * @return Objeto microorganismo.
     * @throws Exception Error en la base de datos.
     */
    default Microorganism update(Microorganism update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab76 SET lab76c2 = ?, lab76c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab76c1 = ?",
                update.getName().trim(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    /**
     * Actualiza microorganismo en la base de datos.
     *
     * @param microorganisms Instancia microorganismo.
     *
     * @return Objeto microorganismo.
     * @throws Exception Error en la base de datos.
     */
    default int updateBatchSensitivity(List<Microorganism> microorganisms) throws Exception
    {
        deleteMicroorganismTest(microorganisms.get(0).getId());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab206");

        for (Microorganism microorganism : microorganisms)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab77c1", microorganism.getSensitivity().getId());
            parameters.put("lab76c1", microorganism.getId());
            parameters.put("lab39c1", microorganism.getTest() == null || microorganism.getTest() == 0 ? null : microorganism.getTest());
            batchArray.add(parameters);
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()])).length;
    }

    /**
     * Elimina los microorganismos y examenes relacionados con un antibiograma
     *
     * @param id id microorganismo
     *
     * @return registros eliminados
     */
    default int deleteMicroorganismTest(Integer id)
    {
        String deleteSql = "DELETE FROM lab206 WHERE lab76c1 = ?";
        return getJdbcTemplate().update(deleteSql, id);
    }

    /**
     * Inserta microorganismos en lote
     *
     * @param create lista
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    default int createAll(List<Microorganism> create) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab76")
                .usingGeneratedKeyColumns("lab76c1");
        for (Microorganism microorganism : create)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab76c2", microorganism.getName().trim());
            parameters.put("lab76c3", timestamp);
            parameters.put("lab04c1", microorganism.getId());
            parameters.put("lab07c1", 1);
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[create.size()]));

        return inserted.length;
    }

    /**
     * Lista microorganismo - antibiotico desde la base de datos.
     *
     * @return Lista microorganismos.
     * @throws Exception Error en la base de datos.
     */
    default List<MicroorganismAntibiotic> listMicroorganismAntibiotic() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab210c1, lab210c2, lab210c3, lab210c4, lab210c5, lab76.lab76c1, lab76.lab76c2, lab79.lab79c1, lab79.lab79c2, lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab210.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab210 "
                    + "LEFT JOIN lab76 ON lab76.lab76c1 = lab210.lab76c1 "
                    + "LEFT JOIN lab79 ON lab79.lab79c1 = lab210.lab79c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab210.lab80c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab210.lab04c1 ", (ResultSet rs, int i) ->
            {
                MicroorganismAntibiotic microAntibiotic = new MicroorganismAntibiotic();
                //Microorganismo
                microAntibiotic.getMicroorganism().setId(rs.getInt("lab76c1"));
                microAntibiotic.getMicroorganism().setName(rs.getString("lab76c2"));
                //Antibiotico
                microAntibiotic.getAntibiotic().setId(rs.getInt("lab79c1"));
                microAntibiotic.getAntibiotic().setName(rs.getString("lab79c2"));
                microAntibiotic.setMethod(rs.getShort("lab210c1"));
                microAntibiotic.setInterpretation(rs.getShort("lab210c2"));
                microAntibiotic.setValueMin(rs.getString("lab210c3"));
                microAntibiotic.setValueMax(rs.getString("lab210c4"));
                //Operación
                microAntibiotic.getOperation().setId(rs.getInt("lab80c1"));
                microAntibiotic.getOperation().setIdParent(rs.getInt("lab80c2"));
                microAntibiotic.getOperation().setCode(rs.getString("lab80c3"));
                microAntibiotic.getOperation().setEsCo(rs.getString("lab80c4"));
                microAntibiotic.getOperation().setEnUsa(rs.getString("lab80c5"));
                /*Usuario*/
                microAntibiotic.setLastTransaction(rs.getTimestamp("lab210c5"));
                microAntibiotic.getUser().setId(rs.getInt("lab04c1"));
                microAntibiotic.getUser().setName(rs.getString("lab04c2"));
                microAntibiotic.getUser().setLastName(rs.getString("lab04c3"));
                microAntibiotic.getUser().setUserName(rs.getString("lab04c4"));

                return microAntibiotic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene un microorganismo - antibiotico desde la base de datos.
     *
     * @param idMicroorganism Id del Microorganismo
     * @param idAntibiotic Id del Antibiotico
     * @param method Metodo
     * @param interpretation Interpretación
     *
     * @return Microorganismo - Antiotico.
     *
     * @throws Exception Error en la base de datos.
     */
    default MicroorganismAntibiotic getMicroorganismAntibiotic(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab210c1, lab210c2, lab210c3, lab210c4, lab210c5, lab76.lab76c1, lab76.lab76c2, lab79.lab79c1, lab79.lab79c2, lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab210.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab210 "
                    + "LEFT JOIN lab76 ON lab76.lab76c1 = lab210.lab76c1 "
                    + "LEFT JOIN lab79 ON lab79.lab79c1 = lab210.lab79c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab210.lab80c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab210.lab04c1 "
                    + "WHERE lab210.lab76c1 = ? AND lab210.lab79c1 = ? AND lab210c1 = ? AND lab210c2 = ? ",
                    new Object[]
                    {
                        idMicroorganism, idAntibiotic, method, interpretation
                    }, (ResultSet rs, int i) ->
            {
                MicroorganismAntibiotic microAntibiotic = new MicroorganismAntibiotic();
                //Microorganismo
                microAntibiotic.getMicroorganism().setId(rs.getInt("lab76c1"));
                microAntibiotic.getMicroorganism().setName(rs.getString("lab76c2"));
                //Antibiotico
                microAntibiotic.getAntibiotic().setId(rs.getInt("lab79c1"));
                microAntibiotic.getAntibiotic().setName(rs.getString("lab79c2"));
                microAntibiotic.setMethod(rs.getShort("lab210c1"));
                microAntibiotic.setInterpretation(rs.getShort("lab210c2"));
                microAntibiotic.setValueMin(rs.getString("lab210c3"));
                microAntibiotic.setValueMax(rs.getString("lab210c4"));
                //Operación
                microAntibiotic.getOperation().setId(rs.getInt("lab80c1"));
                microAntibiotic.getOperation().setIdParent(rs.getInt("lab80c2"));
                microAntibiotic.getOperation().setCode(rs.getString("lab80c3"));
                microAntibiotic.getOperation().setEsCo(rs.getString("lab80c4"));
                microAntibiotic.getOperation().setEnUsa(rs.getString("lab80c5"));
                /*Usuario*/
                microAntibiotic.setLastTransaction(rs.getTimestamp("lab210c5"));
                microAntibiotic.getUser().setId(rs.getInt("lab04c1"));
                microAntibiotic.getUser().setName(rs.getString("lab04c2"));
                microAntibiotic.getUser().setLastName(rs.getString("lab04c3"));
                microAntibiotic.getUser().setUserName(rs.getString("lab04c4"));

                return microAntibiotic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra microorganismo - antibiograma en la base de datos.
     *
     * @param microAntibiotic Instancia microorganismo - antibiotico.
     *
     * @return Instancia Microorganismo - Antibiotico.
     * @throws Exception Error en la base de datos.
     */
    default MicroorganismAntibiotic create(MicroorganismAntibiotic microAntibiotic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab210");

        HashMap parameters = new HashMap();
        parameters.put("lab76c1", microAntibiotic.getMicroorganism().getId());
        parameters.put("lab79c1", microAntibiotic.getAntibiotic().getId());
        parameters.put("lab210c1", microAntibiotic.getMethod());
        parameters.put("lab210c2", microAntibiotic.getInterpretation());
        parameters.put("lab210c3", microAntibiotic.getValueMin());
        parameters.put("lab210c4", microAntibiotic.getValueMax());
        parameters.put("lab210c5", timestamp);
        parameters.put("lab80c1", microAntibiotic.getOperation().getId());
        parameters.put("lab04c1", microAntibiotic.getUser().getId());

        insert.execute(parameters);
        microAntibiotic.setLastTransaction(timestamp);
        return microAntibiotic;
    }

    /**
     * Actualiza microorganismo - antibiograma en la base de datos.
     *
     * @param microAntibiotic Instancia microorganismo - antibiograma.
     *
     * @return Objeto microorganismo - antibiograma.
     * @throws Exception Error en la base de datos.
     */
    default MicroorganismAntibiotic update(MicroorganismAntibiotic microAntibiotic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab210 SET lab210c3 = ?, lab210c4 = ?, lab80c1 = ?, lab210c5 = ?, lab04c1 = ? "
                + "WHERE lab76c1 = ? AND lab79c1 = ? AND lab210c1 = ? AND lab210c2 = ? ",
                microAntibiotic.getValueMin().trim(), microAntibiotic.getValueMax().trim(), microAntibiotic.getOperation().getId(), timestamp, microAntibiotic.getUser().getId(), microAntibiotic.getMicroorganism().getId(), microAntibiotic.getAntibiotic().getId(), microAntibiotic.getMethod(), microAntibiotic.getInterpretation());

        microAntibiotic.setLastTransaction(timestamp);
        return microAntibiotic;
    }

    /**
     * Elimina microorganismo - antibiograma
     *
     * @param idMicroorganism Id del Microorganismo
     * @param idAntibiotic Id del Antibiotico
     * @param method Metodo
     * @param interpretation Interpretación
     *
     */
    default void delete(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception
    {
        String deleteSql = "DELETE FROM lab210 WHERE lab76c1 = ? AND lab79c1 = ? AND lab210c1 = ? AND lab210c2 = ? ";
        getJdbcTemplate().update(deleteSql, idMicroorganism, idAntibiotic, method, interpretation);
    }
    
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
