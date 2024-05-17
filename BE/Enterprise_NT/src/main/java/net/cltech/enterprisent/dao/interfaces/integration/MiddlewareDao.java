package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.integration.middleware.AnatomicalSiteMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.AntibioticMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DeltaCheckMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DemographicItemMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.MicroorganismsMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.ReferenceValueMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.SampleMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.SendAstmMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestToMiddleware;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para el registro de resultados a los exámenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2018
 * @see Creación
 */
public interface MiddlewareDao
{

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplateCont();

    /**
     * Lista la importación de exámenes al Middleware
     *
     * @return Para la importacion de muestras al middleware
     * @throws Exception Error en base de datos
     */
    default List<TestMiddleware> listTest() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab39.lab39c1"
                    + ", lab39.lab39c2"
                    + ", lab39.lab39c3"
                    + ", lab39.lab39c4"
                    + ", lab39.lab39c12"
                    + ", lab39.lab39c11"
                    + ", lab39.lab07c1"
                    + ", lab24.lab24c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c10"
                    + ", lab24.lab07c1"
                    + "  FROM lab39 "
                    + " LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 where lab39.lab39c37 = 0  ", (ResultSet rs, int i) ->
            {
                TestMiddleware testMiddleware = new TestMiddleware();
                testMiddleware.setId(rs.getInt("lab39c1"));
                testMiddleware.setCode(rs.getString("lab39c2"));
                testMiddleware.setAbbr(rs.getString("lab39c3"));
                testMiddleware.setName(rs.getString("lab39c4"));
                testMiddleware.setDecimals(rs.getInt("lab39c12"));
                testMiddleware.setType(rs.getInt("lab39c11"));
                testMiddleware.setActive(rs.getBoolean("lab07c1"));
                testMiddleware.getSample().setId(rs.getInt("lab24c1"));
                testMiddleware.getSample().setCode(rs.getString("lab24c9"));
                testMiddleware.getSample().setName(rs.getString("lab24c2"));
                //testMiddleware.getSample().setMicrobiology(rs.getString("lab24c10").indexOf("3") != -1);
                testMiddleware.getSample().setMicrobiology(rs.getString("lab24c10") != null ? rs.getString("lab24c10").contains("3") : null);
                testMiddleware.getSample().setActive(rs.getBoolean("lab07c1"));
                return testMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las muestras desde la base de datos
     *
     * @return Para la importacion de muestras al middleware
     * @throws Exception Error en base de datos
     */
    default List<SampleMiddleware> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab24.lab24c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c10"
                    + ", lab24.lab07c1"
                    + "  FROM lab24", (ResultSet rs, int i) ->
            {
                SampleMiddleware sampleMiddleware = new SampleMiddleware();
                sampleMiddleware.setId(rs.getInt("lab24c1"));
                sampleMiddleware.setCode(rs.getString("lab24c9"));
                sampleMiddleware.setName(rs.getString("lab24c2"));
                sampleMiddleware.setMicrobiology(rs.getString("lab24c10").indexOf("3") != -1);
                sampleMiddleware.setActive(rs.getBoolean("lab07c1"));
                return sampleMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la orden para envio al middleware.
     *
     *
     * @param orderNumber Numero de la orden
     * @param demographics lista de demograficos
     * @param action
     * @param idSample
     * @param laboratorys
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> orderResultList(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception
    {
        try
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            List<Order> orders = new ArrayList<>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("lab05.lab05c1, lab05c10, lab05c4, ")
                    .append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, ")
                    .append("lab22.lab22c1, lab22.lab22c4, lab22.lab22c5, lab22.lab22c7, lab22.lab103c1, lab103c2, lab103c3, ")
                    .append("lab24.lab24c1 AS ltlab24c1, ") //muestra id
                    .append("lab24.lab24c2 AS ltlab24c2, ") //muestra nombre
                    .append("lab24.lab24c9 AS ltlab24c9, ") //muestra codigo
                    .append("lab24.lab24c10, ") //tipo laboratorio
                    .append("lab39.lab39c1, ") //examen
                    .append("lab39.lab39c2, ") //código
                    .append("lab39.lab39c4, ") //nombre
                    .append("lab40.lab40c1, ") //id del laboratorio
                    .append("lab40.lab40c10, ") //url del laboratorio
                    .append("lab40.lab40c2, ") //codigo del laboratorio
                    .append("lab40.lab40c3, ") //nombre del laboratorio
                    .append("lab40.lab40c11, ") //Si se valida al ingreso
                    .append("lab40.lab40c12, ") //Si se valida en verificacion
                    .append("lab40.lab40c13, ") //Si se envía a Middleware externo
                    .append("lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ")
                    .append("lab57.lab57c8, ") //Estado del examen
                    .append("lab57.lab57c39, ") //Fecha Toma
                    .append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, ")
                    .append("lab10.lab10c1, lab10.lab10c2, ")
                    .append("lab19.lab19c1, lab19.lab19c2, lab19.lab19c3, ")
                    .append("lab904.lab904c1, lab904.lab904c2, lab904.lab904c3, ")
                    .append("lab14.lab14c1, lab14.lab14c3, ")
                    .append("a.lab24C1 AS sampleIdMicro, ")
                    .append("lab158.lab158C1 AS anatomicalSite, ")
                    .append("lab16.lab16c3, lab11.lab11c1 ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM  ").append(lab57).append(" as lab57 ")
                    .append("INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ")
                    .append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ")
                    .append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")// Muestra
                    .append("LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ") // Laboratorio
                    .append("LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                    .append("LEFT JOIN lab24 a ON lab57.lab24C1 = a.lab24C1 ")
                    .append("LEFT JOIN lab158 ON lab57.lab158C1 = lab158.lab158C1 ")
                    .append("LEFT JOIN lab10 ON lab22.lab10C1 = lab10.lab10c1   ")
                    .append("LEFT JOIN lab19 ON lab22.lab19C1 = lab19.lab19c1   ")
                    .append("LEFT JOIN lab14 ON lab22.lab14C1 = lab14.lab14c1   ")
                    .append("LEFT JOIN lab904 ON lab22.lab904C1 = lab904.lab904c1   ")
                    .append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

            demographics.stream().forEach((demographic)
                    ->
            {
                if (demographic.isEncoded())
                {
                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id");
                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code");
                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name");
                    if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    } else
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    }
                } else
                {
                    query.append(", lab_demo_").append(demographic.getId());
                }
            });

            from.append(" WHERE lab22.lab22c1 = ").append(orderNumber).append(" AND lab39.lab39c37 = 0 ");
            if (idSample != null)
            {
                from.append(" AND lab39.lab24c1 = ").append(idSample);
            }
            if (laboratorys != null)
            {
                from.append(" AND lab57.lab40c1 IN(").append(laboratorys).append(")");
            }

           
            from.append(" AND lab57.lab57c16 = 4 AND lab57.lab57c8 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());
            RowCallbackHandler handler = (ResultSet rs)
                    ->
            {
                Order order = new Order(rs.getLong("lab22c1"));
                if (!orders.contains(order))
                {
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setTests(new ArrayList<>());
                    order.setComments(new ArrayList<>());
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setSex(new Item(rs.getInt("lab80c1")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setDemographics(new ArrayList<>());
                    //SEDE
                    order.getBranch().setId(rs.getInt("lab05c1"));
                    order.getBranch().setCode(rs.getString("lab05c10"));
                    order.getBranch().setName(rs.getString("lab05c4"));

                    order.getType().setId(rs.getInt("lab103c1"));
                    order.getType().setCode(rs.getString("lab103c2"));
                    order.getType().setName(rs.getString("lab103c3"));

                    if (rs.getString("lab10c1") != null)
                    {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (rs.getString("lab19c1") != null)
                    {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (rs.getString("lab14c1") != null)
                    {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (rs.getString("lab904c1") != null)
                    {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    //DEMOGRAFICOS
                    order.setDemographics(new ArrayList<>());
                    for (Demographic demographic : demographics)
                    {
                        DemographicValue demoValue = new DemographicValue();
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
                        if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    orders.add(order);
                }
                order = orders.get(orders.indexOf(order));
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setAction(action == null ? "" : action);
                test.setService(rs.getString("lab10c2"));
                test.setSampleIdMicro(rs.getString("sampleIdMicro"));
                test.setAnatomicalSite(rs.getString("anatomicalSite"));
                test.setTestState(rs.getInt("lab57c8"));
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setCode(rs.getInt("lab40c2"));
                laboratory.setName(rs.getString("lab40c3"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                laboratory.setMiddleware(rs.getBoolean("lab40c13"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                order.getTests().add(test);
            };

            getJdbcTemplate().query(query.toString() + from.toString(), handler);
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Consulta la orden para envio al middleware.
     *
     *
     * @param orderNumber Numero de la orden
     * @param demographics lista de demograficos
     * @param action
     * @param idSample
     * @param laboratorys
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> orderResultListDeleteTest(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception
    {
        try
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            List<Order> orders = new ArrayList<>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("lab05.lab05c1, lab05c10, lab05c4, ")
                    .append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, ")
                    .append("lab22.lab22c1, lab22.lab22c4, lab22.lab22c5, lab22.lab22c7, lab22.lab103c1, lab103c2, lab103c3, ")
                    .append("lab24.lab24c1 AS ltlab24c1, ") //muestra id
                    .append("lab24.lab24c2 AS ltlab24c2, ") //muestra nombre
                    .append("lab24.lab24c9 AS ltlab24c9, ") //muestra codigo
                    .append("lab24.lab24c10, ") //tipo laboratorio
                    .append("lab39.lab39c1, ") //examen
                    .append("lab39.lab39c2, ") //código
                    .append("lab39.lab39c4, ") //nombre
                    .append("lab40.lab40c1, ") //id del laboratorio
                    .append("lab40.lab40c10, ") //url del laboratorio
                    .append("lab40.lab40c2, ") //codigo del laboratorio
                    .append("lab40.lab40c3, ") //nombre del laboratorio
                    .append("lab40.lab40c11, ") //Si se valida al ingreso
                    .append("lab40.lab40c12, ") //Si se valida en verificacion
                    .append("lab40.lab40c13, ") //Si se envía a Middleware externo
                    .append("lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ")
                    .append("lab57.lab57c8, ") //Estado del examen
                    .append("lab57.lab57c39, ") //Fecha Toma
                    .append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, ")
                    .append("lab10.lab10c1, lab10.lab10c2, ")
                    .append("lab19.lab19c1, lab19.lab19c2, lab19.lab19c3, ")
                    .append("lab904.lab904c1, lab904.lab904c2, lab904.lab904c3, ")
                    .append("lab14.lab14c1, lab14.lab14c3, ")
                    .append("a.lab24C1 AS sampleIdMicro, ")
                    .append("lab158.lab158C1 AS anatomicalSite, ")
                    .append("lab16.lab16c3, lab11.lab11c1 ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM  ").append(lab57).append(" as lab57 ")
                    .append("INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ")
                    .append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ")
                    .append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")// Muestra
                    .append("LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ") // Laboratorio
                    .append("LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                    .append("LEFT JOIN lab24 a ON lab57.lab24C1 = a.lab24C1 ")
                    .append("LEFT JOIN lab158 ON lab57.lab158C1 = lab158.lab158C1 ")
                    .append("LEFT JOIN lab10 ON lab22.lab10C1 = lab10.lab10c1   ")
                    .append("LEFT JOIN lab19 ON lab22.lab19C1 = lab19.lab19c1   ")
                    .append("LEFT JOIN lab14 ON lab22.lab14C1 = lab14.lab14c1   ")
                    .append("LEFT JOIN lab904 ON lab22.lab904C1 = lab904.lab904c1   ")
                    .append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

            demographics.stream().forEach((demographic)
                    ->
            {
                if (demographic.isEncoded())
                {
                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id");
                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code");
                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name");
                    if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    } else
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    }
                } else
                {
                    query.append(", lab_demo_").append(demographic.getId());
                }
            });

            from.append(" WHERE lab22.lab22c1 = ").append(orderNumber).append(" AND lab39.lab39c37 = 0 ");
            if (idSample != null)
            {
                from.append(" AND lab39.lab24c1 = ").append(idSample);
            }
            if (laboratorys != null)
            {
                from.append(" AND lab57.lab40c1 IN(").append(laboratorys).append(")");
            }

           
            RowCallbackHandler handler = (ResultSet rs)
                    ->
            {
                Order order = new Order(rs.getLong("lab22c1"));
                if (!orders.contains(order))
                {
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setTests(new ArrayList<>());
                    order.setComments(new ArrayList<>());
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setSex(new Item(rs.getInt("lab80c1")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setDemographics(new ArrayList<>());
                    //SEDE
                    order.getBranch().setId(rs.getInt("lab05c1"));
                    order.getBranch().setCode(rs.getString("lab05c10"));
                    order.getBranch().setName(rs.getString("lab05c4"));

                    order.getType().setId(rs.getInt("lab103c1"));
                    order.getType().setCode(rs.getString("lab103c2"));
                    order.getType().setName(rs.getString("lab103c3"));

                    if (rs.getString("lab10c1") != null)
                    {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (rs.getString("lab19c1") != null)
                    {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (rs.getString("lab14c1") != null)
                    {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (rs.getString("lab904c1") != null)
                    {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    //DEMOGRAFICOS
                    order.setDemographics(new ArrayList<>());
                    for (Demographic demographic : demographics)
                    {
                        DemographicValue demoValue = new DemographicValue();
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
                        if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    orders.add(order);
                }
                order = orders.get(orders.indexOf(order));
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setAction(action == null ? "" : action);
                test.setService(rs.getString("lab10c2"));
                test.setSampleIdMicro(rs.getString("sampleIdMicro"));
                test.setAnatomicalSite(rs.getString("anatomicalSite"));
                test.setTestState(rs.getInt("lab57c8"));
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setCode(rs.getInt("lab40c2"));
                laboratory.setName(rs.getString("lab40c3"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                laboratory.setMiddleware(rs.getBoolean("lab40c13"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                order.getTests().add(test);
            };

            getJdbcTemplate().query(query.toString() + from.toString(), handler);
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la orden para envio al middleware.
     *
     *
     * @param orders
     * @param demographics lista de demograficos
     * @param action
     * @param laboratorys
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> orderResultListRemision(RemissionLaboratory orders, List<Demographic> demographics, String action) throws Exception
    {
        try
        {
            List<Order> orderslist = new ArrayList<>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("lab05.lab05c1, lab05c10, lab05c4, ")
                    .append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, ")
                    .append("lab22.lab22c1, lab22.lab22c4, lab22.lab22c5, lab22.lab22c7, lab22.lab103c1, ")
                    .append("lab24.lab24c1 AS ltlab24c1, ") //muestra id
                    .append("lab24.lab24c2 AS ltlab24c2, ") //muestra nombre
                    .append("lab24.lab24c9 AS ltlab24c9, ") //muestra codigo
                    .append("lab24.lab24c10, ") //tipo laboratorio
                    .append("lab39.lab39c1, ") //examen
                    .append("lab39.lab39c2, ") //código
                    .append("lab39.lab39c4, ") //nombre
                    .append("lab40.lab40c1, ") //id del laboratorio
                    .append("lab40.lab40c10, ") //url del laboratorio
                    .append("lab40.lab40c2, ") //codigo del laboratorio
                    .append("lab40.lab40c3, ") //nombre del laboratorio
                    .append("lab40.lab40c11, ") //Si se valida al ingreso
                    .append("lab40.lab40c12, ") //Si se valida en verificacion
                    .append("lab40.lab40c13, ") //Si se envía a Middleware externo
                    .append("lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ")
                    .append("lab57.lab57c8, ") //Estado del examen
                    .append("lab57.lab57c16, ") //Estado de la muestra
                    .append("lab57.lab57c39, ") //Fecha Toma
                    .append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, ")
                    .append("lab10.lab10c1, lab10.lab10c2, ")
                    .append("lab19.lab19c1, lab19.lab19c2, lab19.lab19c3, ")
                    .append("lab904.lab904c1, lab904.lab904c2, lab904.lab904c3, ")
                    .append("lab14.lab14c1, lab14.lab14c3, ")
                    .append("a.lab24C1 AS sampleIdMicro, ")
                    .append("lab158.lab158C1 AS anatomicalSite, ")
                    .append("lab16.lab16c3, lab11.lab11c1 ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ")
                    .append("INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ")
                    .append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")// Muestra
                    .append("LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ") // Laboratorio
                    .append("LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                    .append("LEFT JOIN lab24 a ON lab57.lab24C1 = a.lab24C1 ")
                    .append("LEFT JOIN lab158 ON lab57.lab158C1 = lab158.lab158C1 ")
                    .append("LEFT JOIN lab10 ON lab22.lab10C1 = lab10.lab10c1   ")
                    .append("LEFT JOIN lab19 ON lab22.lab19C1 = lab19.lab19c1   ")
                    .append("LEFT JOIN lab14 ON lab22.lab14C1 = lab14.lab14c1   ")
                    .append("LEFT JOIN lab904 ON lab22.lab904C1 = lab904.lab904c1   ")
                    .append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

            demographics.stream().forEach((demographic)
                    ->
            {
                if (demographic.isEncoded())
                {
                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id");
                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code");
                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name");
                    if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    } else
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    }
                } else
                {
                    query.append(", lab_demo_").append(demographic.getId());
                }
            });

            from.append(" WHERE Lab39.lab39c37 = 0 AND (");
            from.append("  (lab22.lab22c1 = ").append(orders.getOrders().get(0).getOrderNumber()).append(" AND lab57.lab39c1 in (").append(orders.getOrders().get(0).getTests().stream().map(idTest -> String.valueOf(idTest.getId())).collect(Collectors.joining(","))).append(")) )");

            for (int i = 1; i < orders.getOrders().size(); i++)
            {
                from.append("  (lab22.lab22c1 = ").append(orders.getOrders().get(i).getOrderNumber()).append(" AND lab57.lab39c1 in (").append(orders.getOrders().get(i).getTests().stream().map(idTest -> String.valueOf(idTest.getId())).collect(Collectors.joining(","))).append(")) )");
            }

            from.append(" AND lab57.lab40c1 IN(").append(orders.getLaboratory()).append(")");

            from.append(" AND lab57.lab57c8 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());
            RowCallbackHandler handler = (ResultSet rs)
                    ->
            {
                Order order = new Order(rs.getLong("lab22c1"));
                if (!orderslist.contains(order))
                {
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setTests(new ArrayList<>());
                    order.setComments(new ArrayList<>());
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setSex(new Item(rs.getInt("lab80c1")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setDemographics(new ArrayList<>());
                    //SEDE
                    order.getBranch().setId(rs.getInt("lab05c1"));
                    order.getBranch().setCode(rs.getString("lab05c10"));
                    order.getBranch().setName(rs.getString("lab05c4"));

                    order.getType().setId(rs.getInt("lab103c1"));

                    if (rs.getString("lab10c1") != null)
                    {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (rs.getString("lab19c1") != null)
                    {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (rs.getString("lab14c1") != null)
                    {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (rs.getString("lab904c1") != null)
                    {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    //DEMOGRAFICOS
                    order.setDemographics(new ArrayList<>());
                    for (Demographic demographic : demographics)
                    {
                        DemographicValue demoValue = new DemographicValue();
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
                        if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    orderslist.add(order);
                }
                order = orderslist.get(orderslist.indexOf(order));
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setAction(action == null ? "" : action);
                test.setService(rs.getString("lab10c2"));
                test.setSampleIdMicro(rs.getString("sampleIdMicro"));
                test.setAnatomicalSite(rs.getString("anatomicalSite"));
                test.setTestState(rs.getInt("lab57c8"));
                test.setSampleState(rs.getInt("lab57c16"));
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setCode(rs.getInt("lab40c2"));
                laboratory.setName(rs.getString("lab40c3"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                laboratory.setMiddleware(rs.getBoolean("lab40c13"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                order.getTests().add(test);
            };

            getJdbcTemplate().query(query.toString() + from.toString(), handler);
            return orderslist;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la orden para envio al middleware.
     *
     *
     * @param resultFilter
     * @param demographics lista de demograficos
     * @param action
     * @param laboratorys
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> orderResultRange(ResultFilter resultFilter, List<Demographic> demographics, String action, String laboratorys) throws Exception
    {
        try
        {
            List<Order> orders = new ArrayList<>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("lab05.lab05c1, lab05c10, lab05c4, ")
                    .append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, ")
                    .append("lab22.lab22c1, lab22.lab22c4, lab22.lab22c5, lab22.lab22c7, lab10c1, ")
                    .append("lab24.lab24c1 AS ltlab24c1, ") //muestra id
                    .append("lab24.lab24c2 AS ltlab24c2, ") //muestra nombre
                    .append("lab24.lab24c9 AS ltlab24c9, ") //muestra codigo
                    .append("lab24.lab24c10, ") //tipo laboratorio
                    .append("lab39.lab39c1, ") //examen
                    .append("lab39.lab39c2, ") //código
                    .append("lab39.lab39c4, ") //nombre
                    .append("lab40.lab40c1, ") //id del laboratorio
                    .append("lab40.lab40c2, ") //codigo del laboratorio
                    .append("lab40.lab40c3, ") //nombre del laboratorio
                    .append("lab40.lab40c10, ") //url del laboratorio
                    .append("lab40.lab40c11, ") //Si se valida al ingreso
                    .append("lab40.lab40c12, ") //Si se valida en verificacion
                    .append("lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ")
                    .append("lab57.lab57c8, ") //Estado del examen
                    .append("lab57.lab57c39, ") //Fecha Toma
                    .append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, ")
                    .append("lab16.lab16c3, lab11.lab11c1 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ")
                    .append("INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")// Muestra
                    .append("LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ") // Laboratorio
                    .append("LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                    .append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
            demographics.stream().forEach((demographic)
                    ->
            {
                if (demographic.isEncoded())
                {
                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id");
                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code");
                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name");
                    if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    } else
                    {
                        from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON Lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(+demographic.getId()).append(".lab63c1");
                    }
                } else
                {
                    query.append(", lab_demo_").append(demographic.getId());
                }
            });
            //Si es 0 -> por fecha y si es 1  es por ordenes
            if (resultFilter.getFilterId() == 0)
            {
                from.append(" WHERE lab57.lab57c34 BETWEEN ").append(resultFilter.getFirstDate()).append(" AND ").append(resultFilter.getLastDate()).append(" AND lab39.lab39c37 = 0 ");
            } else
            {
                from.append(" WHERE lab22.lab22c1 BETWEEN ").append(resultFilter.getFirstOrder()).append(" AND ").append(resultFilter.getLastOrder()).append(" AND lab39.lab39c37 = 0 ");
            }
            if (laboratorys != null)
            {
                from.append(" AND lab57.lab40c1 IN(").append(laboratorys).append(") ");
            }
            from.append(" AND lab57.lab57c8 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());
            RowCallbackHandler handler = (ResultSet rs)
                    ->
            {
                Order order = new Order(rs.getLong("lab22c1"));
                if (!orders.contains(order))
                {
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setTests(new ArrayList<>());
                    order.setComments(new ArrayList<>());
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setSex(new Item(rs.getInt("lab80c1")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setDemographics(new ArrayList<>());
                    //SEDE
                    order.getBranch().setId(rs.getInt("lab05c1"));
                    order.getBranch().setCode(rs.getString("lab05c10"));
                    order.getBranch().setName(rs.getString("lab05c4"));

                    //Servicio
                    order.getService().setId(rs.getInt("lab10c1"));

                    //DEMOGRAFICOS
                    order.setDemographics(new ArrayList<>());
                    for (Demographic demographic : demographics)
                    {
                        DemographicValue demoValue = new DemographicValue();
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
                        if (demographic.getOrigin().equals(LISEnum.OrderOrigin.ORDER.getValue()))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    orders.add(order);
                }
                order = orders.get(orders.indexOf(order));
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setAction(action == null ? "" : action);
                test.setTestState(rs.getInt("lab57c8"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                laboratory.setCode(rs.getInt("lab40c2"));
                laboratory.setName(rs.getString("lab40c3"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                order.getTests().add(test);
            };
            getJdbcTemplate().query(query.toString() + from.toString(), handler);
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param order Numero de la orden
     * @param action Accion con la cual van a quedar los examenes
     * @param idSample Id de la muestra
     * @param laboratorys Ids de los laboratorios separados por comas
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> allTestByOrder(long order, String action, String idSample, String laboratorys) throws Exception
    {
        try
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57.lab22c1")
                    .append(", lab57.lab39c1") //examen
                    .append(", lab57.lab57c39") //Fecha Toma
                    .append(", lab39.lab39c2") //código
                    .append(", lab39.lab39c4") //nombre
                    .append(", lab39.lab39c62") //cpt
                    .append(", lab40.lab40c1") //id del laboratorio
                    .append(", lab40.lab40c10") //url del laboratorio
                    .append(", lab40.lab40c11") //Si se valida al ingreso
                    .append(", lab40.lab40c12") //Si se valida en verificacion
                    .append(", lt.lab24c1 AS ltlab24c1") //muestra id
                    .append(", lt.lab24c9 AS ltlab24c9") //muestra codigo
                    .append(", lt.lab24c2 AS ltlab24c2") //muestra nombre
                    .append(", lt.lab24c10") //tipo laboratorio
                    .append(", lab16.lab16c3, lab11.lab11c1 ")
                    .append(" FROM ").append(lab57).append(" as lab57 ") //Resultados
                    .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1") //Examen
                    .append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1") // Laboratorio
                    .append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1") // Muestra                    
                    .append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ")
                    .append(" WHERE lab39.lab39c37 = 0")
                    .append(" AND lab57.lab22c1 = ? ");
            if (idSample != null)
            {
                query.append(" AND lab39.lab24c1 = ").append(idSample);
            }
            if (laboratorys != null)
            {
                query.append(" AND lab57.lab40c1 IN(").append(laboratorys).append(")");
            }
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setAction(action == null ? "" : action);
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                test.setCpt(rs.getString("lab39c62") != null ? rs.getString("lab39c62") : "");
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                return test;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta todos los antibioticos registrados y los retorna en una lista.
     *
     * @return
     * @throws Exception
     */
    default List<AntibioticMiddleware> listAntibiotics() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab79c1")
                    .append(", lab79c2")
                    .append(", lab07c1")
                    .append(" FROM lab79");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                AntibioticMiddleware antibioticMiddleware = new AntibioticMiddleware();
                antibioticMiddleware.setId(rs.getInt("lab79c1"));
                antibioticMiddleware.setName(rs.getString("lab79c2"));
                antibioticMiddleware.setActive(rs.getBoolean("lab07c1"));
                return antibioticMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta los sitios anatómicos para enviarlos al middleware
     *
     * @return
     * @throws Exception
     */
    default List<AnatomicalSiteMiddleware> listAnatomicalSite() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab158c1")
                    .append(", lab158c2")
                    .append(", lab158c3")
                    .append(", lab07c1")
                    .append(" FROM lab158");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                AnatomicalSiteMiddleware anatomicalSiteMiddleware = new AnatomicalSiteMiddleware();
                anatomicalSiteMiddleware.setId(rs.getInt("lab158c1"));
                anatomicalSiteMiddleware.setName(rs.getString("lab158c2"));
                anatomicalSiteMiddleware.setCode(rs.getString("lab158c3"));
                anatomicalSiteMiddleware.setActive(rs.getBoolean("lab07c1"));
                return anatomicalSiteMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta a la tabla de microorganismos
     *
     * @return Lista de microorganismos
     * @throws Exception Error en base de datos
     */
    default List<MicroorganismsMiddleware> listMicroorganisms() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab76c1")
                    .append(", lab76c2")
                    .append(", lab07c1")
                    .append(" FROM lab76");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                MicroorganismsMiddleware microorganismsMiddleware = new MicroorganismsMiddleware();
                microorganismsMiddleware.setId(rs.getInt("lab76c1"));
                microorganismsMiddleware.setName(rs.getString("lab76c2"));
                microorganismsMiddleware.setActive(rs.getBoolean("lab07c1"));
                return microorganismsMiddleware; //Retorno el objeto cargado
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * De este espacio para abajo habra una serie de consultas para demograficos
     * items dependiendo la constante que sea retornada
     *
     * @param demographic
     * @return
     * @throws Exception Error en la base de datos
     */
    default List<DemographicItemMiddleware> listDemographicItem_default(Integer demographic) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab63c1")
                    .append(", lab63c2")
                    .append(", lab63c3")
                    .append(", lab07c1")
                    .append(" FROM lab63")
                    .append(" WHERE lab62c1 = ")
                    .append(demographic);
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab63c1"));
                demographicItemMiddleware.setCode(rs.getString("lab63c2"));
                demographicItemMiddleware.setName(rs.getString("lab63c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_ACCOUNT() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab14c1")
                    .append(", lab14c2")
                    .append(", lab14c3")
                    .append(", lab07c1")
                    .append(" FROM lab14");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab14c1"));
                demographicItemMiddleware.setCode(rs.getString("lab14c2"));
                demographicItemMiddleware.setName(rs.getString("lab14c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_PHYSICIAN() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab19c1")
                    .append(", lab19c22")
                    .append(", lab19c2")
                    .append(", lab07c1")
                    .append(" FROM lab19");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab19c1"));
                demographicItemMiddleware.setCode(rs.getString("lab19c22"));
                demographicItemMiddleware.setName(rs.getString("lab19c2"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_RATE() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab904c1")
                    .append(", lab904c2")
                    .append(", lab904c3")
                    .append(", lab07c1")
                    .append(" FROM lab904");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab904c1"));
                demographicItemMiddleware.setCode(rs.getString("lab904c2"));
                demographicItemMiddleware.setName(rs.getString("lab904c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_ORDERTYPE() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab103c1")
                    .append(", lab103c2")
                    .append(", lab103c3")
                    .append(", lab07c1")
                    .append(" FROM lab103");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab103c1"));
                demographicItemMiddleware.setCode(rs.getString("lab103c2"));
                demographicItemMiddleware.setName(rs.getString("lab103c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_BRANCH() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05c1")
                    .append(", lab05c10")
                    .append(", lab05c4")
                    .append(", lab07c1")
                    .append(" FROM lab05");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab05c1"));
                demographicItemMiddleware.setCode(rs.getString("lab05c10"));
                demographicItemMiddleware.setName(rs.getString("lab05c4"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_SERVICE() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab10c1")
                    .append(", lab10c7")
                    .append(", lab10c2")
                    .append(", lab07c1")
                    .append(" FROM lab10");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab10c1"));
                demographicItemMiddleware.setCode(rs.getString("lab10c7"));
                demographicItemMiddleware.setName(rs.getString("lab10c2"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_RACE() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab08c1")
                    .append(", lab08c5")
                    .append(", lab08c2")
                    .append(", lab07c1")
                    .append(" FROM lab08");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab08c1"));
                demographicItemMiddleware.setCode(rs.getString("lab08c5"));
                demographicItemMiddleware.setName(rs.getString("lab08c2"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_DOCUMENT_TYPE() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab54c1")
                    .append(", lab54c2")
                    .append(", lab54c3")
                    .append(", lab07c1")
                    .append(" FROM lab54");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab54c1"));
                demographicItemMiddleware.setCode(rs.getString("lab54c2"));
                demographicItemMiddleware.setName(rs.getString("lab54c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<DemographicItemMiddleware> listDemographicItem_AGE_GROUP() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab13c1")
                    .append(", lab13c2")
                    .append(", lab13c3")
                    .append(", lab07c1")
                    .append(" FROM lab13");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItemMiddleware demographicItemMiddleware = new DemographicItemMiddleware();
                demographicItemMiddleware.setId(rs.getInt("lab13c1"));
                demographicItemMiddleware.setCode(rs.getString("lab13c2"));
                demographicItemMiddleware.setName(rs.getString("lab13c3"));
                demographicItemMiddleware.setActive(rs.getBoolean("lab07c1"));
                return demographicItemMiddleware;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de examenes con valores de referencia para ser envaidos
     * al middleware
     *
     * @return Lista de examenes para el middleware
     * @throws Exception Error en el servicio
     */
    default List<TestToMiddleware> deltacheck() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c1, ")
                    .append("lab39c2 AS code, ")
                    .append("lab39c3 AS abbr, ")
                    .append("lab39c4 AS nameTest, ")
                    .append("lab39c44 AS DeltaDays, ")
                    .append("lab39c45 AS DeltaMin, ")
                    .append("lab39c46 AS DeltaMax ")
                    .append("FROM lab39 ");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                TestToMiddleware test = new TestToMiddleware();
                DeltaCheckMiddleware delta = new DeltaCheckMiddleware();
                test.setId(rs.getInt("lab39c1"));
                test.setName(rs.getString("nameTest"));
                test.setCode(rs.getString("code"));
                test.setAbbr(rs.getString("abbr"));
                test.setAbbr(rs.getString("abbr"));
                delta.setDays(rs.getInt("DeltaDays"));
                delta.setMin(rs.getInt("DeltaMin"));
                delta.setMax(rs.getInt("DeltaMax"));
                test.setDeltaCheck(delta);
                return test;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de los valor de referencia por el id de la prueba con
     * propiedades requeridas por el middleware
     *
     * @param idTest
     * @return Lista de examenes para el middleware
     * @throws Exception Error en el servicio
     */
    default List<ReferenceValueMiddleware> referenceValueToMiddleware(int idTest) throws Exception
    {
        try
        {
            String query = "SELECT lab48.lab48c1 AS lab48c1, "
                    + "lab04.lab04c4 AS analyzerName, "
                    + "(SELECT lab80c5 FROM lab80 WHERE lab80c1 = lab48.lab48c10) AS gender, "
                    + "lab48.lab48c2 AS age, "
                    + "lab48.lab48c3 AS minAge, "
                    + "lab48.lab48c4 AS maxAge, "
                    + "lab48.lab48c5 AS minPanic, "
                    + "lab48.lab48c6 AS maxPanic,  "
                    + "(SELECT lab50c2 FROM lab50 WHERE lab50c1 = lab48.lab50c1_3) AS textReference, "
                    + "lab48.lab48c12 AS minReference, "
                    + "lab48.lab48c13 AS maxReference "
                    + "FROM lab48 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab48.lab39c1 "
                    + "LEFT JOIN lab04 ON lab48.lab48c19 = lab04.lab04c1 "
                    + "WHERE lab39.lab39c1 = ? AND lab48.lab48c17 = 1";

            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i) ->
            {
                ReferenceValueMiddleware referencesValue = new ReferenceValueMiddleware();
                referencesValue.setAnalyzerName(rs.getString("analyzerName"));
                referencesValue.setId(rs.getInt("lab48c1"));
                referencesValue.setGender(rs.getString("gender"));
                referencesValue.setAge(rs.getInt("age") == 1 ? "Years" : "Days");
                referencesValue.setMinAge(rs.getInt("minAge"));
                referencesValue.setMaxAge(rs.getInt("maxAge"));
                referencesValue.setMinPanic(rs.getBigDecimal("minPanic"));
                referencesValue.setMaxPanic(rs.getBigDecimal("maxPanic"));
                referencesValue.setTextReference(rs.getString("textReference"));
                referencesValue.setMinReference(rs.getBigDecimal("minReference"));
                referencesValue.setMaxReference(rs.getBigDecimal("maxReference"));
                return referencesValue;
            }, idTest);
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene un id de usuario analizador por examen y valores de referencia de
     * dicho examen
     *
     * @param idReferenceValue
     * @return Id del usuario analizador
     * @throws Exception Error en el servicio
     */
    default Integer getAnalyzerUserId(int idReferenceValue) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab48c19 ")
                    .append("FROM lab48 ")
                    .append("WHERE lab48c1 = ").append(idReferenceValue);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getObject("lab48c19") == null ? 0 : rs.getInt("lab48c19");
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    default List<ReferenceValue> referenceValuesByUserAnalyzer(int idTest, int idUserAnalyzer) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab48c1, ")
                    .append("lab48c2, ")
                    .append("lab48c3, ")
                    .append("lab48c4, ")
                    .append("lab48c5, ")
                    .append("lab48c6, ")
                    .append("lab48c12, ")
                    .append("lab48c13, ")
                    .append("lab48c14, ")
                    .append("lab48c15, ")
                    .append("lab50c1_1, ")
                    .append("lab50c1_3, ")
                    .append("lab80c1, ")
                    .append("lab80c2, ")
                    .append("lab80c3, ")
                    .append("lab80c4, ")
                    .append("lab80c5,  ")
                    .append("lab48c17  ")
                    .append("FROM lab48 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab48.lab48c10 ")
                    .append("WHERE lab39c1 = ").append(idTest)
                    .append(" AND lab48c17 = 1")
                    .append(" AND lab48c19 = ").append(idUserAnalyzer);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ReferenceValue referenceValue = new ReferenceValue();

                referenceValue.setId(rs.getInt("lab48c1"));
                referenceValue.setUnitAge(rs.getShort("lab48c2"));
                referenceValue.setAgeMin(rs.getInt("lab48c3"));
                referenceValue.setAgeMax(rs.getInt("lab48c4"));
                referenceValue.setPanicMin(rs.getString("lab48c5") == null ? null : rs.getBigDecimal("lab48c5"));
                referenceValue.setPanicMax(rs.getString("lab48c6") == null ? null : rs.getBigDecimal("lab48c6"));
                referenceValue.setNormalMin(rs.getString("lab48c12") == null ? null : rs.getBigDecimal("lab48c12"));
                referenceValue.setNormalMax(rs.getString("lab48c13") == null ? null : rs.getBigDecimal("lab48c13"));
                referenceValue.setReportableMin(rs.getString("lab48c14") == null ? null : rs.getBigDecimal("lab48c14"));
                referenceValue.setReportableMax(rs.getString("lab48c15") == null ? null : rs.getBigDecimal("lab48c15"));
                /*Genero*/
                referenceValue.getGender().setId(rs.getInt("lab80c1"));
                referenceValue.getGender().setIdParent(rs.getInt("lab80c2"));
                referenceValue.getGender().setCode(rs.getString("lab80c3"));
                referenceValue.getGender().setEsCo(rs.getString("lab80c4"));
                referenceValue.getGender().setEnUsa(rs.getString("lab80c5"));
                referenceValue.setState(rs.getInt("lab07c1") == 1);
                /*Resultado Literal Panico*/
                referenceValue.getPanic().setId(rs.getString("lab50c1_1") == null ? null : rs.getInt("lab50c1_1"));
                /*Resultado Literal Normal*/
                referenceValue.getNormal().setId(rs.getString("lab50c1_3") == null ? null : rs.getInt("lab50c1_3"));
                return referenceValue;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene los mensajes de órdenes para enviar al sistema del Middleware
     *
     * @return Lista de
     * net.cltech.enterprisent.domain.integration.middleware.SendAstmMiddleware
     * @throws Exception
     */
    default List<SendAstmMiddleware> getOrdersControlTable() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT cont02c1, ")
                    .append("cont02c2, ")
                    .append("cont02c3, ")
                    .append("cont02c4, ")
                    .append("FROM cont02 ");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                SendAstmMiddleware sendAstmMiddleware = new SendAstmMiddleware();
                sendAstmMiddleware.setLaboratoryRoute(rs.getString("cont02c1"));
                sendAstmMiddleware.setRestServiceRoute(rs.getString("cont02c2"));
                sendAstmMiddleware.setMessageASTM(rs.getString("cont02c3"));
                sendAstmMiddleware.setIndicator(rs.getInt("cont02c4"));

                return sendAstmMiddleware;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Insert el mensaje de una orden no enviada al Middleware, para un envío
     * posterior
     *
     * @param asmt
     * @return
     * @throws Exception
     */
    default boolean insertASTMInControlTable(SendAstmMiddleware asmt) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplateCont())
                    .withTableName("cont02")
                    .usingColumns("cont02c1", "cont02c2", "cont02c3", "cont02c4");

            HashMap parameters = new HashMap();
            parameters.put("cont02c1", asmt.getLaboratoryRoute());
            parameters.put("cont02c2", asmt.getRestServiceRoute());
            parameters.put("cont02c3", asmt.getMessageASTM());
            parameters.put("cont02c4", asmt.getIndicator());

            return insert.execute(parameters) == 1;
        } catch (Exception e)
        {
            return false;
        }
    }

    default int[] deleteControlTableOrders(List<SendAstmMiddleware> records) throws Exception
    {
        return getJdbcTemplate().batchUpdate(""
                + "DELETE   "
                + "FROM     cont02 "
                + "WHERE    cont02c3 = ? "
                + "", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                SendAstmMiddleware record = records.get(i);
                ps.setString(1, record.getMessageASTM());
            }

            @Override
            public int getBatchSize()
            {
                return records.size();
            }
        });
    }

    /**
     * Obtiene los examenes asignados a una orden con algunos datos basicos pero
     * necesarios
     *
     * @param idOrder
     * @return Lista de examenes pertenecientes a esa orden
     * @throws Exception
     */
    default List<Test> getTestWhitVolumes(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1 AS lab39c1, ")
                    .append("lab39.lab39c2 AS lab39c2, ")
                    .append("lab39.lab39c3 AS lab39c3, ")
                    .append("lab39.lab39c4 AS lab39c4, ")
                    .append("lab24.lab24c1 AS lab24c1, ")
                    .append("lab24.lab24c9 AS lab24c9, ")
                    .append("lab39.lab39c13 AS lab39c13, ")
                    .append("lab39.lab39c53 AS lab39c53 ")
                    .append("FROM lab57 ")
                    .append("JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ")
                    .append("WHERE lab57.lab22c1 = ").append(idOrder)
                    .append(" AND lab39.lab39c37 = 0");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.getSample().setId(rs.getInt("lab24c1"));
                test.getSample().setCodesample(rs.getString("lab24c9"));
                test.setVolume(rs.getString("lab39c13"));
                test.setLicuota(rs.getInt("lab39c53") == 1);
                return test;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }
}
