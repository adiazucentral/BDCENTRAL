/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.appointment;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import net.cltech.enterprisent.dao.interfaces.operation.appointment.AppointmentDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.masters.user.Image;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;
import net.cltech.enterprisent.domain.operation.appointment.Availability;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.service.interfaces.masters.appointment.ShiftService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.appointment.AppointmentService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class AppointmentServiceEntepriseNT implements AppointmentService {
    
    @Autowired
    private AppointmentDao dao;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StatisticService statisticService;
    @Autowired(required = true)
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationServices;
    
    private BodyType bodyType;
    
    @Override
    public List<Availability> listAvailabilityRange(int idBranch, int date, int quantity) throws Exception
    {
        Calendar endDate = Calendar.getInstance();
//        endDate.setTime(DateTools.numberToDate(date));
        endDate.add(Calendar.DAY_OF_YEAR, quantity - 1);

        List<Availability> availabilitys = new ArrayList<>();
        
        List<Shift> shifts = shiftService.listShiftsbyBranch(idBranch);
        
        //List<User> users = userService.getSimpleUsersByZone(idZone, date);
        //List<Absence> absences = listAbsences(date, DateTools.dateToNumber(endDate.getTime()));
        List<Appointment> appointments = dao.listAppointmentByDateBranch(date, DateTools.dateToNumber(endDate.getTime()), idBranch);

        for (int i = 0; i < quantity; i++)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateTools.numberToDate(date));
            calendar.add(Calendar.DAY_OF_YEAR, i);

            availabilitys.add(getAvailabilityByDate(DateTools.dateToNumber(calendar.getTime()), shifts, appointments));
        }
        return availabilitys;
    }
    
     /**
     * Obtiene la cantidad de citas que podran ser atendidas este dia y cuantas se han asignado.
     *
     * @param date Fecha
     * @param users Lista de usuarios con jornadas
     * @param absences Lista de ausencias programadas en un rango de fechas
     * @param appointments Lista de citas programas en un rango de fechas
     *
     * @return Disponibilidad por dia.
     */
    private Availability getAvailabilityByDate(int date, List<Shift> shift, List<Appointment> appointments)
    {
        LocalDate localDate = DateTools.localDate(DateTools.numberToDate(date));
        int quantity =  shift.stream()
                .filter(s -> s.getDays().contains(localDate.getDayOfWeek().getValue()))
                .map(s -> s.getQuantity())
                .reduce(0, (x, y) -> x + y);

        Availability availability = new Availability();
        availability.setDate(date);
        availability.setQuantity(quantity);
        int dailyAppointments = appointments.stream()
                .filter(appointment -> appointment.getDate() == date)
                //.filter(appointment -> appointment.getState() != HomeboundEnum.AppointmentState.CANCELED.getValue())
                .collect(Collectors.toList()).size();
        availability.setAmount(dailyAppointments);

        return availability;
    }
    
    @Override
    public Availability getAvailability(int branch, int date, int day) throws Exception
    {
        Availability availability = new Availability();
        availability.setShifts(shiftService.listShift(branch, date, day)); 
        availability.setDate(date);
        return availability;
    }
    
    @Override
    public Integer createConcurrence(Appointment appointment) throws Exception
    {
       
        List<Shift> listShift =  shiftService.listShift(appointment.getBranch().getId(), appointment.getDate(), 0);
        if (listShift != null && !listShift.isEmpty())
        {
            Shift shift = listShift.stream().filter(s -> Objects.equals(s.getId(), appointment.getShift().getId())).findFirst().orElse(null);
            if (shift != null)
            {
                int amount = dao.countConcurrence(appointment.getBranch().getId(), appointment.getShift().getId(), appointment.getDate());
                if (amount < shift.getQuantity())
                {
                    return dao.createConcurrence(appointment, JWT.decode(request).getId());
                }
                else
                {
                    throw new EnterpriseNTException(Arrays.asList("1|concurrences not available"));
                }
            }
            else
            {
                throw new EnterpriseNTException(Arrays.asList("0|shift"));
            }
        }
        else
        {
            throw new EnterpriseNTException(Arrays.asList("0|shift"));
        }
    }
    
     @Override
    public int deleteConcurrence() throws Exception
    {
        return dao.deleteConcurrence(true, null, null, null, null);
    }

    @Override
    public int deleteConcurrence(Integer date, Integer idShift, Integer branch, Integer idUser) throws Exception
    {
        return dao.deleteConcurrence(false, date, idShift, branch, idUser);
    }

    @Override
    public int deleteConcurrence(Integer date, Integer idShift, Integer branch) throws Exception
    {
        return dao.deleteConcurrence(branch, idShift, date);
    }
    
    @Override
    public int deleteConcurrence(Integer id) throws Exception
    {
        return dao.deleteConcurrenceByid(id);
    }
    
    @Override
    public int getSecuenceOrder(int date, int branchId, long initappointment) throws Exception
    {
        return dao.getSecuenceOrder( date, branchId,initappointment );
    }
    
    @Override
    public Appointment createAppointment(Order order) throws Exception
    {
        try {
            AuthorizedUser user = JWT.decode(request);
              
            order.getAppointment().getBranch().setId(user.getBranch());
            order.getAppointment().setDate(order.getCreatedDateShort());
            order.getAppointment().setOrderNumber(order.getOrderNumber());
            dao.createAppointment(order.getAppointment(), user.getId());
            dao.deleteConcurrenceByid(order.getAppointment().getIdConcurrence());// deleteConcurrence(appointment.getPhlebotomist().getId(), appointment.getShift().getId(), appointment.getDate(), user.getId());
            //insertAudit(appointment, user);

            return order.getAppointment();
        } catch (Exception e) {
            throw new EnterpriseNTException(Arrays.asList("0|eror appointment"));
        }
    }
    
    @Override
    public Order reprogramAppointment(Order order) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
   
            try
            {    
                deleteConcurrence(order.getAppointment().getDate(), order.getAppointment().getShift().getId(), order.getBranch().getId(), user.getId());
                if (order.getCreatedDateShort().equals(order.getAppointment().getDate()))
                {
                    order.getAppointment().setState(LISEnum.AppointmentState.REPROGRAM.getValue());
                    order.getAppointment().setOrderNumber(order.getOrderNumber());
                    order.getAppointment().setDate(order.getCreatedDateShort());
                    
                    dao.updateAppointment(order.getAppointment(), user.getId());

                }
                else
                {
                    
                    List<Order> orders = new LinkedList<>();
                    orders.add(order);
                    orders = orderDao.updateOrderState(orders, LISEnum.ResultOrderState.CANCELED);
                    final List<Long> orderList = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
                    statisticService.disableOrders(orderList, LISEnum.ResultOrderState.CANCELED);
                    List<AuditOperation> audit = new ArrayList<>();
                    audit.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", null, null, null, null));  
                    trackingService.registerOperationTracking(audit); 

                   
                    
                    order.setOrderNumber(null);
                    order.setHasAppointment(1);
                    Order newOrder = orderService.create(order, JWT.decode(request).getId(), JWT.decode(request).getBranch());
                    
                    order.getAppointment().setState(LISEnum.AppointmentState.REPROGRAM.getValue());
                    order.getAppointment().setOrderNumber(newOrder.getOrderNumber());
                    order.getAppointment().setDate(newOrder.getCreatedDateShort());
                    dao.updateAppointment(order.getAppointment(), user.getId());

                }                    
            }
            catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                throw new EnterpriseNTException(Arrays.asList("URL not found"));
            }
     
        
        return order;
    }
    
     @Override
    public Order cancelAppointment(Order order) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
   
            try
            {    
                
                List<Order> orders = new LinkedList<>();
                orders.add(order);
                orders = orderDao.updateOrderState(orders, LISEnum.ResultOrderState.CANCELED);
                final List<Long> orderList = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
                statisticService.disableOrders(orderList, LISEnum.ResultOrderState.CANCELED);
                List<AuditOperation> audit = new ArrayList<>();
                audit.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", null, null, null, null));  
                trackingService.registerOperationTracking(audit); 
                
                deleteConcurrence(order.getCreatedDateShort(), order.getAppointment().getShift().getId(), order.getBranch().getId());
                    
                order.getAppointment().setState(LISEnum.AppointmentState.CANCELED.getValue());
                order.getAppointment().setOrderNumber(order.getOrderNumber());
                order.getAppointment().setDate(order.getCreatedDateShort());
                dao.updateAppointment(order.getAppointment(), user.getId());

            }
            catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                throw new EnterpriseNTException(Arrays.asList("URL not found"));
            }
     
        
        return order;
    }
    
    @Override
    public List<OrderSearch> getByEntryDate(int date, int branch) throws Exception
    {
        return dao.getByEntryDate(date, branch);
    }
    
    @Override
    public OrderSearch getByOrder(long order, int branch) throws Exception
    {
        return dao.getByOrder(order, branch);
    }
    
     @Override
    public List<OrderSearch> getByPatientInfo(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception
    {
        if (patientId != null && !patientId.toLowerCase().equals("undefined"))
        {
            patientId = Tools.encrypt(patientId);
        } else
        {
            patientId = null;
        }

        if (lastName != null && !lastName.toLowerCase().equals("undefined"))
        {
            lastName = Tools.encrypt(lastName);
        } else
        {
            lastName = null;
        }

        if (surName != null && !surName.toLowerCase().equals("undefined"))
        {
            surName = Tools.encrypt(surName);
        } else
        {
            surName = null;
        }

        if (name1 != null && !name1.toLowerCase().equals("undefined"))
        {
            name1 = Tools.encrypt(name1);
        } else
        {
            name1 = null;
        }

        if (name2 != null && !name2.toLowerCase().equals("undefined"))
        {
            name2 = Tools.encrypt(name2);
        } else
        {
            name2 = null;
        }
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        return dao.getByPatientInfo(documentType, patientId, lastName, surName, name1, name2, sex, birthday, branch, yearsQuery);
    }
    
    @Override
    public Order changeappointment(Order order) throws Exception
    {
        try
        {    
            dao.updateOrderState(order);
        }
        catch (IllegalArgumentException | HttpClientErrorException ex)
        {
           throw new EnterpriseNTException(Arrays.asList("Error al cambiar estado de la cita"));
        }
     
        
        return order;
    }
    
    @Override
    public String sendEmail(Email email) throws Exception {
        try {
          
            if (!"OUTLOOK".equals(configurationServices.getValue("ServidorCorreoAppointment"))) {
                               
                //Parametros para enviar el correo
                Properties props = System.getProperties();
                props.put("mail.smtp.host",  configurationServices.getValue("SmtpHostNameAppointment"));
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", Integer.parseInt(configurationServices.getValue("SmtpPortAppointment")));
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", Integer.parseInt(configurationServices.getValue("SmtpPortAppointment")));
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.transport.protocol", "smtp");
                Session session = Session.getDefaultInstance(props, null);
                session.setDebug(false);
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(configurationServices.getValue("SmtpAuthUserAppointment")));
                //Se agregan los destinatarios del correo

                for (String string : email.getRecipients()) {
                    if (!string.isEmpty()) {
                        string = string.replace("[", "").replace("]", "").replace("\"", "");
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(string));
                    }
                }
                //Se agrega el contenido del correo
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(email.getBody(), "text/html");
                // se crea multi-part y se le agrega el cuerpo del correo
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                // se crea MimeBodyPart para asignarle la imagen a enviar
                MimeBodyPart imagePart = new MimeBodyPart();
                byte[] decodedBytes = null;
                if (email.getAttachment() != null && email.getAttachment().size() > 0) {
                    for (Image image : email.getAttachment()) {
                        String imageFilePath = image.getPath();
                        decodedBytes = Base64.getDecoder().decode(imageFilePath);
                        DataSource ds = new ByteArrayDataSource(decodedBytes, "application/pdf");
                        imagePart.setDataHandler(new DataHandler(ds));
                        // se asgina el nombre a la imagen
                        imagePart.setFileName(image.getFilename());
                        multipart.addBodyPart(imagePart);
                    }
                }
                // se agrega las multipart al mensaje y se agrega el asunto
                message.setContent(multipart);
                message.setSubject(email.getSubject());
                message.saveChanges();
                try {
                    Transport transport = session.getTransport(configurationServices.getValue("SmtpProtocolAppointment"));
                    transport.connect(configurationServices.getValue("SmtpHostNameAppointment"), Integer.parseInt(configurationServices.getValue("SmtpPortAppointment")), configurationServices.getValue("SmtpAuthUserAppointment"), configurationServices.getValue("SmtpPasswordUserAppointment"));
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception e) {
                    System.out.println(e);
                    OrderCreationLog.error(e.getMessage());
                    return "Se a generado un error a la hora de hacer el envio";
                }
            } else {
                ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
                ExchangeCredentials credentials = new WebCredentials(configurationServices.getValue("SmtpAuthUserAppointment"), configurationServices.getValue("SmtpPasswordUserAppointment"));
                service.setCredentials(credentials);

                service.setUrl(new URI(configurationServices.getValue("SmtpHostNameAppointment")));

                
                EmailMessage msg = new EmailMessage(service);
                msg.setSubject(email.getSubject());

                MessageBody msgBody = new MessageBody();
                msgBody.setBodyType(bodyType.valueOf("HTML"));
                msgBody.setText(email.getBody());
                msg.setBody(msgBody);

                byte[] decodedBytes = null;
                if (email.getAttachment() != null && email.getAttachment().size() > 0) {
                    
                    for (Image image : email.getAttachment()) {
                        if("1".equals(image.getType())){
                            decodedBytes = Base64.getDecoder().decode(image.getPath());
                            msg.getAttachments().addFileAttachment(image.getFilename(), decodedBytes);
                        }
                        else {
                            decodedBytes = Base64.getDecoder().decode(image.getPath());
                            String imageFilePath = String.valueOf(decodedBytes);
                            if (image != null) {
                                msg.getAttachments().addFileAttachment(imageFilePath + image.getFilename());
                            }
                        }
                    }
                }

                for (String string : email.getRecipients()) {
                    if (!string.isEmpty()) {
                        string = string.replace("[", "").replace("]", "").replace("\"", "");
                        msg.getToRecipients().add(string); //email receiver
                    }
                }
                msg.send(); //send email
            }
            return "Se envio el correo";
        } catch (Exception ex) {
            System.out.println(ex);
            OrderCreationLog.error(ex.getMessage());
            return "Se a generado un error a la hora de hacer el envio";
        }
    }
}
