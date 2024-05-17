package net.cltech.enterprisent.dao.interfaces.integration;

import com.google.common.base.Objects;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.domain.integration.resultados.RequestOrdersResultados;
import net.cltech.enterprisent.domain.integration.resultados.RequestUpdateSendResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseDetailMicroorganisms;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderDetailResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderResult;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.operation.demographic.SuperBranch;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.SuperPatient;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.HtmlToTxt;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a la base de datos para la informacion de
 * los demograficos y examenes
 *
 * @version 1.0.0
 * @author javila
 * @since 04/03/2020
 * @see Creación
 */
public interface IntegrationResultadosDao
{

    /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final
     *
     * @param fromOrder
     * @param untilOrder
     * @param type
     * @param centralSystem
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default List<List<Long>> ordersRange(long fromOrder, long untilOrder, int type, Integer centralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT distinct R1.lab22c1 AS lab22c1,")
                    .append("(SELECT COUNT(R2.lab22c1) AS AlreadySent FROM lab57 R2 WHERE R2.lab57c51 IS NOT NULL AND R2.lab22c1 = R1.lab22c1) AS AlreadySent ")
                    .append(" FROM lab57 R1 ")
                    .append(" INNER JOIN lab22 ON lab22.lab22c1 = R1.lab22c1 ");

            if (!Objects.equal(centralSystem, null))
            {
                query.append(" INNER JOIN  lab39 ON lab39.lab39c1 = R1.lab39c1  ")
                        .append(" INNER JOIN Lab61 ON Lab61.lab39c1 = lab39.Lab39C1 and Lab61.lab118c1 = ").append(centralSystem);

            }
            query.append("  WHERE lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab57c50 = 0")
                    .append(" AND R1.lab22c1 >= ").append(fromOrder)
                    .append(" AND R1.lab22c1 <= ").append(untilOrder)
                    .append(" AND lab57c8 >= 3 ");

            if (type != 1)
            {
                query.append(" AND lab57c49 IS NOT NULL ");
            }
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                List<Long> listIdOrder = new ArrayList<>();
                listIdOrder.add(rs.getLong("lab22c1"));
                listIdOrder.add(rs.getLong("AlreadySent"));
                return listIdOrder;
            });

        } catch (Exception e)
        {
            ResultsLog.error(e);
            return null;
        }
    }

    /**
     * Verifica si el paciente uso el servico tribunal electoral
     *
     * @param idPatient
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default int isTribunalElectoral(int idPatient)
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("")
                    .append(" SELECT lab21c1 ")
                    .append(" FROM lab193 ")
                    .append(" WHERE lab21c1 = ").append(idPatient);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return 1;
            });

        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Obtendra una lista de ordenes que tengan examenes validados para ser
     * enviados al HIS
     *
     * @param demographics
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default List<OrderList> orderspendingsendhis(final List<Demographic> demographics) throws Exception
    {

        List<OrderList> listOrders = new LinkedList<>();
        try
        {

            List<String> listOrdersString = new LinkedList<>();
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1 "
                    + " , lab22.lab22c2 "
                    + " , lab22.lab22c7 "
                    + " , lab39.lab39c2 "
                    + " , lab39.lab39c3 "
                    + " , lab39.lab39c4 "
                    + " , lab24.lab24c2 "
                    + " , lab24.lab24c9 "
                    + " , lab57.lab57c1 "
                    + " , lab57.lab57c2 "
                    + " , lab22.lab103c1 "
                    + " , lab103.lab103c2 "
                    + " , lab103.lab103c3 "
                    + " , lab103.lab103c4 "
                    + " , lab22.lab22c3 "
                    + " , lab22.lab21c1 "
                    + " , lab22.lab22c4 "
                    + " , lab22.lab22c5 "
                    + " , lab22.lab22c6 "
                    + " , lab22.lab22c7 "
                    + " , lab22.lab04c1 "
                    + " , lab22.lab22c13 "
                    + " , lab22.lab07c1 "
                    + " , lab22.lab22c7 "
                    + " , lab05.lab05c1 "
                    + " , lab05.lab05c10 "
                    + " , lab05.lab05c11 "
                    + " , lab05.lab05c4 "
                    + " , lab21.lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21c8 "
                    + " , lab21c16 "
                    + " , lab21c17 "
                    + " , lab80.lab80c1 "
                    + " , lab80.lab80c2 "
                    + " , lab80.lab80c3 "
                    + " , lab80.lab80c4 "
                    + " , lab80.lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c14 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab95.lab95c1 ";

            String from = ""
                    + " FROM lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  "
                    + " INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + " LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                    + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 "
                    + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " ";

            for (Demographic demographic : demographics)
            {
                select += Tools.createDemographicsQuery(demographic).get(0);
                from += Tools.createDemographicsQuery(demographic).get(1);
            }

            String where = " WHERE lab57c50 = 0 AND lab57c8 > 3";
            getJdbcTemplate().query(select + from + where, (ResultSet rs, int i)
                    ->
            {
                OrderList order = new OrderList();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setExternalId(rs.getString("lab22c7"));

                TestList test = new TestList();

                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));

                test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                test.getResultComment().setComment(rs.getString("lab95c1"));

                test.getSample().setName(rs.getString("lab24c2"));
                test.getSample().setCodesample(rs.getString("lab24c9"));
                if (!listOrdersString.contains(order.getOrderNumber().toString()))
                {
                    listOrdersString.add(order.getOrderNumber().toString());
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setTurn(rs.getString("lab22c13"));
                    order.setExternalId(rs.getString("lab22c7"));
                    OrderType orderType = new OrderType();
                    orderType.setId(rs.getInt("lab103c1"));
                    orderType.setCode(rs.getString("lab103c2"));
                    orderType.setName(rs.getString("lab103c3"));
                    orderType.setColor(rs.getString("lab103c4"));
                    order.setType(orderType);

                    order.setCreatedDate(rs.getTimestamp("lab22c3"));

                    SuperPatient patient = new SuperPatient();

                    patient.setId(rs.getInt("lab21c1"));
                    patient.setStateTE(isTribunalElectoral(patient.getId()));
                    order.setPatient(patient);
                    //COMENTARIO
                    CommentOrder commentOrder = new CommentOrder();
                    commentOrder.setComment(rs.getString("lab95c1") == null ? "" : rs.getString("lab95c1"));
                    commentOrder.setUser(null);
                    order.getComments().add(commentOrder);
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));

                    order.getPatient().setEmail(rs.getString("lab21c8"));
                    order.getPatient().setPhone(rs.getString("lab21c16"));
                    order.getPatient().setAddress(rs.getString("lab21c17"));
                    //PACIENTE - SEXO
                    order.getPatient().getSex().setId(rs.getInt("lab80c1"));
                    order.getPatient().getSex().setIdParent(rs.getInt("lab80c2"));
                    order.getPatient().getSex().setCode(rs.getString("lab80c3"));
                    order.getPatient().getSex().setEsCo(rs.getString("lab80c4"));
                    order.getPatient().getSex().setEnUsa(rs.getString("lab80c5"));

                    //PACIENTE - TIPO DE DOCUMENTO
                    SuperDocumentType documenttypeLaboratory = new SuperDocumentType();
                    documenttypeLaboratory.setId(rs.getInt("lab54c1"));
                    documenttypeLaboratory.setAbbr(rs.getString("lab54c2"));
                    documenttypeLaboratory.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documenttypeLaboratory);

                    order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                    order.setExternalId(rs.getString("lab22c7"));

                    if (rs.getString("lab05c1") != null)
                    {
                        SuperBranch branch = new SuperBranch();
                        branch.setId(rs.getInt("lab05c1"));
                        branch.setCode(rs.getString("lab05c10"));
                        branch.setName(rs.getString("lab05c4"));
                        branch.setResponsable(rs.getString("lab05c11"));
                        order.setBranch(branch);
                    }

                    List<DemographicValue> demographicsOrder = new LinkedList<>();
                    for (Demographic demographic : demographics)
                    {
                        String[] data;
                        if (demographic.isEncoded())
                        {
                            data = new String[]
                            {
                                rs.getString("demo" + demographic.getId() + "_id"),
                                rs.getString("demo" + demographic.getId() + "_code"),
                                rs.getString("demo" + demographic.getId() + "_name")
                            };
                        } else
                        {
                            data = new String[]
                            {
                                rs.getString("lab_demo_" + demographic.getId())
                            };
                        }
                        demographicsOrder.add(Tools.getDemographicsValue(demographic, data));
                    }
                    order.setAllDemographics(demographicsOrder);
                    listOrders.add(order);
                }
                listOrders.get(listOrdersString.indexOf(order.getOrderNumber().toString())).getTests().add(test);
                return order;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
        return listOrders;
    }

    /**
     * Resultados para la interfaz de resultados por orden y codigo central
     *
     * @param order
     * @param centralSystem
     * @param funtionProfileId Este parametro se encargara de devolver el id del
     * perfil de una manera u otra
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResponseOrderDetailResult> resultsByOrderByCentralCodes(long order, int centralSystem, boolean funtionProfileId) throws Exception
    {

        OrderCreationLog.info("INGRESA A resultsByOrderByCentralCodes :: ");
        try
        {
            String query = ""
                    + "SELECT   lab43.lab43c2 as SectionCode " //area codigo
                    + "       , lab43.lab43c4 as SectionName" //area nombre
                    + "       , lab04.lab04c9 as correoAux" //correo para validar el user code (isnull usercode)
                    + "       , REPLACE(Lab04.Lab04C5, 'SYSTEM', '51949798') as UserCode" //UerCode en caso que correo sea null
                    + "       , lab04.lab04c4 as UserNickName " //Nombres usuario de validacion
                    + "       , lab04.lab04c2 as UserName " //Nombres usuario de validacion
                    + "       , lab04.lab04c3 as UserLastName" //Apellidos usuario de validacion
                    + "       , lab39.lab39c1 as TestId" //Id del examen
                    + "       , lab39.lab39c4 as TestName" //Nombre del examen
                    + "       , lab39.lab39c2 as TestCode" //Código del examen
                    + "       , t.lab57c1 as TestResult" //Resultado del examen
                    + "       , t.Lab57c14 as ProfileId" //Id del perfil (isnull lab39C1)
                    + "       , p.lab39c4 as ProfileName" //Nombre del perfil (isnull vacio)
                    + "       , hp.Lab61c1 as ProfileCups" //codigo cups
                    + "       , t.lab57c49 as InterfaceTestID" //Código central
                    + "       , t.lab57c26 as Antibiogram" //Tiene antibiograma (isnull 0)
                    + "       , h.Lab61c1 as Cups" //si no tiene codigo (isnull lab39C1)
                    + "       , lab39.lab39c27 as Confidential" //Confidencial
                    + "       , t.lab57c18 as ValidateDate" //fecha validacion
                    + "       , t.lab57c20 as preValidateDate" //fecha de pre validacion
                    + "       , t.lab57c2 as ResultDate" //fecha resultado
                    + "       , t.lab57c39 as dateTake" //fecha resultado
                    + "       , lt.lab24c9 as SampleCode " //muestra codigo
                    + "       , lt.lab24c2 as SampleName " //muestra nombre
                    + "       , lab95.lab95c1 as Comment" //comentario
                    + "       , t.lab45c2 as Units" //Unidades
                    + "       , t.lab57c9 as Patological" //patología
                    + "       , lab64.lab64c2 as MethodCode" //Cód Tecnica
                    + "       , lab64.lab64c3 as MethodName" //Nombre tecnica
                    + "       , lab48.lab48c12 as NormalMin" //Normal minimo
                    + "       , lab48.lab48c13 as NormalMax" //Normal maximo
                    + "       , lab48.lab48c5 as PanicMin" //Panico minimo
                    + "       , lab48.lab48c6 as PanicMax" //Panico maximo
                    + "       , lab50.lab50c2 as RefText" //Valor de referencia de texto para web                
                    + "       , lab50.lab50c2 as RefLiteral" //Valor de referencia literal    
                    + "       , lab39.lab39c12 as DecimalDigits" // decimales
                    + "       , lab39.lab39c11 as ResultType" //tipo resultado
                    + "       , t.lab57c50 as HIS" //si es mayor a uno retornar 0
                    + "       , t.lab57c51 as FechaHIS" //Fecha de envio al his-----------------------------
                    + "       , (Select lab57.lab57c49 from lab57 where lab22c1 = t.lab22c1 and lab39c1 = t.lab57c14 ) as InterfaceProfileID " //Código central del perfil
                    + "       , lab57c8 as stateresult" //Estado del resultdo P,F
                    + "       ,lab204c4 "
                    + "       ,t.lab57c67 as determination "
                    + "       ,lab204c6 "
                    + " FROM   lab57 as t"
                    + " INNER JOIN lab39 ON lab39.lab39c1 = t.lab39c1 "
                    + " LEFT JOIN lab39 AS p ON p.lab39c1 = t.lab57c14 "
                    + " LEFT  JOIN Lab61 h  ON h.lab39c1 = lab39.lab39c1 AND h.lab118c1 = ? "
                    + " LEFT  JOIN Lab61 hp ON hp.lab39c1 = p.Lab39C1 AND hp.lab118c1 =  ? "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab204 ON lab204.lab22c1 = t.lab22c1 and lab204.lab39c1 = t.lab39c1"
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = t.lab48c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = t.lab57c19 "
                    + " LEFT JOIN lab64 ON lab64.lab64c1 = t.lab64c1 "
                    + " LEFT JOIN lab50 ON lab50.lab50c1 = t.lab50c1_3 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = t.lab22c1 AND lab95.lab39c1 = t.lab39c1 "
                    + " WHERE t.lab57c8 >= 3 "
                    + " AND t.lab22c1 = ? "
                    + " AND t.lab57c50 = 0 "
                    + " ORDER BY lab43.lab43c3, lab39.lab39c42, lab39.lab39c2";

            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i)
                    ->
            {
                ResponseOrderDetailResult objResponse = new ResponseOrderDetailResult();
                objResponse.setSectionCode(rs.getString("SectionCode"));
                objResponse.setSectionName(rs.getString("SectionName"));
                objResponse.setUserCode(rs.getString("correoAux") != null ? rs.getString("correoAux") : rs.getString("UserCode"));
                objResponse.setUserNickName(rs.getString("UserNickName"));
                objResponse.setUserName(rs.getString("UserName"));
                objResponse.setUserLastName(rs.getString("UserLastName"));
                objResponse.setTestId(rs.getInt("TestId"));
                objResponse.setTestName(rs.getString("TestName"));
                objResponse.setTestCode(rs.getString("TestCode"));
                objResponse.setTestResult(Tools.decrypt(rs.getString("TestResult")));
                objResponse.setStateResult(rs.getInt("stateresult") == 3 ? "P" : "F");

                if (funtionProfileId)
                {
                    objResponse.setProfileId(rs.getString("ProfileId") == null ? rs.getInt("TestId") : rs.getInt("ProfileId"));
                    objResponse.setProfileName(rs.getString("ProfileName") == null ? "" : rs.getString("ProfileName"));
                    //VALORES DE REFERENCIA
                    objResponse.setNormalMax(rs.getDouble("NormalMax"));
                    objResponse.setNormalMin(rs.getDouble("NormalMin"));
                    objResponse.setPanicMin(rs.getDouble("PanicMin"));
                    objResponse.setPanicMax(rs.getDouble("PanicMax"));
                } else
                {
                    objResponse.setProfileId(rs.getInt("ProfileId"));
                    objResponse.setProfileName(rs.getString("ProfileName"));
                    //VALORES DE REFERENCIA suizaLAb
                    objResponse.setNormalMax(rs.getString("NormalMax") == null ? null : rs.getDouble("NormalMax"));
                    objResponse.setNormalMin(rs.getString("NormalMin") == null ? null : rs.getDouble("NormalMin"));
                    objResponse.setPanicMin(rs.getString("PanicMin") == null ? null : rs.getDouble("PanicMin"));
                    objResponse.setPanicMax(rs.getString("PanicMax") == null ? null : rs.getDouble("PanicMax"));
                }
                objResponse.setProfileCups(rs.getString("ProfileCups"));
                objResponse.setInterfaceTestID(rs.getString("InterfaceProfileID") == null ? rs.getString("InterfaceTestID") : rs.getString("InterfaceProfileID"));
                objResponse.setAntibiogram(rs.getInt("Antibiogram"));
                objResponse.setCups(rs.getString("ProfileId") == null ? (rs.getString("Cups") == null ? rs.getString("TestCode") : rs.getString("Cups")) : rs.getString("ProfileCups"));
                objResponse.setConfidential(rs.getInt("Confidential"));
                objResponse.setValidateDate(rs.getString("ValidateDate") == null ? rs.getTimestamp("preValidateDate").toString() : rs.getTimestamp("ValidateDate").toString());
                objResponse.setResultDate(rs.getTimestamp("ResultDate").toString());
                objResponse.setSampleCode(rs.getInt("SampleCode"));
                objResponse.setSampleName(rs.getString("SampleName"));
                //covierte formato HTML a Txt plano
                objResponse.setComment(rs.getString("Comment") == null ? null : HtmlToTxt.htmlToString(rs.getString("Comment")));
                objResponse.setUnits(rs.getString("Units"));
                objResponse.setPatological(rs.getInt("Patological"));
                objResponse.setMethodCode(rs.getString("MethodCode"));
                objResponse.setMethodName(rs.getString("MethodName"));
                objResponse.setRefText(rs.getString("RefText"));
                objResponse.setRefLiteral(rs.getString("RefLiteral"));
                objResponse.setDecimalDigits(rs.getInt("ResultType") == 1 ? rs.getInt("DecimalDigits") : 0);
                objResponse.setResultType(rs.getInt("ResultType"));
                objResponse.setHis(rs.getDate("FechaHIS") == null ? 0 : 1);
                // Nuevos campos requeridos por una interfaz de TS
                objResponse.setValidateDateFormat(rs.getString("ValidateDate") == null ? rs.getTimestamp("preValidateDate").toString() : rs.getTimestamp("ValidateDate").toString());
                objResponse.setResultDateFormat(rs.getString("ResultDate") == null ? new Date().toString() : rs.getTimestamp("ResultDate").toString());
                objResponse.setDateTaken(rs.getString("dateTake") == null ? new Date().toString() : rs.getTimestamp("dateTake").toString());
                objResponse.setCorrection(rs.getDate("FechaHIS") != null ? 1 : 0);
                objResponse.setRecount(rs.getString("lab204c4") == null ? null : HtmlToTxt.htmlToString(rs.getString("lab204c4")));
                objResponse.setComplementations(rs.getString("lab204c6") == null ? null : HtmlToTxt.htmlToString(rs.getString("lab204c6")));
                objResponse.setDeterminations(rs.getString("determination") == null ? null : HtmlToTxt.htmlToString(rs.getString("determination")));
                return objResponse;
            }, centralSystem, centralSystem, order);
        } catch (Exception ex)
        {
            ResultsLog.error(ex);
            return null;
        }
    }

    /**
     * Resultados para la interfaz de resultados por orden y codigo central
     *
     * @param orderI Orden inicial
     * @param orderE Orden final
     * @param dateI
     * @param dateF
     * @param centralSystem
     * @param funtionProfileId Este parametro se encargara de devolver el id del
     * perfil de una manera u otra
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    /*
    default List<ResponseOrderDetailResult> resultsByCentralSystemByDemographicItems(Long orderI, Long orderE, Timestamp dateI, Timestamp dateF, int centralSystem, boolean funtionProfileId) throws Exception
    {
        try
        {
            String table = "lab57.lab57c2";
            String field1 = "" + orderI;
            String field2 = "" + orderE;
            if (orderI <= 0 && orderE <= 0)
            {
                table = "lab57.lab57c51";
                field1 = String.valueOf(dateI);
                field2 = String.valueOf(dateF);
            }
            String query = ""
                    + "SELECT  lab22.lab22c1 as Order" //order
                    + "       , lab22.lab22c7 as OrderHIS" //orderHIS
                    + "       , lab21.lab21c2 as History" //historia
                    + "       , lab21.lab21c3 as PatientName" //PatientName
                    + "       , lab21.lab21c5 as PatientSurname" //PatientSurname(
                    + "       , lab21.lab80c1 as Genero" //Genero
                    + "       , lab21.lab21c7 as BirthDate" //BirthDate
                    + "       , lab21.lab54c1 as TypeDocum" //TypeDocum
                    + "       , lab54.lab54c2 as nameDocum" //TypeDocum
                    + "       , lab21.lab21c20 as CreationDate" //CreationDate "
                    + "       , lab43.lab43c2 as SectionCode " //area codigo
                    + "       , lab43.lab43c4 as SectionName" //area nombre
                    + "       , lab04.lab04c9 as correoAux" //correo para validar el user code (isnull usercode)
                    + "       , REPLACE(Lab04.Lab04C5, 'SYSTEM', '51949798') as UserCode" //UerCode en caso que correo sea null
                    + "       , lab04.lab04c4 as UserNickName " //Nombres usuario de validacion
                    + "       , lab04.lab04c2 as UserName " //Nombres usuario de validacion
                    + "       , lab04.lab04c3 as UserLastName" //Apellidos usuario de validacion
                    + "       , lab39.lab39c1 as TestId" //Id del examen
                    + "       , lab39.lab39c4 as TestName" //Nombre del examen
                    + "       , lab39.lab39c2 as TestCode" //Código del examen
                    + "       , lab57.lab57c1 as TestResult" //Resultado del examen
                    + "       , Lab57.Lab57c14 as ProfileId" //Id del perfil (isnull lab39C1)
                    + "       , p.lab39c4 as ProfileName" //Nombre del perfil (isnull vacio)
                    + "       , hp.Lab61c1 as ProfileCups" //codigo cups
                    + "       , lab57.lab57c49 as InterfaceTestID" //Código central
                    + "       , lab57.lab57c26 as Antibiogram" //Tiene antibiograma (isnull 0)
                    + "       , h.Lab61c1 as Cups" //si no tiene codigo (isnull lab39C1)
                    + "       , lab39.lab39c27 as Confidential" //Confidencial
                    + "       , lab57.lab57c18 as ValidateDate" //fecha validacion
                    + "       , lab57.lab57c2 as ResultDate" //fecha resultado
                    + "       , lab57.lab57c39 as dateTake" //fecha resultado
                    + "       , lt.lab24c9 as SampleCode " //muestra codigo
                    + "       , lt.lab24c2 as SampleName " //muestra nombre
                    + "       , lab95.lab95c1 as Comment" //comentario
                    + "       , lab57.lab45c2 as Units" //Unidades
                    + "       , lab57.lab57c9 as Patological" //patología
                    + "       , lab64.lab64c2 as MethodCode" //Cód Tecnica
                    + "       , lab64.lab64c3 as MethodName" //Nombre tecnica
                    + "       , lab48.lab48c12 as NormalMin" //Normal minimo
                    + "       , lab48.lab48c13 as NormalMax" //Normal maximo
                    + "       , lab48.lab48c5 as PanicMin" //Panico minimo
                    + "       , lab48.lab48c6 as PanicMax" //Panico maximo
                    + "       , lab50.lab50c2 as RefText" //Valor de referencia de texto para web                
                    + "       , lab50.lab50c2 as RefLiteral" //Valor de referencia literal    
                    + "       , lab39.lab39c12 as DecimalDigits" // decimales
                    + "       , lab39.lab39c11 as ResultType" //tipo resultado
                    + "       , lab57.lab57c50 as HIS" //si es mayor a uno retornar 0
                    + "       , lab57.lab57c51 as FechaHIS" //Fecha de envio al his-----------------------------
                    + "       , lab76.lab76c2 as nameMicroo" //Name microorganims
                    + "       , lab204.lab204c2 AS microoComment" //comentario microorganismo
                    + "       , lab79.lab79c2 AS nameAntibioc" //nombre Antibiotico
                    + "       , lab205.lab205c2 AS interpretation" //interpretacion Antibiogram
                    + "       , lab205.lab205c4 AS interpretationMan" //interpretacion manual Antibiogram
                    + "       , lab205.lab205c1 AS cmi" //CMI
                    + "       , lab205.lab205c3 AS cmiMan" //CMI manual
                    + " FROM   lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 "
                    + " LEFT  JOIN Lab61 h  ON h.lab39c1 = lab39.lab39c1 AND h.lab118c1 = ? "
                    + " LEFT  JOIN Lab61 hp ON hp.lab39c1 = p.Lab39C1 AND hp.lab118c1 =  ? "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                    + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                    + " LEFT JOIN lab50 ON lab50.lab50c1 = lab57.lab50c1_3 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "//
                    + " INNER JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "//
                    + " INNER JOIN lab204 ON lab204.lab22c1 = lab57.lab22c1 "
                    + " INNER JOIN lab205 ON lab204.lab204c1 = lab205.lab204c1 "
                    + " INNER JOIN lab76 ON lab204.lab76c1=lab76.lab76c1 "
                    + " INNER JOIN lab79 ON lab79.lab79c1=lab205.lab79c1"
                    + " WHERE lab57.lab57c8 > 3 "
                    + " AND lab39.lab39c37 = 0 "
                    + " AND lab57.lab57c50 = 0 "
                    + " AND " + table + " BETWEEN " + "'" + field1 + "'" + " AND " + "'" + field2 + "'";
            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i)
                    ->
            {
                ResultSensitivity resultSensitivity = new ResultSensitivity();//crea antibiograma
                resultSensitivity.setNameMicroo(rs.getString("nameMicroo"));
                resultSensitivity.setNameAntibioc(rs.getString("nameAntibioc"));
                resultSensitivity.setMicrooComment(rs.getString("microoComment"));
                resultSensitivity.setCmi(rs.getString("cmi") == null ? rs.getString("cmiMan") : rs.getString("cmi"));
                resultSensitivity.setInterpretationt(rs.getString("interpretation") == null ? rs.getString("interpretationMan") : rs.getString("interpretation"));
                ResponseOrderDetailResult objResponse = new ResponseOrderDetailResult();
//                objResponse.setTestCode(rs.getString("---"));//Codigo eaxamen HIS
                objResponse.setTestResult(rs.getString("TestResult"));//Codigo eaxamen Lis
                objResponse.setNormalMin(rs.getDouble("NormalMin"));//valor de referencia minimo
                objResponse.setNormalMax(rs.getDouble("NormalMax"));//valor de referencia maximo
                objResponse.setUnits(rs.getString("Units"));//unida result
                objResponse.setComment(rs.getString("Comment"));//comentario examen 
                objResponse.setResultSensitivity(resultSensitivity);//antibiograma
                objResponse.setOrderLIS(rs.getLong("Order"));//orden LIS
                objResponse.setHistory(rs.getString("History"));//historia
                objResponse.setTestName(rs.getString("TestName"));//nombre examen 
                objResponse.setTestCode(rs.getString("TestCode"));//examen a lis
                objResponse.setValidateDate(rs.getString("CreationDate"));//fecha de registro
                objResponse.setValidateDate(rs.getString("ValidateDate"));//fecha validation
                objResponse.setIdLab57(rs.getLong("Order"));//id lab57
                objResponse.setOrderHIS(rs.getString("OrderHIS"));//orden HIS
                objResponse.setAuthorization(rs.getString("demografico id 18"));//Autorizacion
                objResponse.setDocumentTypeHIS(rs.getString("OrderHIS"));//tipo documento his
                objResponse.setPatological(rs.getInt("Patological"));//patologico
                objResponse.setGender(rs.getString("Genero"));//genero
                objResponse.setPatientName(rs.getString(rs.getString("PatientName")));//nombre primo paciente
                objResponse.setPatientSurname(rs.getString("PatientSurname"));//apellido pacienyte primero
                objResponse.setBirthDate(rs.getString("BirthDate"));//fecha nacimento pacie
                objResponse.setUserName(rs.getString("UserName"));//name usuario valida
                objResponse.setUserNickName(rs.getString("UserNickName"));//nick usuario valida 
                objResponse.setDocument(rs.getString("-----"));//documeto usuario
                objResponse.setNap(rs.getString("Demografico id 18"));//nap
//                objResponse.setLaboratoryName(rs.getString("-----"));//nap
                return objResponse;
            }, centralSystem, centralSystem, orderI);
        } catch (DataAccessException ex)
        {
            ResultsLog.error(ex);
            return null;
        }
    }
    
    
   * /

    /**
     * Responde objeto genral de Resultado para la interfaz de resultados del
     * HIS GHIPS por orden Y Idtest
     *
     * @param OrderToSearch trae orden y idtes
     * @return ResponseOrderDetailResult objeto general
     * @throws Exception Error en la base de datos.
     */
 /*
    default ResponseOrderDetailResult resultsByCentralSystem(OrderToSearch order) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT  lab22.lab22c1 as Order" //order
                    + "       , lab22.lab22c7 as OrderHIS" //orderHIS
                    + "       , lab21.lab21c2 as History" //historia
                    + "       , lab21.lab21c3 as PatientName" //PatientName
                    + "       , lab21.lab21c5 as PatientSurname" //PatientSurname(
                    + "       , lab21.lab80c1 as Genero" //Genero
                    + "       , lab21.lab21c7 as BirthDate" //BirthDate
                    + "       , lab21.lab54c1 as TypeDocum" //TypeDocum
                    + "       , lab54.lab54c2 as nameDocum" //TypeDocum
                    + "       , lab21.lab21c20 as CreationDate" //CreationDate "
                    + "       , lab43.lab43c2 as SectionCode " //area codigo
                    + "       , lab43.lab43c4 as SectionName" //area nombre
                    + "       , lab04.lab04c9 as correoAux" //correo para validar el user code (isnull usercode)
                    + "       , REPLACE(Lab04.Lab04C5, 'SYSTEM', '51949798') as UserCode" //UerCode en caso que correo sea null
                    + "       , lab04.lab04c4 as UserNickName " //Nombres usuario de validacion
                    + "       , lab04.lab04c2 as UserName " //Nombres usuario de validacion
                    + "       , lab04.lab04c3 as UserLastName" //Apellidos usuario de validacion
                    + "       , lab39.lab39c1 as TestId" //Id del examen
                    + "       , lab39.lab39c4 as TestName" //Nombre del examen
                    + "       , lab39.lab39c2 as TestCode" //Código del examen
                    + "       , lab57.lab57c1 as TestResult" //Resultado del examen
                    + "       , Lab57.Lab57c14 as ProfileId" //Id del perfil (isnull lab39C1)
                    + "       , p.lab39c4 as ProfileName" //Nombre del perfil (isnull vacio)
                    + "       , hp.Lab61c1 as ProfileCups" //codigo cups
                    + "       , lab57.lab57c49 as InterfaceTestID" //Código central
                    + "       , lab57.lab57c26 as Antibiogram" //Tiene antibiograma (isnull 0)
                    + "       , h.Lab61c1 as Cups" //si no tiene codigo (isnull lab39C1)
                    + "       , lab39.lab39c27 as Confidential" //Confidencial
                    + "       , lab57.lab57c18 as ValidateDate" //fecha validacion
                    + "       , lab57.lab57c2 as ResultDate" //fecha resultado
                    + "       , lab57.lab57c39 as dateTake" //fecha resultado
                    + "       , lt.lab24c9 as SampleCode " //muestra codigo
                    + "       , lt.lab24c2 as SampleName " //muestra nombre
                    + "       , lab95.lab95c1 as Comment" //comentario
                    + "       , lab57.lab45c2 as Units" //Unidades
                    + "       , lab57.lab57c9 as Patological" //patología
                    + "       , lab64.lab64c2 as MethodCode" //Cód Tecnica
                    + "       , lab64.lab64c3 as MethodName" //Nombre tecnica
                    + "       , lab48.lab48c12 as NormalMin" //Normal minimo
                    + "       , lab48.lab48c13 as NormalMax" //Normal maximo
                    + "       , lab48.lab48c5 as PanicMin" //Panico minimo
                    + "       , lab48.lab48c6 as PanicMax" //Panico maximo
                    + "       , lab50.lab50c2 as RefText" //Valor de referencia de texto para web                
                    + "       , lab50.lab50c2 as RefLiteral" //Valor de referencia literal    
                    + "       , lab39.lab39c12 as DecimalDigits" // decimales
                    + "       , lab39.lab39c11 as ResultType" //tipo resultado
                    + "       , lab57.lab57c50 as HIS" //si es mayor a uno retornar 0
                    + "       , lab57.lab57c51 as FechaHIS" //Fecha de envio al his-----------------------------
                    + "       , lab76.lab76c2 as nameMicroo" //Name microorganims
                    + "       , lab204.lab204c2 AS microoComment" //comentario microorganismo
                    + "       , lab79.lab79c2 AS nameAntibioc" //nombre Antibiotico
                    + "       , lab205.lab205c2 AS interpretation" //interpretacion Antibiogram
                    + "       , lab205.lab205c4 AS interpretationMan" //interpretacion manual Antibiogram
                    + "       , lab205.lab205c1 AS cmi" //CMI
                    + "       , lab205.lab205c3 AS cmiMan" //CMI manual
                    + " FROM   lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 "
                    + " LEFT  JOIN Lab61 h  ON h.lab39c1 = lab39.lab39c1 "
                    + " LEFT  JOIN Lab61 hp ON hp.lab39c1 = p.Lab39C1 "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                    + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                    + " LEFT JOIN lab50 ON lab50.lab50c1 = lab57.lab50c1_3 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "//
                    + " INNER JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "//
                    + " INNER JOIN lab204 ON lab204.lab22c1 = lab57.lab22c1 "
                    + " INNER JOIN lab205 ON lab204.lab204c1 = lab205.lab204c1 "
                    + " INNER JOIN lab76 ON lab204.lab76c1=lab76.lab76c1 "
                    + " INNER JOIN lab79 ON lab79.lab79c1=lab205.lab79c1"
                    + " WHERE lab57.lab57c8 > 3 "
                    + " AND lab39.lab39c37 = 0 "
                    + " AND lab57.lab57c50 = 0 "
                    + " AND lab57.lab22c1 = ? "
                    + " AND p.Lab39C1 = ? ";

            return getJdbcTemplate().queryForObject(query,
                    (ResultSet rs, int i)
                    ->
            {
                ResultSensitivity resultSensitivity = new ResultSensitivity();//crea antibiograma
                resultSensitivity.setNameMicroo(rs.getString("nameMicroo"));
                resultSensitivity.setNameAntibioc(rs.getString("nameAntibioc"));
                resultSensitivity.setMicrooComment(rs.getString("microoComment"));
                resultSensitivity.setCmi(rs.getString("cmi") == null ? rs.getString("cmiMan") : rs.getString("cmi"));
                resultSensitivity.setInterpretationt(rs.getString("interpretation") == null ? rs.getString("interpretationMan") : rs.getString("interpretation"));
                ResponseOrderDetailResult objResponse = new ResponseOrderDetailResult();
//                objResponse.setTestCode(rs.getString("---"));//Codigo eaxamen HIS
                objResponse.setTestResult(rs.getString("TestResult"));//Codigo eaxamen Lis
                objResponse.setNormalMin(rs.getDouble("NormalMin"));//valor de referencia minimo
                objResponse.setNormalMax(rs.getDouble("NormalMax"));//valor de referencia maximo
                objResponse.setUnits(rs.getString("Units"));//unida result
                objResponse.setComment(rs.getString("Comment"));//comentario examen 
                objResponse.setResultSensitivity(resultSensitivity);//antibiograma
                objResponse.setOrderLIS(rs.getLong("Order"));//orden LIS
                objResponse.setHistory(rs.getString("History"));//historia
                objResponse.setTestName(rs.getString("TestName"));//nombre examen 
                objResponse.setTestCode(rs.getString("TestCode"));//examen a lis
                objResponse.setValidateDate(rs.getString("CreationDate"));//fecha de registro
                objResponse.setValidateDate(rs.getString("ValidateDate"));//fecha validation
                objResponse.setIdLab57(rs.getLong("Order"));//id lab57
                objResponse.setOrderHIS(rs.getString("OrderHIS"));//orden HIS
                objResponse.setAuthorization(rs.getString("demografico id 18"));//Autorizacion
                objResponse.setDocumentTypeHIS(rs.getString("OrderHIS"));//tipo documento his
                objResponse.setPatological(rs.getInt("Patological"));//patologico
                objResponse.setGender(rs.getString("Genero"));//genero
                objResponse.setPatientName(rs.getString(rs.getString("PatientName")));//nombre primo paciente
                objResponse.setPatientSurname(rs.getString("PatientSurname"));//apellido pacienyte primero
                objResponse.setBirthDate(rs.getString("BirthDate"));//fecha nacimento pacie
                objResponse.setUserName(rs.getString("UserName"));//name usuario valida
                objResponse.setUserNickName(rs.getString("UserNickName"));//nick usuario valida 
                objResponse.setDocument(rs.getString("-----"));//documeto usuario
                objResponse.setNap(rs.getString("Demografico id 18"));//nap
//                objResponse.setLaboratoryName(rs.getString("-----"));//nap
                return objResponse;
            }, order.getNumberOrder(), order.getCodeTest());
        } catch (DataAccessException ex)
        {
            ResultsLog.error(ex);
            return null;
        }
    }
     */
    /**
     * Actualizar el estado del resultado con su respectiva fecha de
     * actualización
     *
     * @param requestUpdateSendResult
     * @param updateDate
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default int updateResultStateAndDateUpdate(RequestUpdateSendResult requestUpdateSendResult, Timestamp updateDate) throws Exception
    {
        try
        {
            if (requestUpdateSendResult.getTestId() != null)
            {
                return getJdbcTemplate().update("UPDATE lab57"
                        + " SET lab57c50 = ?,"
                        + " lab57c51 = ? "
                        + "WHERE lab39c1 = ? "
                        + "AND lab22c1 = ? "
                        + "and lab57c50 = 0 "
                        + "and lab57c8 >= 3",
                        requestUpdateSendResult.getState(),
                        updateDate,
                        requestUpdateSendResult.getTestId(),
                        requestUpdateSendResult.getOrder());
            } else if (!requestUpdateSendResult.getCentralCode().isEmpty())
            {
                return getJdbcTemplate().update("UPDATE lab57"
                        + " SET lab57c50 = ?,"
                        + " lab57c51 = ? "
                        + "WHERE lab57c49 = ? "
                        + "AND lab22c1 = ? "
                        + "and lab57c50 = 0 "
                        + "and lab57c8 >= 3",
                        requestUpdateSendResult.getState(),
                        updateDate,
                        requestUpdateSendResult.getCentralCode(),
                        requestUpdateSendResult.getOrder());
            } else
            {
                return getJdbcTemplate().update("UPDATE lab57 SET lab57c50 = 3, "
                        + "lab57c51 = ? "
                        + "WHERE lab57c50 = 0 "
                        + "AND lab57c49 IS NULL "
                        + "AND lab22c1 = ? "
                        + "and lab57c8 >= 3",
                        updateDate,
                        requestUpdateSendResult.getOrder());
            }
        } catch (DataAccessException e)
        {
            return 0;
        }
    }

    /**
     * Actualizar el estado del resultado con su respectiva fecha de
     * actualización en el his
     *
     * @param requestUpdateSendResult
     * @param updateDate
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default int updateResultStateAndDateUpdateForSystem(RequestUpdateSendResult requestUpdateSendResult, Timestamp updateDate) throws Exception
    {
        try
        {

            return getJdbcTemplate().update("INSERT INTO lab191 ( "
                    + "lab118c1 , "//Id sSistema
                    + "lab39c1 , "//id test
                    + "lab191c1 , "//code perfil pertenece
                    + "lab22c1 , "//id orden
                    + "lab191c2 , "//state
                    + "lab191c3 ) VALUES ( "
                    + " ? ,? ,his-18 ,?,? )  ",
                    requestUpdateSendResult.getCentralCode(),
                    requestUpdateSendResult.getTestId(),
                    requestUpdateSendResult.getOrder(),
                    requestUpdateSendResult.getState());

        } catch (DataAccessException e)
        {
            return 0;
        }
    }

    /**
     * Resultados de microorganismos, antibioticos y antibiogramas para la
     * interfaz de resultados por id orden y id test
     *
     * @param idOrder
     * @param idTest
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResponseDetailMicroorganisms> listDetailMicroorganisms(long idOrder, int idTest) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab76.lab76c2 AS Mic, " //Nombre del microorganismo
                    + "       lab79.Lab79c2 AS Ant, " //Nombre del antibiotico
                    + "       lab205.lab205c2 AS Result, " //Interpretacion
                    + "       lab77.lab77c4 AS ABName, " //Nombre del antibiograma
                    + "       lab204.lab204c2 AS Comment, " //Comentario
                    + "       lab205.lab205c1 AS CMI, " //Resultado del antibiotico
                    + "       lab204.lab76c1 AS MicID " //Id del microorganismo
                    + " FROM  lab205 "
                    + " INNER JOIN lab204 ON lab204.lab204c1 = lab205.lab204c1 "
                    + " INNER JOIN lab76 ON lab76.Lab76c1 = lab204.lab76c1 "
                    + " INNER JOIN lab77 ON lab77.lab77c1 = lab204.lab77c1 "
                    + " INNER JOIN lab79 ON lab79.lab79c1 = lab205.lab79c1 " // --------------
                    + " WHERE lab204.lab39c1 = ? "
                    + " AND lab204.lab22c1 = ? ";

            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i)
                    ->
            {
                ResponseDetailMicroorganisms detailMicroorganism = new ResponseDetailMicroorganisms();
                detailMicroorganism.setMic(rs.getString("Mic"));
                detailMicroorganism.setAnt(rs.getString("Ant"));
                detailMicroorganism.setRes(rs.getString("Result"));
                detailMicroorganism.setAbName(rs.getString("ABName"));
                detailMicroorganism.setComment(rs.getString("Comment"));
                detailMicroorganism.setCmi(rs.getString("CMI"));
                detailMicroorganism.setMicId(rs.getInt("MicID"));
                return detailMicroorganism;
            }, idTest, idOrder);
        } catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Retorna todos los demograficos, tanto codificados no codificados,
     * dinamicos y fijos
     *
     * @param requestOrdersResultados
     * @param idOrder
     * @param alReadySent
     * @return
     * @throws Exception
     */
    default List<ResponseOrderResult> allDemographicsItemList(RequestOrdersResultados requestOrdersResultados, long idOrder, int alReadySent) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT O.Lab22C1 AS Orden, ")
                    .append("O.Lab22C7 AS OrdenHIS, ")
                    .append("H.Lab21C2 AS Record, ")
                    .append("H.Lab21C3 AS Name, ")
                    .append("H.Lab21C5 AS LastName, ")
                    .append("(CASE WHEN G.Lab80C3 = '3' THEN '0' ELSE G.Lab80C3 END) AS Gender, ")
                    .append("H.Lab21C7 AS BirthDate, ")
                    .append("HS.Lab117C3 AS SerCode, ")
                    .append("S.Lab10C2  AS SerName, ")
                    .append("HSede.Lab117C3 AS SedeCode, ")
                    .append("OT.Lab103C2 AS Priority, ")
                    .append("DT.Lab117C3 AS IDType, ");

            // Se cansultaran campos del ambito solo si este dato se envia
            if (requestOrdersResultados.getIdAmbito() != null && requestOrdersResultados.getIdAmbito() > 0)
            {
                query.append("HAM.Lab117C3 AS AmbitoCode, ");
            }

            // El Pac número y el cuenta key solo seran consultados si estos son enviados
            if (requestOrdersResultados.getPacNum() != null && requestOrdersResultados.getPacNum() > 0)
            {
                query.append("H.Lab_demo_").append(requestOrdersResultados.getPacNum()).append(" AS NumHistoria, ");
            }
            if (requestOrdersResultados.getCuentaKey() != null && requestOrdersResultados.getCuentaKey() > 0)
            {
                query.append("O.Lab_demo_").append(requestOrdersResultados.getCuentaKey()).append(" AS Cuenta, ");
            }

            query.append("H.Lab21C8 AS Correo, ")
                    .append("H.Lab21C16 AS Telefono ")
                    .append(" FROM Lab22 O ")
                    .append("JOIN Lab21 H ON(O.Lab21C1=H.Lab21C1) ")
                    .append("JOIN Lab80 G ON(G.Lab80C1=H.Lab80C1) ")
                    .append("LEFT JOIN Lab10 S ON(O.Lab10C1=S.Lab10C1) ")
                    .append("LEFT JOIN Lab117 HS ON(HS.Lab117C1 = -6 AND HS.Lab117C2 = S.Lab10C1 AND HS.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ")
                    .append("LEFT JOIN Lab05 Sede ON(O.Lab05C1 = Sede.Lab05C1) ")
                    .append("LEFT JOIN Lab117 HSede ON(HSede.Lab117C1 = -5 AND HSede.Lab117C2 = Sede.Lab05C1 AND HSede.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ");

            // Se cansultaran campos del ambito solo si este dato se envia
            if (requestOrdersResultados.getIdAmbito() != null && requestOrdersResultados.getIdAmbito() > 0)
            {
                query.append("LEFT JOIN Lab63 AM ON(O.lab_demo_").append(requestOrdersResultados.getIdAmbito()).append(" = AM.Lab63C1) ");
                query.append("LEFT JOIN Lab117 HAM ON(HAM.Lab117C1 = ").append(requestOrdersResultados.getIdAmbito()).append(" AND HAM.Lab117C2 = AM.Lab63C1 AND HAM.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ");
            }

            query.append("LEFT JOIN Lab103 OT ON(OT.Lab103C1=O.Lab103C1) ")
                    .append("LEFT JOIN Lab117 DT ON(DT.Lab117C1 = -10 AND DT.Lab117C2 = H.Lab54C1 AND DT.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ")
                    .append("WHERE O.Lab22C1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ResponseOrderResult responsObj = new ResponseOrderResult();
                responsObj.setIdOrder(rs.getLong("Orden"));
                responsObj.setInterfaceKey(rs.getString("OrdenHIS") != null ? rs.getString("OrdenHIS") : "");
                responsObj.setRecord(Tools.decrypt(rs.getString("Record")));
                responsObj.setName(Tools.decrypt(rs.getString("Name")));
                responsObj.setLastName(Tools.decrypt(rs.getString("LastName")));
                responsObj.setGender(rs.getInt("Gender"));
                responsObj.setBirthDate(rs.getDate("BirthDate"));
                responsObj.setSerCode(rs.getString("SerCode"));
                responsObj.setSerName(rs.getString("SerName"));
                responsObj.setSedeCode(rs.getString("SedeCode"));
                if (requestOrdersResultados.getIdAmbito() != null && requestOrdersResultados.getIdAmbito() > 0)
                {
                    responsObj.setAmbitoCode(rs.getString("AmbitoCode"));
                }
                responsObj.setPriority(rs.getString("Priority"));
                responsObj.setIdType(rs.getString("IDType"));
                responsObj.setAlreadySent(alReadySent);
                if (requestOrdersResultados.getPacNum() != null && requestOrdersResultados.getPacNum() > 0)
                {
                    responsObj.setNumHistoria(rs.getString("NumHistoria"));
                }
                if (requestOrdersResultados.getCuentaKey() != null && requestOrdersResultados.getCuentaKey() > 0)
                {
                    responsObj.setCuenta(rs.getString("Cuenta"));
                }
                responsObj.setCorreo(rs.getString("Correo"));
                responsObj.setTelefono(rs.getString("Telefono"));
                return responsObj;
            });
        } catch (Exception e)
        {
            ResultsLog.error(e);
            return null;
        }
    }

    /**
     * Obtener información de una prueba por un campo especifico.
     *
     * @param id ID de la prueba a ser consultada.
     * @param code Codigo de la prueba a ser consultada.
     * @param name Nombre de la prueba a ser consultada.
     * @param abbr Abreviatura de la prueba a ser consultada.
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    default Test getTest(Integer id, String code, String name, String abbr) throws Exception
    {
        try
        {

            String query = "SELECT lab39c1, "
                    + "lab39c2, "
                    + "lab39c3, "
                    + "lab39c4, "
                    + "lab39c37, "
                    + "lab39.lab24c1, "
                    + "lab24.lab24c2, "
                    + "lab24.lab24c9, "
                    + "lab24.lab24c4 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab39c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab39c2) = ? ";
            }
            if (abbr != null)
            {
                query = query + "WHERE UPPER(lab39c3) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab39c4) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
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
            if (abbr != null)
            {
                object = abbr.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                /*Muestra*/
                if (rs.getString("lab24c1") != null)
                {
                    test.getSample().setId(rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                }

                return test;
            });
        } catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Retorna todos los demograficos, tanto codificados no codificados,
     * dinamicos y fijos
     *
     * @param requestOrdersResultados
     * @param idOrder
     * @param alReadySent
     * @return
     * @throws Exception
     */
    default List<ResponseOrderResult> allDemographicsItemListForOther(RequestOrdersResultados requestOrdersResultados, long idOrder, int alReadySent) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT O.Lab22C1 AS Orden, ")
                    .append("O.Lab22c7 AS interfaceKey, ")
                    .append("H.Lab21C2 AS Record, ")
                    .append("H.Lab21C3 AS Name, ")
                    .append("H.Lab21C5 AS LastName, ")
                    .append("(CASE WHEN G.Lab80C3 = '3' THEN '0' ELSE G.Lab80C3 END) AS Gender, ")
                    .append("H.Lab21C7 AS BirthDate, ")
                    .append("HS.Lab117C3 AS SerCode, ")
                    .append("S.Lab10C2  AS SerName, ")
                    .append("HSede.Lab117C3 AS SedeCode, ")
                    .append("OT.Lab103C2 AS Priority, ")
                    .append("DT.Lab117C3 AS IDType, ")
                    .append("H.Lab21C8 AS Correo, ")
                    .append("H.Lab21C16 AS Telefono ")
                    .append(" FROM Lab22 O ")
                    .append("JOIN Lab21 H ON(O.Lab21C1=H.Lab21C1) ")
                    .append("JOIN Lab80 G ON(G.Lab80C1=H.Lab80C1) ")
                    .append("LEFT JOIN Lab10 S ON(O.Lab10C1=S.Lab10C1) ")
                    .append("LEFT JOIN Lab117 HS ON(HS.Lab117C1 = -6 AND HS.Lab117C2 = S.Lab10C1 AND HS.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ")
                    .append("LEFT JOIN Lab05 Sede ON(O.Lab05C1 = Sede.Lab05C1) ")
                    .append("LEFT JOIN Lab117 HSede ON(HSede.Lab117C1 = -5 AND HSede.Lab117C2 = Sede.Lab05C1 AND HSede.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ")
                    .append("LEFT JOIN Lab103 OT ON(OT.Lab103C1=O.Lab103C1) ")
                    .append("LEFT JOIN Lab117 DT ON(DT.Lab117C1 = -10 AND DT.Lab117C2 = H.Lab54C1 AND DT.Lab118C1 = ").append(requestOrdersResultados.getCentralSystem()).append(") ")
                    .append("WHERE O.Lab22C1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ResponseOrderResult responsObj = new ResponseOrderResult();
                responsObj.setIdOrder(rs.getLong("Orden"));
                responsObj.setInterfaceKey(rs.getString("interfaceKey"));
                responsObj.setRecord(Tools.decrypt(rs.getString("Record")));
                responsObj.setName(Tools.decrypt(rs.getString("Name")));
                responsObj.setLastName(Tools.decrypt(rs.getString("LastName")));
                responsObj.setGender(rs.getInt("Gender"));
                responsObj.setBirthDate(rs.getDate("BirthDate"));
                responsObj.setSerCode(rs.getString("SerCode"));
                responsObj.setSerName(rs.getString("SerName"));
                responsObj.setSedeCode(rs.getString("SedeCode"));
                responsObj.setPriority(rs.getString("Priority"));
                responsObj.setIdType(rs.getString("IDType"));
                responsObj.setAlreadySent(alReadySent);
                responsObj.setCorreo(rs.getString("Correo"));
                responsObj.setTelefono(rs.getString("Telefono"));
                return responsObj;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Cambia el estado del campo de envio al HIS de 1 o el valor que
     * corresponda a cero Para tener este nuevo resultado en cuenta para ser re
     * enviado
     *
     * @param orderId
     * @param testId
     * @throws Exception
     */
    default void changeToResultNotSentToHIS(long orderId, int testId) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(orderId));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c50 = 0 "
                + "WHERE lab22c1 = ?"
                + " AND lab39c1 = ?",
                orderId,
                testId);
    }

    /**
     * Consulta los resultados de las ordenes que seran enviados a MyM por id de
     * la orden y id del sistema central
     *
     * @param order
     * @param centralSystem
     * @param funtionProfileId Este parametro se encargara de devolver el id del
     * perfil de una manera u otra
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResponseOrderDetailResult> getMyMResults(long order, int centralSystem, boolean funtionProfileId) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT   lab43.lab43c2 as SectionCode " //area codigo
                    + "       , lab43.lab43c4 as SectionName" //area nombre
                    + "       , lab04.lab04c9 as correoAux" //correo para validar el user code (isnull usercode)
                    + "       , REPLACE(Lab04.Lab04C5, 'SYSTEM', '51949798') as UserCode" //UerCode en caso que correo sea null
                    + "       , lab04.lab04c4 as UserNickName " //Nombres usuario de validacion
                    + "       , lab04.lab04c2 as UserName " //Nombres usuario de validacion
                    + "       , lab04.lab04c3 as UserLastName" //Apellidos usuario de validacion
                    + "       , lab39.lab39c1 as TestId" //Id del examen
                    + "       , lab39.lab39c4 as TestName" //Nombre del examen
                    + "       , lab39.lab39c2 as TestCode" //Código del examen
                    + "       , lab57.lab57c1 as TestResult" //Resultado del examen
                    + "       , Lab57.Lab57c14 as ProfileId" //Id del perfil (isnull lab39C1)
                    + "       , p.lab39c4 as ProfileName" //Nombre del perfil (isnull vacio)
                    + "       , hp.Lab61c1 as ProfileCups" //codigo cups
                    + "       , lab57.lab57c49 as InterfaceTestID" //Código central
                    + "       , lab57.lab57c26 as Antibiogram" //Tiene antibiograma (isnull 0)
                    + "       , h.Lab61c1 as Cups" //si no tiene codigo (isnull lab39C1)
                    + "       , lab39.lab39c27 as Confidential" //Confidencial
                    + "       , lab57.lab57c18 as ValidateDate" //fecha validacion
                    + "       , lab57.lab57c2 as ResultDate" //fecha resultado
                    + "       , lab57.lab57c39 as dateTake" //fecha resultado
                    + "       , lt.lab24c9 as SampleCode " //muestra codigo
                    + "       , lt.lab24c2 as SampleName " //muestra nombre
                    + "       , lab95.lab95c1 as Comment" //comentario
                    + "       , lab57.lab45c2 as Units" //Unidades
                    + "       , lab57.lab57c9 as Patological" //patología
                    + "       , lab64.lab64c2 as MethodCode" //Cód Tecnica
                    + "       , lab64.lab64c3 as MethodName" //Nombre tecnica
                    + "       , lab57.lab48c12 as NormalMin" //Normal minimo
                    + "       , lab57.lab48c13 as NormalMax" //Normal maximo
                    + "       , lab57.lab48c5 as PanicMin" //Panico minimo
                    + "       , lab57.lab48c6 as PanicMax" //Panico maximo
                    + "       , lab50.lab50c2 as RefText" //Valor de referencia de texto para web                
                    + "       , lab50.lab50c2 as RefLiteral" //Valor de referencia literal    
                    + "       , lab39.lab39c12 as DecimalDigits" // decimales
                    + "       , lab39.lab39c11 as ResultType" //tipo resultado
                    + "       , lab57.lab57c50 as HIS" //si es mayor a uno retornar 0
                    + "       , lab57.lab57c51 as FechaHIS" //Fecha de envio al his-----------------------------
                    + " FROM   lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 "
                    + " LEFT  JOIN Lab61 h  ON h.lab39c1 = lab39.lab39c1 AND h.lab118c1 = ? "
                    + " LEFT  JOIN Lab61 hp ON hp.lab39c1 = p.Lab39C1 AND hp.lab118c1 =  ? "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                    + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                    + " LEFT JOIN lab50 ON lab50.lab50c1 = lab57.lab50c1_3 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " WHERE lab57.lab57c8 > 3"
                    + " AND lab39.lab39c37 = 0"
                    + " AND lab57.lab22c1 = ?"
                    + " AND lab57.lab57c50 = 0"
                    + " AND lab57.lab57c49 is not null "
                    + " ORDER BY lab43.lab43c3, lab39.lab39c42, lab39.lab39c2";

            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i)
                    ->
            {
                ResponseOrderDetailResult objResponse = new ResponseOrderDetailResult();
                objResponse.setSectionCode(rs.getString("SectionCode"));
                objResponse.setSectionName(rs.getString("SectionName"));
                objResponse.setUserCode(rs.getString("correoAux") != null ? rs.getString("correoAux") : rs.getString("UserCode"));
                objResponse.setUserNickName(rs.getString("UserNickName"));
                objResponse.setUserName(rs.getString("UserName"));
                objResponse.setUserLastName(rs.getString("UserLastName"));
                objResponse.setTestId(rs.getInt("TestId"));
                objResponse.setTestName(rs.getString("TestName"));
                objResponse.setTestCode(rs.getString("TestCode"));
                objResponse.setTestResult(Tools.decrypt(rs.getString("TestResult")));
                if (funtionProfileId)
                {
                    objResponse.setProfileId(rs.getInt("ProfileId") == 0 ? rs.getInt("TestId") : rs.getInt("ProfileId"));
                    objResponse.setProfileName(rs.getString("ProfileName") == null ? "" : rs.getString("ProfileName"));
                } else
                {
                    objResponse.setProfileId(rs.getInt("ProfileId"));
                    objResponse.setProfileName(rs.getString("ProfileName"));
                }
                objResponse.setProfileCups(rs.getString("ProfileCups"));
                objResponse.setInterfaceTestID(rs.getString("InterfaceTestID"));
                objResponse.setAntibiogram(rs.getInt("Antibiogram"));
                objResponse.setCups(rs.getInt("ProfileId") == 0 ? rs.getString("Cups") == null ? rs.getString("TestCode") : rs.getString("Cups") : rs.getString("ProfileCups"));
                objResponse.setConfidential(rs.getInt("Confidential"));
                objResponse.setValidateDate(rs.getTimestamp("ValidateDate").toString());
                objResponse.setResultDate(rs.getTimestamp("ResultDate").toString());
                objResponse.setSampleCode(rs.getInt("SampleCode"));
                objResponse.setSampleName(rs.getString("SampleName"));
                objResponse.setComment(rs.getString("Comment"));
                objResponse.setUnits(rs.getString("Units"));
                objResponse.setPatological(rs.getInt("Patological"));
                objResponse.setMethodCode(rs.getString("MethodCode"));
                objResponse.setMethodName(rs.getString("MethodName"));

                if (rs.getString("NormalMin") != null)
                {
                    objResponse.setNormalMin(rs.getDouble("NormalMin"));
                }

                if (rs.getString("NormalMax") != null)
                {
                    objResponse.setNormalMax(rs.getDouble("NormalMax"));
                }

                if (rs.getString("PanicMin") != null)
                {
                    objResponse.setPanicMin(rs.getDouble("PanicMin"));
                }

                if (rs.getString("PanicMax") != null)
                {
                    objResponse.setPanicMax(rs.getDouble("PanicMax"));
                }

                objResponse.setRefText(rs.getString("RefText"));
                objResponse.setRefLiteral(rs.getString("RefLiteral"));
                objResponse.setDecimalDigits(rs.getInt("ResultType") == 1 ? rs.getInt("DecimalDigits") : 0);
                objResponse.setResultType(rs.getInt("ResultType"));
                objResponse.setHis(rs.getDate("FechaHIS") == null ? 0 : 1);
                // Nuevos campos requeridos por una interfaz de TS
                objResponse.setValidateDateFormat(rs.getTimestamp("ValidateDate").toString());
                objResponse.setResultDateFormat(rs.getTimestamp("ResultDate").toString());
                objResponse.setDateTaken(rs.getTimestamp("dateTake").toString());
                objResponse.setCorrection(rs.getDate("FechaHIS") != null ? 1 : 0);
                return objResponse;
            }, centralSystem, centralSystem, order);
        } catch (Exception ex)
        {
            ex.getMessage();
            return null;
        }
    }
}
