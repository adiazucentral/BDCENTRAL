package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.control.PatientResult;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultOrder;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestPathology;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Interfaz de acceso a datos para las órdenes en el módulo de registro de
 * resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */


public interface ResultOrderDao
{

    public ConfigurationDao getConfigDao();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

     /**
     * Obtiene la lista de órdenes para el procesamiento de los resultados.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * órdenes aplicado por el usuario
     * @param orderByOrderId Indicador para ordenar la lista por número de
     * órden.
     * @param laboratorys
     * @param branch
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default List<ResultOrder> list(final ResultFilter resultFilter, boolean orderByOrderId, List<Integer> laboratorys, int branch) throws Exception
    {
        try
        {
            List<ResultOrder> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = null;
            if(resultFilter.getFirstOrder() == -1 && resultFilter.getFirstDate() == -1)
            {
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                // Consulta de ordenes por historico:
                years = Tools.listOfConsecutiveYears(String.valueOf(y - 3), String.valueOf(y));
            }
            else {
                Long vInitial = resultFilter.getFirstOrder() == -1 ? Long.parseLong(String.valueOf(resultFilter.getFirstDate())) : resultFilter.getFirstOrder();
                Long vFinal = resultFilter.getLastOrder() == 1 ? Long.parseLong(String.valueOf(resultFilter.getLastDate())) : resultFilter.getLastOrder();
                years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            }
           
            
             
            
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    String select = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   lab22.lab22c1 "
                            + "       , lab22.lab103c1 " //id del tipo de orden
                            + "       , lab22.lab21c1 "
                            + "       , lab54c1 " //tipo documento
                            + "       , lab21c2 "
                            + "       , lab21c3 "
                            + "       , lab21c4 "
                            + "       , lab21c5 "
                            + "       , lab21c6 "
                            + "       , lab21c7 " //fecha nacimiento
                            + "       , lab22.lab10c1 " //id del servicio
                            + "       , lab22.lab05c1 " //id del servicio
                            + "       , lab22c10 "
                            + "       , lab22c12 " //inconsistencia
                            + "       , lab21.lab80c1 "
                            + "       , lab22c3 " //fecha de creación
                            + "       , lab39.lab39c1 " //Id Prueba
                            + "       , lab39c2 " //Codigo Prueba
                            + "       , lab39c4 " //Nombre Prueba
                            + "       , lab39c11 " //Tipo de Resultado
                            + "       , lab39c50 " // Requiere prevalidación preeliminar
                            + "       , lab57c1 " //Resultado
                            + "       , lab57c8 " //Estado prueba
                            + "       , lab57c16 " //Estado de la muestra
                            + "       , lab57.lab57c9 " //patologia
                            + "       , lab57c37 " //Fecha verificación
                            + "       , lab57c18 " //Fecha Resultado Validado
                            + "       , lab57c25 " //Imprime
                            + "       , lab57c41 ";
                        
                    //Tiempo Esperado de oportunidad
                    
                    
                    //--------Filtro por sección (BANNER: Se sugiere tener la sección en lab57 para evitar join a lab39)
                    if (resultFilter.getAreaList().size() > 0)
                    {
                        if (resultFilter.getAreaList().size() == 1)
                        {
                            select = select + (" , (SELECT MIN(lab57c8) FROM " + lab57 + " AS lab57 INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab39c37 = 0 AND lab39.lab43c1 = " + resultFilter.getAreaList().get(0) + ") AS orderState ");
                            
                        } else
                        {
                            select = select + (" , (SELECT MIN(lab57c8) FROM " + lab57 + " AS lab57 INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab39c37 = 0 AND lab39.lab43c1 in ("+ resultFilter.getAreaList().stream().map(area -> area.toString()).collect(Collectors.joining(",")) +" )) AS orderState ");
                        }
                    }
                    else
                    {
                        select = select + (" , (SELECT MIN(lab57c8) FROM " + lab57 + " AS lab57 INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab39c37 = 0) AS orderState ");
                    }
                            
                           

                    String from = ""
                            + " FROM " + lab57 + " AS lab57 "
                            + " INNER JOIN " + lab22 + " AS lab22  ON lab22.lab22c1 = lab57.lab22c1"
                            + " INNER JOIN lab39  ON lab39.lab39c1 = lab57.lab39c1"
                            + " INNER JOIN lab21  ON lab21.lab21c1 = lab22.lab21c1"
                            + " INNER JOIN lab93 ON lab93.lab05c1 = lab22.lab05c1"
                            + "";

                    List<Object> parametersList = new ArrayList<>();
                    StringBuilder where = new StringBuilder("");
                    where.append(" WHERE lab22.lab07C1 = 1  AND (lab22c19 = 0 or lab22c19 is null) and lab39c37 = 0");
                    where.append(" AND ((lab57.lab57c8 = 2 AND lab57.lab57c16 = 1) OR (lab57.lab57c8 = 0 AND lab57.lab57c16 = 1) OR (lab57.lab57c16 >= ").append(LISEnum.ResultSampleState.CHECKED.getValue()).append(")) ");

                    
                    //TODO: Identificar las muestras verificadas
                    //+ " WHERE lab159.lab07C1 = ? ";
                    if (resultFilter != null)
                    {
                        //--------Filtro por número de orden o fecha de verificación
                        if (resultFilter.getFirstOrder() > 0)
                        {
                            //where.append(" AND lab22.lab22c1 BETWEEN ? AND ? ");
                            where.append("  AND lab22.lab22c1 > ? AND lab22.lab22c1 < ? and lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue()).append(" ");
                            parametersList.add(resultFilter.getFirstOrder());
                            parametersList.add(resultFilter.getLastOrder());
                        } else if(resultFilter.getFirstDate() > 0)
                        {
                            //where.append(" AND lab57c34 BETWEEN ? AND ? ");
                            where.append(" AND lab57c34 > ? AND lab57c34 < ? ");
                            parametersList.add(resultFilter.getFirstDate());
                            parametersList.add(resultFilter.getLastDate());
                        }

                        //--------Filtro por tipo de orden
                        if (resultFilter.getOrderType() > 0)
                        {
                            where.append(" AND lab22.lab103c1 = ? ");
                            parametersList.add(resultFilter.getOrderType());
                        }

                        //--------Filtro por sección (BANNER: Se sugiere tener la sección en lab57 para evitar join a lab39)
                        if (resultFilter.getAreaList().size() > 0)
                        {
                            if (resultFilter.getAreaList().size() == 1)
                            {
                                where.append(" AND lab39.lab43c1 = ? ");
                                parametersList.add(resultFilter.getAreaList().get(0));
                            } else
                            {
                                where.append(" AND lab39.lab43c1 in (").append(resultFilter.getAreaList().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                            }
                        }

                        //--------Filtro por prueba y prueba confidencial. Incluye perfiles.
                        if (resultFilter.getTestList().size() > 0)
                        {
                            if (resultFilter.getTestList().size() == 1)
                            {
                                where.append(" AND (lab57.lab39c1 = ? OR lab57.lab57c14 = ?) ");
                                parametersList.add(resultFilter.getTestList().get(0));
                                parametersList.add(resultFilter.getTestList().get(0));
                            } else
                            {
                                where.append(" AND (lab57.lab39c1 in (").append(resultFilter.getTestList().stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                                where.append(" OR lab57.lab57c14 in (").append(resultFilter.getTestList().stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")) ");
                            }
                        }
                        //Filtro para examenes confidenciales segun permisos del usuario
                        if (!resultFilter.getUser().isConfidential())
                        {
                            where.append(" AND lab39.lab39c27 = 0 ");
                        }

                        //--------Filtro de resultados
                        if (resultFilter.getIntResultFilter() > 0)
                        {
                            where.append(" AND lab57c8 = ? ");
                            parametersList.add(resultFilter.getIntResultFilter());
                        }

                        //--------Filtro de pánicos (RESULTADOS SIN VALIDAR)
                        if (resultFilter.isPanicFilter())
                        {
                            where.append(" AND lab57.lab57c9 > ? ");
                            parametersList.add(ResultTestPathology.HIGH_REFERENCE.getValue());
                        }

                        //--------Filtro de pánicos críticos (RESULTADOS SIN VALIDAR)
                        if (resultFilter.isCriticFilter())
                        {
                            where.append(" AND lab57.lab57c9 > ? ");
                            parametersList.add(ResultTestPathology.HIGH_PANIC.getValue());
                        }

                        //--------Filtro de urgencias (RESULTADOS SIN VALIDAR)
                        if (resultFilter.isStatFilter())
                        {
                            where.append(" AND lab22.lab103c1 = ? ");
                            parametersList.add(2);
                        }

                        //--------Filtro de adjuntos (RESULTADOS SIN VALIDAR)
                        if (resultFilter.isAttachFilter())
                        {
                            where.append(" AND (lab22c12 > 0 OR lab57c41 > 0)");
                        }

                        //--------Filtro por historia
                        if (resultFilter.getPatientId() != null && !resultFilter.getPatientId().isEmpty())
                        {
                            if (getConfigDao().get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true") && resultFilter.getDocumentType() != 0)
                            {
                                where.append(" AND lab21.lab54c1 = ?");
                                parametersList.add(resultFilter.getDocumentType());
                            }
                            where.append(" AND lab21.lab21c2 = ?");
                            parametersList.add(Tools.encrypt(resultFilter.getPatientId()));
                        }

                        //--------Filtro por demograficos
                        if (resultFilter.getFilterByDemo() != null)
                        {
                            resultFilter.getFilterByDemo().forEach((demographic) ->
                            {
                                where.append(SQLTools.buildSQLDemographicFilter(demographic, parametersList));
                            });
                        }
                        
                        where.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));
                        

                        //--------Filtro de oportunidad (RESULTADOS SIN VALIDAR)
                        if (resultFilter.isTimeFilter())
                        {
                            where.append(" AND lab171c3 = 0 AND lab57c8 < 4");
                        }

                        //--------Filtro por las sedes aturoizadas para el usuario
                        where.append(" AND lab93.lab93c1 = 1 AND lab93.lab04c1 = ?");
                        parametersList.add(resultFilter.getUser().getId());
                        where.append(" ORDER BY lab22.lab22c1");
                       

                    } else
                    {
                        return new ArrayList<>(0);
                    }

                    Object[] parametersArr = new Object[parametersList.size()];
                    parametersArr = parametersList.toArray(parametersArr);

                    RowMapper mapper = (RowMapper<ResultOrder>) (ResultSet rs, int i) ->
                    {
                        ResultOrder bean = new ResultOrder();
                        bean.setOrder(rs.getLong("lab22c1"));
                        bean.setIdOrderType(rs.getInt("lab103c1"));
                    
                        bean.setAttachmentOrder(rs.getInt("lab22c12"));
                        bean.setPatientId(rs.getInt("lab21c1"));
                        bean.setIDType(rs.getString("lab54c1"));
                        bean.setPatientCode(Tools.decrypt(rs.getString("lab21c2")));
                        bean.setPatientName1(Tools.decrypt(rs.getString("lab21c3")));
                        bean.setPatientName2(Tools.decrypt(rs.getString("lab21c4")));
                        bean.setPatientLastName1(Tools.decrypt(rs.getString("lab21c5")));
                        bean.setPatientLastName2(Tools.decrypt(rs.getString("lab21c6")));
                        bean.setPathology(rs.getInt("lab57c9"));
                        bean.setBranch(rs.getString("lab05c1"));
                        bean.setIdService(rs.getInt("lab10c1"));
                        bean.setBirthday(rs.getTimestamp("lab21c7"));
                        bean.setCreateDate(rs.getTimestamp("lab22c3"));
                        bean.setOrderState(rs.getInt("orderState"));
                        /*Resultados*/
                        bean.setTest(rs.getInt("lab39c1"));
                        bean.setTestCode(rs.getString("lab39c2"));
                        bean.setTestName(rs.getString("lab39c4"));
                        bean.setTestResultType(rs.getShort("lab39c11"));
                        bean.setPreliminaryTestValidation(rs.getInt("lab39c50") == 1);
                        bean.setTestResult(Tools.decrypt(rs.getString("lab57c1")));
                        bean.setTestState(rs.getInt("lab57c8"));
                        bean.setSampleState(rs.getInt("lab57c16"));
                        bean.setAttachmentTest(rs.getInt("lab57c41"));
                        bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                        bean.setPrint(rs.getInt("lab57c25") == 1);                        
                        bean.setValidatedDate(rs.getTimestamp("lab57c18"));
                   
                        Item item = new Item();
                        item.setId(rs.getInt("lab80c1"));
                       
                        bean.setSex(item);

                 
                        listOrders.add(bean);
                        return bean;
                    };
                    getJdbcTemplate().query(select + from + where.toString(), mapper, parametersArr);
                }
            }

            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los numeros de ordenes por rango de fecha o numero de orden desde
     * base de datos.
     *
     * @param resultFilter
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Long> rangeOrders(ResultFilter resultFilter) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1 ";
            String from = " "
                    + "FROM lab22 ";
            Object[] params = null;
            //Si es 0 -> por fecha y si es 1  es por ordenes
            if (resultFilter.getFilterId() == 0)
            {
                from = from + " WHERE lab22c2 BETWEEN ? AND ?";
                params = new Object[]
                {
                    resultFilter.getFirstDate(), resultFilter.getLastDate()
                };
            } else
            {
                from = from + " WHERE lab22.lab22c1 BETWEEN ? AND ?";
                params = new Object[]
                {
                    resultFilter.getFirstOrder(), resultFilter.getLastOrder()
                };
            }
            from = from + "  AND (lab22c19 = 0 or lab22c19 is null) ";
            RowMapper mapper = (RowMapper<Long>) (ResultSet rs, int i) -> rs.getLong("lab22c1");
            return getJdbcTemplate().query(query + from, mapper, params);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Este metodo se encargara de actualizar una orden dependiendo del objeto
     * updateOrden, ya que este tendra los parametros para la busqueda y
     * actualizacion de la misma
     *
     * @param updateResult
     */
    default UpdateResult getOldObject(UpdateResult updateResult) throws Exception
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab57.lab22c1, lab57.lab39c1 FROM lab57 WHERE lab22c1 = ? AND lab39C1 = ? ";
            return getJdbcTemplate().queryForObject(query,
                    (ResultSet rs, int i) ->
            {
                UpdateResult updateResultOld = new UpdateResult();
                updateResultOld.setNumberOrder(rs.getLong("lab22c1"));
                updateResultOld.setOldExamIdentifier(rs.getInt("lab39C1"));

                return updateResultOld;

            }, updateResult.getNumberOrder(), updateResult.getOldExamIdentifier());

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista los numeros de ordenes por rango de fecha o numero de orden desde
     * base de datos.
     *
     * @param updateResult
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default Integer getSample(UpdateResult updateResult) throws Exception
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT  lab57.lab24c1 FROM  lab57 WHERE lab22c1 = ? AND lab39C1 = ? ";

            return getJdbcTemplate().queryForObject(query,
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab24c1");
            }, updateResult.getNumberOrder(), updateResult.getOldExamIdentifier());

        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }

    /**
     * Retorna el objeto paciente resultado
     *
     * @param order
     * @param test
     *
     * @return el id del paciente
     * @throws Exception Error en la base de datos.
     */
    default PatientResult getPatientResult(long order, int test) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab57.lab39c1 as test, lab57.lab22c1 as order, lab57.lab57c1 as result, lab21.lab21c1 as idpatient, lab21.lab21c2 as history ")
                    .append(",lab21.lab21c3 as name1, lab21.lab21c4 as name2,lab21.lab21c5 as lastName1,lab21.lab21c6 as lastName2 ")
                    .append(",lab103.lab103c1 as idtype ")
                    .append("FROM lab22 ")
                    .append("INNER JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                    .append("INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 ")
                    .append("WHERE lab22.lab22c1 = ").append(order)
                    .append("AND lab57.lab22c1 = ").append(order)
                    .append("AND lab57.lab39c1 = ").append(test);
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                PatientResult patient = new PatientResult();
                patient.setIdOrden(rs.getLong("order"));
                patient.setDocumentType(rs.getInt("idtype"));
                patient.setPatientRecord(Tools.decrypt(rs.getString("history")));
                patient.setFirstName(Tools.decrypt(rs.getString("name1")));
                patient.setSecondName(Tools.decrypt(rs.getString("name2")));
                patient.setFirstLastName(Tools.decrypt(rs.getString("lastName1")));
                patient.setSecondLastName(Tools.decrypt(rs.getString("lastName2")));
                patient.setIdTest(rs.getInt("test"));
                patient.setResults(Tools.decrypt(rs.getString("result")));

                return patient;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Este metodo se encargara de actualizar una orden dependiendo del objeto
     * updateOrden, ya que este tendra los parametros para la busqueda y
     * actualizacion de la misma
     *
     * @param updateResult n
     * @param valueSample
     * @throws java.lang.Exception
     */
    public void updateReference(UpdateResult updateResult, int valueSample) throws Exception;

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}
