/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.appointment;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


public interface AppointmentDao {
    
    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
    */
    public JdbcTemplate getJdbcTemplate();
    
     /**
     * Lista de citas en un rango de fechas para las calificaciones de los
     * servicios.
     *
     * @param init rango inicial
     * @param end rango final
     * @param branch
     * @return lista de citas
     */
    default List<Appointment> listAppointmentByDateBranch(int init, int end, int branch)
    {
        try
        {
            String query = "SELECT   hmb12c1, hmb12c2, hmb09c1,  hmb12c4 "
                    + "FROM     hmb12 "
                    + "WHERE hmb12c2 BETWEEN ? AND ? AND lab05c1 = ?";
            
            
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("hmb12c1"));
                appointment.setDate(rs.getInt("hmb12c2"));
                appointment.setShift(new Shift());
                appointment.getShift().setId(rs.getInt("hmb09c1"));
                appointment.setState(rs.getInt("hmb12c4"));
              
                return appointment;
            }, init, end, branch);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
     /**
     * Lista ausencias programadas en un rango de fechas
     *
     * @param branch
     * @param idShift Id Jornada.
     * @param date Fecha.
     * @return lista de ausencias
     */
    default int countConcurrence(int branch, int idShift, int date)
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "select ((SELECT COUNT(*) FROM hmb13 WHERE  hmb13.lab05c1 = ? AND hmb13.hmb09c1 = ? AND hmb13.hmb13c1 = ?) + " 
                    + "(SELECT COUNT(*) FROM hmb12 WHERE hmb12c4 != ? AND hmb12.lab05c1 = ? AND hmb12.hmb09c1 = ? AND hmb12.hmb12c2 = ?)) as quantity ", 
                    (ResultSet rs, int i) -> 
                            rs.getInt("quantity"), 
                    branch, idShift, date,LISEnum.AppointmentState.CANCELED.getValue(), branch, idShift, date);
        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }
    
    /**
     * Registra una concurrencia para citas
     *
     * @param appointment informacion de la concurrencia para citas
     * @param user Usuario que registro
     * @throws Exception Error en la base de datos
     */
    default int createConcurrence(Appointment appointment, int user) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("hmb13")
                .usingGeneratedKeyColumns("hmb13c2")
                .usingColumns("lab05c1", "hmb09c1", "hmb13c1", "hmb03c1_1");
        
        HashMap parameters = new HashMap();
        parameters.put("lab05c1", appointment.getBranch().getId());
        parameters.put("hmb09c1", appointment.getShift().getId());
        parameters.put("hmb13c1", appointment.getDate());
        parameters.put("hmb03c1_1", user);
        //insert.execute(parameters);
        Number key = insert.executeAndReturnKey(parameters);
        
        return key.intValue();
    }
    
    /**
     * Consulta todos los demográficos almacenados en el sistema
     *
     * @param date
     * @param branchId
     * @param initappointment
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
    */
    default int getSecuenceOrder(int date, int branchId, long initappointment) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(date));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                        
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED)
                    .append(" select COUNT(lab22c1) as count")
                    .append(" from lab22 ")
                    .append(" where lab22c2 = ").append(date).append(" AND lab05c1 = ").append(branchId).append(" AND lab22c1 >=  ").append(initappointment);

            return getJdbcTemplate().queryForObject(query.toString(),
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
     * Elimina concurrencias registradas
     *
     * @param deleteAll Indica si se elimina toda la información de las
     * concurrencias.
     * @param date Fecha de la cita yyyyMMdd
     * @param idShift Id jornada.
     * @param branch
     * @param idUser Id del usuario que registro.
     * @return registros afectados
     * @throws Exception Error en la base de datos
     */
    default int deleteConcurrence(boolean deleteAll, Integer date, Integer idShift, Integer branch, Integer idUser) throws Exception
    {
        List<Object> params = new ArrayList<>();
        if (deleteAll)
        {   
            return getJdbcTemplate().update("DELETE FROM hmb13");
        } else
        {
            String delete = "DELETE FROM hmb13 WHERE ";
            if (date != null && date > 0)
            {
                delete += " hmb13c1 = ? AND ";
                params.add(date);
            }
            
            if (idShift != null && idShift > 0)
            {
                delete += " hmb09c1 = ? AND ";
                params.add(idShift);
            }
            
            if (branch != null && branch > 0)
            {
                delete += " lab05c1 = ? AND ";
                params.add(branch);
            }
            
            
           
            return getJdbcTemplate().update(delete.substring(0, delete.length() - 4), params.toArray());
        }
    }

    /**
     * Elimina concurrencias registradas
     *
     * @param branch
     * @param date Fecha de la cita yyyyMMdd
     * @param idShift Id jornada.
     * @return registros afectados
     * @throws Exception Error en la base de datos
     */
    default int deleteConcurrence(Integer branch, Integer idShift, Integer date) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM hmb13 WHERE hmb13c1 = ? AND hmb09c1 = ? AND lab05c1 = ?", date, idShift, branch);
    }
    
     /**
     * Elimina concurrencias registradas por id
     *
     * @param idConcurrence
     * @return registros afectados
     * @throws Exception Error en la base de datos
     */
    default int deleteConcurrenceByid(Integer idConcurrence) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM hmb13 WHERE hmb13c2 = ?  ", idConcurrence);
    }
    
     /**
     * Registra una cita
     *
     * @param appointment informacion de la cita
     * @param user Usuario que registro
     * @return Cita creada.
     * @throws Exception Error en la base de datos
     */
    default Appointment createAppointment(Appointment appointment, int user) throws Exception
    {
   
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("hmb12")
                    .usingGeneratedKeyColumns("hmb12c1");
            

            HashMap parameters = new HashMap();
            parameters.put("lab05c1", appointment.getBranch().getId());
            parameters.put("hmb09c1", appointment.getShift().getId());
            parameters.put("hmb12c2", appointment.getDate());
            parameters.put("hmb12c3", timestamp);
            parameters.put("lab04c1", user);
            parameters.put("hmb12c4", LISEnum.AppointmentState.REGISTERED.getValue());
            parameters.put("hmb12c5", appointment.getOrderNumber());

            Number key = insert.executeAndReturnKey(parameters);
            appointment.setState(LISEnum.AppointmentState.REGISTERED.getValue());
            appointment.setId(key.intValue());
            insertTrackingAppointment(user, appointment);       
            return appointment;
        }
        catch (EmptyResultDataAccessException ex)
        {
            
            return null;
        }
    }
    
    /**
     * Actualiza la cita.
     *
     * @param appointment Cita que se actualizara
     * @param idUser Usuario que realizo la acción
     * @return Cita actualizada
     *
     * @throws Exception Error en la base de datos
     */
    default Appointment updateAppointment(Appointment appointment, int idUser) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        
        getJdbcTemplate().update("UPDATE hmb12 SET hmb09c1 = ?, "
                + "hmb12c2 = ?, "
                + "hmb12c3 = ?, "
                + "lab04c1 = ?, "
                + "hmb12c4 = ?, "
                + "hmb12c5 = ? "
                + "WHERE hmb12c1 = ? ",
                appointment.getShift().getId(), 
                appointment.getDate(), 
                timestamp, 
                idUser, 
                appointment.getState(),
                appointment.getOrderNumber(), 
                appointment.getId()
        );
        insertTrackingAppointment(idUser, appointment);
        return appointment;
    }
    
     /**
     * Actualiza el estado y trazabilidad de una orden.
     *

     * @param idUser Usuario que registro
     * @param appointment Datos de la cita
     * @return Trazabilidad
     *
     * @throws Exception Error en la base de datos
     */
    default Appointment insertTrackingAppointment(int idUser, Appointment appointment) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("hmb16");
        
        HashMap parameters = new HashMap();
        parameters.put("hmb12c1", appointment.getId());
        parameters.put("hmb16c1", appointment.getState());
        parameters.put("hmb17c1", appointment.getReason() == null || appointment.getReason().getId() == null ? null : appointment.getReason().getId());
        parameters.put("hmb16c2", appointment.getReason() == null || appointment.getReason().getDescription() == null ? null : appointment.getReason().getDescription());
        parameters.put("hmb16c3", timestamp);
        parameters.put("hmb03c1", idUser);
        parameters.put("hmb03c1_1", appointment.getBranch().getId());
        parameters.put("hmb16c4", appointment.getDate());
        
        insert.execute(parameters);
        
        return appointment;
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
                    + ", hmb12.hmb12c1, hmb12.hmb12c2 "
                    + ", hmb12.hmb09c1, hmb09c2, hmb09c4,hmb09c5 "
                    + " FROM  " + lab22 + " as lab22 "
                    + "INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "INNER JOIN hmb12 on lab22.lab22c1 = hmb12.hmb12c5  "
                    + "INNER JOIN hmb09 on hmb12.hmb09c1 = hmb09.hmb09c1 "
                    + "WHERE lab22.lab07c1 = 1 "
                    + "AND lab22.lab22c2 = ? "
                    + "AND (lab22c19 = 1 ) "
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
                
                orderR.getAppointment().setId(rs.getInt("hmb12c1"));
                orderR.getAppointment().setDate(rs.getInt("hmb12c2"));
                Shift bean = new Shift();
                bean.setId(rs.getInt("hmb09c1"));
                bean.setName(rs.getString("hmb09c2"));
                bean.setInit(rs.getInt("hmb09c4"));
                bean.setEnd(rs.getInt("hmb09c5"));
                
                orderR.getAppointment().setShift(bean);
                
                return orderR;
            }, date);
        } catch (EmptyResultDataAccessException ex) {
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
                    + " , hmb12.hmb12c1 , hmb12.hmb12c2 "
                    + " , hmb12.hmb09c1, hmb09c2, hmb09c4,hmb09c5 "
                    + " FROM " + lab22 + " AS lab22"
                    + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "         INNER JOIN hmb12 on lab22.lab22c1 = hmb12.hmb12c5  "
                    + "         INNER JOIN hmb09 on hmb12.hmb09c1 = hmb09.hmb09c1 "
                    + "WHERE    lab22.lab07c1 = 1  AND (lab22c19 = 1) "
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
                
                orderR.getAppointment().setId(rs.getInt("hmb12c1"));
                orderR.getAppointment().setDate(rs.getInt("hmb12c2"));
                
                Shift bean = new Shift();
                bean.setId(rs.getInt("hmb09c1"));
                bean.setName(rs.getString("hmb09c2"));
                bean.setInit(rs.getInt("hmb09c4"));
                bean.setEnd(rs.getInt("hmb09c5"));
                
                orderR.getAppointment().setShift(bean);
                
                return orderR;
            }, order);
        } catch (EmptyResultDataAccessException ex) {
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
                        + " , hmb12.hmb12c1 , hmb12.hmb12c2 "
                        + " , hmb12.hmb09c1, hmb09c2, hmb09c4,hmb09c5 "
                        + "FROM      " + lab22 + " as lab22 "
                        + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                        + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                        + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                        + "         INNER JOIN hmb12 on lab22.lab22c1 = hmb12.hmb12c5  "
                        + "         INNER JOIN hmb09 on hmb12.hmb09c1 = hmb09.hmb09c1 "
                        + "WHERE    lab22.lab07c1 = 1  AND (lab22c19 = 1) "
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
                    
                    order.getAppointment().setId(rs.getInt("hmb12c1"));
                    order.getAppointment().setDate(rs.getInt("hmb12c2"));
                
                    Shift bean = new Shift();
                    bean.setId(rs.getInt("hmb09c1"));
                    bean.setName(rs.getString("hmb09c2"));
                    bean.setInit(rs.getInt("hmb09c4"));
                    bean.setEnd(rs.getInt("hmb09c5"));

                    order.getAppointment().setShift(bean);
                    
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
     * Actualiza el estado de la orden
     *
     * @param order
     *
     * @return Lista de ordenes actualizadas
     * @throws Exception
     */
    default Order updateOrderState(Order order) throws Exception {

        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
    
        getJdbcTemplate().update("UPDATE " + lab22 + " SET lab22c19 = 0 WHERE lab22c1 = ?",
                order.getOrderNumber()
        );
        return order;
    }

}
