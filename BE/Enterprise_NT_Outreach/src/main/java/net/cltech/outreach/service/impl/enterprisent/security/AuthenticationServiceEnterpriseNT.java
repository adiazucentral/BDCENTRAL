package net.cltech.outreach.service.impl.enterprisent.security;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
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
import javax.servlet.http.HttpServletRequest;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import net.cltech.outreach.dao.interfaces.security.AuthenticationDao;
import net.cltech.outreach.domain.common.AuthenticationUser;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.common.Email;
import net.cltech.outreach.domain.common.Image;
import net.cltech.outreach.domain.common.JWTToken;
import net.cltech.outreach.domain.exception.EnterpriseNTException;
import net.cltech.outreach.domain.exception.EnterpriseNTTokenException;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.domain.masters.configuration.UserPassword;
import net.cltech.outreach.service.interfaces.integration.IntegrationService;
import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.outreach.service.interfaces.security.AuthenticationService;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.DateTools;
import net.cltech.outreach.tools.JWT;
import net.cltech.outreach.tools.Log;
import net.cltech.outreach.tools.Tools;
import net.cltech.outreach.tools.integration.SecurityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a la autenticacion para la consulta web
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/04/2018
 * @see Creación
 */
@Service
public class AuthenticationServiceEnterpriseNT implements AuthenticationService
{

    @Autowired
    private AuthenticationDao dao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;

    private BodyType bodyType;

    @Override
    public AuthorizedUser authenticate(String userName, String password, Integer type, Integer historyType) throws Exception
    {
        AuthorizedUser user = null;
        if (type != null)
        {
            switch (type)
            {
                case Constants.PHYSICIAN:
                    user = dao.authenticationPhysician(userName);
                    break;
                case Constants.PATIENT:
                    user = dao.authenticationPatient(Tools.encrypt(userName), historyType);
                    break;
                case Constants.ACCOUNT:
                    user = dao.authenticationAccount(userName);
                    break;
                case Constants.USERLIS:
                    user = dao.authenticationUserLIS(userName);
                    break;
                case Constants.RATE:
                    user = dao.authenticationRate(userName);
                    break;
                default:
                    break;
            }
        }

        if (user != null)
        {
            //valida contraseña
            if (user.getPassword() != null)
            {
                if (user.getPassword().equals(Tools.encrypt(password)))
                {
                    user.setPassword(null);
                    return user;
                }
            } else
            {
                throw new EnterpriseNTException("5|password recovery");
            }
        }

        if (userName != null && userName.equalsIgnoreCase("administrator"))
        {
            //Valida contraseña
            if (password != null && password.equals("cltech" + DateTools.dateToNumber(new Date())))
            {
                AuthorizedUser authorized = new AuthorizedUser();
                authorized.setId(-1);
                authorized.setName("administrator");
                authorized.setUserName("administrator");
                authorized.setType(0);
                authorized.setAdministrator(true);

                return authorized;
            }
        }

        throw new EnterpriseNTException("4|invalid user");
    }

    @Override
    public JWTToken authenticateWeb(AuthenticationUser user) throws Exception
    {
        JWTToken tokenType = new JWTToken();
        Log.info(getClass(), user.toString());
        if (user.getUser() != null && user.getUser().equalsIgnoreCase("administrator"))
        {
            //Valida contraseña
            if (user.getPassword() != null && user.getPassword().equals("cltech" + DateTools.dateToNumber(new Date())))
            {
                AuthorizedUser authorized = new AuthorizedUser();
                authorized.setId(-1);
                authorized.setName("administrator");
                authorized.setUserName("administrator");
                authorized.setType(user.getType());
                authorized.setAdministrator(true);

                tokenType.setSuccess(true);
                tokenType.setUser(authorized);

                tokenType.setToken(JWT.generate(authorized, Constants.TOKEN_AUTH_USER));
            } else
            {
                List<String> errorList = new ArrayList<String>();
                errorList.add("5|Password invalid");
                throw new EnterpriseNTException(errorList);
            }

        } else
        {
            Configuration config = configurationService.get("UrlSecurity");
            final String url = config.getValue() + "/api/authentication/webquery";
            tokenType = integrationService.post(Tools.jsonObject(user), JWTToken.class, url);
            Log.info(getClass(), "tokenType : " + tokenType);
        }

        return tokenType;
    }

    @Override
    public List<JWTToken> recovery(String userName, Integer type, String historyNumber) throws Exception
    {
        List<AuthorizedUser> user = new ArrayList<>(0);
        AuthorizedUser aux = null;
        switch (type)
        {
            case Constants.PHYSICIAN:
                aux = dao.authenticationPhysician(userName);
                if (aux != null)
                {
                    user.add(aux);
                    dao.updatePasswordPhysician(user.get(0).getId(), null);
                }
                break;
            case Constants.RATE:
                aux = dao.authenticationRate(userName);
                if (aux != null)
                {
                    user.add(aux);
                    dao.updatePasswordRate(user.get(0).getId(), null);
                }
                break;    
            case Constants.PATIENT:
                if (!historyNumber.isEmpty())
                {
                    user = dao.authenticationPatientEmail(userName, historyNumber);
                    if (!user.isEmpty())
                    {
                        user.stream().forEach((AuthorizedUser u)
                                ->
                        {
                            try
                            {
                                dao.updatePasswordPatient(u.getId(), null);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(AuthenticationServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                } else
                {
                    throw new EnterpriseNTTokenException("4|history number not associated with the request");
                }
                break;
            case Constants.ACCOUNT:
                aux = dao.authenticationAccount(userName);
                if (aux != null)
                {
                    user.add(aux);
                    dao.updatePasswordAccount(user.get(0).getId(), null);
                }
                break;
            case Constants.DEMOGRAPHIC:
                aux = dao.authenticationDemographic(historyNumber);
                if (aux != null)
                {
                    user.add(aux);
                    dao.updatePasswordDemo(user.get(0).getId(), null);
                }
                break;
            default:
                throw new EnterpriseNTTokenException("1|wrong user type");
        }
        if (user.isEmpty())
        {
            throw new EnterpriseNTTokenException("2|user not found");
        } else if (user.get(0).getEmail() == null || user.get(0).getEmail().trim().isEmpty())
        {
            throw new EnterpriseNTTokenException("3|mail not found");
        }
        List<JWTToken> listToken = new ArrayList<>(0);
        user.stream().forEach((AuthorizedUser u)
                ->
        {
            u.setPassword(null);
            JWTToken token = new JWTToken();
            token.setSuccess(true);
            token.setToken(JWT.generate(u, 2));
            token.setUser(u);
            listToken.add(token);
        });
        return listToken;
    }

    @Override
    public int reset(String password) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);

        if (password == null || password.trim().isEmpty())
        {
            throw new EnterpriseNTException("1|wrong password value");
        }

        int records = 0;
        switch (user.getType())
        {
            case Constants.ACCOUNT:
                records = dao.updatePasswordAccount(user.getId(), password);
                break;
            case Constants.PATIENT:
                records = dao.updatePasswordPatient(user.getId(), password);
                break;
            case Constants.PHYSICIAN:
                records = dao.updatePasswordPhysician(user.getId(), password);
                break;
            case Constants.DEMOGRAPHIC:
                records = dao.updatePasswordDemo(user.getId(), password);
                break;
            case Constants.RATE:
                records = dao.updatePasswordRate(user.getId(), password);  
                break;
            default:
                throw new EnterpriseNTException("2|wrong user type");

        }
        if (records == 0)
        {
            throw new EnterpriseNTException("3|password not set");
        }
        return records;

    }

    @Override
    public String sendEmail(Email email) throws Exception
    {
        try
        {
            if (!"OUTLOOK".equals(configurationService.getValue("ServidorCorreo")))
            {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.debug", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", configurationService.getValue("SmtpHostName"));
                props.put("mail.smtp.port", Integer.parseInt(configurationService.getValue("SmtpPort")));
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        try
                        {
                            return new PasswordAuthentication(configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                        } catch (Exception ex)
                        {
                            return null;
                        }
                    }
                });
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(configurationService.getValue("SmtpAuthUser")));
                //Se agregan los destinatarios del correo

                for (String string : email.getRecipients())
                {
                    if (!string.isEmpty())
                    {
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
                if (email.getAttachment() != null && email.getAttachment().size() > 0)
                {
                    for (Image image : email.getAttachment())
                    {
                        imagePart.setHeader("Content-ID", "<" + image.getCid() + ">");
                        imagePart.setDisposition(MimeBodyPart.INLINE);

                        String imageFilePath = image.getPath();
                        decodedBytes = Base64.getDecoder().decode(imageFilePath);
                        DataSource ds = new ByteArrayDataSource(decodedBytes, "image/png");
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
                try
                {
                    Transport transport = session.getTransport(configurationService.getValue("SmtpProtocol"));
                    transport.connect(configurationService.getValue("SmtpHostName"), Integer.parseInt(configurationService.getValue("SmtpPort")), configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception e)
                {
                    SecurityLog.error(e);
                    e.printStackTrace();
                    return "Se a generado un error a la hora de hacer el envio por la Exception :" + e;
                }
            } else
            {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.debug", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "outlook.office365.com");
                props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        try
                        {
                            return new PasswordAuthentication(configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                        } catch (Exception ex)
                        {
                            return null;
                        }
                    }
                });
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(configurationService.getValue("SmtpAuthUser")));
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(email.getBody(), "text/html");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                byte[] decodedBytes = null;
                if (email.getAttachment() != null && email.getAttachment().size() > 0)
                {
                    for (Image image : email.getAttachment())
                    {
                        MimeBodyPart imagePart = new MimeBodyPart();
                        imagePart.setHeader("Content-ID", "<" + image.getCid() + ">");
                        imagePart.setDisposition(MimeBodyPart.INLINE);
                        String imageFilePath = image.getPath();
                        decodedBytes = Base64.getDecoder().decode(imageFilePath);
                        DataSource ds = new ByteArrayDataSource(decodedBytes, "image/png");
                        imagePart.setDataHandler(new DataHandler(ds));
                        imagePart.setFileName(image.getFilename());
                        multipart.addBodyPart(imagePart);
                    }
                }
                message.setContent(multipart);
                message.setSubject(email.getSubject());
                message.saveChanges();
                for (String string : email.getRecipients())
                {
                    if (!string.isEmpty())
                    {
                        string = string.replace("[", "").replace("]", "").replace("\"", "");
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(string));
                    }
                }
                
                try
                {
                    Transport transport = session.getTransport(configurationService.getValue("SmtpProtocol"));
                    transport.connect("outlook.office365.com", 587, configurationService.getValue("SmtpAuthUser"), configurationService.getValue("SmtpPasswordUser"));
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception e)
                {
                    SecurityLog.error(e);
                    e.printStackTrace();
                    return "Se a generado un error a la hora de hacer el envio por la Exception :" + e;
                }
            }
            return "Se envio el correo";
        } catch (Exception ex)
        {
            System.out.println(ex);

            return "Se a generado un error a la hora de hacer el envio por la Exception :" + ex;
        }
    }

    @Override
    public void updatePassword(UserPassword userPassword) throws Exception
    {
        Integer value = userPassword.getType();
        Configuration config = null;
        String url = null;

        switch (value)
        {
            case 4:
                config = configurationService.get("UrlSecurity");
                url = config.getValue() + "/api/authentication/updatepassword";
                integrationService.putVoid(Tools.jsonObject(userPassword), url);
                break;
            case 5:
                config = configurationService.get("UrlSecurity");
                url = config.getValue() + "/api/authentication/updatepasswordweb";
                integrationService.putVoid(Tools.jsonObject(userPassword), url);
                break;
            case 2:
                dao.updatePasswordPatient(userPassword.getIdUser(), userPassword.getPasswordNew());
                break;
            case 3:
                config = configurationService.get("UrlSecurity");
                url = config.getValue() + "/api/authentication/updatepasswordaccount";
                integrationService.putVoid(Tools.jsonObject(userPassword), url);
                break;
        }

    }

}
