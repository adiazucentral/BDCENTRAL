package net.cltech.enterprisent.service.impl.enterprisent.masters.user;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.DatatypeConverter;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.domain.common.UserHomeBound;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.user.BranchByUser;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.masters.user.Image;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserAnalyzer;
import net.cltech.enterprisent.domain.masters.user.UserByBranchByAreas;
import net.cltech.enterprisent.domain.masters.user.UserIntegration;
import net.cltech.enterprisent.domain.masters.user.UserPassword;
import net.cltech.enterprisent.domain.masters.user.UserRecoveryPassword;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.ldap.IntegrationServiceLdap;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.jobs.SchedulerLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementa los metodos de acceso a usuarios del sistema
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 25/04/2017
 * @see Creación
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
@Service
public class UserServiceEnterpriseNT implements UserService {

    @Autowired
    private UserDao dao;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ConfigurationService configurationService;
    
//    @Autowired
//    private UserService userService;
    @Autowired
    private IntegrationServiceLdap serviceLdap;

    private BodyType bodyType;

    @Override
    public JWTToken authenticate(AuthenticationUser user) throws Exception {

        final String loginActiveDirectory = configurationService.getValue("LoginActiveDirectory");
        boolean activeDirectory = Boolean.parseBoolean(loginActiveDirectory);
        if (activeDirectory) {

            boolean loginActiveDirectoryOk = serviceLdap.authenticateLDAP(user);

            if (loginActiveDirectoryOk) {
                final String url = configurationService.getValue("UrlSecurity") + "/api/authentication";
                return integrationService.post(Tools.jsonObject(user), JWTToken.class, url);
            }
            
        }

        final String url = configurationService.getValue("UrlSecurity") + "/api/authentication";

        return integrationService.post(Tools.jsonObject(user), JWTToken.class, url);
    }

    @Override
    public List<User> list() throws Exception {
        return dao.list();
    }

    @Override
    public List<User> listDeactivate() throws Exception {
        return dao.listDeactivate();
    }
    
    @Override
    public List<User> SimpleUserList() throws Exception {
        return dao.listUser();
    }

    @Override
    public User create(User user) throws Exception {
        user.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(false, user);
        if (errors.isEmpty()) {
            User created = dao.create(user, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
            trackingService.registerConfigurationTracking(null, created, User.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public User get(Integer id, String username, String identification, String signatureCode) throws Exception {
        return dao.get(id, username, identification, signatureCode);
    }
    
    @Override
    public User getUpdateTestEntry(String username, String password) throws Exception {
        return dao.getPermitUpdate(username, password);
    }

    @Override
    public User getBasicUser(Integer id) throws Exception {
        return dao.getBasicUser(id);
    }

    @Override
    public User update(User user) throws Exception {
        user.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(true, user);
        if (errors.isEmpty()) {
            User userC = dao.getPasswordHistory(user.getId());
            if (user.getPassword() != null) {
                user.setAntepenultimatePassword(userC.getPenultimatePassword());
                user.setPenultimatePassword(userC.getPassword());
                SchedulerLog.info("contraseña usuario " + user.getPassword());
                user.setPassword(Tools.encrypt(user.getPassword()));
                userC.setAntepenultimatePassword(null);
                userC.setPenultimatePassword(null);
                userC.setPassword(null);
            }
            User modifited = dao.update(user, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
            trackingService.registerConfigurationTracking(userC, modifited, User.class);
            return modifited;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public User changeStateUser(User user) throws Exception {
        List<String> errors = new ArrayList<>();
        if (user.getId() == null) {
            errors.add("0|id");
        }
        if (errors.isEmpty()) {
            boolean state = user.isState();
            User userC = dao.get(user.getId(), null, null, null);
            user = userC;
            user.setUser(trackingService.getRequestUser());
            user.setState(state);
            dao.changeStateUser(user);
            trackingService.registerConfigurationTracking(userC, user, User.class);
            return user;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        dao.delete(id);
    }

    @Override
    public List<User> list(boolean state) throws Exception {
        List<User> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((User) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, User user) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit) {
            if (user.getId() == null) {
                errors.add("0|id");
                return errors;
            } else {
                if (user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getUserName().equals("system")) {
                    errors.add("6|user");
                    return errors;
                } else {
                    if (dao.get(user.getId(), null, null, null) == null) {
                        errors.add("2|id");
                        return errors;
                    }
                }
            }

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                
                User userC = dao.getPasswordHistory(user.getId());
                SchedulerLog.error("contraseña " + user.getPassword());
                //String encrypt = Tools.encryptUser(user.getPassword());
                //if (encrypt.equals(userC.getPassword()) || encrypt.equals(userC.getPenultimatePassword()) || encrypt.equals(userC.getAntepenultimatePassword())) {
                    //errors.add("1|New password is in the history");
                //}
            }
        }

        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            User userC = dao.get(null, user.getUserName(), null, null);
            if (userC != null) {
                if (isEdit) {
                    if (!Objects.equals(user.getId(), userC.getId())) {
                        errors.add("1|username");
                    }
                } else {
                    errors.add("1|username");
                }
            }
        } else {
            errors.add("0|username");
        }

        if ((user.getPassword() == null || user.getPassword().isEmpty()) && !isEdit) {
            errors.add("0|password");
        }

        if ((user.getName() == null || user.getName().isEmpty())) {
            errors.add("0|name");
        }

        if ((user.getLastName() == null || user.getLastName().isEmpty())) {
            errors.add("0|last name");
        }

        if (user.getIdentification() != null && !user.getIdentification().isEmpty()) {
            User userC = dao.get(null, null, user.getIdentification(), null);
            if (userC != null) {
                if (isEdit) {
                    if (!Objects.equals(user.getId(), userC.getId())) {
                        errors.add("1|identification");
                    }
                } else {
                    errors.add("1|identification");
                }
            }
        } else {
            errors.add("0|identification");
        }

        if (user.getSignatureCode() != null && !user.getSignatureCode().isEmpty()) {
            User userC = dao.get(null, null, null, user.getSignatureCode());
            if (userC != null) {
                if (isEdit) {
                    if (!Objects.equals(user.getId(), userC.getId())) {
                        errors.add("1|signature code");
                    }
                } else {
                    errors.add("1|signature code");
                }
            }
        }

        if (user.getActivation() == null) {
            errors.add("0|activation date");
        }

        if (user.getActivation() != null && user.getExpiration() != null) {
            if (DateTools.getDateWithoutTime(user.getActivation()).equals(DateTools.getDateWithoutTime(user.getExpiration()))) {
                errors.add("3|dates");
            } else if (DateTools.getDateWithoutTime(user.getActivation()).after(DateTools.getDateWithoutTime(user.getExpiration()))) {
                errors.add("3|dates");
            }
        }

        if (user.getMaxDiscount() != null) {
            if (user.getMaxDiscount() < 0 || user.getMaxDiscount() > 100) {
                errors.add("3|discount");
            }
        } else {
            errors.add("0|discount");
        }

        if (user.getType().getId() != null && user.getType().getId() != 0) {
            if (user.getType().getId() != 11 && user.getType().getId() != 12 && user.getType().getId() != 13) {
                errors.add("3|type");
            }
        } else {
            errors.add("0|type");
        }

        if (user.getRoles().isEmpty()) {
            errors.add("0|roles");
        }

        if (user.getUser().getId() == null || user.getUser().getId() == 0) {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public int insertTest(List<ExcludeTest> excludeList) throws Exception {
        if (!excludeList.isEmpty()) {
            dao.deleteTest(excludeList.get(0).getUser().getId());
            trackingService.registerConfigurationTracking(null, excludeList, List.class);
            return dao.insertTest(excludeList);
        }
        return 0;
    }

    @Override
    public int deleteTest(Integer id) {
        return dao.deleteTest(id);
    }

    @Override
    public List<ExcludeTest> listTest(Integer id) {
        return dao.listTest(id);
    }

    @Override
    public boolean updateProfile(User user) throws Exception {

        final String url = configurationService.getValue("UrlSecurity") + "/api/authentication/updateprofile";
        return integrationService.put(Tools.jsonObject(user), Boolean.class, url);
    }

    @Override
    public void updatePassword(UserPassword userPassword) throws Exception {
        final String url = configurationService.getValue("UrlSecurity") + "/api/authentication/updatepassword";
        integrationService.putVoid(Tools.jsonObject(userPassword), url);
    }

    @Override
    public void recoverPassword(UserRecoveryPassword userPassword) throws Exception {
        String username = userPassword.getUserName();
        String email = userPassword.getEmail();
        String password = Tools.generatePassword();
        int records;
        records = dao.updatePasswordRecovery(password, username, email);
        if (records != 0) {
            Email dataSendEmail = new Email();
            List<String> userEmail = new ArrayList<>();
            userEmail.add(email);
            dataSendEmail.setRecipients(userEmail);
            dataSendEmail.setSubject(configurationService.getValue("NotificacionAsuntoRecuperacion"));
            dataSendEmail.setBody((configurationService.getValue("NotificacionPlantillaRecuperacion")).replace("||USER||", username).replace("||PASSWORD||", password));
            sendEmail(dataSendEmail);
        } else {
            List<String> errorList = new ArrayList<>();
            errorList.add("1|email not found");
            throw new EnterpriseNTException(errorList);
        }
    }

    @Override
    public String sendEmail(Email email) throws Exception {
        try {
          
            if (!"OUTLOOK".equals(configurationService.getValue("ServidorCorreo"))) {
                               
                //Parametros para enviar el correo
                Properties props = System.getProperties();
                props.put("mail.smtp.host",  configurationService.getValue("SmtpHostName"));
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", Integer.parseInt(configurationService.getValue("SmtpPort")));
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", Integer.parseInt(configurationService.getValue("SmtpPort")));
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.transport.protocol", "smtp");
                Session session = Session.getDefaultInstance(props, null);
                session.setDebug(false);
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(configurationService.getValue("SmtpAuthUser")));
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
                    Transport transport = session.getTransport(configurationService.getValue("SmtpProtocol"));
                    transport.connect(configurationService.getValue("SmtpHostName"), Integer.parseInt(configurationService.getValue("SmtpPort")), configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception e) {
                    System.out.println(e);
                    OrderCreationLog.error(e.getMessage());
                    return "Se a generado un error a la hora de hacer el envio";
                }
            } else {
                ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
                ExchangeCredentials credentials = new WebCredentials(configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                service.setCredentials(credentials);

                service.setUrl(new URI(configurationService.getValue("SmtpHostName")));

                
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

    static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {

        @Override
        public boolean autodiscoverRedirectionUrlValidationCallback(
                String redirectionUrl) {
            return redirectionUrl.toLowerCase().startsWith("https://");
        }
    }

    @Override
    public List<User> getByBranchAreas(UserByBranchByAreas filter) throws Exception {
        return dao.getByBranchAreas(filter);
    }

    @Override
    public User createByType(User user) throws Exception {
        user.setUser(trackingService.getRequestUser());
        List<String> errors = validateFieldsByType(false, user);
        if (errors.isEmpty()) {
            User created = dao.createByType(user, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
            trackingService.registerConfigurationTracking(null, created, User.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public UserIntegration userIntegration(String code) throws Exception {
        
        UserIntegration user = dao.getUserByCode(code);
        if(user != null){
            return user;
        }
        else {
            User usercreate = new User();
            usercreate.setName(code);
            usercreate.setPassword(code);
            usercreate.setUserName(code);
            usercreate.setIdentification(code);
            usercreate.getUser().setId(1);      
            usercreate.getType().setId(13);
            Calendar calendar = Calendar.getInstance();
            usercreate.setActivation(new Timestamp(calendar.getTime().getTime()));
            
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(usercreate.getActivation());
            calendar1.add(Calendar.DAY_OF_YEAR, 3000);
            
            usercreate.setExpiration(new Timestamp(calendar1.getTime().getTime()));
            usercreate.setPasswordExpiration(new Timestamp(calendar1.getTime().getTime()));

            User created = dao.createByType(usercreate, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
            
            UserIntegration user1 = new UserIntegration();
            user1.setId(created.getId());
            user1.setPassword(created.getPassword());
            
            List<BranchByUser> listuserbranch = new LinkedList<>();
            BranchByUser branch = new BranchByUser();
            branch.setAccess(true);
            branch.getUser().setId(created.getId());
            branch.getBranch().setId(1);
            listuserbranch.add(branch);
            usercreate.setBranches(listuserbranch);
            
            dao.insertBranchesIntegration(usercreate);
            return user1;
        }
    }

    @Override
    public User updateByType(User user) throws Exception {
        user.setUser(trackingService.getRequestUser());
        List<String> errors = validateFieldsByType(true, user);
        if (errors.isEmpty()) {
            User userC = dao.get(user.getId(), null, null, null);
            User modifited = dao.updateByType(user, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
            trackingService.registerConfigurationTracking(userC, modifited, User.class);
            return modifited;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateFieldsByType(boolean isEdit, User user) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit) {
            if (user.getId() == null) {
                errors.add("0|id");
                return errors;
            } else {
                if (dao.get(user.getId(), null, null, null) == null) {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            User userC = dao.get(null, user.getUserName(), null, null);
            if (userC != null) {
                if (isEdit) {
                    if (!Objects.equals(user.getId(), userC.getId())) {
                        errors.add("1|username");
                    }
                } else {
                    errors.add("1|username");
                }
            }
        } else {
            errors.add("0|username");
        }

        if (user.getType().getId() == ListEnum.UserType.INTEGRATION.getValue()) {
            if ((user.getPassword() == null || user.getPassword().isEmpty()) && !isEdit) {
                errors.add("0|password");
            }
        }

        if ((user.getName() == null || user.getName().isEmpty())) {
            errors.add("0|name");
        }

        if (user.getIdentification() != null && !user.getIdentification().isEmpty()) {
            User userC = dao.get(null, null, user.getIdentification(), null);
            if (userC != null) {
                if (isEdit) {
                    if (!Objects.equals(user.getId(), userC.getId())) {
                        errors.add("1|identification");
                    }
                } else {
                    errors.add("1|identification");
                }
            }
        } else {
            errors.add("0|identification");
        }

        if (user.getActivation() == null) {
            errors.add("0|activation date");
        }

        if (user.getActivation() != null && user.getExpiration() != null) {
            if (DateTools.getDateWithoutTime(user.getActivation()).equals(DateTools.getDateWithoutTime(user.getExpiration()))) {
                errors.add("3|dates");
            } else if (DateTools.getDateWithoutTime(user.getActivation()).after(DateTools.getDateWithoutTime(user.getExpiration()))) {
                errors.add("3|dates");
            }
        }

        if (user.getType().getId() != null && user.getType().getId() != 0) {
            if (user.getType().getId() != 12 && user.getType().getId() != 13) {
                errors.add("3|type");
            }
        } else {
            errors.add("0|type");
        }

        if (user.getUser().getId() == null || user.getUser().getId() == 0) {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public JWTToken authenticateLaboratory(UserHomeBound user) throws Exception {
        user.setType(4);
        final String url = configurationService.getValue("UrlSecurity") + "/api/authentication/webquery";
        return integrationService.post(Tools.jsonObject(user), JWTToken.class, url);

    }

    /**
     * Obtiene una lista con los usuarios de analizadores con destino en
     * microbiologia
     *
     * @return Lista de usuarios
     * @throws java.lang.Exception
     */
    @Override
    public List<UserAnalyzer> getUsersAnalyzers() throws Exception {
        try {
            List<UserAnalyzer> listUsers = dao.getUsersAnalyzers();
            return listUsers;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public JWTToken integrationAuthentication(AuthenticationUser user) throws Exception {
        String url = configurationService.getValue("UrlSecurity") + "/api/authentication/integrationAuthentication";
        return integrationService.post(Tools.jsonObject(user), JWTToken.class, url);
    }
}
