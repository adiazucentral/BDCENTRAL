package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.AnalyzerMicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los destinos de microbiologia.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/02/2017
 * @see Creación
 */
public interface MicrobiologyDestinationDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista los destinos desde la base de datos.
     *
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default List<MicrobiologyDestination> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab207c1, lab207c2, lab207c3, lab207c4, lab207c5, lab207.lab04c1, lab04c2, lab04c3, lab04c4, lab207.lab07c1 "
                    + "FROM lab207 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab207.lab04c1", (ResultSet rs, int i) ->
            {
                MicrobiologyDestination destination = new MicrobiologyDestination();
                destination.setId(rs.getInt("lab207c1"));
                destination.setCode(rs.getString("lab207c2"));
                destination.setName(rs.getString("lab207c3"));
                destination.setReportTask(rs.getInt("lab207c4") == 1);
                /*Usuario*/
                destination.getUser().setId(rs.getInt("lab04c1"));
                destination.getUser().setName(rs.getString("lab04c2"));
                destination.getUser().setLastName(rs.getString("lab04c3"));
                destination.getUser().setUserName(rs.getString("lab04c4"));

                destination.setLastTransaction(rs.getTimestamp("lab207c5"));
                destination.setState(rs.getInt("lab07c1") == 1);

                return destination;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default MicrobiologyDestination create(MicrobiologyDestination destination) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab207")
                .usingGeneratedKeyColumns("lab207c1");

        HashMap parameters = new HashMap();
        parameters.put("lab207c2", destination.getCode().trim());
        parameters.put("lab207c3", destination.getName().trim());
        parameters.put("lab207c4", destination.isReportTask() ? 1 : 0);
        parameters.put("lab207c5", timestamp);
        parameters.put("lab04c1", destination.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        destination.setId(key.intValue());
        destination.setLastTransaction(timestamp);

        return destination;
    }

    /**
     * Obtener información de un destino por un campo especifico.
     *
     * @param id ID del destino a ser consultado.
     * @param code Codigo del destino a ser consultado.
     * @param name Nombre del destino a ser consultado.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default MicrobiologyDestination get(Integer id, String code, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab207c1, lab207c2, lab207c3, lab207c4, lab207c5, lab207.lab04c1, lab04c2, lab04c3, lab04c4, lab207.lab07c1 "
                    + "FROM lab207 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab207.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab207c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab207c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab207c3) = ? ";
            }
            /*Order By, Group By y demas complementos del consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (code != null)
            {
                object = code.toUpperCase();
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                MicrobiologyDestination destination = new MicrobiologyDestination();
                destination.setId(rs.getInt("lab207c1"));
                destination.setCode(rs.getString("lab207c2"));
                destination.setName(rs.getString("lab207c3"));
                destination.setReportTask(rs.getInt("lab207c4") == 1);
                /*Usuario*/
                destination.getUser().setId(rs.getInt("lab04c1"));
                destination.getUser().setName(rs.getString("lab04c2"));
                destination.getUser().setLastName(rs.getString("lab04c3"));
                destination.getUser().setUserName(rs.getString("lab04c4"));

                destination.setLastTransaction(rs.getTimestamp("lab207c5"));
                destination.setState(rs.getInt("lab07c1") == 1);

                return destination;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     *
     * @return Objeto del destino modificada.
     * @throws Exception Error en la base de datos.
     */
    default MicrobiologyDestination update(MicrobiologyDestination destination) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab207 SET lab207c2 = ?, lab207c3 = ?, lab207c4 = ?, lab207c5 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab207c1 = ?",
                destination.getCode(), destination.getName(), destination.isReportTask() ? 1 : 0, timestamp, destination.getUser().getId(), destination.isState() ? 1 : 0, destination.getId());

        destination.setLastTransaction(timestamp);

        return destination;
    }

    /**
     * Obtener todos los usuarios asigados a un destino
     * en analizadores en destinos de microbiologia
     *
     * @param idDestination
     * @return Lista de usuarios asignados a cierto destino
     * @throws Exception Error en la base de datos.
     */
    default List<AnalyzerMicrobiologyDestination> getMicrobiologyAnalyzersDestinations(Integer idDestination) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab186.lab207c1 AS lab207c1, "
                    + "lab04.lab04c1 AS lab04c1, "
                    + "lab04.lab04c4 AS lab04c4, "
                    + "lab04.lab04c30 AS lab04c30, "
                    + "lab40.lab40c3 AS lab40c3 "
                    + "FROM lab04 "
                    + "LEFT JOIN lab186 ON lab186.lab04c1 = lab04.lab04c1 AND lab186.LAB207C1 = ? "
                    + "LEFT JOIN lab207 ON lab207.lab207c1 = lab186.lab207c1 AND lab207.LAB207C1 = ? "
                    + "LEFT JOIN lab40 ON lab40.lab40c1 = lab04.lab04c30 "
                    + "WHERE lab04C15 = 12 AND lab04.lab07c1 = 1";
            
            
            return getConnection().query(query, (ResultSet rs, int i) ->
            {
                AnalyzerMicrobiologyDestination destinations = new AnalyzerMicrobiologyDestination();
                destinations.setUserId(rs.getInt("lab04c1"));
                destinations.setUserName(rs.getString("lab04c4"));
                destinations.setReferenceLaboratory(rs.getInt("lab04c30"));
                destinations.setNameReferenceLaboratory(rs.getString("lab40c3"));
                destinations.setSelected(rs.getString("lab207c1") != null);
                return destinations;
            }, idDestination,idDestination);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Elimina todos los usuarios asigados a un destino en
     * analizadores en destinos de microbiologia
     *
     * @param list
     * @return Número de registros afectados
     * @throws Exception Error en la base de datos.
     */
    default int deleteMicrobiologyAnalyzersDestinations(List<AnalyzerMicrobiologyDestination> list) throws Exception
    {
        try
        {
            int rowsAffected = 0;
            for (AnalyzerMicrobiologyDestination analyzer : list)
            {
                rowsAffected += getConnection().update("DELETE FROM lab186 "
                        + "WHERE lab207c1 = ? "
                        + "AND lab04c1 = ?"
                        , analyzer.getIdMicrobiologyDestination()
                        , analyzer.getUserId());
            }
            
            return rowsAffected;
        }
        catch (EmptyResultDataAccessException ex)
        {
            return -1;
        }
    }
    
    /**
     * Inserta cada registro a analizadores en destinos de microbiologia
     *
     * @param list
     * @throws Exception Error en la base de datos.
     */
    default void saveMicrobiologyAnalyzersDestinations(List<AnalyzerMicrobiologyDestination> list) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab186");
            list.stream().map((analyzer) ->
            {
                HashMap parameters = new HashMap();
                parameters.put("lab207c1", analyzer.getIdMicrobiologyDestination());
                parameters.put("lab04c1", analyzer.getUserId());
                return parameters;
            }).forEachOrdered((parameters) ->
            {
                batchArray.add(parameters);
            });
            insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }
    
    /**
     * Elimina todos los usuarios asigados a un destino, por el id de este, en
     * analizadores en destinos de microbiologia
     *
     * @param idDestination
     * @return Número de registros afectados
     * @throws Exception Error en la base de datos.
     */
    default int deleteMicrobiologyAnalyzersDestinations(int idDestination) throws Exception
    {
        try
        {
            int rowsAffected = 0;
            rowsAffected += getConnection().update("DELETE FROM lab186 "
                        + "WHERE lab207c1 = ? "
                        , idDestination);
            return rowsAffected;
        }
        catch (EmptyResultDataAccessException ex)
        {
            return -1;
        }
    }
    
    /**
     *
     * Elimina un destino del base de datos.
     *
     * @param id ID del destino.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }
}
