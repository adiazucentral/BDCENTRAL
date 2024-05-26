package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.resultados.DemoHeader;
import net.cltech.enterprisent.domain.integration.resultados.DetailFilter;
import net.cltech.enterprisent.domain.integration.resultados.DetailStatus;
import net.cltech.enterprisent.domain.integration.resultados.OrderHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeaderFilter;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeader;
import net.cltech.enterprisent.domain.integration.resultados.TestDetail;
import net.cltech.enterprisent.domain.integration.resultados.TestHeader;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.orders.Deliverytype;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.results.HistoryFilter;
import net.cltech.enterprisent.domain.operation.results.ImageTest;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestBlock;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestHistory;
import net.cltech.enterprisent.domain.operation.results.ResultTestPrint;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestStateOrder;
import net.cltech.enterprisent.domain.operation.results.ValidationRelationship;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.HtmlToTxt;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestPathology;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestState;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para el registro de resultados a los exámenes
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
public interface ResultTestDao
{

    /**
     *
     */
    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Obtiene la conexión a base de datos de documentos
     *
     * @return jdbc
     */
    public JdbcTemplate getDocsJdbcTemplate();

    
   
    
    /**
     * Obtiene el usuario del token
     *
     * @return Usuario logueado
     * @throws java.lang.Exception
     */
    public AuthorizedUser getUser() throws Exception;

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * ordenes aplicado por el usuario
     * @param orderIds Id´s de ordenes
     * @param sampleState
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> list(final ResultFilter resultFilter, List<Long> orderIds, Integer sampleState, int yearsQuery) throws Exception
    {

        try
        {
            List<ResultTest> listResults = new LinkedList<>();
            List<Integer> years = new LinkedList<>();

            if (orderIds == null || orderIds.isEmpty())
            {
                Long vInitial = (resultFilter.getFirstOrder() == -1 || resultFilter.getFirstOrder() == 0) ? Long.parseLong(String.valueOf(resultFilter.getFirstDate())) : resultFilter.getFirstOrder();
                Long vFinal = (resultFilter.getLastOrder() == 1 || resultFilter.getLastOrder() == 0) ? Long.parseLong(String.valueOf(resultFilter.getLastDate())) : resultFilter.getLastOrder();
                years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            } else
            {
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
            }

            String lab22;
            String lab57;
            String lab95;
            String lab900;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab95) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab900) : tableExists;
                if (tableExists)
                {
                    String select = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   lab57.lab22c1 "
                            + "       , lab43.lab43c1 " //area id
                            + "       , lab43.lab43c2 " //area codigo
                            + "       , lab43.lab43c3 " //area abreviatura
                            + "       , lab43.lab43c4 " //area nombre
                            + "       , lab43.lab43c8 " //area validación parcial
                            + "       , lt.lab24c1 AS ltlab24c1 " //muestra id
                            + "       , lt.lab24c9 AS ltlab24c9 " //muestra codigo
                            + "       , lt.lab24c2 AS ltlab24c2 " //muestra nombre
                            + "       , lab45.lab45c1 " //unidad id
                            + "       , lab45.lab45c2 " //unidad nombre
                            + "       , lab45.lab45c3 " //unidad internacional
                            + "       , lab45.lab45c4 " //unidad factor de conversion
                            + "       , lab57.lab57c19 " //Usuario que valido
                            + "       , lab04.lab04c2 " //Nombre suario que valido
                            + "       , lab04.lab04c3 " //Apellido suario que valido
                            + "       , lab04.lab04c10 " //Identificación de Usuario que valido
                            + "       , lab04.lab04c13 " //Codigo de firma del usuario que valido
                            + "       , lab04.lab04c15 " //Tipo de usuario q valido
                            + "       , lab57.lab39c1 " //examen                    
                            + "       , lab900.lab904c1 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c2 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c3 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c4 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab904.lab904c1 " //Id Tarifa en tabla tarifa
                            + "       , lab904.lab904c2 " //Codigo de tarifa 
                            + "       , lab904.lab904c3 " //Nombre de Tarifa
                            + "       , lab39.lab39c2 " //código
                            + "       , lab39.lab39c3 " //abreviatura
                            + "       , lab39.lab39c4 " //nombre
                            + "       , lab39.lab39c32 " //Titulo de grupo
                            + "       , lab39.lab39c37 " //Tipo de prueba
                            + "       , lab39.lab39c42 " //Orden impresion
                            + "       , lab39.lab39c12 " //decimales
                            + "       , lab39.lab39c27 " //confidencial
                            + "       , lab39.lab39c11 " //tipo resultado
                            + "       , lab57c1 " //resultado
                            + "       , lab57c2 " //fecha resultado
                            + "       , lab57c3 " //usuario del resultado
                            + "       , lab57c5 " //usuario de ingreso
                            + "       , lab57c8 " //estado
                            + "       , lab57c16 " //estado muestra
                            + "       , lab57.lab57c9 " //patología
                            + "       , lab57.lab48c12 " //referecnia mínima
                            + "       , lab57.lab48c13 " //referec2nia máxima
                            + "       , lab57.lab48c5 " //panico minimo
                            + "       , lab57.lab48c6 " //panico máximo
                            + "       , lab57.lab48c14 " //reportable minimo
                            + "       , lab57.lab48c15 " //reportable máximo
                            + "       , lab57.lab57c22 " //
                            + "       , lab57.lab57c23 " //
                            + "       , lab57.lab57c25 " // impresion en informes
                            + "       , lab48c16 " //pánico crítico
                            + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                            + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                            + "       , lab57c32 " //tiene comentario
                            + "       , lab57c33 " //numero de repeticiones
                            + "       , lab57c24 " //numero modificacion
                            + "       , lab57c26 " //Tiene antibiograma
                            + "       , lab57c42 " //Tiene plantilla
                            + "       , lab95c1 " //comentario
                            + "       , lab95c2 " //fecha comentario
                            + "       , lab95c3 " //patología comentario
                            + "       , lab95.lab04c1 " //usuario comentario
                            + "       , lab57.lab57c27 " //delta mínimo
                            + "       , lab57.lab57c28 " //delta máximmo
                            + "       , lab57.lab57c6" //último resultado
                            + "       , lab57.lab57c7" //fecha último resultado
                            + "       , lab57.lab57c30" //penúltimo resultado
                            + "       , lab57.lab57c31" //fecha penúltimo resultado
                            + "       , lab57.lab57c18" //fecha validacion
                            + "       , lab57.lab57c19" //fecha validacion
                            + "       , lab57.lab57c4" //fecha ingreso
                            + "       , lt.lab24c10" //tipo laboratorio
                            + "       , lab57c35" //tipo ingreso resultado
                            + "       , lab57c36" //tipo ingreso microbiología
                            + "       , lab57c37" //Fecha verificación
                            + "       , lab57c38" //Id usuario verifica
                            + "       , lab57c39" //Fecha Toma
                            + "       , lab57c40" //Usuario toma
                            + "       , lab57c14" //Id Perfil Padre
                            + "       , lab57c10" //bloqueo
                            + "       , lab57c11" //bloqueo fecha
                            + "       , lab57c12" //bloqueo usuario
                            + "       , lab57c41" //Adjuntos del resultado
                            + "       , userBlock.lab04c2 AS lab04c2_3" //bloqueo usuario
                            + "       , userBlock.lab04c3 AS lab04c3_3" //bloqueo usuario
                            + "       , userBlock.lab04c4 AS lab04c4_3" //bloqueo usuario
                            + "       , lab69.lab69c1" //permitir acceso por el área
                            + "       , lab69.lab69c2" //permitir validacion
                            + "       , lab72.lab39c1 AS lab72_1" // excluir prueba
                            + "       , lab64.lab64c2" //Cód Tecnica
                            + "       , lab64.lab64c3" //Nombre tecnica
                            + "       , lab39P.lab39c1 AS lab39c1p" //Id perfil
                            + "       , lab39P.lab39c4 AS lab39c4p" //Nombre perfil
                            + "       , lab39P.lab39c42 AS lab39c42p" //Ordenamiento del perfil
                            + "       , lab57c43" //tiene delta
                            + "       , lab39.lab39c15" //Dias maximo de modificacion
                            + "       , lab39.lab39c41" //Dias maximo de modificacion de impresion
                            + "       , lab39.lab39c50" //Requiere validacion preliminar
                            + "       , lab39.lab39c10" //Formula,
                            + "       , lab57.lab57c20" //Fecha de validacion preliminar,
                            + "       , lab57.lab57c21" //Usuario de validacion preliminar,
                            + "       , lab57.lab57c48" //Ultimo resultado repetido
                            + "       , lab57.lab57c53" //Dilución
                            + "       , lab21c9"
                            + "       , lab21c10"
                            + "       , lab21.lab80c1"
                            + "       , lab80c3"
                            + "       , lab80c4"
                            + "       , lab80c5"
                            + "       , lab21.lab08c1"
                            + "       , lab08c2"
                            + "       , lab08c5"
                            + "       , lab57.lab40c1" //Laboratorio
                            + "       , lab40c2" //Laboratorio Código
                            + "       , lab40c3" //Laboratorio Nombre
                            + "       , lab21c2" //Historia
                            + "       , lab22.lab10c1" //Id servicio
                            + "       , lab10c2" //Servicio
                            + "       , lab57c55" //Hora portada medico
                            + "       , lab57c61" //tiene comentario interno
                            + "       , lab57c62" //resultado eliminado
                            + "       , lab57c71" //Comentario 2
                            + "";

                    String from = ""
                            + " FROM  " + lab57 + " AS lab57 "
                            + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                            + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                            + " INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                            + " LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 "
                            + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                            + " LEFT JOIN lab04 AS userBlock ON userBlock.lab04c1 = lab57.lab57c12 "
                            + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                            + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                            + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                            + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 "
                            + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 "
                            + " LEFT JOIN " + lab95 + " AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                            + " LEFT JOIN lab69 ON lab69.lab43c1 = lab39.lab43c1 AND lab69.lab04c1 = " + (resultFilter == null ? 0 : resultFilter.getUserId()) + " "
                            + " LEFT JOIN lab72 ON lab72.lab39c1 = lab39.lab39c1 AND lab72.lab04c1 = " + (resultFilter == null ? 0 : resultFilter.getUserId()) + " "
                            + " LEFT JOIN " + lab900 + " AS lab900 ON lab57.lab39c1 = lab900.lab39c1 and lab57.lab22c1 = lab900.lab22c1"
                            + " LEFT JOIN lab904 ON lab900.lab904c1 = lab904.lab904c1 "
                            + " LEFT JOIN " + lab22 + " AS lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                            + " LEFT JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                            + " LEFT JOIN Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                            + " LEFT JOIN Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                            + " LEFT JOIN lab10 ON lab22.lab10c1 = lab10.lab10c1 "
                            + " LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ";

                    String where = " WHERE lab39.lab39c37 = 0  AND (lab22c19 = 0 or lab22c19 is null) ";

                    List<Object> params = new ArrayList<>();

                    if (resultFilter != null)
                    {
                        if (resultFilter.isApplyGrowth())
                        {
                            select += ""
                                    + ", lab57.lab24c1 "
                                    + ", lab24.lab24c2 "
                                    + ", lab24.lab24c9 "
                                    + ", lab57.lab158c1 "
                                    + ", lab158.lab158c2 "
                                    + ", lab158.lab158c3 "
                                    + ", lab57.lab201c1 "
                                    + ", lab201.lab201c2 "
                                    + ", lab200.lab200c2 "
                                    + ", lab200.lab200c3 "
                                    + ", userGrowth.lab04c1 AS lab04c1_1 "
                                    + ", userGrowth.lab04c2 AS lab04c2_1 "
                                    + ", userGrowth.lab04c3 AS lab04c3_1 "
                                    + ", userGrowth.lab04c4 AS lab04c4_1 ";

                            from += ""
                                    + "LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 "
                                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab57.lab24c1_1 "
                                    + "LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 "
                                    + "LEFT JOIN lab201 ON lab201.lab201c1 = lab57.lab201c1 "
                                    + "LEFT JOIN lab04 AS userGrowth ON userGrowth.lab04c1 = lab200.lab04c1_1 ";
                        }

                        switch (resultFilter.getFilterId())
                        {
                            case 1:
                                where = where + " AND lab39.lab39c37 != 2 AND lab57.lab22c1 BETWEEN ? AND ?";
                                params.add(resultFilter.getFirstOrder());
                                params.add(resultFilter.getLastOrder());
                                break;
                            case 0:
                                where = where + " AND lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ?";
                                params.add(resultFilter.getFirstDate());
                                params.add(resultFilter.getLastDate());
                                break;
                            default:
                                break;
                        }

                        if (sampleState != null)
                        {
                            where = where + " AND  (lab57.lab57c16 = 1 OR lab57.lab57c16 = " + sampleState + " )";
                        }

                        if (resultFilter.getTestsId() != null && !resultFilter.getTestsId().isEmpty())
                        {
                            if (resultFilter.getTestsId().size() > 1)
                            {
                                String in = resultFilter.getTestsId().stream()
                                        .map(testId -> testId.toString())
                                        .collect(Collectors.joining(","));
                                where += " AND lab39.lab39c1 IN (" + in + ") ";
                            } else
                            {
                                where += " AND lab39.lab39c1 = ? ";
                                params.add(resultFilter.getTestsId().get(0));
                            }
                        }

                        //Filtro para examenes confidenciales segun permisos del usuario
                        if (resultFilter.getUser() != null)
                        {
                            if (!resultFilter.getUser().isConfidential())
                            {
                                where += " AND lab39.lab39c27 = 0 ";
                            }
                        } else
                        {
                            if (!getUser().isConfidential())
                            {
                                where += " AND lab39.lab39c27 = 0 ";
                            }
                        }
                    }
                    if (orderIds != null && !orderIds.isEmpty())
                    {
                        if (orderIds.size() > 1)
                        {
                            where = where + " AND  lab57.lab22c1 BETWEEN ? AND ? ";
                            params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                            params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                        } else
                        {
                            where = where + " AND lab57.lab22c1 = ? ";
                            params.add(orderIds.get(0));
                        }
                    }

                    if (resultFilter != null)
                    {
                        if (!resultFilter.getTestStates().isEmpty())
                        {
                            where = where + " AND lab57.lab57c8 in (" + resultFilter.getTestStates().stream().map((t) -> t.toString()).collect(Collectors.joining(",")) + ")";
                        }
                    }

                    RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int index) ->
                    {
                        ResultTest bean = new ResultTest();
                        bean.setOrder(rs.getLong("lab22c1"));
                        bean.setProfileId(rs.getInt("lab39c1p"));
                        bean.setProfileName(rs.getString("lab39c4p"));
                        bean.setPrintSortProfile(rs.getInt("lab39c42p"));
                        bean.setTestId(rs.getInt("lab39c1"));
                        bean.setTestCode(rs.getString("lab39c2"));
                        bean.setTestName(rs.getString("lab39c4"));
                        bean.setTestType(rs.getInt("lab39c37"));
                        bean.setFormula(rs.getString("lab39c10"));

                        BillingTest billing = new BillingTest();
                        billing.setServicePrice(rs.getBigDecimal("lab900c2"));
                        billing.setPatientPrice(rs.getBigDecimal("lab900c3"));
                        billing.setInsurancePrice(rs.getBigDecimal("lab900c4"));
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        billing.setRate(rate);
                        bean.setBilling(billing);

                        bean.setGroupTitle(rs.getString("lab39c32"));
                        bean.setPrintSort(rs.getInt("lab39c42"));
                        bean.setEntry(rs.getString("lab57c14") == null);
                        bean.setSampleId(rs.getInt("ltlab24c1"));
                        bean.setSampleCode(rs.getString("ltlab24c9"));
                        bean.setSampleName(rs.getString("ltlab24c2"));
                        bean.setEntryDate(rs.getTimestamp("lab57c4"));
                        bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                        bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));
                        bean.setAttachmentTest(rs.getInt("lab57c41"));
                        bean.setMaxDays(rs.getInt("lab39c15"));
                        bean.setMaxPrintDays(rs.getInt("lab39c41"));
                        bean.setPreliminaryValidation(rs.getInt("lab39c50") == 1);
                        bean.setTechnique(rs.getString("lab64c3"));
                        bean.setCommentResult(rs.getString("lab57c71"));

                        bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                        bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                        //TODO: Desencriptar el resultado
                        bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                        bean.setPreviousResult(Tools.decrypt(rs.getString("lab57c62")));
                        bean.setResultDate(rs.getTimestamp("lab57c2"));
                        bean.setResultUserId(rs.getInt("lab57c3"));

                        bean.setPreliminaryDate(rs.getTimestamp("lab57c20"));
                        bean.setPreliminaryUser(rs.getInt("lab57c21"));

                        bean.setRepeatedResultValue(Tools.decrypt(rs.getString("lab57c48")));

                        bean.setValidationDate(rs.getTimestamp("lab57c18"));
                        bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                        bean.setPrintDate(rs.getTimestamp("lab57c22"));
                        bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                        bean.setTakenDate(rs.getTimestamp("lab57c39"));
                        bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                        bean.setEntryType(rs.getShort("lab57c35"));
                        bean.setState(rs.getInt("lab57c8"));
                        bean.setSampleState(rs.getInt("lab57c16"));
                        bean.setAreaId(rs.getInt("lab43c1"));
                        bean.setAreaCode(rs.getString("lab43c2"));
                        bean.setAreaAbbr(rs.getString("lab43c3"));
                        bean.setAreaName(rs.getString("lab43c4"));
                        bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                        bean.setResultType(rs.getShort("lab39c11"));
                        bean.setPathology(rs.getInt("lab57c9"));
                        bean.setRefLiteral(rs.getString("lab50c2n"));
                        bean.setPanicLiteral(rs.getString("lab50c2p"));
                        bean.setConfidential(rs.getInt("lab39c27") == 1);
                        bean.setRepeatAmmount(rs.getInt("lab57c33"));
                        bean.setModificationAmmount(rs.getInt("lab57c24"));
                        bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                        bean.setHasTemplate(rs.getInt("lab57c42") == 1);
                        bean.setDilution(rs.getBoolean("lab57c53"));
                        bean.setPrint(rs.getInt("lab57c25"));

                        BigDecimal refMin = rs.getBigDecimal("lab48c12");
                        BigDecimal refMax = rs.getBigDecimal("lab48c13");

                        if (!rs.wasNull())
                        {
                            bean.setRefMin(refMin);
                            bean.setRefMax(refMax);

                            String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                            String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                            bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                        } else
                        {
                            bean.setRefMin(BigDecimal.ZERO);
                            bean.setRefMax(BigDecimal.ZERO);
                            bean.setRefInterval(bean.getRefLiteral());
                        }

                        BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                        BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                        if (!rs.wasNull())
                        {
                            bean.setPanicMin(panicMin);
                            bean.setPanicMax(panicMax);
                            String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                            String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                            bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                        } else
                        {
                            bean.setPanicMin(BigDecimal.ZERO);
                            bean.setPanicMax(BigDecimal.ZERO);
                            bean.setPanicInterval(bean.getPanicLiteral());
                        }

                        BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                        BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                        if (!rs.wasNull())
                        {
                            bean.setReportedMin(reportedMin);
                            bean.setReportedMax(reportedMax);

                            String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                            String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                            bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                        } else
                        {
                            bean.setReportedMin(BigDecimal.ZERO);
                            bean.setReportedMax(BigDecimal.ZERO);
                        }

                        BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                        BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                        if (!rs.wasNull())
                        {
                            bean.setDeltaMin(deltaMin);
                            bean.setDeltaMax(deltaMax);
                            bean.setDeltaInterval(deltaMin + " - " + deltaMax);
                        } else
                        {
                            bean.setDeltaMin(BigDecimal.ZERO);
                            bean.setDeltaMax(BigDecimal.ZERO);
                            bean.setDeltaInterval("");
                        }

                        bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                        bean.setLastResultDate(rs.getTimestamp("lab57c7"));
                        bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                        bean.setSecondLastResultDate(rs.getTimestamp("lab57c31"));

                        bean.setCritic(rs.getShort("lab48c16"));
                        bean.setUnitId(rs.getInt("lab45c1"));
                        bean.setUnit(rs.getString("lab45c2"));
                        bean.setUnitInternational(rs.getString("lab45c3"));
                        bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                        bean.setAbbreviation(rs.getString("lab39c3"));
                        bean.setDigits(rs.getShort("lab39c12"));
                        bean.setHasComment(rs.getShort("lab57c32") == 1);
                        bean.setHasCommentInternal(rs.getShort("lab57c61") == 1);

                        //Usuario que valido
                        if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                        {
                            bean.setValidationUserName(rs.getString("lab04c2"));
                            bean.setValidationUserLastName(rs.getString("lab04c3"));
                            bean.setValidationUserIdentification(rs.getString("lab04c10"));
                            bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                            bean.setValidationUserType(rs.getInt("lab04c15"));
                        }

                        ResultTestComment comment = new ResultTestComment();
                        comment.setOrder(rs.getLong("lab22c1"));
                        comment.setTestId(rs.getInt("lab39c1"));
                        comment.setComment(rs.getString("lab95c1"));
                        comment.setCommentDate(rs.getDate("lab95c2"));
                        comment.setPathology(rs.getShort("lab95c3"));
                        comment.setUserId(rs.getInt("lab04c1"));
                        comment.setCommentChanged(false);
                        bean.setResultComment(comment);

                        if (resultFilter != null && resultFilter.isApplyGrowth())
                        {
                            MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
                            microbiologyGrowth.setOrder(null);
                            microbiologyGrowth.setSubSample(null);
                            microbiologyGrowth.setTest(null);
                            if (rs.getString("lab24c1") != null)
                            {
                                microbiologyGrowth.getSample().setId(rs.getInt("lab24c1"));
                                microbiologyGrowth.getSample().setName(rs.getString("lab24c2"));
                                microbiologyGrowth.getSample().setCodesample(rs.getString("lab24c9"));
                            } else
                            {
                                microbiologyGrowth.setSample(null);
                            }

                            if (rs.getString("lab158c1") != null)
                            {
                                microbiologyGrowth.getAnatomicalSite().setId(rs.getInt("lab158c1"));
                                microbiologyGrowth.getAnatomicalSite().setName(rs.getString("lab158c2"));
                                microbiologyGrowth.getAnatomicalSite().setAbbr(rs.getString("lab158c3"));
                            } else
                            {
                                microbiologyGrowth.setAnatomicalSite(null);
                            }

                            if (rs.getString("lab201c1") != null)
                            {
                                microbiologyGrowth.getCollectionMethod().setId(rs.getInt("lab201c1"));
                                microbiologyGrowth.getCollectionMethod().setName(rs.getString("lab201c2"));
                            } else
                            {
                                microbiologyGrowth.setCollectionMethod(null);
                            }

                            /*Usuario Siembra*/
                            if (rs.getString("lab04c1_1") != null)
                            {
                                microbiologyGrowth.getUserGrowth().setId(rs.getInt("lab04c1_1"));
                                microbiologyGrowth.getUserGrowth().setName(rs.getString("lab04c2_1"));
                                microbiologyGrowth.getUserGrowth().setLastName(rs.getString("lab04c3_1"));
                                microbiologyGrowth.getUserGrowth().setUserName(rs.getString("lab04c4_1"));
                            } else
                            {
                                microbiologyGrowth.setUserGrowth(null);
                            }

                            microbiologyGrowth.setLastTransaction(rs.getTimestamp("lab200c2"));
                            microbiologyGrowth.setDateGrowth(rs.getTimestamp("lab200c3"));

                            bean.setMicrobiologyGrowth(microbiologyGrowth);
                        }

                        bean.setLaboratoryType(rs.getString("lab24c10"));

                        bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                        ResultTestBlock block = new ResultTestBlock();
                        block.setOrder(bean.getOrder());
                        block.setTestId(bean.getTestId());
                        block.setBlocked(rs.getShort("lab57c10") == 1);
                        block.setDate(rs.getTimestamp("lab57c11"));
                        AuthorizedUser userBlock = new AuthorizedUser(rs.getInt("lab57c12"));
                        userBlock.setName(rs.getString("lab04c2_3"));
                        userBlock.setLastName(rs.getString("lab04c3_3"));
                        userBlock.setUserName(rs.getString("lab04c4_3"));
                        block.setUser(userBlock);
                        bean.setBlock(block);

                        bean.setGrantAccess(rs.getShort("lab69c1") == 1);
                        if (bean.isGrantAccess())
                        {
                            bean.setGrantAccess(rs.getInt("lab72_1") == 0);
                        }
                        bean.setGrantValidate(bean.isGrantAccess() && rs.getShort("lab69c2") == 1);
                        bean.setDelta(rs.getShort("lab57c43") == 1);

                        Patient patient = new Patient();
                        Item item = new Item();
                        item.setId(rs.getInt("lab80c1"));
                        item.setCode(rs.getString("lab80c3"));
                        item.setEsCo(rs.getString("lab80c4"));
                        item.setEnUsa(rs.getString("lab80c5"));
                        patient.setSex(item);

                        if (rs.getString("lab08c1") != null)
                        {
                            Race race = new Race();
                            race.setId(rs.getInt("lab08c1"));
                            race.setCode(rs.getString("lab08c5"));
                            race.setName(rs.getString("lab08c2"));
                            patient.setRace(race);
                        }

                        patient.setSize(rs.getBigDecimal("lab21c9"));
                        patient.setWeight(rs.getBigDecimal("lab21c10"));
                        patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));

                        bean.setPatient(patient);

                        bean.setLaboratoryId(rs.getInt("lab40c1"));
                        bean.setLaboratoryCode(rs.getString("lab40c2"));
                        bean.setLaboratoryName(rs.getString("lab40c3"));
                        bean.setServiceId(rs.getInt("lab10c1"));
                        bean.setService(rs.getString("lab10c2"));

                        bean.setReportedDoctor(rs.getTimestamp("lab57c55"));

                        listResults.add(bean);
                        return bean;
                    };
                    getJdbcTemplate().query(select + from + where, mapper, params.toArray());
                }
            }
            return listResults;
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * ordenes aplicado por el usuario
     * @param orderIds Id´s de ordenes
     * @param sampleState
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> listResultEncrypt(final ResultFilter resultFilter, List<Long> orderIds, Integer sampleState, int yearsQuery) throws Exception
    {

        try
        {
            List<ResultTest> listResults = new LinkedList<>();
            List<Integer> years = new LinkedList<>();

            if (orderIds == null || orderIds.isEmpty())
            {
                Long vInitial = (resultFilter.getFirstOrder() == -1 || resultFilter.getFirstOrder() == 0) ? Long.parseLong(String.valueOf(resultFilter.getFirstDate())) : resultFilter.getFirstOrder();
                Long vFinal = (resultFilter.getLastOrder() == 1 || resultFilter.getLastOrder() == 0) ? Long.parseLong(String.valueOf(resultFilter.getLastDate())) : resultFilter.getLastOrder();
                years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            } else
            {
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
            }

            String lab22;
            String lab57;
            String lab95;
            String lab900;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab95) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab900) : tableExists;
                if (tableExists)
                {
                    String select = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   lab57.lab22c1 "
                            + "       , lab43.lab43c1 " //area id
                            + "       , lab43.lab43c2 " //area codigo
                            + "       , lab43.lab43c3 " //area abreviatura
                            + "       , lab43.lab43c4 " //area nombre
                            + "       , lab43.lab43c8 " //area validación parcial
                            + "       , lt.lab24c1 AS ltlab24c1 " //muestra id
                            + "       , lt.lab24c9 AS ltlab24c9 " //muestra codigo
                            + "       , lt.lab24c2 AS ltlab24c2 " //muestra nombre
                            + "       , lab45.lab45c1 " //unidad id
                            + "       , lab45.lab45c2 " //unidad nombre
                            + "       , lab45.lab45c3 " //unidad internacional
                            + "       , lab45.lab45c4 " //unidad factor de conversion
                            + "       , lab57.lab57c19 " //Usuario que valido
                            + "       , lab04.lab04c2 " //Nombre suario que valido
                            + "       , lab04.lab04c3 " //Apellido suario que valido
                            + "       , lab04.lab04c10 " //Identificación de Usuario que valido
                            + "       , lab04.lab04c13 " //Codigo de firma del usuario que valido
                            + "       , lab04.lab04c15 " //Tipo de usuario q valido
                            + "       , lab57.lab39c1 " //examen                    
                            + "       , lab900.lab904c1 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c2 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c3 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab900.lab900c4 " //Id Tarifa en tabla detalle examen precio
                            + "       , lab904.lab904c1 " //Id Tarifa en tabla tarifa
                            + "       , lab904.lab904c2 " //Codigo de tarifa 
                            + "       , lab904.lab904c3 " //Nombre de Tarifa
                            + "       , lab39.lab39c2 " //código
                            + "       , lab39.lab39c3 " //abreviatura
                            + "       , lab39.lab39c4 " //nombre
                            + "       , lab39.lab39c32 " //Titulo de grupo
                            + "       , lab39.lab39c37 " //Tipo de prueba
                            + "       , lab39.lab39c42 " //Orden impresion
                            + "       , lab39.lab39c12 " //decimales
                            + "       , lab39.lab39c27 " //confidencial
                            + "       , lab39.lab39c11 " //tipo resultado
                            + "       , lab57c1 " //resultado
                            + "       , lab57c2 " //fecha resultado
                            + "       , lab57c3 " //usuario del resultado
                            + "       , lab57c5 " //usuario de ingreso
                            + "       , lab57c8 " //estado
                            + "       , lab57c16 " //estado muestra
                            + "       , lab57.lab57c9 " //patología
                            + "       , lab57.lab48c12 " //referecnia mínima
                            + "       , lab57.lab48c13 " //referec2nia máxima
                            + "       , lab57.lab48c5 " //panico minimo
                            + "       , lab57.lab48c6 " //panico máximo
                            + "       , lab57.lab48c14 " //reportable minimo
                            + "       , lab57.lab48c15 " //reportable máximo
                            + "       , lab57.lab57c22 " //
                            + "       , lab57.lab57c23 " //
                            + "       , lab57.lab57c25 " // impresion en informes
                            + "       , lab48c16 " //pánico crítico
                            + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                            + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                            + "       , lab57c32 " //tiene comentario
                            + "       , lab57c33 " //numero de repeticiones
                            + "       , lab57c24 " //numero modificacion
                            + "       , lab57c26 " //Tiene antibiograma
                            + "       , lab57c42 " //Tiene plantilla
                            + "       , lab95c1 " //comentario
                            + "       , lab95c2 " //fecha comentario
                            + "       , lab95c3 " //patología comentario
                            + "       , lab95.lab04c1 " //usuario comentario
                            + "       , lab57.lab57c27 " //delta mínimo
                            + "       , lab57.lab57c28 " //delta máximmo
                            + "       , lab57.lab57c6" //último resultado
                            + "       , lab57.lab57c7" //fecha último resultado
                            + "       , lab57.lab57c30" //penúltimo resultado
                            + "       , lab57.lab57c31" //fecha penúltimo resultado
                            + "       , lab57.lab57c18" //fecha validacion
                            + "       , lab57.lab57c19" //fecha validacion
                            + "       , lab57.lab57c4" //fecha ingreso
                            + "       , lt.lab24c10" //tipo laboratorio
                            + "       , lab57c35" //tipo ingreso resultado
                            + "       , lab57c36" //tipo ingreso microbiología
                            + "       , lab57c37" //Fecha verificación
                            + "       , lab57c38" //Id usuario verifica
                            + "       , lab57c39" //Fecha Toma
                            + "       , lab57c40" //Usuario toma
                            + "       , lab57c14" //Id Perfil Padre
                            + "       , lab57c10" //bloqueo
                            + "       , lab57c11" //bloqueo fecha
                            + "       , lab57c12" //bloqueo usuario
                            + "       , lab57c41" //Adjuntos del resultado
                            + "       , userBlock.lab04c2 AS lab04c2_3" //bloqueo usuario
                            + "       , userBlock.lab04c3 AS lab04c3_3" //bloqueo usuario
                            + "       , userBlock.lab04c4 AS lab04c4_3" //bloqueo usuario
                            + "       , lab69.lab69c1" //permitir acceso por el área
                            + "       , lab69.lab69c2" //permitir validacion
                            + "       , lab72.lab39c1 AS lab72_1" // excluir prueba
                            + "       , lab64.lab64c2" //Cód Tecnica
                            + "       , lab64.lab64c3" //Nombre tecnica
                            + "       , lab39P.lab39c1 AS lab39c1p" //Id perfil
                            + "       , lab39P.lab39c4 AS lab39c4p" //Nombre perfil
                            + "       , lab39P.lab39c42 AS lab39c42p" //Ordenamiento del perfil
                            + "       , lab57c43" //tiene delta
                            + "       , lab39.lab39c15" //Dias maximo de modificacion
                            + "       , lab39.lab39c41" //Dias maximo de modificacion de impresion
                            + "       , lab39.lab39c50" //Requiere validacion preliminar
                            + "       , lab39.lab39c10" //Formula,
                            + "       , lab57.lab57c20" //Fecha de validacion preliminar,
                            + "       , lab57.lab57c21" //Usuario de validacion preliminar,
                            + "       , lab57.lab57c48" //Ultimo resultado repetido
                            + "       , lab57.lab57c53" //Dilución
                            + "       , lab21c9"
                            + "       , lab21c10"
                            + "       , lab21.lab80c1"
                            + "       , lab80c3"
                            + "       , lab80c4"
                            + "       , lab80c5"
                            + "       , lab21.lab08c1"
                            + "       , lab08c2"
                            + "       , lab08c5"
                            + "       , lab57.lab40c1" //Laboratorio
                            + "       , lab21c2" //Historia
                            + "       , lab22.lab10c1" //Id servicio
                            + "       , lab10c2" //Servicio
                            + "       , lab57c55" //Hora portada medico
                            + "       , lab57c61" //tiene comentario interno
                            + "       , lab57c62" //resultado eliminado
                            + "       , lab57c71" //Comentario 2
                            + "       , lab57c54" //Remision
                            + "";

                    String from = ""
                            + " FROM  " + lab57 + " AS lab57 "
                            + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                            + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                            + " INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                            + " LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 "
                            + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                            + " LEFT JOIN lab04 AS userBlock ON userBlock.lab04c1 = lab57.lab57c12 "
                            + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                            + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                            + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                            + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 "
                            + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 "
                            + " LEFT JOIN " + lab95 + " AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                            + " LEFT JOIN lab69 ON lab69.lab43c1 = lab39.lab43c1 AND lab69.lab04c1 = " + (resultFilter == null ? 0 : resultFilter.getUserId()) + " "
                            + " LEFT JOIN lab72 ON lab72.lab39c1 = lab39.lab39c1 AND lab72.lab04c1 = " + (resultFilter == null ? 0 : resultFilter.getUserId()) + " "
                            + " LEFT JOIN " + lab900 + " AS lab900 ON lab57.lab39c1 = lab900.lab39c1 and lab57.lab22c1 = lab900.lab22c1"
                            + " LEFT JOIN lab904 ON lab900.lab904c1 = lab904.lab904c1 "
                            + " LEFT JOIN " + lab22 + " AS lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                            + " LEFT JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                            + " LEFT JOIN Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                            + " LEFT JOIN Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                            + " LEFT JOIN lab10 ON lab22.lab10c1 = lab10.lab10c1 ";

                    List<Object> params = new ArrayList<>();
                    StringBuilder where = new StringBuilder("");
                    where.append(" WHERE lab39.lab39c37 = 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                    if (resultFilter != null)
                    {
                        if (resultFilter.isApplyGrowth())
                        {
                            select += ""
                                    + ", lab57.lab24c1 "
                                    + ", lab24.lab24c2 "
                                    + ", lab24.lab24c9 "
                                    + ", lab57.lab158c1 "
                                    + ", lab158.lab158c2 "
                                    + ", lab158.lab158c3 "
                                    + ", lab57.lab201c1 "
                                    + ", lab201.lab201c2 "
                                    + ", lab200.lab200c2 "
                                    + ", lab200.lab200c3 "
                                    + ", userGrowth.lab04c1 AS lab04c1_1 "
                                    + ", userGrowth.lab04c2 AS lab04c2_1 "
                                    + ", userGrowth.lab04c3 AS lab04c3_1 "
                                    + ", userGrowth.lab04c4 AS lab04c4_1 ";

                            from += ""
                                    + "LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 "
                                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab57.lab24c1_1 "
                                    + "LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 "
                                    + "LEFT JOIN lab201 ON lab201.lab201c1 = lab57.lab201c1 "
                                    + "LEFT JOIN lab04 AS userGrowth ON userGrowth.lab04c1 = lab200.lab04c1_1 ";
                        }

                        switch (resultFilter.getFilterId())
                        {
                            case 1:
                                where.append(" AND lab39.lab39c37 != 2 AND lab57.lab22c1 BETWEEN ? AND ?");
                                params.add(resultFilter.getFirstOrder());
                                params.add(resultFilter.getLastOrder());
                                break;
                            case 0:
                                where.append(" AND lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ?");
                                params.add(resultFilter.getFirstDate());
                                params.add(resultFilter.getLastDate());
                                break;
                            default:
                                break;
                        }

                        if (sampleState != null)
                        {
                             where.append(" AND  (lab57.lab57c16 = 1 OR lab57.lab57c16 = ").append(sampleState).append( " )");
                        }

                        if (resultFilter.getTestsId() != null && !resultFilter.getTestsId().isEmpty())
                        {
                            if (resultFilter.getTestsId().size() > 1)
                            {
                                where.append(" AND lab39.lab39c1 IN (").append(resultFilter.getTestsId().stream().map(testId -> testId.toString()).collect(Collectors.joining(","))).append(") ");
                            } else
                            {
                                where.append(" AND lab39.lab39c1 = ? ");
                                params.add(resultFilter.getTestsId().get(0));
                            }
                        }

                        //Filtro para examenes confidenciales segun permisos del usuario
                        if (resultFilter.getUser() != null)
                        {
                            if (!resultFilter.getUser().isConfidential())
                            {
                                where.append(" AND lab39.lab39c27 = 0 ");
                            }
                        } else
                        {
                            if (!getUser().isConfidential())
                            {
                                where.append(" AND lab39.lab39c27 = 0 ");
                            }
                        }

                    }
                    if (orderIds != null && !orderIds.isEmpty())
                    {
                        if (orderIds.size() > 1)
                        {
                            where.append(" AND  lab57.lab22c1 BETWEEN ? AND ? ");
                            params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                            params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                        } else
                        {
                            where.append(" AND lab57.lab22c1 = ? ");
                            params.add(orderIds.get(0));
                        }
                    }

                    if (resultFilter != null)
                    {
                        if (!resultFilter.getTestStates().isEmpty())
                        {
                            where.append(" AND lab57.lab57c8 in (").append(resultFilter.getTestStates().stream().map((t) -> t.toString()).collect(Collectors.joining(","))).append(") ");
                        }
                    }

                    //--------Filtro por demograficos
                    if (!resultFilter.getFilterByDemo().isEmpty())
                    {
                        resultFilter.getFilterByDemo().forEach((demographic) ->
                        {
                            where.append(SQLTools.buildSQLDemographicFilter(demographic, params));
                        });
                    }

                    RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int index) ->
                    {
                        ResultTest bean = new ResultTest();
                        bean.setOrder(rs.getLong("lab22c1"));
                        bean.setProfileId(rs.getInt("lab39c1p"));
                        bean.setProfileName(rs.getString("lab39c4p"));
                        bean.setPrintSortProfile(rs.getInt("lab39c42p"));
                        bean.setTestId(rs.getInt("lab39c1"));
                        bean.setTestCode(rs.getString("lab39c2"));
                        bean.setTestName(rs.getString("lab39c4"));
                        bean.setTestType(rs.getInt("lab39c37"));
                        bean.setFormula(rs.getString("lab39c10"));

                        BillingTest billing = new BillingTest();
                        billing.setServicePrice(rs.getBigDecimal("lab900c2"));
                        billing.setPatientPrice(rs.getBigDecimal("lab900c3"));
                        billing.setInsurancePrice(rs.getBigDecimal("lab900c4"));
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        billing.setRate(rate);
                        bean.setBilling(billing);

                        bean.setGroupTitle(rs.getString("lab39c32"));
                        bean.setPrintSort(rs.getInt("lab39c42"));
                        bean.setEntry(rs.getString("lab57c14") == null);
                        bean.setSampleId(rs.getInt("ltlab24c1"));
                        bean.setSampleCode(rs.getString("ltlab24c9"));
                        bean.setSampleName(rs.getString("ltlab24c2"));
                        bean.setEntryDate(rs.getTimestamp("lab57c4"));
                        bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                        bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));
                        bean.setAttachmentTest(rs.getInt("lab57c41"));
                        bean.setMaxDays(rs.getInt("lab39c15"));
                        bean.setMaxPrintDays(rs.getInt("lab39c41"));
                        bean.setPreliminaryValidation(rs.getInt("lab39c50") == 1);
                        bean.setTechnique(rs.getString("lab64c3"));
                        bean.setCommentResult(rs.getString("lab57c71"));
                        bean.setRemission(rs.getInt("lab57c54") == 1);

                        bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                        bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                        //TODO: Desencriptar el resultado
                        bean.setResultencript(Tools.decrypt(rs.getString("lab57c1")));
                        bean.setPreviousResult(Tools.decrypt(rs.getString("lab57c62")));
                        bean.setResultDate(rs.getTimestamp("lab57c2"));
                        bean.setResultUserId(rs.getInt("lab57c3"));

                        bean.setPreliminaryDate(rs.getTimestamp("lab57c20"));
                        bean.setPreliminaryUser(rs.getInt("lab57c21"));

                        bean.setRepeatedResultValue(Tools.decrypt(rs.getString("lab57c48")));

                        bean.setValidationDate(rs.getTimestamp("lab57c18"));
                        bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                        bean.setPrintDate(rs.getTimestamp("lab57c22"));
                        bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                        bean.setTakenDate(rs.getTimestamp("lab57c39"));
                        bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                        bean.setEntryType(rs.getShort("lab57c35"));
                        bean.setState(rs.getInt("lab57c8"));
                        bean.setSampleState(rs.getInt("lab57c16"));
                        bean.setAreaId(rs.getInt("lab43c1"));
                        bean.setAreaCode(rs.getString("lab43c2"));
                        bean.setAreaAbbr(rs.getString("lab43c3"));
                        bean.setAreaName(rs.getString("lab43c4"));
                        bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                        bean.setResultType(rs.getShort("lab39c11"));
                        bean.setPathology(rs.getInt("lab57c9"));
                        bean.setRefLiteral(rs.getString("lab50c2n"));
                        bean.setPanicLiteral(rs.getString("lab50c2p"));
                        bean.setConfidential(rs.getInt("lab39c27") == 1);
                        bean.setRepeatAmmount(rs.getInt("lab57c33"));
                        bean.setModificationAmmount(rs.getInt("lab57c24"));
                        bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                        bean.setHasTemplate(rs.getInt("lab57c42") == 1);
                        bean.setDilution(rs.getBoolean("lab57c53"));
                        bean.setPrint(rs.getInt("lab57c25"));

                        BigDecimal refMin = rs.getBigDecimal("lab48c12");
                        BigDecimal refMax = rs.getBigDecimal("lab48c13");

                        if (!rs.wasNull())
                        {
                            bean.setRefMin(refMin);
                            bean.setRefMax(refMax);

                            String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                            String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                            bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                        } else
                        {
                            bean.setRefMin(BigDecimal.ZERO);
                            bean.setRefMax(BigDecimal.ZERO);
                            bean.setRefInterval(bean.getRefLiteral());
                        }

                        BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                        BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                        if (!rs.wasNull())
                        {
                            bean.setPanicMin(panicMin);
                            bean.setPanicMax(panicMax);
                            String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                            String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                            bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                        } else
                        {
                            bean.setPanicMin(BigDecimal.ZERO);
                            bean.setPanicMax(BigDecimal.ZERO);
                            bean.setPanicInterval(bean.getPanicLiteral());
                        }

                        BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                        BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                        if (!rs.wasNull())
                        {
                            bean.setReportedMin(reportedMin);
                            bean.setReportedMax(reportedMax);

                            String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                            String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                            bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                        } else
                        {
                            bean.setReportedMin(BigDecimal.ZERO);
                            bean.setReportedMax(BigDecimal.ZERO);
                        }

                        BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                        BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                        if (!rs.wasNull())
                        {
                            bean.setDeltaMin(deltaMin);
                            bean.setDeltaMax(deltaMax);
                            bean.setDeltaInterval(deltaMin + " - " + deltaMax);
                        } else
                        {
                            bean.setDeltaMin(BigDecimal.ZERO);
                            bean.setDeltaMax(BigDecimal.ZERO);
                            bean.setDeltaInterval("");
                        }

                        bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                        bean.setLastResultDate(rs.getTimestamp("lab57c7"));
                        bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                        bean.setSecondLastResultDate(rs.getTimestamp("lab57c31"));

                        bean.setCritic(rs.getShort("lab48c16"));
                        bean.setUnitId(rs.getInt("lab45c1"));
                        bean.setUnit(rs.getString("lab45c2"));
                        bean.setUnitInternational(rs.getString("lab45c3"));
                        bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                        bean.setAbbreviation(rs.getString("lab39c3"));
                        bean.setDigits(rs.getShort("lab39c12"));
                        bean.setHasComment(rs.getShort("lab57c32") == 1);
                        bean.setHasCommentInternal(rs.getShort("lab57c61") == 1);

                        //Usuario que valido
                        if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                        {
                            bean.setValidationUserName(rs.getString("lab04c2"));
                            bean.setValidationUserLastName(rs.getString("lab04c3"));
                            bean.setValidationUserIdentification(rs.getString("lab04c10"));
                            bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                            bean.setValidationUserType(rs.getInt("lab04c15"));
                        }

                        ResultTestComment comment = new ResultTestComment();
                        comment.setOrder(rs.getLong("lab22c1"));
                        comment.setTestId(rs.getInt("lab39c1"));
                        comment.setComment(rs.getString("lab95c1"));
                        comment.setCommentDate(rs.getDate("lab95c2"));
                        comment.setPathology(rs.getShort("lab95c3"));
                        comment.setUserId(rs.getInt("lab04c1"));
                        comment.setCommentChanged(false);
                        bean.setResultComment(comment);

                        if (resultFilter != null && resultFilter.isApplyGrowth())
                        {
                            MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
                            microbiologyGrowth.setOrder(null);
                            microbiologyGrowth.setSubSample(null);
                            microbiologyGrowth.setTest(null);
                            if (rs.getString("lab24c1") != null)
                            {
                                microbiologyGrowth.getSample().setId(rs.getInt("lab24c1"));
                                microbiologyGrowth.getSample().setName(rs.getString("lab24c2"));
                                microbiologyGrowth.getSample().setCodesample(rs.getString("lab24c9"));
                            } else
                            {
                                microbiologyGrowth.setSample(null);
                            }

                            if (rs.getString("lab158c1") != null)
                            {
                                microbiologyGrowth.getAnatomicalSite().setId(rs.getInt("lab158c1"));
                                microbiologyGrowth.getAnatomicalSite().setName(rs.getString("lab158c2"));
                                microbiologyGrowth.getAnatomicalSite().setAbbr(rs.getString("lab158c3"));
                            } else
                            {
                                microbiologyGrowth.setAnatomicalSite(null);
                            }

                            if (rs.getString("lab201c1") != null)
                            {
                                microbiologyGrowth.getCollectionMethod().setId(rs.getInt("lab201c1"));
                                microbiologyGrowth.getCollectionMethod().setName(rs.getString("lab201c2"));
                            } else
                            {
                                microbiologyGrowth.setCollectionMethod(null);
                            }

                            /*Usuario Siembra*/
                            if (rs.getString("lab04c1_1") != null)
                            {
                                microbiologyGrowth.getUserGrowth().setId(rs.getInt("lab04c1_1"));
                                microbiologyGrowth.getUserGrowth().setName(rs.getString("lab04c2_1"));
                                microbiologyGrowth.getUserGrowth().setLastName(rs.getString("lab04c3_1"));
                                microbiologyGrowth.getUserGrowth().setUserName(rs.getString("lab04c4_1"));
                            } else
                            {
                                microbiologyGrowth.setUserGrowth(null);
                            }

                            microbiologyGrowth.setLastTransaction(rs.getTimestamp("lab200c2"));
                            microbiologyGrowth.setDateGrowth(rs.getTimestamp("lab200c3"));

                            bean.setMicrobiologyGrowth(microbiologyGrowth);
                        }

                        bean.setLaboratoryType(rs.getString("lab24c10"));

                        bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                        ResultTestBlock block = new ResultTestBlock();
                        block.setOrder(bean.getOrder());
                        block.setTestId(bean.getTestId());
                        block.setBlocked(rs.getShort("lab57c10") == 1);
                        block.setDate(rs.getTimestamp("lab57c11"));
                        AuthorizedUser userBlock = new AuthorizedUser(rs.getInt("lab57c12"));
                        userBlock.setName(rs.getString("lab04c2_3"));
                        userBlock.setLastName(rs.getString("lab04c3_3"));
                        userBlock.setUserName(rs.getString("lab04c4_3"));
                        block.setUser(userBlock);
                        bean.setBlock(block);

                        bean.setGrantAccess(rs.getShort("lab69c1") == 1);
                        if (bean.isGrantAccess())
                        {
                            bean.setGrantAccess(rs.getInt("lab72_1") == 0);
                        }
                        bean.setGrantValidate(bean.isGrantAccess() && rs.getShort("lab69c2") == 1);
                        bean.setDelta(rs.getShort("lab57c43") == 1);

                        Patient patient = new Patient();
                        Item item = new Item();
                        item.setId(rs.getInt("lab80c1"));
                        item.setCode(rs.getString("lab80c3"));
                        item.setEsCo(rs.getString("lab80c4"));
                        item.setEnUsa(rs.getString("lab80c5"));
                        patient.setSex(item);

                        if (rs.getString("lab08c1") != null)
                        {
                            Race race = new Race();
                            race.setId(rs.getInt("lab08c1"));
                            race.setCode(rs.getString("lab08c5"));
                            race.setName(rs.getString("lab08c2"));
                            patient.setRace(race);
                        }

                        patient.setSize(rs.getBigDecimal("lab21c9"));
                        patient.setWeight(rs.getBigDecimal("lab21c10"));
                        patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));

                        bean.setPatient(patient);

                        bean.setLaboratoryId(rs.getInt("lab40c1"));
                        bean.setServiceId(rs.getInt("lab10c1"));
                        bean.setService(rs.getString("lab10c2"));

                        bean.setReportedDoctor(rs.getTimestamp("lab57c55"));

                        listResults.add(bean);
                        return bean;
                    };
                    getJdbcTemplate().query(select + from + where.toString(), mapper, params.toArray());
                }
            }
            return listResults;
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados.
     *
     * @param orderNumber
     * @param idTest
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default ResultTestComment getCommentResultTests(long orderNumber, int idTest) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab57.lab22c1 "
                    + "       , lab57.lab39c1 " //examen
                    + "       , lab57.lab57c1 " //resultado
                    + "       , lab95c1 " //comentario
                    + "       , lab95c2 " //fecha comentario
                    + "       , lab95c3 " //patología comentario
                    + "       , lab95.lab04c1 " //usuario comentario
                    + " FROM    lab57 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " WHERE lab57.lab22c1 = ?  and lab57.lab39c1 = ? ", new Object[]
                    {
                        orderNumber, idTest
                    }, (ResultSet rs, int i) ->
            {
                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setResult(Tools.decrypt(rs.getString("lab57c1")));
                comment.setCommentChanged(false);

                return comment;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return null;
        }
    }

    /**
     * Verifica si el resultado tiene comentarios.
     *
     * @param orderNumber
     * @param idTest
     *
     * @return boolean
     * @throws Exception Error en la base de datos.
     */
    default boolean checkCommentResult(long orderNumber, int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append(" SELECT * FROM lab95")
                    .append(" WHERE lab22c1 = ? ")
                    .append(" AND lab39c1 = ? ");
            return getJdbcTemplate().queryForObject(query.toString(), new Object[]
            {
                orderNumber, idTest
            }, (ResultSet rs, int i) ->
            {
                return true;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return false;
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param order Numero de Orden
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<ResultTest> list(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;

            String select = "SELECT   lab57.lab22c1 "
                    + "       , lab43.lab43c1 " //area id
                    + "       , lab43.lab43c2 " //area codigo
                    + "       , lab43.lab43c3 " //area abreviatura
                    + "       , lab43.lab43c4 " //area nombre
                    + "       , lab43.lab43c8 " //area validación parcial
                    + "       , lt.lab24c1 AS ltlab24c1 " //muestra id
                    + "       , lt.lab24c9 AS ltlab24c9 " //muestra codigo
                    + "       , lt.lab24c2 AS ltlab24c2 " //muestra nombre
                    + "       , lab45.lab45c1 " //unidad id
                    + "       , lab45.lab45c2 " //unidad nombre
                    + "       , lab45.lab45c3 " //unidad internacional
                    + "       , lab45.lab45c4 " //unidad factor de conversion
                    + "       , lab57.lab39c1 " //examen
                    + "       , lab39c2 " //código
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c4 " //nombre
                    + "       , lab39c37 " //nombre
                    + "       , lab39c16 " //dias Entrega
                    + "       , lab39c17 " //dias Procesamiento
                    + "       , lab57c1 " //resultado
                    + "       , lab57c2 " //fecha resultado
                    + "       , lab57c3 " //usuario del resultado
                    + "       , lab57c5 " //usuario de ingreso
                    + "       , lab57c8 " //estado
                    + "       , lab57c16 " //estado muestra
                    + "       , lab39c11 " //tipo resultado
                    + "       , lab57c9 " //patología
                    + "       , lab57.lab48c12 " //referecnia mínima
                    + "       , lab57.lab48c13 " //referec2nia máxima
                    + "       , lab57.lab48c5 " //panico minimo
                    + "       , lab57.lab48c6 " //panico máximo
                    + "       , lab57.lab48c14 " //reportable minimo
                    + "       , lab57.lab48c15 " //reportable máximo
                    + "       , lab57.lab57c22 " //
                    + "       , lab57.lab57c23 " //
                    + "       , lab48c16 " //pánico crítico
                    + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                    + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c12 " //decimales
                    + "       , lab39c27 " //confidencial
                    + "       , lab39c23 " //elimina de perfil
                    + "       , lab57c32 " //tiene comentario
                    + "       , lab57c33 " //numero de repeticiones
                    + "       , lab57c24 " //numero modificacion
                    + "       , lab95c1 " //comentario
                    + "       , lab95c2 " //fecha comentario
                    + "       , lab95c3 " //patología comentario
                    + "       , lab95.lab04c1 " //usuario comentario
                    + "       , lab57.lab57c27 " //delta mínimo
                    + "       , lab57.lab57c28 " //delta máximmo
                    + "       , lab57.lab57c6" //último resultado
                    + "       , lab57.lab57c7" //fecha último resultado
                    + "       , lab57.lab57c30" //penúltimo resultado
                    + "       , lab57.lab57c31" //fecha penúltimo resultado
                    + "       , lab57.lab57c18" //fecha validacion
                    + "       , lab57.lab57c19" //usuario validacion
                    + "       , lab04.lab04c2 " //Nombre suario que valido
                    + "       , lab04.lab04c3 " //Apellido suario que valido
                    + "       , lab04.lab04c10 " //Identificación de Usuario que valido
                    + "       , lab04.lab04c12 " //Firma del usuario que valido(Imagen)
                    + "       , lab04.lab04c13 " //Codigo de firma del usuario que valido
                    + "       , lab57.lab57c4" //fecha ingreso
                    + "       , lt.lab24c10" //tipo laboratorio
                    + "       , lab57c35" //tipo ingreso resultado
                    + "       , lab57c36" //tipo ingreso microbiología
                    + "       , lab57c37" //Fecha verificación
                    + "       , lab57c38" //Id usuario verifica
                    + "       , lab57c39" //Fecha Toma
                    + "       , lab57c40" //Usuario toma
                    + "       , lab57c42" //Indica si el examen tiene plantilla
                    + "       , lab57c14" //Id Perfil Padre
                    + "       , lab57c15" //Id Paquete Padre
                    + "       , lab39c28" //Resultado en el ingreso
                    + "       , lab57c50" //Envio al sistema externo
                    + "       , lab57c49" //Codigo de homologacion asignado
                    + "       , lab57c59" //Codigo de homologacion asignado
                    + "       , lab57c71" //Comentario 2
                    + "";

            String from = ""
                    + " FROM     " + lab57 + " as lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                    + " LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab48.lab50c1_3 "
                    + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab48.lab50c1_1 "
                    + " LEFT JOIN " + lab95 + " as lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab57.lab22c1 = ? ORDER BY lab57.lab57c4";
            List<Object> params = new ArrayList<>();
            params.add(order);
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));

                bean.setTestId(rs.getInt("lab39c1"));

                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setEntry(rs.getString("lab57c14") == null);
                bean.setDeleteProfile(rs.getInt("lab39c23"));
                bean.setProfileId(rs.getInt("lab57c14"));
                bean.setPackageId(rs.getInt("lab57c15"));
                bean.setResultRequest(rs.getInt("lab39c28") == 1);
                bean.setPrintsample(rs.getString("lab57c59") != null);

                if (rs.getString("ltlab24c1") != null)
                {
                    bean.setSampleId(rs.getInt("ltlab24c1"));
                    bean.setSampleCode(rs.getString("ltlab24c9"));
                    bean.setSampleName(rs.getString("ltlab24c2"));
                }
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));

                bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                //TODO: Desencriptar el resultado
                bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                bean.setResultDate(rs.getTimestamp("lab57c2"));
                bean.setResultUserId(rs.getInt("lab57c3"));

                bean.setValidationDate(rs.getTimestamp("lab57c18"));
                bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                bean.setPrintDate(rs.getTimestamp("lab57c22"));
                bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                bean.setTakenDate(rs.getTimestamp("lab57c39"));
                bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                bean.setEntryType(rs.getShort("lab57c35"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setDeliveryDays(rs.getInt("lab39c16"));
                bean.setProccessDays(rs.getString("lab39c17"));
                bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                bean.setResultType(rs.getShort("lab39c11"));
                bean.setPathology(rs.getInt("lab57c9"));
                bean.setRefLiteral(rs.getString("lab50c2n"));
                bean.setPanicLiteral(rs.getString("lab50c2p"));
                bean.setConfidential(rs.getInt("lab39c27") == 1);
                bean.setRepeatAmmount(rs.getInt("lab57c33"));
                bean.setModificationAmmount(rs.getInt("lab57c24"));
                bean.setHasTemplate(rs.getInt("lab57c42") == 1);

                //Usuario que valido
                if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                {
                    bean.setValidationUserName(rs.getString("lab04c2"));
                    bean.setValidationUserLastName(rs.getString("lab04c3"));
                    bean.setValidationUserIdentification(rs.getString("lab04c10"));
                    bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                    /*Firma*/
                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("lab04c12");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    bean.setValidationUserSignature(photo64);
                }

                BigDecimal refMin = rs.getBigDecimal("lab48c12");
                BigDecimal refMax = rs.getBigDecimal("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);
                    String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                    String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                    bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                } else
                {
                    bean.setRefMin(BigDecimal.ZERO);
                    bean.setRefMax(BigDecimal.ZERO);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);
                    String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                    String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                    bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                } else
                {
                    bean.setPanicMin(BigDecimal.ZERO);
                    bean.setPanicMax(BigDecimal.ZERO);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                    String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                    bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                } else
                {
                    bean.setReportedMin(BigDecimal.ZERO);
                    bean.setReportedMax(BigDecimal.ZERO);
                }

                BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(BigDecimal.ZERO);
                    bean.setDeltaMax(BigDecimal.ZERO);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnitId(rs.getInt("lab45c1"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setUnitInternational(rs.getString("lab45c3"));
                bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);
                bean.setLaboratoryType(rs.getString("lab24c10"));
                bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));
                bean.setSentCentralSystem(rs.getInt("lab57c50"));
                bean.setHomologationCode(rs.getString("lab57c49"));
                bean.setCommentResult(rs.getString("lab57c71"));
                return bean;
            };

            return getJdbcTemplate().query(select + from + where, mapper, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Reportar un resultado.
     *
     * @param obj Objeto de tipo ResultTest
     *
     * @return Objeto de tipo ResultTest modificado.
     * @throws Exception Error en la base de datos.
     */
    default ResultTest reported(ResultTest obj) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(obj.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String lab29 = year.equals(currentYear) ? "lab29" : "lab29_" + year;

            Date resultDate = new Date();

            if (obj.getResultComment().getCommentChanged())
            {
                if (checkCommentResult(obj.getOrder(), obj.getTestId()))
                {
                    String update = ""
                            + "UPDATE  " + lab95
                            + " SET    lab95c1 = ?,"
                            + "       lab95c2 = ?,"
                            + "       lab95c3 = ?,"
                            + "       lab95c4 = 0,"
                            + "       lab04c1 = ? ";
                    String where = ""
                            + " WHERE "
                            + " lab39c1 = ? "
                            + " AND lab22c1 = ? ";
                    getJdbcTemplate().update(update + where, new Object[]
                    {
                        obj.getResultComment().getComment(), resultDate, obj.getResultComment().getPathology(), obj.getUserId(), obj.getTestId(), obj.getOrder()
                    });
                } else
                {
                    SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getJdbcTemplate());
                    insertJdbc.withTableName(lab95);
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", obj.getOrder());
                    parameters.put("lab39c1", obj.getTestId());
                    parameters.put("lab95c1", obj.getResultComment().getComment());
                    parameters.put("lab95c2", resultDate);
                    parameters.put("lab95c3", obj.getResultComment().getPathology());
                    parameters.put("lab95c4", 0);
                    parameters.put("lab04c1", obj.getUserId());

                    insertJdbc.execute(parameters);
                }
                obj.setHasComment(Boolean.TRUE);
            }

            if (obj.getResultComment().getCommentChanged())
            {
                if (obj.getResultComment().getComment() == null)
                {
                    obj.setHasComment(false);
                    String delete = ""
                            + "DELETE FROM " + lab95;
                    String where = ""
                            + " WHERE "
                            + " lab39c1 = ? "
                            + " AND lab22c1 = ? ";

                    getJdbcTemplate().update(delete + where, new Object[]
                    {
                        obj.getTestId(), obj.getOrder()
                    });
                }

                String update = ""
                        + "UPDATE    " + lab57
                        + " SET      lab57c8 = ?," //estado
                        + "         lab57c9 = ?," //patologia
                        + "         lab57c65 = 0," //envio automatico de resultado
                        + "         lab57c32 = ? "; //tiene comentario
                String where = ""
                        + " WHERE "
                        + " lab39c1 = ? "
                        + " AND lab22c1 = ? ";

                getJdbcTemplate().update(update + where, new Object[]
                {
                    ResultTestState.REPORTED.getValue(), obj.getPathology(), obj.getHasComment() ? 1 : 0, obj.getTestId(), obj.getOrder()
                });
                obj.setState(ResultTestState.REPORTED.getValue());
            }

            if (obj.getResultChanged())
            {
                if (obj.getResultRepetition() != null)
                {
                    Date modifyDate = new Date();

                    SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getJdbcTemplate());
                    insertJdbc.withTableName(lab29).usingGeneratedKeyColumns("lab29c1");
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", obj.getOrder());
                    parameters.put("lab39c1", obj.getTestId());
                    parameters.put("lab29c2", obj.getResultRepetition().getResult());
                    parameters.put("lab29c3", obj.getResultDate() == null ? resultDate : obj.getResultDate());
                    parameters.put("lab29c4", obj.getPathology());
                    parameters.put("lab04c1_1", obj.getResultUserId());
                    parameters.put("lab29c5", obj.getResultRepetition().getType() + "");
                    parameters.put("lab30c1", obj.getResultRepetition().getReasonId());
                    parameters.put("lab29c7", obj.getResultRepetition().getReasonComment());
                    parameters.put("lab29c8", modifyDate);
                    parameters.put("lab04c1_2", obj.getUserId());
                    parameters.put("lab29c9", obj.getEntryType());

                    insertJdbc.execute(parameters);
                }

                String print = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT  lab39c25 "
                        + " FROM   lab39"
                        + " WHERE  lab39c1 = ? ";

                int iTestPrint = getJdbcTemplate().queryForObject(print, new Object[]
                {
                    obj.getTestId()
                }, Integer.class);

                obj.setPrint(iTestPrint == 3 || iTestPrint == 1 ? 1 : 2);
                //TODO: Encriptar el resultado
                // Y Cambio de estado del mismo
                String update = ""
                        + "UPDATE    " + lab57
                        + " SET      lab57c1 = ?," //resultado
                        + "         lab57c2 = ?," //fecha
                        + "         lab57c3 = ?," //usuario
                        + "         lab57c8 = ?," //estado
                        + "         lab57c9 = ?," //patologia
                        + "         lab57c32 = ?," //tiene comentario
                        + "         lab57c50 = 0," //Envio de orden a un sistema externo
                        + "         lab57c25 = ?, " //si cambio de estado de impresion
                        + "         lab57c53 = ?, " //Dilución
                        + "         lab57c62 = ?, " //Resultadfo Anterior
                        + "         lab57c65 = 0, " //Resultado Anterior
                        + "         lab57c18 = ?, " //Fecha de validacion
                        + "         lab57c19 = ?, " //Usuario validacion
                        + "         lab57c70 = ?, " //Resultado en ingles
                        + "         lab57c74 = 0 "; //Envio a bd central
                String where = ""
                        + " WHERE "
                        + " lab39c1 = ? "
                        + " AND lab22c1 = ? ";

                getJdbcTemplate().update(update + where, new Object[]
                {
                    Tools.encrypt(obj.getResult()),
                    resultDate,
                    obj.getUserId(),
                    ResultTestState.REPORTED.getValue(),
                    obj.getPathology(),
                    obj.getHasComment() ? 1 : 0,
                    iTestPrint == 3 || iTestPrint == 1 ? 1 : 2,
                    obj.isDilution() ? 1 : 0,
                    obj.getPreviousResult() != null && !obj.getPreviousResult().isEmpty() ? Tools.encrypt(obj.getPreviousResult()) : "",
                    null, null,
                    Tools.encrypt(obj.getResultEnglish()),
                    obj.getTestId(),
                    obj.getOrder()
                });
                obj.setState(ResultTestState.REPORTED.getValue());
            }

            if (obj.getResultChanged() || obj.getResultComment().getCommentChanged())
            {

                if (obj.getPathology() != obj.getOrderPathology())
                {

                    String select = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT  max(lab57c9) "
                            + " FROM   " + lab57 + " as lab57 "
                            + " WHERE  lab22c1 = ? ";

                    int orderPathology = getJdbcTemplate().queryForObject(select, new Object[]
                    {
                        obj.getOrder()
                    }, Integer.class);

                    String update = ""
                            + "UPDATE    " + lab22
                            + " SET      lab57c9 = ?"
                            + " WHERE "
                            + " lab22c1 = ? ";

                    getJdbcTemplate().update(update, new Object[]
                    {
                        orderPathology, obj.getOrder()
                    });

                    obj.setOrderPathology(orderPathology);
                }

                obj.setResultDate(resultDate);
                obj.setState(ResultTestState.REPORTED.getValue());

                //obj.getResultComment().setCommentChanged(false);
                //obj.setResultChanged(false);
            }
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
        return obj;
    }

    /**
     * Elimina las gráficas de un resultado
     *
     * @param order
     * @param idTest
     * @return True si se elimiaron los registros, False si no fueron eliminados
     * @throws Exception
     */
    default boolean updateResultComment(Long order, int idTest, String comment) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String update = ""
                + "UPDATE    " + lab57
                + " SET   lab57c71 = ? "; //tiene comentario
        String where = ""
                + " WHERE "
                + " lab39c1 = ? "
                + " AND lab22c1 = ? ";

        getJdbcTemplate().update(update + where, new Object[]
        {
            comment, idTest, order
        });

        return true;
    }

    /**
     * Elimina las gráficas de un resultado
     *
     * @param order
     * @param idTest
     * @return True si se elimiaron los registros, False si no fueron eliminados
     * @throws Exception
     */
    default boolean deleteResultComment(Long order, int idTest) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String update = ""
                + "UPDATE    " + lab57
                + " SET   lab57c32 = ? "; //tiene comentario
        String where = ""
                + " WHERE "
                + " lab39c1 = ? "
                + " AND lab22c1 = ? ";

        getJdbcTemplate().update(update + where, new Object[]
        {
            0, idTest, order
        });

        String query = " DELETE FROM " + lab95 + " WHERE lab22c1 = ? AND lab39c1 = ? ";
        return getJdbcTemplate().update(query,
                order,
                idTest) > 0;
    }

    /**
     * Se obtienen las gráficas de una orden e
     *
     * @param order Numero de la orden
     * @param testId Id del examen
     * @return Objeto de imagenes de analizador
     * @throws Exception
     */
    default ImageTest getResultGraphics(long order, int testId) throws Exception
    {
        try
        {
            return getDocsJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab39c1, doc03c1, doc03c2, doc03c3, doc03c4, doc03c5, doc03c6, doc03c7, doc03c8, doc03c9, doc03c10 "
                    + "FROM doc03 "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ? ", new Object[]
                    {
                        order, testId
                    }, (ResultSet rs, int i) ->
            {
                ImageTest imageTest = new ImageTest();
                imageTest.setOrder(rs.getInt("lab22c1"));
                imageTest.setTestId(rs.getInt("lab39c1"));
                imageTest.setImageName1(rs.getString("doc03c1"));
                imageTest.setImageName2(rs.getString("doc03c3"));
                imageTest.setImageName3(rs.getString("doc03c5"));
                imageTest.setImageName4(rs.getString("doc03c7"));
                imageTest.setImageName5(rs.getString("doc03c9"));

                /*Imagenes*/
                byte[] imageBytes = rs.getBytes("doc03c2");
                if (imageBytes != null)
                {
                    imageTest.setImage1(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c4");
                if (imageBytes != null)
                {
                    imageTest.setImage2(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c6");
                if (imageBytes != null)
                {
                    imageTest.setImage3(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c8");
                if (imageBytes != null)
                {
                    imageTest.setImage4(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c10");
                if (imageBytes != null)
                {
                    imageTest.setImage5(Base64.getEncoder().encodeToString(imageBytes));
                }

                return imageTest;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Se obtienen las gráficas de una orden
     *
     * @param order Numero de la orden
     * @param testId Id del examen
     * @return Objeto de imagenes de analizador
     * @throws Exception
     */
    default List<ImageTest> getResultGraphics(long order, List<Integer> testsfilter) throws Exception
    {
        try
        {
            return getDocsJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab39c1, doc03c1, doc03c2, doc03c3, doc03c4, doc03c5, doc03c6, doc03c7, doc03c8, doc03c9, doc03c10 "
                    + "FROM doc03 "
                    + "WHERE lab22c1 = ? and lab39c1 IN (" + testsfilter.stream().map((t) -> t.toString()).collect(Collectors.joining(",")) + ")"
                    + " ", new Object[]
                    {
                        order
                    }, (ResultSet rs, int i) ->
            {
                ImageTest imageTest = new ImageTest();
                imageTest.setOrder(rs.getLong("lab22c1"));
                imageTest.setTestId(rs.getInt("lab39c1"));
                imageTest.setImageName1(rs.getString("doc03c1"));
                imageTest.setImageName2(rs.getString("doc03c3"));
                imageTest.setImageName3(rs.getString("doc03c5"));
                imageTest.setImageName4(rs.getString("doc03c7"));
                imageTest.setImageName5(rs.getString("doc03c9"));

                /*Imagenes*/
                byte[] imageBytes = rs.getBytes("doc03c2");
                if (imageBytes != null)
                {
                    imageTest.setImage1(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c4");
                if (imageBytes != null)
                {
                    imageTest.setImage2(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c6");
                if (imageBytes != null)
                {
                    imageTest.setImage3(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c8");
                if (imageBytes != null)
                {
                    imageTest.setImage4(Base64.getEncoder().encodeToString(imageBytes));
                }
                imageBytes = rs.getBytes("doc03c10");
                if (imageBytes != null)
                {
                    imageTest.setImage5(Base64.getEncoder().encodeToString(imageBytes));
                }

                return imageTest;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta las graficas de resultados recibidas del sistema Middleware
     *
     * @param imageTest Objeto de imagenes
     * @return True si se insertaron las graficas, False si no fueron insertadas
     * @throws Exception
     */
    default boolean insertResultGraphics(ImageTest imageTest) throws Exception
    {
        if (imageTest.getImageName1() == null && imageTest.getImageName1().isEmpty() && imageTest.getImage1() == null && imageTest.getImage1().isEmpty())
        {
            return false;
        }
        SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getDocsJdbcTemplate());
        insertJdbc.withTableName("doc03");
        HashMap parameters = new HashMap();
        parameters.put("lab22c1", imageTest.getOrder());
        parameters.put("lab39c1", imageTest.getTestId());

        parameters.put("doc03c1", imageTest.getImageName1());
        byte[] imageByte = DatatypeConverter.parseBase64Binary(imageTest.getImage1());
        parameters.put("doc03c2", imageByte);

        if (imageTest.getImage2() != null)
        {
            parameters.put("doc03c3", imageTest.getImageName2());
            imageByte = DatatypeConverter.parseBase64Binary(imageTest.getImage2());
            parameters.put("doc03c4", imageByte);
        }
        if (imageTest.getImage3() != null)
        {
            parameters.put("doc03c5", imageTest.getImageName3());
            imageByte = DatatypeConverter.parseBase64Binary(imageTest.getImage3());
            parameters.put("doc03c6", imageByte);
        }
        if (imageTest.getImage4() != null)
        {
            parameters.put("doc03c7", imageTest.getImageName4());
            imageByte = DatatypeConverter.parseBase64Binary(imageTest.getImage4());
            parameters.put("doc03c8", imageByte);
        }
        if (imageTest.getImage5() != null)
        {
            parameters.put("doc03c9", imageTest.getImageName5());
            imageByte = DatatypeConverter.parseBase64Binary(imageTest.getImage5());
            parameters.put("doc03c10", imageByte);
        }
        return insertJdbc.execute(parameters) > 1;
    }

    /**
     * Elimina las gráficas de un resultado
     *
     * @param imageTest Objeto de imagenes
     * @return True si se elimiaron los registros, False si no fueron eliminados
     * @throws Exception
     */
    default boolean deleteResultGraphics(ImageTest imageTest) throws Exception
    {
        String query = " DELETE FROM doc03 WHERE lab22c1 = ? AND lab39c1 = ? ";
        return getDocsJdbcTemplate().update(query,
                imageTest.getOrder(),
                imageTest.getTestId()) > 1;
    }

    /**
     * Repetir un resultado.
     *
     * @param obj Objeto de tipo ResultTest
     *
     * @return Objeto de tipo ResultTest modificado.
     * @throws Exception Error en la base de datos.
     */
    default ResultTest rerun(ResultTest obj) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(obj.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab29 = year.equals(currentYear) ? "lab29" : "lab29_" + year;
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            Date rerunDate = new Date();

            SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getJdbcTemplate());
            insertJdbc.withTableName(lab29).usingGeneratedKeyColumns("lab29c1");
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", obj.getOrder());
            parameters.put("lab39c1", obj.getTestId());
            parameters.put("lab29c2", obj.getResult());
            parameters.put("lab29c3", obj.getResultDate() == null ? rerunDate : obj.getResultDate());
            parameters.put("lab29c4", obj.getPathology());
            parameters.put("lab04c1_1", obj.getResultUserId());
            parameters.put("lab29c5", obj.getResultRepetition().getType() + "");
            parameters.put("lab30c1", obj.getResultRepetition().getReasonId());
            parameters.put("lab29c7", obj.getResultRepetition().getReasonComment());
            parameters.put("lab29c8", rerunDate);
            parameters.put("lab04c1_2", obj.getUserId());
            parameters.put("lab29c9", obj.getEntryType());

            insertJdbc.execute(parameters);

            //Actualización
            //TODO: Encriptar el resultado
            String update = ""
                    + "UPDATE    " + lab57
                    + " SET      lab57c1 = ?," //resultado
                    + "         lab57c2 = ?," //fecha
                    + "         lab57c3 = ?," //usuario
                    + "         lab57c18 = ?," //Fecha de validacion
                    + "         lab57c19 = ?," //usuario de validacion
                    + "         lab57c20 = ?," //Fecha de prevalidacion
                    + "         lab57c21 = ?," //usuario de prevalidacion
                    + "         lab57c22 = ?," //Fecha de impresion
                    + "         lab57c23 = ?," //usuario de impresion
                    + "         lab57c8 = ?," //estado
                    + "         lab57c9 = ?," //patologia
                    + "         lab57c33 = ?,"//ha sido repetido
                    + "         lab57c46 = ?,"//Fecha de la repeticion
                    + "         lab57c47 = ?,"//Usuario de repeticion
                    + "         lab57c48 = ?,"//Ultimo resultado repetido
                    + "         lab57c70 = ?";//Resultado en ingles
            String from = "";
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + from + where, new Object[]
            {
                null, null, null, null, null, null, null, null, null, ResultTestState.RERUN.getValue(), ResultTestPathology.NORMAL.getValue(), 1, rerunDate, obj.getUserId(), Tools.encrypt(obj.getRepeatedResultValue()), null, obj.getTestId(), obj.getOrder()
            });

            obj.setResult(null);
            obj.setResultDate(null);
            obj.setValidationDate(null);
            obj.setValidationUserId(null);
            obj.setValidationUserName(null);
            obj.setValidationUserLastName(null);
            obj.setValidationUserIdentification(null);
            obj.setValidationUserSignatureCode(null);
            obj.setValidationUserSignature(null);
            obj.setResultDate(null);
            obj.setPrintDate(null);
            obj.setPrintUserId(null);
            obj.setState(ResultTestState.RERUN.getValue());
            obj.setPathology(ResultTestPathology.NORMAL.getValue());

            if (obj.getPathology() != obj.getOrderPathology())
            {
                String select = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT  max(lab57c9) "
                        + " FROM   " + lab57 + " as lab57 "
                        + " WHERE  lab22c1 = ? ";

                int orderPathology = getJdbcTemplate().queryForObject(select, new Object[]
                {
                    obj.getOrder()
                }, Integer.class);

                update = ""
                        + "UPDATE    " + lab22
                        + " SET      lab57c9 = ?"
                        + " WHERE "
                        + " lab22c1 = ? ";

                getJdbcTemplate().update(update, new Object[]
                {
                    orderPathology, obj.getOrder()
                });

                obj.setOrderPathology(orderPathology);
            }

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
        return obj;
    }

    /**
     * pasar examen a ordenado
     *
     * @param obj Objeto de tipo ResultTest
     *
     * @return Objeto de tipo ResultTest modificado.
     * @throws Exception Error en la base de datos.
     */
    default ResultTest ordered(ResultTest obj) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(obj.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            //Actualización
            //TODO: Encriptar el resultado
            String update = ""
                    + "UPDATE   " + lab57
                    + " SET      lab57c1 = ?," //resultado
                    + "         lab57c2 = ?," //fecha
                    + "         lab57c3 = ?," //usuario
                    + "         lab57c18 = ?," //Fecha de validacion
                    + "         lab57c19 = ?," //usuario de validacion
                    + "         lab57c20 = ?," //Fecha de prevalidacion
                    + "         lab57c21 = ?," //usuario de prevalidacion
                    + "         lab57c22 = ?," //Fecha de impresion
                    + "         lab57c23 = ?," //usuario de impresion
                    + "         lab57c8 = ?," //estado
                    + "         lab57c9 = ?, " //patologia
                    + "         lab57c62 = ?, " //resultado anterior
                    + "         lab57c70 = ? "; //resultado en ingles

            String from = "";
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + from + where, new Object[]
            {
                null, null, null, null, null, null, null, null, null, ResultTestState.ORDERED.getValue(), ResultTestPathology.NORMAL.getValue(), Tools.decrypt(obj.getPreviousResult()), null, obj.getTestId(), obj.getOrder()
            });

            obj.setResult(null);
            obj.setResultDate(null);
            obj.setValidationDate(null);
            obj.setValidationUserId(null);
            obj.setValidationUserName(null);
            obj.setValidationUserLastName(null);
            obj.setValidationUserIdentification(null);
            obj.setValidationUserSignatureCode(null);
            obj.setValidationUserSignature(null);
            obj.setResultDate(null);
            obj.setPrintDate(null);
            obj.setPrintUserId(null);
            obj.setState(ResultTestState.ORDERED.getValue());
            obj.setPathology(ResultTestPathology.NORMAL.getValue());

            if (obj.getPathology() != obj.getOrderPathology())
            {
                String select = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT  max(lab57c9) "
                        + " FROM   " + lab57 + " as lab57 "
                        + " WHERE  lab22c1 = ? ";

                int orderPathology = getJdbcTemplate().queryForObject(select, new Object[]
                {
                    obj.getOrder()
                }, Integer.class);

                update = ""
                        + "UPDATE    " + lab22
                        + " SET      lab57c9 = ?"
                        + " WHERE "
                        + " lab22c1 = ? ";

                getJdbcTemplate().update(update, new Object[]
                {
                    orderPathology, obj.getOrder()
                });

                obj.setOrderPathology(orderPathology);
            }

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
        return obj;
    }

    /**
     * obtener el estado actual del examen de una orden.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    default int getTestState(long orderId, int testId) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  lab57c8 "
                    + " FROM   lab57"
                    + " WHERE  lab22c1 = ? and lab39c1 = ? ";

            int orderPathology = getJdbcTemplate().queryForObject(select, new Object[]
            {
                orderId, testId
            }, Integer.class);

            return orderPathology;
            //TODO: Actualizar el historico para el paciente: Lab17
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return -1;
        }
    }

    /**
     * obtener el resultado actual de un examen.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     *
     * @throws Exception Error en la base de datos.
     */
    default String getResultTest(long orderId, int testId) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  lab57c1 "
                    + " FROM   lab57"
                    + " WHERE  lab22c1 = ? and lab39c1 = ? ";

            String resultTest = getJdbcTemplate().queryForObject(select, new Object[]
            {
                orderId, testId
            }, String.class);

            return Tools.decrypt(resultTest);
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return "";
        }
    }

    /**
     * Validación final de un resultado.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @param userId Identificador del usuario que valida el resultado
     * @param validateDate Fecha de validación de la prueba
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    default int validated(long orderId, int testId, int userId, Date validateDate) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String update = ""
                    + "UPDATE    " + lab57
                    + " SET      lab57c18 = ?," //fecha
                    + "         lab57c19 = ?," //usuario
                    + "         lab57c8 = ?" //estado
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            return getJdbcTemplate().update(update, new Object[]
            {
                validateDate, userId, ResultTestState.VALIDATED.getValue(), testId, orderId
            });

            //TODO: Actualizar el historico para el paciente: Lab17
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return 0;
        }
    }

    /**
     * Prevalidacion de un resultado.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @param userId Identificador del usuario que valida el resultado
     * @param validateDate Fecha de validación de la prueba
     *
     * @throws Exception Error en la base de datos.
     */
    default void prevalidated(long orderId, int testId, int userId, Date validateDate) throws Exception
    {
        try
        {
            String update = ""
                    + "UPDATE   lab57 "
                    + "SET      lab57c20 = ?," //fecha
                    + "         lab57c21 = ?," //usuario
                    + "         lab57c8 = ?";  //estado

            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + where, new Object[]
            {
                validateDate, userId, ResultTestState.PREVIEW.getValue(), testId, orderId
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
    }

    /**
     * Imprimir un resultado.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @param userId Identificador del usuario que imprime el resultado
     *
     * @throws Exception Error en la base de datos.
     */
    default void printed(long orderId, int testId, int userId) throws Exception
    {
        try
        {
            String update = ""
                    + "UPDATE   lab57 "
                    + "SET      lab57c22= ?," //fecha
                    + "         lab57c23 = ?," //usuario
                    + "         lab57c8 = ?";  //estado
            String from = "";
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + from + where, new Object[]
            {
                new java.util.Date(), userId, ResultTestState.DELIVERED.getValue(), testId, orderId
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param orders lista de ordenes a consultar searchType == 2
     * @param hasTraceability Indica si se tiene en cuenta configuracion de
     * trazabilidad de la muestra
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> orderResultList(Long vInitial, Long vFinal, int searchType, List<Long> orders, boolean hasTraceability, int yearsQuery) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1 AS lab22c1, "
                    + "lab22.lab22c2 AS lab22c2, "
                    + "lab22.lab22c3 AS lab22c3, "
                    + "lab22.lab22c4 AS lab22c4, "
                    + "lab22.lab22c5 AS lab22c5, "
                    + "lab22.lab07c1 AS lab07c1, "
                    + "lab22.lab22c7 AS lab22c7, "
                    + "lab21.lab21c1 AS lab21c1, "
                    + "lab21.lab21c2 AS lab21c2, "
                    + "lab21.lab21c3 AS lab21c3, "
                    + "lab21.lab21c4 AS lab21c4, "
                    + "lab21.lab21c5 AS lab21c5, "
                    + "lab21.lab21c6 AS lab21c6, "
                    + "lab21.lab21c7 AS lab21c7, "
                    + "lab21.lab21c8 AS lab21c8, "
                    + "lab21.lab21c9 AS lab21c9, "
                    + "lab21.lab21c10 AS lab21c10, "
                    + "lab21.lab21c11 AS lab21c11, "
                    + "lab21.lab21c16 AS lab21c16, "
                    + "lab21.lab21c17 AS lab21c17, "
                    + "lab80.lab80c1 AS lab80c1, "
                    + "lab80.lab80c2 AS lab80c2, "
                    + "lab80.lab80c3 AS lab80c3, "
                    + "lab80.lab80c4 AS lab80c4, "
                    + "lab80.lab80c5 AS lab80c5, "
                    + "lab54.lab54c1 AS lab54c1, "
                    + "lab54.lab54c2 AS lab54c2, "
                    + "lab54.lab54c3 AS lab54c3, "
                    + "lab05.lab05c1 AS lab05c1, "
                    + "lab05.lab05c10 AS lab05c10, "
                    + "lab05.lab05c4 AS lab05c4 ";
            String from = "FROM lab22 "
                    + "INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "LEFT JOIN lab80  ON lab80.lab80c1 = lab21.lab80c1  "
                    + "LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1  "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                    + (hasTraceability ? "INNER JOIN lab144 ON lab144.lab22c1 = lab22.lab22c1  " : "");

            Object[] params = null;
            switch (searchType)
            {
                case 1:
                    from += " WHERE lab22.lab22c1 BETWEEN ? AND ?";
                    params = new Object[]
                    {
                        vInitial, vFinal
                    };
                    break;
                case 0:
                    from += " WHERE lab22c2 BETWEEN ? AND ?";
                    params = new Object[]
                    {
                        vInitial, vFinal
                    };
                    break;
                default:
                    from += " WHERE lab22.lab22c1 IN(" + orders.stream().map(order -> order.toString()).collect(Collectors.joining(",")) + ")";
                    break;
            }
            // Se tiene en cuenta el estado de verificacion de la muestra si esta activa en la configuracion
            from += (hasTraceability ? " AND lab144c3 = 4" : "");
            from += " AND (lab22c19 = 0 or lab22c19 is null) ";

            params = new Object[]
            {
                vInitial, vFinal
            };

            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i) ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));
                order.setCreatedDate(rs.getTimestamp("lab22c3"));
                order.setState(rs.getInt("lab07c1"));
                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                order.getPatient().setPhone(rs.getString("lab21c16"));
                order.getPatient().setAddress(rs.getString("lab21c17"));
                //SEDE
                order.getBranch().setId(rs.getInt("lab05c1"));
                order.getBranch().setCode(rs.getString("lab05c10"));
                order.getBranch().setName(rs.getString("lab05c4"));

                try
                {
                    List<ResultTest> tests = list(null, Arrays.asList(order.getOrderNumber()), null, yearsQuery);
                    order.setResultTest(tests);
                } catch (Exception ex)
                {
                    order.setResultTest(new ArrayList<>());
                    Log.error(ResultTestDao.class, ex);
                }

                return order;
            };
            if (searchType == 2)
            {
                return getJdbcTemplate().query(query + from, mapper);
            } else
            {
                return getJdbcTemplate().query(query + from, mapper, params);
            }
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las repeticiones de un examen
     *
     * @param orderNumber numero de orden
     * @param testId id del exámen
     * @return Lista de repeticiones
     * @throws Exception Error en la base de datos
     */
    default List<ResultTestRepetition> listTestReruns(Long orderNumber, int testId) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab39c1, lab29c2, lab29c3, lab29c4 "
                    + ", lab29.lab04c1_1  , a.lab04c2 resultName, a.lab04c3 resultLastName, a.lab04c4 resultUserName  "
                    + ", lab04c1_2  , b.lab04c2 repeatName, b.lab04c3 repeatLastName, b.lab04c4 repeatUserName "
                    + ", lab30.lab30c1, lab30c2,  lab29c7 "
                    + ", lab29c9, lab29c7 ";
            String from = " "
                    + "FROM lab29 "
                    + "LEFT JOIN lab04 a ON a.lab04c1  = lab29.lab04c1_1  "
                    + "LEFT JOIN lab04 b ON b.lab04c1  = lab29.lab04c1_2  "
                    + "LEFT JOIN lab30 ON lab30.lab30c1 = lab29.lab30c1  "
                    + "WHERE lab22c1 = ? AND lab39c1 = ? AND lab29c5 = 'R' ";

            Object[] params = new Object[]
            {
                orderNumber, testId
            };
            RowMapper mapper = (RowMapper<ResultTestRepetition>) (ResultSet rs, int i) ->
            {
                ResultTestRepetition repeat = new ResultTestRepetition();
                repeat.setOrder(rs.getLong("lab22c1"));
                repeat.setTestId(rs.getInt("lab39c1"));

                repeat.setResult(rs.getString("lab29c2"));
                repeat.setResultDate(rs.getTimestamp("lab29c3"));
                repeat.setResultPathology(rs.getInt("lab29c4"));

                repeat.setReasonId(rs.getInt("lab30c1"));
                repeat.setReason(rs.getString("lab30c2"));
                repeat.setReasonComment(rs.getString("lab29c7"));
                repeat.setResultUser(new AuthorizedUser(rs.getInt("lab04c1_1")));
                repeat.getResultUser().setName(rs.getString("resultName"));
                repeat.getResultUser().setLastName(rs.getString("resultLastName"));
                repeat.getResultUser().setUserName(rs.getString("resultUserName"));
                //
                repeat.setRepeatUser(new AuthorizedUser(rs.getInt("lab04c1_2")));
                repeat.getRepeatUser().setName(rs.getString("repeatName"));
                repeat.getRepeatUser().setLastName(rs.getString("repeatLastName"));
                repeat.getRepeatUser().setUserName(rs.getString("repeatUserName"));

                return repeat;
            };
            return getJdbcTemplate().query(query + from, mapper, params);

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Indica si la orden y el examen tienen tareas asociadas.
     *
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @return true si la orden y el examen tienen tareas asociadas.
     */
    default boolean isReportedTasks(Long idOrder, Integer idTest)
    {
        try
        {
            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT 1 "
                    + "FROM lab208 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ?";

            Integer result = getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) -> rs.getInt(1));

            return result == 1;

        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Lista del histórico de resultados de un examen
     *
     * @param id identificador del pacientes
     * @param testId id del exámen
     * @param yearsQuery Años de consulta (historicos)
     * @return Lista de repeticiones
     * @throws Exception Error en la base de datos
     */
    default List<ResultTestHistory> listTestHistory(int id, int testId, int yearsQuery) throws Exception
    {
        try
        {
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);

            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<ResultTestHistory> listOrders = new LinkedList<>();
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                String query = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab22.lab22c1, "
                        + "lab57.lab39c1, "
                        + "lab39c2, "
                        + "lab39c4, "
                        + "lab39c3, "
                        + "lab57c1, "
                        + "lab57.lab57c9, "
                        + "lab57c18, "
                        + "lab57c4, "
                        + "lab48c12, "
                        + "lab48c13, "
                        + "lab50c2, "
                        + "lab39c11 ";
                String from = " "
                        + "FROM " + lab57 + " as lab57 "
                        + "INNER JOIN   " + lab22 + " as lab22 ON lab22.lab22c1  = lab57.lab22c1 "
                        + "INNER JOIN lab39 ON lab39.lab39c1  = lab57.lab39c1 "
                        + "LEFT JOIN lab50  ON lab50.lab50c1  = lab57.lab50c1_3 "
                        + "WHERE lab22.lab07c1 = 1 and lab22.lab21c1 = ? AND lab57.lab39c1 = ? AND lab57.lab57c8 >= ?  AND (lab22c19 = 0 or lab22c19 is null) "
                        + "ORDER BY lab57c20 DESC";

                Object[] params = new Object[]
                {
                    id, testId, LISEnum.ResultTestState.VALIDATED.getValue()
                };
                RowMapper mapper = (RowMapper<ResultTestHistory>) (ResultSet rs, int i) ->
                {
                    ResultTestHistory bean = new ResultTestHistory();
                    bean.setOrder(rs.getLong("lab22c1"));
                    bean.setTestId(rs.getInt("lab39c1"));
                    bean.setTestCode(rs.getString("lab39c2"));
                    bean.setAbbr(rs.getString("lab39c3"));
                    bean.setTestName(rs.getString("lab39c4"));
                    bean.setPathology(rs.getInt("lab57c9"));

                    //TODO: Desencriptar el resultado
                    bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                    bean.setValidateDate(rs.getTimestamp("lab57c18"));
                    bean.setEntryDate(rs.getTimestamp("lab57c4"));

                    BigDecimal refMin = rs.getBigDecimal("lab48c12");
                    BigDecimal refMax = rs.getBigDecimal("lab48c13");

                    if (!rs.wasNull())
                    {
                        bean.setRefMin(refMin);
                        bean.setRefMax(refMax);
                    } else
                    {
                        //bean.setRefMin(0F);
                        //bean.setRefMax(0F);
                    }

                    bean.setRefLiteral(rs.getString("lab50c2"));
                    bean.setResultType(rs.getShort("lab39c11"));
                    listOrders.add(bean);
                    return bean;
                };

                getJdbcTemplate().query(query + from, mapper, params);

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    
    default List<ResultTestHistory> listTestHistoryFilterUser(int id, int testId, int idUser, int typeUser, int yearsQuery, int demographicquery) throws Exception
    {
        try
        {
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);

            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
            String lab22;
            String lab57;
           
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<ResultTestHistory> listOrders = new LinkedList<>();
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                
              
                String query = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab22.lab22c1, "
                        + "lab57.lab39c1, "
                        + "lab39c2, "
                        + "lab39c4, "
                        + "lab39c3, "
                        + "lab57c1, "
                        + "lab57.lab57c9, "
                        + "lab57c18, "
                        + "lab57c4, "
                        + "lab48c12, "
                        + "lab48c13, "
                        + "lab50c2, "
                        + "lab39c11 ";
                String from = " "
                        + "FROM " + lab57 + " as lab57 "
                        + "INNER JOIN   " + lab22 + " as lab22 ON lab22.lab22c1  = lab57.lab22c1 "
                        + "INNER JOIN lab39 ON lab39.lab39c1  = lab57.lab39c1 "
                        + "LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 "
                        + "LEFT JOIN lab19 ON lab22.lab19c1 = lab19.lab19c1 "
                        + "LEFT JOIN lab50  ON lab50.lab50c1  = lab57.lab50c1_3 "
                        + "WHERE lab22.lab07c1 = 1 and lab22.lab21c1 = ? AND lab57.lab39c1 = ? AND lab57.lab57c8 >= ?  AND (lab22c19 = 0 or lab22c19 is null) ";
                
                 switch (typeUser)
                {
                    case Constants.AUTHENTICATION_PHYSICIAN:
                        from = from + "AND lab22.lab19c1 = " + idUser;
                        break;
                    case Constants.AUTHENTICATION_PATIENT:
                        from = from + "AND lab14c23 = 1";
                        break;
                    case Constants.AUTHENTICATION_ACCOUNT:
                        from = from + "AND lab22.lab14c1 = " + idUser;
                        break;
                    case Constants.AUTHENTICATION_DEMOGRAPHIC:
                        from = from + "AND lab_demo_" + demographicquery +  " = (select lab181c8 from lab181 where lab181c1 = " + idUser  + ")";
                         break;
                }
                
                from = from 
                        + "ORDER BY lab57c20 DESC";

                Object[] params = new Object[]
                {
                    id, testId, LISEnum.ResultTestState.VALIDATED.getValue()
                };
                RowMapper mapper = (RowMapper<ResultTestHistory>) (ResultSet rs, int i) ->
                {
                    ResultTestHistory bean = new ResultTestHistory();
                    bean.setOrder(rs.getLong("lab22c1"));
                    bean.setTestId(rs.getInt("lab39c1"));
                    bean.setTestCode(rs.getString("lab39c2"));
                    bean.setAbbr(rs.getString("lab39c3"));
                    bean.setTestName(rs.getString("lab39c4"));
                    bean.setPathology(rs.getInt("lab57c9"));

                    //TODO: Desencriptar el resultado
                    bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                    bean.setValidateDate(rs.getTimestamp("lab57c18"));
                    bean.setEntryDate(rs.getTimestamp("lab57c4"));

                    BigDecimal refMin = rs.getBigDecimal("lab48c12");
                    BigDecimal refMax = rs.getBigDecimal("lab48c13");

                    if (!rs.wasNull())
                    {
                        bean.setRefMin(refMin);
                        bean.setRefMax(refMax);
                    } else
                    {
                        //bean.setRefMin(0F);
                        //bean.setRefMax(0F);
                    }

                    bean.setRefLiteral(rs.getString("lab50c2"));
                    bean.setResultType(rs.getShort("lab39c11"));
                    listOrders.add(bean);
                    return bean;
                };

                getJdbcTemplate().query(query + from , mapper, params);

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza la cantidad de ajuntos de la orden o del examen
     *
     * @param order número de orden
     * @param testId id del exámen
     * @param add indica si se adiciona (true) o se rest (false) un adjunto \
     *
     * @throws Exception Error en la base de datos
     */
    default void updateAttachmentCount(Long order, int testId, boolean add) throws Exception
    {
        try
        {
            if (testId > 0)
            {
                String update = ""
                        + "UPDATE lab57 "
                        + "SET    lab57c41 = lab57c41 " + (add ? "+" : "-") + " 1";
                String where = ""
                        + " WHERE "
                        + " lab39c1 = " + testId
                        + " AND lab22c1 = " + order;
                getJdbcTemplate().update(update + where);
            } else
            {
                String update = ""
                        + "UPDATE lab22 "
                        + "SET    lab22c12 = lab22c12 " + (add ? "+" : "-") + " 1";
                String where = ""
                        + " WHERE "
                        + " lab22c1 = " + order;
                getJdbcTemplate().update(update + where);
            }
        } catch (EmptyResultDataAccessException ex)
        {
        }
    }

    /**
     * Cambia estado del examen a impreso(reportado)
     *
     * @param order Objeto de la orden
     * @param target
     * @param user
     * @param sendAutomaticResult
     * @return
     */
    default int printTest(Order order, int target, int user, Boolean sendAutomaticResult)
    {
        List<Object[]> parameters = new ArrayList<>();

        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String query = "";

        if (!sendAutomaticResult)
        {
            query = "UPDATE " + lab57 + " set lab57c8 = ?, lab57c22 = ?, lab57c23 = ? WHERE lab57c8 < 5 AND lab22c1 = ? AND lab39c1 = ?";
        } else
        {
            query = "UPDATE " + lab57 + " set lab57c8 = ?, lab57c22 = ?, lab57c23 = ?, lab57c65 = 1 WHERE lab57c8 < 5 AND lab22c1 = ? AND lab39c1 = ?";
        }

        Date currentDate = new Date();

        for (ResultTest result : order.getResultTest())
        {
            parameters.add(new Object[]
            {
                LISEnum.ResultTestState.DELIVERED.getValue(),
                currentDate,
                user,
                order.getOrderNumber(),
                result.getTestId()
            });
        }
        return getJdbcTemplate().batchUpdate(query, parameters).length;

    }

    /**
     * Bloqueo o desbloqueo de una prueba.
     *
     * @param block Estado de bloqueo de la prueba (1-Bloquea, 0-Desbloquea)
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    default ResultTestBlock blocked(ResultTestBlock block) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(block.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String update = ""
                    + "UPDATE " + lab57 + " "
                    + "SET      lab57c11 = ?," //fecha
                    + "         lab57c12 = ?," //usuario
                    + "         lab57c10 = ?";  //bloqueo

            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + where, new Object[]
            {
                block.getDate(), block.getUser().getId(), block.isBlocked() ? 1 : 0, block.getTestId(), block.getOrder()
            });

            return block;

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return null;
        }
    }

    /**
     * impresion de una prueba.
     *
     * @param print
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    default ResultTestPrint print(ResultTestPrint print) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(print.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String update = ""
                    + "UPDATE  " + lab57 + " "
                    + "SET      lab57c25 = ?"; //fecha

            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + where, new Object[]
            {
                print.isPrint() ? 1 : 2, print.getTestId(), print.getOrder()
            });

            return print;

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return null;
        }
    }

    /**
     * Lista relaciones de examenes por identificador de la prueba/entrevista.
     *
     * @param type tipo de alarma (1-prueba / 2-pregunta)
     * @param id identificador de la prueba o la pregunta
     * @param orderId identificador de la orden
     *
     * @return Lista de relacion de examenes.
     * @throws Exception Error en la base de datos.
     */
    default List<ValidationRelationship> listAlarms(int type, int id, Long orderId) throws Exception
    {
        try
        {
            if (type == 2)
            {
                return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab74c1, lab74c2, lab74c3, lab74c4, lab73c2, lab70.lab70c1,lab70.lab70c2,lab70.lab70c3, lab70.lab70c4, lab23.lab90c1, lab23c1 "
                        + "FROM lab74 "
                        + "INNER JOIN lab73 ON lab73.lab73c1 = lab74.lab73c1 "
                        + "INNER JOIN lab23 ON lab23.lab70c1 = lab74.lab74c6 "
                        + "INNER JOIN lab70 ON lab70.lab70c1 = lab23.lab70c1 "
                        + "WHERE lab74.lab74c1 = ? AND lab23.lab22c1 = ? ",
                        new Object[]
                        {
                            type, orderId
                        }, (ResultSet rs, int i) ->
                {
                    ValidationRelationship relation = new ValidationRelationship();
                    relation.setType(rs.getInt("lab74c1"));
                    relation.setOperator(rs.getString("lab74c2"));
                    relation.setResult(rs.getString("lab74c3"));
                    relation.setResult2(rs.getString("lab74c4"));
                    relation.setAlarm(rs.getString("lab73c2"));
                    relation.setSurveyText(rs.getString("lab23c1"));
                    relation.setSurveyCode(rs.getInt("lab90c1"));
                    return relation;
                });
            } else
            {
                return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab74c1, lab74c2, lab74c3, lab74c4"
                        + ",lab73c2 "
                        + "FROM lab74 "
                        + "INNER JOIN lab73 ON lab73.lab73c1 = lab74.lab73c1 "
                        + "WHERE lab74.lab74c1 = ? AND lab74.lab74c6 = ? ",
                        new Object[]
                        {
                            type, id
                        }, (ResultSet rs, int i) ->
                {
                    ValidationRelationship relation = new ValidationRelationship();
                    relation.setType(rs.getInt("lab74c1"));
                    relation.setOperator(rs.getString("lab74c2"));
                    relation.setResult(rs.getString("lab74c3"));
                    relation.setResult2(rs.getString("lab74c4"));
                    relation.setAlarm(rs.getString("lab73c2"));
                    return relation;
                });
            }
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener información de un resultado o examen de una orden.
     *
     * @param idOrder Id de la orden.
     * @param idTest Id del examen.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    default Result getInformation(Long idOrder, Integer idTest) throws Exception
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57c1, lab57c2, lab57c4, lab57c8, lab57c9, lab57c18, lab57c22, lab57c37, lab57c39, lab57c44, lab57c46, lab200.lab200c2, lab200.lab200c3, "
                    + "userTake.lab04c1 AS userTakec1, userTake.lab04c2 AS userTakec2, userTake.lab04c3 AS userTakec3, userTake.lab04c16 AS userTakec16, "
                    + "userSample.lab04c1 AS userSamplec1, userSample.lab04c2 AS userSamplec2, userSample.lab04c3 AS userSamplec3, userSample.lab04c16 AS userSamplec16, "
                    + "userResult.lab04c1 AS userResultc1, userResult.lab04c2 AS userResultc2, userResult.lab04c3 AS userResultc3, userResult.lab04c16 AS userResultc16, "
                    + "userOrdered.lab04c1 AS userOrderedc1, userOrdered.lab04c2 AS userOrderedc2, userOrdered.lab04c3 AS userOrderedc3, userOrdered.lab04c16 AS userOrderedc16, "
                    + "userRetakes.lab04c1 AS userRetakesc1, userRetakes.lab04c2 AS userRetakesc2, userRetakes.lab04c3 AS userRetakesc3, userRetakes.lab04c16 AS userRetakesc16, "
                    + "userRepetition.lab04c1 AS userRepetitionc1, userRepetition.lab04c2 AS userRepetitionc2, userRepetition.lab04c3 AS userRepetitionc3, userRepetition.lab04c16 AS userRepetitionc16, "
                    + "userValidation.lab04c1 AS userValidationc1, userValidation.lab04c2 AS userValidationc2, userValidation.lab04c3 AS userValidationc3, userValidation.lab04c16 AS userValidationc16, "
                    + "userPrinted.lab04c1 AS userPrintedc1, userPrinted.lab04c2 AS userPrintedc2, userPrinted.lab04c3 AS userPrintedc3, userPrinted.lab04c16 AS userPrintedc16, "
                    + "userMicroSample.lab04c1 AS userMicroSamplec1, userMicroSample.lab04c2 AS userMicroSamplec2, userMicroSample.lab04c3 AS userMicroSamplec3, userMicroSample.lab04c16 AS userMicroSamplec16, "
                    + "userMicroGrowth.lab04c1 AS userMicroGrowthc1, userMicroGrowth.lab04c2 AS userMicroGrowthc2, userMicroGrowth.lab04c3 AS userMicroGrowthc3, userMicroGrowth.lab04c16 AS userMicroGrowthc16 "
                    + "FROM  " + lab57 + " as lab57 "
                    + "LEFT JOIN lab04 AS userTake ON userTake.lab04c1 = lab57.lab57c40 "
                    + "LEFT JOIN lab04 AS userSample ON userSample.lab04c1 = lab57.lab57c38 "
                    + "LEFT JOIN lab04 AS userResult ON userResult.lab04c1 = lab57.lab57c3 "
                    + "LEFT JOIN lab04 AS userOrdered ON userOrdered.lab04c1 = lab57.lab57c5 "
                    + "LEFT JOIN lab04 AS userRetakes ON userRetakes.lab04c1 = lab57.lab57c45 "
                    + "LEFT JOIN lab04 AS userRepetition ON userRepetition.lab04c1 = lab57.lab57c47 "
                    + "LEFT JOIN lab04 AS userValidation ON userValidation.lab04c1 = lab57.lab57c19 "
                    + "LEFT JOIN lab04 AS userPrinted ON userPrinted.lab04c1 = lab57.lab57c23 "
                    + "LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab04 AS userMicroSample ON userMicroSample.lab04c1 = lab200.lab04c1 "
                    + "LEFT JOIN lab04 AS userMicroGrowth ON userMicroGrowth.lab04c1 = lab200.lab04c1_1 "
                    + "WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?",
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                Result result = new Result();
                result.setResult(Tools.decrypt(rs.getString("lab57c1")));
                result.setState(rs.getInt("lab57c8"));
                result.setPathology(rs.getInt("lab57c9"));
                result.setDateTake(rs.getTimestamp("lab57c39"));
                if (rs.getString("userTakec1") != null)
                {
                    result.getUserTake().setId(rs.getInt("userTakec1"));
                    result.getUserTake().setName(rs.getString("userTakec2"));
                    result.getUserTake().setLastName(rs.getString("userTakec3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userTakec16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserTake().setPhoto(photo64);
                } else
                {
                    result.setUserTake(null);
                }
                result.setDateSample(rs.getTimestamp("lab57c37"));
                if (rs.getString("userSamplec1") != null)
                {
                    result.getUserSample().setId(rs.getInt("userSamplec1"));
                    result.getUserSample().setName(rs.getString("userSamplec2"));
                    result.getUserSample().setLastName(rs.getString("userSamplec3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userSamplec16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserSample().setPhoto(photo64);
                } else
                {
                    result.setUserSample(null);
                }
                result.setDateResult(rs.getTimestamp("lab57c2"));
                if (rs.getString("userResultc1") != null)
                {
                    result.getUserResult().setId(rs.getInt("userResultc1"));
                    result.getUserResult().setName(rs.getString("userResultc2"));
                    result.getUserResult().setLastName(rs.getString("userResultc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userResultc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserResult().setPhoto(photo64);
                } else
                {
                    result.setUserResult(null);
                }
                result.setDateOrdered(rs.getTimestamp("lab57c4"));
                if (rs.getString("userOrderedc1") != null)
                {
                    result.getUserOrdered().setId(rs.getInt("userOrderedc1"));
                    result.getUserOrdered().setName(rs.getString("userOrderedc2"));
                    result.getUserOrdered().setLastName(rs.getString("userOrderedc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userOrderedc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserOrdered().setPhoto(photo64);
                } else
                {
                    result.setUserOrdered(null);
                }
                result.setDateValidation(rs.getTimestamp("lab57c18"));
                if (rs.getString("userValidationc1") != null)
                {
                    result.getUserValidation().setId(rs.getInt("userValidationc1"));
                    result.getUserValidation().setName(rs.getString("userValidationc2"));
                    result.getUserValidation().setLastName(rs.getString("userValidationc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userValidationc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserValidation().setPhoto(photo64);
                } else
                {
                    result.setUserValidation(null);
                }
                result.setDatePrinted(rs.getTimestamp("lab57c22"));
                if (rs.getString("userPrintedc1") != null)
                {
                    result.getUserPrinted().setId(rs.getInt("userPrintedc1"));
                    result.getUserPrinted().setName(rs.getString("userPrintedc2"));
                    result.getUserPrinted().setLastName(rs.getString("userPrintedc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userPrintedc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserPrinted().setPhoto(photo64);
                } else
                {
                    result.setUserPrinted(null);
                }
                result.setDateMicrobiologySample(rs.getTimestamp("lab200c2"));
                if (rs.getString("userMicroSamplec1") != null)
                {
                    result.getUserMicrobiologySample().setId(rs.getInt("userMicroSamplec1"));
                    result.getUserMicrobiologySample().setName(rs.getString("userMicroSamplec2"));
                    result.getUserMicrobiologySample().setLastName(rs.getString("userMicroSamplec3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userMicroSamplec16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserMicrobiologySample().setPhoto(photo64);
                } else
                {
                    result.setUserMicrobiologySample(null);
                }
                result.setDateMicrobiologyGrowth(rs.getTimestamp("lab200c3"));
                if (rs.getString("userMicroGrowthc1") != null)
                {
                    result.getUserMicrobiologyGrowth().setId(rs.getInt("userMicroGrowthc1"));
                    result.getUserMicrobiologyGrowth().setName(rs.getString("userMicroGrowthc2"));
                    result.getUserMicrobiologyGrowth().setLastName(rs.getString("userMicroGrowthc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userMicroGrowthc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserMicrobiologyGrowth().setPhoto(photo64);
                } else
                {
                    result.setUserMicrobiologyGrowth(null);
                }
                //Rellamado y retoma
                result.setDateRetakes(rs.getTimestamp("lab57c44"));
                if (rs.getString("userRetakesc1") != null)
                {
                    result.getUserRetakes().setId(rs.getInt("userRetakesc1"));
                    result.getUserRetakes().setName(rs.getString("userRetakesc2"));
                    result.getUserRetakes().setLastName(rs.getString("userRetakesc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userRetakesc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserRetakes().setPhoto(photo64);
                } else
                {
                    result.setUserRetakes(null);
                }
                result.setDateRepetition(rs.getTimestamp("lab57c46"));
                if (rs.getString("userRepetitionc1") != null)
                {
                    result.getUserRepetition().setId(rs.getInt("userRepetitionc1"));
                    result.getUserRepetition().setName(rs.getString("userRepetitionc2"));
                    result.getUserRepetition().setLastName(rs.getString("userRepetitionc3"));

                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userRepetitionc16");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    result.getUserRepetition().setPhoto(photo64);
                } else
                {
                    result.setUserRepetition(null);
                }
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información del estado y resultado de una orden
     *
     * @param idOrder Id de la orden.
     * @param idTest Id del examen.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    default ResultTestStateOrder getResultState(Long idOrder, Integer idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String scriptSQL = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57c1, lab57c8, lab21c1, lab57c2, lab57c3, lab57c62 "
                    + "FROM  " + lab57 + " as lab57 "
                    + "JOIN " + lab22 + " as lab22 on LAB22.LAB22C1 = LAB57.LAB22C1 "
                    + "WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?";

            return getJdbcTemplate().queryForObject(scriptSQL,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                ResultTestStateOrder result = new ResultTestStateOrder();
                result.setResult(Tools.decrypt(rs.getString("lab57c1")));
                result.setState(rs.getInt("lab57c8"));
                result.setPatientId(rs.getInt("lab21c1"));
                result.setDateResult(rs.getTimestamp("lab57c2"));
                result.setUserResult(rs.getInt("lab57c3"));
                result.setPreviousResult(Tools.decrypt(rs.getString("lab57c62")));
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex.getMessage());
            return null;
        }
    }

    /**
     * Obtener información del estado y resultado de una orden
     *
     * @param idOrder Id de la orden.
     * @param idTest Id del examen.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    default List<ResultTestStateOrder> getResultState2(Long idOrder, Integer idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String scriptSQL = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57c1, lab57c8, lab21c1, lab57c2, lab57c3 "
                    + "FROM  " + lab57 + " as lab57 "
                    + "JOIN " + lab22 + " as lab22 on LAB22.LAB22C1 = LAB57.LAB22C1 "
                    + "WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?";

            return getJdbcTemplate().query(scriptSQL,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                ResultTestStateOrder result = new ResultTestStateOrder();
                result.setResult(Tools.decrypt(rs.getString("lab57c1")));
                result.setState(rs.getInt("lab57c8"));
                result.setPatientId(rs.getInt("lab21c1"));
                result.setDateResult(rs.getTimestamp("lab57c2"));
                result.setUserResult(rs.getInt("lab57c3"));
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex.getMessage());
            return null;
        }
    }

    /**
     * Inserta los resultados de la entrevista de pánico.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a la que se le realizo la entrevista.
     * @param tests Listado de las pruebas a las que aplica la entrevista.
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int insertPanicSurvey(List<Question> questions, List<ResultTest> tests, long idOrder) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab179 = year.equals(currentYear) ? "lab179" : "lab179_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName(lab179);

        for (Question question : questions)
        {
            for (ResultTest test : tests)
            {

                if (!question.isOpen())
                {
                    if (question.getAnswers().isEmpty())
                    {
                        HashMap parameters = new HashMap();
                        parameters.put("lab22c1", idOrder);
                        parameters.put("lab39c1", test.getTestId());
                        parameters.put("lab70c1", question.getId());
                        parameters.put("lab179c2", timestamp);
                        parameters.put("lab04c1", getUser().getId());
                        parameters.put("lab179c3", (test.getPathology() >= ResultTestPathology.PANIC.getValue() ? 1 : 0));
                        parameters.put("lab179c4", (test.getDelta() ? 1 : 0));
                        parameters.put("lab179c5", (test.getPathology() >= ResultTestPathology.CRITICAL.getValue() ? 1 : 0));
                        batchArray.add(parameters);
                    } else
                    {
                        for (Answer answer : question.getAnswers())
                        {
                            if (answer.isSelected())
                            {
                                HashMap parameters = new HashMap();
                                parameters.put("lab22c1", idOrder);
                                parameters.put("lab39c1", test.getTestId());
                                parameters.put("lab70c1", question.getId());
                                parameters.put("lab90c1", answer.getId());
                                parameters.put("lab179c2", timestamp);
                                parameters.put("lab04c1", getUser().getId());
                                parameters.put("lab179c3", (test.getPathology() >= ResultTestPathology.PANIC.getValue() ? 1 : 0));
                                parameters.put("lab179c4", (test.getDelta() ? 1 : 0));
                                parameters.put("lab179c5", (test.getPathology() >= ResultTestPathology.CRITICAL.getValue() ? 1 : 0));

                                batchArray.add(parameters);
                            }
                        }
                    }
                } else
                {
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", idOrder);
                    parameters.put("lab39c1", test.getTestId());
                    parameters.put("lab70c1", question.getId());
                    parameters.put("lab179c1", question.getInterviewAnswer());
                    parameters.put("lab179c2", timestamp);
                    parameters.put("lab04c1", getUser().getId());
                    parameters.put("lab179c3", (test.getPathology() >= ResultTestPathology.PANIC.getValue() ? 1 : 0));
                    parameters.put("lab179c4", (test.getDelta() ? 1 : 0));
                    parameters.put("lab179c5", (test.getPathology() >= ResultTestPathology.CRITICAL.getValue() ? 1 : 0));

                    batchArray.add(parameters);
                }
            }
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()])).length;
    }

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados, por
     * filtro de examenes y muestras. Y que son ingresados.
     *
     * @param orderId Id de ordenes.
     * @param samples Muestras
     * @param tests Examenes
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     */
    default List<ResultTest> listResultTest(long orderId, String samples, String tests)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab57.lab22c1 ");
            select.append(" , lab43.lab43c1 "); //area id
            select.append(" , lab43.lab43c2 "); //area codigo
            select.append(" , lab43.lab43c3 "); //area abreviatura
            select.append(" , lab43.lab43c4 "); //area nombre
            select.append(" , lab57.lab39c1 "); //examen
            select.append(" , lab39c2 "); //código
            select.append(" , lab39c3 "); //Abrv
            select.append(" , lab39c37 "); //Abrv
            select.append(" , lab39.lab24c1 "); //Muestra
            select.append(" , lab39c4 "); //nombre
            select.append(" , lab57c8 "); //estado
            select.append(" , lab57c14 "); //estado
            select.append(" , lab57c15 "); //estado
            select.append(" , lab57c16 "); //estado muestra
            StringBuilder from = new StringBuilder();
            from.append(" FROM     ").append(lab57).append(" as lab57 ");
            from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append(" INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            StringBuilder where = new StringBuilder();
            where.append(" WHERE lab39c37 = 0 ");
            where.append(" AND lab57.lab22c1 = ? ");
            List<Object> params = new ArrayList<>();
            params.add(orderId);
            if (!samples.equals(""))
            {
                where.append(" AND lab39.lab24c1 IN (").append(samples).append(") ");
            }
            if (!tests.equals(""))
            {
                where.append(" AND lab39.lab39c1 IN (").append(tests).append(") ");
            }
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setSampleId(rs.getInt("lab24c1"));
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setEntry(rs.getString("lab57c14") == null);
                bean.setProfileId(rs.getInt("lab57c14"));
                bean.setPackageId(rs.getInt("lab57c15"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                return bean;
            };
            return getJdbcTemplate().query(select.toString() + from.toString() + where.toString(), params.toArray(), mapper);
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados, con
     * la union de los examenes de las ordenes hijas de rellamado.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * ordenes aplicado por el usuario
     * @param orderIds Id´s de ordenes
     * @param areabypackage
     * @param language
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> getTestsUnionDaughter(final ResultFilter resultFilter, List<Long> orderIds, boolean areabypackage) throws Exception
    {
        try
        {
            List<ResultTest> listResults = new LinkedList<>();
            List<Integer> years = new LinkedList<>();
            String lab22;
            String lab57;
            String lab95;
            String lab221;
            int currentYear = DateTools.dateToNumberYear(new Date());
            if (orderIds != null || orderIds.size() > 0)
            {
                // Consulta de ordenes por historico:
                Collections.sort(orderIds);
                Long vInitial = orderIds.get(0);
                Long vFinal = orderIds.size() > 1 ? orderIds.get(orderIds.size() - 1) : orderIds.get(0);
                years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            } else
            {
                switch (resultFilter.getFilterId())
                {
                    case 2:
                        years = Tools.listOfConsecutiveYears(String.valueOf(resultFilter.getIdExternalOrder()), String.valueOf(resultFilter.getIdExternalOrder()));
                        break;
                    case 1:
                        years = Tools.listOfConsecutiveYears(String.valueOf(resultFilter.getFirstOrder()), String.valueOf(resultFilter.getLastOrder()));
                        break;
                    case 0:
                        years = Tools.listOfConsecutiveYears(String.valueOf(resultFilter.getFirstOrder()), String.valueOf(resultFilter.getLastOrder()));
                        break;
                    default:
                        break;
                }
            }

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
                lab221 = year.equals(currentYear) ? "lab221" : "lab221_" + year;

                StringBuilder select = new StringBuilder();
                StringBuilder selectCommit = new StringBuilder();
                selectCommit.append(ISOLATION_READ_UNCOMMITTED);

                select.append("SELECT   lab57.lab22c1 ");
                select.append("       , lab43.lab43c1 "); //area id
                select.append("       , lab43.lab43c2 "); //area codigo
                select.append("       , lab43.lab43c3 "); //area abreviatura
                select.append("       , lab43.lab43c4 "); //area nombre
                select.append("       , lab43.lab43c8 "); //area validación parcial
                select.append("       , lab43.lab43c9 "); //area nombre ingles
                select.append("       , lt.lab24c1 AS ltlab24c1 "); //muestra id
                select.append("       , lt.lab24c9 AS ltlab24c9 "); //muestra codigo
                select.append("       , lt.lab24c2 AS ltlab24c2 "); //muestra nombre
                select.append("       , lab57.lab45c2 "); //unidad nombre
                select.append("       , lab04.lab04c2 "); //Nombre suario que valido
                select.append("       , lab04.lab04c3 "); //Apellido suario que valido
                select.append("       , lab04.lab04c10 "); //Identificación de Usuario que valido
                select.append("       , lab04.lab04c12 "); //Firma del usuario que valido(Imagen)
                select.append("       , lab04.lab04c13 "); //Codigo de firma del usuario que valido
                select.append("       , userPrevalidation.lab04c2 AS userPrevalidationc2 "); //Nombre suario que prevalido
                select.append("       , userPrevalidation.lab04c3 AS userPrevalidationc3 "); //Apellido suario que prevalido
                select.append("       , userPrevalidation.lab04c10 AS userPrevalidationc10 "); //Identificación de Usuario que prevalido
                select.append("       , userPrevalidation.lab04c12 AS userPrevalidationc12 "); //Firma del usuario que prevalido(Imagen)
                select.append("       , userPrevalidation.lab04c13 AS userPrevalidationc13 "); //Codigo de firma del usuario que prevalido
                select.append("       , lab57.lab39c1 "); //examen
                select.append("       , lab39.lab39c2 "); //código
                select.append("       , lab39.lab39c3 "); //abreviatura
                select.append("       , lab39.lab39c4 "); //nombre
                select.append("       , lab39.lab39c32 "); //Titulo de grupo
                select.append("       , lab39.lab39c37 "); //Tipo de prueba
                select.append("       , lab39.lab39c42 "); //Orden impresion
                select.append("       , lab39.lab39c12 "); //decimales
                select.append("       , lab39.lab39c27 "); //confidencial
                select.append("       , lab39.lab39c11 "); //tipo resultado
                select.append("       , lab39.lab39c33 "); //Comentario Fijo
                select.append("       , lab39.lab39c58 "); //Nombre examen ingles
                select.append("       , lab39.lab39c54 "); //Nombre examen ingles
                select.append("       , lab39.lab39c59 "); //Comentario fijo en ingles
                select.append("       , lab39.lab39c60 "); //Comentario preliminar en ingles
                select.append("       , lab39.lab39c61 "); //Comentario general en ingles
                select.append("       , lab57c1 "); //resultado
                select.append("       , lab57c2 "); //fecha resultado
                select.append("       , lab57c3 "); //usuario del resultado
                select.append("       , lab57c5 "); //usuario de ingreso
                select.append("       , lab57c8 "); //estado
                select.append("       , lab57c16 "); //estado muestra
                select.append("       , lab57.lab57c9 "); //patología
                select.append("       , lab57.lab48c12 "); //referecnia mínima
                select.append("       , lab57.lab48c13 "); //referec2nia máxima
                select.append("       , lab57.lab48c5 "); //panico minimo
                select.append("       , lab57.lab48c6 "); //panico máximo
                select.append("       , lab57.lab48c14 "); //reportable minimo
                select.append("       , lab57.lab48c15 "); //reportable máximo
                select.append("       , lab57.lab57c22 "); //
                select.append("       , lab57.lab57c23 "); //
                select.append("       , lab57.lab57c63 "); //
                select.append("       , lab57.lab57c70 "); // Resultado en ingles
                select.append("       , lab48c16 "); //pánico crítico
                select.append("       , lab48c9 "); //comentario del valor de refererncia
                select.append("       , lab48c20 "); //comentario del valor de refererncia en ingles
                select.append("       , lab50n.lab50c2 as lab50c2n "); //normal literal
                select.append("       , lab50p.lab50c2 as lab50c2p "); //pánico literal
                select.append("       , lab57c32 "); //tiene comentario
                select.append("       , lab57c33 "); //numero de repeticiones
                select.append("       , lab57c24 "); //numero modificacion
                select.append("       , lab57c26 "); //Tiene antibiograma
                select.append("       , lab57c42 "); //Tiene plantilla
                select.append("       , lab95c1 "); //comentario
                select.append("       , lab95c2 "); //fecha comentario
                select.append("       , lab95c3 "); //patología comentario
                select.append("       , lab95.lab04c1 "); //usuario comentario
                select.append("       , lab57.lab57c27 "); //delta mínimo
                select.append("       , lab57.lab57c28 "); //delta máximmo
                select.append("       , lab57.lab57c6 "); //último resultado
                select.append("       , lab57.lab57c7 "); //fecha último resultado
                select.append("       , lab57.lab57c30 "); //penúltimo resultado
                select.append("       , lab57.lab57c31 "); //fecha penúltimo resultado
                select.append("       , lab57.lab57c18 "); //fecha validacion
                select.append("       , lab57.lab57c19 "); //usuario validacion
                select.append("       , lab57.lab57c20 "); //fecha prevalidacion
                select.append("       , lab57.lab57c21 "); //usuario prevvalidacion
                select.append("       , lab57.lab57c4 "); //fecha ingreso
                select.append("       , lt.lab24c10 "); //tipo laboratorio
                select.append("       , lab57c35 "); //tipo ingreso resultado
                select.append("       , lab57c36 "); //tipo ingreso microbiología
                select.append("       , lab57c37 "); //Fecha verificación
                select.append("       , lab57c38 "); //Id usuario verifica
                select.append("       , lab57c39 "); //Fecha Toma
                select.append("       , lab57c40 "); //Usuario toma
                select.append("       , lab57c14 "); //Id Perfil Padre
                select.append("       , lab57c15 "); //Id paquete Padre
                select.append("       , lab39T.lab39c4 AS lab39c4t "); //Nombre paquete
                select.append("       , lab39T.lab39c38 AS lab39c38t "); //Examenes dependientes
                select.append("       , lab39T.lab39c58 AS lab39c58t "); //Nombre paquete ingles
                select.append("       , lab57c10 "); //bloqueo
                select.append("       , lab57c11 "); //bloqueo fecha
                select.append("       , lab57c12 "); //bloqueo usuario
                select.append("       , lab57c41 "); //Adjuntos del resultado
                select.append("       , userBlock.lab04c2 AS lab04c2_3 "); //bloqueo usuario
                select.append("       , userBlock.lab04c3 AS lab04c3_3 "); //bloqueo usuario
                select.append("       , userBlock.lab04c4 AS lab04c4_3 "); //bloqueo usuario
                select.append("       , lab69.lab69c1 "); //permitir acceso por el área
                select.append("       , lab69.lab69c2 "); //permitir validacion
                select.append("       , lab72.lab39c1 AS lab72_1 "); // excluir prueba
                select.append("       , lab64.lab64c2 "); //Cód Tecnica
                select.append("       , lab64.lab64c3 "); //Nombre tecnica
                select.append("       , lab39P.lab39c1 AS lab39c1p "); //Id perfil
                select.append("       , lab39P.lab39c4 AS lab39c4p "); //Nombre perfil
                select.append("       , lab39P.lab39c58 AS lab39c58p "); //Nombre perfil en ingles
                select.append("       , lab39P.lab39c38 AS lab39c38p "); //Examenes dependientes
                select.append("       , lab39P.lab39c42 AS lab39c42p "); //orden de impresion del perfil
                select.append("       , lab57c43 "); //tiene delta
                select.append("       , lab39.lab39c15 "); //Dias maximo de modificacion
                select.append("       , lab39.lab39c41 "); //Dias maximo de modificacion de impresion
                select.append("       , lab57.lab57c67 "); //Comentario determinaciones
                select.append("       , lab57.lab57c71 "); //Comentario II

                StringBuilder from = new StringBuilder();
                from.append(" FROM  ").append(lab57).append(" as lab57 ");
                from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
                from.append(" INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                from.append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 ");
                from.append(" LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 ");
                from.append(" LEFT JOIN lab39 lab39T ON lab39T.lab39c1 = lab57.lab57c15 ");
                from.append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 ");
                from.append(" LEFT JOIN lab04 AS userPrevalidation ON userPrevalidation.lab04c1 = lab57.lab57c21 ");
                from.append(" LEFT JOIN lab04 AS userBlock ON userBlock.lab04c1 = lab57.lab57c12 ");
                from.append(" LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 ");
                from.append(" LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 ");
                from.append(" LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 ");
                from.append(" LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 ");
                from.append(" LEFT JOIN ").append(lab95).append(" AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
                from.append(" LEFT JOIN lab69 ON lab69.lab43c1 = lab39.lab43c1 AND lab69.lab04c1 = ").append(resultFilter == null ? 0 : resultFilter.getUserId()).append(" ");
                from.append(" LEFT JOIN lab72 ON lab72.lab39c1 = lab39.lab39c1 AND lab72.lab04c1 = ").append(resultFilter == null ? 0 : resultFilter.getUserId()).append(" ");;
                StringBuilder where = new StringBuilder();
                where.append(" WHERE lab57.lab57c25 = 1 AND lab57c10 = 0 AND lab39.lab39c37 = 0 ");

                List<Object> params = new ArrayList<>();

                if (resultFilter != null)
                {
                    if (resultFilter.isApplyGrowth())
                    {
                        select.append(", lab57.lab24c1_1 ");
                        select.append(", lab24.lab24c2 ");
                        select.append(", lab24.lab24c9 ");
                        select.append(", lab57.lab158c1 ");
                        select.append(", lab158.lab158c2 ");
                        select.append(", lab158.lab158c3 ");
                        select.append(", lab57.lab201c1 ");
                        select.append(", lab201.lab201c2 ");
                        select.append(", lab200.lab200c2 ");
                        select.append(", lab200.lab200c3 ");
                        select.append(", userGrowth.lab04c1 AS lab04c1_1 ");
                        select.append(", userGrowth.lab04c2 AS lab04c2_1 ");
                        select.append(", userGrowth.lab04c3 AS lab04c3_1 ");
                        select.append(", userGrowth.lab04c4 AS lab04c4_1 ");

                        from.append(" LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 ");
                        from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = lab57.lab24c1_1 ");
                        from.append(" LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 ");
                        from.append(" LEFT JOIN lab201 ON lab201.lab201c1 = lab57.lab201c1 ");
                        from.append(" LEFT JOIN lab04 AS userGrowth ON userGrowth.lab04c1 = lab200.lab04c1_1 ");
                    }

                    switch (resultFilter.getFilterId())
                    {
                        case 2:
                            from.append(" JOIN").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                            // Filtramos los resultados de los examenes por el id de la orden externa
                            where.append(" AND lab22.lab22c7 = ? ");
                            params.add(resultFilter.getIdExternalOrder());
                            params.add(resultFilter.getIdExternalOrder());
                            break;
                        case 1:
                            where.append(" AND lab39.lab39c37 != 2 AND lab57.lab22c1 BETWEEN ? AND ? ");
                            params.add(resultFilter.getFirstOrder());
                            params.add(resultFilter.getLastOrder());
                            break;
                        case 0:
                            where.append(" AND lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                            params.add(resultFilter.getFirstOrder());
                            params.add(resultFilter.getLastOrder());
                            break;
                        default:
                            break;
                    }

                    if (resultFilter.getTestsId() != null && !resultFilter.getTestsId().isEmpty())
                    {
                        if (resultFilter.getTestsId().size() > 1)
                        {
                            String in = resultFilter.getTestsId().stream()
                                    .map(testId -> testId.toString())
                                    .collect(Collectors.joining(","));
                            where.append(" AND lab39.lab39c1 IN (").append(in).append(") ");
                        } else
                        {
                            where.append(" AND lab39.lab39c1 = ? ");
                            params.add(resultFilter.getTestsId().get(0));
                        }
                    }
                }

                if (resultFilter != null)
                {
                    if (!resultFilter.isConfidential())
                    {
                        where.append(" AND lab39.lab39c27 = 0 ");
                    }
                } else
                {
                    //Filtro para examenes confidenciales segun permisos del usuario
                    if (!getUser().isConfidential())
                    {
                        where.append(" AND lab39.lab39c27 = 0 ");
                    }
                }

                //Se copia las condiciones para la consulta de la union de hijos de rellamados
                StringBuilder whereUnion = new StringBuilder();
                whereUnion.append(where.toString());
                if (orderIds != null && !orderIds.isEmpty())
                {
                    if (orderIds.size() > 1)
                    {
                        where.append(" AND lab57.lab22c1 BETWEEN ? AND ? ");
                        params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                        params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                        //Se agrega la condicion para la consulta para la union
                        whereUnion.append(" AND lab221.lab22c1_1 BETWEEN ? AND ? ");
                        params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                        params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                    } else
                    {
                        where.append(" AND lab57.lab22c1 = ? ");
                        params.add(orderIds.get(0));
                        //Se agrega la condicion para la consulta para la union
                        whereUnion.append(" AND lab221.lab22c1_1 = ? ");
                        params.add(orderIds.get(0));
                    }
                }
                StringBuilder union = new StringBuilder();
                union.append(selectCommit);
                union.append("(")
                        .append(select.toString())
                        .append(from.toString())
                        .append(where.toString())
                        .append(") UNION ALL (")
                        .append(select.toString())
                        .append(from.toString())
                        .append(" INNER JOIN " + lab221 + " AS lab221 ON lab221.lab22c1_2 = lab57.lab22c1 ")
                        .append(whereUnion.toString())
                        .append(" )");
                RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
                {
                    ResultTest bean = new ResultTest();
                    bean.setOrder(rs.getLong("lab22c1"));
                    bean.setProfileId(rs.getInt("lab39c1p"));
                    bean.setProfileName(rs.getString("lab39c4p"));
                    bean.setProfileNameEnglish(rs.getString("lab39c58p") == null ? "NO DATA LANGUAGE" : rs.getString("lab39c58p"));                    
                    bean.setDependentExam(rs.getInt("lab39c38p") == 1 || rs.getInt("lab39c38t") == 1);
                    bean.setPackageId(rs.getInt("lab57c15"));
                    bean.setPackageName(rs.getString("lab39c4t"));
                    bean.setPackageNameEnglish(rs.getString("lab39c58t"));
                    bean.setPrintSortProfile(rs.getInt("lab39c42p"));
                    bean.setTestId(rs.getInt("lab39c1"));
                    bean.setTestCode(rs.getString("lab39c2"));
                    bean.setTestName(rs.getString("lab39c4"));
                    bean.setNameTestEnglish(rs.getString("lab39c58") == null ? "NO DATA LANGUAGE" : rs.getString("lab39c58"));
                    bean.setFixedCommentEnglish(rs.getString("lab39c59") == null ? "" : rs.getString("lab39c59"));
                    bean.setPrintCommentEnglish(rs.getString("lab39c60") == null ? "" : rs.getString("lab39c60"));
                    bean.setTemplateComment(rs.getString("lab39c54") == null ? "" : rs.getString("lab39c54"));
                    bean.setGeneralInformationEnglish(rs.getString("lab39c61") == null ? "" : rs.getString("lab39c61"));
                    bean.setTestType(rs.getInt("lab39c37"));
                    bean.setGroupTitle(rs.getString("lab39c32"));
                    bean.setPrintSort(rs.getInt("lab39c42"));
                    bean.setEntry(rs.getString("lab57c14") == null);
                    bean.setCommentResult(rs.getString("lab57c71"));

                    bean.setSampleId(rs.getInt("ltlab24c1"));
                    bean.setSampleCode(rs.getString("ltlab24c9"));
                    bean.setSampleName(rs.getString("ltlab24c2"));
                    bean.setEntryDate(rs.getTimestamp("lab57c4"));
                    bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                    bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));
                    bean.setAttachmentTest(rs.getInt("lab57c41"));
                    bean.setMaxDays(rs.getInt("lab39c15"));
                    bean.setMaxPrintDays(rs.getInt("lab39c41"));
                    bean.setTechnique(rs.getString("lab64c3"));
                    bean.setCodetechnique(rs.getString("lab64c2"));

                    bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                    bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                    //TODO: Desencriptar el resultado
                    bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                    bean.setResultDate(rs.getTimestamp("lab57c2"));
                    bean.setResultUserId(rs.getInt("lab57c3"));

                    bean.setResultEnglish(rs.getString("lab57c70") == null ? "" : Tools.decrypt(rs.getString("lab57c70")));

                    bean.setPrintDate(rs.getTimestamp("lab57c22"));
                    bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                    bean.setTakenDate(rs.getTimestamp("lab57c39"));
                    bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                    bean.setEntryType(rs.getShort("lab57c35"));
                    bean.setState(rs.getInt("lab57c8"));
                    bean.setSampleState(rs.getInt("lab57c16"));

                    bean.setAreaId(rs.getInt("lab57c15") == 0 || areabypackage ? rs.getInt("lab43c1") : 0);
                    bean.setAreaCode(rs.getInt("lab57c15") == 0 || areabypackage ? rs.getString("lab43c2") : "");
                    bean.setAreaAbbr(rs.getInt("lab57c15") == 0 || areabypackage ? rs.getString("lab43c3") : "");
                    bean.setAreaName(rs.getInt("lab57c15") == 0 || areabypackage ? rs.getString("lab43c4") : "");
                    bean.setNameAreaEnglish(rs.getInt("lab57c15") == 0 || areabypackage ? (rs.getString("lab43c9") == null ? "NO DATA LANGUAGE" : rs.getString("lab43c9")) : "");

                    bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                    bean.setResultType(rs.getShort("lab39c11"));
                    bean.setPathology(rs.getInt("lab57c9"));
                    bean.setRefLiteral(rs.getString("lab50c2n"));
                    bean.setPanicLiteral(rs.getString("lab50c2p"));
                    bean.setConfidential(rs.getInt("lab39c27") == 1);
                    bean.setRepeatAmmount(rs.getInt("lab57c33"));
                    bean.setModificationAmmount(rs.getInt("lab57c24"));
                    bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                    bean.setHasTemplate(rs.getInt("lab57c42") == 1);
                    bean.setDeterminations(rs.getString("lab57c67"));

                    BigDecimal refMin = rs.getBigDecimal("lab48c12");
                    BigDecimal refMax = rs.getBigDecimal("lab48c13");
                    if (!rs.wasNull())
                    {
                        bean.setRefMin(refMin);
                        bean.setRefMax(refMax);

                        String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                        String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                        bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                    } else
                    {
                        bean.setRefMin(BigDecimal.ZERO);
                        bean.setRefMax(BigDecimal.ZERO);
                        bean.setRefInterval(bean.getRefLiteral());
                    }

                    BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                    BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                    if (!rs.wasNull())
                    {
                        bean.setPanicMin(panicMin);
                        bean.setPanicMax(panicMax);
                        String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                        String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                        bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                    } else
                    {
                        bean.setPanicMin(BigDecimal.ZERO);
                        bean.setPanicMax(BigDecimal.ZERO);
                        bean.setPanicInterval(bean.getPanicLiteral());
                    }

                    BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                    BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                    if (!rs.wasNull())
                    {
                        bean.setReportedMin(reportedMin);
                        bean.setReportedMax(reportedMax);
                        String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                        String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                        bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                    } else
                    {
                        bean.setReportedMin(BigDecimal.ZERO);
                        bean.setReportedMax(BigDecimal.ZERO);
                    }

                    BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                    BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                    if (!rs.wasNull())
                    {
                        bean.setDeltaMin(deltaMin);
                        bean.setDeltaMax(deltaMax);
                        bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                    } else
                    {
                        bean.setDeltaMin(BigDecimal.ZERO);
                        bean.setDeltaMax(BigDecimal.ZERO);
                        bean.setDeltaInterval("");
                    }
                    bean.setRefComment(rs.getString("lab48c9"));
                    bean.setRefCommentEnglish(rs.getString("lab48c20") == null ? "NO DATA LANGUAGE" : rs.getString("lab48c20"));

                    bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                    bean.setLastResultDate(rs.getDate("lab57c7"));
                    bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                    bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                    bean.setCritic(rs.getShort("lab48c16"));
                    bean.setUnit(rs.getString("lab45c2"));
                    bean.setAbbreviation(rs.getString("lab39c3"));
                    bean.setDigits(rs.getShort("lab39c12"));
                    bean.setHasComment(rs.getShort("lab57c32") == 1);
                    bean.setPrintComment(rs.getString("lab57c63"));

                    //Usuario que prevalida
                    if (bean.getState() == LISEnum.ResultTestState.PREVIEW.getValue())
                    {
                        //PreValidacion del examen
                        bean.setValidationDate(rs.getTimestamp("lab57c20"));
                        bean.setValidationUserId(rs.getInt("lab57c21") == 0 ? null : rs.getInt("lab57c21"));
                        bean.setValidationUserName(rs.getString("userPrevalidationc2"));
                        bean.setValidationUserLastName(rs.getString("userPrevalidationc3"));
                        bean.setValidationUserIdentification(rs.getString("userPrevalidationc10"));
                        bean.setValidationUserSignatureCode(rs.getString("userPrevalidationc13"));
                        /*Firma*/

 /*String photo64 = "";
                            byte[] photoBytes = rs.getBytes("userPrevalidationc12");
                            if (photoBytes != null)
                            {
                                photo64 = Base64.getEncoder().encodeToString(photoBytes);
                            }

                            bean.setValidationUserSignature(photo64);*/
                    } else if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())

                    {
                        //Validacion del examen
                        bean.setValidationDate(rs.getTimestamp("lab57c18"));
                        bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));
                        //Usuario que valido
                        bean.setValidationUserName(rs.getString("lab04c2"));
                        bean.setValidationUserLastName(rs.getString("lab04c3"));
                        bean.setValidationUserIdentification(rs.getString("lab04c10"));
                        bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                        /*Firma*/
 /*String photo64 = "";
                            byte[] photoBytes = rs.getBytes("lab04c12");
                            if (photoBytes != null)
                            {
                                photo64 = Base64.getEncoder().encodeToString(photoBytes);
                            }
                            bean.setValidationUserSignature(photo64);*/
                    }

                    ResultTestComment comment = new ResultTestComment();
                    comment.setOrder(rs.getLong("lab22c1"));
                    comment.setTestId(rs.getInt("lab39c1"));
                    comment.setComment(rs.getString("lab95c1"));
                    comment.setCommentDate(rs.getDate("lab95c2"));
                    comment.setPathology(rs.getShort("lab95c3"));
                    comment.setUserId(rs.getInt("lab04c1"));
                    comment.setCommentChanged(false);
                    bean.setResultComment(comment);

                    if (resultFilter != null && resultFilter.isApplyGrowth())
                    {
                        MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
                        microbiologyGrowth.setOrder(null);
                        microbiologyGrowth.setSubSample(null);
                        microbiologyGrowth.setTest(null);
                        if (rs.getString("lab24c1_1") != null)
                        {
                            microbiologyGrowth.getSample().setId(rs.getInt("lab24c1_1"));
                            microbiologyGrowth.getSample().setName(rs.getString("lab24c2"));
                            microbiologyGrowth.getSample().setCodesample(rs.getString("lab24c9"));
                        } else
                        {
                            microbiologyGrowth.setSample(null);
                        }

                        if (rs.getString("lab158c1") != null)
                        {
                            microbiologyGrowth.getAnatomicalSite().setId(rs.getInt("lab158c1"));
                            microbiologyGrowth.getAnatomicalSite().setName(rs.getString("lab158c2"));
                            microbiologyGrowth.getAnatomicalSite().setAbbr(rs.getString("lab158c3"));
                        } else
                        {
                            microbiologyGrowth.setAnatomicalSite(null);
                        }

                        if (rs.getString("lab201c1") != null)
                        {
                            microbiologyGrowth.getCollectionMethod().setId(rs.getInt("lab201c1"));
                            microbiologyGrowth.getCollectionMethod().setName(rs.getString("lab201c2"));
                        } else
                        {
                            microbiologyGrowth.setCollectionMethod(null);
                        }

                        /*Usuario Siembra*/
                        if (rs.getString("lab04c1_1") != null)
                        {
                            microbiologyGrowth.getUserGrowth().setId(rs.getInt("lab04c1_1"));
                            microbiologyGrowth.getUserGrowth().setName(rs.getString("lab04c2_1"));
                            microbiologyGrowth.getUserGrowth().setLastName(rs.getString("lab04c3_1"));
                            microbiologyGrowth.getUserGrowth().setUserName(rs.getString("lab04c4_1"));
                        } else
                        {
                            microbiologyGrowth.setUserGrowth(null);
                        }

                        microbiologyGrowth.setLastTransaction(rs.getTimestamp("lab200c2"));
                        microbiologyGrowth.setDateGrowth(rs.getTimestamp("lab200c3"));

                        bean.setMicrobiologyGrowth(microbiologyGrowth);
                    }

                    bean.setLaboratoryType(rs.getString("lab24c10"));

                    bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                    ResultTestBlock block = new ResultTestBlock();
                    block.setOrder(bean.getOrder());
                    block.setTestId(bean.getTestId());
                    block.setBlocked(rs.getShort("lab57c10") == 1);
                    block.setDate(rs.getTimestamp("lab57c11"));
                    AuthorizedUser userBlock = new AuthorizedUser(rs.getInt("lab57c12"));
                    userBlock.setName(rs.getString("lab04c2_3"));
                    userBlock.setLastName(rs.getString("lab04c3_3"));
                    userBlock.setUserName(rs.getString("lab04c4_3"));
                    block.setUser(userBlock);
                    bean.setBlock(block);

                    bean.setGrantAccess(rs.getShort("lab69c1") == 1);
                    if (bean.isGrantAccess())
                    {
                        bean.setGrantAccess(rs.getInt("lab72_1") == 0);
                    }
                    bean.setGrantValidate(bean.isGrantAccess() && rs.getShort("lab69c2") == 1);
                    bean.setDelta(rs.getShort("lab57c43") == 1);

                    listResults.add(bean);
                    return bean;
                };
                getJdbcTemplate().query(union.toString(), mapper, params.toArray());
            }

            return listResults;
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * //METODO CLONADO PARA QUITAR VALIDACION DEL TOKEN DE NT Y USAR LA DE
     * PATIENT Obtiene la lista de exámenes para el procesamiento de los
     * resultados, con la union de los examenes de las ordenes hijas de
     * rellamado.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * ordenes aplicado por el usuario
     * @param orderIds Id´s de ordenes
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> getTestsUnionDaughterApp(final ResultFilter resultFilter, List<Long> orderIds) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();

            select.append("SELECT   lab57.lab22c1 ");
            select.append("       , lab43.lab43c1 "); //area id
            select.append("       , lab43.lab43c2 "); //area codigo
            select.append("       , lab43.lab43c3 "); //area abreviatura
            select.append("       , lab43.lab43c4 "); //area nombre
            select.append("       , lab43.lab43c8 "); //area validación parcial
            select.append("       , lt.lab24c1 AS ltlab24c1 "); //muestra id
            select.append("       , lt.lab24c9 AS ltlab24c9 "); //muestra codigo
            select.append("       , lt.lab24c2 AS ltlab24c2 "); //muestra nombre
            select.append("       , lab45.lab45c1 "); //unidad id
            select.append("       , lab45.lab45c2 "); //unidad nombre
            select.append("       , lab45.lab45c3 "); //unidad internacional
            select.append("       , lab45.lab45c4 "); //unidad factor de conversion
            select.append("       , lab04.lab04c2 "); //Nombre suario que valido
            select.append("       , lab04.lab04c3 "); //Apellido suario que valido
            select.append("       , lab04.lab04c10 "); //Identificación de Usuario que valido
            select.append("       , lab04.lab04c12 "); //Firma del usuario que valido(Imagen)
            select.append("       , lab04.lab04c13 "); //Codigo de firma del usuario que valido
            select.append("       , userPrevalidation.lab04c2 AS userPrevalidationc2 "); //Nombre suario que prevalido
            select.append("       , userPrevalidation.lab04c3 AS userPrevalidationc3 "); //Apellido suario que prevalido
            select.append("       , userPrevalidation.lab04c10 AS userPrevalidationc10 "); //Identificación de Usuario que prevalido
            select.append("       , userPrevalidation.lab04c12 AS userPrevalidationc12 "); //Firma del usuario que prevalido(Imagen)
            select.append("       , userPrevalidation.lab04c13 AS userPrevalidationc13 "); //Codigo de firma del usuario que prevalido
            select.append("       , lab57.lab39c1 "); //examen
            select.append("       , lab39.lab39c2 "); //código
            select.append("       , lab39.lab39c3 "); //abreviatura
            select.append("       , lab39.lab39c4 "); //nombre
            select.append("       , lab39.lab39c32 "); //Titulo de grupo
            select.append("       , lab39.lab39c37 "); //Tipo de prueba
            select.append("       , lab39.lab39c42 "); //Orden impresion
            select.append("       , lab39.lab39c12 "); //decimales
            select.append("       , lab39.lab39c27 "); //confidencial
            select.append("       , lab39.lab39c11 "); //tipo resultado
            select.append("       , lab57c1 "); //resultado
            select.append("       , lab57c2 "); //fecha resultado
            select.append("       , lab57c3 "); //usuario del resultado
            select.append("       , lab57c5 "); //usuario de ingreso
            select.append("       , lab57c8 "); //estado
            select.append("       , lab57c16 "); //estado muestra
            select.append("       , lab57.lab57c9 "); //patología
            select.append("       , lab57.lab48c12 "); //referecnia mínima
            select.append("       , lab57.lab48c13 "); //referec2nia máxima
            select.append("       , lab57.lab48c5 "); //panico minimo
            select.append("       , lab57.lab48c6 "); //panico máximo
            select.append("       , lab57.lab48c14 "); //reportable minimo
            select.append("       , lab57.lab48c15 "); //reportable máximo
            select.append("       , lab57.lab57c22 "); //
            select.append("       , lab57.lab57c23 "); //
            select.append("       , lab48c16 "); //pánico crítico
            select.append("       , lab50n.lab50c2 as lab50c2n "); //normal literal
            select.append("       , lab50p.lab50c2 as lab50c2p "); //pánico literal
            select.append("       , lab57c32 "); //tiene comentario
            select.append("       , lab57c33 "); //numero de repeticiones
            select.append("       , lab57c24 "); //numero modificacion
            select.append("       , lab57c26 "); //Tiene antibiograma
            select.append("       , lab57c42 "); //Tiene plantilla
            select.append("       , lab95c1 "); //comentario
            select.append("       , lab95c2 "); //fecha comentario
            select.append("       , lab95c3 "); //patología comentario
            select.append("       , lab95.lab04c1 "); //usuario comentario
            select.append("       , lab57.lab57c27 "); //delta mínimo
            select.append("       , lab57.lab57c28 "); //delta máximmo
            select.append("       , lab57.lab57c6 "); //último resultado
            select.append("       , lab57.lab57c7 "); //fecha último resultado
            select.append("       , lab57.lab57c30 "); //penúltimo resultado
            select.append("       , lab57.lab57c31 "); //fecha penúltimo resultado
            select.append("       , lab57.lab57c18 "); //fecha validacion
            select.append("       , lab57.lab57c19 "); //usuario validacion
            select.append("       , lab57.lab57c20 "); //fecha prevalidacion
            select.append("       , lab57.lab57c21 "); //usuario prevvalidacion
            select.append("       , lab57.lab57c4 "); //fecha ingreso
            select.append("       , lt.lab24c10 "); //tipo laboratorio
            select.append("       , lab57c35 "); //tipo ingreso resultado
            select.append("       , lab57c36 "); //tipo ingreso microbiología
            select.append("       , lab57c37 "); //Fecha verificación
            select.append("       , lab57c38 "); //Id usuario verifica
            select.append("       , lab57c39 "); //Fecha Toma
            select.append("       , lab57c40 "); //Usuario toma
            select.append("       , lab57c14 "); //Id Perfil Padre
            select.append("       , lab57c10 "); //bloqueo
            select.append("       , lab57c11 "); //bloqueo fecha
            select.append("       , lab57c12 "); //bloqueo usuario
            select.append("       , lab57c41 "); //Adjuntos del resultado
            select.append("       , userBlock.lab04c2 AS lab04c2_3 "); //bloqueo usuario
            select.append("       , userBlock.lab04c3 AS lab04c3_3 "); //bloqueo usuario
            select.append("       , userBlock.lab04c4 AS lab04c4_3 "); //bloqueo usuario
            select.append("       , lab69.lab69c1 "); //permitir acceso por el área
            select.append("       , lab69.lab69c2 "); //permitir validacion
            select.append("       , lab72.lab39c1 AS lab72_1 "); // excluir prueba
            select.append("       , lab64.lab64c2 "); //Cód Tecnica
            select.append("       , lab64.lab64c3 "); //Nombre tecnica
            select.append("       , lab39P.lab39c1 AS lab39c1p "); //Id perfil
            select.append("       , lab39P.lab39c4 AS lab39c4p "); //Nombre perfil
            select.append("       , lab39P.lab39c42 AS lab39c42p "); //orden de impresion del perfil
            select.append("       , lab57c43 "); //tiene delta
            select.append("       , lab39.lab39c15 "); //Dias maximo de modificacion
            select.append("       , lab39.lab39c41 "); //Dias maximo de modificacion de impresion

            StringBuilder from = new StringBuilder();
            from.append(" FROM    lab57 ");
            from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append(" INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            from.append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 ");
            from.append(" LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 ");
            from.append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 ");
            from.append(" LEFT JOIN lab04 AS userPrevalidation ON userPrevalidation.lab04c1 = lab57.lab57c21 ");
            from.append(" LEFT JOIN lab04 AS userBlock ON userBlock.lab04c1 = lab57.lab57c12 ");
            from.append(" LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
            from.append(" LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 ");
            from.append(" LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 ");
            from.append(" LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 ");
            from.append(" LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 ");
            from.append(" LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
            from.append(" LEFT JOIN lab69 ON lab69.lab43c1 = lab39.lab43c1 AND lab69.lab04c1 = ").append(resultFilter == null ? 0 : resultFilter.getUserId()).append(" ");
            from.append(" LEFT JOIN lab72 ON lab72.lab39c1 = lab39.lab39c1 AND lab72.lab04c1 = ").append(resultFilter == null ? 0 : resultFilter.getUserId()).append(" ");;
            StringBuilder where = new StringBuilder();
            where.append(" WHERE lab39.lab39c37 = 0 ");

            List<Object> params = new ArrayList<>();

            if (resultFilter != null)
            {
                if (resultFilter.isApplyGrowth())
                {
                    select.append(", lab57.lab24c1 ");
                    select.append(", lab24.lab24c2 ");
                    select.append(", lab24.lab24c9 ");
                    select.append(", lab57.lab158c1 ");
                    select.append(", lab158.lab158c2 ");
                    select.append(", lab158.lab158c3 ");
                    select.append(", lab57.lab201c1 ");
                    select.append(", lab201.lab201c2 ");
                    select.append(", lab200.lab200c2 ");
                    select.append(", lab200.lab200c3 ");
                    select.append(", userGrowth.lab04c1 AS lab04c1_1 ");
                    select.append(", userGrowth.lab04c2 AS lab04c2_1 ");
                    select.append(", userGrowth.lab04c3 AS lab04c3_1 ");
                    select.append(", userGrowth.lab04c4 AS lab04c4_1 ");

                    from.append(" LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 ");
                    from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = lab57.lab24c1_1 ");
                    from.append(" LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 ");
                    from.append(" LEFT JOIN lab201 ON lab201.lab201c1 = lab57.lab201c1 ");
                    from.append(" LEFT JOIN lab04 AS userGrowth ON userGrowth.lab04c1 = lab200.lab04c1_1 ");
                }

                switch (resultFilter.getFilterId())
                {
                    case 2:
                        from.append(" JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                        // Filtramos los resultados de los examenes por el id de la orden externa
                        where.append(" AND lab22.lab22c7 = ? ");
                        params.add(resultFilter.getIdExternalOrder());
                        params.add(resultFilter.getIdExternalOrder());
                        break;
                    case 1:
                        where.append(" AND lab39.lab39c37 != 2 AND lab57.lab22c1 BETWEEN ? AND ? ");
                        params.add(resultFilter.getFirstOrder());
                        params.add(resultFilter.getLastOrder());
                        break;
                    case 0:
                        where.append(" AND lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                        params.add(resultFilter.getFirstOrder());
                        params.add(resultFilter.getLastOrder());
                        break;
                    default:
                        break;
                }

                if (resultFilter.getTestsId() != null && !resultFilter.getTestsId().isEmpty())
                {
                    if (resultFilter.getTestsId().size() > 1)
                    {
                        String in = resultFilter.getTestsId().stream()
                                .map(testId -> testId.toString())
                                .collect(Collectors.joining(","));
                        where.append(" AND lab39.lab39c1 IN (").append(in).append(") ");
                    }

                }
            }

            if (resultFilter != null)
            {
                if (!resultFilter.isConfidential())
                {
                    where.append(" AND lab39.lab39c27 = 0 ");
                }
            }

            //Se copia las condiciones para la consulta de la union de hijos de rellamados
            StringBuilder whereUnion = new StringBuilder();
            whereUnion.append(where.toString());
            if (orderIds != null && !orderIds.isEmpty())
            {
                if (orderIds.size() > 1)
                {
                    where.append(" AND lab57.lab22c1 BETWEEN ? AND ? ");
                    params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                    params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                    //Se agrega la condicion para la consulta para la union
                    whereUnion.append(" AND lab221.lab22c1_1 BETWEEN ? AND ? ");
                    params.add(orderIds.stream().mapToLong(order -> order).min().getAsLong());
                    params.add(orderIds.stream().mapToLong(order -> order).max().getAsLong());
                } else
                {
                    where.append(" AND lab57.lab22c1 = ? ");
                    params.add(orderIds.get(0));
                    //Se agrega la condicion para la consulta para la union
                    whereUnion.append(" AND lab221.lab22c1_1 = ? ");
                    params.add(orderIds.get(0));
                }
            }
            StringBuilder union = new StringBuilder();
            union.append("(")
                    .append(select.toString())
                    .append(from.toString())
                    .append(where.toString())
                    .append(") UNION ALL (")
                    .append(select.toString())
                    .append(from.toString())
                    .append(" INNER JOIN lab221 ON lab221.lab22c1_2 = lab57.lab22c1 ")
                    .append(whereUnion.toString())
                    .append(" )");
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setProfileId(rs.getInt("lab39c1p"));
                bean.setProfileName(rs.getString("lab39c4p"));
                bean.setPrintSortProfile(rs.getInt("lab39c42p"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setGroupTitle(rs.getString("lab39c32"));
                bean.setPrintSort(rs.getInt("lab39c42"));
                bean.setEntry(rs.getString("lab57c14") == null);
                bean.setSampleId(rs.getInt("ltlab24c1"));
                bean.setSampleCode(rs.getString("ltlab24c9"));
                bean.setSampleName(rs.getString("ltlab24c2"));
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));
                bean.setAttachmentTest(rs.getInt("lab57c41"));
                bean.setMaxDays(rs.getInt("lab39c15"));
                bean.setMaxPrintDays(rs.getInt("lab39c41"));
                bean.setTechnique(rs.getString("lab64c3"));
                bean.setCodetechnique(rs.getString("lab64c2"));

                bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                //TODO: Desencriptar el resultado
                bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                bean.setResultDate(rs.getTimestamp("lab57c2"));
                bean.setResultUserId(rs.getInt("lab57c3"));

                bean.setPrintDate(rs.getTimestamp("lab57c22"));
                bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                bean.setTakenDate(rs.getTimestamp("lab57c39"));
                bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                bean.setEntryType(rs.getShort("lab57c35"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                bean.setResultType(rs.getShort("lab39c11"));
                bean.setPathology(rs.getInt("lab57c9"));
                bean.setRefLiteral(rs.getString("lab50c2n"));
                bean.setPanicLiteral(rs.getString("lab50c2p"));
                bean.setConfidential(rs.getInt("lab39c27") == 1);
                bean.setRepeatAmmount(rs.getInt("lab57c33"));
                bean.setModificationAmmount(rs.getInt("lab57c24"));
                bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                bean.setHasTemplate(rs.getInt("lab57c42") == 1);

                BigDecimal refMin = rs.getBigDecimal("lab48c12");
                BigDecimal refMax = rs.getBigDecimal("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);

                    String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                    String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                    bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                } else
                {
                    bean.setRefMin(BigDecimal.ZERO);
                    bean.setRefMax(BigDecimal.ZERO);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);
                    String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                    String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                    bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                } else
                {
                    bean.setPanicMin(BigDecimal.ZERO);
                    bean.setPanicMax(BigDecimal.ZERO);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                    String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                    bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                } else
                {
                    bean.setReportedMin(BigDecimal.ZERO);
                    bean.setReportedMax(BigDecimal.ZERO);
                }

                BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(BigDecimal.ZERO);
                    bean.setDeltaMax(BigDecimal.ZERO);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnitId(rs.getInt("lab45c1"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setUnitInternational(rs.getString("lab45c3"));
                bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                //Usuario que prevalida
                if (bean.getState() == LISEnum.ResultTestState.PREVIEW.getValue())
                {
                    //PreValidacion del examen
                    bean.setValidationDate(rs.getTimestamp("lab57c20"));
                    bean.setValidationUserId(rs.getInt("lab57c21") == 0 ? null : rs.getInt("lab57c21"));
                    bean.setValidationUserName(rs.getString("userPrevalidationc2"));
                    bean.setValidationUserLastName(rs.getString("userPrevalidationc3"));
                    bean.setValidationUserIdentification(rs.getString("userPrevalidationc10"));
                    bean.setValidationUserSignatureCode(rs.getString("userPrevalidationc13"));
                    /*Firma*/
                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("userPrevalidationc12");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    bean.setValidationUserSignature(photo64);
                } else if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                {
                    //Validacion del examen
                    bean.setValidationDate(rs.getTimestamp("lab57c18"));
                    bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));
                    //Usuario que valido
                    bean.setValidationUserName(rs.getString("lab04c2"));
                    bean.setValidationUserLastName(rs.getString("lab04c3"));
                    bean.setValidationUserIdentification(rs.getString("lab04c10"));
                    bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                    /*Firma*/
                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("lab04c12");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    bean.setValidationUserSignature(photo64);
                }

                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);

                if (resultFilter != null && resultFilter.isApplyGrowth())
                {
                    MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
                    microbiologyGrowth.setOrder(null);
                    microbiologyGrowth.setSubSample(null);
                    microbiologyGrowth.setTest(null);
                    if (rs.getString("lab24c1") != null)
                    {
                        microbiologyGrowth.getSample().setId(rs.getInt("lab24c1"));
                        microbiologyGrowth.getSample().setName(rs.getString("lab24c2"));
                        microbiologyGrowth.getSample().setCodesample(rs.getString("lab24c9"));
                    } else
                    {
                        microbiologyGrowth.setSample(null);
                    }

                    if (rs.getString("lab158c1") != null)
                    {
                        microbiologyGrowth.getAnatomicalSite().setId(rs.getInt("lab158c1"));
                        microbiologyGrowth.getAnatomicalSite().setName(rs.getString("lab158c2"));
                        microbiologyGrowth.getAnatomicalSite().setAbbr(rs.getString("lab158c3"));
                    } else
                    {
                        microbiologyGrowth.setAnatomicalSite(null);
                    }

                    if (rs.getString("lab201c1") != null)
                    {
                        microbiologyGrowth.getCollectionMethod().setId(rs.getInt("lab201c1"));
                        microbiologyGrowth.getCollectionMethod().setName(rs.getString("lab201c2"));
                    } else
                    {
                        microbiologyGrowth.setCollectionMethod(null);
                    }

                    /*Usuario Siembra*/
                    if (rs.getString("lab04c1_1") != null)
                    {
                        microbiologyGrowth.getUserGrowth().setId(rs.getInt("lab04c1_1"));
                        microbiologyGrowth.getUserGrowth().setName(rs.getString("lab04c2_1"));
                        microbiologyGrowth.getUserGrowth().setLastName(rs.getString("lab04c3_1"));
                        microbiologyGrowth.getUserGrowth().setUserName(rs.getString("lab04c4_1"));
                    } else
                    {
                        microbiologyGrowth.setUserGrowth(null);
                    }

                    microbiologyGrowth.setLastTransaction(rs.getTimestamp("lab200c2"));
                    microbiologyGrowth.setDateGrowth(rs.getTimestamp("lab200c3"));

                    bean.setMicrobiologyGrowth(microbiologyGrowth);
                }

                bean.setLaboratoryType(rs.getString("lab24c10"));

                bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                ResultTestBlock block = new ResultTestBlock();
                block.setOrder(bean.getOrder());
                block.setTestId(bean.getTestId());
                block.setBlocked(rs.getShort("lab57c10") == 1);
                block.setDate(rs.getTimestamp("lab57c11"));
                AuthorizedUser userBlock = new AuthorizedUser(rs.getInt("lab57c12"));
                userBlock.setName(rs.getString("lab04c2_3"));
                userBlock.setLastName(rs.getString("lab04c3_3"));
                userBlock.setUserName(rs.getString("lab04c4_3"));
                block.setUser(userBlock);
                bean.setBlock(block);

                bean.setGrantAccess(rs.getShort("lab69c1") == 1);
                if (bean.isGrantAccess())
                {
                    bean.setGrantAccess(rs.getInt("lab72_1") == 0);
                }
                bean.setGrantValidate(bean.isGrantAccess() && rs.getShort("lab69c2") == 1);
                bean.setDelta(rs.getShort("lab57c43") == 1);

                return bean;
            };

            return getJdbcTemplate().query(ISOLATION_READ_UNCOMMITTED + union.toString(), mapper, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el objeto por examenen y por orden
     *
     * @param order Numero de Orden
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<ResultTest> listByIdTest(long order, int idTest) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab57.lab22c1 "
                    + "       , lab43.lab43c1 " //area id
                    + "       , lab43.lab43c2 " //area codigo
                    + "       , lab43.lab43c3 " //area abreviatura
                    + "       , lab43.lab43c4 " //area nombre
                    + "       , lab43.lab43c8 " //area validación parcial
                    + "       , lt.lab24c1 AS ltlab24c1 " //muestra id
                    + "       , lt.lab24c9 AS ltlab24c9 " //muestra codigo
                    + "       , lt.lab24c2 AS ltlab24c2 " //muestra nombre
                    + "       , lab45.lab45c1 " //unidad id
                    + "       , lab45.lab45c2 " //unidad nombre
                    + "       , lab45.lab45c3 " //unidad internacional
                    + "       , lab45.lab45c4 " //unidad factor de conversion
                    + "       , lab57.lab39c1 " //examen
                    + "       , lab39c2 " //código
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c4 " //nombre
                    + "       , lab39c37 " //nombre
                    + "       , lab39c10 " //formula
                    + "       , lab57c1 " //resultado
                    + "       , lab57c2 " //fecha resultado
                    + "       , lab57c3 " //usuario del resultado
                    + "       , lab57c5 " //usuario de ingreso
                    + "       , lab57c8 " //estado
                    + "       , lab57c16 " //estado muestra
                    + "       , lab39c11 " //tipo resultado
                    + "       , lab57c9 " //patología
                    + "       , lab57.lab48c12 " //referecnia mínima
                    + "       , lab57.lab48c13 " //referec2nia máxima
                    + "       , lab57.lab48c5 " //panico minimo
                    + "       , lab57.lab48c6 " //panico máximo
                    + "       , lab57.lab48c14 " //reportable minimo
                    + "       , lab57.lab48c15 " //reportable máximo
                    + "       , lab57.lab57c22 " //
                    + "       , lab57.lab57c23 " //
                    + "       , lab48c16 " //pánico crítico
                    + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                    + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c12 " //decimales
                    + "       , lab39c27 " //confidencial
                    + "       , lab57c32 " //tiene comentario
                    + "       , lab57c33 " //numero de repeticiones
                    + "       , lab57c24 " //numero modificacion
                    + "       , lab95c1 " //comentario
                    + "       , lab95c2 " //fecha comentario
                    + "       , lab95c3 " //patología comentario
                    + "       , lab95.lab04c1 " //usuario comentario
                    + "       , lab57.lab57c27 " //delta mínimo
                    + "       , lab57.lab57c28 " //delta máximmo
                    + "       , lab57.lab57c6" //último resultado
                    + "       , lab57.lab57c7" //fecha último resultado
                    + "       , lab57.lab57c30" //penúltimo resultado
                    + "       , lab57.lab57c31" //fecha penúltimo resultado
                    + "       , lab57.lab57c18" //fecha validacion
                    + "       , lab57.lab57c19" //usuario validacion
                    + "       , lab04.lab04c2 " //Nombre suario que valido
                    + "       , lab04.lab04c3 " //Apellido suario que valido
                    + "       , lab04.lab04c10 " //Identificación de Usuario que valido
                    + "       , lab04.lab04c12 " //Firma del usuario que valido(Imagen)
                    + "       , lab04.lab04c13 " //Codigo de firma del usuario que valido
                    + "       , lab57.lab57c4" //fecha ingreso
                    + "       , lt.lab24c10" //tipo laboratorio
                    + "       , lab57c35" //tipo ingreso resultado
                    + "       , lab57c36" //tipo ingreso microbiología
                    + "       , lab57c37" //Fecha verificación
                    + "       , lab57c38" //Id usuario verifica
                    + "       , lab57c39" //Fecha Toma
                    + "       , lab57c40" //Usuario toma
                    + "       , lab57c42" //Indica si el examen tiene plantilla
                    + "       , lab57c14" //Id Perfil Padre
                    + "       , lab57c15" //Id Paquete Padre
                    + "       , lab39c28" //Resultado en el ingreso
                    + "       , lab57c50" //Envio al sistema externo
                    + "";

            String from = ""
                    + " FROM    lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                    + " LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " LEFT JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab48.lab50c1_3 "
                    + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab48.lab50c1_1 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ? ";
            List<Object> params = new ArrayList<>();
            params.add(order);
            params.add(idTest);
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));

                bean.setTestId(rs.getInt("lab39c1"));

                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setFormula(rs.getString("lab39c10"));
                bean.setEntry(rs.getString("lab57c14") == null);
                bean.setProfileId(rs.getInt("lab57c14"));
                bean.setPackageId(rs.getInt("lab57c15"));
                bean.setResultRequest(rs.getInt("lab39c28") == 1);
                if (rs.getString("ltlab24c1") != null)
                {
                    bean.setSampleId(rs.getInt("ltlab24c1"));
                    bean.setSampleCode(rs.getString("ltlab24c9"));
                    bean.setSampleName(rs.getString("ltlab24c2"));
                }
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));

                bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                //TODO: Desencriptar el resultado
                bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                bean.setResultDate(rs.getTimestamp("lab57c2"));
                bean.setResultUserId(rs.getInt("lab57c3"));

                bean.setValidationDate(rs.getTimestamp("lab57c18"));
                bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                bean.setPrintDate(rs.getTimestamp("lab57c22"));
                bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                bean.setTakenDate(rs.getTimestamp("lab57c39"));
                bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                bean.setEntryType(rs.getShort("lab57c35"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                bean.setResultType(rs.getShort("lab39c11"));
                bean.setPathology(rs.getInt("lab57c9"));
                bean.setRefLiteral(rs.getString("lab50c2n"));
                bean.setPanicLiteral(rs.getString("lab50c2p"));
                bean.setConfidential(rs.getInt("lab39c27") == 1);
                bean.setRepeatAmmount(rs.getInt("lab57c33"));
                bean.setModificationAmmount(rs.getInt("lab57c24"));
                bean.setHasTemplate(rs.getInt("lab57c42") == 1);

                //Usuario que valido
                if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                {
                    bean.setValidationUserName(rs.getString("lab04c2"));
                    bean.setValidationUserLastName(rs.getString("lab04c3"));
                    bean.setValidationUserIdentification(rs.getString("lab04c10"));
                    bean.setValidationUserSignatureCode(rs.getString("lab04c13"));
                    /*Firma*/
                    String photo64 = "";
                    byte[] photoBytes = rs.getBytes("lab04c12");
                    if (photoBytes != null)
                    {
                        photo64 = Base64.getEncoder().encodeToString(photoBytes);
                    }
                    bean.setValidationUserSignature(photo64);
                }

                BigDecimal refMin = rs.getBigDecimal("lab48c12");
                BigDecimal refMax = rs.getBigDecimal("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);

                    String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                    String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                    bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                } else
                {
                    bean.setRefMin(BigDecimal.ZERO);
                    bean.setRefMax(BigDecimal.ZERO);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);

                    String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                    String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                    bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                } else
                {
                    bean.setPanicMin(BigDecimal.ZERO);
                    bean.setPanicMax(BigDecimal.ZERO);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                    String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                    bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                } else
                {
                    bean.setReportedMin(BigDecimal.ZERO);
                    bean.setReportedMax(BigDecimal.ZERO);
                }

                BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(BigDecimal.ZERO);
                    bean.setDeltaMax(BigDecimal.ZERO);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnitId(rs.getInt("lab45c1"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setUnitInternational(rs.getString("lab45c3"));
                bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);
                bean.setLaboratoryType(rs.getString("lab24c10"));
                bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));
                bean.setSentCentralSystem(rs.getInt("lab57c50"));
                return bean;
            };
            return getJdbcTemplate().query(select + from + where, mapper, params.toArray());

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el item de un examen para saber el genero de sexo.
     *
     * @param id
     * @return Lista de generos
     * @throws Exception Error en la base de datos.
     */
    default Item getGenderTest(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT DISTINCT lab80c1, lab80c2, lab80c3, lab80c4, lab80c5")
                    .append(" FROM lab80")
                    .append(" INNER JOIN lab39")
                    .append(" ON lab80.lab80c1 = lab39.lab39c6 ")
                    .append(" WHERE lab39.lab39c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                Item item = new Item();

                item.setId(rs.getInt("lab80c1"));
                item.setIdParent(rs.getInt("lab80c2"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));

                return item;
            });

        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene informacion importante para el codigo de barras
     *
     * @param order Numero de la orden
     * @param idTest
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default Test getTestInfoBarcode(long order, int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab57.lab22c1")
                    .append(", lab57.lab39c1") //examen
                    .append(", lab57.lab57c39") //Fecha Toma
                    .append(", lab39.lab39c2") //código
                    .append(", lab39.lab39c3") //Abreviatura
                    .append(", lab39.lab39c4") //nombre
                    .append(", lab39.lab39c37 AS testType") //Tipo de examen
                    .append(", lab40.lab40c1") //id del laboratorio
                    .append(", lab40.lab40c10") //url del laboratorio
                    .append(", lab40.lab40c11") //Si se valida al ingreso
                    .append(", lab40.lab40c12") //Si se valida en verificacion
                    .append(", lt.lab24c1 AS ltlab24c1") //muestra id
                    .append(", lt.lab24c9 AS ltlab24c9") //muestra codigo
                    .append(", lt.lab24c2 AS ltlab24c2") //muestra nombre
                    .append(", lt.lab24c10") //tipo laboratorio
                    .append(", lab57.lab57c14 AS profileID ") //Id del perfil
                    .append(", lab16.lab16c3, lab11.lab11c1 ")
                    .append(" FROM lab57") //Resultados
                    .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1") //Examen
                    .append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1") // Laboratorio
                    .append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1") // Muestra                    
                    .append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ")
                    .append(" WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?");

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setProfile(rs.getInt("profileID"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                test.setTestType(rs.getShort("testType"));
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
            }, order, idTest);
        } catch (EmptyResultDataAccessException ex)
        {
            return new Test();
        }
    }

    /**
     * Obtiene informacion importante para el codigo de barras
     *
     * @param order Numero de la ordens
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> getTestsInfoBarcode(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab57.lab22c1")
                    .append(", lab57.lab39c1") //examen
                    .append(", lab57.lab57c39") //Fecha Toma
                    .append(", lab39.lab39c2") //código
                    .append(", lab39.lab39c3") //Abreviatura
                    .append(", lab39.lab39c4") //nombre
                    .append(", lab39.lab39c37 AS testType") //Tipo de examen
                    .append(", lab40.lab40c1") //id del laboratorio
                    .append(", lab40.lab40c10") //url del laboratorio
                    .append(", lab40.lab40c11") //Si se valida al ingreso
                    .append(", lab40.lab40c12") //Si se valida en verificacion
                    .append(", lt.lab24c1 AS ltlab24c1") //muestra id
                    .append(", lt.lab24c9 AS ltlab24c9") //muestra codigo
                    .append(", lt.lab24c2 AS ltlab24c2") //muestra nombre
                    .append(", lt.lab24c10") //tipo laboratorio
                    .append(", lab57.lab57c14 AS profileID ") //Id del perfil
                    .append(", lab16.lab16c3, lab11.lab11c1 ")
                    .append(" FROM lab57") //Resultados
                    .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1") //Examen
                    .append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1") // Laboratorio
                    .append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1") // Muestra                    
                    .append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ")
                    .append(" WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setProfile(rs.getInt("profileID"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                test.setTestType(rs.getShort("testType"));
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
            return new ArrayList<>();
        }
    }

    default List<Deliverytype> listDeliverytypesByOrderAndTestId(long orderId, int testId)
    {
        try
        {

            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab75 = "lab75";

            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            lab75 = year.equals(currentYear) ? "lab75" : "lab75_" + year;

            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab75c1")
                    .append(", lab75c2")
                    .append(", lab75c3")
                    .append(", lab75.lab04c1 AS userId")
                    .append(", lab04.lab04c2 AS userNames")
                    .append(", lab04.lab04c3 AS userLastNames")
                    .append(", lab04.lab04c4 AS userNickName")
                    .append(", lab75c5")
                    .append(", lab39c2")
                    .append(", lab39c3")
                    .append(", lab39c4")
                    .append(", lab75.lab80c1")
                    .append(", lab80.lab80c3")
                    .append(", lab80.lab80c4")
                    .append(", lab80.lab80c5")
                    .append(" FROM ")
                    .append(lab75)
                    .append(" as lab75 ")
                    .append("INNER JOIN lab39 ON lab75.lab75c3 = lab39.lab39c1 ")
                    .append("INNER JOIN lab80 ON lab80.lab80c1 = lab75.lab80c1 ")
                    .append("INNER JOIN lab04 ON lab04.lab04c1 = lab75.lab04c1 ")
                    .append("WHERE lab75.lab75c2 = ").append(orderId)
                    .append(" AND lab75.lab75c3 = ").append(testId);

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                // Nombres del usuario
                String userFullName;
                userFullName = rs.getString("userNames");
                userFullName += rs.getString("userLastNames").isEmpty() ? "" : " " + rs.getString("userLastNames");

                Deliverytype deliverytype = new Deliverytype();
                deliverytype.setId(rs.getInt("lab75c1"));
                deliverytype.setOrder(rs.getLong("lab75c2"));
                deliverytype.setTest(rs.getInt("lab75c3"));
                deliverytype.setCode(rs.getString("lab39c2"));
                deliverytype.setAbbr(rs.getString("lab39c3"));
                deliverytype.setName(rs.getString("lab39c4"));
                deliverytype.setUpdateDate(rs.getTimestamp("lab75c5"));

                deliverytype.setUpdateUser(rs.getLong("userId"));
                deliverytype.setUserFullName(userFullName);
                deliverytype.setUserName(rs.getString("userNickName"));

                //datos-tipo de entrega
                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                deliverytype.setDeliverytype(item);

                return deliverytype;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene informacion basica de los examenes de una orden excluyendo los
     * examenes o perfiles hijos .
     *
     * @param order Numero de Orden
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<ResultTest> listTestOrder(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

            String select = "SELECT   lab57.lab22c1 "
                    + "       , lab57.lab39c1 " //examen
                    + "       , lab39c2 " //código
                    + "       , lab39c4 " //nombre
                    + "       , lab39c37 " //tipo de prueba
                    + "       , lab57c14" //Id Perfil Padre
                    + "       , lab57c15" //Id Paquete Padre
                    + "       , lab904c1" //Id tarifa
                    + "";

            String from = ""
                    + " FROM     " + lab57 + " as lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN " + lab900 + " ON lab900.lab39c1 = lab57.lab39c1 and lab900.lab22c1 = lab57.lab22c1 ";
             
            String where = ""
                    + " WHERE lab57.lab22c1 = ? and ((lab57c14 is null and lab39c37 = 0) or (lab57c15 is null and lab39c37 != 0)) ";
            List<Object> params = new ArrayList<>();
            params.add(order);
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setProfileId(rs.getInt("lab57c14"));
                bean.setPackageId(rs.getInt("lab57c15"));
                
                if(rs.getInt("lab904c1") != 0){
                    Rate rate = new Rate();
                    rate.setId(rs.getInt("lab904c1"));
                    bean.setRate(rate);
                }
                
                return bean;
            };

            return getJdbcTemplate().query(select + from + where, mapper, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Hora reportada al medico
     *
     * @param orderId
     * @param testId
     * @param date
     *
     * @throws Exception Error en la base de datos.
     */
    default void reportedCritical(Long orderId, int testId, Date date) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String update = ""
                    + "UPDATE    " + lab57
                    + " SET      lab57c55 = ? "; //Hora reportada 

            String from = "";
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + from + where, new Object[]
            {
                date, testId, orderId
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
        }
    }

    /**
     * Obtiene todos los examenes de la ultima orden de un paciente
     *
     * @param order Numero de Orden
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<Test> testsLastOrder(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String select = "SELECT   lab39.lab39c1 "
                    + "       , lab39c2 " //Codigo examen
                    + "       , lab39c3 " //Abreviatura examen
                    + "       , lab39c4 " //Examen
                    + "";

            String from = ""
                    + " FROM     " + lab57 + " as lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ";

            String where = ""
                    + " WHERE lab39c55 = 1 AND lab57.lab22c1 = ? "
                    + " ORDER BY lab39.lab39c1";

            List<Object> params = new ArrayList<>();
            params.add(order);

            RowMapper mapper = (RowMapper<Test>) (ResultSet rs, int i) ->
            {
                Test bean = new Test();
                bean.setId(rs.getInt("lab39c1"));
                bean.setCode(rs.getString("lab39c2"));
                bean.setAbbr(rs.getString("lab39c3"));
                bean.setName(rs.getString("lab39c4"));
                return bean;
            };

            return getJdbcTemplate().query(select + from + where, mapper, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param date
     * @param dateInit
     * @param dateFinal
     * @param demographics
     * @param printDemographic
     * @param itemPrintDemographic
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<Order> listOrdersResults(long date, Timestamp dateInit, Timestamp dateFinal, List<Demographic> demographics, String printDemographic, String itemPrintDemographic) throws Exception
    {
        try
        {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(date), String.valueOf(date));
            String lab22;
            String lab57;
            String lab221;

            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years)
            {

                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab221 = year.equals(currentYear) ? "lab221" : "lab221_" + year;

                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT ");
                query.append("lab22.lab22c1, lab04c2, lab04c3, lab04c4, lab221.lab22c1_1, ");
                query.append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, ");
                query.append("lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, ");
                query.append("lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, ");
                query.append("lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, ");
                query.append("lab21c10, lab21c11, lab21c16, lab21c17, ");
                query.append("lab22.lab07c1, lab22c9, lab22.lab04c1, lab22c2, lab22c3 ");
                StringBuilder from = new StringBuilder();
                from.append(" FROM  ").append(lab57).append(" as lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
                from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                from.append("LEFT JOIN ").append(lab221).append(" as lab221 ON lab221.lab22c1_2 = lab22.lab22c1 ");

                SQLTools.buildSQLDemographicSelect(demographics, query, from);

                StringBuilder where = new StringBuilder("");
                where.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c18 BETWEEN ? AND ? AND lab57c8 >= ? AND (lab57c65 = 0 or lab57c65 is null)");
                where.append(" AND lab39.lab07c1 != ? AND lab22.lab21c1 != 0 AND lab22.lab07c1 = 1 AND lab39c27 = 0  AND (lab22c19 = 0 or lab22c19 is null) ");
                where.append(" AND lab22.lab_demo_").append(printDemographic).append(" = ").append(itemPrintDemographic);

                List<Object> params = new ArrayList<>();
                params.add(dateInit);
                params.add(dateFinal);
                params.add(LISEnum.ResultTestState.VALIDATED.getValue());
                params.add(LISEnum.ResultOrderState.CANCELED.getValue());

                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i) ->
                {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setCreatedDate(rs.getTimestamp("lab22c3"));
                    order.setFatherOrder(rs.getLong("lab22c1_1"));
                    order.setState(rs.getInt("lab07c1"));
                    order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                    order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                    order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                    order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                    order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decryptPatient(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decryptPatient(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decryptPatient(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decryptPatient(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decryptPatient(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
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

                    //MEDICO
                    if (query.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null)
                    {
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setCode(rs.getString("lab19c22"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                        order.getPhysician().setAlternativeMails(rs.getString("lab19c24"));
                    }

                    if (query.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null)
                    {
                        //SEDE
                        order.getBranch().setId(rs.getInt("lab05c1"));
                        order.getBranch().setCode(rs.getString("lab05c10"));
                        order.getBranch().setName(rs.getString("lab05c4"));
                    }
                    if (query.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null)
                    {
                        //SERVICIO
                        order.getService().setId(rs.getInt("lab10c1"));
                        order.getService().setCode(rs.getString("lab10c7"));
                        order.getService().setName(rs.getString("lab10c2"));
                    }
                    if (query.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null)
                    {
                        //MEDICO
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setCode(rs.getString("lab19c22"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                    }
                    if (query.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null)
                    {
                        //EMPRESA
                        order.getAccount().setId(rs.getInt("lab14c1"));
                        order.getAccount().setNit(rs.getString("lab14c2"));
                        order.getAccount().setName(rs.getString("lab14c3"));
                    }
                    if (query.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null)
                    {
                        //TARIFA
                        order.getRate().setId(rs.getInt("lab904c1"));
                        order.getRate().setCode(rs.getString("lab904c2"));
                        order.getRate().setName(rs.getString("lab904c3"));
                    }
                    if (query.toString().contains("lab21lab08lab08c1") == true && rs.getString("lab21lab08lab08c1") != null)
                    {
                        //PACIENTE - RAZA
                        order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
                        order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
                        order.getPatient().getRace().setValue(rs.getFloat("lab21lab08lab08c4"));
                    }
                    if (query.toString().contains("lab21lab54lab54c1") == true && rs.getString("lab21lab54lab54c1") != null)
                    {
                        //PACIENTE - TIPO DE DOCUMENTO
                        order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));
                        order.getPatient().getDocumentType().setAbbr(rs.getString("lab21lab54lab54c2"));
                        order.getPatient().getDocumentType().setName(rs.getString("lab21lab54lab54c3"));
                    }
                    if (query.toString().contains("lab103c1") == true && rs.getString("lab103c1") != null)
                    {
                        //TIPO DE ORDEN
                        order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                        order.getType().setCode(rs.getString("lab103c2"));
                        order.getType().setName(rs.getString("lab103c3"));
                        order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                    }
                    //ORDER_HIS
                    if (query.toString().contains("lab22c7") == true)
                    {
                        order.setExternalId(rs.getString("lab22c7"));
                    }
                    if (demographics != null)
                    {
                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics)
                        {
                            if (demographic.getOrigin() != null)
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
                        }
                    }

                    listOrders.add(order);
                    return order;
                };

                List<Order> orderOrdering = getJdbcTemplate().query(query.toString() + from.toString() + where.toString(), mapper, params.toArray());
                orderOrdering = orderOrdering.stream().distinct().collect(Collectors.toList());

                return orderOrdering;
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param idOrder
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default boolean getProccesOrder(long idOrder) throws Exception
    {
        try
        {
            String selectConfidentialOrder = "SELECT COUNT(*) "
                    + " FROM lab57 INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1"
                    + " WHERE lab22c1 = ? and lab39.lab39c27 = 1";
            Integer sql = getJdbcTemplate().queryForObject(selectConfidentialOrder, Integer.class, idOrder);
            if (sql == 0)
            {
                String select = "SELECT COUNT(*) "
                        + "";

                String from = ""
                        + " FROM  Lab57 "
                        + " INNER JOIN Lab22 ON lab22.lab22c1 = lab57.lab22c1 ";
                String where = ""
                        + " WHERE lab57.lab22c1 = ? AND  "
                        + "(SELECT COUNT(*) FROM lab57 INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 WHERE lab22c1 = ?"
                        + " AND lab39.lab39c27 = 0 AND lab39.lab39c37 = 0)"
                        + " = (SELECT COUNT(*) FROM lab57 INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1"
                        + " WHERE lab22c1 = ? AND lab57c8 = 4 AND lab39.lab39c27 = 0 )";
                sql = getJdbcTemplate().queryForObject(select + from + where, Integer.class, idOrder, idOrder, idOrder);
            } else
            {
                sql = 0;
            }
            return sql > 0;
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return false;
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param idOrder
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default Integer getTotalTestOrder(long idOrder) throws Exception
    {
        try
        {
            String select = "SELECT COUNT(lab57.lab39c1) "
                    + "";
            String from = ""
                    + " FROM  Lab57 "
                    + " INNER JOIN Lab39 ON lab39.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab22c1 = ? and lab39.lab39c37 = 0 and lab57c16 = 4";

            return getJdbcTemplate().queryForObject(select + from + where, Integer.class, idOrder);
        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param idOrder
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default List<SuperTest> gettestpendingorder(long idOrder) throws Exception
    {
        try
        {
            String select = "SELECT lab57.lab39c1, lab39c2 , lab39c3, lab39c4, lab39c58  ";
            String from = ""
                    + " FROM  Lab57 "
                    + " INNER JOIN Lab39 ON lab39.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab39c37 = 0 and lab57c14 is null and lab57c8 < 4 and lab22c1 = " + idOrder;

            String union = " UNION ";

            String selectprofile = " SELECT DISTINCT lab57.lab57c14, lab39c2, lab39c3, lab39c4, lab39c58 ";
            String fromprofile = ""
                    + " FROM  Lab57 "
                    + " INNER JOIN Lab39 ON lab39.lab39c1 = lab57.lab57c14 ";
            String whereprofile = ""
                    + " WHERE lab57c8 < 4 and lab22c1 = " + idOrder;

            return getJdbcTemplate().query(select + from + where + union + selectprofile + fromprofile + whereprofile,
                    (ResultSet rs, int i) ->
            {
                SuperTest bean = new SuperTest();
                bean.setId(rs.getInt("lab39c1"));
                bean.setCode(rs.getString("lab39c2"));
                bean.setAbbr(rs.getString("lab39c3"));
                bean.setName(rs.getString("lab39c4"));
                bean.setNameTestEnglish(rs.getString("lab39c58") == null ? "NO DATA LANGUAGE" : rs.getString("lab39c58"));
                return bean;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene todos los examenes validados de una orden
     *
     * @param idOrder
     * @return Lista de Resultados asignados
     * @throws Exception Error en base de datos
     */
    default Integer getTotalTestValidOrder(long idOrder) throws Exception
    {
        try
        {
            String select = "SELECT COUNT(lab57.lab39c1) "
                    + "";
            String from = ""
                    + " FROM  Lab57 "
                    + " INNER JOIN Lab39 ON lab39.lab39c1 = lab57.lab39c1  ";
            String where = ""
                    + " WHERE lab22c1 = ? and lab57c8 >= 4 and lab39.lab39c37 = 0 ";

            return getJdbcTemplate().queryForObject(select + from + where, Integer.class, idOrder);
        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }

    /**
     * Actualiza la fecha de trasnporte de las muestras de una orden
     *
     * @param orders
     * @param user
     * @return True - Si la fecha de atendicon de la orden se actualizo, False-
     * Si no fue así
     * @throws Exception Error en la base de datos.
     */
    default boolean updatePrintSample(List<Order> orders, int user) throws Exception
    {
        try
        {
            // Fecha de atención
            Timestamp concurrentDate = new Timestamp(new Date().getTime());

            String update = "UPDATE lab57 SET lab57c59 = ?, lab57c60 = ? ";
            String where = "WHERE lab57c60 is null and lab22c1 = ? and lab24c1 in ";

            orders.forEach((order) ->
            {
                String samples = "(" + order.getSamples().stream().map(sample -> sample.getId().toString()).collect(Collectors.joining(",")) + ")";
                getJdbcTemplate().update(update + where + samples, concurrentDate, user, order.getOrderNumber());
            });

            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * obtener el estado minimo de la lista de examenes de una orden.
     *
     * @param orderId Número de orden de laboratorio
     * @return estado minimo
     *
     * @throws Exception Error en la base de datos.
     */
    default int checkValidationOrder(long orderId) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  MIN(lab57c8) "
                    + " FROM   " + lab57 + " as lab57 "
                    + " WHERE  lab22c1 = ?";

            int minimalStatus = getJdbcTemplate().queryForObject(select, new Object[]
            {
                orderId
            }, Integer.class);

            return minimalStatus;
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return -1;
        }
    }

    /**
     * Obtiene todos los examenes que no tengan impresa la etiqueta de homebound
     *
     * @param order Numero de Orden
     * @param tests Lista de examenes
     * @return Lista de examenes
     * @throws Exception Error en base de datos
     */
    default List<Integer> testByCodeEmptyHomebound(long order, List<Integer> tests) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT   lab57.lab39c1 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM ").append(lab57).append(" as lab57 ");

            StringBuilder where = new StringBuilder();
            where.append(" WHERE lab57.lab22c1 = ").append(order);
            where.append("  AND lab57.lab39c1 IN(").append(tests.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");
            where.append(" AND lab57c59 IS NULL ");

            return getJdbcTemplate().query(query.toString() + from.toString() + where.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c1");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza la fecha de impresion de etiqueda de homebound
     *
     * @param idOrder Numero de Orden
     * @param tests Lista de examenes
     * @return Lista de examenes
     * @throws Exception Error en base de datos
     */
    default int updatePrintCodeHomebound(long idOrder, List<Integer> tests) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();

        String update = "UPDATE " + lab57 + " SET lab57c59 = ? ";
        String where = " WHERE lab22c1 = ? AND lab39c1 = ? ";

        tests.forEach((test) ->
        {
            parameters.add(new Object[]
            {
                new Timestamp(currentDate.getTime()),
                idOrder,
                test
            });
        });

        return getJdbcTemplate().batchUpdate(update + where, parameters).length;
    }

    /**
     * Update fecha de ingreso de resultados
     *
     * @param order
     * @param idTest
     * @return True si se elimiaron los registros, False si no fueron eliminados
     * @throws Exception
     */
    default boolean updateFechaIngresoDate(Long order, int idTest, Timestamp date) throws Exception
    {
        try
        {
            String update = ""
                    + "UPDATE lab57  "
                    + " SET lab57c2 = ? "; //tiene comentario
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? ";

            getJdbcTemplate().update(update + where, new Object[]
            {
                date, idTest, order
            });

            IntegrationHisLog.info("DATE ACTULIZADA ");
            return true;

        } catch (Exception e)
        {
            IntegrationHisLog.info("DATE  NOT ACTULIZADA  POR " + e);
            return false;
        }

    }

    default List<OrderHeader> preheader(final ResultHeaderFilter filter, String startDate, String endDate) throws Exception
    {
        try
        {
            HashMap<Long, OrderHeader> listOrders = new HashMap<>();

            // Consulta de ordenes por historico:
            String lab57;

            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = new LinkedList<>();

            if (filter.getDays() > 0)
            {
                years = Tools.listOfConsecutiveYears(startDate, endDate);
            } else if (filter.getInit() > 0 && filter.getEnd() > 0)
            {
                years = Tools.listOfConsecutiveYears(String.valueOf(filter.getInit()), String.valueOf(filter.getEnd()));
            }

            for (Integer year : years)
            {
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab57);
                if (tableExists)
                {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    query.append("SELECT lab57c1, lab57.lab22c1, lab57.lab39c1, lab57c8, lab57c14, lab39c37 ");

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39c37 = 0 ");
                    from.append(" LEFT JOIN lab191 ON lab57.lab39c1 = lab191.lab39c1 AND lab57.lab22c1 = lab191.lab22c1 AND lab191.lab118c1 = ").append(filter.getIdSystemTest());

                    if (filter.getIdSystemTest() > 0)
                    {
                        from.append(" INNER JOIN lab61 ON lab57.lab39c1 = lab61.lab39c1 AND lab61.lab118c1 = ").append(filter.getIdSystemTest());
                    }

                    if (filter.getDays() > 0)
                    {
                        from.append(" WHERE lab57.lab22c1 BETWEEN ").append(startDate).append(" AND ").append(endDate);
                    } else
                    {
                        from.append(" WHERE lab57.lab22c1 BETWEEN ").append(filter.getInit()).append(" AND ").append(filter.getEnd());
                    }

                    from.append(" AND ( lab191c2 IS NULL OR lab191c2 = 0 OR lab191c2 = 2 )");

                    RowMapper mapper = (RowMapper<OrderHeader>) (ResultSet rs, int i) ->
                    {

                        OrderHeader order = new OrderHeader();
                        if (!listOrders.containsKey(rs.getLong("lab22c1")))
                        {
                            order.setOrder(rs.getLong("lab22c1"));

                            listOrders.put(order.getOrder(), order);
                        } else
                        {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        TestHeader test = new TestHeader();

                        test.setTestId(rs.getInt("lab39c1"));
                        test.setTestStatus(rs.getInt("lab57c8"));
                        test.setProfileId(rs.getInt("lab57c14"));
                        test.setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.setTypeTest(rs.getInt("lab39c37"));

                        if (!listOrders.get(order.getOrder()).getTests().contains(test))
                        {
                            listOrders.get(order.getOrder()).getTests().add(test);
                        }

                        return order;
                    };
                    getJdbcTemplate().query(query.toString() + "  " + from.toString() + "  ", mapper);
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    default List<ResultHeader> header(final ResultHeaderFilter filter, String startDate, String endDate, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype) throws Exception
    {
        try
        {
            HashMap<Long, ResultHeader> listOrders = new HashMap<>();

            // Consulta de ordenes por historico:
            String lab22;
            String lab60;
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = new LinkedList<>();

            if (filter.getDays() > 0)
            {
                years = Tools.listOfConsecutiveYears(startDate, endDate);
            } else if (filter.getInit() > 0 && filter.getEnd() > 0)
            {
                years = Tools.listOfConsecutiveYears(String.valueOf(filter.getInit()), String.valueOf(filter.getEnd()));
            }

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab60 = year.equals(currentYear) ? "lab60" : "lab60_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab60) : tableExists;
                if (tableExists)
                {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    query.append(" SELECT lab22.lab22c1, lab21c3, lab21c4, lab21c5, lab21c6, lab21c2, lab21lab80.lab80c4, lab21c7, lab22c3, ");
                    query.append(" lab60order.lab60c3 as commentOrder, lab60patient.lab60c3 as diagnosis, lab22.lab103c1, lab103c2, ");
                    query.append(" lab05.lab05c1, lab05c10, lab05c4 ");

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM ").append(lab22).append(" AS lab22 ");
                    from.append(" INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
                    from.append(" INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append(" LEFT JOIN lab60 lab60order ON lab60order.lab60c2 = lab22.lab22c1 ");
                    from.append(" LEFT JOIN lab60 lab60patient ON lab60patient.lab60c2 = lab22.lab21c1 ");
                    from.append(" LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ");
                    from.append(" INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");

                    if (documenttype)
                    {
                        query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                        from.append(" LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    }
                    if (service)
                    {
                        query.append(" , lab22.lab10c1, lab10c2, lab10c7 ");
                        from.append(" LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    }
                    if (account)
                    {
                        query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                        from.append(" LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    }
                    if (physician)
                    {
                        query.append(" ,lab22.lab19c1, lab19c2, lab19c3, lab19c22 ");
                        from.append(" LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    }
                    if (rate)
                    {
                        query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                        from.append(" LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    }
                    if (race)
                    {
                        query.append(" , lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5 ");
                        from.append(" LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    }

                    for (Demographic demographic : demographics)
                    {
                        if (demographic.getId() > -1)
                        {
                            query.append(Tools.createDemographicsQuery(demographic).get(0));
                            from.append(Tools.createDemographicsQuery(demographic).get(1));
                        }
                    }

                    if (filter.getDays() > 0)
                    {
                        from.append(" WHERE lab22.lab22c1 BETWEEN ").append(startDate).append(" AND ").append(endDate);
                    } else
                    {
                        from.append(" WHERE lab22.lab22c1 BETWEEN ").append(filter.getInit()).append(" AND ").append(filter.getEnd());
                    }

                    switch (filter.getTypeEntry())
                    {
                        case 1:
                            from.append(" AND lab22c18 = 1 ");
                            break;
                        case 2:
                            from.append(" AND lab22c18 = 0  )");
                            break;
                    }

                    from.append(" AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");

                    RowMapper mapper = (RowMapper<ResultHeader>) (ResultSet rs, int i) ->
                    {
                        ResultHeader order = new ResultHeader();
                        if (!listOrders.containsKey(rs.getLong("lab22c1")))
                        {
                            order.setOrderId(rs.getLong("lab22c1"));
                            order.setIdentification(Tools.decrypt(rs.getString("lab21c2")));

                            DateFormat datetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date createdDate = new Date(rs.getTimestamp("lab22c3").getTime());
                            order.setCreatedDate(datetime.format(createdDate));

                            order.setOrderTypeId(rs.getInt("lab103c1"));
                            order.setOrderType(rs.getString("lab103c2"));

                            String name = Tools.decrypt(rs.getString("lab21c3"));
                            String name2 = Tools.decrypt(rs.getString("lab21c4"));
                            String lastName = Tools.decrypt(rs.getString("lab21c5"));
                            String surName = Tools.decrypt(rs.getString("lab21c6"));

                            order.setNames(name + (!"".equals(name2) ? " " + name2 : ""));
                            order.setLastnames(lastName + (!"".equals(surName) ? " " + surName : ""));

                            if (rs.getString("lab80c4") != null)
                            {
                                String gender = rs.getString("lab80c4");
                                order.setGender(String.valueOf(gender.charAt(0)));
                            }
                            
                            if(rs.getTimestamp("lab21c7") != null) {
                                DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
                                Date dateFormat = new Date(rs.getTimestamp("lab21c7").getTime());
                                order.setBirthday(simple.format(dateFormat));
                            }

                            List<DemoHeader> demographicsOrder = new LinkedList<>();

                            for (Demographic demographic : demographics)
                            {
                                String[] data;

                                if (demographic.getId() > -1)
                                {
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
                                    demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                } else
                                {
                                    switch (demographic.getId())
                                    {
                                        case Constants.ACCOUNT:
                                            if (account)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab14c1"),
                                                    rs.getString("lab14c2"),
                                                    rs.getString("lab14c3")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                        case Constants.PHYSICIAN:
                                            if (physician)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab19c1"),
                                                    rs.getString("lab19c22"),
                                                    rs.getString("lab19c2") + rs.getString("lab19c3")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                        case Constants.RATE:
                                            if (rate)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab904c1"),
                                                    rs.getString("lab904c2"),
                                                    rs.getString("lab904c3")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                        case Constants.BRANCH:
                                            data = new String[]
                                            {
                                                rs.getString("lab05c1"),
                                                rs.getString("lab05c10"),
                                                rs.getString("lab05c4")
                                            };
                                            demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            break;
                                        case Constants.SERVICE:
                                            if (service)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab10c1"),
                                                    rs.getString("lab10c7"),
                                                    rs.getString("lab10c2")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                        case Constants.RACE:
                                            if (race)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab21lab08lab08c1"),
                                                    rs.getString("lab21lab08lab08c2"),
                                                    rs.getString("lab21lab08lab08c5")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                        case Constants.DOCUMENT_TYPE:
                                            if (documenttype)
                                            {
                                                data = new String[]
                                                {
                                                    rs.getString("lab21lab54lab54c1"),
                                                    rs.getString("lab21lab54lab54c2"),
                                                    rs.getString("lab21lab54lab54c3")
                                                };
                                                demographicsOrder.add(Tools.getDemographicHeader(demographic, data));
                                            }
                                            break;
                                    }
                                }
                            }

                            order.setDemographics(demographicsOrder);
                            listOrders.put(order.getOrderId(), order);

                        } else
                        {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        if (rs.getString("commentOrder") != null && !rs.getString("commentOrder").isEmpty())
                        {
                            String comment = rs.getString("commentOrder");
                            Integer init = comment.indexOf(">", 0);
                            Integer end = comment.indexOf("</", 0);
                            if (init > -1 && end > -1)
                            {
                                comment = comment.substring(comment.indexOf(">", 0), comment.indexOf("</", 0)).replaceFirst(">", "");
                                if (!comment.isEmpty() && !"".equals(comment))
                                {
                                    comment = HtmlToTxt.htmlToString(comment);
                                }
                                listOrders.get(order.getOrderId()).setComment(comment);
                            }
                        }

                        if (rs.getString("diagnosis") != null && !rs.getString("diagnosis").isEmpty())
                        {
                            String diagnosis = rs.getString("diagnosis");
                            Integer init = diagnosis.indexOf(">", 0);
                            Integer end = diagnosis.indexOf("</", 0);
                            if (init > -1 && end > -1)
                            {
                                diagnosis = diagnosis.substring(diagnosis.indexOf(">", 0), diagnosis.indexOf("</", 0)).replaceFirst(">", "");
                                if (!diagnosis.isEmpty() && !"".equals(diagnosis))
                                {
                                    diagnosis = HtmlToTxt.htmlToString(diagnosis);
                                }
                                listOrders.get(order.getOrderId()).setDiagnosis(diagnosis);
                            }
                        }
                        return order;
                    };

                    getJdbcTemplate().query(query.toString() + "  " + from.toString() + "  ", mapper);
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    default List<TestDetail> detail(final DetailFilter filter) throws Exception
    {
        try
        {
            List<TestDetail> listResults = new LinkedList<>();

            Integer year = Tools.YearOfOrder(String.valueOf(filter.getOrderLis()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());

            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;

            boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
            tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
            tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab95) : tableExists;
            if (tableExists)
            {
                String select = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT   lab57.lab22c1 "
                        + "       , lab43.lab43c1 " //area id
                        + "       , lab43.lab43c2 " //area codigo
                        + "       , lab43.lab43c3 " //area abreviatura
                        + "       , lab43.lab43c4 " //area nombre
                        + "       , lab43.lab43c8 " //area validación parcial
                        + "       , lt.lab24c1 AS ltlab24c1 " //muestra id
                        + "       , lt.lab24c9 AS ltlab24c9 " //muestra codigo
                        + "       , lt.lab24c2 AS ltlab24c2 " //muestra nombre
                        + "       , lab45.lab45c1 " //unidad id
                        + "       , lab45.lab45c2 " //unidad nombre
                        + "       , lab45.lab45c3 " //unidad internacional
                        + "       , lab45.lab45c4 " //unidad factor de conversion
                        + "       , lab57.lab57c19 " //Usuario que valido
                        + "       , lab04.lab04c2 " //Nombre suario que valido
                        + "       , lab04.lab04c3 " //Apellido suario que valido
                        + "       , lab57.lab39c1 " //examen                    
                        + "       , lab39.lab39c2 " //código
                        + "       , lab39.lab39c3 " //abreviatura
                        + "       , lab39.lab39c4 " //nombre
                        + "       , lab39.lab39c37 " //Tipo de prueba
                        + "       , lab39.lab39c12 " //decimales
                        + "       , lab39.lab39c27 " //confidencial
                        + "       , lab39.lab39c11 " //tipo resultado
                        + "       , lab57c1 " //resultado
                        + "       , lab57c2 " //fecha resultado
                        + "       , lab57c3 " //usuario del resultado
                        + "       , lab57c5 " //usuario de ingreso
                        + "       , lab57c8 " //estado
                        + "       , lab57c16 " //estado muestra
                        + "       , lab57.lab57c9 " //patología
                        + "       , lab57.lab48c12 " //referecnia mínima
                        + "       , lab57.lab48c13 " //referec2nia máxima
                        + "       , lab57.lab48c5 " //panico minimo
                        + "       , lab57.lab48c6 " //panico máximo
                        + "       , lab57.lab48c14 " //reportable minimo
                        + "       , lab57.lab48c15 " //reportable máximo
                        + "       , lab57.lab57c22 " //
                        + "       , lab57.lab57c23 " //
                        + "       , lab48c16 " //pánico crítico
                        + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                        + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                        + "       , lab57c32 " //tiene comentario
                        + "       , lab57c33 " //numero de repeticiones
                        + "       , lab57c24 " //numero modificacion
                        + "       , lab57c26 " //Tiene antibiograma
                        + "       , lab57c42 " //Tiene plantilla
                        + "       , lab95c1 " //comentario
                        + "       , lab95c2 " //fecha comentario
                        + "       , lab95c3 " //patología comentario
                        + "       , lab95.lab04c1 " //usuario comentario
                        + "       , lab57.lab57c27 " //delta mínimo
                        + "       , lab57.lab57c28 " //delta máximmo
                        + "       , lab57.lab57c6" //último resultado
                        + "       , lab57.lab57c7" //fecha último resultado
                        + "       , lab57.lab57c30" //penúltimo resultado
                        + "       , lab57.lab57c31" //fecha penúltimo resultado
                        + "       , lab57.lab57c18" //fecha validacion
                        + "       , lab57.lab57c19" //fecha validacion
                        + "       , lab57.lab57c4" //fecha ingreso
                        + "       , lt.lab24c10" //tipo laboratorio
                        + "       , lab57c35" //tipo ingreso resultado
                        + "       , lab57c36" //tipo ingreso microbiología
                        + "       , lab57c37" //Fecha verificación
                        + "       , lab57c38" //Id usuario verifica
                        + "       , lab57c39" //Fecha Toma
                        + "       , lab57c40" //Usuario toma
                        + "       , lab57c14" //Id Perfil Padre
                        + "       , lab64.lab64c2" //Cód Tecnica
                        + "       , lab64.lab64c3" //Nombre tecnica
                        + "       , lab39P.lab39c1 AS lab39c1p" //Id perfil
                        + "       , lab39P.lab39c4 AS lab39c4p" //Nombre perfil
                        + "       , lab57c43" //tiene delta
                        + "       , lab39.lab39c50" //Requiere validacion preliminar
                        + "       , lab39.lab39c10" //Formula,
                        + "       , lab57.lab57c20" //Fecha de validacion preliminar,
                        + "       , lab57.lab57c21" //Usuario de validacion preliminar,
                        + "       , lab57.lab57c48" //Ultimo resultado repetido
                        + "       , lab57.lab57c53" //Dilución
                        + "       , lab57.lab40c1" //Laboratorio  
                        + "       , lab57c62" //resultado eliminado
                        + "       , testH.lab61c1 as testHomologation" //Homologación Examen
                        + "       , testP.lab61c1 as profileHomologation" //Homologación Perfil
                        + "       , lab76.lab76c2 AS Mic " //Nombre del microorganismo
                        + "       , lab79.Lab79c2 AS Ant " //Nombre del antibiotico
                        + "       , lab205.lab205c2 AS Result " //Interpretacion
                        + "       , lab77.lab77c4 AS ABName " //Nombre del antibiograma
                        + "       , lab204.lab204c2 AS Comment " //Comentario
                        + "       , lab205.lab205c1 AS CMI " //Resultado del antibiotico
                        + "       , lab204.lab76c1 AS MicID "; //Id del microorganismo

                String from = ""
                        + " FROM  " + lab57 + " AS lab57 "
                        + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                        + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                        + " INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                        + " LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 "
                        + " LEFT JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 "
                        + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                        + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                        + " LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1 "
                        + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 "
                        + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 "
                        + " LEFT JOIN " + lab95 + " AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                        + " LEFT JOIN " + lab22 + " AS lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                        + " LEFT JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                        + " LEFT JOIN Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                        + " LEFT JOIN Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                        + " LEFT JOIN lab10 ON lab22.lab10c1 = lab10.lab10c1 "
                        + " LEFT JOIN lab61 testH ON lab57.lab39c1 = testH.lab39c1 AND testH.lab118c1 = " + filter.getIdSystemTest()
                        + " LEFT JOIN lab61 testP ON lab57.lab57c14 = testP.lab39c1 AND testH.lab118c1 = " + filter.getIdSystemTest()
                        + " LEFT JOIN lab191 ON lab191.lab22c1 = lab57.lab22c1 AND lab191.lab39c1 = lab39.lab39c1 and lab191.lab118c1 = " + filter.getIdSystemTest()
                        + " LEFT JOIN lab204 ON lab204.lab22c1 = lab57.lab22c1 AND lab204.lab39c1 = lab57.lab39c1 "
                        + " LEFT JOIN lab205 ON lab204.lab204c1 = lab205.lab204c1  "
                        + " LEFT JOIN lab76 ON lab76.Lab76c1 = lab204.lab76c1 "
                        + " LEFT JOIN lab77 ON lab77.lab77c1 = lab204.lab77c1 "
                        + " LEFT JOIN lab79 ON lab79.lab79c1 = lab205.lab79c1 ";

                String where = " WHERE lab39.lab39c37 = 0 ";

                switch (filter.getForwarding())
                {
                    case 0:
                        where = where + " AND ( lab191c2 IS NULL OR lab191c2 = 0 OR lab191c2 = 2 ) ";
                        break;
                    case 1:
                        where = where + " AND lab191c2 = 1 ";
                        break;
                }

                List<Object> params = new ArrayList<>();

                where = where + " AND lab57.lab22c1 = ? ";
                params.add(filter.getOrderLis());

                if (filter.getIdTest() != null && filter.getIdTest() != "")
                {
                    String[] items = filter.getIdTest().split(",");
                    if (items.length > 0)
                    {
                        where = where + " AND lab57.lab39c1 IN( " + filter.getIdTest() + " ) ";
                    }
                }

                where = where + " AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ";

                RowMapper mapper = (RowMapper<TestDetail>) (ResultSet rs, int index) ->
                {
                    TestDetail bean = new TestDetail();
                    DateFormat simple = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    bean.setOrder(rs.getLong("lab22c1"));
                    bean.setProfileId(rs.getInt("lab39c1p"));
                    bean.setProfileName(rs.getString("lab39c4p") == null ? "" : rs.getString("lab39c4p"));
                    bean.setTestId(rs.getInt("lab39c1"));
                    bean.setTestCode(rs.getString("lab39c2") == null ? "" : rs.getString("lab39c2"));
                    bean.setTestName(rs.getString("lab39c4") == null ? "" : rs.getString("lab39c4"));
                    bean.setTestType(rs.getInt("lab39c37"));
                    bean.setFormula(rs.getString("lab39c10") == null ? "" : rs.getString("lab39c10"));

                    bean.setHomologationCode(rs.getString("testHomologation") == null ? "" : rs.getString("testHomologation"));
                    bean.setHomologationProfile(rs.getString("profileHomologation") == null ? "" : rs.getString("profileHomologation"));

                    bean.setSampleId(rs.getInt("ltlab24c1"));
                    bean.setSampleCode(rs.getString("ltlab24c9") == null ? "" : rs.getString("ltlab24c9"));
                    bean.setSampleName(rs.getString("ltlab24c2") == null ? "" : rs.getString("ltlab24c2"));

                    if (rs.getTimestamp("lab57c4") != null)
                    {
                        Date entryDate = new Date(rs.getTimestamp("lab57c4").getTime());
                        bean.setEntryDate(simple.format(entryDate));
                    }

                    bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                    bean.setReportTask(isReportedTasks(bean.getOrder(), bean.getTestId()));
                    bean.setPreliminaryValidation(rs.getInt("lab39c50") == 1);
                    bean.setTechnique(rs.getString("lab64c3") == null ? "" : rs.getString("lab64c3"));

                    if (rs.getTimestamp("lab57c37") != null)
                    {
                        Date verificationDate = new Date(rs.getTimestamp("lab57c37").getTime());
                        bean.setVerificationDate(simple.format(verificationDate));
                    }

                    bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                    //TODO: Desencriptar el resultado
                    bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                    bean.setPreviousResult(Tools.decrypt(rs.getString("lab57c62")));

                    if (rs.getTimestamp("lab57c2") != null)
                    {
                        Date resultDate = new Date(rs.getTimestamp("lab57c2").getTime());
                        bean.setResultDate(simple.format(resultDate));
                    }

                    bean.setResultUserId(rs.getInt("lab57c3"));

                    if (rs.getTimestamp("lab57c20") != null)
                    {
                        Date preliminaryDate = new Date(rs.getTimestamp("lab57c20").getTime());
                        bean.setPreliminaryDate(simple.format(preliminaryDate));
                    }

                    bean.setPreliminaryUser(rs.getInt("lab57c21"));

                    bean.setRepeatedResultValue(Tools.decrypt(rs.getString("lab57c48")));

                    if (rs.getTimestamp("lab57c18") != null)
                    {
                        Date validationDate = new Date(rs.getTimestamp("lab57c18").getTime());
                        bean.setValidationDate(simple.format(validationDate));
                    }

                    bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                    if (rs.getTimestamp("lab57c22") != null)
                    {
                        Date printDate = new Date(rs.getTimestamp("lab57c22").getTime());
                        bean.setPrintDate(simple.format(printDate));
                    }

                    bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                    if (rs.getTimestamp("lab57c39") != null)
                    {
                        Date takenDate = new Date(rs.getTimestamp("lab57c39").getTime());
                        bean.setTakenDate(simple.format(takenDate));
                    }

                    bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                    bean.setEntryType(rs.getShort("lab57c35"));
                    bean.setState(rs.getInt("lab57c8"));
                    bean.setSampleState(rs.getInt("lab57c16"));
                    bean.setAreaId(rs.getInt("lab43c1"));
                    bean.setAreaCode(rs.getString("lab43c2") == null ? "" : rs.getString("lab43c2"));
                    bean.setAreaAbbr(rs.getString("lab43c3") == null ? "" : rs.getString("lab43c3"));
                    bean.setAreaName(rs.getString("lab43c4") == null ? "" : rs.getString("lab43c4"));
                    bean.setAreaPartialValidation(rs.getInt("lab43c8") == 1);
                    bean.setResultType(rs.getShort("lab39c11"));
                    bean.setPathology(rs.getInt("lab57c9"));
                    bean.setRefLiteral(rs.getString("lab50c2n") == null ? "" : rs.getString("lab50c2n"));
                    bean.setPanicLiteral(rs.getString("lab50c2p") == null ? "" : rs.getString("lab50c2p"));
                    bean.setConfidential(rs.getInt("lab39c27") == 1);
                    bean.setRepeatAmmount(rs.getInt("lab57c33"));
                    bean.setModificationAmmount(rs.getInt("lab57c24"));
                    bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                    bean.setHasTemplate(rs.getInt("lab57c42") == 1);
                    bean.setDilution(rs.getBoolean("lab57c53"));

                    BigDecimal refMin = rs.getBigDecimal("lab48c12");
                    BigDecimal refMax = rs.getBigDecimal("lab48c13");

                    bean.setRefMinT(rs.getString("lab48c12") == null ? "" : rs.getString("lab48c12"));
                    bean.setRefMaxT(rs.getString("lab48c13") == null ? "" : rs.getString("lab48c13"));

                    if (!rs.wasNull())
                    {
                        bean.setRefMin(refMin);
                        bean.setRefMax(refMax);

                        String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                        String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                        bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                    } else
                    {
                        bean.setRefMin(BigDecimal.ZERO);
                        bean.setRefMax(BigDecimal.ZERO);
                        bean.setRefInterval(bean.getRefLiteral());
                    }

                    BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                    BigDecimal panicMax = rs.getBigDecimal("lab48c6");

                    bean.setPanicMinT(rs.getString("lab48c5") == null ? "" : rs.getString("lab48c5"));
                    bean.setPanicMaxT(rs.getString("lab48c6") == null ? "" : rs.getString("lab48c6"));

                    if (!rs.wasNull())
                    {
                        bean.setPanicMin(panicMin);
                        bean.setPanicMax(panicMax);
                        String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                        String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                        bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                    } else
                    {
                        bean.setPanicMin(BigDecimal.ZERO);
                        bean.setPanicMax(BigDecimal.ZERO);
                        bean.setPanicInterval(bean.getPanicLiteral());
                    }

                    BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                    BigDecimal reportedMax = rs.getBigDecimal("lab48c15");

                    bean.setReportedMinT(rs.getString("lab48c14") == null ? "" : rs.getString("lab48c14"));
                    bean.setReportedMaxT(rs.getString("lab48c15") == null ? "" : rs.getString("lab48c15"));
                    if (!rs.wasNull())
                    {
                        bean.setReportedMin(reportedMin);
                        bean.setReportedMax(reportedMax);

                        String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                        String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                        bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                    } else
                    {
                        bean.setReportedMin(BigDecimal.ZERO);
                        bean.setReportedMax(BigDecimal.ZERO);
                    }

                    BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                    BigDecimal deltaMax = rs.getBigDecimal("lab57c28");

                    bean.setDeltaMinT(rs.getString("lab57c27") == null ? "" : rs.getString("lab57c27"));
                    bean.setDeltaMaxT(rs.getString("lab57c28") == null ? "" : rs.getString("lab57c28"));

                    if (!rs.wasNull())
                    {
                        bean.setDeltaMin(deltaMin);
                        bean.setDeltaMax(deltaMax);
                        bean.setDeltaInterval(deltaMin + " - " + deltaMax);
                    } else
                    {
                        bean.setDeltaMin(BigDecimal.ZERO);
                        bean.setDeltaMax(BigDecimal.ZERO);
                        bean.setDeltaInterval("");
                    }

                    bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));

                    if (rs.getTimestamp("lab57c7") != null)
                    {
                        Date lastResultDate = new Date(rs.getTimestamp("lab57c7").getTime());
                        bean.setLastResultDate(simple.format(lastResultDate));
                    }

                    bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));

                    if (rs.getTimestamp("lab57c31") != null)
                    {
                        Date secondLastResultDate = new Date(rs.getTimestamp("lab57c31").getTime());
                        bean.setSecondLastResultDate(simple.format(secondLastResultDate));
                    }

                    bean.setCritic(rs.getShort("lab48c16"));
                    bean.setUnitId(rs.getInt("lab45c1"));
                    bean.setUnit(rs.getString("lab45c2") == null ? "" : rs.getString("lab45c2"));
                    bean.setUnitInternational(rs.getString("lab45c3") == null ? "" : rs.getString("lab45c3"));
                    bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                    bean.setAbbreviation(rs.getString("lab39c3") == null ? "" : rs.getString("lab39c3"));
                    bean.setDigits(rs.getShort("lab39c12"));

                    //Usuario que valido
                    if (bean.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                    {
                        bean.setValidationUserName(rs.getString("lab04c2") == null ? "" : rs.getString("lab04c2"));
                        bean.setValidationUserLastName(rs.getString("lab04c3") == null ? "" : rs.getString("lab04c3"));
                    }

                    ResultTestComment comment = new ResultTestComment();
                    comment.setOrder(rs.getLong("lab22c1"));
                    comment.setTestId(rs.getInt("lab39c1"));
                    comment.setComment(rs.getString("lab95c1") == null ? "" : rs.getString("lab95c1"));
                    comment.setCommentDate(rs.getDate("lab95c2"));
                    comment.setPathology(rs.getShort("lab95c3"));
                    comment.setUserId(rs.getInt("lab04c1"));
                    comment.setCommentChanged(false);
                    bean.setResultComment(comment);

                    bean.setLaboratoryType(rs.getString("lab24c10") == null ? "" : rs.getString("lab24c10"));
                    bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                    bean.setDelta(rs.getShort("lab57c43") == 1);
                    bean.setLaboratoryId(rs.getInt("lab40c1"));

                    bean.setMicroorganism(rs.getString("Mic") == null ? "" : rs.getString("Mic"));
                    bean.setAntibiotic(rs.getString("Ant") == null ? "" : rs.getString("Ant"));
                    bean.setInterpretation(rs.getString("Result") == null ? "" : rs.getString("Result"));
                    bean.setAntibiogram(rs.getString("ABName") == null ? "" : rs.getString("ABName"));
                    bean.setMicrobialDetectionComment(rs.getString("Comment") == null ? "" : rs.getString("Comment"));
                    bean.setCmi(rs.getString("CMI") == null ? "" : rs.getString("CMI"));
                    bean.setMicroorganismId(rs.getInt("MicID"));

                    listResults.add(bean);
                    return bean;
                };
                getJdbcTemplate().query(select + from + where, mapper, params.toArray());
            }
            return listResults;
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    default DetailStatus status(final DetailStatus status) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        
        if (checkStatusResult(status.getOrderId(), status.getTestId(), status.getIdSystemTest()))
        {
            String update = ""
                    + "UPDATE  lab191 "
                    + " SET    lab191c2 = ?,"
                    + "       lab191c3 = ? "; 
            String where = ""
                    + " WHERE "
                    + " lab39c1 = ? "
                    + " AND lab22c1 = ? AND lab118c1 = ? ";
            getJdbcTemplate().update(update + where, new Object[]
            {
                status.getStatus(), timestamp, status.getTestId(), status.getOrderId(), status.getIdSystemTest()
            });
        } else {
            try
            {

                SimpleJdbcInsert insertJdbc = new SimpleJdbcInsert(getJdbcTemplate());
                insertJdbc.withTableName("lab191");
                HashMap parameters = new HashMap();
                parameters.put("lab118c1", status.getIdSystemTest());
                parameters.put("lab39c1", status.getTestId());
                parameters.put("lab191c1", 0);
                parameters.put("lab22c1", status.getOrderId());
                parameters.put("lab191c2", status.getStatus());
                parameters.put("lab191c3", timestamp);
                boolean result = insertJdbc.execute(parameters) >= 1;
                if (!result)
                {
                    IntegrationHisLog.info("ORDER : " + status.getOrderId() + " TEST : " + status.getTestId() + " NOT INSERTED");
                }
            } catch (EmptyResultDataAccessException ex)
            {
                IntegrationHisLog.info("ORDER : " + status.getOrderId() + " TEST : " + status.getTestId() + " NOT INSERTED");
                ex.getMessage();
            }
        }
        return status;
    }
    
    
    
    /**
     * Verifica si el envio ya existe
     *
     * @param orderNumber
     * @param idTest
     * @para idCentralSystem
     *
     * @return boolean
     * @throws Exception Error en la base de datos.
     */
    default boolean checkStatusResult(long orderNumber, int idTest, int idCentralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append(" SELECT lab191c2 FROM lab191")
                    .append(" WHERE lab22c1 = ? ")
                    .append(" AND lab39c1 = ? ")
                    .append(" AND lab118c1 = ? ");
                                
            return getJdbcTemplate().queryForObject(query.toString(), new Object[]
            {
                orderNumber, idTest, idCentralSystem
            }, (ResultSet rs, int i) ->
            {
                return true;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return false;
        }
    }

}
