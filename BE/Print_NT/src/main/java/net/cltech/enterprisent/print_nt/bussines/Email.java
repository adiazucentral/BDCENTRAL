/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import net.cltech.enterprisent.print_nt.App;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintConfiguration;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintNode;
import net.cltech.enterprisent.print_nt.bussines.persistence.NTPersistence;
import net.cltech.enterprisent.print_nt.tools.Log;

/**
 *
 * @author equijano
 */
public class Email
{

    private final Gson gson;
    private final NTPersistence nt;
    private final App app;
    private final PrintConfiguration printConfiguration;
    private BodyType bodyType;

    public Email(NTPersistence nt, App app, PrintConfiguration printConfiguration)
    {
        this.gson = new Gson();
        this.nt = nt;
        this.app = app;
        this.printConfiguration = printConfiguration;
    }

    public void sendEmail(List<String> recipients, String body, String subject, String attachment, String nameFile) throws URISyntaxException, Exception
    {

        try
        {
            if (!"OUTLOOK".equals(printConfiguration.getServerEmail()))
            {
                //Parametros para enviar el correo
                Properties props = System.getProperties();
                props.put("mail.smtp.host", printConfiguration.getHost());
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", printConfiguration.getPort());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", printConfiguration.getPort());
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.transport.protocol", "smtp");
                Session mailSession = Session.getDefaultInstance(props, null);
                mailSession.setDebug(false);

                MimeMessage mailMessage = new MimeMessage(mailSession);
                //Se agrega quien es el emisor del correo
                mailMessage.setFrom(new InternetAddress(printConfiguration.getEmail()));
                //Se agregan los destinatarios del correo
                for (String string : recipients)
                {
                    String[] arrOfStr = string.split(";");

                    for (String a : arrOfStr)
                    {
                        mailMessage.addRecipient(RecipientType.TO, new InternetAddress(a));
                    }

                }
                //Se agrega el contenido del correo
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                body = body == null ? "" : body;
                messageBodyPart.setContent(body, "text/html");
                // se crea multi-part y se le agrega el cuerpo del correo
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                // se crea MimeBodyPart para asignarle la imagen a enviar
                MimeBodyPart imagePart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                imagePart.setDataHandler(new DataHandler(source));
                imagePart.setFileName(nameFile);
                multipart.addBodyPart(imagePart);

                // se agrega las multipart al mensaje y se agrega el asunto
                mailMessage.setContent(multipart);
                mailMessage.setSubject(subject);
                mailMessage.saveChanges();
                //Se establece la conexion para enviar el mensaje
                Transport transport = mailSession.getTransport("smtp");
                transport.connect(printConfiguration.getHost(), Integer.parseInt(printConfiguration.getPort()), printConfiguration.getEmail(), printConfiguration.getPassword());
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
                transport.close();
            } else
            {
                ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
                ExchangeCredentials credentials = new WebCredentials(printConfiguration.getEmail(), printConfiguration.getPassword());
                service.setCredentials(credentials);

                service.setUrl(new URI(printConfiguration.getHost()));
                EmailMessage msg = new EmailMessage(service);
                msg.setSubject(subject);
                MessageBody msgBody = new MessageBody();
                msgBody.setBodyType(bodyType.valueOf("HTML"));
                msgBody.setText(body);
                msg.setBody(msgBody);

                if (attachment != null)
                {
                    msg.getAttachments().addFileAttachment(attachment);
                }
                for (String string : recipients)
                {
                    if (!string.isEmpty())
                    {
                        string = string.replace("[", "").replace("]", "").replace("\"", "");

                        String[] arrOfStr = string.split(";");

                        for (String a : arrOfStr)
                        {
                            msg.getToRecipients().add(a);
                        }
                    }
                }
                msg.send(); //send email
            }
        } catch (AddressException ex)
        {

            Log.info("error email" + ex);
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex)
        {
            Log.info("error email" + ex);
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * permite enviar correos y guardar losg en base de datos por llave de
     * configuracion
     *
     * @param PrintNode
     * @return void
     * @since 1.0
     */
    public void sendEmail(PrintNode print, String attachment) throws URISyntaxException, Exception
    {

        List<String> recipients = Arrays.asList(print.getPatientEmail());
        String body = print.getEmailBody();
        String subject = print.getEmailSubjectPatient();
        String nameFile = print.getNameFile() + ".pdf";

        try
        {
            if (!"OUTLOOK".equals(printConfiguration.getServerEmail()))
            {
                //Parametros para enviar el correo
                Properties props = System.getProperties();
                props.put("mail.smtp.host", printConfiguration.getHost());
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", printConfiguration.getPort());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", printConfiguration.getPort());
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.transport.protocol", "smtp");
                Session mailSession = Session.getDefaultInstance(props, null);
                mailSession.setDebug(false);

                MimeMessage mailMessage = new MimeMessage(mailSession);
                //Se agrega quien es el emisor del correo
                mailMessage.setFrom(new InternetAddress(printConfiguration.getEmail()));
                //Se agregan los destinatarios del correo
                for (String string : recipients)
                {
                    String[] arrOfStr = string.split(";");

                    for (String a : arrOfStr)
                    {
                        mailMessage.addRecipient(RecipientType.TO, new InternetAddress(a));
                    }

                }
                //Se agrega el contenido del correo
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body, "text/html");
                // se crea multi-part y se le agrega el cuerpo del correo
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                // se crea MimeBodyPart para asignarle la imagen a enviar
                MimeBodyPart imagePart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                imagePart.setDataHandler(new DataHandler(source));
                imagePart.setFileName(nameFile);
                multipart.addBodyPart(imagePart);

                // se agrega las multipart al mensaje y se agrega el asunto
                mailMessage.setContent(multipart);
                mailMessage.setSubject(subject);
                mailMessage.saveChanges();
                //Se establece la conexion para enviar el mensaje
                Transport transport = mailSession.getTransport("smtp");
                transport.connect(printConfiguration.getHost(), Integer.parseInt(printConfiguration.getPort()), printConfiguration.getEmail(), printConfiguration.getPassword());
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
                transport.close();
            } else
            {
                ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
                ExchangeCredentials credentials = new WebCredentials(printConfiguration.getEmail(), printConfiguration.getPassword());
                service.setCredentials(credentials);

                service.setUrl(new URI(printConfiguration.getHost()));
                EmailMessage msg = new EmailMessage(service);
                msg.setSubject(subject);
                MessageBody msgBody = new MessageBody();
                msgBody.setBodyType(bodyType.valueOf("HTML"));
                msgBody.setText(body);
                msg.setBody(msgBody);

                if (attachment != null)
                {
                    msg.getAttachments().addFileAttachment(attachment);
                }
                for (String string : recipients)
                {
                    if (!string.isEmpty())
                    {
                        string = string.replace("[", "").replace("]", "").replace("\"", "");

                        String[] arrOfStr = string.split(";");

                        for (String a : arrOfStr)
                        {
                            msg.getToRecipients().add(a);
                        }
                    }
                }
                msg.send(); //send email
            }
            // crea file .txt por correo enviado
            String message = nameFile + "\\" + recipients.get(0).split(";")[0] + "\\" + nameFile.substring(12, nameFile.length() - 3).replace("_", " ");

            createFileByEmail(message, print);
        } catch (AddressException ex)
        {

            Log.info("error email" + ex);
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            createFileByEmail(ex.toString(), print);
        } catch (MessagingException ex)
        {
            Log.info("error email" + ex);
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            createFileByEmail(ex.toString(), print);
        } catch (Exception e)
        {
            createFileByEmail(e.toString(), print);
        }
    }

    /**
     * Crea Archivo .txt y guarda log en base de datos por correo enviado
     *
     * @param message mensage a gurdar en el file
     * @param print objeto con los datos para en envio del correo
     * @return void
     */
    public static void createFileByEmail(String message, PrintNode print) throws IOException
    {
        Log.saveLogs(message, print);

    }

}
