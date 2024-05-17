package net.cltech.enterprisent.dao.interfaces.operation.tracking;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.audit.Action;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.Order;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * Auditoria del proceso de laboratorio.
 *
 * @author eacuna
 * @since 30/10/2017
 * @see Creación
 */
public interface AuditDao
{

    /**
     * Obtiene la conexion a la base de datos
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
     * Obtener informacion de la auditoria en la base de datos por orden
     *
     * @param order
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<Action> getActions(Long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab22c1,lab03c1,lab03c2,lab03c3,lab03c4,lab03c5,lab03c6 ");
            query.append(", lab04.lab04c1, lab04c2,lab04c3,lab04c4 ");
            query.append(", lab03.lab30c1, lab30.lab30c2, lab30.lab30c3 ");
            query.append(" FROM lab03 ");
            query.append(" INNER JOIN lab04 on lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 on lab30.lab30c1 = lab03.lab30c1 ");
            query.append(" WHERE  lab03.lab22c1 = ? ");
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                Action audit = new Action();
                Order order1 = new Order();
                order1.setOrderNumber(rs.getLong("lab22c1"));
                audit.setAction(rs.getString("lab03c2"));
                audit.setDate(rs.getTimestamp("lab03c5"));
                audit.setJson(rs.getString("lab03c4"));
                audit.setUser(new User());
                audit.getUser().setId(rs.getInt("lab04c1"));
                audit.getUser().setName(rs.getString("lab04c2"));
                audit.getUser().setLastName(rs.getString("lab04c3"));
                audit.getUser().setUserName(rs.getString("lab04c4"));
                if (rs.getString("lab30c1") != null)
                {
                    audit.setReason(new Motive());
                    audit.getReason().setId(rs.getInt("lab30c1"));
                    audit.getReason().setName(rs.getString("lab30c2"));
                    audit.getReason().setDescription(rs.getString("lab03c3"));
                    audit.getReason().setUser(null);
                }
                return audit;
            }, order);

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Obtener informacion de la auditoria en la base de datos por orden
     *
     * @param order
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<Action> getActionsgetActionsDelete(Long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab22c1,lab03c1,lab03c2,lab03c3,lab03c4,lab03c5,lab03c6 ");
            query.append(", lab04.lab04c1, lab04c2,lab04c3,lab04c4 ");
            query.append(", lab03.lab30c1, lab30.lab30c2, lab30.lab30c3 ");
            query.append(" FROM lab03 ");
            query.append(" INNER JOIN lab04 on lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 on lab30.lab30c1 = lab03.lab30c1 ");
            query.append(" WHERE  lab03.lab22c1 = ? and lab03c2 = 'D' ");
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                Action audit = new Action();
                Order order1 = new Order();
                order1.setOrderNumber(rs.getLong("lab22c1"));
                audit.setAction(rs.getString("lab03c2"));
                audit.setDate(rs.getTimestamp("lab03c5"));
                audit.setJson(rs.getString("lab03c4"));
                audit.setUser(new User());
                audit.getUser().setId(rs.getInt("lab04c1"));
                audit.getUser().setName(rs.getString("lab04c2"));
                audit.getUser().setLastName(rs.getString("lab04c3"));
                audit.getUser().setUserName(rs.getString("lab04c4"));
                if (rs.getString("lab30c1") != null)
                {
                    audit.setReason(new Motive());
                    audit.getReason().setId(rs.getInt("lab30c1"));
                    audit.getReason().setName(rs.getString("lab30c2"));
                    audit.getReason().setDescription(rs.getString("lab03c3"));
                    audit.getReason().setUser(null);
                }
                return audit;
            }, order);

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Lista motivos de modificacion/repeticion
     *
     * @param orders Lista de ordenes a consultar
     * @param reason Motivo
     * @return Lista de motivos
     */
    default List<Motive> listMotives(String orders, LISEnum.ResultReason reason)
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab22c1,lab39c1 ");
            query.append(", lab30.lab30c1, lab30c2, lab30c3 ");
            query.append(" FROM lab29 ");
            query.append(" INNER JOIN lab30 on lab30.lab30c1 = lab29.lab30c1 ");
            query.append(" WHERE  lab29.lab29c5 = ? ");
            if (orders != null && !orders.trim().isEmpty())
            {
                query.append(" AND lab29.lab22c1 in (").append(orders).append(") ");
            }
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setOrder(rs.getLong("lab22c1"));
                motive.setTest(rs.getInt("lab39c1"));
                motive.setName(rs.getString("lab30c2"));
                return motive;
            }, reason.getValue());
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la trazabilidad de la orden
     *
     * @param order Numero de orden
     * @return Trazabilidad de la orden.
     */
    default List<AuditEvent> listTrackingOrder(Long order)
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
            query.append(" lab04.lab04c1, lab04.lab04c4, ");
            query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3 ");
            query.append(" FROM lab03 ");
            query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 AND lab30.lab07c1 = '1' ");
            query.append(" WHERE lab03.lab22c1 = ? AND lab03c3 = 'O' ORDER BY lab03c5 ASC ");
            Object[] params = new Object[]
            {
                order
            };
            return getJdbcTemplate().query(query.toString(), params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setAction(rs.getString("lab03c2"));
                event.setType(rs.getString("lab03c3"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setComment(rs.getString("lab03c6"));
                if (rs.getString("lab30c1") != null)
                {
                    event.setReason(new Motive());
                    event.getReason().setId(rs.getInt("lab30c1"));
                    event.getReason().setName(rs.getString("lab30c2"));
                    event.getReason().setDescription(rs.getString("lab03c3"));
                    event.getReason().setUser(null);
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la auditoria del almacenamiento de la muestra
     *
     * @param order número de orden
     * @param sample id muestra
     * @return Lista de cambios realizados
     */
    default List<AuditEvent> sampleStorageTracking(Long order, int sample)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
            query.append(" lab04.lab04c1, lab04.lab04c4, ");
            query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3 ");
            query.append(" FROM  ").append(lab03).append(" as lab03 ");
            query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 AND lab30.lab07c1 = '1' ");
            query.append(" WHERE lab03.lab22c1 = ? AND lab03c1 = ? AND lab03c3 = ? ");
            query.append(" ORDER BY lab03c5 ");
            Object[] params = new Object[]
            {
                order, sample, AuditOperation.TYPE_SAMPLE_STORAGE
            };
            return getJdbcTemplate().query(query.toString(), params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                event.setComment(rs.getString("lab03c6"));
                event.setType(rs.getString("lab03c3"));
                if (rs.getString("lab30c1") != null)
                {
                    event.setReason(new Motive());
                    event.getReason().setId(rs.getInt("lab30c1"));
                    event.getReason().setName(rs.getString("lab30c2"));
                    event.getReason().setDescription(rs.getString("lab03c3"));
                    event.getReason().setUser(null);
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

      /**
     * Obtener el detalle de la auditoria de la orden
     *
     * @param order
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getAuditOrderSample(Long order, int sample) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;
           
            
            String query = ""
                    + "SELECT lab03.lab03c2"
                    + " ,lab03.lab03c1"
                    + " ,lab03.lab03c3"
                    + " ,lab03.lab03c4"
                    + " ,lab03.lab03c5"
                    + " ,lab03.lab03c6"
                    + " ,lab03.lab03c7"
                    + " ,lab03.lab03c8"
                    + " ,lab53c3"
                    + " ,lab03.lab04c1"
                    + " ,lab04c4"
                    + " ,lab04c2"
                    + " ,lab04c3"
                    + " FROM " + lab03 + " as lab03 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 "
                    + " LEFT JOIN lab53 ON lab53.lab53c1 = lab03.lab03c7 "
                    + " WHERE  lab03.lab22c1 = ?  and (lab03c3 = 'S' OR lab03c3 = 'SS') and lab03c1 = ?";
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        order, sample
                    }, (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setId(rs.getInt("lab03c1"));
                audit.setAction(rs.getString("lab03c2"));
                audit.setFieldType(rs.getString("lab03c3"));
                audit.setInformation(rs.getString("lab03c4"));
                audit.setDate(rs.getTimestamp("lab03c5"));

                audit.setUser(rs.getInt("lab04c1"));
                audit.setUsername(rs.getString("lab04c4"));
                audit.setName1(rs.getString("lab04c2"));
                audit.setLastName(rs.getString("lab04c3"));

                audit.setIdDestination(rs.getInt("lab03c7"));
                audit.setDestination(rs.getString("lab53c3"));

                return audit;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Consulta la trazabilidad del paciente
     *
     * @param id Id del paciente
     * @param yearsQuery Años de consulta (historicos)
     * @return Trazabilidad del paciente.
     */
    default List<AuditEvent> listTrackingPatient(Integer id, int yearsQuery)
    {
        try
        {
            List<AuditEvent> audits = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
            String lab03;

            for (Integer year : years)
            {
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append(" SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
                query.append(" lab04.lab04c1, lab04.lab04c4, ");
                query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3, lab30.lab30c4 ");
                query.append(" FROM  ").append(lab03).append(" as lab03 ");
                query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
                query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 AND lab30.lab07c1 = '1' ");
                query.append(" WHERE lab03.lab03c1 = ? AND lab03c3 = 'D' ORDER BY lab03c5 ");
                Object[] params = new Object[]
                {
                    id
                };
                getJdbcTemplate().query(query.toString(), params, (ResultSet rs, int i) ->
                {
                    AuditEvent event = new AuditEvent();
                    event.setDate(rs.getTimestamp("lab03c5"));
                    event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                    event.getUser().setUserName(rs.getString("lab04c4"));
                    event.setCurrent(rs.getString("lab03c4"));
                    event.setAction(rs.getString("lab03c2"));
                    event.setComment(rs.getString("lab03c6"));
                    event.setType(rs.getString("lab03c3"));
                    if (rs.getString("lab30c1") != null)
                    {
                        event.setReason(new Motive());
                        event.getReason().setId(rs.getInt("lab30c1"));
                        event.getReason().setName(rs.getString("lab30c2"));
                        event.getReason().setDescription(rs.getString("lab03c3"));
                        event.getReason().setUser(null);
                    }

                    audits.add(event);
                    return event;
                });
            }

            return audits;

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la trazabilidad de la muestra para una orden y muestra
     *
     * @param order Numero de orden
     * @param sample Id muestra
     * @return Trazabilidad de la muestra.
     */
    default List<AuditEvent> listTrackingSample(Long order, Integer sample)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

            Object[] params = null;
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
            query.append(" lab04.lab04c1, lab04.lab04c4, ");
            query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3, lab30.lab30c4 ");
            query.append(" FROM  ").append(lab03).append(" as lab03 ");
            query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 AND lab30.lab07c1 = '1' ");
            if (sample != null)
            {
                query.append(" WHERE lab03.lab22c1 = ? AND lab03c1 = ? AND lab03c3 = 'S' ORDER BY lab03c5 ASC ");
                params = new Object[]
                {
                    order, sample
                };
            } else
            {
                query.append(" WHERE lab03.lab22c1 = ? AND lab03c3 = 'S' ORDER BY lab03c5 ASC ");
                params = new Object[]
                {
                    order
                };
            }
            return getJdbcTemplate().query(query.toString(), params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                event.setComment(rs.getString("lab03c6"));
                event.setType(rs.getString("lab03c3"));
                if (rs.getString("lab30c1") != null)
                {
                    event.setReason(new Motive());
                    event.getReason().setId(rs.getInt("lab30c1"));
                    event.getReason().setName(rs.getString("lab30c2"));
                    event.getReason().setDescription(rs.getString("lab03c3"));
                    event.getReason().setUser(null);
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
       /**
     * Obtener el detalle de la auditoria de la orden
     *
     * @param order
     * @param test
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getAuditOrderTest(Long order, int test) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;
                
            String query = ""
                    + "SELECT lab03.lab03c2"
                    + " ,lab03.lab03c1"
                    + " ,lab03.lab03c3"
                    + " ,lab03.lab03c4"
                    + " ,lab80c4"
                    + " ,lab80c5"
                    + " ,lab03.lab03c5"
                    + " ,lab03.lab03c6"
                    + " ,lab03.lab03c7"
                    + " ,lab03.lab03c8"
                    + " ,lab39c4"
                    + " ,lab39c37"
                    + " ,lab03.lab04c1"
                    + " ,lab04c4"
                    + " ,lab04c2"
                    + " ,lab04c3"
                    + " ,lab03.lab03c10"
                    + " ,lab03.lab75c1"
                    + " FROM " + lab03 + " as lab03 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 "
                    + " LEFT JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1 "
                    + " LEFT JOIN lab80 ON lab03.lab75c1 = lab80.lab80c1 "
                    + " WHERE  lab03.lab22c1 = ? and (lab03.lab03c3 = 'T' OR lab03.lab03c3 = 'BK' OR lab03.lab03c3 = 'RC' OR lab03.lab03c3 = 'DO') and lab03c1 = ?";
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        order, test
                    }, (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setId(rs.getInt("lab03c1"));
                audit.setAction(rs.getString("lab03c2"));
                audit.setFieldType(rs.getString("lab03c3"));
                audit.setInformation(rs.getString("lab03c4"));
                audit.setDate(rs.getTimestamp("lab03c5"));
                
                if(rs.getString("lab75C1") != null){
                    audit.setDelivery(rs.getInt("lab75C1"));
                    audit.setReceivesPerson(rs.getString("lab03c10"));
                    audit.setDeliveryEsCo(rs.getString("lab80c4"));
                    audit.setDeliveryEnUSA(rs.getString("lab80c5"));
                }
                
                audit.setUser(rs.getInt("lab04c1"));
                audit.setUsername(rs.getString("lab04c4"));
                audit.setName1(rs.getString("lab04c2"));
                audit.setLastName(rs.getString("lab04c3"));
            
                audit.setComment(rs.getString("lab03c6"));
                audit.setResult(rs.getString("lab03c8"));
                audit.setTestType(rs.getInt("lab39c37"));
              
                audit.setFieldDescription(rs.getString("lab39c4"));
                
                return audit;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la auditoria de las muestras de una orden agrupadas por muestra
     *
     * @param order Numero de orden
     * @return Trazabilidad de las muestras.
     */
    default List<AuditEvent> listTrackingSamplesByOrder(Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
            query.append(" lab04.lab04c1, lab04.lab04c4, ");
            query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3, lab30.lab30c4 ");
            query.append(" FROM  ").append(lab03).append(" as lab03 ");
            query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 ");
            query.append(" AND lab30.lab07c1 = '1' ");
            query.append(" WHERE lab03.lab22c1 = ? AND lab03c3 = 'S' ORDER BY lab03c1, lab03c5 ");
            Object[] params = new Object[]
            {
                order
            };
            return getJdbcTemplate().query(query.toString(), params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                event.setComment(rs.getString("lab03c6"));
                event.setType(rs.getString("lab03c3"));
                if (rs.getString("lab30c1") != null)
                {
                    event.setReason(new Motive());
                    event.getReason().setId(rs.getInt("lab30c1"));
                    event.getReason().setName(rs.getString("lab30c2"));
                    event.getReason().setDescription(rs.getString("lab03c3"));
                    event.getReason().setUser(null);
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta las auditorias de laboratorios por usuario, tipo y rango de
     * fecha
     *
     * @param user
     * @param initialDate
     * @param finalDate
     * @param type
     * @return Trazabilidad de las muestras.
     */
    default List<AuditEvent> listLaboratoriesByUser(Integer user, String initialDate, String finalDate, String type) throws ParseException
    {
        try
        {
            StringBuilder query = new StringBuilder();
            StringBuilder where = new StringBuilder();
            List<Timestamp> times = new ArrayList<>(0);
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT lab03.lab03c2, lab03.lab03c3, lab03.lab03c4, lab03c5, lab03c6, ");
            query.append(" lab04.lab04c1, lab04.lab04c4, ");
            query.append(" lab03.lab30c1, lab30.lab30c2, lab30.lab30c3, lab30.lab30c4 ");
            query.append(" FROM lab03 ");
            query.append(" INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
            query.append(" LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 AND lab30.lab07c1 = '1' ");
            if (user != null)
            {
                where.append(" WHERE lab03.lab04c1 = ").append(user).append(" ");
            }
            if (initialDate != null)
            {
                times = Tools.rangeDates(initialDate, finalDate);
                where.append(where.toString().equals("") ? " WHERE " : " AND ").append(" lab03.lab03c5 BETWEEN ").append(" ?").append(" AND ").append(" ? ");
            }
            where.append(where.toString().equals("") ? " WHERE " : " AND ").append(" lab03c3 = '").append(type).append("' ");
            where.append(" ORDER BY lab03.lab03c5 ASC ");
            return getJdbcTemplate().query(query.toString() + where.toString(), (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                event.setComment(rs.getString("lab03c6"));
                event.setType(rs.getString("lab03c3"));
                if (rs.getString("lab30c1") != null)
                {
                    event.setReason(new Motive());
                    event.getReason().setId(rs.getInt("lab30c1"));
                    event.getReason().setName(rs.getString("lab30c2"));
                    event.getReason().setDescription(rs.getString("lab03c3"));
                    event.getReason().setUser(null);
                }
                return event;
            }, times.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtener el detalle de la auditoria de la orden
     *
     * @param order
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getAuditOrder(Long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;
            String lab75 = year.equals(currentYear) ? "lab75" : "lab75_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = ""
                    + "SELECT lab03.lab03c2"
                    + " ,lab03.lab03c1"
                    + " ,lab03.lab03c3"
                    + " ,lab03.lab03c4"
                    + " ,lab80c4"
                    + " ,lab80c5"
                    + " ,lab03.lab03c5"
                    + " ,lab03.lab03c6"
                    + " ,lab03.lab03c7"
                    + " ,lab03.lab03c8"
                    + " ,lab53c3"
                    + " ,lab39c4"
                    + " ,lab39c37"
                    + " ,lab39.lab24c1"
                    + " ,lab24c2"
                    + " ,lab03.lab04c1"
                    + " ,lab04c4"
                    + " ,lab04c2"
                    + " ,lab04c3"
                    + " ,lab30.lab30c1"
                    + " ,lab30.lab30c2"
                    + " ,lab03.lab03c10"
                    + " ,lab03.lab75c1"
                    + " FROM " + lab03 + " as lab03 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 "
                    + " LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 "
                    + " LEFT JOIN lab53 ON lab53.lab53c1 = lab03.lab03c7 "
                    + " LEFT JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1 and (lab03.lab03c3 = 'T' OR lab03.lab03c3 = 'BK' OR lab03.lab03c3 = 'RC' OR lab03.lab03c3 = 'DO') "
                    + " LEFT JOIN lab24 ON lab24.lab24c1 = lab03.lab03c1 and (lab03c3 = 'S' OR lab03c3 = 'SS')"
                    + " LEFT JOIN lab80 ON lab03.lab75c1 = lab80.lab80c1 "
                    + " WHERE  lab03.lab22c1 = ? OR (lab03c3 = 'P' AND lab03c1 = (Select lab21c1 from " + lab22 + " as lab22 where lab22c1 = ?))";
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        order, order
                    }, (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setId(rs.getInt("lab03c1"));
                audit.setAction(rs.getString("lab03c2"));
                audit.setFieldType(rs.getString("lab03c3"));
                audit.setInformation(rs.getString("lab03c4"));
                audit.setDate(rs.getTimestamp("lab03c5"));
                
                if(rs.getString("lab75C1") != null){
                    audit.setDelivery(rs.getInt("lab75C1"));
                    audit.setReceivesPerson(rs.getString("lab03c10"));
                    audit.setDeliveryEsCo(rs.getString("lab80c4"));
                    audit.setDeliveryEnUSA(rs.getString("lab80c5"));
                }
                
                audit.setUser(rs.getInt("lab04c1"));
                audit.setUsername(rs.getString("lab04c4"));
                audit.setName1(rs.getString("lab04c2"));
                audit.setLastName(rs.getString("lab04c3"));

                audit.setMotive(rs.getInt("lab30c1"));
                audit.setComment(rs.getString("lab03c6"));
                audit.setIdDestination(rs.getInt("lab03c7"));
                audit.setDestination(rs.getString("lab53c3"));
                audit.setResult(rs.getString("lab03c8"));
                audit.setSampleTest(rs.getInt("lab24c1"));
                audit.setTestType(rs.getInt("lab39c37"));
               
                if ("S".equals(audit.getFieldType()) || "SS".equals(audit.getFieldType()))
                {
                    audit.setFieldDescription(rs.getString("lab24c2"));
                } else
                {
                    audit.setFieldDescription(rs.getString("lab39c4"));
                }

                return audit;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener el detalle de la auditoria de la orden
     *
     * @param initialDate
     * @param finalDate
     * @param user
     * @param yearsQuery Años de consulta (historicos)
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getAuditUser(String initialDate, String finalDate, int user, int yearsQuery) throws Exception
    {
        try
        {
            List<AuditOperation> audits = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
            String lab03;

            for (Integer year : years)
            {
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;
                List<Timestamp> times = new ArrayList<>(0);
                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab03);
                if (tableExists)
                {
                    String query = ""
                            + "SELECT lab03.lab03c2"
                            + " ,lab03.lab03c1"
                            + " ,lab03.lab03c3"
                            + " ,lab03.lab22c1"
                            + " ,lab03.lab03c4"
                            + " ,lab03.lab03c3"
                            + " ,lab03.lab03c5"
                            + " ,lab03.lab03c6"
                            + " ,lab03.lab03c7"
                            + " ,lab03.lab03c8"
                            + " ,lab53c3"
                            + " ,lab39c4"
                            + " ,lab39c37"
                            + " ,lab39.lab24c1"
                            + " ,lab24c2"
                            + " ,lab03.lab04c1"
                            + " ,lab04c4"
                            + " ,lab30.lab30c1"
                            + " ,lab30.lab30c2"
                            + " FROM " + lab03 + " as lab03 "
                            + " INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 "
                            + " LEFT JOIN lab30 ON lab30.lab30c1 = lab03.lab30c1 "
                            + " LEFT JOIN lab53 ON lab53.lab53c1 = lab03.lab03c7 "
                            + " LEFT JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1 and (lab03.lab03c3 = 'T' OR lab03.lab03c3 = 'BK' OR lab03.lab03c3 = 'RC' OR lab03.lab03c3 = 'DO') "
                            + " LEFT JOIN lab24 ON lab24.lab24c1 = lab03.lab03c1 and (lab03c3 = 'S' OR lab03c3 = 'SS')"
                            + " WHERE  lab03.lab04c1 = ? ";
                    /*Order By, Group By y demas complementos de la consulta*/
                    query = query + "";

                    if (initialDate != null)
                    {
                        times = Tools.rangeDates(initialDate, finalDate);
                        query = query + " AND lab03.lab03c5 BETWEEN ? AND  ? ";
                    }

                    query = query + " ORDER BY lab03.lab03c5 ASC ";

                    getJdbcTemplate().query(query,
                            new Object[]
                            {
                                user, times.get(0), times.get(1)
                            }, (ResultSet rs, int i) ->
                    {
                        AuditOperation audit = new AuditOperation();
                        audit.setId(rs.getInt("lab03c1"));
                        audit.setOrder(rs.getLong("lab22c1"));
                        audit.setAction(rs.getString("lab03c2"));
                        audit.setFieldType(rs.getString("lab03c3"));
                        audit.setInformation(rs.getString("lab03c4"));
                        audit.setDate(rs.getTimestamp("lab03c5"));
                        audit.setUser(rs.getInt("lab04c1"));
                        audit.setUsername(rs.getString("lab04c4"));
                        audit.setMotive(rs.getInt("lab30c1"));
                        audit.setComment(rs.getString("lab03c6"));
                        audit.setIdDestination(rs.getInt("lab03c7"));
                        audit.setDestination(rs.getString("lab53c3"));
                        audit.setResult(rs.getString("lab03c8"));
                        audit.setSampleTest(rs.getInt("lab24c1"));
                        audit.setTestType(rs.getInt("lab39c37"));
                        if ("S".equals(audit.getFieldType()) || "SS".equals(audit.getFieldType()))
                        {
                            audit.setFieldDescription(rs.getString("lab24c2"));
                        } else
                        {
                            audit.setFieldDescription(rs.getString("lab39c4"));
                        }

                        audits.add(audit);

                        return audit;
                    });
                }
            }

            return audits;

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener la auditoria general de una orden
     *
     * @param init
     * @param end
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getAuditGeneralOrder(Long init, Long end) throws Exception
    {
        try
        {
            List<AuditOperation> audits = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(init), String.valueOf(end));
            String lab22;
            String lab03;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

                String query = " SELECT lab03c2, lab03c4, lab03.lab22c1, lab22c3, lab03c5, lab22.lab04c1, register.lab04c4 as register, causer.lab04c4 as causer, "
                        + " lab03c3, lab39c4 ,lab39c37, lab03c8, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6"
                        + " FROM  " + lab03 + " as lab03 "
                        + " LEFT JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab03.lab22c1 "
                        + " LEFT JOIN lab21 ON (lab22.lab21c1 = lab21.lab21c1 AND lab03c3 in ('D','T','RT','O')) OR (lab03.lab03c1 = lab21.lab21c1 AND lab03c3 in ('P')) "
                        + " LEFT JOIN lab04 causer ON causer.lab04c1 = lab03.lab04c1 "
                        + " LEFT JOIN lab04 register ON register.lab04c1 = lab22.lab04c1 "
                        + " LEFT JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1 and (lab03.lab03c3 = 'T') "
                        + " WHERE (lab03c2 in ('U','D')) "
                        + " AND (lab03c3 in ('D','T','RT','O','P')) "
                        + " AND ("
                        + " (lab03.lab03c3 = 'P' AND lab03.lab03c1 in (Select lab21c1 from " + lab22 + " as lab22 where lab22c1 BETWEEN ? AND ?)) "
                        + "OR "
                        + "(lab03.lab22c1 BETWEEN ? AND ?)) AND (lab22c19 = 0 or lab22c19 is null) ";

                /*Order By, Group By y demas complementos de la consulta*/
                query = query + "";

                getJdbcTemplate().query(query,
                        new Object[]
                        {
                            init, end, init, end
                        }, (ResultSet rs, int i) ->
                {
                    AuditOperation audit = new AuditOperation();

                    audit.setAction(rs.getString("lab03c2"));
                    audit.setOrder(rs.getLong("lab22c1"));
                    audit.setDateOrder(rs.getTimestamp("lab22c3"));
                    audit.setDate(rs.getTimestamp("lab03c5"));
                    audit.setRegister(rs.getString("register"));
                    audit.setUsername(rs.getString("causer"));
                    audit.setFieldType(rs.getString("lab03c3"));
                    audit.setInformation(rs.getString("lab03c4"));
                    audit.setFieldDescription(rs.getString("lab39c4"));
                    audit.setResult(rs.getString("lab03c8"));
                    audit.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    audit.setName1(Tools.decrypt(rs.getString("lab21c3")));
                    audit.setName2(Tools.decrypt(rs.getString("lab21c4")));
                    audit.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    audit.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                    audits.add(audit);

                    return audit;
                });
            }
            return audits;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener el detalle para la auditoria de la caja de una orden
     *
     * @param order
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getCashAuditByOrderNumber(Long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab917c1 AS actions")
                    .append(", lab917c2 AS jsonInformation")
                    .append(", lab917c3 AS executionType")
                    .append(", lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab917c4 AS registrationDate")
                    .append(", lab22c1 AS orderNumber")
                    .append(" FROM lab917 ")
                    .append("JOIN lab04 ON lab04.lab04c1 = lab917.lab04c1 ")
                    .append("WHERE lab22c1 = ").append(order)
                    .append(" AND lab917c3 = 'CB'");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setAction(rs.getString("actions"));
                audit.setInformation(rs.getString("jsonInformation"));
                audit.setExecutionType(rs.getString("executionType"));
                audit.setUser(rs.getInt("userId"));
                audit.setUsername(rs.getString("userName"));
                audit.setDate(rs.getTimestamp("registrationDate"));
                audit.setOrder(rs.getLong("orderNumber"));
                return audit;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la trazabilidad de las auditorias registradas para una factura
     * durante un rango de fechas
     *
     * @param initDate
     * @param endDate
     * @return Lista de trazabilidad para la factura
     * @throws Exception Error en base de datos
     */
    default List<AuditOperation> getTraceabilityOfInvoices(Timestamp initDate, Timestamp endDate) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab901.lab901c1 AS invoiceId")
                    .append(", lab901.lab901c2 AS invoiceNumber")
                    .append(", lab917c1 AS actions")
                    .append(", lab917c2 AS jsonInformation")
                    .append(", lab917c3 AS executionType")
                    .append(", lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab917c4 AS registrationDate")
                    .append(", lab22c1 AS orderNumber")
                    .append(" FROM lab917 ")
                    .append("JOIN lab04 ON lab04.lab04c1 = lab917.lab04c1 ")
                    .append("LEFT JOIN lab901 ON lab901.lab901c1 = lab917.lab901c1 ")
                    .append("WHERE lab917.lab917c4 BETWEEN '").append(initDate).append("'")
                    .append(" AND '").append(endDate).append("'")
                    .append(" AND lab917c1 = 'I'")
                    .append(" AND lab917c3 = 'INV'");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setInvoiceId(rs.getLong("invoiceId"));
                audit.setInvoiceNumber(rs.getString("invoiceNumber"));
                audit.setAction(rs.getString("actions"));
                audit.setInformation(rs.getString("jsonInformation"));
                audit.setExecutionType(rs.getString("executionType"));
                audit.setUser(rs.getInt("userId"));
                audit.setUsername(rs.getString("userName"));
                audit.setDate(rs.getTimestamp("registrationDate"));
                audit.setOrder(rs.getLong("orderNumber"));
                return audit;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el detalle de una factura por el número de esta para su auditoria
     *
     * @param invoiceNumber
     * @return Detalle de la factura para la auditoria
     * @throws Exception Error en la base de datos.
     */
    default List<AuditOperation> getTraceabilityOfInvoice(String invoiceNumber) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab901.lab901c1 AS invoiceId")
                    .append(", lab901.lab901c2 AS invoiceNumber")
                    .append(", lab917c1 AS actions")
                    .append(", lab917c2 AS jsonInformation")
                    .append(", lab917c3 AS executionType")
                    .append(", lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab917c4 AS registrationDate")
                    .append(", lab22c1 AS orderNumber")
                    .append(" FROM lab917 ")
                    .append("JOIN lab04 ON lab04.lab04c1 = lab917.lab04c1 ")
                    .append("JOIN lab901 ON lab901.lab901c1 = lab917.lab901c1 ")
                    .append("WHERE lab901.lab901c2 = '").append(invoiceNumber).append("'");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                AuditOperation audit = new AuditOperation();
                audit.setInvoiceId(rs.getLong("invoiceId"));
                audit.setInvoiceNumber(rs.getString("invoiceNumber"));
                audit.setAction(rs.getString("actions"));
                audit.setInformation(rs.getString("jsonInformation"));
                audit.setExecutionType(rs.getString("executionType"));
                audit.setUser(rs.getInt("userId"));
                audit.setUsername(rs.getString("userName"));
                audit.setDate(rs.getTimestamp("registrationDate"));
                audit.setOrder(rs.getLong("orderNumber"));
                return audit;
            });
        } catch (DataAccessException ex)
        {
            return null;
        }
    }
}
