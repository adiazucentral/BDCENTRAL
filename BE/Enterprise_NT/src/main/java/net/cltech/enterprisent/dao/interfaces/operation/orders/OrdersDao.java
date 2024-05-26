package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.IdsPatientOrderTest;
import net.cltech.enterprisent.domain.integration.siga.SigaFilterOrders;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.FilterTestPrice;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.FilterSubsequentPayments;
import net.cltech.enterprisent.domain.operation.orders.InconsistentOrder;
import net.cltech.enterprisent.domain.operation.orders.LastOrderPatient;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.OrderNumSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.ShiftOrder;
import net.cltech.enterprisent.domain.operation.orders.SuperPatient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.orders.TicketTest;
import net.cltech.enterprisent.domain.operation.tracking.PackageTracking;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para todo lo relacionado con ordenes y pacientes
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creación
 */
public interface OrdersDao {

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Agrega la columna de un demografico a la tabla de pacientes
     *
     * @param demographic
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     *
     * @throws Exception Error en base de datos
     */
    public void addDemographicToPatient(Demographic demographic) throws Exception;

    /**
     * Agrega la columna de un demografico a la tabla de ordenes
     *
     * @param demographic
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     *
     * @throws Exception Error en base de datos
     */
    public void addDemographicToOrder(Demographic demographic, int yearsQuery) throws Exception;

    /**
     * Busca una orden por el numero de orden
     *
     * @param orderNumber Numero de orden
     * @param demographics Lista de
     * {net.cltech.enterprisent.domain.masters.demographic.Demographic} con los
     * demograficos configurados para la orden activos
     * @param laboratorys
     * @param idbranch
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default Order get(long orderNumber, final List<Demographic> demographics,  List<Integer> laboratorys, int idbranch ) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT  lab22.lab22c1 "
                    + " , lab22.lab22c2 "
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
                    + " , lab04.lab04c2 "
                    + " , lab04.lab04c3 "
                    + " , lab04.lab04c4 "
                    + " , lab22.lab07c1 "
                    + " , lab05.lab05c1 "
                    + " , lab05.lab05c10 "
                    + " , lab05.lab05c4 "
                    + " , lab10.lab10c1 "
                    + " , lab10.lab10c7 "
                    + " , lab10.lab10c8 "
                    + " , lab10.lab10c2 "
                    + " , lab19.lab19c1 "
                    + " , lab19.lab19c2 "
                    + " , lab19.lab19c3 "
                    + " , lab19.lab19c22 "
                    + " , lab14.lab14c1 "
                    + " , lab14.lab14c2 "
                    + " , lab14.lab14c3 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 "
                    + " , lab04c1_1.lab04c1 AS userCreateId"
                    + " , lab04c1_1.lab04c2 AS userCreateName"
                    + " , lab04c1_1.lab04c3 AS userCreateLastName"
                    + " , lab04c1_1.lab04c4 AS userCreateNick";
            String from = ""
                    + " FROM " + lab22 + " AS lab22"
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                    + " INNER JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 "
                    + " INNER JOIN lab04 lab04c1_1 ON lab04c1_1.lab04c1 = lab22.lab04c1_1"
                    + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                    + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                    + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                    + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                    + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                    + "";
            for (Demographic demographic : demographics) {
                if (demographic.isEncoded()) {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE lab22.lab22c1 = ? AND (lab22c19 = 0 or lab22c19 is null)";
            
            where += (SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));

            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
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

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                // Usuario que creo la orden
                User user = new User();
                user.setId(rs.getInt("userCreateId"));
                user.setName(rs.getString("userCreateName"));
                user.setLastName(rs.getString("userCreateLastName"));
                user.setUserName(rs.getString("userCreateNick"));
                order.setCreateUser(user);
                // Ultimo usuario que modifico la orden
                User userUpdate = new User();
                userUpdate.setId(rs.getInt("lab04c1"));
                userUpdate.setName(rs.getString("lab04c2"));
                userUpdate.setLastName(rs.getString("lab04c3"));
                userUpdate.setUserName(rs.getString("lab04c4"));
                order.setLastUpdateUser(userUpdate);

                order.setActive(rs.getInt("lab07c1") == 1);
                order.setExternalId(rs.getString("lab22c7"));

                if (rs.getString("lab05c1") != null) {
                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                }

                if (rs.getString("lab10c1") != null) {
                    ServiceLaboratory service = new ServiceLaboratory();
                    service.setId(rs.getInt("lab10c1"));
                    service.setCode(rs.getString("lab10c7"));
                    service.setHospitalSampling(rs.getInt("lab10c8") == 1);
                    service.setName(rs.getString("lab10c2"));
                    order.setService(service);
                }

                if (rs.getString("lab19c1") != null) {
                    Physician physician = new Physician();
                    physician.setId(rs.getInt("lab19c1"));
                    physician.setCode(rs.getString("lab19c22"));
                    physician.setLastName(rs.getString("lab19c3"));
                    physician.setName(rs.getString("lab19c2") + " " + rs.getString("lab19c3"));
                    order.setPhysician(physician);
                }

                if (rs.getString("lab14c1") != null) {
                    Account account = new Account();
                    account.setId(rs.getInt("lab14c1"));
                    account.setNit(rs.getString("lab14c2"));
                    account.setName(rs.getString("lab14c3"));
                    order.setAccount(account);
                }

                if (rs.getString("lab904c1") != null) {
                    Rate rate = new Rate();
                    rate.setId(rs.getInt("lab904c1"));
                    rate.setCode(rs.getString("lab904c2"));
                    rate.setName(rs.getString("lab904c3"));
                    order.setRate(rate);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded()) {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                        }
                    } else {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                return order;
            }, orderNumber);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
    
     /**
     * Busca una orden por el numero de orden
     *
     * @param orderNumber Numero de orden
     * @param demographics Lista de
     * {net.cltech.enterprisent.domain.masters.demographic.Demographic} con los
     * demograficos configurados para la orden activos
     * @param filterappointment
     * @param laboratorys
     * @param idbranch
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default Order getAppointment(long orderNumber, final List<Demographic> demographics, int filterappointment,  int idbranch) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab22.lab22c2 "
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
                    + " , lab04.lab04c2 "
                    + " , lab04.lab04c3 "
                    + " , lab04.lab04c4 "
                    + " , lab22.lab07c1 "
                    + " , lab05.lab05c1 "
                    + " , lab05.lab05c10 "
                    + " , lab05.lab05c4 "
                    + " , lab10.lab10c1 "
                    + " , lab10.lab10c7 "
                    + " , lab10.lab10c8 "
                    + " , lab10.lab10c2 "
                    + " , lab19.lab19c1 "
                    + " , lab19.lab19c2 "
                    + " , lab19.lab19c3 "
                    + " , lab19.lab19c22 "
                    + " , lab14.lab14c1 "
                    + " , lab14.lab14c2 "
                    + " , lab14.lab14c3 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 "
                    + " , lab04c1_1.lab04c1 AS userCreateId"
                    + " , lab04c1_1.lab04c2 AS userCreateName"
                    + " , lab04c1_1.lab04c3 AS userCreateLastName"
                    + " , lab04c1_1.lab04c4 AS userCreateNick";
                    
           
            
            String from = ""
                    + " FROM " + lab22 + " AS lab22"
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                    + " INNER JOIN lab04 lab04c1_1 ON lab04c1_1.lab04c1 = lab22.lab04c1_1"
                    + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                    + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                    + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                    + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                    + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 ";
                    
            if(filterappointment == 1){
                select  = select + " , hmb12.hmb12c1, hmb12.hmb12c2, hmb12.hmb09c1, hmb09c2, hmb09c4,hmb09c5 ";
                
                from = from + " INNER JOIN hmb12 on lab22.lab22c1 = hmb12.hmb12c5  "
                            + " INNER JOIN hmb09 on hmb12.hmb09c1 = hmb09.hmb09c1 ";
            }
            
                    
            for (Demographic demographic : demographics) {
                if (demographic.isEncoded()) {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    lab22c1 = ? AND (lab22c19 = 1) and lab22.lab05c1 = ?";
            
            
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
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

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                // Usuario que creo la orden
                User user = new User();
                user.setId(rs.getInt("userCreateId"));
                user.setName(rs.getString("userCreateName"));
                user.setLastName(rs.getString("userCreateLastName"));
                user.setUserName(rs.getString("userCreateNick"));
                order.setCreateUser(user);
                // Ultimo usuario que modifico la orden
                User userUpdate = new User();
                userUpdate.setId(rs.getInt("lab04c1"));
                userUpdate.setName(rs.getString("lab04c2"));
                userUpdate.setLastName(rs.getString("lab04c3"));
                userUpdate.setUserName(rs.getString("lab04c4"));
                order.setLastUpdateUser(userUpdate);

                order.setActive(rs.getInt("lab07c1") == 1);
                order.setExternalId(rs.getString("lab22c7"));
                if(filterappointment == 1){
                    order.getAppointment().setId(rs.getInt("hmb12c1"));
                    order.getAppointment().setDate(rs.getInt("hmb12c2"));

                    Shift bean = new Shift();
                    bean.setId(rs.getInt("hmb09c1"));
                    bean.setName(rs.getString("hmb09c2"));
                    bean.setInit(rs.getInt("hmb09c4"));
                    bean.setEnd(rs.getInt("hmb09c5"));

                    order.getAppointment().setShift(bean);
                }

                if (rs.getString("lab05c1") != null) {
                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                }

                if (rs.getString("lab10c1") != null) {
                    ServiceLaboratory service = new ServiceLaboratory();
                    service.setId(rs.getInt("lab10c1"));
                    service.setCode(rs.getString("lab10c7"));
                    service.setHospitalSampling(rs.getInt("lab10c8") == 1);
                    service.setName(rs.getString("lab10c2"));
                    order.setService(service);
                }

                if (rs.getString("lab19c1") != null) {
                    Physician physician = new Physician();
                    physician.setId(rs.getInt("lab19c1"));
                    physician.setCode(rs.getString("lab19c22"));
                    physician.setLastName(rs.getString("lab19c3"));
                    physician.setName(rs.getString("lab19c2") + " " + rs.getString("lab19c3"));
                    order.setPhysician(physician);
                }

                if (rs.getString("lab14c1") != null) {
                    Account account = new Account();
                    account.setId(rs.getInt("lab14c1"));
                    account.setNit(rs.getString("lab14c2"));
                    account.setName(rs.getString("lab14c3"));
                    order.setAccount(account);
                }

                if (rs.getString("lab904c1") != null) {
                    Rate rate = new Rate();
                    rate.setId(rs.getInt("lab904c1"));
                    rate.setCode(rs.getString("lab904c2"));
                    rate.setName(rs.getString("lab904c3"));
                    order.setRate(rate);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded()) {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                        }
                    } else {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                return order;
            }, orderNumber, idbranch);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Busca una orden por el numero de orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default Order getConfigPrint(long orderNumber) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab22.lab22c17 ";
            String from = ""
                    + " FROM " + lab22 + " AS lab22"
                    + "";
            String where = " WHERE    lab22c1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setConfigPrint(rs.getString("lab22c17"));

                return order;
            }, orderNumber);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Busca una orden por el numero de orden
     *
     * @param order Numero de orden
     *
     *
     * @throws Exception Error en base de datos
     */
    default Order updateConfigPrint(Order order) throws Exception {
        if (order.getOrderNumber() > 0) {
            Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            Date date = new Date();
            StringBuilder query = new StringBuilder();
            //paso de los comentarios fijos a el comentario del resultado
            query.append("UPDATE ").append(lab22).append(" SET ")
                    .append(" lab22c17 = '").append(order.getConfigPrint()).append("'")
                    .append(" WHERE ")
                    .append(" lab22c1 = ").append(order.getOrderNumber());
            getJdbcTemplate().update(query.toString());

        }
        return order;
    }

    /**
     * Busca una orden por el numero de orden
     *
     * @param orderNumber Numero de orden
     * @param demographics Lista de
     * {net.cltech.enterprisent.domain.masters.demographic.Demographic} con los
     * demograficos configurados para la orden activos
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default Order getEmailDemo(long orderNumber, final List<Demographic> demographics) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab22.lab21c1 "
                    + " , lab103.lab103c1 "
                    + " , lab103.lab103c6 "
                    + " , lab10.lab10c1 "
                    + " , lab10.lab10c10 "
                    + " , lab14.lab14c1 "
                    + " , lab14.lab14c21 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c9 ";
            String from = ""
                    + " FROM " + lab22 + " AS lab22"
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                    + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                    + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                    + "";
            for (Demographic demographic : demographics) {
                if (demographic.isEncoded()) {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";
                    select += ", demo" + demographic.getId() + ".lab63c7 as demo" + demographic.getId() + "_email";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    lab22c1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                DemographicValue demoValue = null;
                // Tipo de orden 
                if (rs.getString("lab103c1") != null) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab103c1"));
                    demoValue.setDemographic("ORDERTYPE");
                    demoValue.setEmail(rs.getString("lab103c6"));
                    order.getDemographics().add(demoValue);
                }

                // SERVICIO
                if (rs.getString("lab10c1") != null) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab10c1"));
                    demoValue.setDemographic("SERVICE");
                    demoValue.setEmail(rs.getString("lab10c10"));
                    order.getDemographics().add(demoValue);
                }

                // CLIENTE
                if (rs.getString("lab14c1") != null) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab14c1"));
                    demoValue.setDemographic("ACOUNT");
                    demoValue.setEmail(rs.getString("lab14c21"));
                    order.getDemographics().add(demoValue);
                }
                // RATE
                if (rs.getString("lab904c1") != null) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab904c1"));
                    demoValue.setDemographic("RATE");
                    demoValue.setEmail(rs.getString("lab904c9"));
                    order.getDemographics().add(demoValue);
                }

                for (Demographic demographic : demographics) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded()) {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                            demoValue.setEmail(rs.getString("demo" + demographic.getId() + "_email"));
                            order.getDemographics().add(demoValue);
                        }
                    }
                }
                return order;
            }, orderNumber);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene una lista de ordenes por fecha de creacion
     *
     * @param whereFilter
     * @param orderNumber
     * @param paramsFilter
     * @param patientId
     * @param patientIds
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<Order> list(String whereFilter, List<Object> paramsFilter, Long orderNumber, Integer patientId, String patientIds, int yearsQuery,  List<Integer> laboratorys, int idbranch) throws Exception {
        int currentYear = DateTools.dateToNumberYear(new Date());
        // Consulta de ordenes por historico:
        List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
        String lab22;
        String lab221;
        String lab57;
        
        List<Order> listOrders = new LinkedList<>();
        for (Integer year : years) {
            lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            lab221 = year.equals(currentYear) ? "lab221" : "lab221_" + year;
            boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
            tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab221) : tableExists;
            if (tableExists) {
                List<Object> params = new ArrayList<>();
                String select = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT DISTINCT  "
                        + "c.lab22c1, "
                        + "c.lab22c2, "
                        + "c.lab22c3, "
                        + "c.lab22c4, "
                        + "c.lab22c5, "
                        + "c.lab22c6, "
                        + "c.lab07c1, "
                        + "c.lab22c7, "
                        + "lab22c11, "
                        + "c.lab21c1, "
                        + "c.lab103c1, "
                        + "lab103.lab103c2, "
                        + "lab103.lab103c3, "
                        + "lab103.lab103c4, "
                        + "c.lab04c1, "
                        + "lab04.lab04c2, "
                        + "lab04.lab04c3, "
                        + "lab04.lab04c4,  "
                        + "lab21.lab21c1, "
                        + "lab21c2, "
                        + "lab21c3, "
                        + "lab21c4, "
                        + "lab21c5, "
                        + "lab21c6, "
                        + "lab21c8, "
                        + "lab21c16, "
                        + "lab21c17, "
                        + "lab80.lab80c1, "
                        + "lab80.lab80c2, "
                        + "lab80.lab80c3, "
                        + "lab80.lab80c4, "
                        + "lab80.lab80c5, "
                        + "lab21c7, "
                        + "CAST(lab21c14 AS VARCHAR(MAX)) AS lab21c14, "
                        + "lab54.lab54c1, "
                        + "lab54.lab54c2, "
                        + "lab54.lab54c3, "
                        + "lab05.lab05c1, "
                        + "lab05.lab05c10, "
                        + "lab05.lab05c4, "
                        + "lab10.lab10c1, "
                        + "lab10.lab10c7, "
                        + "lab10.lab10c2, "
                        + "lab19.lab19c1, "
                        + "lab19.lab19c2, "
                        + "lab19.lab19c3, "
                        + "lab14.lab14c1, "
                        + "lab14.lab14c2, "
                        + "lab14.lab14c3, "
                        + "(select b.lab22c1_1 from " + lab22 + " AS a INNER JOIN " + lab221 + " AS b ON a.lab22c1 = b.lab22c1_2 where lab22c1 = c.lab22c1) AS fathetOrder, "
                        + "lab904.lab904c1, "
                        + "lab904.lab904c2, "
                        + "lab904.lab904c3 ";
                String from = "FROM     " + lab22 + " AS c "
                        + " INNER JOIN " + lab57 +  " AS lab57 ON c.lab22c1 = lab57.lab22c1 "
                        + " INNER JOIN lab103 ON c.lab103c1 = lab103.lab103c1 "
                        + " INNER JOIN lab04 ON c.lab04c1 = lab04.lab04c1 "
                        + " INNER JOIN lab21 ON lab21.lab21c1 = c.lab21c1 "
                        + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                        + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 "
                        + " LEFT JOIN lab05 ON c.lab05c1 = lab05.lab05C1 "
                        + " LEFT JOIN lab10 ON c.lab10c1 = lab10.lab10C1 "
                        + " LEFT JOIN lab19 ON c.lab19c1 = lab19.lab19C1 "
                        + " LEFT JOIN lab14 ON c.lab14c1 = lab14.lab14C1 "
                        + " LEFT JOIN lab904 ON c.lab904c1 = lab904.lab904C1 ";
                String where = "WHERE c.lab07c1 != 0 AND c.lab21c1 != 0  AND (c.lab22c19 = 0 or c.lab22c19 is null)";

                try {
                    if (orderNumber != null) {
                        where += " AND c.lab22c1 = ?";
                        params.add(orderNumber);
                    }
                    if (patientId != null) {
                        where += " AND lab21.lab21c1 = ?";
                        params.add(patientId);
                    } else if (patientIds != null && !patientIds.isEmpty()) {
                        where += " AND lab21.lab21c1 IN (" + patientIds + ")";
                    }
                    
                    where += (SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));
                   
                    if (whereFilter != null) {
                        
                        where += whereFilter;
                        params.addAll(paramsFilter);
                    }
                    
                    where = where.replace("lab22.", "c.");
                    
                    

                    getJdbcTemplate().query(select + from + where, params.toArray(), (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setExternalId(rs.getString("lab22c7"));
                        order.setFatherOrder(rs.getLong("fathetOrder"));
                        try {
                            order.setDaughterOrder(getDaughterOrder(rs.getLong("lab22c1")));
                        } catch (Exception e) {
                            order.setDaughterOrder(new ArrayList<>());
                        }

                        order.setRecallNumber(rs.getString("lab22c11") == null ? null : rs.getLong("lab22c11"));

                        OrderType orderType = new OrderType();
                        orderType.setId(rs.getInt("lab103c1"));
                        orderType.setCode(rs.getString("lab103c2"));
                        orderType.setName(rs.getString("lab103c3"));
                        orderType.setColor(rs.getString("lab103c4"));
                        order.setType(orderType);

                        order.setCreatedDate(rs.getTimestamp("lab22c3"));

                        Patient patient = new Patient();
                        patient.setId(rs.getInt("lab21c1"));
                        order.setPatient(patient);

                        //PACIENTE
                        order.getPatient().setId(rs.getInt("lab21c1"));
                        order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                        order.getPatient().setOrderNumber(order.getOrderNumber());
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
                        order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                        order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                        order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                        order.getPatient().setPhoto(rs.getString("lab21c14"));

                        order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                        order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                        order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                        User user = new User();
                        user.setId(rs.getInt("lab04c1"));
                        user.setName(rs.getString("lab04c2"));
                        user.setLastName(rs.getString("lab04c3"));
                        user.setUserName(rs.getString("lab04c4"));
                        order.setLastUpdateUser(user);

                        order.setActive(rs.getInt("lab07c1") == 1);
                        order.setExternalId(rs.getString("lab22c7"));

                        if (rs.getString("lab05c1") != null) {
                            Branch branch = new Branch();
                            branch.setId(rs.getInt("lab05c1"));
                            branch.setCode(rs.getString("lab05c10"));
                            branch.setName(rs.getString("lab05c4"));
                            order.setBranch(branch);
                        }

                        if (rs.getString("lab10c1") != null) {
                            ServiceLaboratory service = new ServiceLaboratory();
                            service.setId(rs.getInt("lab10c1"));
                            service.setCode(rs.getString("lab10c7"));
                            service.setName(rs.getString("lab10c2"));
                            order.setService(service);
                        }

                        if (rs.getString("lab19c1") != null) {
                            Physician physician = new Physician();
                            physician.setId(rs.getInt("lab19c1"));
                            physician.setLastName(rs.getString("lab19c2"));
                            physician.setName(rs.getString("lab19c3"));
                            order.setPhysician(physician);
                        }

                        if (rs.getString("lab14c1") != null) {
                            Account account = new Account();
                            account.setId(rs.getInt("lab14c1"));
                            account.setNit(rs.getString("lab14c2"));
                            account.setName(rs.getString("lab14c3"));
                            order.setAccount(account);
                        }

                        if (rs.getString("lab904c1") != null) {
                            Rate rate = new Rate();
                            rate.setId(rs.getInt("lab904c1"));
                            rate.setCode("lab904c2");
                            rate.setName(rs.getString("lab904c3"));
                            order.setRate(rate);
                        }
                        listOrders.add(order);
                        return order;
                    });

                } catch (EmptyResultDataAccessException ex) {
                    return new ArrayList<>();
                }
            }
        }
        return listOrders;
    }

    /**
     * Obtiene una lista de ordenes por fecha de creacion
     *
     *
     * @param filter
     * @param whereFilter
     * @param paramsFilter
     * @param yearsQuery Años de consulta (historicos)
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<OrderList> getOrdersbyPatient(OrderSearchFilter filter, String whereFilter, List<Object> paramsFilter, int yearsQuery, List<Integer> laboratorys, int idbranch ) throws Exception {

        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);

        // Consulta de ordenes por historico:
        List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
        String lab22;
        String lab57;
        int currentYear = DateTools.dateToNumberYear(new Date());

        List<OrderList> listOrders = new LinkedList<>();
        for (Integer year : years) {
            lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab57);
            tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab22) : tableExists;
            List<Object> params = new ArrayList<>();

            if (tableExists) {

                String select = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT   DISTINCT "
                        + "lab57.lab22c1, "
                        + "lab21.lab21c1, "
                        + "lab21c2, "
                        + "lab21c3, "
                        + "lab21c4, "
                        + "lab21c5, "
                        + "lab21c6, "
                        + "(select b.lab22c1_1 from lab22 AS a INNER JOIN lab221 AS b ON a.lab22c1 = b.lab22c1_2 where lab22c1 = lab57.lab22c1) AS fathetOrder, "
                        + "lab54.lab54c1, "
                        + "lab54.lab54c2, "
                        + "lab54.lab54c3 ";

                String from = "FROM " + lab57 + " as  lab57 "
                        + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                        + " INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                        + " INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                        + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ";
                
                String where = "WHERE 1=1 "; 
                where += (SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));
                   
                if (whereFilter != null) {

                    where += whereFilter;
                    params.addAll(paramsFilter);
                }
                    
                where = where.replace("lab22c1", "lab57.lab22c1");;

                where += " AND lab22.lab07c1 != 0 AND lab21.lab21c1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ";

                try {
                    if (filter.getRecord() != null) {
                        where += " AND lab21c2 = ?";
                        params.add(Tools.encrypt(filter.getRecord()));
                    }
                    if (filter.getDocumentType() != null) {
                        where += " AND lab21.lab54c1 = ?";
                        params.add(filter.getDocumentType());
                    }

                    if (filter.getSections() != null) {
                        where += " AND lab39.lab43c1 IN(" + (filter.getSections().stream().map(section -> section.toString()).collect(Collectors.joining(","))) + ")";
                    }

                    if (filter.getGender() != null) {
                        where += " AND lab21.lab80c1 = ?";
                        params.add(filter.getGender());
                    }

                    getJdbcTemplate().query(select + from + where, params.toArray(), (ResultSet rs, int i)
                            -> {
                        OrderList order = new OrderList();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        order.setFatherOrder(rs.getLong("fathetOrder"));

                        //PACIENTE
                        SuperPatient patient = new SuperPatient();
                        patient.setId(rs.getInt("lab21c1"));
                        patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                        patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                        SuperDocumentType superDocumentType = new SuperDocumentType();

                        //PACIENTE - TIPO DE DOCUMENTO
                        superDocumentType.setId(rs.getInt("lab54c1"));
                        superDocumentType.setAbbr(rs.getString("lab54c2"));
                        superDocumentType.setName(rs.getString("lab54c3"));
                        patient.setDocumentType(superDocumentType);
                        order.setPatient(patient);

                        listOrders.add(order);
                        return order;
                    });
                } catch (EmptyResultDataAccessException ex) {
                    return new ArrayList<>();
                }

            }

        }
        return listOrders;
    }

    /**
     * Obtiene una lista de ordenes por fecha de creacion
     *
     *
     * @param filter
     * @param whereFilter
     * @param paramsFilter
     * @param yearsQuery Años de consulta (historicos)
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<OrderList> getOrdersbyPatientStorage(OrderSearchFilter filter, String whereFilter, List<Object> paramsFilter, int yearsQuery) throws Exception {

        List<String> listOrdersString = new LinkedList<>();

        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);

        // Consulta de ordenes por historico:
        List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(y - yearsQuery), String.valueOf(y));
        String lab22;
        int currentYear = DateTools.dateToNumberYear(new Date());

        List<OrderList> listOrders = new LinkedList<>();
        for (Integer year : years) {
            lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            List<Object> params = new ArrayList<>();
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT "
                    + "lab22.lab22c1 "
                    + " ,lab24.lab24c2 "
                    + " ,lab24.lab24c9 "
                    + " ,lab56.lab56c3 ";

            String from = "FROM " + lab22 + " as  lab22 "
                    + " INNER JOIN lab11 ON lab22.lab22c1 = lab11.lab22c1 "
                    + " INNER JOIN lab24 ON lab24.lab24c1 = lab11.lab24c1 "
                    + " INNER JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                    + " INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ";

            String where = "WHERE lab22.lab07c1 != 0 AND lab21.lab21c1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ";

            try {
                if (filter.getRecord() != null) {
                    where += " AND lab21c2 = ?";
                    params.add(Tools.encrypt(filter.getRecord()));
                }
                if (filter.getDocumentType() != null) {
                    where += " AND lab21.lab54c1 = ?";
                    params.add(filter.getDocumentType());
                }

                if (filter.getGender() != null) {
                    where += " AND lab21.lab80c1 = ?";
                    params.add(filter.getGender());
                }

                if (whereFilter != null) {
                    where += whereFilter.replace("lab22c1", "lab57.lab22c1");
                    params.addAll(paramsFilter);
                }

                getJdbcTemplate().query(select + from + where, params.toArray(), (ResultSet rs, int i)
                        -> {
                    OrderList order = new OrderList();
                    order.setOrderNumber(rs.getLong("lab22c1"));

                    Sample sample = new Sample();
                    sample.setName(rs.getString("lab24c2"));
                    sample.setCodesample(rs.getString("lab24c9"));
                    String Imabas64 = "";
                    byte[] ImaBytes = rs.getBytes("lab56c3");
                    if (ImaBytes != null) {
                        Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                    }

                    Container container = new Container();
                    container.setImage(Imabas64);
                    sample.setContainer(container);

                    if (!listOrdersString.contains(order.getOrderNumber().toString())) {
                        listOrdersString.add(order.getOrderNumber().toString());
                        listOrders.add(order);
                    }

                    listOrders.get(listOrdersString.indexOf(order.getOrderNumber().toString())).getSamples().add(sample);
                    return order;
                });
            } catch (EmptyResultDataAccessException ex) {
                return new ArrayList<>();
            }
        }
        return listOrders;
    }

    /**
     * Obtiene los usuarios con las fimras q han validado una orden.
     *
     * @param idOrder
     * @return Lista de usuarios
     * @throws Exception Error en base de datos
     */
    default List<User> getUserValidate(long idOrder) throws Exception {

        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        // Año actual
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        // Según el año de la orden, este me indicará en que tabla del historicó buscarla
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String select = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT lab04c1, lab04c12  ";
        String from = "FROM "
                + lab57
                + " as lab57 "
                + " INNER JOIN lab04 ON lab04.lab04c1 = lab57.lab57c19 ";
        String where = "WHERE lab22c1  = " + idOrder;

        try {
            return getJdbcTemplate().query(select + from + where, (ResultSet rs, int i)
                    -> {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));

                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c12");
                if (photoBytes != null) {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                user.setPhoto(photo64);

                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    default List<Order> get(int shortDate, final List<Demographic> demographics) throws Exception {

        String select = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   "
                + "lab22.lab22c1, lab22.lab22c2, lab22.lab22c3, lab22.lab22c4,lab22.lab22c5, lab22.lab22c6, "
                + "lab22.lab07c1, lab22.lab22c7, "
                + "lab22.lab21c1, "
                + "lab22.lab103c1, lab103.lab103c2, lab103.lab103c3, lab103.lab103c4, "
                + "lab22.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4,  "
                + "lab05.lab05c1, lab05.lab05c10, lab05.lab05c4,"
                + "lab10.lab10c1, lab10.lab10c7, lab10.lab10c2, "
                + "lab19.lab19c1, lab19.lab19c2, lab19.lab19c3, "
                + "lab14.lab14c1, lab14.lab14c2, lab14.lab14c3, "
                + "lab904.lab904c1, lab904.lab904c2, lab904.lab904c3 ";
        String from = "FROM     lab22 "
                + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 ";

        try {
            for (Demographic demographic : demographics) {
                if (demographic.isEncoded()) {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }

            String where = ""
                    + " WHERE   lab22.lab07C1 = 1 AND lab22c3 = ?  AND (lab22c19 = 0 or lab22c19 is null) ";
            return getJdbcTemplate().query(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));

                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                orderType.setCode(rs.getString("lab103c2"));
                orderType.setName(rs.getString("lab103c3"));
                orderType.setColor(rs.getString("lab103c4"));
                order.setType(orderType);

                order.setCreatedDate(rs.getTimestamp("lab22c3"));

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                order.setLastUpdateUser(user);

                order.setActive(rs.getInt("lab07c1") == 1);
                order.setExternalId(rs.getString("lab22c7"));

                if (rs.getString("lab05c1") != null) {
                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                }

                if (rs.getString("lab10c1") != null) {
                    ServiceLaboratory service = new ServiceLaboratory();
                    service.setId(rs.getInt("lab10c1"));
                    service.setCode(rs.getString("lab10c7"));
                    service.setName(rs.getString("lab10c2"));
                    order.setService(service);
                }

                if (rs.getString("lab19c1") != null) {
                    Physician physician = new Physician();
                    physician.setId(rs.getInt("lab19c1"));
                    physician.setLastName(rs.getString("lab19c2"));
                    physician.setName(rs.getString("lab19c3"));
                    order.setPhysician(physician);
                }

                if (rs.getString("lab14c1") != null) {
                    Account account = new Account();
                    account.setId(rs.getInt("lab14c1"));
                    account.setNit(rs.getString("lab14c2"));
                    account.setName(rs.getString("lab14c3"));
                    order.setAccount(account);
                }

                if (rs.getString("lab904c1") != null) {
                    Rate rate = new Rate();
                    rate.setId(rs.getInt("lab904c1"));
                    rate.setCode("lab904c2");
                    rate.setName(rs.getString("lab904c3"));
                    order.setRate(rate);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded()) {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                        }
                    } else {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                return order;
            }, shortDate);
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca una orden por el numero de orden
     *
     * @param orderNumber Numero de orden
     * @param demographics Lista de
     * {net.cltech.enterprisent.domain.masters.demographic.Demographic} con los
     * demograficos configurados para la orden activos
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default Order get(long orderNumber, final List<Demographic> demographics) throws Exception
    {
        try
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab22.lab22c2 "
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
                    + " , lab04.lab04c2 "
                    + " , lab04.lab04c3 "
                    + " , lab04.lab04c4 "
                    + " , lab22.lab07c1 "
                    + " , lab05.lab05c1 "
                    + " , lab05.lab05c10 "
                    + " , lab05.lab05c4 "
                    + " , lab10.lab10c1 "
                    + " , lab10.lab10c7 "
                    + " , lab10.lab10c8 "
                    + " , lab10.lab10c2 "
                    + " , lab19.lab19c1 "
                    + " , lab19.lab19c2 "
                    + " , lab19.lab19c3 "
                    + " , lab19.lab19c22 "
                    + " , lab14.lab14c1 "
                    + " , lab14.lab14c2 "
                    + " , lab14.lab14c3 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 "
                    + " , lab22.lab22c19 "
                    + " , lab04c1_1.lab04c1 AS userCreateId"
                    + " , lab04c1_1.lab04c2 AS userCreateName"
                    + " , lab04c1_1.lab04c3 AS userCreateLastName"
                    + " , lab04c1_1.lab04c4 AS userCreateNick";
            String from = ""
                    + " FROM " + lab22 + " AS lab22"
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                    + " INNER JOIN lab04 lab04c1_1 ON lab04c1_1.lab04c1 = lab22.lab04c1_1"
                    + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                    + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                    + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                    + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                    + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                    + "";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    lab22c1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i)
                    ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
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

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                // Usuario que creo la orden
                User user = new User();
                user.setId(rs.getInt("userCreateId"));
                user.setName(rs.getString("userCreateName"));
                user.setLastName(rs.getString("userCreateLastName"));
                user.setUserName(rs.getString("userCreateNick"));
                order.setCreateUser(user);
                // Ultimo usuario que modifico la orden
                User userUpdate = new User();
                userUpdate.setId(rs.getInt("lab04c1"));
                userUpdate.setName(rs.getString("lab04c2"));
                userUpdate.setLastName(rs.getString("lab04c3"));
                userUpdate.setUserName(rs.getString("lab04c4"));
                order.setLastUpdateUser(userUpdate);

                order.setActive(rs.getInt("lab07c1") == 1);
                order.setExternalId(rs.getString("lab22c7"));

                if (rs.getString("lab05c1") != null)
                {
                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                }

                if (rs.getString("lab10c1") != null)
                {
                    ServiceLaboratory service = new ServiceLaboratory();
                    service.setId(rs.getInt("lab10c1"));
                    service.setCode(rs.getString("lab10c7"));
                    service.setHospitalSampling(rs.getInt("lab10c8") == 1);
                    service.setName(rs.getString("lab10c2"));
                    order.setService(service);
                }

                if (rs.getString("lab19c1") != null)
                {
                    Physician physician = new Physician();
                    physician.setId(rs.getInt("lab19c1"));
                    physician.setCode(rs.getString("lab19c22"));
                    physician.setLastName(rs.getString("lab19c3"));
                    physician.setName(rs.getString("lab19c2") + " " + rs.getString("lab19c3"));
                    order.setPhysician(physician);
                }

                if (rs.getString("lab14c1") != null)
                {
                    Account account = new Account();
                    account.setId(rs.getInt("lab14c1"));
                    account.setNit(rs.getString("lab14c2"));
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
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                return order;
            }, orderNumber);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Busca ordenes en el sistema por los filtros enviados
     *
     * @param documentType
     * @param encryptedPatientId Historia encriptada, null en caso de no
     * requerir filtro
     * @param encryptedLastName Apellido encriptado, null en caso de no requerir
     * filtro
     * @param encryptedSurName Segundo Apellido encriptad1, null en caso de no
     * requerir filtro
     * @param encryptedName1 Nombre encriptado, null en caso de no requerir
     * filtro
     * @param encryptedName2 Segundo encriptado, null en caso de no requerir
     * filtro
     * @param sex Id sexo, null en caso de no requerir filtro
     * @param birthday Fecha de Nacimiento, null en caso de no requerir filtro
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @param yearsQuery Años de consulta (historicos)
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacia si no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<OrderSearch> getByPatientInfo(Integer documentType, String encryptedPatientId, String encryptedLastName, String encryptedSurName, String encryptedName1, String encryptedName2, Integer sex, Date birthday, int branch, int yearsQuery) throws Exception {
        try {
            List<OrderSearch> listOrders = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                String query = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT   lab22.lab22c1 "
                        + " , lab21.lab21c1 "
                        + " , lab54.lab54c1 "
                        + " , lab54.lab54c3 "
                        + " , lab21.lab21c2 "
                        + " , lab21.lab21c3 "
                        + " , lab21.lab21c4 "
                        + " , lab21.lab21c5 "
                        + " , lab21.lab21c6 "
                        + " , lab80.lab80c3 "
                        + " , lab21.lab21c7 "
                        + "FROM      " + lab22 + " as lab22 "
                        + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                        + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                        + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                        + "WHERE    lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) "
                        + (((documentType != null && documentType != 0) ? " AND Lab21.Lab54C1 = " + documentType + " " : ""))
                        + (((documentType != null && documentType == 0) ? " AND Lab21.Lab54C1 != 1 " : ""))
                        + (encryptedPatientId != null ? " AND lab21.lab21c2 = ? " : "")
                        + (encryptedLastName != null ? " AND lab21.lab21c5 = ? " : "")
                        + (encryptedSurName != null ? " AND lab21.lab21c6 = ? " : "")
                        + (encryptedName1 != null ? " AND lab21.lab21c3 = ? " : "")
                        + (encryptedName2 != null ? " AND lab21.lab21c4 = ? " : "")
                        + (sex != null ? " AND lab21.lab80c1 = ? " : "")
                        + (birthday != null ? " AND lab21.lab21c7 = ? " : "")
                        + (branch != -1 ? " AND lab22.lab05c1 = ? " : "");

                List parameters = new ArrayList(0);
                if (encryptedPatientId != null) {
                    parameters.add(encryptedPatientId);
                }
                if (encryptedLastName != null) {
                    parameters.add(encryptedLastName);
                }
                if (encryptedSurName != null) {
                    parameters.add(encryptedSurName);
                }
                if (encryptedName1 != null) {
                    parameters.add(encryptedName1);
                }
                if (encryptedName2 != null) {
                    parameters.add(encryptedName2);
                }
                if (sex != null) {
                    parameters.add(sex);
                }
                if (birthday != null) {
                    parameters.add(birthday);
                }
                if (branch != -1) {
                    parameters.add(branch);
                }
                getJdbcTemplate().query(query, (ResultSet rs, int rowNum)
                        -> {
                    OrderSearch order = new OrderSearch();
                    order.setOrder(rs.getLong("lab22c1"));
                    order.setPatientIdDB(rs.getInt("lab21c1"));
                    order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.setDocumentTypeId(rs.getInt("lab54c1"));
                    order.setDocumentType(rs.getString("lab54c3"));
                    order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.setSex(rs.getInt("lab80c3"));
                    order.setBirthday(rs.getTimestamp("lab21c7"));
                    listOrders.add(order);
                    return order;
                }, parameters.toArray());

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error("Error en consulta de ordenes:" + ex);
            return new ArrayList(0);
        }
    }

    /**
     * Consulta una orden por numero de orden
     *
     * @param order Numero de orden
     * @param branch Id Sede, -1 en caso de no querer hacer filtro por sede
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}, null
     * en caso de no encontrar datos
     * @throws Exception Error en base de datos
     */
    default OrderSearch getByOrder(long order, int branch) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            // Año actual
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            // Según el año de la orden, este me indicará en que tabla del historicó buscarla
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab21.lab21c1 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c3 "
                    + " , lab21.lab21c2 "
                    + " , lab21.lab21c3 "
                    + " , lab21.lab21c4 "
                    + " , lab21.lab21c5 "
                    + " , lab21.lab21c6 "
                    + " , lab80.lab80c3 "
                    + " , lab21.lab21c7 "
                    + " FROM " + lab22 + " AS lab22"
                    + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "WHERE    lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) "
                    + "         AND lab22.lab22c1 = ? "
                    + (branch != -1 ? " AND lab22.lab05c1 = " + branch : "");
            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int rowNum)
                    -> {
                OrderSearch orderR = new OrderSearch();
                orderR.setOrder(rs.getLong("lab22c1"));
                orderR.setPatientIdDB(rs.getInt("lab21c1"));
                orderR.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                orderR.setDocumentTypeId(rs.getInt("lab54c1"));
                orderR.setDocumentType(rs.getString("lab54c3"));
                orderR.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                orderR.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                orderR.setName1(Tools.decrypt(rs.getString("lab21c3")));
                orderR.setName2(Tools.decrypt(rs.getString("lab21c4")));
                orderR.setSex(rs.getInt("lab80c3"));
                orderR.setBirthday(rs.getTimestamp("lab21c7"));
                return orderR;
            }, order);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene ordenes buscandolas por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacio en caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    default List<OrderSearch> getByEntryDate(int date, int branch) throws Exception {
        try {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(date));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1"
                    + ", lab21.lab21c1"
                    + ", lab54.lab54c1"
                    + ", lab54.lab54c3"
                    + ", lab21.lab21c2"
                    + ", lab21.lab21c3"
                    + ", lab21.lab21c4"
                    + ", lab21.lab21c5"
                    + ", lab21.lab21c6"
                    + ", lab80.lab80c3"
                    + ", lab21.lab21c7"
                    + " FROM  " + lab22 + " as lab22 "
                    + "INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "WHERE lab22.lab07c1 = 1 "
                    + "AND lab22.lab22c2 = ? "
                    + "AND (lab22c19 = 0 or lab22c19 is null) "
                    + (branch != -1 ? " AND lab22.lab05c1 = " + branch : "");
            return getJdbcTemplate().query(query, (ResultSet rs, int rowNum)
                    -> {
                OrderSearch orderR = new OrderSearch();
                orderR.setOrder(rs.getLong("lab22c1"));
                orderR.setPatientIdDB(rs.getInt("lab21c1"));
                orderR.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                orderR.setDocumentTypeId(rs.getInt("lab54c1"));
                orderR.setDocumentType(rs.getString("lab54c3"));
                orderR.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                orderR.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                orderR.setName1(Tools.decrypt(rs.getString("lab21c3")));
                orderR.setName2(Tools.decrypt(rs.getString("lab21c4")));
                orderR.setSex(rs.getInt("lab80c3"));
                orderR.setBirthday(rs.getTimestamp("lab21c7"));
                return orderR;
            }, date);
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList(0);
        }
    }

    /**
     * Obtiene el numero de ordenes buscandolas por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderNumSearch},
     * vacio en caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    default OrderNumSearch getByEntryDateN(int date, int branch) throws Exception {
        try {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab21.lab21c1 "
                    + " , lab21.lab21c7 "
                    + "FROM     lab22 "
                    + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "WHERE    lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) "
                    + "         AND lab22.lab22c2 = ? "
                    + (branch != -1 ? " AND lab22.lab05c1 = " + branch : "");
            List<Long> lista = getJdbcTemplate().query(query, (ResultSet rs, int rowNum)
                    -> {
                Long o = rs.getLong("lab22c1");
                return o;
            }, date);

            OrderNumSearch order = new OrderNumSearch();
            order.setOrders(lista);
            return order;

        } catch (EmptyResultDataAccessException ex) {
            OrderNumSearch order = new OrderNumSearch();
            order.setOrders(new ArrayList(0));
            return order;
        }
    }

    /**
     * Obtiene las ordenes buscandolas por el numero de historia
     *
     * @param encryptedPatientId Historia encriptada
     * @param demographics
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<Order> getByPatientId(String encryptedPatientId, List<Demographic> demographics) throws Exception {
        try {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab22.lab22c2 "
                    + " , lab22.lab103c1 "
                    + " , lab103.lab103c2 "
                    + " , lab103.lab103c3 "
                    + " , lab103.lab103c4 "
                    + " , lab22.lab22c3 "
                    + " , lab22.lab21c1 "
                    + " , lab22.lab22c4 "
                    + " , lab22.lab22c5 "
                    + " , lab22.lab22c6 "
                    + " , lab22.lab04c1 "
                    + " , lab04.lab04c2 "
                    + " , lab04.lab04c3 "
                    + " , lab04.lab04c4 "
                    + " , lab22.lab07c1 "
                    + " , lab22.lab22c7 "
                    + " , lab22.lab22c13 "
                    + " , lab05.lab05c1 "
                    + " , lab05.lab05c10 "
                    + " , lab05.lab05c4 "
                    + " , lab10.lab10c1 "
                    + " , lab10.lab10c7 "
                    + " , lab10.lab10c2 "
                    + " , lab19.lab19c1 "
                    + " , lab19.lab19c2 "
                    + " , lab19.lab19c3 "
                    + " , lab14.lab14c1 "
                    + " , lab14.lab14c2 "
                    + " , lab14.lab14c3 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 ";
            String from = ""
                    + " FROM     lab22 "
                    + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                    + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                    + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                    + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                    + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                    + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                    + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                    + "";
            for (Demographic demographic : demographics) {
                if (demographic.isEncoded()) {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else {
                    select += ", Lab22.lab_demo_" + demographic.getId();
                }
            }
            String where = ""
                    + " WHERE  lab22.lab07C1 = 1 AND  lab21.lab21c2 = ?  AND (lab22c19 = 0 or lab22c19 is null) ";
            return getJdbcTemplate().query(select + from + where, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));
                order.setTurn(rs.getString("lab22c13"));

                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                orderType.setCode(rs.getString("lab103c2"));
                orderType.setName(rs.getString("lab103c3"));
                orderType.setColor(rs.getString("lab103c4"));
                order.setType(orderType);

                order.setCreatedDate(rs.getTimestamp("lab22c3"));

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                order.setLastUpdateUser(user);

                order.setActive(rs.getInt("lab07c1") == 1);
                order.setExternalId(rs.getString("lab22c7"));

                if (rs.getString("lab05c1") != null) {
                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                }

                if (rs.getString("lab10c1") != null) {
                    ServiceLaboratory service = new ServiceLaboratory();
                    service.setId(rs.getInt("lab10c1"));
                    service.setCode(rs.getString("lab10c7"));
                    service.setName(rs.getString("lab10c2"));
                    order.setService(service);
                }

                if (rs.getString("lab19c1") != null) {
                    Physician physician = new Physician();
                    physician.setId(rs.getInt("lab19c1"));
                    physician.setLastName(rs.getString("lab19c2"));
                    physician.setName(rs.getString("lab19c3"));
                    order.setPhysician(physician);
                }

                if (rs.getString("lab14c1") != null) {
                    Account account = new Account();
                    account.setId(rs.getInt("lab14c1"));
                    account.setNit(rs.getString("lab14c2"));
                    account.setName(rs.getString("lab14c3"));
                    order.setAccount(account);
                }

                if (rs.getString("lab904c1") != null) {
                    Rate rate = new Rate();
                    rate.setId(rs.getInt("lab904c1"));
                    rate.setCode("lab904c2");
                    rate.setName(rs.getString("lab904c3"));
                    order.setRate(rate);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics) {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded()) {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                        }
                    } else {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                return order;
            }, encryptedPatientId);
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Crea una orden en el sistema
     *
     * @param order
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @param listRequeridDemographis lista de demograficos obligatorios pero no
     * requeridos
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden asignado
     * @throws Exception Error en base de datos
     */
    default Order create(Order order, List<DemographicBranch> listRequeridDemographis) throws Exception {
        try {

            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab22");
            HashMap<String, Object> parameters = new HashMap<>(0);
            parameters.put("lab22c1", order.getOrderNumber() != null ? order.getOrderNumber() : 000000000000);
            parameters.put("lab22c2", order.getCreatedDateShort() != null ? order.getCreatedDateShort() : 0);
            parameters.put("lab103c1", order.getType() != null ? order.getType().getId() : 0);
            parameters.put("lab22c3", new Timestamp(order.getCreatedDate() != null ? order.getCreatedDate().getTime() : new Date().getTime()));
            parameters.put("lab21c1", order.getPatient() != null ? order.getPatient().getId() : 0);
            parameters.put("lab22c4", order.isHomebound() ? 1 : 0);
            parameters.put("lab22c5", order.getMiles() != null ? order.getMiles() : 0);
            parameters.put("lab22c6", new Timestamp(order.getLastUpdateDate() != null ? order.getLastUpdateDate().getTime() : new Date().getTime()));
            parameters.put("lab04c1", order.getLastUpdateUser() != null ? order.getLastUpdateUser().getId() : 0);
            parameters.put("lab07c1", 1);
            parameters.put("lab22c7", order.getExternalId() != null ? order.getExternalId() : null);
            parameters.put("lab05c1", order.getBranch() != null ? order.getBranch().getId() : null);
            parameters.put("lab10c1", order.getService() != null ? order.getService().getId() : null);
            parameters.put("lab19c1", order.getPhysician() != null ? order.getPhysician().getId() : null);
            parameters.put("lab14c1", order.getAccount() != null ? order.getAccount().getId() : null);
            parameters.put("lab904c1", order.getRate() != null ? order.getRate().getId() : null);
            parameters.put("lab22c8", order.getTests() != null ? order.getTests().size() : 0);
            parameters.put("lab22c13", order.getTurn() != null ? order.getTurn() : null);
            parameters.put("lab04c1_1", order.getLastUpdateUser() != null ? order.getLastUpdateUser().getId() : 0);
            parameters.put("lab22c10", order.isInconsistency() ? 1 : 0);
            parameters.put("lab22c19", order.getHasAppointment());

            if (order.getDemographics() != null && !order.getDemographics().isEmpty()) {
                order.getDemographics().forEach((demographic)
                        -> {
                    parameters.put("lab_demo_" + demographic.getIdDemographic(), demographic.isEncoded() ? demographic.getCodifiedId() : demographic.getNotCodifiedValue());
                });
            }

            if (listRequeridDemographis != null && listRequeridDemographis.isEmpty()) {
                listRequeridDemographis.forEach((demographic)
                        -> {
                    if (demographic.getEncoded()) {
                        parameters.put("lab_demo_" + demographic.getId(), demographic.getDefaultValue());
                    } else {
                        parameters.put("lab_demo_" + demographic.getId(), demographic.getDefaultValue());
                    }
                });
            }
            int rows = insert.execute(parameters);
            return rows > 0 ? order : null;
        } catch (Exception e) {
            OrderCreationLog.info("ORDER : " + order.getOrderNumber() + "ERROR " + e);
            return null;
        }

    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> listOderingfixed() throws Exception {
        try {
            return getJdbcTemplate().query(""
                    + "SELECT lab62c1"
                    + ", lab195c1"
                    + " FROM lab195 ", (ResultSet rs, int i)
                    -> {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setOrdering(rs.getShort("lab195c1"));
                return demographic;
            });
        } catch (Exception ex) {
            return new ArrayList<>(0);
        }
    }

    default List<Demographic> listOderingH() throws Exception {
        try {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab62c5"
                    + ", lab62c6"
                    + ", lab62c7"
                    + ", lab62c8"
                    + ", lab62c9"
                    + ", lab62c10"
                    + ", lab62c11"
                    + ", lab62c12"
                    + ", lab62.lab07c1"
                    + ", lab62.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab62c13"
                    + ", lab62c14"
                    + ", lab63c1"
                    + " FROM lab62 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab62.lab04c1 "
                    + "WHERE lab62.lab07c1 = 1 AND lab62c3 = 'H' ", (ResultSet rs, int i)
                    -> {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrdering(rs.getShort("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);

                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));

                demographic.setState(rs.getInt("lab07c1") == 1);
                demographic.setModify(rs.getInt("lab62c13") == 1);
                demographic.setDefaultValueRequired(rs.getString("lab62c14"));
                demographic.setDemographicItem(rs.getInt("lab63c1"));

                demographic.setItem(null);
                demographic.setValue(null);
                demographic.setCode(null);
                demographic.setSource(rs.getString("lab62c3"));
                demographic.setType(rs.getInt("lab62c4"));
                demographic.setCoded(rs.getBoolean("lab62c4"));
                return demographic;
            });
        } catch (Exception ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    default List<Demographic> listOderingO() throws Exception {
        try {
            return getJdbcTemplate().query(""
                    + "SELECT lab62.lab62c1"
                    + ", lab62c2"
                    + ", lab62c3"
                    + ", lab62c4"
                    + ", lab62c5"
                    + ", lab62c6"
                    + ", lab62c7"
                    + ", lab62c8"
                    + ", lab62c9"
                    + ", lab62c10"
                    + ", lab62c11"
                    + ", lab62c12"
                    + ", lab62.lab07c1"
                    + ", lab62.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab62c13"
                    + ", lab62c14"
                    + ", lab63c1"
                    + " FROM lab62 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab62.lab04c1 "
                    + "WHERE lab62.lab07c1 = 1 AND lab62c3 = 'O' ", (ResultSet rs, int i)
                    -> {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrdering(rs.getShort("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);

                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));

                demographic.setState(rs.getInt("lab07c1") == 1);
                demographic.setModify(rs.getInt("lab62c13") == 1);
                demographic.setDefaultValueRequired(rs.getString("lab62c14"));
                demographic.setDemographicItem(rs.getInt("lab63c1"));

                demographic.setItem(null);
                demographic.setValue(null);
                demographic.setCode(null);
                demographic.setSource(rs.getString("lab62c3"));
                demographic.setType(rs.getInt("lab62c4"));
                demographic.setCoded(rs.getBoolean("lab62c4"));
                return demographic;
            });
        } catch (Exception ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza una orden en el sistema
     *
     * @param order
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden actualizado
     * @throws Exception Error en base de datos
     */
    default Order update(Order order) throws Exception {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

        String update = ""
                + "UPDATE  " + lab22
                + " SET lab103c1 = ? "
                + ", lab21c1 = ? "
                + ", lab22c4 = ? "
                + ", lab22c5 = ? "
                + ", lab22c6 = ? "
                + ", lab04c1 = ? "
                + ", lab07c1 = ? "
                + ", lab22c7 = ? "
                + ", lab05c1 = ? "
                + ", lab10c1 = ? "
                + ", lab19c1 = ? "
                + ", lab14c1 = ? "
                + ", lab904c1 = ? "
                + ", lab22c13 = ? "
                + ", lab22c20 = 0 ";
        Object[] args = new Object[15 + (order.getDemographics() != null ? order.getDemographics().size() : 0)];
        args[0] = order.getType().getId();
        args[1] = order.getPatient() != null ? order.getPatient().getId() : 0;
        args[2] = order.isHomebound() ? 1 : 0;
        args[3] = order.getMiles() != null ? order.getMiles() : 0;
        args[4] = new Timestamp(order.getLastUpdateDate().getTime());
        args[5] = order.getLastUpdateUser() != null ? order.getLastUpdateUser().getId() : 0;
        args[6] = 1;
        args[7] = order.getExternalId() != null ? order.getExternalId() : 0;
        args[8] = order.getBranch() != null ? order.getBranch().getId() : null;
        args[9] = order.getService() != null ? order.getService().getId() : null;
        args[10] = order.getPhysician() != null ? order.getPhysician().getId() : null;
        args[11] = order.getAccount() != null ? order.getAccount().getId() : null;
        args[12] = order.getRate() != null ? order.getRate().getId() : null;
        args[13] = order.getTurn();

        int index = 14;
        for (DemographicValue demographic : order.getDemographics()) {
            update += ", lab_demo_" + demographic.getIdDemographic() + " = ? ";
            args[index] = demographic.isEncoded() ? demographic.getCodifiedId() : demographic.getNotCodifiedValue();
            index++;
        }

        update += " WHERE lab22c1 = ? ";
        args[index] = order.getOrderNumber();
        int affectedRows = getJdbcTemplate().update(update, args);
        return affectedRows > 0 ? order : null;
    }

    /**
     * Actualiza historia del paciente
     *
     * @param order Orden con id del paciente
     *
     * @return registros afectados
     * @throws Exception
     */
    default int updatePatient(Order order) throws Exception {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

        String update = ""
                + "UPDATE    " + lab22
                + " SET      lab21c1 = ?, lab22c20 = 0 "
                + " WHERE lab22c1 = ? ";
        return getJdbcTemplate().update(update, order.getPatient().getId(), order.getOrderNumber());
    }

    /**
     * Actualiza el estado de la orden
     *
     * @param orders lista de ordenes
     * @param state estado a actualizar
     *
     * @return Lista de ordenes actualizadas
     * @throws Exception
     */
    default List<Order> updateOrderState(List<Order> orders, LISEnum.ResultOrderState state) throws Exception {

        List<Object[]> parameters = new ArrayList<>(0);
        Integer year = Tools.YearOfOrder(String.valueOf(orders.get(0).getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

        String query = "UPDATE " + lab22 + " SET lab22c9 = lab07c1, lab07c1 = ?, lab22c20 = 0 WHERE lab22c1 = ?";
        for (Order order : orders) {

            parameters.add(new Object[]{
                state.getValue(),
                order.getOrderNumber(),});

            order.setState(state.getValue());
        }
        getJdbcTemplate().batchUpdate(query, parameters);
        return orders;
    }

    /**
     * Actualiza el estado de la orden al estado anterior y estable el estado
     * anterior
     *
     * @param orders lista de ordenes
     *
     * @return Lista de ordenes actualizadas
     * @throws Exception
     */
    default List<Order> updateToPreviousState(List<Order> orders) throws Exception {
        List<Object[]> parameters = new ArrayList<>(0);
        String query = "UPDATE lab22 SET lab07c1 = lab22c9, lab22c9=?, lab22c20 = 0 WHERE lab22c1 = ?";

        for (Order order : orders) {
            parameters.add(new Object[]{
                order.getState(),
                order.getOrderNumber()
            });

            order.setState(LISEnum.ResultOrderState.ORDERED.getValue());
        }
        getJdbcTemplate().batchUpdate(query, parameters);
        return orders;
    }

    /**
     * Obtiene el precio de una examen por tarifa y la vigencia activa
     *
     * @param test Id del examen
     * @param rate Id de la tarifa
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default BigDecimal getPriceTest(int test, int rate) throws Exception {
        try {

            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab55c1 FROM lab55 "
                    + "WHERE lab39c1 = ? AND lab904c1 = ?",
                    new Object[]{
                        test, rate
                    }, (ResultSet rs, int i)
                    -> {
                return rs.getBigDecimal("lab55c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Obtiene la tarifa de una orden
     *
     * @param order
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default int getRateOrder(Long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab904c1 FROM  " + lab22 + " as lab22 "
                    + "WHERE lab22c1 = ?",
                    new Object[]{
                        order
                    }, (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab904c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return -1;
        }
    }

    /**
     * Obtiene el tipo de orden de una orden
     *
     * @param order
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default int getTypeOrder(Long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab103c1 FROM  " + lab22 + " as lab22 "
                    + "WHERE lab103c1 = ?",
                    new Object[]{
                        order
                    }, (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab103c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return -1;
        }
    }

    /**
     * Obtiene la cantidad de ordenes creadas entre la orden inicial y el numero
     * maximo segun la configuracion
     *
     * @param init Orden Inicial
     * @param end Orden Final
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default Integer getQuantityOrderCreated(Long init, Long end) throws Exception {
        try {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT COUNT(*) AS quantity FROM lab22 "
                    + "WHERE lab22c1 BETWEEN ? AND ?  AND (lab22c19 = 0 or lab22c19 is null) ",
                    new Object[]{
                        init, end
                    }, (ResultSet rs, int i)
                    -> {
                return rs.getInt("quantity");
            });
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    /**
     * Obtiene los examenes para el talon de reclamancion
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.TicketTest}
     * @throws Exception Error en base de datos
     */
    default List<TicketTest> getTicketTest(long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   Lab39.Lab39C1 "
                    + " , Lab39.Lab39C2 "
                    + " , Lab39.Lab39C3 "
                    + " , Lab39.Lab39C4 "
                    + " , Lab39.Lab39C16 "
                    + " , Lab39.Lab39C17 "
                    + " , Lab39.Lab39C37 "
                    + " , lab39.lab39c52 "
                    + " , lab57.lab57c56 "
                    + " , lab57c15 "
                    + " , lab39c64 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "         INNER JOIN Lab39 ON Lab57.Lab39C1 = Lab39.Lab39C1 "
                    + "WHERE    Lab57.Lab22C1 = ? "
                    + "         AND Lab57.Lab57C14 IS NULL ",
                    new Object[]{
                        order
                    }, (ResultSet rs, int i)
                    -> {
                TicketTest test = new TicketTest();
                test.setId(rs.getInt("Lab39C1"));
                test.setCode(rs.getString("Lab39C2"));
                test.setAbbr(rs.getString("Lab39C3"));
                test.setName(rs.getString("Lab39C4"));
                test.setDeliveryDays(rs.getInt("Lab39C16"));
                test.setProccessDays(rs.getString("Lab39C17"));
                test.setType(rs.getInt("Lab39C37"));
                test.setInformedConsent(rs.getBoolean("lab39c52"));
                if (rs.getString("lab57c56") != null) {
                    test.setDateDelivery(rs.getTimestamp("lab57c56"));
                }
                test.setIdPackage(rs.getInt("lab57c15"));
                test.setDeployPackage(rs.getInt("lab39c64"));

                return test;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta un nuevo comentario a la orden
     *
     * @param order Orden
     * @param comment Comentario
     * @param user Id Usuario
     * @param date Fecha
     * @return Id generado
     * @throws Exception Error en base de datos
     */
    default int insertComment(long order, String comment, int user, Date date) throws Exception {
//        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("Lab60");
//        Map<String, Object> parameters = new HashMap<>(0);
//        parameters.put("Lab22C1", order);
//        parameters.put("Lab60C2", new Timestamp(date.getTime()));
//        parameters.put("Lab04C1", user);
//        parameters.put("Lab60C3", comment);
        return 1;
    }

    /**
     * Actualiza el comentario de la orden
     *
     * @param order Orden
     * @param comment Comentario
     * @param user Usuario
     * @param date Fecha
     * @return Numero de registros actualizados
     * @throws Exception Error en base de datos
     */
    default int updateComment(long order, String comment, int user, Date date) throws Exception {
        return getJdbcTemplate().update(""
                + "UPDATE   Lab60 "
                + "SET      Lab60C3 = ? "
                + " , Lab60C2 = ? "
                + " , Lab04C1 = ? "
                + "WHERE    Lab22C1 = ? ",
                comment,
                new Timestamp(date.getTime()),
                user,
                order);
    }

    /**
     * Obtiene el comentario de la orden
     *
     * @param order Numero de orden
     * @return Comentario o null si no existe comentario
     * @throws Exception Error en base de datos
     */
    default String getComment(long order) throws Exception {
        try {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   Lab60C3 "
                    + "FROM     Lab60 "
                    + "WHERE    Lab22C1 = ? ",
                    (ResultSet rs, int numRow)
                    -> {
                return rs.getString("Lab60C3");
            },
                    order);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    default List<OrderSearch> getRecall(long order) throws Exception {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c1 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c2 "
                + " , lab54.lab54c3 "
                + " , lab144.lab144c1 "
                + "FROM     lab144 "
                + "         INNER JOIN lab22 on lab144.lab22c1 = lab22.lab22c1 "
                + "         INNER JOIN lab21 on lab22.lab21c1 = lab21.lab21c1 "
                + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "         LEFT JOIN lab54 on lab21.lab54c1 = lab54.lab54c1 "
                + "WHERE    lab144c3 = 1 "
                + "         AND lab22.lab22c1 = ? "
                + "         AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ", (ResultSet rs, int rowNum)
                -> {
            OrderSearch record = new OrderSearch();
            record.setOrder(rs.getLong("lab22c1"));
            record.setPatientIdDB(rs.getInt("lab21c1"));
            record.setDocumentTypeId(rs.getString("lab54c1") == null ? null : rs.getInt("lab54c1"));
            record.setDocumentType(rs.getString("lab54c3") == null ? null : rs.getString("lab54c3"));
            record.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            record.setLastName(Tools.decrypt(rs.getString("lab21c3")));
            record.setSurName(Tools.decrypt(rs.getString("lab21c4")));
            record.setName1(Tools.decrypt(rs.getString("lab21c5")));
            record.setName2(Tools.decrypt(rs.getString("lab21c6")));
            record.setSex(rs.getInt("lab80c3"));
            record.setBirthday(rs.getTimestamp("lab21c7"));
            record.setRecallId(rs.getInt("lab144c1"));
            return record;
        }, order);
    }

    default List<OrderSearch> getRecall(int dateI, int dateF) throws Exception {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c1 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c2 "
                + " , lab54.lab54c3 "
                + " , lab144.lab144c1 "
                + "FROM     lab144 "
                + "         INNER JOIN lab22 on lab144.lab22c1 = lab22.lab22c1 "
                + "         INNER JOIN lab21 on lab22.lab21c1 = lab21.lab21c1 "
                + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "         LEFT JOIN lab54 on lab21.lab54c1 = lab54.lab54c1 "
                + "WHERE    lab144c3 = 1 "
                + "         AND lab22.lab22c2 between ? and ?"
                + "         AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ", (ResultSet rs, int rowNum)
                -> {
            OrderSearch record = new OrderSearch();
            record.setOrder(rs.getLong("lab22c1"));
            record.setPatientIdDB(rs.getInt("lab21c1"));
            record.setDocumentTypeId(rs.getString("lab54c1") == null ? null : rs.getInt("lab54c1"));
            record.setDocumentType(rs.getString("lab54c3") == null ? null : rs.getString("lab54c3"));
            record.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            record.setLastName(Tools.decrypt(rs.getString("lab21c3")));
            record.setSurName(Tools.decrypt(rs.getString("lab21c4")));
            record.setName1(Tools.decrypt(rs.getString("lab21c5")));
            record.setName2(Tools.decrypt(rs.getString("lab21c6")));
            record.setSex(rs.getInt("lab80c3"));
            record.setBirthday(rs.getTimestamp("lab21c7"));
            record.setRecallId(rs.getInt("lab144c1"));
            return record;
        }, dateI, dateF);
    }

    default List<OrderSearch> getRecall(String lastNameEncrypted, String surNameEncrypted, String name1Encrypted, String name2Encrypted) throws Exception {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c1 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c2 "
                + " , lab54.lab54c3 "
                + " , lab144.lab144c1 "
                + "FROM     lab144 "
                + "         INNER JOIN lab22 on lab144.lab22c1 = lab22.lab22c1 "
                + "         INNER JOIN lab21 on lab22.lab21c1 = lab21.lab21c1 "
                + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "         LEFT JOIN lab54 on lab21.lab54c1 = lab54.lab54c1 "
                + "WHERE    lab144c3 = 1 "
                + (lastNameEncrypted != null ? "AND lab21.lab21c3 = '" + lastNameEncrypted + "'" : "")
                + (surNameEncrypted != null ? "AND lab21.lab21c4 = '" + surNameEncrypted + "'" : "")
                + (name1Encrypted != null ? "AND lab21.lab21c5 = '" + name1Encrypted + "'" : "")
                + (name2Encrypted != null ? "AND lab21.lab21c6 = '" + name2Encrypted + "'" : "")
                + "         AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ", (ResultSet rs, int rowNum)
                -> {
            OrderSearch record = new OrderSearch();
            record.setOrder(rs.getLong("lab22c1"));
            record.setPatientIdDB(rs.getInt("lab21c1"));
            record.setDocumentTypeId(rs.getString("lab54c1") == null ? null : rs.getInt("lab54c1"));
            record.setDocumentType(rs.getString("lab54c3") == null ? null : rs.getString("lab54c3"));
            record.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            record.setLastName(Tools.decrypt(rs.getString("lab21c3")));
            record.setSurName(Tools.decrypt(rs.getString("lab21c4")));
            record.setName1(Tools.decrypt(rs.getString("lab21c5")));
            record.setName2(Tools.decrypt(rs.getString("lab21c6")));
            record.setSex(rs.getInt("lab80c3"));
            record.setBirthday(rs.getTimestamp("lab21c7"));
            record.setRecallId(rs.getInt("lab144c1"));
            return record;
        });
    }

    default List<OrderSearch> getRecall(Integer documentTypeId, String patientIdEncrypted) throws Exception {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c1 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c2 "
                + " , lab54.lab54c3 "
                + " , lab144.lab144c1 "
                + "FROM     lab144 "
                + "         INNER JOIN lab22 on lab144.lab22c1 = lab22.lab22c1 "
                + "         INNER JOIN lab21 on lab22.lab21c1 = lab21.lab21c1 "
                + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "         LEFT JOIN lab54 on lab21.lab54c1 = lab54.lab54c1 "
                + "WHERE    lab144c3 = 1 "
                + (documentTypeId != null ? " AND lab21.lab54c1 = " + documentTypeId : "")
                + "         AND lab21.lab21c2 = ? "
                + "         AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ", (ResultSet rs, int rowNum)
                -> {
            OrderSearch record = new OrderSearch();
            record.setOrder(rs.getLong("lab22c1"));
            record.setPatientIdDB(rs.getInt("lab21c1"));
            record.setDocumentTypeId(rs.getString("lab54c1") == null ? null : rs.getInt("lab54c1"));
            record.setDocumentType(rs.getString("lab54c3") == null ? null : rs.getString("lab54c3"));
            record.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            record.setLastName(Tools.decrypt(rs.getString("lab21c3")));
            record.setSurName(Tools.decrypt(rs.getString("lab21c4")));
            record.setName1(Tools.decrypt(rs.getString("lab21c5")));
            record.setName2(Tools.decrypt(rs.getString("lab21c6")));
            record.setSex(rs.getInt("lab80c3"));
            record.setBirthday(rs.getTimestamp("lab21c7"));
            record.setRecallId(rs.getInt("lab144c1"));
            return record;
        }, patientIdEncrypted);
    }

    default List<Order> withoutTurn(String history, String type,  List<Integer> laboratorys, int idbranch) throws Exception {
        
        
        String filter = SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch);
        
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT DISTINCT  lab22.lab22c1 "
                + " , lab22.lab22c2 "
                + " , lab22.lab103c1 "
                + " , lab103.lab103c2 "
                + " , lab103.lab103c3 "
                + " , lab103.lab103c4 "
                + " , lab21.lab21c2, lab21.lab21c3, lab21.lab21c4, lab21.lab21c5, lab21.lab21c6, lab21.lab21c7 "
                + " , lab21.lab54c1 "
                + " , lab22.lab22c3 "
                + " , lab22.lab21c1 "
                + " , lab22.lab22c4 "
                + " , lab22.lab22c5 "
                + " , lab22.lab22c6 "
                + " , lab22.lab22c13 "
                + " , lab22.lab04c1 "
                + " , lab04.lab04c2 "
                + " , lab04.lab04c3 "
                + " , lab04.lab04c4 "
                + " , lab22.lab07c1 "
                + " , lab22.lab22c7 "
                + " , lab05.lab05c1 "
                + " , lab05.lab05c10 "
                + " , lab05.lab05c4 "
                + " , lab10.lab10c1 "
                + " , lab10.lab10c7 "
                + " , lab10.lab10c2 "
                + " , lab19.lab19c1 "
                + " , lab19.lab19c2 "
                + " , lab19.lab19c3 "
                + " , lab14.lab14c1 "
                + " , lab14.lab14c2 "
                + " , lab14.lab14c3 "
                + " , lab904.lab904c1 "
                + " , lab904.lab904c2 "
                + " , lab904.lab904c3 "
                + " , lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5 "
                + " FROM     lab22 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                + " INNER JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 "
                + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                + "WHERE  lab22.lab07c1 = 1 AND  lab21.lab21c2 = ?  AND (lab22c19 = 0 or lab22c19 is null) "
                + (type != null ? " AND lab21.lab54c1 = " + type : "")
                + "         AND (lab22c13 IS NULL OR lab22c13 = '') "
                + filter, (ResultSet rs, int rowNum)
                -> {
            Order order = new Order();
            order.setOrderNumber(rs.getLong("lab22c1"));
            order.setCreatedDateShort(rs.getInt("lab22c2"));
            order.setTurn(rs.getString("lab22c13"));

            OrderType orderType = new OrderType();
            orderType.setId(rs.getInt("lab103c1"));
            orderType.setCode(rs.getString("lab103c2"));
            orderType.setName(rs.getString("lab103c3"));
            orderType.setColor(rs.getString("lab103c4"));
            order.setType(orderType);

            order.setCreatedDate(rs.getTimestamp("lab22c3"));

            Patient patient = new Patient();
            patient.setId(rs.getInt("lab21c1"));
            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
            patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
            patient.setBirthday(rs.getTimestamp("lab21c7"));
            Item sex = new Item();
            sex.setId(rs.getInt("lab80c1"));
            sex.setIdParent(rs.getInt("lab80c2"));
            sex.setCode(rs.getString("lab80c3"));
            sex.setEsCo(rs.getString("lab80c4"));
            sex.setEnUsa(rs.getString("lab80c5"));
            patient.setSex(sex);

            DocumentType documentType = new DocumentType();
            documentType.setId(rs.getInt("lab54c1"));
            patient.setDocumentType(documentType);
            order.setPatient(patient);

            order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
            order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
            order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            user.setName(rs.getString("lab04c2"));
            user.setLastName(rs.getString("lab04c3"));
            user.setUserName(rs.getString("lab04c4"));

            order.setLastUpdateUser(user);
            order.setActive(rs.getInt("lab07c1") == 1);
            order.setExternalId(rs.getString("lab22c7"));
            if (rs.getString("lab05c1") != null) {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setName(rs.getString("lab05c4"));
                order.setBranch(branch);
            }
            if (rs.getString("lab10c1") != null) {
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                service.setCode(rs.getString("lab10c7"));
                service.setName(rs.getString("lab10c2"));
                order.setService(service);
            }
            if (rs.getString("lab19c1") != null) {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setLastName(rs.getString("lab19c2"));
                physician.setName(rs.getString("lab19c3"));
                order.setPhysician(physician);
            }
            if (rs.getString("lab14c1") != null) {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                order.setAccount(account);
            }
            if (rs.getString("lab904c1") != null) {
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode("lab904c2");
                rate.setName(rs.getString("lab904c3"));
                order.setRate(rate);
            }
            return order;
        }, history);
    }
    
    
    default List<Order> withoutAppointmentTurn(String history, String type,  List<Integer> laboratorys, int idbranch) throws Exception {
        
        String filter = SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch);
        
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT  DISTINCT lab22.lab22c1 "
                + " , lab22.lab22c2 "
                + " , lab22.lab103c1 "
                + " , lab103.lab103c2 "
                + " , lab103.lab103c3 "
                + " , lab103.lab103c4 "
                + " , lab21.lab21c2, lab21.lab21c3, lab21.lab21c4, lab21.lab21c5, lab21.lab21c6, lab21.lab21c7 "
                + " , lab21.lab54c1 "
                + " , lab22.lab22c3 "
                + " , lab22.lab21c1 "
                + " , lab22.lab22c4 "
                + " , lab22.lab22c5 "
                + " , lab22.lab22c6 "
                + " , lab22.lab22c13 "
                + " , lab22.lab04c1 "
                + " , lab04.lab04c2 "
                + " , lab04.lab04c3 "
                + " , lab04.lab04c4 "
                + " , lab22.lab07c1 "
                + " , lab22.lab22c7 "
                + " , lab05.lab05c1 "
                + " , lab05.lab05c10 "
                + " , lab05.lab05c4 "
                + " , lab10.lab10c1 "
                + " , lab10.lab10c7 "
                + " , lab10.lab10c2 "
                + " , lab19.lab19c1 "
                + " , lab19.lab19c2 "
                + " , lab19.lab19c3 "
                + " , lab14.lab14c1 "
                + " , lab14.lab14c2 "
                + " , lab14.lab14c3 "
                + " , lab904.lab904c1 "
                + " , lab904.lab904c2 "
                + " , lab904.lab904c3 "
                + " , lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5 "
                + " FROM     lab22 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                + " INNER JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 "        
                + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                + "WHERE  lab22.lab07c1 = 1 AND  lab21.lab21c2 = ?  AND (lab22c19 = 1) "
                + (type != null ? " AND lab21.lab54c1 = " + type : "")
                + "         AND (lab22c13 IS NULL OR lab22c13 = '') "
                + filter, (ResultSet rs, int rowNum)
                -> {
            Order order = new Order();
            order.setOrderNumber(rs.getLong("lab22c1"));
            order.setCreatedDateShort(rs.getInt("lab22c2"));
            order.setTurn(rs.getString("lab22c13"));

            OrderType orderType = new OrderType();
            orderType.setId(rs.getInt("lab103c1"));
            orderType.setCode(rs.getString("lab103c2"));
            orderType.setName(rs.getString("lab103c3"));
            orderType.setColor(rs.getString("lab103c4"));
            order.setType(orderType);

            order.setCreatedDate(rs.getTimestamp("lab22c3"));

            Patient patient = new Patient();
            patient.setId(rs.getInt("lab21c1"));
            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
            patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
            patient.setBirthday(rs.getTimestamp("lab21c7"));
            Item sex = new Item();
            sex.setId(rs.getInt("lab80c1"));
            sex.setIdParent(rs.getInt("lab80c2"));
            sex.setCode(rs.getString("lab80c3"));
            sex.setEsCo(rs.getString("lab80c4"));
            sex.setEnUsa(rs.getString("lab80c5"));
            patient.setSex(sex);

            DocumentType documentType = new DocumentType();
            documentType.setId(rs.getInt("lab54c1"));
            patient.setDocumentType(documentType);
            order.setPatient(patient);

            order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
            order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
            order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            user.setName(rs.getString("lab04c2"));
            user.setLastName(rs.getString("lab04c3"));
            user.setUserName(rs.getString("lab04c4"));

            order.setLastUpdateUser(user);
            order.setActive(rs.getInt("lab07c1") == 1);
            order.setExternalId(rs.getString("lab22c7"));
            if (rs.getString("lab05c1") != null) {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setName(rs.getString("lab05c4"));
                order.setBranch(branch);
            }
            if (rs.getString("lab10c1") != null) {
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                service.setCode(rs.getString("lab10c7"));
                service.setName(rs.getString("lab10c2"));
                order.setService(service);
            }
            if (rs.getString("lab19c1") != null) {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setLastName(rs.getString("lab19c2"));
                physician.setName(rs.getString("lab19c3"));
                order.setPhysician(physician);
            }
            if (rs.getString("lab14c1") != null) {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                order.setAccount(account);
            }
            if (rs.getString("lab904c1") != null) {
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode("lab904c2");
                rate.setName(rs.getString("lab904c3"));
                order.setRate(rate);
            }
            return order;
        }, history);
    }
    
    

    /**
     * Actualiza el turno de una lista de ordenes
     *
     * @param object
     * {@link net.cltech.enterprisent.domain.operation.orders.ShiftOrder}
     *
     * @return {@link numero de elementos modificados}
     * @throws Exception Error en base de datos
     */
    default int shiftOrders(ShiftOrder object) throws Exception {
        if (object.getOrders().size() > 0) {
            List<Object[]> parameters = new ArrayList<>(0);
            String query = "UPDATE lab22 SET lab22c13 = ?, lab22c20 = 0 WHERE lab22c1 = ?";
            object.getOrders().stream().forEach((String order)
                    -> {
                parameters.add(new Object[]{
                    object.getTurn(),
                    Long.parseLong(order)
                });
            });
            return getJdbcTemplate().batchUpdate(query, parameters).length;
        }
        return 0;

    }

    default List<Order> ordersByTurn(String turn) throws Exception {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab22.lab22c2 "
                + " , lab22.lab103c1 "
                + " , lab103.lab103c2 "
                + " , lab103.lab103c3 "
                + " , lab103.lab103c4 "
                + " , lab22.lab22c3 "
                + " , lab22.lab21c1, lab21.lab21c2, lab21.lab21c3, lab21.lab21c4, lab21.lab21c5, lab21.lab21c6, lab21.lab21c7 "
                + " , lab22.lab22c4 "
                + " , lab22.lab22c5 "
                + " , lab22.lab22c6 "
                + " , lab22.lab22c13 "
                + " , lab22.lab04c1 "
                + " , lab04.lab04c2 "
                + " , lab04.lab04c3 "
                + " , lab04.lab04c4 "
                + " , lab22.lab07c1 "
                + " , lab22.lab22c7 "
                + " , lab05.lab05c1 "
                + " , lab05.lab05c10 "
                + " , lab05.lab05c4 "
                + " , lab10.lab10c1 "
                + " , lab10.lab10c7 "
                + " , lab10.lab10c2 "
                + " , lab19.lab19c1 "
                + " , lab19.lab19c2 "
                + " , lab19.lab19c3 "
                + " , lab14.lab14c1 "
                + " , lab14.lab14c2 "
                + " , lab14.lab14c3 "
                + " , lab904.lab904c1 "
                + " , lab904.lab904c2 "
                + " , lab904.lab904c3 "
                + " , lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5 "
                + " FROM     lab22 "
                + " INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                + " INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 "
                + " LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 "
                + " LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 "
                + " LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 "
                + " LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 "
                + " LEFT JOIN  lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                + " LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 "
                + "WHERE   lab22.lab07c1 = 1 AND lab22c13 = ? AND lab22c2 = ?  AND (lab22c19 = 0 or lab22c19 is null) ", (ResultSet rs, int rowNum)
                -> {
            Order order = new Order();
            order.setOrderNumber(rs.getLong("lab22c1"));
            order.setCreatedDateShort(rs.getInt("lab22c2"));
            order.setTurn(rs.getString("lab22c13"));

            OrderType orderType = new OrderType();
            orderType.setId(rs.getInt("lab103c1"));
            orderType.setCode(rs.getString("lab103c2"));
            orderType.setName(rs.getString("lab103c3"));
            orderType.setColor(rs.getString("lab103c4"));
            order.setType(orderType);

            order.setCreatedDate(rs.getTimestamp("lab22c3"));

            if (rs.getString("lab21c1") != null) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                patient.setBirthday(rs.getTimestamp("lab21c7"));
                Item sex = new Item();
                sex.setId(rs.getInt("lab80c1"));
                sex.setIdParent(rs.getInt("lab80c2"));
                sex.setCode(rs.getString("lab80c3"));
                sex.setEsCo(rs.getString("lab80c4"));
                sex.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(sex);
                order.setPatient(patient);
            }

            order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
            order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
            order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            user.setName(rs.getString("lab04c2"));
            user.setLastName(rs.getString("lab04c3"));
            user.setUserName(rs.getString("lab04c4"));

            order.setLastUpdateUser(user);
            order.setActive(rs.getInt("lab07c1") == 1);
            order.setExternalId(rs.getString("lab22c7"));
            if (rs.getString("lab05c1") != null) {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setName(rs.getString("lab05c4"));
                order.setBranch(branch);
            }
            if (rs.getString("lab10c1") != null) {
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                service.setCode(rs.getString("lab10c7"));
                service.setName(rs.getString("lab10c2"));
                order.setService(service);
            }
            if (rs.getString("lab19c1") != null) {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setLastName(rs.getString("lab19c2"));
                physician.setName(rs.getString("lab19c3"));
                order.setPhysician(physician);
            }
            if (rs.getString("lab14c1") != null) {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                order.setAccount(account);
            }
            if (rs.getString("lab904c1") != null) {
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode("lab904c2");
                rate.setName(rs.getString("lab904c3"));
                order.setRate(rate);
            }
            return order;
        }, turn, Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())));
    }

    /**
     * Obtiene todas las ordenes buscandolas por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacio en caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    default List<OrderSearch> ordersByEntryDate(int date) throws Exception {
        try {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab22.lab22c1 "
                    + " , lab21.lab21c1 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c3 "
                    + " , lab21.lab21c2 "
                    + " , lab21.lab21c3 "
                    + " , lab21.lab21c4 "
                    + " , lab21.lab21c5 "
                    + " , lab21.lab21c6 "
                    + " , lab80.lab80c3 "
                    + " , lab21.lab21c7 "
                    + "FROM     lab22 "
                    + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "WHERE    lab22.lab07c1 = 1 AND lab22.lab22c2 = ?  AND (lab22c19 = 0 or lab22c19 is null) ";
            return getJdbcTemplate().query(query, (ResultSet rs, int rowNum)
                    -> {
                OrderSearch orderR = new OrderSearch();
                orderR.setOrder(rs.getLong("lab22c1"));
                orderR.setPatientIdDB(rs.getInt("lab21c1"));
                orderR.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                orderR.setDocumentTypeId(rs.getInt("lab54c1"));
                orderR.setDocumentType(rs.getString("lab54c3"));
                orderR.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                orderR.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                orderR.setName1(Tools.decrypt(rs.getString("lab21c3")));
                orderR.setName2(Tools.decrypt(rs.getString("lab21c4")));
                orderR.setSex(rs.getInt("lab80c3"));
                orderR.setBirthday(rs.getTimestamp("lab21c7"));
                return orderR;
            }, date);
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList(0);
        }
    }

    /**
     * Obtiene una lista de ordenes con informacion de pacientes
     *
     * @param whereFilter
     * @param paramsFilter
     * @param filter
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<Order> ordersWithPatient(String whereFilter, List<Object> paramsFilter, OrderSearchFilter filter,  List<Integer> laboratorys, int branch) throws Exception {

        List<Order> orders = new LinkedList<>();
        int currentYear = DateTools.dateToNumberYear(new Date());

        String initYear = Integer.toString(currentYear);
        String endYear = Integer.toString(currentYear);

        if (filter.getInit() != null && filter.getEnd() != null) {
            initYear = String.valueOf(filter.getInit());
            endYear = String.valueOf(filter.getEnd());
        }

        // Consulta de ordenes por historico:
        List<Integer> years = Tools.listOfConsecutiveYears(initYear, endYear);
        String lab22;

        for (Integer year : years) {
            lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            List<Object> params = new ArrayList<>();
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  DISTINCT "
                    + "lab22.lab22c1, "
                    + "lab22.lab07c1, "
                    + "lab22.lab21c1, "
                    + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6,lab21c8, lab21c16, lab21c17,"
                    + "lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab21c7,  lab54.lab54c1, lab54.lab54c2, lab54.lab54c3 ";
            String from = "FROM      " + lab22 + " as lab22 "
                    + " INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + " INNER JOIN lab57 ON lab57.lab22c1 = lab22.lab22c1 "
                    + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                    + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ";
            String where = "WHERE lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab22.lab21c1 != 0 ";

            try {
                if (whereFilter != null) {
                    
                    whereFilter = whereFilter.replace("lab22c1", "lab22.lab22c1");
                    where += whereFilter;
                    params.addAll(paramsFilter);
                }
                
                where+= (SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));

                getJdbcTemplate().query(select + from + where, params.toArray(), (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));

                    Patient patient = new Patient();
                    patient.setId(rs.getInt("lab21c1"));
                    order.setPatient(patient);

                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
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
                    order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                    order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                    order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                    
                    order.setActive(rs.getInt("lab07c1") == 1);

                    orders.add(order);

                    return order.cleanFull();
                });
            } catch (EmptyResultDataAccessException ex) {
                return new ArrayList<>();
            }
        }
        return orders;
    }

    /**
     * Busca y trae las ordenes con la informacion necesaria para los reportes,
     * rango y filtro de demograficos
     *
     * @param orderInitial
     * @param demographics Lista de
     * {net.cltech.enterprisent.domain.masters.demographic.Demographic} con los
     * demograficos configurados para la orden activos
     * @param orderEnd
     * @param demographicsFilter
     * @param type
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null si no es encontrada
     * @throws Exception Error en base de datos
     */
    default List<Order> orderReport(long orderInitial, long orderEnd, final List<Demographic> demographics, final List<Demographic> demographicsFilter, int type,  List<Integer> laboratorys, int idbranch) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(orderInitial), String.valueOf(orderEnd));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                final StringBuilder select = new StringBuilder();
                final StringBuilder from = new StringBuilder();
                final StringBuilder where = new StringBuilder();

                select.append(ISOLATION_READ_UNCOMMITTED);
                select.append("SELECT   lab22.lab22c1");
                select.append(", lab22.lab22c2 ");
                select.append(", lab22.lab103c1 ");
                select.append(", lab103.lab103c2 ");
                select.append(", lab103.lab103c3 ");
                select.append(", lab103.lab103c4 ");
                select.append(", lab22.lab22c3 ");
                select.append(", lab22.lab21c1 ");
                select.append(", lab22.lab22c4 ");
                select.append(", lab22.lab22c5 ");
                select.append(", lab22.lab22c6 ");
                select.append(", lab22.lab04c1 ");
                select.append(", lab22.lab22c13 ");
                select.append(", lab04.lab04c2 ");
                select.append(", lab04.lab04c3 ");
                select.append(", lab04.lab04c4 ");
                select.append(", lab22.lab07c1 ");
                select.append(", lab22.lab22c7 ");
                select.append(", lab05.lab05c1 ");
                select.append(", lab05.lab05c2 ");
                select.append(", lab05.lab05c10 ");
                select.append(", lab05.lab05c4 ");
                select.append(", lab10.lab10c1 ");
                select.append(", lab10.lab10c7 ");
                select.append(", lab10.lab10c2 ");
                select.append(", lab19.lab19c1 ");
                select.append(", lab19.lab19c2 ");
                select.append(", lab19.lab19c3 ");
                select.append(", lab19.lab19c22 ");
                select.append(", lab14.lab14c1 ");
                select.append(", lab14.lab14c2 ");
                select.append(", lab14.lab14c3 ");
                select.append(", lab904.lab904c1 ");
                select.append(", lab904.lab904c2 ");
                select.append(" , lab904.lab904c3 ");
                select.append(" , lab39.lab39c1 ");
                select.append(" , lab39.lab39c2 ");
                select.append(" , lab39.lab39c3 ");
                select.append(" , lab57.lab57c14 ");
                select.append(" , lab57.lab57c15 ");
                select.append(" , lab39.lab24c1 ");
                select.append(" , lab39.lab39c26 ");
                select.append(" , lab39.lab39c37 ");
                select.append(" , lab16.lab16c3, lab11.lab11c1 ");
                from.append(" FROM      ").append(lab57).append(" as lab57 ");
                from.append(" INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append(" INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
                from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
                from.append(" INNER JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 ");
                from.append(" INNER JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 ");
                from.append(" LEFT JOIN  lab05 ON lab22.lab05c1 = lab05.lab05C1 ");
                from.append(" LEFT JOIN  lab10 ON lab22.lab10c1 = lab10.lab10C1 ");
                from.append(" LEFT JOIN  lab19 ON lab22.lab19c1 = lab19.lab19C1 ");
                from.append(" LEFT JOIN  lab14 ON lab22.lab14c1 = lab14.lab14C1 ");
                from.append(" LEFT JOIN  lab904 ON lab22.lab904c1 = lab904.lab904C1 ");
                from.append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                from.append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

                demographics.stream().forEach((Demographic demographic)
                        -> {
                    if (demographic.getOrigin().equals("O")) {
                        if (demographic.isEncoded()) {
                            select.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            select.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            select.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else {
                            select.append(", lab22.lab_demo_").append(demographic.getId());
                        }
                    } else {
                        if (demographic.getOrigin().equals("H")) {
                            if (demographic.isEncoded()) {
                                select.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                select.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                select.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                select.append(", lab21.lab_demo_").append(demographic.getId());
                            }
                        }
                    }
                });
                switch (type) {
                    case 1:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                        break;
                    case 0:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                        break;
                    default:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                        break;
                }
                List<Object> parameters = new ArrayList<>(0);
                parameters.add(orderInitial);
                parameters.add(orderEnd);
                demographicsFilter.stream().forEach((Demographic demographic)
                        -> {
                    if (demographic.isEncoded()) {
                        where.append(" AND demo").append(demographic.getId()).append(".lab63c1 = ? ");
                        parameters.add(demographic.getId());
                    } else {
                        where.append(" AND Lab22.lab_demo_").append(demographic.getId()).append(" = ? ");
                        parameters.add(String.valueOf(demographic.getDemographicItem()));
                    }
                });
                
                
                where.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));

              
                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    if (!listOrders.contains(order)) {
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setTurn(rs.getString("lab22c13"));

                        OrderType orderType = new OrderType();
                        orderType.setId(rs.getInt("lab103c1"));
                        orderType.setCode(rs.getString("lab103c2"));
                        orderType.setName(rs.getString("lab103c3"));
                        orderType.setColor(rs.getString("lab103c4"));
                        order.setType(orderType);

                        order.setCreatedDate(rs.getTimestamp("lab22c3"));

                        Patient patient = new Patient();
                        patient.setId(rs.getInt("lab21c1"));
                        order.setPatient(patient);

                        order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                        order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                        order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                        User user = new User();
                        user.setId(rs.getInt("lab04c1"));
                        user.setName(rs.getString("lab04c2"));
                        user.setLastName(rs.getString("lab04c3"));
                        user.setUserName(rs.getString("lab04c4"));
                        order.setLastUpdateUser(user);

                        order.setActive(rs.getInt("lab07c1") == 1);
                        order.setExternalId(rs.getString("lab22c7"));

                        if (rs.getString("lab05c1") != null) {
                            Branch branch = new Branch();
                            branch.setId(rs.getInt("lab05c1"));
                            branch.setAbbreviation(rs.getString("lab05c2"));
                            branch.setCode(rs.getString("lab05c10"));
                            branch.setName(rs.getString("lab05c4"));
                            order.setBranch(branch);
                        }

                        if (rs.getString("lab10c1") != null) {
                            ServiceLaboratory service = new ServiceLaboratory();
                            service.setId(rs.getInt("lab10c1"));
                            service.setCode(rs.getString("lab10c7"));
                            service.setName(rs.getString("lab10c2"));
                            order.setService(service);
                        }

                        if (rs.getString("lab19c1") != null) {
                            Physician physician = new Physician();
                            physician.setId(rs.getInt("lab19c1"));
                            physician.setCode(rs.getString("lab19c22"));
                            physician.setLastName(rs.getString("lab19c2"));
                            physician.setName(rs.getString("lab19c3"));
                            order.setPhysician(physician);
                        }

                        if (rs.getString("lab14c1") != null) {
                            Account account = new Account();
                            account.setId(rs.getInt("lab14c1"));
                            account.setNit(rs.getString("lab14c2"));
                            account.setName(rs.getString("lab14c3"));
                            order.setAccount(account);
                        }

                        if (rs.getString("lab904c1") != null) {
                            Rate rate = new Rate();
                            rate.setId(rs.getInt("lab904c1"));
                            rate.setCode(rs.getString("lab904c2"));
                            rate.setName(rs.getString("lab904c3"));
                            order.setRate(rate);
                        }

                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics) {
                            demoValue = new DemographicValue();
                            demoValue.setIdDemographic(demographic.getId());
                            demoValue.setDemographic(demographic.getName());
                            demoValue.setEncoded(demographic.isEncoded());
                            if (demographic.isEncoded()) {
                                if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                                }
                            } else {
                                demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                            }
                            if (demographic.getOrigin().equals("O")) {
                                order.getDemographics().add(demoValue);
                            } else {
                                order.getPatient().getDemographics().add(demoValue);
                            }
                        }
                        listOrders.add(order);
                    }

                    Test test = new Test();
                    test.setId(rs.getInt("lab39c1"));
                    test.setCode(rs.getString("lab39c2"));
                    test.setAbbr(rs.getString("lab39c3"));
                    test.getSample().setId(rs.getInt("lab24c1"));

                    test.setProfile(rs.getInt("lab57c14"));

                    test.setRackStore(rs.getString("lab16c3"));
                    test.setPositionStore(rs.getString("lab11c1"));
                    test.setShowInQuery(rs.getInt("lab39c26") == 1);
                    test.setTestType(rs.getShort("lab39c37"));

                    listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                    return order;
                };
                getJdbcTemplate().query(select.toString() + from.toString() + where.toString(), parameters.toArray(), mapper);
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Error en etiquetas " + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el numero de ordenes ingresadas en una fecha especifica
     *
     * @param date Fecha de la orden
     * @param idUser Usuario que creo la orden
     * @param idBranch Sede donde se creo la orden
     *
     * @return numero de ordenes
     * @throws Exception Error en base de datos
     */
    default long numberOrders(int date, Integer idUser, Integer idBranch) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   COUNT(lab22.lab22c1) AS count ");
            select.append(" FROM     lab22 ");
            select.append(" WHERE     lab22c2 = ? ");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(date);
            if (idUser != null) {
                select.append(" AND     lab04c1_1 = ? ");
                parameters.add(idUser);
            }
            if (idBranch != null) {
                select.append(" AND     lab05c1 = ? ");
                parameters.add(idBranch);
            }

            select.append(" AND (lab22c19 = 0 or lab22c19 is null)");
            return getJdbcTemplate().queryForObject(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("count");
            });
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    /**
     * Busca ordenes con examenes en estado de retoma en el sistema por los
     * filtros enviados
     *
     * @param documentType
     * @param encryptedPatientId Historia encriptada, null en caso de no
     * requerir filtro
     * @param encryptedLastName Apellido encriptado, null en caso de no requerir
     * filtro
     * @param encryptedSurName Segundo Apellido encriptad1, null en caso de no
     * requerir filtro
     * @param encryptedName1 Nombre encriptado, null en caso de no requerir
     * filtro
     * @param encryptedName2 Segundo encriptado, null en caso de no requerir
     * filtro
     * @param sex Id sexo, null en caso de no requerir filtro
     * @param birthday Fecha de Nacimiento, null en caso de no requerir filtro
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @param demographics
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<Order> getByPatientInfoRecalled(Integer documentType, String encryptedPatientId, String encryptedLastName, String encryptedSurName, String encryptedName1, String encryptedName2, Integer sex, Date birthday, int branch, final List<Demographic> demographics, boolean handledservice) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            final StringBuilder from = new StringBuilder();
            final StringBuilder where = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   o.lab22c1");
            select.append(", o.lab22c2 ");
            select.append(", o.lab103c1 ");
            select.append(", lab103.lab103c2 ");
            select.append(", lab103.lab103c3 ");
            select.append(", lab103.lab103c4 ");
            select.append(", lab57c14 ");//Id Perfil Padre
            select.append(", o.lab22c3 ");
            select.append(", o.lab21c1 ");
            select.append(", o.lab22c4 ");
            select.append(", o.lab22c5 ");
            select.append(", o.lab22c6 ");
            select.append(", o.lab04c1 ");
            select.append(", o.lab22c13 ");
            select.append(", lab04.lab04c2 ");
            select.append(", lab04.lab04c3 ");
            select.append(", lab04.lab04c4 ");
            select.append(", o.lab07c1 ");
            select.append(", o.lab22c7 ");
            select.append(", e.lab39c1 ");
            select.append(", e.lab39c2 ");
            select.append(", e.lab39c4 ");
            select.append(", e.lab39c37 ");
            select.append(", lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10 ");
            select.append(", lab24.lab56c1, lab56.lab56c2 ");
            select.append(", lab21c2, lab21c3, lab21c4, lab21c5, lab21c6,lab21c8, lab21c16, lab21c17, ");
            select.append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab21c7, lab21c14, lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ");
            select.append("lab16.lab16c3, lab11.lab11c1 ");
            from.append(" FROM lab57 res ");
            from.append(" JOIN lab22 AS o ON o.lab22c1 = res.lab22c1 ");
            from.append(" JOIN lab21 ON lab21.lab21c1 = o.lab21c1 ");
            from.append(" JOIN lab39 AS e ON e.lab39c1 = res.lab39c1 ");
            from.append(" JOIN lab04 ON o.lab04c1 = lab04.lab04c1 ");
            from.append(" LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ");
            from.append(" LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ");
            from.append(" LEFT JOIN lab221 ON lab221.lab22c1_2 = o.lab22c1 ");
            from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = e.lab24c1 ");
            from.append(" LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            from.append(" LEFT JOIN lab11 ON lab11.lab24c1 = res.lab24c1 AND lab11.lab22c1 = res.lab22c1 ");
            from.append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
            SQLTools.buildSQLDemographicSelect(demographics, select, from);
            where.append(" WHERE e.lab39c37 != 2 AND res.lab57c16 = 1 ");
            if (handledservice) {
                where.append(" AND (lab10.lab10c8 = 0 OR (lab10.lab10c1 = 0 OR lab10.lab10c8 is null)) ");
            }
            where.append(" AND lab221.lab22c1_2 IS NULL");
            where.append(" AND NOT EXISTS(SELECT 1 FROM lab221 AS r JOIN lab57 AS res2 ON r.lab22c1_2 = res2.lab22c1 JOIN lab39 AS e2 ON e2.lab39c1 = res2.lab39c1 WHERE r.lab22c1_1 = o.lab22c1 AND res2.lab39c1 = res.lab39c1 AND e2.lab39c37 != 2)");
            where.append(documentType != null ? " AND Lab21.Lab54C1 = ? " : "")
                    .append(encryptedPatientId != null ? " AND lab21.lab21c2 = ? " : "")
                    .append(encryptedLastName != null ? " AND lab21.lab21c5 = ? " : "")
                    .append(encryptedSurName != null ? " AND lab21.lab21c6 = ? " : "")
                    .append(encryptedName1 != null ? " AND lab21.lab21c3 = ? " : "")
                    .append(encryptedName2 != null ? " AND lab21.lab21c4 = ? " : "")
                    .append(sex != null ? " AND lab21.lab80c1 = ? " : "")
                    .append(birthday != null ? " AND lab21.lab21c7 = ? " : "")
                    .append(branch != -1 ? " AND o.lab05c1 = ? " : "");
            List parameters = new ArrayList(0);
            if (documentType != null) {
                parameters.add(documentType);
            }
            if (encryptedPatientId != null) {
                parameters.add(encryptedPatientId);
            }
            if (encryptedLastName != null) {
                parameters.add(encryptedLastName);
            }
            if (encryptedSurName != null) {
                parameters.add(encryptedSurName);
            }
            if (encryptedName1 != null) {
                parameters.add(encryptedName1);
            }
            if (encryptedName2 != null) {
                parameters.add(encryptedName2);
            }
            if (sex != null) {
                parameters.add(sex);
            }
            if (birthday != null) {
                parameters.add(birthday);
            }
            if (branch != -1) {
                parameters.add(branch);
            }
            List<Order> listOrders = new ArrayList<>();
            List<Demographic> demographicsFinal = demographics.stream().filter(d -> d.getId() > 0).collect(Collectors.toList());
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                if (!listOrders.contains(order)) {
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setTurn(rs.getString("lab22c13"));

                    OrderType orderType = new OrderType();
                    orderType.setId(rs.getInt("lab103c1"));
                    orderType.setCode(rs.getString("lab103c2"));
                    orderType.setName(rs.getString("lab103c3"));
                    orderType.setColor(rs.getString("lab103c4"));
                    order.setType(orderType);

                    order.setCreatedDate(rs.getTimestamp("lab22c3"));

                    Patient patient = new Patient();
                    patient.setId(rs.getInt("lab21c1"));
                    order.setPatient(patient);

                    //PACIENTE
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
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
                    order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                    order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                    order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                    order.getPatient().setPhoto(rs.getString("lab21c14"));

                    order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                    order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                    order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                    User user = new User();
                    user.setId(rs.getInt("lab04c1"));
                    user.setName(rs.getString("lab04c2"));
                    user.setLastName(rs.getString("lab04c3"));
                    user.setUserName(rs.getString("lab04c4"));
                    order.setLastUpdateUser(user);

                    order.setActive(rs.getInt("lab07c1") == 1);
                    order.setExternalId(rs.getString("lab22c7"));

                    if (select.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null) {
                        Branch branchAux = new Branch();
                        branchAux.setId(rs.getInt("lab05c1"));
                        branchAux.setCode(rs.getString("lab05c10"));
                        branchAux.setName(rs.getString("lab05c4"));
                        order.setBranch(branchAux);
                    }

                    if (select.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null) {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setCode(rs.getString("lab10c7"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (select.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setCode(rs.getString("lab19c22"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (select.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null) {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setNit(rs.getString("lab14c2"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (select.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null) {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    DemographicValue demoValue = null;
                    for (Demographic demographic : demographicsFinal) {
                        demoValue = new DemographicValue();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded()) {
                            if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                            }
                        } else {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        if (demographic.getOrigin().equals("O")) {
                            order.getDemographics().add(demoValue);
                        } else {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    listOrders.add(order);
                }
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setProfileId(rs.getInt("lab57c14"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));

                if (rs.getString("lab24c1") != null) {
                    test.setSample(new Sample());
                    test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                    test.getSample().getContainer().setName(rs.getString("lab56c2"));
                }
                Rate rateOrder = listOrders.get(listOrders.indexOf(order)).getRate();
                if (rateOrder != null) {
                    test.setRate(rateOrder);
                    try {
                        test.setPrice(getPriceTest(test.getId(), rateOrder.getId()));
                    } catch (Exception ex) {
                        test.setPrice(BigDecimal.ZERO);
                    }
                }
                listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                return order;
            };
            getJdbcTemplate().query((select.toString() + from.toString() + where.toString()).replace("lab22.", "o."), parameters.toArray(), mapper);
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta una orden con examenes en estado de retoma por numero de orden
     *
     * @param orderNumber Numero de orden
     * @param branch Id Sede, -1 en caso de no querer hacer filtro por sede
     * @param demographics
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrar datos
     * @throws Exception Error en base de datos
     */
    default Order getByOrderRecalled(long orderNumber, int branch, final List<Demographic> demographics, boolean handledservice) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            final StringBuilder from = new StringBuilder();
            final StringBuilder where = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   o.lab22c1");
            select.append(", o.lab22c2 ");
            select.append(", lab57c14 ");//Id Perfil Padre
            select.append(", o.lab103c1 ");
            select.append(", lab103.lab103c2 ");
            select.append(", lab103.lab103c3 ");
            select.append(", lab103.lab103c4 ");
            select.append(", o.lab22c3 ");
            select.append(", o.lab21c1 ");
            select.append(", o.lab22c4 ");
            select.append(", o.lab22c5 ");
            select.append(", o.lab22c6 ");
            select.append(", o.lab04c1 ");
            select.append(", o.lab22c13 ");
            select.append(", lab04.lab04c2 ");
            select.append(", lab04.lab04c3 ");
            select.append(", lab04.lab04c4 ");
            select.append(", o.lab07c1 ");
            select.append(", o.lab22c7 ");
            select.append(", e.lab39c1 ");
            select.append(", e.lab39c2 ");
            select.append(", e.lab39c4 ");
            select.append(", e.lab39c37 ");
            select.append(", lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10 ");
            select.append(", lab24.lab56c1, lab56.lab56c2 ");
            select.append(", lab21c2, lab21c3, lab21c4, lab21c5, lab21c6,lab21c8, lab21c16, lab21c17,");
            select.append("lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab21c7, lab21c14, lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ");
            select.append("lab16.lab16c3, lab11.lab11c1 ");
            select.append(", e.lab43c1, lab43c2, lab43c4 ");
            from.append(" FROM lab57 res ");
            from.append(" JOIN lab22 AS o ON o.lab22c1 = res.lab22c1 ");
            from.append(" JOIN lab21 ON lab21.lab21c1 = o.lab21c1 ");
            from.append(" JOIN lab39 AS e ON e.lab39c1 = res.lab39c1 ");
            from.append(" JOIN lab04 ON o.lab04c1 = lab04.lab04c1 ");
            from.append(" LEFT JOIN lab43 ON lab43.lab43c1 = e.lab43c1 ");
            from.append(" LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ");
            from.append(" LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ");
            from.append(" LEFT JOIN lab221 ON lab221.lab22c1_2 = o.lab22c1 ");
            from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = e.lab24c1 ");
            from.append(" LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            from.append(" LEFT JOIN lab11 ON lab11.lab24c1 = res.lab24c1 AND lab11.lab22c1 = res.lab22c1 ");
            from.append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

            SQLTools.buildSQLDemographicSelect(demographics, select, from);
            where.append(" WHERE e.lab39c37 != 2 AND res.lab57c16 = 1 ");
            if (handledservice) {
                where.append(" AND (lab10.lab10c8 = 0 OR (lab10.lab10c1 = 0 OR lab10.lab10c8 is null)) ");
            }
            where.append(" AND o.lab22c1 = ? ").append(branch != -1 ? " AND o.lab05c1 = " + branch : "");
            where.append(" AND NOT EXISTS(SELECT 1 FROM lab221 AS r JOIN lab57 AS res2 ON r.lab22c1_2 = res2.lab22c1 JOIN lab39 AS e2 ON e2.lab39c1 = res2.lab39c1 WHERE r.lab22c1_1 = o.lab22c1 AND res2.lab39c1 = res.lab39c1 AND e2.lab39c37 != 2)");
            List parameters = new ArrayList(0);
            parameters.add(orderNumber);
            List<Order> listOrders = new ArrayList<>();
            List<Demographic> demographicsFinal = demographics.stream().filter(d -> d.getId() > 0).collect(Collectors.toList());
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                if (!listOrders.contains(order)) {
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setTurn(rs.getString("lab22c13"));

                    OrderType orderType = new OrderType();
                    orderType.setId(rs.getInt("lab103c1"));
                    orderType.setCode(rs.getString("lab103c2"));
                    orderType.setName(rs.getString("lab103c3"));
                    orderType.setColor(rs.getString("lab103c4"));
                    order.setType(orderType);

                    order.setCreatedDate(rs.getTimestamp("lab22c3"));

                    Patient patient = new Patient();
                    patient.setId(rs.getInt("lab21c1"));
                    order.setPatient(patient);
                    //PACIENTE
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
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
                    order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                    order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                    order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                    order.getPatient().setPhoto(rs.getString("lab21c14"));

                    order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                    order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                    order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                    User user = new User();
                    user.setId(rs.getInt("lab04c1"));
                    user.setName(rs.getString("lab04c2"));
                    user.setLastName(rs.getString("lab04c3"));
                    user.setUserName(rs.getString("lab04c4"));
                    order.setLastUpdateUser(user);

                    order.setActive(rs.getInt("lab07c1") == 1);
                    order.setExternalId(rs.getString("lab22c7"));

                    if (select.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null) {
                        Branch branchAux = new Branch();
                        branchAux.setId(rs.getInt("lab05c1"));
                        branchAux.setCode(rs.getString("lab05c10"));
                        branchAux.setName(rs.getString("lab05c4"));
                        order.setBranch(branchAux);
                    }

                    if (select.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null) {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setCode(rs.getString("lab10c7"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (select.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setCode(rs.getString("lab19c22"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (select.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null) {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setNit(rs.getString("lab14c2"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (select.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null) {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    DemographicValue demoValue = null;
                    for (Demographic demographic : demographicsFinal) {
                        demoValue = new DemographicValue();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded()) {
                            if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                            }
                        } else {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        if (demographic.getOrigin().equals("O")) {
                            order.getDemographics().add(demoValue);
                        } else {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    listOrders.add(order);
                }
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setProfileId(rs.getInt("lab57c14"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));

                if (rs.getString("lab24c1") != null) {
                    test.setSample(new Sample());
                    test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                    test.getSample().getContainer().setName(rs.getString("lab56c2"));

                }

                if (rs.getString("lab43c1") != null) {
                    test.setArea(new Area());
                    test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                    test.getArea().setAbbreviation(rs.getString("lab43c2"));
                    test.getArea().setName(rs.getString("lab43c4"));
                }

                Rate rateOrder = listOrders.get(listOrders.indexOf(order)).getRate();
                if (rateOrder != null) {
                    test.setRate(rateOrder);
                    try {
                        test.setPrice(getPriceTest(test.getId(), rateOrder.getId()));
                    } catch (Exception ex) {
                        test.setPrice(BigDecimal.ZERO);
                    }
                }
                listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                return order;
            };
            getJdbcTemplate().query((select.toString() + from.toString() + where.toString()).replace("lab22.", "o."), parameters.toArray(), mapper);
            if (listOrders.isEmpty()) {
                return null;
            } else {
                return listOrders.get(0);
            }
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene ordenes con examenes en estado de retoma buscandolas por fecha de
     * ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @param demographics
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    default List<Order> getByEntryDateRecalled(int date, int branch, final List<Demographic> demographics, boolean handledservice) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            final StringBuilder from = new StringBuilder();
            final StringBuilder where = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab22.lab22c1");
            select.append(", lab22.lab22c2 ");
            select.append(", lab22.lab103c1 ");
            select.append(", lab22.lab103c1 ");
            select.append(", lab57c14 ");//Id Perfil Padre
            select.append(", lab103.lab103c2 ");
            select.append(", lab103.lab103c3 ");
            select.append(", lab103.lab103c4 ");
            select.append(", lab22.lab22c3 ");
            select.append(", lab22.lab21c1 ");
            select.append(", lab22.lab22c4 ");
            select.append(", lab22.lab22c5 ");
            select.append(", lab22.lab22c6 ");
            select.append(", lab22.lab04c1 ");
            select.append(", lab22.lab22c13 ");
            select.append(", lab04.lab04c2 ");
            select.append(", lab04.lab04c3 ");
            select.append(", lab04.lab04c4 ");
            select.append(", lab22.lab07c1 ");
            select.append(", lab22.lab22c7 ");
            select.append(", e.lab39c1 ");
            select.append(", e.lab39c2 ");
            select.append(", e.lab39c4 ");
            select.append(", e.lab39c37 ");
            select.append(", lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10 ");
            select.append(", lab24.lab56c1, lab56.lab56c2 ");
            select.append(", lab21c2, lab21c3, lab21c4, lab21c5, lab21c6,lab21c8, lab21c16, lab21c17,");
            select.append(" lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4, lab80.lab80c5, lab21c7, lab21c14, lab54.lab54c1, lab54.lab54c2, lab54.lab54c3, ");
            select.append(" lab16.lab16c3, lab11.lab11c1 ");
            select.append(", e.lab43c1, lab43c2, lab43c4 ");
            from.append(" FROM  lab57  res ");
            from.append(" JOIN lab22  ON lab22.lab22c1 = res.lab22c1 ");
            from.append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
            from.append(" JOIN lab39 AS e ON e.lab39c1 = res.lab39c1 ");
            from.append(" LEFT JOIN lab43 ON lab43.lab43c1 = e.lab43c1 ");
            from.append(" JOIN lab04 ON lab22.lab04c1 = lab04.lab04c1 ");
            from.append(" LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ");
            from.append(" LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ");
            from.append(" LEFT JOIN lab221 ON lab221.lab22c1_2 = lab22.lab22c1 ");
            from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = e.lab24c1 ");
            from.append(" LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            from.append(" LEFT JOIN lab11 ON lab11.lab24c1 = res.lab24c1 AND lab11.lab22c1 = res.lab22c1 ");
            from.append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
            SQLTools.buildSQLDemographicSelect(demographics, select, from);
            where.append(" WHERE e.lab39c37 != 2 AND res.lab57c16 = 1 ");
            if (handledservice) {
                where.append(" AND (lab10.lab10c8 = 0 OR (lab10.lab10c1 = 0 OR lab10.lab10c8 is null)) ");
            }
            where.append(" AND lab22.lab22c2 = ? ").append(branch != -1 ? " AND lab22.lab05c1 = " + branch : "");
            where.append(" AND lab221.lab22c1_2 IS NULL AND (lab22c19 = 0 or lab22c19 is null)");
            where.append(" AND NOT EXISTS(SELECT 1 FROM lab221 r JOIN lab57 AS res2 ON r.lab22c1_2 = res2.lab22c1 JOIN lab39 AS e2 ON e2.lab39c1 = res2.lab39c1 WHERE r.lab22c1_1 = lab22.lab22c1 AND res2.lab39c1 = res.lab39c1 AND e2.lab39c37 != 2)");
            List parameters = new ArrayList(0);
            parameters.add(date);
            List<Order> listOrders = new ArrayList<>();
            List<Demographic> demographicsFinal = demographics.stream().filter(d -> d.getId() > 0).collect(Collectors.toList());
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                if (!listOrders.contains(order)) {
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setTurn(rs.getString("lab22c13"));

                    OrderType orderType = new OrderType();
                    orderType.setId(rs.getInt("lab103c1"));
                    orderType.setCode(rs.getString("lab103c2"));
                    orderType.setName(rs.getString("lab103c3"));
                    orderType.setColor(rs.getString("lab103c4"));
                    order.setType(orderType);

                    order.setCreatedDate(rs.getTimestamp("lab22c3"));

                    Patient patient = new Patient();
                    patient.setId(rs.getInt("lab21c1"));
                    order.setPatient(patient);
                    //PACIENTE
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
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
                    order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                    order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                    order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                    order.getPatient().setPhoto(rs.getString("lab21c14"));

                    order.setHomebound(rs.getString("lab22c4") == null ? false : (rs.getShort("lab22c4") == 1));
                    order.setMiles(rs.getString("lab22c5") == null ? null : rs.getInt("lab22c5"));
                    order.setLastUpdateDate(rs.getTimestamp("lab22c6"));

                    User user = new User();
                    user.setId(rs.getInt("lab04c1"));
                    user.setName(rs.getString("lab04c2"));
                    user.setLastName(rs.getString("lab04c3"));
                    user.setUserName(rs.getString("lab04c4"));
                    order.setLastUpdateUser(user);

                    order.setActive(rs.getInt("lab07c1") == 1);
                    order.setExternalId(rs.getString("lab22c7"));

                    if (select.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null) {
                        Branch branchAux = new Branch();
                        branchAux.setId(rs.getInt("lab05c1"));
                        branchAux.setCode(rs.getString("lab05c10"));
                        branchAux.setName(rs.getString("lab05c4"));
                        order.setBranch(branchAux);
                    }

                    if (select.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null) {
                        ServiceLaboratory service = new ServiceLaboratory();
                        service.setId(rs.getInt("lab10c1"));
                        service.setCode(rs.getString("lab10c7"));
                        service.setName(rs.getString("lab10c2"));
                        order.setService(service);
                    }

                    if (select.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        Physician physician = new Physician();
                        physician.setId(rs.getInt("lab19c1"));
                        physician.setCode(rs.getString("lab19c22"));
                        physician.setLastName(rs.getString("lab19c2"));
                        physician.setName(rs.getString("lab19c3"));
                        order.setPhysician(physician);
                    }

                    if (select.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null) {
                        Account account = new Account();
                        account.setId(rs.getInt("lab14c1"));
                        account.setNit(rs.getString("lab14c2"));
                        account.setName(rs.getString("lab14c3"));
                        order.setAccount(account);
                    }

                    if (select.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null) {
                        Rate rate = new Rate();
                        rate.setId(rs.getInt("lab904c1"));
                        rate.setCode(rs.getString("lab904c2"));
                        rate.setName(rs.getString("lab904c3"));
                        order.setRate(rate);
                    }

                    DemographicValue demoValue = null;
                    for (Demographic demographic : demographicsFinal) {
                        demoValue = new DemographicValue();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded()) {
                            if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                            }
                        } else {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        if (demographic.getOrigin().equals("O")) {
                            order.getDemographics().add(demoValue);
                        } else {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }
                    listOrders.add(order);
                }
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setProfileId(rs.getInt("lab57c14"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                if (rs.getString("lab24c1") != null) {
                    test.setSample(new Sample());
                    test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                    test.getSample().getContainer().setName(rs.getString("lab56c2"));
                }

                if (rs.getString("lab43c1") != null) {
                    test.setArea(new Area());
                    test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                    test.getArea().setAbbreviation(rs.getString("lab43c2"));
                    test.getArea().setName(rs.getString("lab43c4"));
                }

                Rate rateOrder = listOrders.get(listOrders.indexOf(order)).getRate();
                if (rateOrder != null) {
                    test.setRate(rateOrder);
                    try {
                        test.setPrice(getPriceTest(test.getId(), rateOrder.getId()));
                    } catch (Exception ex) {
                        test.setPrice(BigDecimal.ZERO);
                    }
                }
                listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                return order;
            };
            getJdbcTemplate().query(select.toString() + from.toString() + where.toString(), parameters.toArray(), mapper);
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el numero de orden padre a partir de rellamado
     *
     * @param order numero de la orden
     * @return Numero de orden padre
     * @throws Exception Error en base de datos
     */
    default Long getFatherOrder(long order) throws Exception {
        try {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab22c1_1, lab22c1_2 FROM lab221 WHERE lab22c1_2 = ? ";
            List parameters = new ArrayList(0);
            parameters.add(order);
            return getJdbcTemplate().queryForObject(query, parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1_1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene el numero de orden hoja a partir de rellamado
     *
     * @param order numero de la orden
     * @return Numero de orden hija
     * @throws Exception Error en base de datos
     */
    default List<Long> getDaughterOrder(long order) throws Exception {
        try {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab22c1_1, lab22c1_2 FROM lab221 WHERE lab22c1_1 = ? ";
            List parameters = new ArrayList(0);
            parameters.add(order);
            return getJdbcTemplate().query(query, parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1_2");
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el numero de orden del LIS que corresponde a la orden HIS
     *
     * @param orderhis numero de la orden HIS
     * @return Numero de orden LIS
     * @throws Exception Error en base de datos
     */
    default List<Long> getOrderLisHis(String orderhis) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab22c1 ");
            select.append(" FROM     lab22 ");
            select.append(" WHERE    lab22c7 = ? ");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(orderhis);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    /**
     * Obtiene el numero de orden del LIS que corresponde a la orden HIS
     *
     * @param orderhis numero de la orden HIS
     * @param days
     * @return Numero de orden LIS
     * @throws Exception Error en base de datos
     */
    default List<Long> getOrderLisHisByDays(String orderhis, int days) throws Exception {
        try {

            // Obtener la fecha actual
            LocalDate fechaOriginal = LocalDate.now();

            // Restar 5 días
            LocalDate fechaRestada = fechaOriginal.minusDays(days);

            // Formatear la fecha como "YYYYMMDD"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String fechaActualFormateada = fechaOriginal.format(formatter);
            String fechaFormateada = fechaRestada.format(formatter);

            // Convertir la fecha formateada en un entero
            int fechaEntero = Integer.parseInt(fechaFormateada);
            // Convertir la fecha formateada en un entero
            int fechaActualEntero = Integer.parseInt(fechaActualFormateada);

            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab22c1 ");
            select.append(" FROM     lab22 ");
            select.append(" WHERE    lab22c7 = ? AND (lab22c2 >= ? and lab22c2 <= ?)");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(orderhis);
            parameters.add(fechaActualEntero);
            parameters.add(fechaEntero);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    /**
     * Actualiza el turno de una orden segun la orden his
     *
     * @param order
     * @param turn numero del turno a actualizar.
     *
     * @return Numero de orden actualizada
     * @throws Exception
     */
    default Long updateOrderTurn(long order, String turn) throws Exception {

        List<Object[]> parameters = new ArrayList<>(0);
        String query = "UPDATE lab22 SET lab22c13 = ?, lab22c20 = 0 WHERE lab22c1 = ?";

        parameters.add(new Object[]{
            turn,
            order
        });

        getJdbcTemplate().batchUpdate(query, parameters);
        return order;
    }

    /**
     * Obtiene todos los requerimientos asociados a un examen
     *
     * @param idOrder
     * @param idTest
     * @return Lista de requerimientos del examen
     * @throws Exception Error presentado en el servicio
     */
    default List<String> getRequirements(long idOrder, int idTest) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT R.lab41c3 AS lab41c3 ")
                    .append("FROM lab57 Results ")
                    .append("JOIN lab39 Test ON (Results.lab39c1 = Test.lab39c1) ")
                    .append("JOIN lab71 RT ON (Test.lab39c1 = RT.lab39c1) ")
                    .append("JOIN lab41 R ON (RT.lab41c1 = R.lab41c1) ")
                    .append("WHERE Test.lab39c1 = ").append(idTest).append(" AND Results.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getString("lab41c3");
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene el numero de las ordenes del LIS que corresponde a la orden HIS
     *
     * @param orderhis numero de la orden HIS
     * @return Numero de orden LIS
     * @throws Exception Error en base de datos
     */
    default List<Long> getOrdersLisByIdHis(String orderhis) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("WHERE lab22c7 = ?");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(orderhis);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el numero de las ordenes del LIS que corresponde a la orden HIS
     *
     * @param orderhis numero de la orden HIS
     * @return Numero de orden LIS
     * @throws Exception Error en base de datos
     */
    default List<Long> getValidOrdersByHIS(String orderhis, String branch) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("LEFT JOIN lab05 on lab05.lab05c1 = lab22.lab05c1  ")
                    .append("WHERE lab22c7 = ? AND lab05c10 = ? ");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(orderhis);
            parameters.add(branch);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta un listado de ordenes por un filtro en especifico, obteniendo
     * las ordenes para la realizacion de un pago posterior.
     *
     * @param filter
     * @return Lista de ids de las ordenes
     * @throws Exception Error en el servicio
     */
    default List<Long> subsequentPayments(FilterSubsequentPayments filter) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            StringBuilder where = new StringBuilder();
            List<Object> params = new ArrayList<>();

            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22.lab22c1 AS lab22c1 ")
                    .append("FROM lab22 ")
                    .append("JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ");

            if (filter.isOutstandingBalance()) {
                where.append("WHERE lab902.lab902c9 != 0 ");
            } else {
                where.append("WHERE lab902.lab902c9 = 0 ");
            }

            // Filtro por rango de fechas
            if (filter.getInitialDate() > 0 && filter.getEndDate() > 0) {
                where.append("AND lab22.lab22c2 BETWEEN ").append(filter.getInitialDate()).append(" AND ").append(filter.getEndDate());
            }
            // Filtro por nombre del paciente
            if (!filter.getFirstName().isEmpty()) {
                where.append("AND lab21.lab21c3 = '").append(Tools.encrypt(filter.getFirstName().toUpperCase())).append("' ");
            }
            if (!filter.getSecondName().isEmpty()) {
                where.append("AND lab21.lab21c4 = '").append(Tools.encrypt(filter.getSecondName().toUpperCase())).append("' ");
            }
            if (!filter.getFirstSurname().isEmpty()) {
                where.append("AND lab21.lab21c5 = '").append(Tools.encrypt(filter.getFirstSurname().toUpperCase())).append("' ");
            }
            if (!filter.getSecondSurname().isEmpty()) {
                where.append("AND lab21.lab21c6 = '").append(Tools.encrypt(filter.getSecondSurname().toUpperCase())).append("' ");
            }
            // Filtro por demografico
            if (filter.getDemographic() != null) {

                String sqlDemographic = SQLTools.buildSQLDemographicFilter(filter.getDemographic(), params);
                where.append(sqlDemographic);
            }

            return getJdbcTemplate().query(select.toString() + where.toString(), params.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene lista de ordenes por rango de ingreso
     *
     * @param filter Objeto que representa el filtro de las ordenes
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<Long> getOrdersByDate(SigaFilterOrders filter) throws Exception {
        try {
            Date dateNow = new Date(System.currentTimeMillis());
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                    .append("INNER JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("WHERE lab22.lab22c13 = '' and lab21.lab21c2 = ? ")
                    .append("AND lab22.lab22c3 between ? AND ?  AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append(filter.getType() != null ? " AND lab54.lab54c5 = ? " : "");

            List<Object> parameters = new ArrayList<>(0);
            parameters.add(Tools.encrypt(filter.getHistory()));
            parameters.add(filter.getDateFilter());
            parameters.add(dateNow);
            if (filter.getType() != null) {
                parameters.add(Integer.parseInt(filter.getType()));
            }
            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el procentaje de un paciente
     *
     * @param test Id del examen
     * @param rate Id de la tarifa
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default Double getPatientPercentageTest(int test, int rate) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab55c2")
                    .append(" FROM lab55 ")
                    .append("WHERE lab39c1 = ").append(test)
                    .append(" AND lab904c1 = ").append(rate);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    -> {
                return rs.getDouble("lab55c2");
            });
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error("error" + ex);
            return 0.0;
        }
    }

    /**
     * Obtiene el procentaje de un paciente
     *
     * @param test Id del examen
     * @param rate Id de la tarifa
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default Double getTaxTest(int test) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab39c49")
                    .append(" FROM lab39 ")
                    .append("WHERE lab39c1 = ").append(test);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    -> {
                return rs.getDouble("lab39c49");
            });
        } catch (EmptyResultDataAccessException ex) {
            return 0.0;
        }
    }

    /**
     * Obtiene lista de ids de las ordenes por rango de fechas
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<Long> getIdOrders(String startDate, String endDate) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT DISTINCT lab22c1 ")
                    .append("FROM lab57 ")
                    .append("WHERE lab22c1 BETWEEN ").append(startDate).append(" AND ")
                    .append(endDate)
                    .append(" AND ( lab57c68 = 0  OR lab57c68 IS NULL )");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene lista de ids de las ordenes por rango de fechas Y laboratorio
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<IdsPatientOrderTest> getIdOrdersByLaboratory(long startDate, long endDate, int laboratoryID) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append(" SELECT  DISTINCT lab57.lab22c1 AS lab22c1 , ")
                    .append(" lab21c1")
                    .append(" FROM LAB57 ")
                    .append(" inner join  lab22 on lab57.lab22c1 = lab22.lab22c1 ")
                    .append(" WHERE lab57.lab22c1 between ")
                    .append(String.valueOf(startDate))
                    .append(" AND ")
                    .append(String.valueOf(endDate))
                    .append(" AND lab57.lab40C1 = ").append(String.valueOf(laboratoryID))
                    .append(" AND lab57c68 = 0  AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append(" order by lab22c1");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    -> {
                IdsPatientOrderTest idsPatientOrderTest = new IdsPatientOrderTest();
                idsPatientOrderTest.setOrderNumber(rs.getLong("lab22c1"));
                idsPatientOrderTest.setIdPatient(rs.getLong("lab21c1"));

                return idsPatientOrderTest;
            });
        } catch (Exception ex) {
            OrderCreationLog.info("ERROR POR " + ex);
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene lista de ids de las ordenes por rango de fechas Y laboratorio
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<Long> getIdTestbyOrderAndLaboratory(long numberOrden, long idLaboratory) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append(" SELECT lab39c1 ")
                    .append(" FROM LAB57 ")
                    .append(" WHERE lab22c1 =  ")
                    .append(String.valueOf(numberOrden))
                    .append(" AND ")
                    .append(" lab40C1 = ")
                    .append(String.valueOf(idLaboratory));

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab39c1");
            });
        } catch (Exception ex) {
            OrderCreationLog.info("ERROR POR " + ex);
            return new ArrayList<>();
        }
    }

    /**
     * Actauliza el estado de examenes para integracion con kbits
     *
     * @param numberOrden Numero de orden
     * @param idTest Id del examen
     * @param status Estado. 0-> No enviar, 1-> Enviar a Kbits, 2-> Combo no
     * facturada, 3-> Combo facturada, 4 -> Enviado
     * @return Long
     * @throws Exception Error presentado en base de datos
     */
    default boolean updateOrderStatus(long numberOrden, int idTest, int status) throws Exception {
        try {

            Integer year = Tools.YearOfOrder(String.valueOf(numberOrden));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder select = new StringBuilder();
            if (idTest > 0) {
                select.append(" UPDATE ").append(lab57).append(" SET lab57c68 = ? WHERE lab22c1 = ? AND lab39c1 = ? ");
                getJdbcTemplate().update(select.toString(), status, numberOrden, idTest);
            } else {
                select.append(" UPDATE ").append(lab57).append(" SET lab57c68 = ? WHERE lab22c1 = ?");
                getJdbcTemplate().update(select.toString(), status, numberOrden);
            }
            return true;
        } catch (Exception e) {
            OrderCreationLog.info("Error actualización estado Kbits " + e);
            return false;
        }
    }

    /**
     * Por medio del id del examen se verifica la existencia de este examen
     * dentro de la tabla de concurrencia
     *
     * @param order
     * @return Booleano -> Existencia de la factura del cleinte para la ordem
     * @throws java.lang.Exception
     */
    default boolean getBillingAccountByOrder(Long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT 1 AS EXIST ")
                    .append("FROM  ").append(lab900).append(" as lab900 ")
                    .append("WHERE lab901c1i is not null AND lab22c1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getInt("EXIST") == 1;
            });
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Consultamos el id del paciente asociado a una orden por el id de la misma
     *
     * @param order
     * @return Id del paciente
     * @throws java.lang.Exception
     */
    default int getIdPatientByOrderNumber(Long order) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c1")
                    .append(" FROM lab22 ")
                    .append("WHERE lab22c1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab21c1");
            });
        } catch (DataAccessException e) {
            return -1;
        }
    }

    /**
     * Obtiene los examenes relacionados a una muestra que estan verificados
     *
     * @param sample numero de la muestra
     * @return lista de examenes
     * @throws Exception Error en base de datos
     */
    default List<Integer> getTestSample(Long order, int sample) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab57.lab39c1 ");
            select.append(" FROM     lab57 ");
            select.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            select.append(" WHERE   lab57.lab24c1 = ? AND lab39.lab39c37 = 0 AND lab57.lab57c16 = 4 AND lab22c1 = ?");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(sample);
            parameters.add(order);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab39c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene los examenes relacionados a una muestra que estan verificados
     *
     * @param sample numero de la muestra
     * @return lista de examenes
     * @throws Exception Error en base de datos
     */
    default List<Integer> getTestSampleCode(Long order, String sampleCode) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab57.lab39c1 ");
            select.append(" FROM  lab57 ");
            select.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            select.append(" INNER JOIN lab24 ON  lab57.lab24c1 = lab24.lab24c1 ");
            select.append(" WHERE lab24.lab24c9 = ? AND lab39.lab39c37 = 0 AND lab57.lab57c16 = 4 AND lab22c1 = ?");
            List<Object> parameters = new ArrayList<>(0);
            parameters.add(sampleCode);
            parameters.add(order);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab39c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    /**
     * Obtiene los examenes relacionados a una muestra que estan verificados
     *
     * @param sample numero de la muestra
     * @return lista de examenes
     * @throws Exception Error en base de datos
     */
    default List<Integer> getTestSampleTake(Long order, List<Integer> sample) throws Exception {
        try {
            final StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);

            select.append("SELECT   lab57.lab39c1 ");
            select.append(" FROM    lab57 ");
            select.append(" WHERE   lab57.lab24c1 in ( ").append(sample.stream().map(s -> s.toString()).collect(Collectors.joining(","))).append(") AND lab57.lab57c16 = 3 AND lab22c1 = ?");

            List<Object> parameters = new ArrayList<>(0);
            parameters.add(order);

            return getJdbcTemplate().query(select.toString(), parameters.toArray(), (ResultSet rs, int i)
                    -> {
                return rs.getInt("lab39c1");
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    /**
     * Se encarga de volver el campo lab22c14 a nulo - SU VALOR ORIGINAL
     *
     * @param idOrder
     *
     * @throws java.lang.Exception Error de base de datos
     */
    default void returnToOriginalState(long idOrder) throws Exception {
        try {
            getJdbcTemplate().update("UPDATE lab22 SET lab22c14 = NULL, lab22c20 = 0 WHERE lab22c1 = ?", idOrder);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Obtiene una lista de ordenes con inconcistencias
     *
     * @param branch
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacia si
     * no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<InconsistentOrder> listOrdersInconsistencies(Integer branch) throws Exception {
        String select = "SELECT c.lab22c1, "
                + "c.lab22c2, "
                + "c.lab22c3, "
                + "c.lab22c4, "
                + "c.lab22c5, "
                + "c.lab22c6, "
                + "c.lab07c1, "
                + "c.lab22c7, "
                + "c.lab22c10, "
                + "c.lab22c11, "
                + "c.lab21c1, "
                + "c.lab103c1, "
                + "lab103.lab103c2, "
                + "lab103.lab103c3, "
                + "lab103.lab103c4, "
                + "c.lab04c1, "
                + "lab04.lab04c2, "
                + "lab04.lab04c3, "
                + "lab04.lab04c4,  "
                + "lab21.lab21c1, "
                + "lab21c2, "
                + "lab21c3, "
                + "lab21c4, "
                + "lab21c5, "
                + "lab21c6, "
                + "lab21c8, "
                + "lab21c16, "
                + "lab21c17, "
                + "lab80.lab80c1, "
                + "lab80.lab80c2, "
                + "lab80.lab80c3, "
                + "lab80.lab80c4, "
                + "lab80.lab80c5, "
                + "lab21c7, "
                + "lab21c14, "
                + "lab54.lab54c1, "
                + "lab54.lab54c2, "
                + "lab54.lab54c3, "
                + "lab05.lab05c1, "
                + "lab05.lab05c10, "
                + "lab05.lab05c4,"
                + "lab10.lab10c1, "
                + "lab10.lab10c7, "
                + "lab10.lab10c2, "
                + "lab19.lab19c1, "
                + "lab19.lab19c2, "
                + "lab19.lab19c3, "
                + "lab14.lab14c1, "
                + "lab14.lab14c2, "
                + "lab14.lab14c3, "
                + "(select b.lab22c1_1 from lab22 AS a INNER JOIN lab221 AS b ON a.lab22c1 = b.lab22c1_2 where lab22c1 = c.lab22c1) AS fathetOrder, "
                + "lab904.lab904c1, "
                + "lab904.lab904c2, "
                + "lab904.lab904c3 ";
        String from = "FROM     lab22 AS c "
                + " INNER JOIN lab103 ON c.lab103c1 = lab103.lab103c1 "
                + " INNER JOIN lab04 ON c.lab04c1 = lab04.lab04c1 "
                + " INNER JOIN lab21 ON lab21.lab21c1 = c.lab21c1 "
                + " LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 "
                + " LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 "
                + " LEFT JOIN lab05 ON c.lab05c1 = lab05.lab05C1 "
                + " LEFT JOIN lab10 ON c.lab10c1 = lab10.lab10C1 "
                + " LEFT JOIN lab19 ON c.lab19c1 = lab19.lab19C1 "
                + " LEFT JOIN lab14 ON c.lab14c1 = lab14.lab14C1 "
                + " LEFT JOIN lab904 ON c.lab904c1 = lab904.lab904C1 ";
        String where = "WHERE c.lab07c1 != 0 AND c.lab21c1 != 0 AND c.lab22c10 = 1 and c.lab05c1 = " + branch + " AND (c.lab22c19 = 0 or c.lab22c19 is null)";

        try {
            return getJdbcTemplate().query(select + from + where, (ResultSet rs, int i)
                    -> {
                InconsistentOrder order = new InconsistentOrder();
                order.setOrderNumber(rs.getLong("lab22c1"));

                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                order.setPatient(patient);

                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                order.getPatient().setOrderNumber(order.getOrderNumber());
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
                order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                order.getPatient().getDocumentType().setAbbr(rs.getString("lab54c2"));
                order.getPatient().getDocumentType().setName(rs.getString("lab54c3"));
                order.getPatient().setPhoto(rs.getString("lab21c14"));

                return order;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la ultima orden de un pacyeiente buscandolas por fecha de ingreso
     * y sede
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @param patient Id del paciente
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacio en caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    public LastOrderPatient getLastOrder(int date, int branch, int patient) throws Exception;

    /**
     * Obtiene el numero de las ordenes del LIS que corresponde a la orden HIS
     *
     * @param orderhis numero de la orden HIS
     * @param branch
     * @return Numero de orden LIS
     * @throws Exception Error en base de datos
     */
    default List<Long> getOrdersLisByIdHis(String orderhis, String branch) throws Exception {
        try {
            StringBuilder select = new StringBuilder();
            select.append("SELECT lab22c1")
                    .append(" FROM lab22 ")
                    .append("INNER JOIN LAB05 ON LAB22.LAB05C1 = LAB05.LAB05C1 ")
                    .append("WHERE lab22c7 = '").append(orderhis).append("'")
                    .append(" AND LAB05.lab05c10 = '").append(branch).append("'");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    -> {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el tipo de orden de una orden
     *
     * @param order Id orden
     *
     * @return Tipo de orden
     * @throws Exception Error
     */
    default OrderType getOrderTypeByOrder(long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab22.lab103c1, lab103c2, lab103c3")
                    .append(" FROM ").append(lab22).append(" as lab22 ")
                    .append(" JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ")
                    .append(" WHERE lab22c1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    -> {
                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                orderType.setCode(rs.getString("lab103c2"));
                orderType.setName(rs.getString("lab103c3"));
                return orderType;
            });
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error("error" + ex);
            return null;
        }
    }

    /**
     * Obtiene el servicio de una orden
     *
     * @param order Id orden
     *
     * @return Tipo de orden
     * @throws Exception Error
     */
    default ServiceLaboratory getServiceByOrder(long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab22.lab10c1, lab10c2, lab10c8")
                    .append(" FROM ").append(lab22).append(" as lab22 ")
                    .append(" JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 ")
                    .append(" WHERE lab22c1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    -> {
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                service.setName(rs.getString("lab10c2"));
                service.setHospitalSampling(rs.getInt("lab10c8") == 1);
                return service;
            });
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error("error" + ex);
            return null;
        }
    }

    /**
     * Obtiene el precio de un examen por tarifa y la vigencia activa
     *
     * @param test
     * @param rate
     * @param tax
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default TestPrice getPriceTest(int test, int rate, int tax) throws Exception {
        try {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + " SELECT   lab55c1, lab55c2, lab39c49 "
                    + " FROM     lab55 "
                    + " LEFT JOIN lab39 ON lab39.lab39c1 = lab55.lab39c1 "
                    + " WHERE lab55.lab39c1 = ? AND lab904c1 = ?";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i)
                    -> {
                TestPrice price = new TestPrice();
                price.setServicePrice(rs.getBigDecimal("lab55c1"));
                price.setPatientPercentage(rs.getBigDecimal("lab55c2"));

                BigDecimal patientPrice = rs.getBigDecimal("lab55c1").multiply(rs.getBigDecimal("lab55c2")).divide(new BigDecimal(100));
                price.setPatientPrice(patientPrice);

                BigDecimal insurancePrice = rs.getBigDecimal("lab55c1").subtract(patientPrice);
                price.setInsurancePrice(insurancePrice);

                price.setTax(rs.getString("lab39c49") == null ? tax : rs.getDouble("lab39c49") == 0.0 ? tax : rs.getDouble("lab39c49"));

                return price;
            }, test, rate);

        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Obtiene el precio de una lista de examenes por tarifa y la vigencia
     * activa
     *
     * @param filter Filtros
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default List<TestPrice> getPricesTests(FilterTestPrice filter, int tax) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT  lab55.lab39c1, lab55c1, lab55c2, lab39c49 ");

            StringBuilder from = new StringBuilder();

            from.append(" FROM   lab55 ");
            from.append(" LEFT JOIN lab39 ON lab39.lab39c1 = lab55.lab39c1 ");

            from.append(" WHERE lab904c1 = ").append(filter.getRateId());

            from.append(" AND lab55.lab39c1 in (").append(filter.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");

            List<TestPrice> prices = new ArrayList<>();
            RowMapper mapper = (RowMapper<TestPrice>) (ResultSet rs, int i)
                    -> {
                TestPrice price = new TestPrice();

                price.setTestId(rs.getInt("lab39c1"));

                price.setServicePrice(rs.getBigDecimal("lab55c1"));
                price.setPatientPercentage(rs.getBigDecimal("lab55c2"));

                BigDecimal patientPrice = rs.getBigDecimal("lab55c1").multiply(rs.getBigDecimal("lab55c2")).divide(new BigDecimal(100));
                price.setPatientPrice(patientPrice);

                BigDecimal insurancePrice = rs.getBigDecimal("lab55c1").subtract(patientPrice);
                price.setInsurancePrice(insurancePrice);

                price.setTax(rs.getString("lab39c49") == null ? tax : rs.getDouble("lab39c49") == 0.0 ? tax : rs.getDouble("lab39c49"));

                prices.add(price);
                return price;
            };

            getJdbcTemplate().query(query.toString() + " " + from.toString(), mapper);

            return prices;
        } catch (EmptyResultDataAccessException ex) {
            EventsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta los medicos auxiliares de una orden
     *
     * @param auxiliaryPhysicians Lista de medicos auxiliares
     * @param idOrder Id de la orden
     * @throws Exception Error en base de datos
     */
    default void insertAuxiliaryPhysicians(List<Physician> auxiliaryPhysicians, Long idOrder) throws Exception {
        try {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab194");
            auxiliaryPhysicians.stream().map((physician)
                    -> {
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", idOrder);
                parameters.put("lab19c1", physician.getId());
                parameters.put("lab194c1", physician.getIdDemoAux());

                return parameters;
            }).forEachOrdered((parameters)
                    -> {
                batchArray.add(parameters);
            });

            insert.executeBatch(batchArray.toArray(new HashMap[auxiliaryPhysicians.size()]));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Obtiene el precio de una lista de examenes por tarifa y la vigencia
     * activa
     *
     * @param idOrder Id de la orden
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default List<Physician> getAuxiliaryPhysicians(Long idOrder) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT  lab19.lab19c1, lab19c22, lab19c2, lab19c3, lab19c21, lab194c1 ");

            StringBuilder from = new StringBuilder();

            from.append(" FROM   lab194 ");
            from.append(" LEFT JOIN lab19 ON lab19.lab19c1 = lab194.lab19c1 ");

            from.append(" WHERE lab194.lab22c1 = ").append(idOrder);

            List<Physician> list = new ArrayList<>();
            RowMapper mapper = (RowMapper<Physician>) (ResultSet rs, int i)
                    -> {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setCode(rs.getString("lab19c22"));
                physician.setLastName(rs.getString("lab19c3"));
                physician.setName(rs.getString("lab19c2") + " " + rs.getString("lab19c3"));
                physician.setEmail(rs.getString("lab19c21"));
                physician.setIdDemoAux(rs.getInt("lab194c1"));

                list.add(physician);
                return physician;
            };

            getJdbcTemplate().query(query.toString() + " " + from.toString(), mapper);

            return list;
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el precio de una lista de examenes por tarifa y la vigencia
     * activa
     *
     * @param idOrder Id de la orden
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    default List<Physician> getAuxiliaryPhysiciansReport(Long idOrder) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT  lab19.lab19c1, lab19c22, lab19c2, lab19c3, lab19c21, lab194c1 ");

            StringBuilder from = new StringBuilder();

            from.append(" FROM   lab194 ");
            from.append(" LEFT JOIN lab19 ON lab19.lab19c1 = lab194.lab19c1 ");

            from.append(" WHERE lab194.lab22c1 = ").append(idOrder);

            List<Physician> list = new ArrayList<>();
            RowMapper mapper = (RowMapper<Physician>) (ResultSet rs, int i)
                    -> {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setCode(rs.getString("lab19c22"));
                physician.setLastName(rs.getString("lab19c3"));
                physician.setName(rs.getString("lab19c2"));
                physician.setEmail(rs.getString("lab19c21"));
                physician.setIdDemoAux(rs.getInt("lab194c1"));

                list.add(physician);
                return physician;
            };

            getJdbcTemplate().query(query.toString() + " " + from.toString(), mapper);

            return list;
        } catch (EmptyResultDataAccessException ex) {
            OrderCreationLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Eliminar los medicos auxiliares de una orden
     *
     * @param idOrder Id de la orden
     */
    default void deleteAuxiliaryPhysicians(Long idOrder) throws Exception {
        String query = " DELETE FROM lab194 WHERE lab22c1 = " + idOrder;
        getJdbcTemplate().execute(query);
    }

    /**
     * Inserta la auditoria de los paquetes de una orden
     *
     * @param tracking Lista de auditoria
     * @param idUser Id del usuario
     * @throws Exception Error en base de datos
     */
    default void packageTracking(List<PackageTracking> tracking, Integer idUser) throws Exception {
        try {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab301")
                    .usingGeneratedKeyColumns("lab301c1");

            tracking.stream().map((t)
                    -> {
                HashMap parameters = new HashMap();
                parameters.put("lab301c2", t.getIdPackage());
                parameters.put("lab301c3", t.getIdChilds());
                parameters.put("lab301c4", t.getPartial());
                parameters.put("lab04c1", idUser);
                parameters.put("lab301c5", timestamp);
                parameters.put("lab22c1", t.getOrder());

                return parameters;
            }).forEachOrdered((parameters)
                    -> {
                batchArray.add(parameters);
            });

            insert.executeBatch(batchArray.toArray(new HashMap[tracking.size()]));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Obtiene la auditoria de paquetes de una orden
     *
     * @param idOrder
     * @return Lista de auditoria, vacia si no se encuentra ningun registro
     * @throws Exception Error en base de datos
     */
    default List<PackageTracking> getPackageTracking(Long idOrder) throws Exception {
        List<PackageTracking> list = new LinkedList<>();

        List<Object> params = new ArrayList<>();

        String select = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT "
                + " lab301c1 "
                + " , lab301c2 "
                + " , lab39c2 "
                + " , lab39c4 "
                + " , lab301c3"
                + " , lab301c4 "
                + " , lab301.lab04c1 "
                + " , lab04c2 "
                + " , lab04c3 "
                + " , lab04c4 "
                + " , lab301c4 "
                + " , lab301c5 "
                + " , lab22c1 ";

        String from = "FROM lab301 "
                + " INNER JOIN lab04 ON lab301.lab04c1 = lab04.lab04c1 "
                + " INNER JOIN lab39 ON lab301.lab301c2 = lab39.lab39c1 ";

        String where = "WHERE lab22c1 = ? ";
        params.add(idOrder);

        try {
            getJdbcTemplate().query(select + from + where, params.toArray(), (ResultSet rs, int i)
                    -> {
                PackageTracking tracking = new PackageTracking();
                tracking.setId(rs.getInt("lab301c1"));
                tracking.setIdPackage(rs.getInt("lab301c2"));
                tracking.setCodePackage(rs.getString("lab39c2"));
                tracking.setNamePackage(rs.getString("lab39c4"));
                tracking.setIdChilds(rs.getString("lab301c3"));
                tracking.setPartial(rs.getInt("lab301c4"));
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                tracking.setUser(user);
                tracking.setLastTransaction(rs.getTimestamp("lab301c5"));
                tracking.setOrder(rs.getLong("lab22c1"));

                list.add(tracking);
                return tracking;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * Actualiza el estado de la orden
     *
     * @param orders lista de ordenes
     * @param state estado a actualizar
     *
     * @return Lista de ordenes actualizadas
     * @throws Exception
     */
    default int updateOrderStateByOrder(long order, LISEnum.ResultOrderState state) throws Exception {

        List<Object[]> parameters = new ArrayList<>(0);
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

        String query = "UPDATE " + lab22 + " SET lab22c9 = lab07c1, lab07c1 = ?, lab22c20 = 0 WHERE lab22c1 = ?";

        getJdbcTemplate().update(query, state.getValue(), order);
        return 1;
    }
}
