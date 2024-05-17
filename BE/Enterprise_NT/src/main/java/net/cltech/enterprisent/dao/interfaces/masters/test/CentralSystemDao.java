package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.domain.masters.test.HomologationCode;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.user.StandardizationUser;
import net.cltech.enterprisent.domain.masters.user.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Sistemas Centrales.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
public interface CentralSystemDao
{

    /**
     * Lista los sistemas centrales desde la base de datos.
     *
     * @return Lista de sistemas centrales.
     * @throws Exception Error en la base de datos.
     */
    default List<CentralSystem> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab118c1, lab118c2, lab118c3, lab118c4,lab118c5, lab118.lab04c1, lab04c2, lab04c3, lab04c4, lab118.lab07c1 "
                    + "FROM lab118 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab118.lab04c1", (ResultSet rs, int i) ->
            {
                CentralSystem centralSystem = new CentralSystem();
                centralSystem.setId(rs.getInt("lab118c1"));
                centralSystem.setName(rs.getString("lab118c2"));
                centralSystem.setEHR(rs.getInt("lab118c3") == 1);
                centralSystem.setRepeatCode(rs.getInt("lab118c5") == 1);
                /*Usuario*/
                centralSystem.getUser().setId(rs.getInt("lab04c1"));
                centralSystem.getUser().setName(rs.getString("lab04c2"));
                centralSystem.getUser().setLastName(rs.getString("lab04c3"));
                centralSystem.getUser().setUserName(rs.getString("lab04c4"));

                centralSystem.setLastTransaction(rs.getTimestamp("lab118c4"));
                centralSystem.setState(rs.getInt("lab07c1") == 1);

                return centralSystem;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva Sistema Central en la base de datos.
     *
     * @param centralSystem Instancia con los datos del Sistema Central.
     *
     * @return Instancia con los datos del Sistema Central.
     * @throws Exception Error en la base de datos.
     */
    default CentralSystem create(CentralSystem centralSystem) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab118")
                .usingGeneratedKeyColumns("lab118c1");

        HashMap parameters = new HashMap();
        parameters.put("lab118c2", centralSystem.getName().trim());
        parameters.put("lab118c3", centralSystem.isEHR() ? 1 : 0);
        parameters.put("lab118c4", timestamp);
        parameters.put("lab118c5", centralSystem.isRepeatCode());
        parameters.put("lab04c1", centralSystem.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        centralSystem.setId(key.intValue());
        centralSystem.setLastTransaction(timestamp);

        return centralSystem;
    }

    /**
     * Obtener información de un Sistema Central por un campo especifico.
     *
     * @param id ID del Sistema Central a ser consultada.
     * @param name Nombre del Sistema Central a ser consultada.
     *
     * @return Instancia con los datos del Sistema Central.
     * @throws Exception Error en la base de datos.
     */
    default CentralSystem get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab118c1, lab118c2, lab118c3, lab118c4, lab118c5, lab118.lab04c1, lab04c2, lab04c3, lab04c4, lab118.lab07c1 "
                    + "FROM lab118 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab118.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab118c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab118c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                CentralSystem centralSystem = new CentralSystem();
                centralSystem.setId(rs.getInt("lab118c1"));
                centralSystem.setName(rs.getString("lab118c2"));
                centralSystem.setEHR(rs.getInt("lab118c3") == 1);
                centralSystem.setRepeatCode(rs.getInt("lab118c5") == 1);
                /*Usuario*/
                centralSystem.getUser().setId(rs.getInt("lab04c1"));
                centralSystem.getUser().setName(rs.getString("lab04c2"));
                centralSystem.getUser().setLastName(rs.getString("lab04c3"));
                centralSystem.getUser().setUserName(rs.getString("lab04c4"));

                centralSystem.setLastTransaction(rs.getTimestamp("lab118c4"));
                centralSystem.setState(rs.getInt("lab07c1") == 1);

                return centralSystem;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un Sistema Central en la base de datos.
     *
     * @param centralSystem Instancia con los datos del Sistema Central.
     *
     * @return Objeto del Sistema Central modificada.
     * @throws Exception Error en la base de datos.
     */
    default CentralSystem update(CentralSystem centralSystem) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab118 SET lab118c2 = ?, lab118c3 = ?, lab118c4 = ?, lab118c5 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab118c1 = ?",
                centralSystem.getName(), centralSystem.isEHR() ? 1 : 0, timestamp, centralSystem.isRepeatCode() ? 1 : 0, centralSystem.getUser().getId(), centralSystem.isState() ? 1 : 0, centralSystem.getId());

        centralSystem.setLastTransaction(timestamp);

        return centralSystem;
    }

    /**
     * Obtiene listado de examenes para la homologación
     *
     * @param centralSystem id del sistema central
     *
     * @return lista de Standardization
     */
    default List<Standardization> standardizationList(int centralSystem)
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c36, lab39.lab07c1, lab39.lab43c1, lab39c37, lab39c42, lab39c43, lab39c31, "
                    + " lab39c44, lab39c45, lab39c46, lab61.lab61c1 , lab61.lab118c1 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab61 ON lab61.lab39c1 = lab39.lab39c1 AND lab61.lab118c1 = ? "
                    + "WHERE lab39.lab07c1 = 1 ", (ResultSet rs, int i) ->
            {
                Standardization testBasic = new Standardization();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.getCentralSystem().setId(rs.getInt("lab118c1"));
                testBasic.getCodes().add(rs.getString("lab61c1"));
                return testBasic;
            }, centralSystem);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Insertas la homologacion de examenes
     *
     * @param standardization bean con datos a insertar
     *
     * @return informacion de datos insertados
     * @throws Exception Error en la base de datos.
     */
    default Standardization insertStandardization(Standardization standardization) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab61");
        deleteStandardization(standardization);
        for (String code : standardization.getCodes())
        {

            HashMap parameters = new HashMap();
            parameters.put("lab61c1", code);
            parameters.put("lab118c1", standardization.getCentralSystem().getId());
            parameters.put("lab39c1", standardization.getId());
            batchArray.add(parameters);
        }
        insert.executeBatch(batchArray.toArray(new HashMap[standardization.getCodes().size()]));
        return standardization;
    }

    /**
     * Elimina la homologacion de un examen con un sistema central
     *
     * @param standardization
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteStandardization(Standardization standardization) throws Exception
    {
        String query = " DELETE FROM lab61 WHERE lab39c1 = ? AND lab118c1 = ? ";
        getJdbcTemplate().update(query,
                standardization.getId(),
                standardization.getCentralSystem().getId());
    }

    /**
     * Consulta los demograficos item por sistema central y demografico.
     *
     * @param idCentralSystem Id sistema central.
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    default List<StandardizationDemographic> demographicsItemList(int idCentralSystem, int idDemographic) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab118c1, lab117c1, lab117c2, lab117c3 "
                    + "FROM lab117 "
                    + "WHERE lab118c1 = ? AND lab117c1 = ?",
                    new Object[]
                    {
                        idCentralSystem, idDemographic
                    }, (ResultSet rs, int i) ->
            {
                StandardizationDemographic standardizationDemographic = new StandardizationDemographic();
                standardizationDemographic.setId(rs.getInt("lab118c1"));
                standardizationDemographic.getDemographic().setId(rs.getInt("lab117c1"));
                standardizationDemographic.getDemographicItem().setId(rs.getInt("lab117c2"));
                standardizationDemographic.getCentralCode().add(rs.getString("lab117c3"));

                return standardizationDemographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Insertas la homologacion de demograficos
     *
     * @param demographic bean con datos a insertar
     *
     * @return informacion de datos insertados
     * @throws Exception Error en la base de datos.
     */
    default StandardizationDemographic insertStandardizationDemographic(StandardizationDemographic demographic) throws Exception
    {
        deleteStandardizationDemographics(demographic);

        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab117");
        for (String code : demographic.getCentralCode())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab118c1", demographic.getId());
            parameters.put("lab117c1", demographic.getDemographic().getId());
            parameters.put("lab117c2", demographic.getDemographicItem().getId());
            parameters.put("lab117c3", code);
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[demographic.getCentralCode().size()]));
        return demographic;
    }

    /**
     * Elimina la homologacion de un demografico con un sistema central
     *
     * @param demographic Objeto Homologación de Demograficos
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteStandardizationDemographics(StandardizationDemographic demographic) throws Exception
    {
        String query = " DELETE FROM lab117 WHERE lab118c1 = ? AND lab117c1 = ? AND lab117c2 = ? ";
        getJdbcTemplate().update(query,
                demographic.getId(),
                demographic.getDemographic().getId(),
                demographic.getDemographicItem().getId());
    }

    /**
     * Registra una Lista de diagnosticos en la base de datos
     *
     * @param demographics Instancia con los datos de la homologación de
     * demograficos.
     *
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    default int createAllStandardizationDemographics(List<StandardizationDemographic> demographics) throws Exception
    {
        int i = 0;
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab117");
        for (StandardizationDemographic demographic : demographics)
        {
            for (String code : demographic.getCentralCode())
            {
                HashMap parameters = new HashMap();
                parameters.put("lab118c1", demographic.getId());
                parameters.put("lab117c1", demographic.getDemographic().getId());
                parameters.put("lab117c2", demographic.getDemographicItem().getId());
                parameters.put("lab117c3", code);
                batchArray.add(parameters);
                i++;
            }
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[i]));

        return inserted.length;
    }

    /**
     * Consulta los usuarios por sistema central.
     *
     * @param idCentralSystem Id sistema central.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    default List<StandardizationUser> usersList(int idCentralSystem) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab96.lab118c1, lab96.lab96c1 "
                    + "FROM lab04 "
                    + "LEFT JOIN lab96 ON lab96.lab04c1 = lab04.lab04c1 AND lab96.lab118c1 = ?",
                    new Object[]
                    {
                        idCentralSystem
                    }, (ResultSet rs, int i) ->
            {
                StandardizationUser standardizationUser = new StandardizationUser();
                standardizationUser.setId(idCentralSystem);
                standardizationUser.getUserStandardization().setId(rs.getInt("lab04c1"));
                standardizationUser.getUserStandardization().setName(rs.getString("lab04c2"));
                standardizationUser.getUserStandardization().setLastName(rs.getString("lab04c3"));
                standardizationUser.getUserStandardization().setUserName(rs.getString("lab04c4"));
                standardizationUser.setCentralCode(rs.getString("lab96c1"));

                return standardizationUser;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta los usuarios por sistema central.
     *
     * @param idCentralSystem Id sistema central.
     * @param idUser
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    default StandardizationUser getStandardizationUser(int idCentralSystem, int idUser) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab96.lab04c1, lab96.lab118c1, lab118c2, lab96c1, lab04.lab04c2, lab04.lab04c3 "
                    + "FROM lab96 "
                    + "LEFT JOIN lab04 ON lab96.lab04c1 = lab04.lab04c1 "
                    + "LEFT JOIN lab118 ON lab96.lab118c1 = lab118.lab118c1 "
                    + "WHERE lab96.lab04c1 = ? AND lab96.lab118c1 = ? ",
                    new Object[]
                    {
                        idUser, idCentralSystem
                    }, (ResultSet rs, int i) ->
            {
                StandardizationUser standardizationUser = new StandardizationUser();
                standardizationUser.setId(rs.getInt("lab118c1"));
                standardizationUser.setName(rs.getString("lab118c2"));
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2") == null ? "" : rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3") == null ? "" : rs.getString("lab04c3"));
                standardizationUser.setUserStandardization(user);
                standardizationUser.setCentralCode(rs.getString("lab96c1"));
                return standardizationUser;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Insertas la homologacion de usuarios
     *
     * @param user bean con datos a insertar
     *
     * @return informacion de datos insertados
     * @throws Exception Error en la base de datos.
     */
    default StandardizationUser insertStandardizationUser(StandardizationUser user) throws Exception
    {
        deleteStandardizationUser(user);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab96");

        HashMap parameters = new HashMap();
        parameters.put("lab118c1", user.getId());
        parameters.put("lab04c1", user.getUserStandardization().getId());
        parameters.put("lab96c1", user.getCentralCode());

        insert.execute(parameters);
        return user;
    }

    /**
     * Elimina la homologacion de un usuario con un sistema central
     *
     * @param user Objeto Homologación de Demograficos
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteStandardizationUser(StandardizationUser user) throws Exception
    {
        getJdbcTemplate().update("DELETE FROM lab96 WHERE lab118c1 = ? AND lab04c1 = ? ", user.getId(), user.getUserStandardization().getId());
    }

    /**
     * Lista los codigos de homologacion de un examen
     *
     * @param centralsystem
     * @return Lista de codigos de homologacion
     * @throws Exception Error en la base de datos.
     */
    default List<HomologationCode> getHomologationCode(int centralsystem) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab61c1, lab39c1 "
                    + "FROM lab61 "
                    + " WHERE lab118c1 = ? ", (ResultSet rs, int i) ->
            {

                HomologationCode homologationCode = new HomologationCode();
                homologationCode.setId(rs.getInt("lab39c1"));
                homologationCode.setCodes(rs.getString("lab61c1"));
                return homologationCode;
            }, centralsystem);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * ide de test  de homologacion de un examen
     *
     * @param centralsystem
     * @return Lista de codigos de homologacion
     * @throws Exception Error en la base de datos.
     */
    default Integer getHomologationIdTest(int idCentralSystem, String homologationCode) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c1")
                    .append(" FROM lab61 ")
                    .append("WHERE lab118c1 = ").append(idCentralSystem)
                    .append(" AND lab61c1 = '").append(homologationCode).append("'");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c1");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }

    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Consulta el id del item demografico por el id del demografico, sistema
     * central y codigo de homologación
     *
     * @param idCentralSystem Id sistema central.
     * @param idDemographic Id demografico
     * @param homologationCode
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    default Integer getIdItemDemoByHomologationCode(int idCentralSystem, int idDemographic, String homologationCode) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab117c2")
                    .append(" FROM lab117 ")
                    .append("WHERE lab118c1 = ").append(idCentralSystem)
                    .append(" AND lab117c1 = ").append(idDemographic)
                    .append(" AND lab117c3 = '").append(homologationCode).append("'");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab117c2");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

}
