package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * Antibiograma.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 27/06/2017
 * @see Creación
 */
public interface SensitivityDao
{

    /**
     * Lista antibiograma desde la base de datos.
     *
     * @return Lista antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    default List<Sensitivity> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab77c1, lab77c2,lab77c3,lab77c4, lab77c5, lab77c7, lab77.lab04c1, lab04c2, lab04c3, lab04c4, lab77.lab07c1 "
                    + "FROM lab77 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab77.lab04c1", (ResultSet rs, int i) ->
            {
                Sensitivity create = new Sensitivity();
                create.setId(rs.getInt("lab77c1"));
                create.setCode(rs.getString("lab77c2"));
                create.setAbbr(rs.getString("lab77c3"));
                create.setName(rs.getString("lab77c4"));
                create.setSuppressionRule(rs.getInt("lab77c7") == 1);
                /*Usuario*/
                create.getUser().setId(rs.getInt("lab04c1"));
                create.getUser().setName(rs.getString("lab04c2"));
                create.getUser().setLastName(rs.getString("lab04c3"));
                create.getUser().setUserName(rs.getString("lab04c4"));

                create.setLastTransaction(rs.getTimestamp("lab77c5"));
                create.setState(rs.getInt("lab07c1") == 1);

                return create;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra antibiograma en la base de datos.
     *
     * @param create Instancia antibiograma.
     *
     * @return Instancia antibiograma.
     * @throws Exception Error en la base de datos.
     */
    default Sensitivity create(Sensitivity create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab77")
                .usingGeneratedKeyColumns("lab77c1");

        HashMap parameters = new HashMap();
        parameters.put("lab77c2", create.getCode().trim());
        parameters.put("lab77c3", create.getAbbr().trim());
        parameters.put("lab77c4", create.getName().trim());
        parameters.put("lab77c5", timestamp);
        parameters.put("lab77c7", create.isSuppressionRule() ? 1 : 0);
        parameters.put("lab04c1", create.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);
        create.setState(true);

        return create;
    }

    /**
     * Actualiza antibiograma en la base de datos.
     *
     * @param update Instancia antibiograma.
     *
     * @return Objeto antibiograma.
     * @throws Exception Error en la base de datos.
     */
    default Sensitivity update(Sensitivity update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab77 SET lab77c2 = ?,lab77c3 = ?,lab77c4 = ?, lab77c5 = ?, lab77c7 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab77c1 = ?",
                update.getCode().trim(), update.getAbbr().trim(), update.getName().trim(), timestamp, update.isSuppressionRule() ? 1 : 0, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    /**
     * Inserta antibioticos del antibiograma
     *
     * @param antibiotics lista
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    default int insertAntibiotics(List<AntibioticBySensitivity> antibiotics) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab78");
        for (AntibioticBySensitivity antibiotic : antibiotics)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab77c1", antibiotic.getId());
            parameters.put("lab79c1", antibiotic.getAntibiotic().getId());
            parameters.put("lab45c1", antibiotic.getUnit());
            parameters.put("lab78c2", antibiotic.getLine());
            batchArray.add(parameters);
        }
        return insert.executeBatch(batchArray.toArray(new HashMap[antibiotics.size()])).length;
    }

    /**
     * Elimina antibioticos del antibiograma
     *
     *
     * @param id
     *
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteAntibiotics(Integer id) throws Exception
    {
        String deleteSql = "DELETE FROM lab78 WHERE lab77c1 = ?";
        Object[] params =
        {
            id
        };
        return getJdbcTemplate().update(deleteSql, params);
    }
    
    /**
     * Obtiene los antibiogramas relacionados a una orden y a un examen
     *
     * @param idOrder
     * @param idTest
     * @return numero de registros eliminados
     * @throws Exception
     */
    default List<Sensitivity> getAntibiogramByOrderIdByTestId(long idOrder, int idTest) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;
            
        StringBuilder query = new StringBuilder();
        query.append("select Anti.lab77c1 AS lab77c1, ")
                .append("Anti.lab77c2 AS lab77c2, ")
                .append("Anti.lab77c3 AS lab77c3, ")
                .append("Anti.lab77c4 AS lab77c4, ")
                .append("Anti.lab77c7 AS lab77c7, ")
                .append("Mic.lab204c1 AS lab204c1 ")
                .append("from lab77 Anti ")
                .append("JOIN ").append(lab204).append(" as lab204 Mic on (Mic.lab22c1 = ").append(idOrder).append(" AND Mic.lab39c1 = ").append(idTest).append(") ");
        return getJdbcTemplate().query(query.toString(), 
                (ResultSet rs, int i) ->
        {
            Sensitivity antibiogram = new Sensitivity();
            antibiogram.setId(rs.getInt("lab77c1"));
            antibiogram.setCode(rs.getString("lab77c2"));
            antibiogram.setAbbr(rs.getString("lab77c3"));
            antibiogram.setName(rs.getString("lab77c4"));
            antibiogram.setState((rs.getInt("lab77c7") == 1));
            antibiogram.setIdMicrobialDeteccion(rs.getInt("lab204c1"));
            return antibiogram;
        });
    }
    
    /**
     * Actualiza el antibiograma de la lista de microorganismos 
     *
     * @param microorganisms Instancia microorganismo.
     * @param idSensitivity Id del antibiograma.
     *
     * @return Objeto microorganismo.
     * @throws Exception Error en la base de datos.
     */
    default int generalSensitivity(List<Microorganism> microorganisms, Integer idSensitivity) throws Exception
    {
        deleteSensitivity();
        
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab206");

        for (Microorganism microorganism : microorganisms)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab77c1", idSensitivity);
            parameters.put("lab76c1", microorganism.getId());
            parameters.put("lab39c1", null);
            batchArray.add(parameters);
        }
        return insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()])).length;
    }
   
    /**
     * Elimina la relación de antibiogramas - microorganismos
     *
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteSensitivity() throws Exception
    {
        String deleteSql = "DELETE FROM lab206 WHERE lab39c1 IS NULL";
        return getJdbcTemplate().update(deleteSql);
    }
    
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
