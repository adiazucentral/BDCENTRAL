package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Julian
 */
public interface IntegrationConsultaWebHisDao
{
    /**
     * Atravez de este metodo podremos acceder a la información de la base de datos
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();
    
    /**
     * Por medio del id del paciente obtendremos la lista de todos los examenes relacionadas con ese paciente
     * 
     * @param idPatient
     * @return Lista de ordenes asociadas a un paciente
     * @throws java.lang.Exception
     */
    default List<TestBasic> allExamesForPatient(int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab57.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c37  ")
                 .append("FROM lab57  ")
                 .append("INNER JOIN lab22 ON lab57.lab22c1 = lab22.lab22c1 ")
                 .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                 .append("INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ")
                 .append("WHERE lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null)  AND ((lab57.lab57c8 >= 4  OR LAB39C37 != 0) AND lab57c14 is null  ) AND lab21.lab21c1 = ").append(idPatient);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                TestBasic test = new TestBasic();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setResultType(rs.getShort("lab39c11"));
                test.setTestType(rs.getShort("lab39c37"));
                return test;
            });
        } catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Por medio del id del examen obtendremos los datos requeridos para este modulo
     * 
     * @param idTest
     * @return Lista de ordenes asociadas a un paciente
     * @throws java.lang.Exception
     */
    default TestBasic getSingularTestForPatient(int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab39.lab39c1 AS lab39c1, ")
                    .append("lab39.lab43c1 AS lab43c1, ")
                    .append("lab39.lab39c2 AS lab39c2, ")
                    .append("lab39.lab39c3 AS lab39c3, ")
                    .append("lab39.lab39c4 AS lab39c4, ")
                    .append("lab39.lab39c37 AS lab39c37, ")
                    .append("lab22.lab22c7 AS lab22c7, ")
                    .append("lab39.lab39c11 AS lab39c11 ")
                    .append("FROM lab57 ")
                    .append("JOIN  lab39 ON (lab39.lab39c1 = lab57.lab39c1) ")
                    .append("JOIN  lab22 ON (lab22.lab22c1 = lab57.lab22c1) ")
                    .append("WHERE AND lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null) AND lab39.lab39c1 = ").append(idTest).append(" AND lab22.lab22c7 IS NOT NULL");

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                TestBasic test = new TestBasic();
                Area area = new Area();
                test.setId(rs.getInt("lab39c1"));
                area.setId(rs.getInt("lab43c1"));
                test.setArea(area);
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setResultType(rs.getShort("lab39c11"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setExternalSystemOrder(rs.getString("lab22c7"));
                return test;
            });
        } catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene un paciente buscandolo por numero de orden
     *
     * @param order Numero de Orden
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient getPatient(String order, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String select = ""
                    + "SELECT DISTINCT lab21.lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 ";
            String from = ""
                    + " FROM     Lab22 "
                    + "         INNER JOIN  Lab21 ON Lab21.Lab21C1 = Lab22.Lab21C1 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    Lab22c7 = ? AND (lab22c19 = 0 or lab22c19 is null) ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                return patient;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Por medio del id del examen se verifica la existencia de este examen dentro de la tabla de concurrencia
     * 
     * @param idTest
     * @return Booleano -> Existencia de este en concurrencia
     * @throws java.lang.Exception
     */
    default boolean examsThatAreNotPart(int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT 1 AS EXIST ")
                 .append("FROM lab46 ")
                 .append("WHERE lab46c1 = ").append(idTest);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("EXIST") == 1;
            });
        } catch (Exception e)
        {
            return false;
        }
    }
}
