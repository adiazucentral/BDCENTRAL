package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderHisPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.PatientPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.QuestionPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.ResultsTestPendingResults;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a la base de datos para la informacion de
 * los resultados para minsa
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 29/04/2022
 * @see Creación
 */
public interface IntegrationMinsaDao
{

    public JdbcTemplate getConnectionStat();

    public JdbcTemplate getConnection();

    default List<OrderPendingResults> pendingResultsOrdersEpi(int centralSytem, List<Demographic> demographics, int days, int orderdigits) throws Exception
    {
        try
        {
            HashMap<String, OrderPendingResults> results = new HashMap<>();
            String select = " SELECT Lab57.Lab22C1 as Orden \n"
                    + ",lab21.lab21c2 as hPatient \n"
                    + ",lab21.lab21c3 as name1 \n"
                    + ",lab21.lab21c4 as name2 \n"
                    + ",lab21.lab21c5 as firstName \n"
                    + ",lab21.lab21c6 as lastName \n"
                    + ",lab80.lab80c4 as sex \n"
                    + ",lab21c7 as birthDay \n "
                    + ",lab103.lab103c2 as typeOrderCode \n "
                    + ",lab103.lab103c3 as typeOrderName \n "
                    + ",lab57c2 as dateResult \n"
                    + ",Lab21.lab21c1 as idPatient \n "
                    + ",Lab54c2 as CodTipeDocument \n "
                    + ",Lab54c3 as NameTipeDocument \n "
                    + ",lab57c1 AS Resultado \n"
                    + ",Lab57.Lab57C3 AS FResultado \n "
                    + ",Lab57.Lab57C18 AS FValidacion \n "
                    + ",lab61.lab61c1 AS CodHIS \n"
                    + ",lab39.lab39c1 AS IdExamen \n"
                    + ",lab39.lab39c2 AS CodExamen \n"
                    + ",Lab57.Lab57c9 AS Patologico \n"
                    + ",Lab95.Lab95C1 AS Comentario \n"
                    + ",lab22.lab05c1 AS idSede \n"
                    + ",lab05.lab05c4 AS nameSede \n"
                    + ",lab22.lab19c1 AS idMedico \n"
                    + ",lab19.lab19c2 AS nameMedico \n";
            String from = " FROM	Lab57 \n"
                    + " LEFT JOIN Lab39  ON Lab57.Lab39C1 = Lab39.Lab39C1 \n"
                    + " LEFT JOIN Lab22 ON Lab57.Lab22C1 = Lab22.Lab22C1 \n"
                    + " LEFT JOIN Lab21 ON Lab21.Lab21C1 = Lab22.Lab21C1 \n"
                    + " LEFT JOIN Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 \n"
                    + " INNER JOIN Lab61 ON Lab57.Lab39C1 = Lab61.Lab39C1 AND Lab61.Lab118C1 =" + centralSytem + " \n"
                    + " LEFT JOIN Lab95 ON Lab57.lab22c1 = Lab95.lab22c1 and lab95.lab39c1 = lab57.lab39c1 \n"
                    + " LEFT JOIN Lab80 ON lab21.lab80c1 = lab80.lab80c1 \n"
                    + " LEFT JOIN Lab103 ON lab103.lab103c1 = lab22.lab103c1 \n"
                    + " LEFT JOIN Lab05 ON lab22.lab05c1 = lab05.lab05c1 \n"
                    + " LEFT JOIN Lab19 ON lab22.lab19c1 = lab19.lab19c1 \n"
                    + " LEFT JOIN lab191 ON lab191.lab22c1 = lab57.lab22c1 AND lab191.lab39c1  = lab39.lab39c1 and lab191.lab118c1 = " + centralSytem + " \n";

            String where = " WHERE Lab57.Lab57C8 > 3 "
                    + " AND lab191.lab22c1 IS NULL"
                    + " AND Lab22.Lab07C1 = 1  AND (lab22c19 = 0 or lab22c19 is null) \n";

            for (Demographic demographic : demographics)
            {
                String demoTypeTable = "Lab22";
                if (demographic.getSource().equals("H"))
                {
                    demoTypeTable = "Lab21";
                }
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON " + demoTypeTable + ".lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", " + demoTypeTable + ".lab_demo_" + demographic.getId();
                }

            }

            String between = " AND Lab57.Lab22C1 BETWEEN " + Tools.buildInitialOrder(days, orderdigits) + " AND " + Tools.buildFinalOrder(orderdigits);

            getConnection().query(select + from + where + between, (ResultSet rs, int i)
                    ->
            {
                try
                {
                    OrderPendingResults order;
                    PatientPendingResults patient;
                    ResultsTestPendingResults result;
                    order = new OrderPendingResults(Long.toString(rs.getLong("Orden")));
                    if (!results.containsKey(order.getOrder()))
                    {
                        //Paciente
                        patient = new PatientPendingResults(Tools.decrypt(rs.getString("hPatient").trim()));
                        patient.setNames(Tools.decrypt(rs.getString("name1")));
                        patient.setLastName(Tools.decrypt(rs.getString("lastName")));
                        patient.setFirstName(Tools.decrypt(rs.getString("firstName")));
                        patient.setSex(rs.getString("sex"));
                        patient.setBirthDay(rs.getTimestamp("birthDay"));
                        patient.setTypedocumentCode(rs.getString("CodTipeDocument"));
                        patient.setTypedocumentName(rs.getString("NameTipeDocument"));

                        //Orden
                        order.setPatient(patient);
                        order.setOrderTypeName(rs.getString("typeOrderName"));
                        order.setOrderTypeCode(rs.getString("typeOrderCode"));
                        order.setDemographics(new ArrayList<>());
                        order.setResult(new ArrayList<>());
                        order.setRegist(rs.getTimestamp("dateResult"));
                        order.setIdBranch(rs.getInt("idSede"));
                        order.setNameBranch(rs.getString("nameSede"));
                        order.setIdPhysician(rs.getInt("idMedico"));
                        order.setNamePhysician(rs.getString("nameMedico"));
                        //Entrevistas
                        order.setInterview(interviewOrderResult(order.getOrder()));
                        //Demograficos 
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
                            //VALIDA EL TIPO DE DEMOGRAFICO 
                            if (demographic.getSource().equals("H"))
                            {
                                patient.getDemographics().add(demoValue);
                            } else
                            {
                                order.getDemographics().add(demoValue);
                            }

                        }

                        results.put(order.getOrder(), order);
                    }
                    order = results.get(order.getOrder());
                    //Resultado
                    result = new ResultsTestPendingResults();
                    result.setResult(rs.getString("Resultado") != null ? Tools.decrypt(rs.getString("Resultado")) : "");
                    result.setValidation(rs.getString("Resultado") != null ? rs.getTimestamp("FValidacion") : new Date());
                    result.setRegist(rs.getTimestamp("dateResult"));
                    result.setTestCode(rs.getString("CodHIS"));
                    result.setResultLiteral(rs.getString("Resultado") != null ? Tools.decrypt(rs.getString("Resultado")) : "");
                    result.setId(rs.getInt("IdExamen"));
                    result.setPatologic(rs.getString("Patologico"));
                    result.setComment(rs.getString("Comentario"));
                    order.getResult().add(result);
                    return order;
                } catch (Exception e)
                {
                    return new OrderPendingResults();
                }

            });
            return new ArrayList<>(results.values());
        } catch (DataAccessException e)
        {
            IntegrationHisLog.error(e);
            return new ArrayList<>();
        }
    }

    default List<String> homologationDemographics(int idCentralSystem, int idDemographic, int idDemographicItem)
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(" select lab117c3 from lab117 where lab118c1 = ").append(idCentralSystem)
                    .append(" and lab117c1 = ").append(idDemographic)
                    .append(" and lab117c2 = ").append(idDemographicItem);

            return getConnection().query(select.toString(), (ResultSet rs, int i)
                    ->
            {
                String codeDemo = rs.getString("lab117c3");
                return codeDemo;
            });

        } catch (DataAccessException e)
        {
            IntegrationHisLog.error(e);
            return new ArrayList<>();
        }
    }

    default List<QuestionPendingResults> interviewOrderResult(String order)
    {
        try
        {
            HashMap<String, QuestionPendingResults> interviewsO = new HashMap<>();
            StringBuilder select = new StringBuilder();
            select.append(" select lab23.lab22c1 as numberOrder, ")
                    .append(" lab23.lab70c1 as idQuestion,")
                    .append(" lab70.lab70c3 as question,")
                    .append(" lab70.lab70c4 as typeQuestion,")
                    .append(" lab23.lab23c1 as openAnswer,")
                    .append(" lab90.lab90c2 as closedAnswer")
                    .append(" from lab23")
                    .append(" left join lab70 on lab70.lab70c1 = lab23.lab70c1")
                    .append(" left join lab90 on lab90.lab90c1 = lab23.lab90c1")
                    .append(" where lab23.lab22c1 = ").append(order);

            getConnection().query(select.toString(), (ResultSet rs, int i)
                    ->
            {
                try
                {
                    QuestionPendingResults pendingResults = new QuestionPendingResults();
                    if (!interviewsO.containsKey(rs.getString("numberOrder")))
                    {
                        pendingResults = interviewsO.get(rs.getString("numberOrder"));
                        if (rs.getString("closedAnswer") != null && !rs.getString("closedAnswer").isEmpty())
                        {
                            pendingResults.getClosedAnswer().add(rs.getString("closedAnswer"));
                        }
                    } else
                    {
                        pendingResults.setOrder(rs.getString("numberOrder"));
                        pendingResults.setIdQuestion(rs.getInt("idQuestion"));
                        pendingResults.setQuestion(rs.getString("question"));
                        pendingResults.setTypeQuestion(rs.getString("typeQuestion"));
                        pendingResults.setOpenAnswer(rs.getString("openAnswer"));
                        if (rs.getString("closedAnswer") != null && !rs.getString("closedAnswer").isEmpty())
                        {
                            pendingResults.getClosedAnswer().add(rs.getString("closedAnswer"));
                        }

                        interviewsO.put(rs.getString("numberOrder"), pendingResults);
                    }
                    return pendingResults;

                } catch (SQLException ex)
                {
                    IntegrationHisLog.error(ex);
                    return new ArrayList<>();
                }
            });
            return new ArrayList<>(interviewsO.values());

        } catch (DataAccessException e)
        {
            IntegrationHisLog.error(e);
            return new ArrayList<>();
        }
    }

    default boolean insertOrderHis(List<OrderHisPendingResults> hisPendingResults)
    {
        try
        {

            boolean result = true;
            for (OrderHisPendingResults hisPendingResult : hisPendingResults)
            {
                SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getConnection());
                insertJdbc.withTableName("lab191");
                HashMap parameters = new HashMap();
                parameters.put("lab118c1", hisPendingResult.getIdCentralSystem());
                parameters.put("lab39c1", hisPendingResult.getIdTest());
                parameters.put("lab191c1", 0);
                parameters.put("lab22c1", hisPendingResult.getOrder());
                parameters.put("lab191c2", 1);
                parameters.put("lab191c3", new Date());
                result = insertJdbc.execute(parameters) >= 1;
                if (!result)
                {
                    IntegrationHisLog.info("ORDER : " + hisPendingResult.getOrder() + " TEST : " + hisPendingResult.getIdTest() + " NOT INSERTED");
                }
            }
            return result;

        } catch (DataAccessException e)
        {
            IntegrationHisLog.error(e);
            return false;
        }
    }

}
