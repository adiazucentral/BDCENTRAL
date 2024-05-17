package net.cltech.outreach.dao.interfaces.operation;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.domain.operation.OrderSearch;
import net.cltech.outreach.domain.operation.ResultTest;
import net.cltech.outreach.domain.operation.ResultTestComment;
import net.cltech.outreach.domain.operation.ResultTestHistory;
import net.cltech.outreach.tools.DateTools;
import net.cltech.outreach.tools.Tools;
import net.cltech.outreach.tools.enums.LISEnum;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Interfaz de acceso a datos para ordenes y pacientes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2018
 * @see Creación
 */
public interface OrderDao
{

    /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Busca ordenes en el sistema por los filtros enviados
     *
     * @param filter Filtros.
     * @param showResult Mostrar resultados: 1 -> Ingresados, 2 -> Verificados.
     * @param user Usuario que realizo la consulta.
     * @param yearsQuery Años de consulta
     * @param orderAuxPhysician Lista de ordenes del medico auxiliar
     * @param isAuxPhysicians Indica si se utilizan medicos auxiliares
     * @param queryDemo Demografico consulta web
     * @param idItemDemo Id item demografico consulta web
     * @return Lista de Ordenes.
     * @throws Exception Error en base de datos.
     */
    public List<OrderSearch> listOrders(Filter filter, int showResult, AuthorizedUser user, int yearsQuery, List<Long> orderAuxPhysician, boolean isAuxPhysicians, Demographic queryDemo, QueryDemographic idItemDemo) throws Exception;

    default Demographic getDemographic(String value) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT   lab63c1 ,"
                    + "         lab63c2 ,"
                    + "         lab63c3  "
                    + "FROM     lab63 "
                    + "WHERE    lab63c1 = ?",
                    new Object[]
                    {
                        value
                    }, (ResultSet rs, int i) ->

            {

                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab63c1"));
                demographic.setCode(rs.getString("lab63c2"));
                demographic.setName(rs.getString("lab63c3"));

                return demographic;

            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

   /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados por
     * orden.
     *
     * @param idOrder Numero de orden
     *
     * @return Lista de {@link net.cltech.outreach.domain.operation.ResultTest}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> listResults(long idOrder, int area) throws Exception
    {
        try
        {
             // Consulta de ordenes por historico:
            Integer year = Tools.listOfConsecutiveYears(String.valueOf(idOrder), String.valueOf(idOrder)).get(0);
           
            String lab57;
            String lab95;
            int currentYear = DateTools.dateToNumberYear(new Date());
            lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
            
            
            String select = ""
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
                    + "       , lab39.lab39c2 " //código
                    + "       , lab39.lab39c3 " //abreviatura
                    + "       , lab39.lab39c4 " //nombre
                    + "       , lab39.lab39c37 " //nombre
                    + "       , lab57c1 " //resultado
                    + "       , lab57c2 " //fecha resultado
                    + "       , lab57c3 " //usuario del resultado
                    + "       , lab57c5 " //usuario de ingreso
                    + "       , lab57c8 " //estado
                    + "       , lab57c16 " //estado muestra
                    + "       , lab39.lab39c11 " //tipo resultado
                    + "       , lab57c9 " //patología
                    + "       , lab57.lab48c12 " //referecnia mínima
                    + "       , lab57.lab48c13 " //referec2nia máxima
                    + "       , lab57.lab48c5 " //panico minimo
                    + "       , lab57.lab48c6 " //panico máximo
                    + "       , lab57.lab48c14 " //reportable minimo
                    + "       , lab57.lab48c15 " //reportable máximo
                    + "       , lab57.lab57c22 " //
                    + "       , lab57.lab57c23 " //
                    + "       , lab57.lab57c10 " //
                    + "       , lab48c16 " //pánico crítico
                    + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                    + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                    + "       , lab39.lab39c3 " //abreviatura
                    + "       , lab39.lab39c12 " //decimales
                    + "       , lab39.lab39c27 " //confidencial
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
                    + "       , lab57.lab57c19" //fecha validacion
                    + "       , lab57.lab57c4" //fecha ingreso
                    + "       , lab57.lab57c14"
                    + "       , lt.lab24c10" //tipo laboratorio
                    + "       , lab57c35" //tipo ingreso resultado
                    + "       , lab57c36" //tipo ingreso microbiología
                    + "       , lab57c37" //Fecha verificación
                    + "       , lab57c38" //Id usuario verifica
                    + "       , lab57c39" //Fecha Toma
                    + "       , lab57c40" //Usuario toma
                    + "       , lab57c26" //Tiene Antibiograma.
                    + "       , lab39P.lab39c1 AS lab39c1p " //Id perfil
                    + "       , lab39P.lab39c4 AS lab39c4p " //Nombre perfil
                    
                    + "";

            String from = ""
                    + " FROM  "+lab57+" as  lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 "
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 "
                    + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 "
                    + " LEFT JOIN " + lab95 + " as lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab39.lab39c37 = 0 AND lab57.lab22c1 = ?";
            
            List<Object> params = new ArrayList<>();
            params.add(idOrder);
            
            if(area > 0){
                where = where + " AND lab39.lab43c1 = ?";
                params.add(area);
            }

            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setTestType(rs.getInt("lab39c37"));
                bean.setEntry(rs.getString("lab57c14") == null);
                bean.setSampleId(rs.getInt("ltlab24c1"));
                bean.setSampleCode(rs.getString("ltlab24c9"));
                bean.setSampleName(rs.getString("ltlab24c2"));
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));
                bean.setBlockTest(rs.getInt("lab57c10"));

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

                Float refMin = rs.getFloat("lab48c12");
                Float refMax = rs.getFloat("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);
                    bean.setRefInterval(refMin.toString() + " - " + refMax.toString());
                } else
                {
                    bean.setRefMin(0F);
                    bean.setRefMax(0F);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                Float panicMin = rs.getFloat("lab48c5");
                Float panicMax = rs.getFloat("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);
                    bean.setPanicInterval(panicMin.toString() + " - " + panicMax.toString());
                } else
                {
                    bean.setPanicMin(0F);
                    bean.setPanicMax(0F);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                Float reportedMin = rs.getFloat("lab48c14");
                Float reportedMax = rs.getFloat("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    bean.setReportedInterval(reportedMin.toString() + " - " + reportedMax.toString());
                } else
                {
                    bean.setReportedMin(0F);
                    bean.setReportedMax(0F);
                }

                Float deltaMin = rs.getFloat("lab57c27");
                Float deltaMax = rs.getFloat("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(0F);
                    bean.setDeltaMax(0F);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(rs.getString("lab57c6"));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnitId(rs.getInt("lab45c1"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setUnitInternational(rs.getString("lab45c3"));
                bean.setUnitConversionFactor(rs.getFloat("lab45c4"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                bean.setLaboratoryType(rs.getString("lab24c10"));
                bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));
                bean.setProfileId(rs.getInt("lab39c1p"));
                bean.setProfileName(rs.getString("lab39c4p"));
                bean.setHasAntibiogram(rs.getInt("lab57c26") == 1);
                
                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);
                
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
     * Obtiene la lista de exámenes para el procesamiento de los resultados por
     * orden.
     *
     * @param filter Filtros de busqueda 
     * @param user Medico Auxiliar
     * @param yearsQuery Años de consulta
     * 
     * @return Lista de {@link net.cltech.outreach.domain.operation.ResultTest}
     * @throws Exception Error en la base de datos.
     */
    default List<Long> ordersByAuxPhysician(Filter filter, AuthorizedUser user, int yearsQuery) throws Exception
    {
        try
        {
            String lab22;
            
            int currentYear = DateTools.dateToNumberYear(new Date());
            
            List<Integer> years = new ArrayList<>();
            
            if(filter.getDateNumber() != null || filter.getYear() != null || filter.getOrder() != null) {
                
                if(filter.getDateNumber() != null && (filter.getYear() == null || filter.getYear() == 0) && ( filter.getOrder() == null || filter.getOrder() == 0 ) ) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getDateNumber()), String.valueOf(filter.getDateNumber()));
                }

                if(filter.getYear() != null && (filter.getDateNumber() == null || filter.getDateNumber() == 0) && ( filter.getOrder() == null || filter.getOrder() == 0 )) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getYear()), String.valueOf(filter.getYear()));
                }

                if(filter.getOrder() != null && (filter.getDateNumber() == null || filter.getDateNumber() == 0) && (filter.getYear() == null || filter.getYear() == 0) ) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getOrder()), String.valueOf(filter.getOrder()));
                }

            } else {
                // Consulta de ordenes por historico:
                years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
            }
            
            List<Long> listOrders = new LinkedList<>();
            
            for (Integer year : years) {
                
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                
                String query = ""
                + " SELECT   lab194.lab22c1 "
                + " FROM     lab194 "
                + " INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab194.lab22c1 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ";

                String where = "WHERE lab22.lab07c1 = 1 AND lab194.lab19c1 = ? "
                        + (filter.getOrder() != null ? " AND lab194.lab22c1 = ? " : "")
                        + (filter.getDateNumber() != null ? " AND lab22.lab22c2 = ? " : "")
                        + (filter.getDocumentType() != null ? " AND lab21.lab54C1 = ? " : "")
                        + (filter.getPatientId() != null ? " AND lab21.lab21c2 = ? " : "")
                        + (filter.getLastName() != null ? " AND lab21.lab21c5 = ? " : "")
                        + (filter.getSurName() != null ? " AND lab21.lab21c6 = ? " : "")
                        + (filter.getName1() != null ? " AND lab21.lab21c3 = ? " : "")
                        + (filter.getName2() != null ? " AND lab21.lab21c4 = ? " : "")
                        + (filter.getYear() != null ? " AND to_char(lab22c3, 'yyyy') = '" + filter.getYear() + "' " : "");

                List parameters = new ArrayList(0);

                parameters.add(user.getId());

                if (filter.getOrder() != null)
                {
                    parameters.add(filter.getOrder());
                }
                if (filter.getDateNumber() != null)
                {
                    parameters.add(filter.getDateNumber());
                }
                if (filter.getDocumentType() != null)
                {
                    parameters.add(filter.getDocumentType());
                }
                if (filter.getPatientId() != null)
                {
                    parameters.add(filter.getPatientId());
                }
                if (filter.getLastName() != null)
                {
                    parameters.add(filter.getLastName());
                }
                if (filter.getSurName() != null)
                {
                    parameters.add(filter.getSurName());
                }
                if (filter.getName1() != null)
                {
                    parameters.add(filter.getName1());
                }
                if (filter.getName2() != null)
                {
                    parameters.add(filter.getName2());
                }

                getJdbcTemplate().query(query + where, (ResultSet rs, int rowNum) ->
                {
                    listOrders.add(rs.getLong("lab22c1"));
                    return rs.getLong("lab22c1");
                }, parameters.toArray());
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList(0);
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
            String lab95;
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<ResultTestHistory> listOrders = new LinkedList<>();
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;

                String query = ""
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
                        + "lab39c11 "
                        + ", lab95c1 " //comentario
                        + ", lab95c2 " //fecha comentario
                        + ", lab95c3 " //patología comentario
                        + ", lab95.lab04c1 "; //usuario comentario
                String from = " "
                        + "FROM " + lab57 + " as lab57 "
                        + "INNER JOIN   " + lab22 + " as lab22 ON lab22.lab22c1  = lab57.lab22c1 "
                        + "INNER JOIN lab39 ON lab39.lab39c1  = lab57.lab39c1 "
                        + "LEFT JOIN lab50  ON lab50.lab50c1  = lab57.lab50c1_3 "
                        + " LEFT JOIN " + lab95 + " as lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                        + "WHERE lab22.lab21c1 = ? AND lab57.lab39c1 = ? AND lab57.lab57c8 >= ? "
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
                    }

                    bean.setRefLiteral(rs.getString("lab50c2"));
                    bean.setResultType(rs.getShort("lab39c11"));
                    
                    ResultTestComment comment = new ResultTestComment();
                    comment.setOrder(rs.getLong("lab22c1"));
                    comment.setTestId(rs.getInt("lab39c1"));
                    comment.setComment(rs.getString("lab95c1"));
                    comment.setCommentDate(rs.getDate("lab95c2"));
                    comment.setPathology(rs.getShort("lab95c3"));
                    comment.setUserId(rs.getInt("lab04c1"));
                    comment.setCommentChanged(false);
                    bean.setResultComment(comment);
                    
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
}
