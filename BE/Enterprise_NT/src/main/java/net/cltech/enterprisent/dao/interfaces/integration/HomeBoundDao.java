package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.homebound.AccountHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.AnswerHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Appointment;
import net.cltech.enterprisent.domain.integration.homebound.BasicHomeboundPatient;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.BranchHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ContainerHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DocumentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.GenderHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PatientHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PhysicianHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ProfileHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.QuestionHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.RateHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.SampleHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.TestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Track;
import net.cltech.enterprisent.domain.integration.homebound.TransportSampleHomebound;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.TestInformation;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para el registro de resultados a los exámenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/02/2020
 * @see Creación
 */
public interface HomeBoundDao
{

    /**
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista las pruebas de Home Bound desde la base de datos.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestHomeBound> listTestHomeBound() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab24.lab24c1 AS lab24c1_1, lab24.lab24c9 AS lab24c9_1, lab24.lab24c2 AS lab24c2_1,  lab39.lab07c1, lab39c37, lab39c6, lab39.lab43c1, lab39c9, lab39c7, lab39c8, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab43.lab43c1 AS lab43c1_1 , lab43c2, lab43c3, lab43c4, lab43c5, lab39.lab39c24  "
                    + "FROM lab39 "
                    + " LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                    + " LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 ",
                    (ResultSet rs, int i) ->
            {
                TestHomeBound testHomeBound = new TestHomeBound();
                testHomeBound.setId(rs.getInt("lab39c1"));
                testHomeBound.setCode(rs.getString("lab39c2"));
                testHomeBound.setAbbr(rs.getString("lab39c3"));
                testHomeBound.setName(rs.getString("lab39c4"));
                testHomeBound.setInfo(rs.getString("lab39c4"));
                testHomeBound.getSample().setId(rs.getInt("lab24c1_1"));
                testHomeBound.getSample().setCodesample(rs.getString("lab24c9_1"));
                testHomeBound.getSample().setName(rs.getString("lab24c2_1"));
                testHomeBound.setActive(rs.getBoolean("lab07c1"));
                testHomeBound.setType(rs.getInt("lab39c37"));
                testHomeBound.setViewInOrder(rs.getBoolean("lab39c24"));
                /*Genero*/
                testHomeBound.getSex().setId(rs.getInt("lab80c1"));
                testHomeBound.getSex().setIdParent(rs.getInt("lab80c2"));
                testHomeBound.getSex().setCode(rs.getString("lab80c3"));
                testHomeBound.getSex().setEsCo(rs.getString("lab80c4"));
                testHomeBound.getSex().setEnUsa(rs.getString("lab80c5"));
                /*Area*/
                testHomeBound.getArea().setId(rs.getInt("lab43c1_1"));
                testHomeBound.getArea().setOrdering(rs.getShort("lab43c2"));
                testHomeBound.getArea().setAbbreviation(rs.getString("lab43c3"));
                testHomeBound.getArea().setName(rs.getString("lab43c4"));
                testHomeBound.getArea().setColor(rs.getString("lab43c5"));
                testHomeBound.setAgeUnit(rs.getInt("lab39c9"));
                testHomeBound.setAgeMin(rs.getInt("lab39c7"));
                testHomeBound.setAgeMax(rs.getInt("lab39c8"));

                readRequirement(testHomeBound);

                return testHomeBound;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default List<ProfileHomeBound> listProfileHomeBound() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab07c1, lab39c37  "
                    + "FROM lab39 ",
                    (ResultSet rs, int i) ->
            {
                ProfileHomeBound testHomeBound = new ProfileHomeBound();
                testHomeBound.setId(rs.getInt("lab39c1"));
                testHomeBound.setCode(rs.getString("lab39c2"));
                testHomeBound.setAbbr(rs.getString("lab39c3"));
                testHomeBound.setName(rs.getString("lab39c4"));
                testHomeBound.setActive(rs.getBoolean("lab07c1"));
                testHomeBound.setType(rs.getInt("lab39c37"));             

                return testHomeBound;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

     /**
     * Obtiene los examenes hijos de un perfil o paquete retornando objeto
     * TestInformation
     *
     * @param test
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<TestInformation> getChildsByProfile(long test) throws Exception
    {
        return getConnection().query(""
                + "SELECT   lab39.lab39c1"
                + " , lab39.lab39c2 "
                + " , lab39.lab39c3 "
                + " , lab39.lab39c4 "
                + "FROM     lab46 "
                + "         INNER JOIN lab39 ON lab46.lab46c1 = lab39.lab39c1 "
                + "WHERE    lab46.lab39c1 = ? ",
                new Object[]
                {
                    test
                }, (ResultSet rs, int i) ->
        {
            TestInformation testInformation = new TestInformation();
            testInformation.setId(rs.getInt("lab39c1"));
            testInformation.setCode(rs.getString("lab39c2"));
            testInformation.setAbbr(rs.getString("lab39c3"));
            testInformation.setName(rs.getString("lab39c4"));           
            return testInformation;
        });
    }

    /**
     * Obtener requerimientos asociadas a una prueba.
     *
     * @param testHomeBound
     */
    default void readRequirement(TestHomeBound testHomeBound)
    {
        try
        {

            testHomeBound.setRequirements(getConnection().query("SELECT lab41.lab41c1, lab41c2, lab41c3, lab07c1, lab71.lab39c1 FROM lab41 "
                    + "LEFT JOIN lab71 ON lab71.lab41c1 = lab41.lab41c1 "
                    + "AND lab71.lab39c1 = ?"
                    + "",
                    new Object[]
                    {
                        testHomeBound.getId()
                    }, (ResultSet rs, int i) ->
            {
                Requirement requirement = new Requirement();
                requirement.setId(rs.getInt("lab41c1"));
                requirement.setCode(rs.getString("lab41c2"));
                requirement.setRequirement(rs.getString("lab41c3"));
                requirement.setState(rs.getInt("lab07c1") == 1);
                requirement.setSelected(rs.getString("lab39c1") != null);

                return requirement;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            testHomeBound.setRequirements(new ArrayList<>());
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param orders
     * @param demographics Lista de demograficos.
     * @param branch
     * @param user
     * @param tests
     * @return Lista de ordenes.
     */
    default List<Order> listOrders(List<Long> orders, List<Demographic> demographics, int branch, int user, List<Integer> tests, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype)
    {
        try
        {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5,  lab22c7, lab22c10, lab22c7, ");
            query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
            query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, ");
            query.append("lab05.lab05c1, lab05c10, lab05c4, ");
            query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4, ");
            query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, ");
            query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, lab39.lab39c38, ");
            query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
            query.append("lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, ");
            query.append("lab24.lab56c1, lab56.lab56c2,  lab56.lab56c3, ");
            query.append("p.lab39c38 as testdependence ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ");
            from.append("INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
            from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
            from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
            from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
            from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
            from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
            from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
            from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            from.append("LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 ");
            from.append("INNER JOIN lab69 ON lab69.lab43c1 = lab43.lab43c1 and lab69.lab04c1 = ").append(user);

            if (documenttype)
            {
                query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
            }
            if (service)
            {
                query.append(" , lab22.lab10c1, lab10c2, lab10c7 ");
                from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
            }
            if (account)
            {
                query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
            }
            if (physician)
            {
                query.append(" ,lab22.lab19c1,  lab19c2, lab19c3, lab19c21 ");
                from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
            }
            if (rate)
            {
                query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
            }
            if (race)
            {
                query.append(" , lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5 ");
                from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
            }

            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("O"))
                {
                    if (demographic.isEncoded())
                    {
                        query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                        query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                        query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                    } else
                    {
                        query.append(", lab22.lab_demo_").append(demographic.getId());
                    }
                } else
                {
                    if (demographic.getOrigin().equals("H"))
                    {
                        if (demographic.isEncoded())
                        {
                            query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else
                        {
                            query.append(", lab21.lab_demo_").append(demographic.getId());
                        }
                    }
                }
            }
            Object[] params = null;

            from.append(" WHERE lab22.lab07c1 = 1  AND lab22.lab05c1 = ").append(branch).append(" AND lab57.lab22c1 in (").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(")");
            from.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");
            from.append(" AND (lab22c19 = 0 or lab22c19 is null)  ORDER BY lab22c3 DESC");

            List<Order> listOrders = new ArrayList<>();
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i) ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setOrderHis(rs.getString("lab22c7"));
                if (listOrders.contains(order))
                {
                    Test test = new Test();
                    test.setId(rs.getInt("lab39c1"));
                    test.setCode(rs.getString("lab39c2"));
                    test.setAbbr(rs.getString("lab39c3"));
                    test.setName(rs.getString("lab39c4"));
                    test.setTestType(rs.getShort("lab39c37"));
                    test.setProfile(rs.getInt("lab57c14"));
                    test.setTestState(rs.getInt("lab57c8"));
                    test.setSampleState(rs.getInt("lab57c16"));
                    test.setDependentTest(rs.getInt("testdependence"));

                    test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                    test.getArea().setAbbreviation(rs.getString("lab43c3"));
                    test.getArea().setName(rs.getString("lab43c4"));

                    test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                    test.getSample().getContainer().setName(rs.getString("lab56c2"));

                    String Imabas64 = "";
                    byte[] ImaBytes = rs.getBytes("lab56c3");
                    if (ImaBytes != null)
                    {
                        Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                    }
                    test.getSample().getContainer().setImage(Imabas64);

                    listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                } else
                {
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setCreatedDate(rs.getTimestamp("lab22c3"));
                    order.setInconsistency(rs.getInt("lab22c10") == 1);

                    order.setMiles(rs.getInt("lab22c5"));
                    order.setExternalId(rs.getString("lab22c7"));

                    order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                    order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                    order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                    order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                    //TIPO DE ORDEN
                    order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                    order.getType().setCode(rs.getString("lab103c2"));
                    order.getType().setName(rs.getString("lab103c3"));
                    order.getType().setColor(rs.getString("lab103c4"));
                    order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setBirthDayFormat(format.format(rs.getTimestamp("lab21c7")));

                    order.getPatient().setEmail(rs.getString("lab21c8"));
                    order.getPatient().setSize(rs.getBigDecimal("lab21c9"));
                    order.getPatient().setWeight(rs.getBigDecimal("lab21c10"));
                    order.getPatient().setDateOfDeath(rs.getTimestamp("lab21c11"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
                    order.getPatient().setPhone(rs.getString("lab21c16"));
                    order.getPatient().setAddress(rs.getString("lab21c17"));
                    //PACIENTE - SEXO
                    order.getPatient().getSex().setId(rs.getInt("lab21lab80lab80c1"));
                    order.getPatient().getSex().setIdParent(rs.getInt("lab21lab80lab80c2"));
                    order.getPatient().getSex().setCode(rs.getString("lab21lab80lab80c3"));
                    order.getPatient().getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                    order.getPatient().getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));

                    //SEDE
                    order.getBranch().setId(rs.getInt("lab05c1"));
                    order.getBranch().setCode(rs.getString("lab05c10"));
                    order.getBranch().setName(rs.getString("lab05c4"));
                    if (service)
                    {
                        //SERVICIO
                        ServiceLaboratory serviceLaboratory = new ServiceLaboratory();
                        serviceLaboratory.setId(rs.getInt("lab10c1"));
                        serviceLaboratory.setCode(rs.getString("lab10c7"));
                        serviceLaboratory.setName(rs.getString("lab10c2"));
                        order.setService(serviceLaboratory);
                    }

                    if (account)
                    {
                        //EMPRESA
                        Account accountLaboratory = new Account();
                        accountLaboratory.setNit(rs.getString("lab14c2"));
                        accountLaboratory.setId(rs.getInt("lab14c1"));
                        accountLaboratory.setName(rs.getString("lab14c3"));
                        accountLaboratory.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                        order.setAccount(accountLaboratory);
                    }

                    if (physician)
                    {
                        //MEDICO
                        Physician physicianLaboratory = new Physician();
                        physicianLaboratory.setId(rs.getInt("lab19c1"));
                        physicianLaboratory.setName(rs.getString("lab19c2"));
                        physicianLaboratory.setName(rs.getString("lab19c2"));
                        physicianLaboratory.setEmail(rs.getString("lab19c21"));
                        order.setPhysician(physicianLaboratory);
                    }
                    if (rate)
                    {
                        //TARIFA
                        Rate rateLaboratory = new Rate();
                        rateLaboratory.setId(rs.getInt("lab904c1"));
                        rateLaboratory.setCode(rs.getString("lab904c2"));
                        rateLaboratory.setName(rs.getString("lab904c3"));
                        order.setRate(rateLaboratory);
                    }
                    if (race)
                    {
                        //PACIENTE - RAZA
                        order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
                        order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
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
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            }
                        } else
                        {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        if (demographic.getOrigin().equals("O"))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }

                    if (rs.getString("lab39c1") != null)
                    {
                        Test test = new Test();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.setTestType(rs.getShort("lab39c37"));
                        test.setProfile(rs.getInt("lab57c14"));
                        test.setTestState(rs.getInt("lab57c8"));
                        test.setSampleState(rs.getInt("lab57c16"));
                        test.setDependentTest(rs.getInt("testdependence"));

                        test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                        test.getArea().setAbbreviation(rs.getString("lab43c3"));
                        test.getArea().setName(rs.getString("lab43c4"));

                        test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                        test.getSample().setName(rs.getString("lab24c2"));
                        test.getSample().setCodesample(rs.getString("lab24c9"));
                        test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                        test.getSample().setCanstiker(rs.getInt("lab24c4"));
                        test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                        test.getSample().getContainer().setName(rs.getString("lab56c2"));

                        String Imabas64 = "";
                        byte[] ImaBytes = rs.getBytes("lab56c3");
                        if (ImaBytes != null)
                        {
                            Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                        }
                        test.getSample().getContainer().setImage(Imabas64);

                        order.getTests().add(test);
                    }
                    listOrders.add(order);
                }
                return order;
            };
            EventsLog.info(query.toString() + " " + from.toString());
            getConnection().query(query.toString() + " " + from.toString(), mapper);

            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            EventsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param orders
     * @param demographics Lista de demograficos.
     * @param branch
     * @param user
     * @param tests
     * @return Lista de ordenes.
     */
    default List<Order> listBasicOrders(List<Long> orders, List<Demographic> demographics, int branch, int user, List<Integer> tests, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype, List<Integer> samplestate)
    {
        try
        {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5,  lab22c7, lab22c10, lab22c7, ");
            query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
            query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, ");
            query.append("lab05.lab05c1, lab05c10, lab05c4, ");
            query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ");
            from.append("INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
            from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
            from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
            from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
            from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
            from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
            from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");

            from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            //from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
            //from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            //from.append("LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 ");
            from.append("INNER JOIN lab69 ON lab69.lab43c1 = lab43.lab43c1 and lab69.lab04c1 = ").append(user);

            if (documenttype)
            {
                query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
            }
            if (service)
            {
                query.append(" , lab22.lab10c1, lab10c2, lab10c7 ");
                from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
            }
            if (account)
            {
                query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
            }
            if (physician)
            {
                query.append(" ,lab22.lab19c1,  lab19c2, lab19c3, lab19c21 ");
                from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
            }
            if (rate)
            {
                query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
            }
            if (race)
            {
                query.append(" , lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5 ");
                from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
            }

            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("O"))
                {
                    if (demographic.isEncoded())
                    {
                        query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                        query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                        query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                    } else
                    {
                        query.append(", lab22.lab_demo_").append(demographic.getId());
                    }
                } else
                {
                    if (demographic.getOrigin().equals("H"))
                    {
                        if (demographic.isEncoded())
                        {
                            query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else
                        {
                            query.append(", lab21.lab_demo_").append(demographic.getId());
                        }
                    }
                }
            }
            Object[] params = null;

            from.append(" WHERE lab22.lab07c1 = 1  AND lab22.lab05c1 = ").append(branch).append(" AND lab57.lab22c1 in (").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(")");
            from.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");

            if (!samplestate.isEmpty())
            {

                from.append(" AND lab57c16 in (").append(samplestate.stream().map(s -> s.toString()).collect(Collectors.joining(","))).append(")");
            }

            from.append(" AND (lab22c19 = 0 or lab22c19 is null) ORDER BY lab22c3 DESC");

            List<Order> listOrders = new ArrayList<>();
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i) ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setOrderHis(rs.getString("lab22c7"));

                order.setCreatedDateShort(rs.getInt("lab22c2"));
                order.setCreatedDate(rs.getTimestamp("lab22c3"));
                order.setInconsistency(rs.getInt("lab22c10") == 1);

                order.setMiles(rs.getInt("lab22c5"));
                order.setExternalId(rs.getString("lab22c7"));

                order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                //TIPO DE ORDEN
                order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.getType().setCode(rs.getString("lab103c2"));
                order.getType().setName(rs.getString("lab103c3"));
                order.getType().setColor(rs.getString("lab103c4"));
                order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c6")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                order.getPatient().setBirthDayFormat(format.format(rs.getTimestamp("lab21c7")));

                order.getPatient().setEmail(rs.getString("lab21c8"));
                order.getPatient().setSize(rs.getBigDecimal("lab21c9"));
                order.getPatient().setWeight(rs.getBigDecimal("lab21c10"));
                order.getPatient().setDateOfDeath(rs.getTimestamp("lab21c11"));
                order.getPatient().setOrderNumber(order.getOrderNumber());
                order.getPatient().setPhone(rs.getString("lab21c16"));
                order.getPatient().setAddress(rs.getString("lab21c17"));
                //PACIENTE - SEXO
                order.getPatient().getSex().setId(rs.getInt("lab21lab80lab80c1"));
                order.getPatient().getSex().setIdParent(rs.getInt("lab21lab80lab80c2"));
                order.getPatient().getSex().setCode(rs.getString("lab21lab80lab80c3"));
                order.getPatient().getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                order.getPatient().getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));

                //SEDE
                order.getBranch().setId(rs.getInt("lab05c1"));
                order.getBranch().setCode(rs.getString("lab05c10"));
                order.getBranch().setName(rs.getString("lab05c4"));
                if (service)
                {
                    //SERVICIO
                    ServiceLaboratory serviceLaboratory = new ServiceLaboratory();
                    serviceLaboratory.setId(rs.getInt("lab10c1"));
                    serviceLaboratory.setCode(rs.getString("lab10c7"));
                    serviceLaboratory.setName(rs.getString("lab10c2"));
                    order.setService(serviceLaboratory);
                }

                if (account)
                {
                    //EMPRESA
                    Account accountLaboratory = new Account();
                    accountLaboratory.setNit(rs.getString("lab14c2"));
                    accountLaboratory.setId(rs.getInt("lab14c1"));
                    accountLaboratory.setName(rs.getString("lab14c3"));
                    accountLaboratory.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                    order.setAccount(accountLaboratory);
                }

                if (physician)
                {
                    //MEDICO
                    Physician physicianLaboratory = new Physician();
                    physicianLaboratory.setId(rs.getInt("lab19c1"));
                    physicianLaboratory.setName(rs.getString("lab19c2"));
                    physicianLaboratory.setName(rs.getString("lab19c2"));
                    physicianLaboratory.setEmail(rs.getString("lab19c21"));
                    order.setPhysician(physicianLaboratory);
                }
                if (rate)
                {
                    //TARIFA
                    Rate rateLaboratory = new Rate();
                    rateLaboratory.setId(rs.getInt("lab904c1"));
                    rateLaboratory.setCode(rs.getString("lab904c2"));
                    rateLaboratory.setName(rs.getString("lab904c3"));
                    order.setRate(rateLaboratory);
                }
                if (race)
                {
                    //PACIENTE - RAZA
                    order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                    order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
                    order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
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
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    if (demographic.getOrigin().equals("O"))
                    {
                        order.getDemographics().add(demoValue);
                    } else
                    {
                        order.getPatient().getDemographics().add(demoValue);
                    }
                }

                return order;
            };

            return getConnection().query(query.toString() + " " + from.toString(), mapper);

            //return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene una lista de generos de NT para ser enviados a Homebound
     *
     * @return Lista de generos
     * @throws Exception Error en la base de datos.
     */
    default List<GenderHomeBound> listGenresLanguage() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab80c1, lab80c3, lab80c4, lab80c5")
                    .append(" FROM lab80")
                    .append(" WHERE lab80c2 = 6");

            return getConnection().query(query.toString(), (ResultSet rs, int i) ->
            {
                GenderHomeBound gender = new GenderHomeBound();
                gender.setId(rs.getInt("lab80c1"));
                gender.setCode(rs.getString("lab80c3"));
                gender.setName(rs.getString("lab80c4"));
                gender.setNameEn(rs.getString("lab80c5"));
                return gender;
            });

        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Retorna Un objeto cita por numeo de orden
     *
     * @param order
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default Appointment getAppointment(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab22.lab22c1, lab21.lab21c1 as idpatient , lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab103.lab103c1 as idtype, ")
                    .append("lab103.lab103c2 as codetype, lab22.lab10c1 as idservice, lab19.lab19c1 as idphysician, ")
                    .append("lab19.lab19c2 as namephysician, lab19.lab19c22 as codephysician, lab14.lab14c1 as idaccount, ")
                    .append("lab14.lab14c3 as accountname, lab14.lab14c13 as codeccount, ")
                    .append("lab904.lab904c1 as idrate, lab904c2 as codrate, lab904.lab904c3 as namerate, ")
                    .append("lab05.lab05c1 as idbranch, lab05.lab05c4 as namebranch, lab05.lab05c10 as codebranch, ")
                    .append("lab54.lab54c1, lab54c2, lab54c3,  ")
                    .append("lab21.lab08c1 as idgender , lab80c3, lab80c4, lab80c5 ")
                    .append("FROM lab22 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                    .append("INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 ")
                    .append("LEFT JOIN lab19 ON lab22.lab19c1 = lab19.lab19c1 ")
                    .append("LEFT JOIN lab14 ON  lab22.lab14c1 = lab14.lab14c1 ")
                    .append("LEFT JOIN lab904 ON  lab22.lab904c1 = lab904.lab904c1 ")
                    .append("LEFT JOIN lab05 ON  lab22.lab05c1 = lab05.lab05c1 ")
                    .append("LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ")
                    .append("INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 ")
                    .append("WHERE lab22.lab22c1 =").append(order)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null) ");

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                Appointment appointment = new Appointment();

                appointment.setOrderNumber(rs.getLong("lab22c1"));
                PatientHomeBound patient = new PatientHomeBound();
                patient.setId(rs.getInt("idpatient"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                GenderHomeBound item = new GenderHomeBound();
                item.setId(rs.getInt("idgender"));
                item.setCode(rs.getString("lab80c3"));
                item.setName(rs.getString("lab80c4"));
                item.setNameEn(rs.getString("lab80c5"));
                patient.setSex(item);

                if (rs.getString("lab54c1") != null)
                {
                    DocumentTypeHomeBound documentType = new DocumentTypeHomeBound();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setCode(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                appointment.setPatient(patient);
                appointment.setType(rs.getString("codetype"));
                appointment.setIdService(rs.getInt("idservice"));

                PhysicianHomeBound physicianHomeBound = new PhysicianHomeBound();
                physicianHomeBound.setId(rs.getInt("idphysician"));
                physicianHomeBound.setName(rs.getString("namephysician"));
                physicianHomeBound.setCode(rs.getString("codephysician"));
                appointment.setPhysician(physicianHomeBound);

                AccountHomeBound accountHomeBound = new AccountHomeBound();
                accountHomeBound.setId(rs.getInt("idaccount"));
                accountHomeBound.setName(rs.getString("accountname"));
                accountHomeBound.setCode(rs.getString("codeccount"));
                appointment.setAccount(accountHomeBound);

                RateHomeBound rate = new RateHomeBound();
                rate.setId(rs.getInt("idrate"));
                rate.setCode(rs.getString("codrate"));
                rate.setName(rs.getString("namerate"));
                appointment.setRate(rate);
                BranchHomeBound branch = new BranchHomeBound();
                branch.setId(rs.getInt("idbranch"));
                branch.setName(rs.getString("namebranch"));
                branch.setCode(rs.getString("codebranch"));
                appointment.setBranch(branch);

                return appointment;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Retorna un listado de examenes con su tarifa
     *
     * @param order
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default List<BillingTestHomeBound> getTestsList(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57.lab22c1, lab57.lab39c1, lab39.lab39c1 as idtest, lab39.lab39c2 as codetest, lab39.lab39c3 as abbrtest, ")
                    .append("lab39.lab39c4 as nametest, lab900.lab904c1 as lab900, lab904.lab904c1 as idrate, lab904.lab904c2 as coderate, ")
                    .append("lab904.lab904c3 as namerate ")
                    .append("FROM lab57 ")
                    .append("INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ")
                    .append("LEFT JOIN lab900 ON lab57.lab39c1 = lab900.lab39c1 and lab57.lab22c1 = lab900.lab22c1 ")
                    .append("LEFT JOIN lab904 ON lab900.lab904c1 = lab904.lab904c1 ")
                    .append("WHERE lab57.lab22c1 =").append(order);

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                BillingTestHomeBound testHomeBoundRate = new BillingTestHomeBound();

                testHomeBoundRate.setId(rs.getInt("idtest"));
                testHomeBoundRate.setCode(rs.getString("codetest"));
                testHomeBoundRate.setAbbr(rs.getString("abbrtest"));
                testHomeBoundRate.setName(rs.getString("nametest"));
                RateHomeBound rate = new RateHomeBound();
                rate.setId(rs.getInt("idrate"));
                rate.setCode(rs.getString("coderate"));
                rate.setName(rs.getString("namerate"));
                testHomeBoundRate.setRate(rate);

                return testHomeBoundRate;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene las muestras asignadas a una orden
     *
     * @param idOrder
     *
     * @return Lista de estudios correspondientes para esa orden
     * @throws java.lang.Exception
     */
    default List<SampleHomeBound> getSamplesByIdOrder(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT DISTINCT lab24.lab24c1 AS lab24c1, ")
                    .append("lab24.lab24c2 AS lab24c2, ")
                    .append("lab24.lab24c9 AS lab24c9, ")
                    .append("lab57.lab22c1 AS lab22c1, ")
                    .append("lab24.lab56c1, lab56c2, ")
                    .append("lab24.lab04c1, lab24.lab24c8, lab24.lab24c3, lab24.lab24c4, lab24.lab24c6 ")
                    .append("FROM lab57 ")
                    .append("JOIN lab24 on lab24.lab24c1 = lab57.lab24c1 ")
                    .append("JOIN lab56 on lab56.lab56c1 = lab24.lab56c1 ")
                    .append("JOIN lab22 on lab22.lab22c1 = lab57.lab22c1 ")
                    .append("WHERE lab57.lab22c1 = ").append(idOrder)
                    .append(" AND lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null) ");

            return getConnection().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                try
                {
                    SampleHomeBound sample = new SampleHomeBound();

                    sample.setId(rs.getInt("lab24c1"));
                    sample.setName(rs.getString("lab24c2"));
                    sample.setCode(rs.getString("lab24c9"));

                    ContainerHomeBound container = new ContainerHomeBound();

                    container.setId(rs.getInt("lab56c1"));
                    container.setName(rs.getString("lab56c2"));

                    sample.setContainer(container);

                    sample.setTakenUser(rs.getInt("lab04c1"));
                    sample.setTakenDate(rs.getDate("lab24c8"));
                    sample.setPrint(rs.getBoolean("lab24c3"));
                    sample.setCount(rs.getInt("lab24c4"));
                    sample.setInfo(rs.getString("lab24c6"));

                    return sample;
                } catch (Exception ex)
                {
                    return null;
                }
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Lista las pruebas de Home Bound desde la base de datos.
     *
     * @param idOrder
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestHomeBound> listTestHomeBoundByIdOrder(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab24.lab24c1 AS lab24c1_1, lab24.lab24c9 AS lab24c9_1, "
                    + "lab24.lab24c2 AS lab24c2_1,  lab39.lab07c1, lab39c11, lab39c6, lab39.lab43c1, lab39c9, lab39c7, lab39c8, "
                    + "lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab43.lab43c1 AS lab43c1_1 , lab43c2, lab43c3, lab43c4, "
                    + "lab43c5, lab39.lab39c24  "
                    + "FROM lab39 "
                    + " LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                    + " LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                    + " LEFT JOIN lab57 ON lab57.lab39c1 = lab39.lab39c1 "
            ).append("WHERE lab57.lab22c1 = ").append(idOrder);

            return getConnection().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                TestHomeBound testHomeBound = new TestHomeBound();
                testHomeBound.setId(rs.getInt("lab39c1"));
                testHomeBound.setCode(rs.getString("lab39c2"));
                testHomeBound.setAbbr(rs.getString("lab39c3"));
                testHomeBound.setName(rs.getString("lab39c4"));
                testHomeBound.setInfo(rs.getString("lab39c4"));
                testHomeBound.getSample().setId(rs.getInt("lab24c1_1"));
                testHomeBound.getSample().setCodesample(rs.getString("lab24c9_1"));
                testHomeBound.getSample().setName(rs.getString("lab24c2_1"));
                testHomeBound.setActive(rs.getBoolean("lab07c1"));
                testHomeBound.setType(rs.getInt("lab39c11"));
                testHomeBound.setViewInOrder(rs.getBoolean("lab39c24"));
                /*Genero*/
                testHomeBound.getSex().setId(rs.getInt("lab80c1"));
                testHomeBound.getSex().setIdParent(rs.getInt("lab80c2"));
                testHomeBound.getSex().setCode(rs.getString("lab80c3"));
                testHomeBound.getSex().setEsCo(rs.getString("lab80c4"));
                testHomeBound.getSex().setEnUsa(rs.getString("lab80c5"));
                /*Area*/
                testHomeBound.getArea().setId(rs.getInt("lab43c1_1"));
                testHomeBound.getArea().setOrdering(rs.getShort("lab43c2"));
                testHomeBound.getArea().setAbbreviation(rs.getString("lab43c3"));
                testHomeBound.getArea().setName(rs.getString("lab43c4"));
                testHomeBound.getArea().setColor(rs.getString("lab43c5"));
                testHomeBound.setAgeUnit(rs.getInt("lab39c9"));
                testHomeBound.setAgeMin(rs.getInt("lab39c7"));
                testHomeBound.setAgeMax(rs.getInt("lab39c8"));

                readRequirement(testHomeBound);

                return testHomeBound;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la información básica del paciente por medio de número de la
     * orden
     *
     * @param orders
     * @return Paciente con su información básica
     * @throws Exception Error presentado en el servicio
     */
    default List<BasicHomeboundPatient> getPatientBasicInfo(List<Long> orders) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT Pat.lab21c2 AS history")
                    .append(", docType.lab54c2 AS docTypeAbbr")
                    .append(", Pat.lab21c3 AS name1")
                    .append(", Pat.lab21c4 AS name2")
                    .append(", Pat.lab21c5 AS lastname1")
                    .append(", Pat.lab21c6 AS lastname2")
                    .append(", O.lab22c1 ")
                    .append(" FROM lab22 O ")
                    .append("JOIN lab21 Pat ON (O.lab21c1 = Pat.lab21c1) ")
                    .append("JOIN lab54 docType ON (docType.lab54c1 = Pat.lab54c1) ")
                    .append("WHERE O.lab22c1 in ( ").append(orders.stream().map(o -> o.toString()).collect(Collectors.joining(","))).append(")");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                BasicHomeboundPatient patient = new BasicHomeboundPatient();
                patient.setHistory(Tools.decrypt(rs.getString("history")));
                patient.setDocumentTypeCode(rs.getString("docTypeAbbr"));
                patient.setFirstName(Tools.decrypt(rs.getString("name1")));
                patient.setSecondName(Tools.decrypt(rs.getString("name2")));
                patient.setFirstSurname(Tools.decrypt(rs.getString("lastname1")));
                patient.setSecondSurname(Tools.decrypt(rs.getString("lastname2")));
                patient.setOrderNumber(rs.getLong("lab22c1"));
                return patient;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Consulta una entrevista con sus preguntas y sus respectivas respuestas o
     * respuesta por el id de una orden
     *
     * @param order Orden
     *
     * @return Entrevista con preguntas y respuestas
     * @throws Exception Error presentado en el servicio
     */
    default List<QuestionHomeBound> getInterviewByOrderId(Order order) throws Exception
    {
        try
        {
            String tests = order.getSamples().stream()
                    .map(sample -> sample.getTests().stream().map(test -> test.getId().toString()).collect(Collectors.joining(", ")))
                    .collect(Collectors.joining(", "));

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT DISTINCT lab70.lab70c1 AS questionId")
                    .append(", lab70.lab70c2 AS questionName")
                    .append(", lab70.lab70c3 AS questionFullName")
                    .append(", lab70.lab70c5 AS questionControl")
                    .append(", lab23.lab23c1 AS answerText")
                    .append(", lab23.lab70c5 AS type")
                    .append(" FROM lab23 ")
                    .append(" FULL JOIN lab70 On lab70.lab70c1 = lab23.lab70c1 AND lab23.lab22c1 = ").append(order.getOrderNumber())
                    .append(" INNER JOIN lab92 ON lab70.lab70c1 = lab92.lab70c1 AND (lab70.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) ")
                    .append(" LEFT JOIN lab44 ON lab44.lab44c1 = lab92.lab44c1 AND (lab44.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) ")
                    .append(" LEFT JOIN lab66 ON lab66.lab44c1 = lab44.lab44c1 ")
                    .append(" WHERE (lab66c1 = ").append(order.getType().getId()).append(" AND lab44c3 = 1) OR ")
                    .append("(lab66c1 IN(").append(tests)
                    .append(") AND lab44c3 = 3) ")
                    .append(" AND ((lab70.lab70c5 = 1 OR lab70.lab70c5 = 5) OR (lab23.lab70c5 = 1 OR lab23.lab70c5 = 5))")
                    .append("ORDER BY lab70.lab70c1 ASC");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                QuestionHomeBound question = new QuestionHomeBound();
                question.setId(rs.getInt("questionId"));
                question.setName(rs.getString("questionName"));
                question.setQuestion(rs.getString("questionFullName"));
                question.setControl((rs.getString("type") == null || rs.getString("type").equals("-1")) ? rs.getInt("questionControl") : rs.getInt("type"));
                question.setAnsweredText(rs.getString("answerText"));

                try
                {
                    question.setAnswers(getAnswersByQuestionId(question.getId()));
                } catch (Exception e)
                {
                    question.setAnswers(new ArrayList<>());
                }
                return question;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Consulta las respuestas a una pregunta por el id de esta
     *
     * @param questionId id de la pregunta
     *
     * @return Respuestas de una pregunta
     * @throws Exception Error presentado en el servicio
     */
    default List<AnswerHomeBound> getAnswersByQuestionId(int questionId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab90.lab90c1 AS answerId")
                    .append(", lab90.lab90c2 AS answerName")
                    .append(" FROM lab91 ")
                    .append("JOIN lab90 ON lab90.lab90c1 = lab91.lab90c1 ")
                    .append("WHERE lab91.lab70c1 = ").append(questionId)
                    .append(" AND lab90.lab07c1 = 1");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                AnswerHomeBound answer = new AnswerHomeBound();
                answer.setId(rs.getInt("answerId"));
                answer.setName(rs.getString("answerName"));
                return answer;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Elimina las preguntas asociadas a una orden.
     *
     * @param idOrder Id de la orden a la que se le borrara la entrevista.
     * @param idQuestion Id de la pregunta a eliminar
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int deleteInterview(long idOrder, int idQuestion) throws Exception
    {
        String deleteSql = "DELETE FROM lab23 WHERE lab22c1 = ? AND lab70c1 =  ? AND Lab53c1 IS NULL";
        return getConnection().update(deleteSql, idOrder, idQuestion);
    }

    /**
     * Inserta los resultados de la entrevista de la orden.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a la que se le realizo la entrevista.
     * @param user Usuario que ingreso la entrevista.
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int insertResultInterview(List<QuestionHomeBound> questions, long idOrder, AuthorizedUser user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab23");

        for (QuestionHomeBound question : questions)
        {
            deleteInterview(idOrder, question.getId());

            if (question.getAnswers() != null)
            {
                if (question.getAnswers().isEmpty())
                {
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", idOrder);
                    parameters.put("lab70c1", question.getId());
                    parameters.put("lab23c1", question.getAnsweredText());
                    parameters.put("lab23c2", timestamp);
                    parameters.put("lab04c1", user.getId());
                    parameters.put("lab70c5", question.getControl());
                    batchArray.add(parameters);
                } else
                {
                    for (Integer answer : question.getAnsweredIds())
                    {
                        HashMap parameters = new HashMap();
                        parameters.put("lab22c1", idOrder);
                        parameters.put("lab70c1", question.getId());
                        parameters.put("lab90c1", answer);
                        parameters.put("lab23c2", timestamp);
                        parameters.put("lab04c1", user.getId());
                        parameters.put("lab70c5", question.getControl());
                        batchArray.add(parameters);
                    }
                }
            } else
            {
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", idOrder);
                parameters.put("lab70c1", question.getId());
                parameters.put("lab23c1", question.getAnsweredText());
                parameters.put("lab23c2", timestamp);
                parameters.put("lab04c1", user.getId());
                parameters.put("lab70c5", question.getControl());
                batchArray.add(parameters);
            }
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));

        return inserted.length;
    }

    /**
     * Consulta todos los items demográficos de un demográfico por el id de este
     *
     * @param demographicId
     *
     * @return Lista de items demográficos
     * @throws Exception Error en la base de datos.
     */
    default List<DemographicHomeBound> getItemDemographicsByDemoId(int demographicId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab62.lab62c1")
                    .append(", lab63.lab63c1")
                    .append(", lab63.lab63c2")
                    .append(", lab63.lab63c3")
                    .append(", lab62.lab62c4")
                    .append(" FROM lab63 ")
                    .append("JOIN lab62 ON lab62.lab62c1 = lab63.lab62c1 ")
                    .append("WHERE lab62.lab62c1 = ").append(demographicId);

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                DemographicHomeBound demo = new DemographicHomeBound();
                demo.setId(rs.getInt("lab62c1"));
                demo.setItem(rs.getInt("lab63c1"));
                demo.setCode(rs.getString("lab63c2"));
                demo.setValue(rs.getString("lab63c3"));
                demo.setCoded(rs.getBoolean("lab62c4"));
                return demo;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Consulta todos los demográficos almacenados en el sistema
     *
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
     */
    default List<DemographicHomeBound> getAllDemographics() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab62c1")
                    .append(", lab62c2")
                    .append(", lab62c3")
                    .append(", lab62c4")
                    .append(", lab62c8")
                    .append(" FROM lab62 ")
                    .append(" WHERE lab07c1 = 1");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                DemographicHomeBound demo = new DemographicHomeBound();
                demo.setId(rs.getInt("lab62c1"));
                demo.setName(rs.getString("lab62c2"));
                demo.setSource(rs.getString("lab62c3"));
                demo.setType(rs.getInt("lab62c4"));
                demo.setDefaultValue(rs.getString("lab62c8"));
                demo.setCoded(rs.getBoolean("lab62c4"));
                return demo;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    
        /**
     * Consulta todos los demográficos almacenados en el sistema
     *
     * @param orderNumber
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
     */
    default Track getTracking(Long orderNumber) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                        
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append(" select lab22c1, lab22c2, lab22.lab07c1, lab103c3 , lab21c2, lab21c3, lab21c4,lab21c5, lab21c6, lab80c4, lab21c7, lab21.lab21c1")
                    .append(" from ").append(lab22).append(" as lab22 ")
                    .append(" inner join lab103 on lab22.lab103c1 = lab103.lab103c1 ")
                    .append(" inner join lab21 on lab22.lab21c1 = lab21.lab21c1 ")
                    .append(" inner join lab80 on lab21.lab80c1 = lab80.lab80c1 ")
                    .append(" where lab22c1 = ").append(orderNumber);

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Track track = new Track();
                
                track.setOrderNumber(rs.getLong("lab22c1"));
                track.setOrderDate(rs.getInt("lab22c2"));
                track.setIsActive(rs.getInt("lab07c1") == 1);
                track.setType(rs.getString("lab103c3"));
                track.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                track.setNames(Tools.decrypt(rs.getString("lab21c3")) + " " + Tools.decrypt(rs.getString("lab21c4")));
                track.setLastName(Tools.decrypt(rs.getString("lab21c5")) + " " + Tools.decrypt(rs.getString("lab21c6")));
                track.setGender(rs.getString("lab80c4"));
                track.setBirthday(rs.getString("lab21c7"));
                track.setPatientIdDB(rs.getInt("lab21c1"));
                
                return track;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }
    
           /**
     * Consulta todos los demográficos almacenados en el sistema
     *
     * @param date
     * @param branchId
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
     */
    default int getSecuenceOrder(int date, int branchId) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(date));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                        
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append(" select COUNT(lab22c1) as count")
                    .append(" from ").append(lab22).append(" as lab22 ")
                    .append(" where lab22c19 = 1 and lab22c2 = ").append(date).append(" AND lab05c1 = ").append(branchId);

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("count");
            });
        } catch (DataAccessException e)
        {
            return 0;
        }
    }
    
    /**
     * Actualiza la fecha de atencion de una orden
     *
     * @param orderId
     * @return True - Si la fecha de atendicon de la orden se actualizo, False-
     * Si no fue así
     * @throws Exception Error en la base de datos.
     */
    default boolean updateAppointmentDate(long orderId) throws Exception
    {
        try
        {
            // Fecha de atención
            Timestamp appointmentDate = new Timestamp(new Date().getTime());

            return getConnection().update("UPDATE lab22 "
                    + "SET lab22c15 = ?"
                    + " WHERE lab22c1 = ?",
                    appointmentDate,
                    orderId) > 0;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Actualiza la fecha de trasnporte de las muestras de una orden
     *
     * @param transportSampleHomebound
     * @return True - Si la fecha de atendicon de la orden se actualizo, False-
     * Si no fue así
     * @throws Exception Error en la base de datos.
     */
    default boolean updateTransportSample(List<TransportSampleHomebound> transportSampleHomebound) throws Exception
    {
        try
        {
            List<Object[]> parameters = new ArrayList<>();
            // Fecha de atención
            Timestamp concurrentDate = new Timestamp(new Date().getTime());

            String update = "UPDATE lab57 SET lab57c57 = ?, lab57c58 = ? ";
            String where = "WHERE lab22c1 = ? and lab57c16 = ? and lab24c1 in ";

            transportSampleHomebound.forEach((order) ->
            {
                String samples = "(" + order.getSamples().stream().map(sample -> sample.toString()).collect(Collectors.joining(",")) + ")";
                getConnection().update(update + where + samples, concurrentDate, order.getUser(), order.getOrder(), 3);
            });

            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Actualiza la informacion de un paciente y sus demograficos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     *
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default Patient updatePatientFromHomebound(Patient patient) throws Exception
    {
        String update = ""
                + "UPDATE   lab21 "
                + "SET lab21c2 = ? "
                + " , lab21c3 = ? "
                + " , lab21c4 = ? "
                + " , lab21c5 = ? "
                + " , lab21c6 = ? "
                + " , lab80c1 = ? "
                + " , lab21c7 = ? "
                + " , lab21c8 = ? "
                + " , lab21c9 = ? "
                + " , lab21c10 = ? "
                + " , lab21c11 = ? "
                + " , lab21c12 = ? "
                + " , lab04c1 = ? "
                + " , lab08c1 = ? "
                + " , lab54c1 = ? "
                + " , lab21c16 = ? "
                + " , lab21c17 = ? "
                + " , lab21c18 = ? ";
        Object[] args = new Object[19 + (!patient.getDemographicsHomebound().isEmpty() ? patient.getDemographicsHomebound().size() : 0)];
        args[0] = patient.getPatientId();
        args[1] = patient.getName1();
        args[2] = patient.getName2();
        args[3] = patient.getLastName();
        args[4] = patient.getSurName();
        args[5] = patient.getSex().getId();
        args[6] = new Timestamp(patient.getBirthday().getTime());
        args[7] = patient.getEmail();
        args[8] = patient.getSize();
        args[9] = patient.getWeight();
        args[10] = patient.getDateOfDeath() != null ? new Timestamp(patient.getDateOfDeath().getTime()) : null;
        args[11] = new Timestamp(patient.getLastUpdateDate().getTime());
        args[12] = patient.getLastUpdateUser().getId();
        args[13] = patient.getRace() == null ? null : patient.getRace().getId();
        args[14] = patient.getDocumentType() == null ? null : patient.getDocumentType().getId();
        args[15] = patient.getPhone();
        args[16] = patient.getAddress();
        args[17] = patient.getPatientId();
        int index = 18;
        if (!patient.getDemographicsHomebound().isEmpty())
        {

            for (DemographicHomeBound demographic : patient.getDemographicsHomebound())
            {
                update += ", lab_demo_" + demographic.getId() + " = ? ";
                args[index] = demographic.getType() == 1 ? demographic.getItem() : demographic.getValue();
                index++;
            }

        }

        args[index] = patient.getId();

        update += " WHERE lab21c1 = ? ";

        int affectedRows = getConnection().update(update, args);
        return affectedRows > 0 ? patient : null;
    }

    /**
     * Inserta un nuevo paciente con sus demograficos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     *
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default Patient insertPatientFromHomebound(Patient patient) throws Exception
    {
        patient.setPasswordWebQuery(Tools.generatePassword());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab21")
                .usingGeneratedKeyColumns("lab21c1");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab21c2", patient.getPatientId());
        parameters.put("lab21c3", patient.getName1());
        parameters.put("lab21c4", patient.getName2());
        parameters.put("lab21c5", patient.getLastName());
        parameters.put("lab21c6", patient.getSurName());
        parameters.put("lab80c1", patient.getSex().getId());
        parameters.put("lab21c7", new Timestamp(patient.getBirthday().getTime()));
        parameters.put("lab21c8", patient.getEmail());
        parameters.put("lab21c9", patient.getSize());
        parameters.put("lab21c10", patient.getWeight());
        parameters.put("lab21c11", patient.getDateOfDeath() != null ? new Timestamp(patient.getDateOfDeath().getTime()) : null);
        parameters.put("lab21c12", new Timestamp(patient.getLastUpdateDate().getTime()));
        parameters.put("lab21c13", patient.getDiagnostic());
        parameters.put("lab04c1", patient.getLastUpdateUser().getId());
        parameters.put("lab08c1", patient.getRace() == null ? null : patient.getRace().getId());
        parameters.put("lab54c1", patient.getDocumentType() == null ? null : patient.getDocumentType().getId());
        parameters.put("lab21c16", patient.getPhone());
        parameters.put("lab21c17", patient.getAddress());
        parameters.put("lab21c18", patient.getPatientId());
        parameters.put("lab21c20", new Timestamp(patient.getLastUpdateDate().getTime()));
        parameters.put("lab04c1_2", patient.getLastUpdateUser().getId());
        parameters.put("lab21c19", patient.getPasswordWebQuery());
        if (patient.getDemographicsHomebound() != null && !patient.getDemographicsHomebound().isEmpty())
        {
            patient.getDemographicsHomebound().forEach((demographic) ->
            {
                parameters.put("lab_demo_" + demographic.getId(), demographic.getType() == 1 ? demographic.getItem() : demographic.getValue());
            });
        }

        int id = insert.executeAndReturnKey(parameters).intValue();
        patient.setId(id);
        return patient;
    }
}
