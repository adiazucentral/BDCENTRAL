package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.integration.mobile.AuthenticationData;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;
import net.cltech.enterprisent.domain.integration.mobile.LisPatient;
import net.cltech.enterprisent.domain.integration.mobile.OrderEt;
import net.cltech.enterprisent.domain.integration.mobile.PatientEmailUpdate;
import net.cltech.enterprisent.domain.integration.mobile.Requirement;
import net.cltech.enterprisent.domain.integration.mobile.Sample;
import net.cltech.enterprisent.domain.integration.mobile.TestEt;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * pacientes para al app Móvil.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/08/2020
 * @see Creación
 */
public interface IntegrationMobileDao
{

    /**
     * Retorna el usuario autorizado del paciente
     *
     * @param email
     * @param password
     *
     * @return AuthorizedPatient
     * @throws Exception Error en la base de datos.
     */
    default AuthorizedPatient getEtUserAuthentication(String email, String password) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab21c1 AS idPatient, lab21c2 AS patientId, lab21c5 AS lastName, ")
                    .append("lab21c6 AS subName, lab21c3 AS name1, lab21c8 AS email ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21.lab21c8 = '").append(email)
                    .append("' AND lab21.lab21c19 = '").append(Tools.encrypt(password)).append("'");
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                AuthorizedPatient authorizedPatient = new AuthorizedPatient();

                authorizedPatient.setId(rs.getInt("idPatient"));
                authorizedPatient.setPatientId(Tools.decrypt(rs.getString("patientId")));
                authorizedPatient.setLastName(Tools.decrypt(rs.getString("lastName")));
                authorizedPatient.setSurName(Tools.decrypt(rs.getString("subName")));
                authorizedPatient.setName1(Tools.decrypt(rs.getString("name1")));
                authorizedPatient.setEmail(rs.getString("email"));
                authorizedPatient.setTemporalPassword(false);

                return authorizedPatient;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Retorna 1 si actualiza
     *
     * @param id
     * @param password
     *
     * @return boolean
     *
     */
    default boolean changePassword(int id, String password)
    {

        return getJdbcTemplate().update("UPDATE lab21"
                + " SET lab21c19 = ? "
                + " WHERE lab21c1 = ? ",
                password,
                id) == 1;
    }

    /**
     *
     * Retorna listado de Ordenes de un paciente
     *
     * @param limit
     * @param idPatient
     * @return Lista de ordenes de un paciente
     * @throws Exception Error en la base de datos.
     */
    default List<OrderEt> getEtOrders(int limit, int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab22.lab22c1 AS ordernumber, lab22.lab22c3 AS datecreation, ")
                    .append("lab103.lab103c3 as ordertype ")
                    .append("FROM lab22 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1  = lab21.lab21c1 ")
                    .append("INNER JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 ")
                    .append("INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 ")
                    .append("WHERE lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null)  AND lab22.lab21c1 = ").append(idPatient)
                    .append(" AND (lab57.lab57c8  = ").append(LISEnum.ResultTestState.VALIDATED.getValue())
                    .append(" OR lab57.lab57c8  = ").append(LISEnum.ResultTestState.DELIVERED.getValue()).append(")")
                    .append(" ORDER BY lab22.lab22c1 DESC");
                    //.append(" LIMIT ").append(limit);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                OrderEt orderEt = new OrderEt();

                orderEt.setOrder(rs.getLong("ordernumber"));
                orderEt.setDate(rs.getDate("datecreation"));
                orderEt.setType(rs.getString("ordertype"));

                return orderEt;
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     *
     * Retorna paciente
     *
     * @param patientId
     * @return Lista de ordenes de un paciente
     * @throws Exception Error en la base de datos.
     */
    default List<LisPatient> getPatientByPatientId(String patientId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append(" lab21c1 AS id,")
                    .append(" lab54c3 AS documentType,")
                    .append(" lab21c2 AS record,")
                    .append(" lab21c3 AS firstName,")
                    .append(" lab21c4 AS secondName,")
                    .append(" lab21c5 AS firstLastname,")
                    .append(" lab21c6 AS secondLastname,")
                    .append(" lab21c8 AS email,")
                    .append(" lab21c14 AS patientPhoto")
                    .append(" FROM lab21")
                    .append(" INNER JOIN lab54 on lab54.lab54c1 = lab21.lab54c1")
                    .append(" WHERE lab21c2 = '").append(Tools.encrypt(patientId)).append("'");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                LisPatient patient = new LisPatient();

                patient.setId(rs.getInt("id"));
                patient.setDocumentType(rs.getString("documentType"));
                patient.setPatientId(Tools.decrypt(rs.getString("record")));
                patient.setFirstName(Tools.decrypt(rs.getString("firstName")));
                patient.setSecondName(Tools.decrypt(rs.getString("secondName")));
                patient.setFirsLastname(Tools.decrypt(rs.getString("firstLastname")));
                patient.setSecondLastname(Tools.decrypt(rs.getString("secondLastname")));
                patient.setEmail(rs.getString("email"));
                patient.setPhotoBase64(rs.getString("patientPhoto"));

                return patient;
            });
        } catch (Exception e)
        {
             return null;
        }
    }

    /**
     * Retorna 1 si actualiza el campo de clave a nulo para restaurar la clave
     *
     * @param patientId
     *
     * @return boolean
     *
     */
    default boolean restorPatientPassword(String patientId)
    {

        patientId = Tools.encrypt(patientId);

        return getJdbcTemplate().update("UPDATE lab21"
                + " SET lab21c19 = NULL "
                + " WHERE lab21c2 = ? ",
                patientId) == 1;
    }

    /**
     * Retorna el usuario autorizado del paciente por su email
     *
     * @param id
     *
     * @return AuthorizedPatient
     * @throws Exception Error en la base de datos.
     */
    default AuthenticationData get(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab21c8 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21.lab21c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                AuthenticationData user = new AuthenticationData();

                user.setUser(rs.getString("lab21c8"));

                return user;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    default List<TestEt> getEtTests() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT")
                    .append(" lab39.lab39c1 AS id,")
                    .append(" lab39.lab39c2 AS code,")
                    .append(" lab39.lab39c3 AS abbr,")
                    .append(" lab39.lab39c4 AS name,")
                    .append(" lab39.lab39c35 AS info,")
                    .append(" lab39.lab07c1 AS status,")
                    .append(" lab39.lab39c24 AS viewInOrder,")
                    .append(" lab24.lab24c1 AS sampleId,")
                    .append(" lab24.lab24c2 AS sampleCode,")
                    .append(" lab24.lab24c3 AS sampleName")
                    .append(" FROM lab39")
                    .append(" INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1")
                    .append(" WHERE lab39.lab39c24 = 1");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                TestEt testList = new TestEt();
                testList.setId(rs.getInt("id"));
                testList.setCode(rs.getString("code"));
                testList.setAbbr(rs.getString("abbr"));
                testList.setName(rs.getString("name"));
                testList.setInfo(rs.getString("info"));
                /*Sample*/
                Sample testSample = new Sample();
                testSample.setId(rs.getInt("sampleId"));
                testSample.setCode(rs.getString("sampleCode"));
                testSample.setName(rs.getString("sampleNAme"));
                testList.setSample(testSample);

                testList.setActive(rs.getInt("status") == 1);
                testList.setViewInOrder(rs.getInt("viewInOrder") == 1);

                return testList;
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }

    default List<Requirement> getRequirements(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT")
                    .append(" lab41.lab41c1 AS id,")
                    .append(" lab41.lab41c2 AS code,")
                    .append(" lab41.lab41c3 AS description")
                    .append(" FROM lab41")
                    .append(" INNER JOIN lab71 ON lab71.lab41c1 = lab41.lab41c1")
                    .append(" WHERE lab71.lab39c1 = ").append(id);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Requirement requirement = new Requirement();

                requirement.setId(rs.getInt("id"));
                requirement.setCode(rs.getString("code"));
                requirement.setDescription(rs.getString("description"));

                return requirement;
            });

        } catch (DataAccessException e)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Retorna el id del paciente
     *
     * @return el id del paciente
     * @throws Exception Error en la base de datos.
     */
    default Integer getIdPatient(String userName) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c1 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21c8 = '").append(userName).append("'");
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab21c1");
            });
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Retorna el id del paciente
     *
     * @return el id del paciente
     * @throws Exception Error en la base de datos.
     */
    default String havePassword(String userName) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c19 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21c8 = '").append(userName).append("'");
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getString("lab21c19");
            });
        } catch (Exception e)
        {
            return "";
        }
    }
    
    
    
     /**
     * Retorna el usuario autorizado del paciente
     *
     * @param email
     * @param id    
     *
     * @return AuthorizedPatient
     * @throws Exception Error en la base de datos.
     */
    default AuthorizedPatient getEtUserAuthenticationPasswordNull(String email, int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab21c1 AS idPatient, lab21c2 AS patientId, lab21c5 AS lastName, ")
                    .append("lab21c6 AS subName, lab21c3 AS name1, lab21c8 AS email ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21.lab21c8 = '").append(email)
                    .append("' AND lab21.lab21c1 = ").append(id);
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                AuthorizedPatient authorizedPatient = new AuthorizedPatient();

                authorizedPatient.setId(rs.getInt("idPatient"));
                authorizedPatient.setPatientId(Tools.decrypt(rs.getString("patientId")));
                authorizedPatient.setLastName(Tools.decrypt(rs.getString("lastName")));
                authorizedPatient.setSurName(Tools.decrypt(rs.getString("subName")));
                authorizedPatient.setName1(Tools.decrypt(rs.getString("name1")));
                authorizedPatient.setEmail(rs.getString("email"));
                authorizedPatient.setTemporalPassword(true);
                return authorizedPatient;
            });
        } catch (Exception e)
        {
            return null;
        }
    }
    
    default int patientEmailUpdate(PatientEmailUpdate patient) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab21 SET lab21c8 = ? "
                    + "WHERE lab21c1 = ?",
                    patient.getEmail(),
                    patient.getId());
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
