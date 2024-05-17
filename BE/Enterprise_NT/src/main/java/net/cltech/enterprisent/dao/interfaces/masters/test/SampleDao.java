package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.SampleByService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Muestras.
 *
 * @version 1.0.0
 * @author enavas
 * @since 28/04/2017
 * @see Creación
 */
public interface SampleDao
{

    /**
     * Lista las muestras desde la base de datos
     *
     * @return Lista de Muestras
     * @throws Exception Error en base de datos
     */
    default List<Sample> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab24.lab24c1"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c3"
                    + ", lab24.lab24c4"
                    + ", lab24.lab24c5"
                    + ", lab24.lab24c6"
                    + ", lab24.lab24c7"
                    + ", lab24.lab24c8"
                    + ", lab24.lab04c1"
                    + ", lab24.lab07c1"
                    + ", lab24.lab56c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c10"
                    + ", lab24.lab24c11"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab56.lab56c2"
                    + ", lab24.lab24c12"
                    + ", lab24.lab24c13"
                    + ", lab24.lab24c14"
                    + ", lab24.lab24c15"
                    + ", lab24.lab24c16"
                    + ", lab24.lab24c17"
                    + "  FROM lab24"
                    + "  INNER JOIN lab56 ON lab56.lab56c1=lab24.lab56c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab24.lab04c1", (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setPrintable(rs.getBoolean("lab24c3"));
                sample.setCanstiker(rs.getInt("lab24c4"));
                sample.setCheck(rs.getBoolean("lab24c5"));
                sample.setManagementsample(rs.getString("lab24c6"));
                sample.setDaysstored(rs.getInt("lab24c7"));
                sample.setLastTransaction(rs.getTimestamp("lab24c8"));
                sample.setState(rs.getBoolean("lab07c1"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTypebarcode(rs.getBoolean("lab24c11"));
                sample.setQualityTime(rs.getLong("lab24c12"));
                sample.setQualityPercentage(rs.getInt("lab24c13"));
                sample.setMinimumTemperature(rs.getFloat("lab24c15"));
                sample.setMaximumTemperature(rs.getFloat("lab24c16"));
                sample.setCoveredSample(rs.getBoolean("lab24c17"));

                /*Usuario*/
                sample.getUser().setId(rs.getInt("lab04c1"));
                sample.getUser().setName(rs.getString("lab04c2"));
                sample.getUser().setLastName(rs.getString("lab04c3"));
                sample.getUser().setUserName(rs.getString("lab04c4"));
                /*Container */
                sample.getContainer().setId(rs.getInt("lab56c1"));
                sample.getContainer().setName(rs.getString("lab56c2"));
                sample.setSpecialStorage(rs.getBoolean("lab24c14"));
                return sample;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva Muestra en la base de datos
     *
     * @param sample Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos de la muestra.
     * @throws Exception Error en base de datos
     */
    default Sample create(Sample sample) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab24")
                .usingGeneratedKeyColumns("lab24c1");

        HashMap parameters = new HashMap();

        parameters.put("lab24c2", sample.getName());
        parameters.put("lab24c3", sample.isPrintable() ? 1 : 0);
        parameters.put("lab24c4", sample.getCanstiker());
        parameters.put("lab24c5", sample.isCheck() ? 1 : 0);
        parameters.put("lab24c6", sample.getManagementsample());
        parameters.put("lab24c7", sample.getDaysstored());
        parameters.put("lab24c8", timestamp);
        parameters.put("lab04c1", sample.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab56c1", sample.getContainer().getId());
        parameters.put("lab24c9", sample.getCodesample());
        parameters.put("lab24c10", sample.getLaboratorytype());
        parameters.put("lab24c11", sample.isTypebarcode() ? 1 : 0);
        parameters.put("lab24c12", sample.getQualityTime());
        parameters.put("lab24c13", sample.getQualityPercentage());
        parameters.put("lab24c14", sample.getSpecialStorage() ? 1 : 0);
        parameters.put("lab24c15", sample.getMinimumTemperature());
        parameters.put("lab24c16", sample.getMaximumTemperature());
        parameters.put("lab24c17", sample.isCoveredSample() ? 1 : 0);

        Number key = insert.executeAndReturnKey(parameters);

        sample.setId(key.intValue());
        sample.setLastTransaction(timestamp);
        return sample;
    }

    /**
     * Obtener informacion de la muestra en la base de datos
     *
     * @param id Id de la muestra.
     * @param name Nombre de la muestra.
     * @param code Codigo de la muestra.
     * @param container Codigo de el recipiente.
     * @param state Estado del recipiente.
     *
     * @return Instancia con los datos de la muestra.
     * @throws Exception Error en base de datos
     */
    default List<Sample> get(Integer id, String name, String code, Integer container, Boolean state) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab24.lab24c1"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c3"
                    + ", lab24.lab24c4"
                    + ", lab24.lab24c5"
                    + ", lab24.lab24c6"
                    + ", lab24.lab24c7"
                    + ", lab24.lab24c8"
                    + ", lab24.lab04c1"
                    + ", lab24.lab07c1"
                    + ", lab24.lab56c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c10"
                    + ", lab24.lab24c11"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab56.lab56c2"
                    + ", lab24.lab24c12"
                    + ", lab24.lab24c13"
                    + ", lab24.lab24c14"
                    + ", lab24.lab24c15"
                    + ", lab24.lab24c16"
                    + ", lab24.lab24c17"
                    + "  FROM lab24"
                    + "  INNER JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1 = lab24.lab04c1";
            //where
            if (id != null)
            {
                query += " WHERE lab24.lab24c1 = ? ";
            }

            if (name != null)
            {
                query += (query.contains("WHERE") ? " AND " : " WHERE ") + " UPPER(lab24.lab24c2) = ? ";
            }

            if (code != null)
            {
                query += (query.contains("WHERE") ? " AND " : " WHERE ") + " UPPER(lab24.lab24c9) = ? ";
            }

            if (container != null)
            {
                query += (query.contains("WHERE") ? " AND " : " WHERE ") + " lab24.lab56c1 = ? ";
            }

            if (state != null)
            {
                query += (query.contains("WHERE") ? " AND " : " WHERE ") + " lab24.lab07c1 = ? ";
            }


            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            List<Object> params = new ArrayList<>();
            if (id != null)
            {
                params.add(id);
            }
            if (name != null)
            {
                params.add(name.toUpperCase());
            }
            if (code != null)
            {
                params.add(code.toUpperCase());
            }
            if (container != null)
            {
                params.add(container);
            }

            if (state != null)
            {
                params.add(state ? 1 : 0);
            }
            return getJdbcTemplate().query(query,
                    params.toArray(), (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setPrintable(rs.getBoolean("lab24c3"));
                sample.setCanstiker(rs.getInt("lab24c4"));
                sample.setCheck(rs.getBoolean("lab24c5"));
                sample.setManagementsample(rs.getString("lab24c6"));
                sample.setDaysstored(rs.getInt("lab24c7"));
                sample.setLastTransaction(rs.getTimestamp("lab24c8"));
                sample.setState(rs.getBoolean("lab07c1"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTypebarcode(rs.getBoolean("lab24c11"));
                sample.setQualityTime(rs.getLong("lab24c12"));
                sample.setQualityPercentage(rs.getInt("lab24c13"));
                sample.setSpecialStorage(rs.getBoolean("lab24c14"));
                sample.setMinimumTemperature(rs.getFloat("lab24c15"));
                sample.setMaximumTemperature(rs.getFloat("lab24c16"));
                sample.setCoveredSample(rs.getBoolean("lab24c17"));
                /*Usuario*/
                sample.getUser().setId(rs.getInt("lab04c1"));
                sample.getUser().setName(rs.getString("lab04c2"));
                sample.getUser().setLastName(rs.getString("lab04c3"));
                sample.getUser().setUserName(rs.getString("lab04c4"));
                /*container*/
                sample.getContainer().setId(rs.getInt("lab56c1"));
                sample.getContainer().setName(rs.getString("lab56c2"));

                return sample;
            });

        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener informacion de la muestra por examen en la base de datos
     *
     * @param idTest Id del examen.
     *
     * @return Instancia con los datos de la muestra.
     * @throws Exception Error en base de datos
     */
    default Sample getSampleByTest(Integer idTest) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("SELECT lab24.lab24c1, lab24.lab24c2, lab24.lab24c9 "
                    + "FROM lab39 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "WHERE lab39.lab39c1 = ? ",
                    new Object[]
                    {
                        idTest
                    }, (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCodesample(rs.getString("lab24c9"));
                return sample;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la informacion de la muestra en la base de datos
     *
     * @param sample Instancia con los datos de la muestra.
     *
     * @return Objeto actualizado
     * @throws Exception Error en base de datos
     */
    default Sample update(Sample sample) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query = ""
                + " UPDATE lab24 SET"
                + "  lab24c2=?"
                + ", lab24c3=?"
                + ", lab24c4=?"
                + ", lab24c5=?"
                + ", lab24c6=?"
                + ", lab24c7=?"
                + ", lab24c8=?"
                + ", lab04c1=?"
                + ", lab07c1=?"
                + ", lab56c1=?"
                + ", lab24c9=?"
                + ", lab24c10=?"
                + ", lab24c11=?"
                + ", lab24c12=?"
                + ", lab24c13=?"
                + ", lab24c14=?"
                + ", lab24c15=?"
                + ", lab24c16=?"
                + ", lab24c17=?"
                + " WHERE lab24c1 = ?";

        getJdbcTemplate().update(query, sample.getName(),
                sample.isPrintable() ? 1 : 0,
                sample.getCanstiker(),
                sample.isCheck() ? 1 : 0,
                sample.getManagementsample(),
                sample.getDaysstored(),
                timestamp,
                sample.getUser().getId(),
                sample.isState() ? 1 : 0,
                sample.getContainer().getId(),
                sample.getCodesample(),
                sample.getLaboratorytype(),
                sample.isTypebarcode() ? 1 : 0,
                sample.getQualityTime(),
                sample.getQualityPercentage(),
                sample.getSpecialStorage() ? 1 : 0,
                sample.getMinimumTemperature(),
                sample.getMaximumTemperature(),
                sample.isCoveredSample() ? 1 : 0,
                sample.getId()
        );
        sample.setLastTransaction(timestamp);
        return sample;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las muestras de un servicio desde la base de datos.
     *
     * @param idService Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<SampleByService> listSampleByService(int idService) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab171.lab10c1, lab10c2, lab171c1, lab171c2, lab171c3, lab24.lab24c1, lab24.lab24c2, lab24.lab24c9, lab24.lab24c10, "
                    + " lab171.lab04c1, lab04c2, lab04c3, lab04c4, lab171c5 "
                    + "FROM lab24 "
                    + "LEFT JOIN lab171 ON lab171.lab171c1 = lab24.lab24c1 AND lab171.lab171c3 = 1 AND lab171.lab10c1 = ? "
                    + "LEFT JOIN lab10 ON lab171.lab10c1 = lab10.lab10c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab171.lab04c1  ",
                    new Object[]
                    {
                        idService
                    }, (ResultSet rs, int i)
                    ->
            {
                SampleByService sampleByService = new SampleByService();
                sampleByService.getSample().setId(rs.getInt("lab24c1"));
                sampleByService.getSample().setName(rs.getString("lab24c2"));
                sampleByService.getSample().setCodesample(rs.getString("lab24c9"));
                sampleByService.getSample().setLaboratorytype(rs.getString("lab24c10"));
                sampleByService.getService().setId(rs.getInt("lab10c1"));
                sampleByService.getService().setName(rs.getString("lab10c2"));
                sampleByService.setExpectedTime(rs.getString("lab171c2") == null ? 0 : rs.getInt("lab171c2"));
                
                sampleByService.setLastTransaction(rs.getTimestamp("lab171c5"));
                /*Usuario*/
                sampleByService.getUser().setId(rs.getInt("lab04c1"));
                sampleByService.getUser().setName(rs.getString("lab04c2"));
                sampleByService.getUser().setLastName(rs.getString("lab04c3"));
                sampleByService.getUser().setUserName(rs.getString("lab04c4"));
                
                return sampleByService;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta muestras por servicio desde la base de datos.
     *
     * @param sample Objeto a ser registrado.
     *
     * @return Objeto registrado.
     * @throws java.lang.Exception
     */
    default SampleByService insertSamplesByService(SampleByService sample) throws Exception
    {
        deleteSampleByService(sample.getService().getId(), sample.getSample().getId());
        
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab171");

        HashMap parameters = new HashMap();
        parameters.put("lab10c1", sample.getService().getId());
        parameters.put("lab171c1", sample.getSample().getId());
        parameters.put("lab171c2", sample.getExpectedTime());
        parameters.put("lab171c3", 1);
        parameters.put("lab171c5", timestamp);
            parameters.put("lab04c1", sample.getUser().getId());
        insert.execute(parameters);

        return sample;
    }

    /**
     * Eliminar muestras por servicios.
     *
     * @param idService Id del servicio.
     * @param idSample Id de la muestra.
     *
     * @throws Exception Error en base de datos.
     */
    default void deleteSampleByService(int idService, int idSample) throws Exception
    {
        getJdbcTemplate().execute("DELETE FROM lab171 WHERE lab171c1 = " + idSample + " AND lab10c1 = " + idService + " AND lab171c3 = 1");
    }

    /**
     * Lista las muestras asignadas a una muestra
     *
     * @param id id muestra padre
     *
     * @return lista de submuestras
     * @throws Exception Error en base de datos
     */
    default List<Sample> listSubSample(int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab24.lab24c1,lab24c2,lab24c9,lab24c10, lab97.lab24c1 padre "
                    + " FROM lab24 "
                    + " INNER JOIN lab97 ON lab24.lab24c1 = lab97.lab97c1 "
                    + " WHERE lab24.lab07c1 = 1 AND lab97.lab24c1 = ?",
                    (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setSelected(rs.getString("padre") != null);
                sample.setLaboratorytype(rs.getString("lab24c10"));

                return sample;
            }, id);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
      /**
     * Lista las muestras asignadas a una muestra
     *
     * @param id id muestra padre
     *
     * @return lista de submuestras
     * @throws Exception Error en base de datos
     */
    default List<Sample> listSubSampleSelect(int id) throws Exception
    {
        try
        {   
            return getJdbcTemplate().query(""
                    + "SELECT lab24.lab24c1,lab24c2,lab24c9,lab24c10, lab97.lab24c1 padre "
                    + " FROM lab24 "
                    + " LEFT JOIN lab97 ON lab24.lab24c1 = lab97.lab97c1 AND lab97.lab24c1 = ? "
                    + " WHERE lab24.lab24c1 != ? AND lab24.lab07c1 = 1",
                    (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setSelected(rs.getString("padre") != null);
                sample.setLaboratorytype(rs.getString("lab24c10"));

                return sample;
            }, id, id);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    

    /**
     * Inserta las submuestras de una muestra (lab97)
     *
     * @param sample muestra con información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error de base de datos
     */
    default int insertSubSample(Sample sample) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab97");

        sample.getSubSamples().stream().map((subSample)
                ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab24c1", sample.getId());
            parameters.put("lab97c1", subSample.getId());
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        return insert.executeBatch(batchArray.toArray(new HashMap[sample.getSubSamples().size()])).length;
    }

    /**
     * Elimina las submuestras de una muestra
     *
     * @param idSample id de la muestra
     *
     * @return registros afectados
     */
    default int deleteSubSamples(int idSample)
    {
        return getJdbcTemplate().update("DELETE FROM lab97 WHERE lab24c1 = ? ", idSample);
    }
    
     /**
     * obtiene el tipo de laboratorio de una muestra
     *
     * @param idSample id de la muestra
     *
     * @return registros afectados
     */
    default Sample getTypeLaboratoryBySample(int idSample)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT lab24c10, lab24c9")
                .append(" FROM lab24 ")
                .append("WHERE lab24c1 = ").append(idSample);

        return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) -> 
        {
            Sample sample = new Sample();
            sample.setCodesample(rs.getString("lab24c9"));
            sample.setLaboratorytype(rs.getString("lab24c10"));
            return sample;
        });
    }
}
