/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.document.DocumentDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.document.Document;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.document.DocumentService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de documentos para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/12/2017
 * @see Creacion
 */
@Service
public class DocumentServiceEnterpriseNT implements DocumentService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DocumentDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<Document> listattachments(Long idOrder) throws Exception
    {
        List<Document> objDoc = dao.listattament(idOrder);
        objDoc.forEach(item ->
        {
            try
            {
                item.setFile(getAttachmentBase64(item.getPath()));
                User objUser = userDao.get(item.getUser().getId(), null, null, null);
                item.getUser().setUserName(objUser.getUserName());
            } catch (Exception e)
            {
            }
        });
        return objDoc;
    }

    @Override
    public List<Document> listattachments(Long idOrder, Integer idTest) throws Exception
    {
        List<Document> objDoc;
        if (idTest == null || idTest == 0)
        {
            objDoc = dao.listResultattament(idOrder);
        } else
        {
            objDoc = dao.listResultattament(idOrder, idTest);
        }
        objDoc.forEach(item ->
        {
            try
            {
                item.setFile(getAttachmentBase64(item.getPath()));
                User objUser = userDao.get(item.getUser().getId(), null, null, null);
                item.getUser().setUserName(objUser.getUserName());
            } catch (Exception e)
            {
            }
        });
        return objDoc;
    }
    
    public String getAttachmentBase64(String url) throws Exception
    {
        File file = new File(url);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fis.read(buffer);
        fis.close();

        String base64 = java.util.Base64.getEncoder().encodeToString(buffer);
        return base64;
    }

    @Override
    public List<Document> list(Long idOrder) throws Exception
    {
        List<Document> objDoc = dao.list(idOrder);
        objDoc.forEach(item ->
        {
            try
            {
                item.setFile(getAttachmentBase64(item.getPath()));
                User objUser = userDao.get(item.getUser().getId(), null, null, null);
                item.getUser().setUserName(objUser.getUserName());
            } catch (Exception e)
            {
            }
        });
        return objDoc;
    }

    @Override
    public List<Document> list(Long idOrder, Integer idTest) throws Exception
    {
        List<Document> objDoc;
        if (idTest == null || idTest == 0)
        {
            objDoc = dao.listResultDocument(idOrder);
        } else
        {
            objDoc = dao.listResultDocument(idOrder, idTest);
        }
        objDoc.forEach(item ->
        {
            try
            {
                item.setFile(getAttachmentBase64(item.getPath()));
                User objUser = userDao.get(item.getUser().getId(), null, null, null);
                item.getUser().setUserName(objUser.getUserName());
            } catch (Exception e)
            {
            }
        });
        return objDoc;
    }

    @Override
    public Document saveDocument(Document document) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        document.setUser(session);
        Document obj;
        List<AuditOperation> audit = new ArrayList<>();
        List<Document> documents;
        String path = configurationService.get("RutaAdjuntos").getValue();
        
        if (document.getIdOrder() != null)
        {
            if (document.getIdTest() == null || document.getIdTest() == 0)
            {
                documents = dao.list(document.getIdOrder());
                if (documents.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(document.getName())))
                {
                    if (document.isReplace())
                    {
                        document.setPath(path + "/Adjuntos/" + document.getIdOrder() + "/"+ document.getName());
                        obj = dao.updateOrderDocument(document);
                        
                        byte[] ImaBytes = Base64.decodeBase64(obj.getFile());
                        InputStream inputStream = new ByteArrayInputStream(ImaBytes);
                        saveFileAttachment(inputStream, path + "\\Adjuntos\\" + obj.getIdOrder() + "\\"  , obj.getName()  );
                        
                        audit.add(new AuditOperation(obj.getIdOrder(), null, null, document.isReplace() ? AuditOperation.ACTION_UPDATE : AuditOperation.ACTION_INSERT, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                    } else
                    {
                        throw new EnterpriseNTException(Arrays.asList("1|document already exists"));
                    }
                } else
                {
                    document.setPath(path + "/Adjuntos/" + document.getIdOrder() + "/"+ document.getName());
                    obj = dao.saveOrderDocument(document);
                    
                    byte[] ImaBytes = Base64.decodeBase64(obj.getFile());
                    InputStream inputStream = new ByteArrayInputStream(ImaBytes);
                    saveFileAttachment(inputStream, path + "\\Adjuntos\\" + obj.getIdOrder() + "\\" , obj.getName() );
                    
                    audit.add(new AuditOperation(obj.getIdOrder(), null, null, document.isReplace() ? AuditOperation.ACTION_UPDATE : AuditOperation.ACTION_INSERT, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                }
            } else
            {
                documents = dao.listResultDocument(document.getIdOrder(), document.getIdTest());
                if (documents.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(document.getName())))
                {
                    if (document.isReplace())
                    {
                        document.setPath(path + "/Adjuntos/" + document.getIdOrder() + "/" + document.getIdTest() + "/" +  document.getName());
                        obj = dao.updateResultDocument(document);
                        byte[] ImaBytes = Base64.decodeBase64(obj.getFile());
                        InputStream inputStream = new ByteArrayInputStream(ImaBytes);
                        saveFileAttachment(inputStream, path + "\\Adjuntos\\" + obj.getIdOrder() + "\\ " + obj.getIdTest() + "\\ ", obj.getName()  );

                        audit.add(new AuditOperation(document.getIdOrder(), document.getIdTest(), null, document.isReplace() ? AuditOperation.ACTION_UPDATE : AuditOperation.ACTION_INSERT, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                    } else
                    {
                        throw new EnterpriseNTException(Arrays.asList("1|document already exists"));
                    }
                } else
                {
                    document.setPath(path + "/Adjuntos/" + document.getIdOrder() + "/" + document.getIdTest() + "/" +  document.getName());
                    obj = dao.saveResultDocument(document);
                    byte[] ImaBytes = Base64.decodeBase64(obj.getFile());
                    InputStream inputStream = new ByteArrayInputStream(ImaBytes);
                    saveFileAttachment(inputStream, path + "\\Adjuntos\\" + obj.getIdOrder() + "\\" + obj.getIdTest() + "\\", obj.getName() );
               
                    audit.add(new AuditOperation(obj.getIdOrder(), document.getIdTest(), null, document.isReplace() ? AuditOperation.ACTION_UPDATE : AuditOperation.ACTION_INSERT, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                }
                //Actualiza resultado
                ResultTest result = resultService.get(document.getIdOrder()).stream().filter(test -> test.getTestId() == document.getIdTest()).findAny().orElse(null);
                if (result != null)
                {
                    if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
                    {
                        result.setResult(Constants.RESULT_ATTACHMENT);
                        result.setResultChanged(true);
                        result.setUserId(session.getId());
                        result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                        resultService.reportedTest(result);
                    }
                }
                resultTestDao.updateAttachmentCount(obj.getIdOrder(), obj.getIdTest(), true);
            }
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0|order"));
        }

        Document objAudit;
        objAudit = (Document) obj.clone();
        objAudit.setFile("");

        trackingService.registerOperationTracking(audit);

        return obj;
    }
    
    
    public static void saveFileAttachment(InputStream inputFile, String folderPath, String fileName) throws IOException {
        OutputStream outputFile = null;

        try {
            // Create the folder if it doesn't exist
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // Create the folder and any parent directories if necessary
            }

            // Create the output file in the specified path
            File file = new File(folderPath + fileName);
            outputFile = new FileOutputStream(file);

            // Transfer data from input stream to output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputFile.read(buffer)) > 0) {
                outputFile.write(buffer, 0, length);
            }
        } finally {
            // Close input and output streams
            if (outputFile != null) {
                outputFile.close();
            }
            if (inputFile != null) {
                inputFile.close();
            }
        }
    }

    @Override
    public Document deleteDocument(Document document) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        document.setUser(session);
        Document obj;
        List<AuditOperation> audit = new ArrayList<>();
        List<Document> documents;
        if (document.getIdOrder() != null)
        {
            if (document.getIdTest() == null || document.getIdTest() == 0)
            {
                documents = dao.list(document.getIdOrder());
                if (documents.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(document.getName())))
                {
                    if (document.isDelete())
                    {
                        dao.deleteOrderDocument(document);
                        audit.add(new AuditOperation(document.getIdOrder(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                    } else
                    {
                        throw new EnterpriseNTException(Arrays.asList("1|document does not exist"));
                    }
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("1|document does not exist"));
                }
            } else
            {
                documents = dao.listResultDocument(document.getIdOrder(), document.getIdTest());
                if (documents.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(document.getName())))
                {
                    if (document.isDelete())
                    {
                        dao.deleteResultDocument(document);
                        audit.add(new AuditOperation(document.getIdOrder(), document.getIdTest(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_DOCUMENT, document.getName(), null, null, null, null));
                    } else
                    {
                        throw new EnterpriseNTException(Arrays.asList("1|document does not exist"));
                    }
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("1|document does not exist"));
                }
            }
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0|order"));
        }

        trackingService.registerOperationTracking(audit);
        resultTestDao.updateAttachmentCount(document.getIdOrder(), document.getIdTest() == null ? 0 : document.getIdTest(), false);

        return document;
    }
}
