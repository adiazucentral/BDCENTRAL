/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.appointment;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;

import net.cltech.enterprisent.domain.operation.appointment.Availability;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;


public interface AppointmentService {
    
     public List<Availability> listAvailabilityRange(int idBranch, int date, int quantity) throws Exception;
     
     /**
     * Obtiene la disponibilidad de ruteros con respecto a la zona y fecha
     *
     * @param branch
     * @param date Fecha Agendamiento
     * @param day
     * @return Disponibilidad
     * @throws Exception Error en el servicio
     */
    public Availability getAvailability(int branch, int date, int day) throws Exception;
    
     /**
     * Registra una concurrencia para citas
     *
     * @param appointment informacion de la concurrencia para citas
     * @return 
     * @throws Exception Error en la base de datos
     */
    public Integer createConcurrence(Appointment appointment) throws Exception;
    
    /**
     * Elimina todas las concurrencias para citas.
     *
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public int deleteConcurrence() throws Exception;

    /**
     * Elimina concurrencias en citas.
     *
     * @param date Fecha de la cita yyyyMMdd.
     * @param idShift Id jornada.
     * @param idPhlebotomist Id del rutero.
     * @param idUser Id del usuario que registro.
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public int deleteConcurrence(Integer date, Integer idShift, Integer idPhlebotomist, Integer idUser) throws Exception;
    
    /**
     * Elimina concurrencias en citas.
     *
     * @param id Fecha de la cita yyyyMMdd.
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public int deleteConcurrence(Integer id) throws Exception;

    /**
     * Elimina la concurrencia de una cita.
     *
     * @param date Fecha de la cita yyyyMMdd.
     * @param idShift Id jornada.
     * @param idPhlebotomist Id del rutero.
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public int deleteConcurrence(Integer date, Integer idShift, Integer idPhlebotomist) throws Exception;
    
    /**
     *consulta la cocurrenc para las citas
     *
     * @param date Fecha de la cita yyyyMMdd.
     * @param branchId
     * @param initappointment
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public int getSecuenceOrder(int date, int branchId, long initappointment) throws Exception;
    
    
    /**
     *crea una cita relaconada a una orden
     *
     * @param order
     * @return Cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    public Appointment createAppointment(Order order) throws Exception;
    
    /**
     * Realiza la reprogramación de una cita.
     *
     * @param order
     * @return Cita actualizada.
     * @throws Exception Error en la base de datos.
    */
    public Order reprogramAppointment(Order order) throws Exception;
    
        /**
     * Realiza la reprogramación de una cita.
     *
     * @param order
     * @return Cita actualizada.
     * @throws Exception Error en la base de datos.
    */
    public Order cancelAppointment(Order order) throws Exception;
    
    /**
     * Busca las citas por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error en el servicio
     */
    public List<OrderSearch> getByEntryDate(int date, int branch) throws Exception;
    
        /**
     * Busca por numero de orden
     *
     * @param order Numero de orden
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}, null
     * en caso de no encontrar
     * @throws Exception Error en el servicio
     */
    public OrderSearch getByOrder(long order, int branch) throws Exception;
    
     /**
     * Busca ordenes de acuerdo a los criterios enviados
     *
     * @param documentType Id tipo documento (si se envia null no se busca por
     * este filtro)
     * @param patientId Historia (si se envia null no se busca por este filtro)
     * @param lastName Apellido (si se envia null no se busca por este filtro)
     * @param surName Segundo Apellido (si se envia null no se busca por este
     * filtro)
     * @param name1 Nombre 1 (si se envia null no se busca por este filtro)
     * @param name2 Nombre 2 (si se envia null no se busca por este filtro)
     * @param sex Sexo (si se envia null no se busca por este filtro)
     * @param birthday Fecha de Nacimiento (si se envia null no se busca por
     * este filtro)
     * @param branch Id Sede de consulta, -1 si no se manejan sedes
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacio si no se encuentra ninguna orden
     * @throws Exception Errores presentados en el servicio
     */
    public List<OrderSearch> getByPatientInfo(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception;

        /**
     * Realiza la reprogramación de una cita.
     *
     * @param order
     * @return Cita actualizada.
     * @throws Exception Error en la base de datos.
    */
    public Order changeappointment(Order order) throws Exception;
    
     /**
     * Realiza el envio de correo
     *
     * @param email informacion del correo
     * @return informaciónde del correo
     * @throws Exception Error en el servicio
     */
    public String sendEmail(Email email) throws Exception;
     
}
