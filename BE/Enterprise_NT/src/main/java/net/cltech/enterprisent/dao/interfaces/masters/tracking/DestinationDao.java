package net.cltech.enterprisent.dao.interfaces.masters.tracking;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.tracking.SampleOportunity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * destinos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/07/2017
 * @see Creación
 */
public interface DestinationDao
{

    /**
     * Lista los destinos desdel base de datos.
     *
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default List<Destination> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab53c1, lab53c2, lab53c3, lab53c4, lab53c5, lab53c6, lab53c7, lab53.lab04c1, lab04c2, lab04c3, lab04c4, lab53.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab53 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab53.lab53c5 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab53.lab04c1", (ResultSet rs, int i) ->
            {
                Destination destination = new Destination();
                destination.setId(rs.getInt("lab53c1"));
                destination.setCode(rs.getString("lab53c2"));
                destination.setName(rs.getString("lab53c3"));
                destination.setDescription(rs.getString("lab53c4"));
                destination.setColor(rs.getString("lab53c6"));
                /*Tipo*/
                destination.getType().setId(rs.getInt("lab80c1"));
                destination.getType().setIdParent(rs.getInt("lab80c2"));
                destination.getType().setCode(rs.getString("lab80c3"));
                destination.getType().setEsCo(rs.getString("lab80c4"));
                destination.getType().setEnUsa(rs.getString("lab80c5"));
                /*Usuario*/
                destination.getUser().setId(rs.getInt("lab04c1"));
                destination.getUser().setName(rs.getString("lab04c2"));
                destination.getUser().setLastName(rs.getString("lab04c3"));
                destination.getUser().setUserName(rs.getString("lab04c4"));

                destination.setLastTransaction(rs.getTimestamp("lab53c7"));
                destination.setState(rs.getInt("lab07c1") == 1);

                return destination;
            });
        } catch (EmptyResultDataAccessException ex)
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
    default Destination create(Destination destination) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab53")
                .usingGeneratedKeyColumns("lab53c1");

        HashMap parameters = new HashMap();
        parameters.put("lab53c2", destination.getCode().trim());
        parameters.put("lab53c3", destination.getName().trim());
        parameters.put("lab53c4", destination.getDescription().trim());
        parameters.put("lab53c5", destination.getType().getId());
        parameters.put("lab53c6", destination.getColor().trim());
        parameters.put("lab53c7", timestamp);
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
    default Destination get(Integer id, String code, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab53c1, lab53c2, lab53c3, lab53c4, lab53c5, lab53c6, lab53c7, lab53.lab04c1, lab04c2, lab04c3, lab04c4, lab53.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab53 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab53.lab53c5 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab53.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab53c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab53c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab53c3) = ? ";
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
                Destination destination = new Destination();
                destination.setId(rs.getInt("lab53c1"));
                destination.setCode(rs.getString("lab53c2"));
                destination.setName(rs.getString("lab53c3"));
                destination.setDescription(rs.getString("lab53c4"));
                destination.setColor(rs.getString("lab53c6"));
                /*Tipo*/
                destination.getType().setId(rs.getInt("lab80c1"));
                destination.getType().setIdParent(rs.getInt("lab80c2"));
                destination.getType().setCode(rs.getString("lab80c3"));
                destination.getType().setEsCo(rs.getString("lab80c4"));
                destination.getType().setEnUsa(rs.getString("lab80c5"));
                /*Usuario*/
                destination.getUser().setId(rs.getInt("lab04c1"));
                destination.getUser().setName(rs.getString("lab04c2"));
                destination.getUser().setLastName(rs.getString("lab04c3"));
                destination.getUser().setUserName(rs.getString("lab04c4"));

                destination.setLastTransaction(rs.getTimestamp("lab53c7"));
                destination.setState(rs.getInt("lab07c1") == 1);

                return destination;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Obtener información de un destino por un campo especifico.
     *
     * @param id ID del destino a ser consultado.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default Destination getNameDestination(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab53c3 FROM lab53 WHERE lab53c1 = ? ";
            
            Object object = null;
            if (id != null)
            {
                object = id;
            }
        

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Destination destination = new Destination();
                destination.setName(rs.getString("lab53c3"));
                return destination;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un destino por id en la ruta.
     *
     * @param id ID del destino en la ruta a ser consultado.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default Destination getByRoute(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab53.lab53c1, lab53c2, lab53c3, lab53c4, lab53c5, lab53c6, lab53c7, lab53.lab04c1, lab04c2, lab04c3, lab04c4, lab53.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab42 "
                    + "INNER JOIN lab53 ON lab53.lab53c1 = lab42.lab53c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab53.lab53c5 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab53.lab04c1 "
                    + "WHERE lab42.lab42c1 = ?";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Destination destination = new Destination();
                destination.setId(rs.getInt("lab53c1"));
                destination.setCode(rs.getString("lab53c2"));
                destination.setName(rs.getString("lab53c3"));
                destination.setDescription(rs.getString("lab53c4"));
                destination.setColor(rs.getString("lab53c6"));
                /*Tipo*/
                destination.getType().setId(rs.getInt("lab80c1"));
                destination.getType().setIdParent(rs.getInt("lab80c2"));
                destination.getType().setCode(rs.getString("lab80c3"));
                destination.getType().setEsCo(rs.getString("lab80c4"));
                destination.getType().setEnUsa(rs.getString("lab80c5"));
                /*Usuario*/
                destination.getUser().setId(rs.getInt("lab04c1"));
                destination.getUser().setName(rs.getString("lab04c2"));
                destination.getUser().setLastName(rs.getString("lab04c3"));
                destination.getUser().setUserName(rs.getString("lab04c4"));

                destination.setLastTransaction(rs.getTimestamp("lab53c7"));
                destination.setState(rs.getInt("lab07c1") == 1);

                return destination;
            });
        } catch (EmptyResultDataAccessException ex)
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
    default Destination update(Destination destination) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab53 SET lab53c2 = ?, lab53c3 = ?, lab53c4 = ?, lab53c5 = ?, lab53c6 = ?, lab53c7 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab53c1 = ?",
                destination.getCode(), destination.getName(), destination.getDescription(), destination.getType().getId(), destination.getColor(), timestamp, destination.getUser().getId(), destination.isState() ? 1 : 0, destination.getId());

        destination.setLastTransaction(timestamp);

        return destination;
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

    /**
     * Lista la ruta asociada a una sede, una muestra y un tipo de orden desde
     * la base de datos.
     *
     * @param idBranch Id de la sede en la que se debe hacer la consulta.
     * @param idSample Id de la muestra en la que se debe hacer la consulta.
     * @param idOrderType Id del tipo de orden en la que se debe hacer la
     * consulta.
     * @param assignment Indica si la consulta trae examenes o servicios.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default AssignmentDestination getAssignment(Integer idBranch, Integer idSample, Integer idOrderType, boolean assignment) throws Exception
    {
        try
        {
            return getConnection().queryForObject(""
                    + "SELECT lab52c1, lab52c2, lab52.lab04c1, lab04c2, lab04c3, lab04c4, lab52.lab07c1 "
                    + "FROM lab52 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab52.lab04c1 "
                    + "WHERE lab52.lab05c1 = ? AND lab52.lab24c1 = ? AND lab52.lab103c1 = ? AND lab52.lab07c1 = 1",
                    new Object[]
                    {
                        idBranch, idSample, idOrderType
                    }, (ResultSet rs, int i) ->
            {
                AssignmentDestination assignmentDestination = new AssignmentDestination();
                assignmentDestination.setId(rs.getInt("lab52c1"));
                /*Usuario*/
                assignmentDestination.getUser().setId(rs.getInt("lab04c1"));
                assignmentDestination.getUser().setName(rs.getString("lab04c2"));
                assignmentDestination.getUser().setLastName(rs.getString("lab04c3"));
                assignmentDestination.getUser().setUserName(rs.getString("lab04c4"));

                assignmentDestination.setLastTransaction(rs.getTimestamp("lab52c2"));
                assignmentDestination.setState(rs.getInt("lab07c1") == 1);

                getRoute(assignmentDestination, assignment, idSample);

                return assignmentDestination;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new AssignmentDestination();
        }
    }

    /**
     * Lista la ruta asociada a una sede, una muestra y un tipo de orden desde
     * la base de datos.
     *
     * @param assignmentDestination Objeto de la asignacion de destinos.
     * @param assignment Indica si la consulta trae examenes o servicios.
     * @param idSample Id de la muestra en la que se debe hacer la consulta.
     */
    default void getRoute(AssignmentDestination assignmentDestination, boolean assignment, int idSample)
    {
        try
        {
            assignmentDestination.setDestinationRoutes(getConnection().query(""
                    + "SELECT lab42.lab42c1, lab42.lab42c2, lab53.lab53c1, lab53c2, lab53c3, lab53c4, lab53c5, lab53c6, lab53c7, lab53.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab42 "
                    + "INNER JOIN lab53 ON lab42.lab53c1 = lab53.lab53c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab53.lab53c5 "
                    + "WHERE lab42.lab52c1 = ?",
                    new Object[]
                    {
                        assignmentDestination.getId()
                    }, (ResultSet rs, int i) ->
            {
                DestinationRoute route = new DestinationRoute();
                route.setId(rs.getInt("lab42c1"));
                route.setOrder(rs.getInt("lab42c2"));
                route.getDestination().setId(rs.getInt("lab53c1"));
                route.getDestination().setCode(rs.getString("lab53c2"));
                route.getDestination().setName(rs.getString("lab53c3"));
                route.getDestination().setDescription(rs.getString("lab53c4"));
                route.getDestination().setColor(rs.getString("lab53c6"));
                /*Tipo*/
                route.getDestination().getType().setId(rs.getInt("lab80c1"));
                route.getDestination().getType().setIdParent(rs.getInt("lab80c2"));
                route.getDestination().getType().setCode(rs.getString("lab80c3"));
                route.getDestination().getType().setEsCo(rs.getString("lab80c4"));
                route.getDestination().getType().setEnUsa(rs.getString("lab80c5"));

                route.getDestination().setLastTransaction(rs.getTimestamp("lab53c7"));
                route.getDestination().setState(rs.getInt("lab07c1") == 1);

                if (assignment)
                {
                    readTests(route, idSample);
                } else
                {
                    readServices(route);
                }

                return route;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            assignmentDestination.setDestinationRoutes(new ArrayList<>(0));
        }
    }

    /**
     * Obtener examenes asociados a un destino.
     *
     * @param destination Instancia con los datos del destino.
     * @param idSample Id de la muestra
     */
    default void readTests(DestinationRoute destination, int idSample)
    {
        try
        {
            destination.setTests(getConnection().query(" SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c37, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab87.lab42c1 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab87 ON lab87.lab39c1 = lab39.lab39c1 AND lab87.lab42c1 = ? "
                    + "WHERE lab39.lab39c37 = 0 AND lab39.lab24c1 = ? ",
                    new Object[]
                    {
                        destination.getId(), idSample
                    }, (ResultSet rs, int i) ->
            {
                TestBasic test = new TestBasic();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setResultType(rs.getShort("lab39c11"));
                test.setTestType(rs.getShort("lab39c37"));
                /*Area*/
                test.getArea().setId(rs.getInt("lab43c1"));
                test.getArea().setAbbreviation(rs.getString("lab43c3"));
                test.getArea().setName(rs.getString("lab43c4"));
                test.setSelected(rs.getString("lab42c1") != null);

                return test;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            destination.setTests(new ArrayList<>());
        }
    }

    /**
     * Obtener servicios asociados a un destino.
     *
     * @param destination Instancia con los datos del destino.
     */
    default void readServices(DestinationRoute destination)
    {
        try
        {
            destination.setSampleOportunitys(getConnection().query(" SELECT lab10.lab10c1, lab10c2, lab10c7, lab10.lab07c1, lab83.lab42c1, lab83c1, lab83c2 "
                    + "FROM lab10 "
                    + "LEFT JOIN lab83 ON lab83.lab10c1 = lab10.lab10c1 AND lab83.lab42c1 = ? ",
                    new Object[]
                    {
                        destination.getId()
                    }, (ResultSet rs, int i) ->
            {
                SampleOportunity oportunity = new SampleOportunity();
                oportunity.getService().setId(rs.getInt("lab10c1"));
                oportunity.getService().setName(rs.getString("lab10c2"));
                oportunity.getService().setCode(rs.getString("lab10c7"));
                oportunity.setExpectedTime(rs.getString("lab83c1") == null ? null : rs.getInt("lab83c1"));
                oportunity.setMaximumTime(rs.getString("lab83c2") == null ? null : rs.getInt("lab83c2"));
                oportunity.setSelected(rs.getString("lab42c1") != null);

                return oportunity;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            destination.setSampleOportunitys(new ArrayList<>());
        }
    }

    /**
     * Registra un nuevo destino en la base de datos.
     *
     * @param assignment Instancia con los datos de la asignacion de destinos.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default AssignmentDestination createAssignment(AssignmentDestination assignment) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab52")
                .usingGeneratedKeyColumns("lab52c1");

        HashMap parameters = new HashMap();
        parameters.put("lab103c1", assignment.getOrderType().getId());
        parameters.put("lab05c1", assignment.getBranch().getId());
        parameters.put("lab24c1", assignment.getSample().getId());
        parameters.put("lab52c2", timestamp);
        parameters.put("lab04c1", assignment.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        assignment.setId(key.intValue());
        assignment.setLastTransaction(timestamp);

        createRoute(assignment);

        return assignment;
    }

    /**
     * Registra una nueva ruta en la base de datos.
     *
     * @param assignment Instancia con los datos de la asignacion de destinos.
     *
     * @throws Exception Error en la base de datos.
     */
    default void createRoute(AssignmentDestination assignment) throws Exception
    {
        for (DestinationRoute route : assignment.getDestinationRoutes())
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab42")
                    .usingGeneratedKeyColumns("lab42c1");

            HashMap parameters = new HashMap();
            parameters.put("lab53c1", route.getDestination().getId());
            parameters.put("lab52c1", assignment.getId());
            parameters.put("lab42c2", route.getOrder());

            Number key = insert.executeAndReturnKey(parameters);
            route.setId(key.intValue());

            createTest(route);
        }
    }

    /**
     * Registra una nueva ruta en la base de datos.
     *
     * @param route Instancia con los datos de la asignacion de destinos.
     *
     * @throws Exception Error en la base de datos.
     */
    default void createTest(DestinationRoute route) throws Exception
    {
        for (TestBasic test : route.getTests())
        {
            if (test.isSelected())
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                        .withTableName("lab87");

                HashMap parameters = new HashMap();
                parameters.put("lab42c1", route.getId());
                parameters.put("lab39c1", test.getId());

                insert.execute(parameters);
            }
        }
    }

    /**
     * Registra una nueva ruta en la base de datos.
     *
     * @param destinations Instancia con los datos de la asignacion de destinos.
     *
     * @throws Exception Error en la base de datos.
     */
    default int createSampleOportunity(List<DestinationRoute> destinations) throws Exception
    {
        int i = 0;
        for (DestinationRoute destination : destinations)
        {
            deleteSampleOportunity(destination.getId());
            for (SampleOportunity sampleOportunity : destination.getSampleOportunitys())
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                        .withTableName("lab83");

                HashMap parameters = new HashMap();
                parameters.put("lab10c1", sampleOportunity.getService().getId());
                parameters.put("lab42c1", destination.getId());
                parameters.put("lab83c1", sampleOportunity.getExpectedTime());
                parameters.put("lab83c2", sampleOportunity.getMaximumTime());

                insert.execute(parameters);
                i++;
            }
        }
        return i;
    }

    /**
     * Desactiva la asociación de destinos en la base de datos.
     *
     * @param idAssignment Id de la asignacion a ser desactivada.
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteAssigment(Integer idAssignment) throws Exception
    {
        getConnection().update("UPDATE lab52 SET lab07c1 = 0 WHERE lab52c1 = ?",
                idAssignment);
    }

    /**
     * Elimina las oportunidades de la muestra asociadas a una ruta de destinos
     * en la base de datos.
     *
     * @param idDestinationRoute Id de la ruta del destino a ser desactivada.
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteSampleOportunity(Integer idDestinationRoute) throws Exception
    {
        getConnection().update("DELETE FROM lab83 WHERE lab42c1 = ?",
                idDestinationRoute);
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

}
