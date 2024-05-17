package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.integration.middleware.DemographicMiddleware;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.AlarmDays;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Demograficos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2017
 * @see Creación
 */
public interface DemographicDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab62c5"
                    + ", lab62c6"
                    + ", lab62c7"
                    + ", lab62c8"
                    + ", lab62c9"
                    + ", lab62c10"
                    + ", lab62c11"
                    + ", lab62c12"
                    + ", lab62c15"
                    + ", lab62.lab07c1"
                    + ", lab62.lab04c1"
                    + ", lab195c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab62c13"
                    + ", lab62c14"
                    + ", lab63c1"
                    + " FROM lab62 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab62.lab04c1 "
                    + "LEFT JOIN lab195 ON lab195.lab62c1 = lab62.lab62c1", (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrdering(rs.getShort("lab62c6"));
                demographic.setOrderingDemo(rs.getInt("lab195c1"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setPromiseTime(rs.getShort("lab62c15") == 1);
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);

                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));

                demographic.setState(rs.getInt("lab07c1") == 1);
                demographic.setModify(rs.getInt("lab62c13") == 1);
                demographic.setDefaultValueRequired(rs.getString("lab62c14"));
                demographic.setDemographicItem(rs.getInt("lab63c1"));

                demographic.setItem(null);
                demographic.setValue(null);
                demographic.setCode(null);
                demographic.setSource(rs.getString("lab62c3"));
                demographic.setType(rs.getInt("lab62c4"));
                demographic.setCoded(rs.getBoolean("lab62c4"));
                return demographic;
            });
        } catch (Exception ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> listOderingH() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab62c5"
                    + ", lab62c6"
                    + ", lab62c7"
                    + ", lab62c8"
                    + ", lab62c9"
                    + ", lab62c10"
                    + ", lab62c11"
                    + ", lab62c12"
                    + ", lab62c14"
                    + " FROM lab62 "
                    + "WHERE lab07c1 = 1 AND lab62c3 = 'H' ", (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrderingDemo(rs.getInt("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);
                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                demographic.setDefaultValueRequired(rs.getString("lab62c14"));

                return demographic;
            });
        } catch (Exception ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> listOderingO() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab62c5"
                    + ", lab62c6"
                    + ", lab62c7"
                    + ", lab62c8"
                    + ", lab62c9"
                    + ", lab62c10"
                    + ", lab62c11"
                    + ", lab62c12"
                    + ", lab62c14"
                    + " FROM lab62 "
                    + "WHERE lab07c1 = 1 AND lab62c3 = 'O' ", (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrderingDemo(rs.getInt("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);
                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                demographic.setDefaultValueRequired(rs.getString("lab62c14"));

                return demographic;
            });
        } catch (Exception ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> listOderingfixed() throws Exception
    {
        try
        {
            String query = "SELECT lab62c1"
                    + ", lab195c1"
                    + " FROM lab195";

            return getJdbcTemplate().query(query, (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setOrdering(rs.getShort("lab195c1"));
                return demographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @param filterdemographics
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> getDemographicIds(List<Integer> filterdemographics) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab63c1"
                    + " FROM lab62 "
                    + " WHERE lab07c1 = 1 AND lab62c1 in (" + filterdemographics.stream().map(d -> d.toString()).collect(Collectors.joining(",")) + ")", (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setDemographicItem(rs.getInt("lab63c1"));
                demographic.setItem(null);
                demographic.setValue(null);
                demographic.setCode(null);
                demographic.setSource(rs.getString("lab62c3"));
                demographic.setType(rs.getInt("lab62c4"));
                demographic.setCoded(rs.getBoolean("lab62c4"));
                return demographic;
            });
        } catch (Exception ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo demografico en la base de datos.
     *
     * @param demographic Instancia con los datos del demografico.
     *
     * @return Instancia con los datos del demografico.
     * @throws Exception Error en la base de datos.
     */
    public Demographic create(Demographic demographic) throws Exception;

    /**
     * Obtener información de un demografico por un campo especifico.
     *
     * @param id ID del demografico a ser consultado.
     * @param name Nombre del demografico a ser consultado.
     * @param sort Ordenamiento
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Demographic get(Integer id, String name, Integer sort) throws Exception;

    /**
     * Obtener información de un demografico por un campo especifico.
     *
     * @param id ID del demografico a ser consultado.
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Demographic getOrdening(Integer id) throws Exception;

    /**
     * Obtener información de un demografico por un campo especifico.
     *
     * @param id ID del demografico a ser consultado.
     * @param name Nombre del demografico a ser consultado.
     * @param sort Ordenamiento
     * @param type Tipo de demografico por orden O y por historia H
     *
     * @return Instancia con los datos del área.
     */
    default Demographic get(Integer id, String name, Integer sort, String type)
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab62c1, lab62c2, lab62c3, lab62c4, lab62c5, lab62c6, lab62c7, lab62c8, lab62c9, lab62c10, lab62c11, lab62c12, lab62.lab07c1, lab62.lab04c1, lab04c2, lab04c3, lab04c4, lab62c13, lab63c1 "
                    + "FROM lab62 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab62.lab04c1 ";
            /*Where*/
            List<Object> params = new ArrayList<>();
            if (id != null)
            {
                query += query + "WHERE lab62c1 = ? ";
                params.add(id);
            }
            if (name != null)
            {
                query += !query.contains("WHERE") ? "WHERE UPPER(lab62c2) = ? " : " AND UPPER(lab62c2) = ? ";
                params.add(name);
            }
            if (sort != null)
            {
                query += !query.contains("WHERE") ? "WHERE lab62c6 = ? " : " AND lab62c6 = ? ";
                params.add(sort);
            }
            if (type != null)
            {
                query += !query.contains("WHERE") ? "WHERE lab62c3 = ? " : " AND lab62c3 = ? ";
                params.add(type);
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";
            return getJdbcTemplate().queryForObject(query, params.toArray(), (ResultSet rs, int i)
                    ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrdering(rs.getShort("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);

                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));
                demographic.setState(rs.getInt("lab07c1") == 1);
                demographic.setModify(rs.getInt("lab62c13") == 1);
                demographic.setDemographicItem(rs.getInt("lab63c1"));

                return demographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un demografico en la base de datos.
     *
     * @param demographic Instancia con los datos del demografico.
     *
     * @return Objeto del demografico modificado.
     * @throws Exception Error en la base de datos.
     */
    public Demographic update(Demographic demographic) throws Exception;

    /**
     *
     * Elimina un demografico de la base de datos.
     *
     * @param id ID del demografico.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Inserta los examenes relacionados al demografico
     *
     * @param excludeList lista de examenes
     *
     * @return registros insertados
     */
    default int insertDemographicTest(List<ExcludeTest> excludeList)
    {
        List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab104");
        excludeList.stream().map((demographic)
                ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab39c1", demographic.getTest().getId());
            parameters.put("lab104c1", demographic.getDemographicItem());
            parameters.put("lab104c2", demographic.getId());
            parameters.put("lab104c3", timestamp);
            parameters.put("lab04c1", demographic.getUser().getId());
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });
        return insert.executeBatch(batchArray.toArray(new HashMap[excludeList.size()])).length;
    }

    /**
     * Elimina examenes relacionados con demografico item
     *
     * @param id id demográfico
     * @param idItem id demografico item
     *
     * @return registros eliminados
     */
    default int deleteDemographicTest(Integer id, Integer idItem)
    {
        return getJdbcTemplate().update("DELETE FROM lab104 WHERE lab104c1 = ? AND lab104c2 = ?", idItem, id);
    }

    /**
     * lista examenes relacionados al demografico item
     *
     * @param id id demográfico
     * @param idItem id demografico item
     *
     * @return lista de examenes
     */
    default List<ExcludeTest> listDemographicTest(Integer id, Integer idItem)
    {
        try
        {
            String query = "SELECT  lab39.lab39c1,lab39c2,lab39c3,lab39c4, lab104c1 "
                    + " , lab104.lab04c1, lab04c2, lab04c3, lab04c4, lab104c3 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab104 ON (lab39.lab39c1 = lab104.lab39c1 AND lab104c1 = ? AND lab104c2 = ? ) "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab104.lab04c1 "
                    + "WHERE lab39.lab07c1 = 1";

            return getJdbcTemplate().query(query, (ResultSet rs, int i)
                    ->
            {
                ExcludeTest bean = new ExcludeTest();
                bean.getTest().setSelected(rs.getString("lab104c1") != null);
                bean.getTest().setAbbr(rs.getString("lab39c3"));
                bean.getTest().setName(rs.getString("lab39c4"));
                bean.getTest().setCode(rs.getString("lab39c2"));
                bean.getTest().setId(rs.getInt("lab39c1"));
                bean.setId(id);

                bean.setLastTransaction(rs.getTimestamp("lab104c3"));
                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                bean.setDemographicItem(idItem);
                return bean;
            }, idItem, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Inserta los examenes relacionados con días alarma
     *
     * @param alarmDays lista de examenes con días alarma
     *
     * @return registros insertados
     */
    default int insertAlarmDaysTest(AlarmDays alarmDays)
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab178");
        for (TestBasic test : alarmDays.getTest())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab39c1", test.getId());
            parameters.put("lab178c1", alarmDays.getDemographic().getId());
            parameters.put("lab178c2", test.getDeltacheckDays());
            parameters.put("lab178c3", alarmDays.getDemographic().getDemographicItem());
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[alarmDays.getTest().size()]));

        return inserted.length;
    }

    /**
     * Elimina examenes relacionados con días alarma
     *
     * @param idDemographic id demográfico
     * @param demographicItem id demografico item
     *
     * @return registros eliminados
     */
    default int deleteAlarmDaysTest(Integer idDemographic, Integer demographicItem)
    {
        String deleteSql = "DELETE FROM lab178 WHERE lab178c1 = " + idDemographic + " AND lab178c3=" + demographicItem;
        return getJdbcTemplate().update(deleteSql);
    }

    /**
     * lista examenes relacionados a días alarma
     *
     * @param idDemographic id demográfico
     * @param demographicItem id demografico item
     *
     * @return lista de examenes
     */
    default List<TestBasic> listAlarmDaysTest(Integer idDemographic, Integer demographicItem) throws Exception
    {
        try
        {
            String query = "SELECT lab39.lab39c1,lab39c2,lab39c3,lab39c4, lab178c1, lab178c2, lab178c3, lab43c1"
                    + " FROM lab39"
                    + " LEFT JOIN lab178 ON lab178.lab39c1 = lab39.lab39c1 AND lab178.lab178c1 = " + idDemographic + " AND lab178.lab178c3 = " + demographicItem
                    + " WHERE lab39.lab07c1 = 1"
                    + " ORDER BY lab178.lab178c2 DESC";

            return getJdbcTemplate().query(query, (ResultSet rs, int i)
                    ->
            {
                TestBasic bean = new TestBasic();
                Area area = new Area();
                bean.setDeltacheckDays(rs.getInt("lab178c2"));
                bean.setName(rs.getString("lab39c4"));
                bean.setCode(rs.getString("lab39c2"));
                bean.setId(rs.getInt("lab39c1"));
                area.setId(rs.getInt("lab43c1"));
                bean.setArea(area);

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualizar el ordenamiento de una lista de demograficos
     *
     * @param demographic
     * @return
     */
    default Demographic updateOrder(Demographic demographic)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        getJdbcTemplate().update("UPDATE lab62 SET lab62c6 = ?, lab62c12 = ?, lab04c1 = ? "
                + "WHERE lab62c1 = ?",
                demographic.getOrdering(), timestamp, demographic.getUser().getId(), demographic.getId());

        demographic.setLastTransaction(timestamp);
        return demographic;
    }

    /**
     * Actualizar el ordenamiento de una lista de demograficos
     *
     * @param demographic
     * @return
     */
    default Demographic updateOrdering(Demographic demographic)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        getJdbcTemplate().update("UPDATE lab195 SET lab195c1 = ?, lab195c2 = ?, lab04c1 = ? "
                + "WHERE lab62c1 = ?",
                demographic.getOrderingDemo(), timestamp, demographic.getUser().getId(), demographic.getId());

        demographic.setLastTransaction(timestamp);
        return demographic;
    }

    /**
     * Actualizar el ordenamiento de una lista de demograficos
     *
     * @param demographic
     * @return
     */
    default Demographic newOrdering(Demographic demographic)
    {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab195")
                .usingColumns("lab62c1", "lab195c1", "lab195c2", "lab04c1");

        HashMap parameters = new HashMap();
        parameters.put("lab62c1", demographic.getId());
        parameters.put("lab195c1", demographic.getOrderingDemo());
        parameters.put("lab195c2", timestamp);
        parameters.put("lab04c1", demographic.getUser().getId());
        insert.execute(parameters);
        return demographic;
    }

    /**
     * Lista demográficos al Middleware
     *
     * @return Para la importacion de demográficos al middleware
     * @throws Exception Error en base de datos
     */
    default List<DemographicMiddleware> listMiddleware() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62.lab62c2"
                    + ", lab62.lab62c3"
                    + ", lab62.lab62c4"
                    + ", lab62.lab07c1"
                    + "  FROM lab62", new RowMapper<DemographicMiddleware>()
            {
                @Override
                public DemographicMiddleware mapRow(ResultSet rs, int i) throws SQLException
                {
                    DemographicMiddleware demographic = new DemographicMiddleware();

                    demographic.setId(rs.getInt("lab62c1"));
                    demographic.setName(rs.getString("lab62c2"));
                    demographic.setSource(rs.getString("lab62c3"));
                    demographic.setCodified(rs.getBoolean("lab62c4"));
                    demographic.setActive(rs.getBoolean("lab07c1"));

                    return demographic;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene registros de un demografico con su item correspondiente de la
     * tabla de encriptación de resultados
     *
     * @param demographics
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    default List<DemographicReportEncryption> getDemographicsReportEncrypt(List<DemographicReportEncryption> demographics) throws Exception
    {
        List<DemographicReportEncryption> mastersList = new ArrayList<>();
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab185c1, lab185c2, lab185c3 ")
                    .append("FROM lab185 ")
                    .append("WHERE lab185c1 = ?");

            int idLastDemo = 0;

            for (DemographicReportEncryption demographic : demographics)
            {
                // Verificamos que los id de demograficos sean diferentes
                // para poder obtener una sola lista de items pertenecientes a este demografico
                if (idLastDemo != demographic.getIdDemographic())
                {
                    idLastDemo = demographic.getIdDemographic();
                    mastersList.addAll(getJdbcTemplate().query(query.toString(),
                            (ResultSet rs, int i)
                            ->
                    {
                        DemographicReportEncryption obj = new DemographicReportEncryption();
                        obj.setIdDemographic(rs.getInt("lab185c1"));
                        obj.setIdDemographicItem(rs.getInt("lab185c2"));
                        obj.setEncryption(rs.getInt("lab185c3"));
                        return obj;
                    }, demographic.getIdDemographic()));
                }
            }

            return mastersList;
        } catch (Exception e)
        {
            return mastersList;
        }
    }

    /**
     * Elimina el registro de un demografico con su item correspondiente de la
     * tabla de encriptación de resultados
     *
     * @param demographics
     * @return 1 - si todos los registros se eliminaron satisfactoriamente, -1 -
     * si algun registro no pudo ser eliminado
     * @throws Exception Error en base de datos
     */
    default int deleteDemographicsReportEncrypt(List<DemographicReportEncryption> demographics) throws Exception
    {
        try
        {
            int idLastDemo = 0;
            for (DemographicReportEncryption demographic : demographics)
            {
                if (idLastDemo != demographic.getIdDemographic())
                {
                    idLastDemo = demographic.getIdDemographic();
                    getJdbcTemplate().update("DELETE FROM lab185 "
                            + " WHERE lab185c1 = ? ",
                            demographic.getIdDemographic());
                }
            };

            return 1;
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Inserta un demografico con su item correspondiente en la tabla de
     * encriptación de resultados
     *
     * @param demographics
     * @return 1 - si todos los campos se registraron satisfactoriamente, -1 -
     * si algun campo no pudo ser registrado
     * @throws Exception Error en base de datos
     */
    default int createDemographicsReportEncrypt(List<DemographicReportEncryption> demographics) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab185");
            demographics.stream().map((demographic)
                    ->
            {
                HashMap parameters = new HashMap();
                parameters.put("lab185c1", demographic.getIdDemographic());
                parameters.put("lab185c2", demographic.getIdDemographicItem());
                parameters.put("lab185c3", demographic.getEncryption());
                return parameters;
            }).forEachOrdered((parameters)
                    ->
            {
                batchArray.add(parameters);
            });

            insert.executeBatch(batchArray.toArray(new HashMap[demographics.size()]));
            return 1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    /**
     * Obtiene registros de un demografico dinamicos con respecto a la tabla de
     * encriptación de resultados
     *
     * @param idDemographic
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    default List<DemographicReportEncryption> getDemographicsReportEncryptById(int idDemographic) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Demo.lab62c1 AS lab62c1, ")
                    .append("Item.lab63c1 AS lab63c1, ")
                    .append("RE.lab185c2 AS lab185c2, ")
                    .append("RE.lab185c3 AS lab185c3, ")
                    .append("Item.lab63c2 AS lab63c2, ")
                    .append("Item.lab63c3 AS lab63c3 ")
                    .append("FROM lab63 Item ")
                    .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab63c1 ")
                    .append("JOIN lab62 Demo ON Demo.lab62c1 = Item.lab62c1 ")
                    .append("WHERE Item.lab07c1 = 1 AND Item.lab62c1 = ").append(idDemographic);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicReportEncryption demo = new DemographicReportEncryption();
                demo.setIdDemographic(rs.getInt("lab62c1"));
                demo.setIdDemographicItem(rs.getInt("lab63c1"));
                demo.setEncryption(rs.getObject("lab185c3") == null ? 0 : rs.getInt("lab185c3"));
                demo.setSelected(rs.getObject("lab185c2") != null);
                demo.setCode(rs.getString("lab63c2"));
                demo.setName(rs.getString("lab63c3"));
                return demo;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene registros de un demografico fijo con todos sus items con respecto
     * a la tabla de encriptación de resultados
     *
     * @param idDemographic
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    default List<DemographicReportEncryption> demographicsPermanentReportEncryptById(int idDemographic) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            StringBuilder code = new StringBuilder();
            StringBuilder name = new StringBuilder();
            StringBuilder idDemoItem = new StringBuilder();

            query.append("SELECT RE.lab185c1 AS lab185c1, ")
                    .append("RE.lab185c2 AS lab185c2, ")
                    .append("RE.lab185c3 AS lab185c3, ");

            if (idDemographic == Constants.ACCOUNT)
            {
                query.append("Item.lab14c1 AS lab14c1, ")
                        .append("Item.lab14c3 AS lab14c3, ")
                        .append("Item.lab14c13 AS lab14c13 ")
                        .append("FROM lab14 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab14c1 ");

                // Asignacion de nombres de columnas codigo y nombre del item del demografico
                name.append("lab14c3");
                code.append("lab14c13");
                idDemoItem.append("lab14c1");
            }

            if (idDemographic == Constants.PHYSICIAN)
            {
                query.append("Item.lab19c1 AS lab19c1, ")
                        .append("Item.lab19c2 AS lab19c2, ")
                        .append("Item.lab19c22 AS lab19c22 ")
                        .append("FROM lab19 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab19c1 ");

                // Asignacion de nombres de columnas
                name.append("lab19c2");
                code.append("lab19c22");
                idDemoItem.append("lab19c1");
            }

            if (idDemographic == Constants.RATE)
            {
                query.append("Item.lab904c1 AS lab904c1, ")
                        .append("Item.lab904c2 AS lab904c2, ")
                        .append("Item.lab904c3 AS lab904c3 ")
                        .append("FROM lab904 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab904c1 ");

                // Asignacion de nombres de columnas
                name.append("lab904c3");
                code.append("lab904c2");
                idDemoItem.append("lab904c1");
            }

            if (idDemographic == Constants.ORDERTYPE)
            {
                query.append("Item.lab103c1 AS lab103c1, ")
                        .append("Item.lab103c2 AS lab103c2, ")
                        .append("Item.lab103c3 AS lab103c3 ")
                        .append("FROM lab103 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab103c1 ");

                // Asignacion de nombres de columnas
                name.append("lab103c3");
                code.append("lab103c2");
                idDemoItem.append("lab103c1");
            }

            if (idDemographic == Constants.BRANCH)
            {
                query.append("Item.lab05c1 AS lab05c1, ")
                        .append("Item.lab05c4 AS lab05c4, ")
                        .append("Item.lab05c10 AS lab05c10 ")
                        .append("FROM lab05 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab05c1 ");

                // Asignacion de nombres de columnas
                name.append("lab05c4");
                code.append("lab05c10");
                idDemoItem.append("lab05c1");
            }

            if (idDemographic == Constants.SERVICE)
            {
                query.append("Item.lab10c1 AS lab10c1, ")
                        .append("Item.lab10c2 AS lab10c2, ")
                        .append("Item.lab10c7 AS lab10c7 ")
                        .append("FROM lab10 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab10c1 ");

                // Asignacion de nombres de columnas
                name.append("lab10c2");
                code.append("lab10c7");
                idDemoItem.append("lab10c1");
            }

            if (idDemographic == Constants.RACE)
            {
                query.append("Item.lab08c1 AS lab08c1, ")
                        .append("Item.lab08c2 AS lab08c2, ")
                        .append("Item.lab08c5 AS lab08c5 ")
                        .append("FROM lab08 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab08c1 ");

                // Asignacion de nombres de columnas
                name.append("lab08c2");
                code.append("lab08c5");
                idDemoItem.append("lab08c1");
            }

            if (idDemographic == Constants.DOCUMENT_TYPE)
            {
                query.append("Item.lab54c1 AS lab54c1, ")
                        .append("Item.lab54c2 AS lab54c2, ")
                        .append("Item.lab54c3 AS lab54c3 ")
                        .append("FROM lab54 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab54c1 ");

                // Asignacion de nombres de columnas
                name.append("lab54c3");
                code.append("lab54c2");
                idDemoItem.append("lab54c1");
            }

            if (idDemographic == Constants.AGE_GROUP)
            {
                query.append("Item.lab13c1 AS lab13c1, ")
                        .append("Item.lab13c2 AS lab13c2, ")
                        .append("Item.lab13c3 AS lab13c3 ")
                        .append("FROM lab13 Item ")
                        .append("LEFT JOIN lab185 RE ON RE.lab185c2 = Item.lab13c1 ");

                // Asignacion de nombres de columnas
                name.append("lab13c3");
                code.append("lab13c2");
                idDemoItem.append("lab13c1");
            }

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicReportEncryption demo = new DemographicReportEncryption();
                demo.setIdDemographic(idDemographic);
                demo.setIdDemographicItem(rs.getInt(idDemoItem.toString()));
                demo.setEncryption(rs.getObject("lab185c3") == null ? 0 : rs.getInt("lab185c3"));
                demo.setSelected(rs.getObject("lab185c2") != null);
                demo.setCode(rs.getString(code.toString()));
                demo.setName(rs.getString(name.toString()));
                return demo;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene registros de un demografico con su item correspondiente de la
     * tabla de encriptación de resultados
     *
     * @param idDemographic
     * @param demographicItem
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    default List<DemographicReportEncryption> getDemographicByIdAndDemographicitem(int idDemographic, int demographicItem) throws Exception
    {
        List<DemographicReportEncryption> mastersList = new ArrayList<>();
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab185c1, lab185c2, lab185c3 ")
                    .append("FROM lab185 ")
                    .append("WHERE lab185c1 = ? AND lab185c2 = ?");

            mastersList.add(getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicReportEncryption obj = new DemographicReportEncryption();
                obj.setIdDemographic(rs.getInt("lab185c1"));
                obj.setIdDemographicItem(rs.getInt("lab185c2"));
                obj.setEncryption(rs.getInt("lab185c3"));
                return obj;
            }, idDemographic, demographicItem));

            return mastersList;
        } catch (DataAccessException e)
        {
            return mastersList;
        }
    }

    /**
     * Obtiene el valor de cualquier demográfico sea de la orden o del paciente:
     *
     * @param origin
     * @param column
     * @param orderOrPatientId
     * @return Valor del demográfico
     * @throws Exception Error en base de datos
     */
    default String getValueOfAnyDemographic(String origin, String column, long orderOrPatientId)
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ").append(column).append(" AS demographicValue");

            if (origin.equalsIgnoreCase("o"))
            {
                query.append(" FROM lab22 ")
                        .append("WHERE lab22c1 = ").append(orderOrPatientId);
            } else
            {
                query.append(" FROM lab21 ")
                        .append("WHERE lab21c1 = ").append(orderOrPatientId);
            }

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getString("demographicValue");
            });
        } catch (DataAccessException e)
        {
            return "Erro dentro de la consulta";
        }
    }
}
