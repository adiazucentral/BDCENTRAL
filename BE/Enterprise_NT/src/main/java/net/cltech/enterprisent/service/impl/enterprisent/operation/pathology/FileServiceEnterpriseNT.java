/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.FileDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.pathology.File;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.operation.pathology.FileService;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de documentos para Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/03/2021
 * @see Creacion
 */
@Service
public class FileServiceEnterpriseNT implements FileService
{
    @Autowired
    private FileDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HttpServletRequest request;
    
    @Override
    public List<File> list(Integer idCase) throws Exception
    {
        List<File> objDoc = dao.list(idCase);
        objDoc.forEach(item ->
        {
            try
            {
                User objUser = userDao.get(item.getUserCreated().getId(), null, null, null);
                item.getUserCreated().setUserName(objUser.getUserName());
            } catch (Exception e)
            {
            }
        });
        return objDoc;
    }
        
    @Override
    public File save(File file) throws Exception
    {
        File obj;
        List<File> documents;
        if (file.getIdCase() != null)
        {
            documents = dao.list(file.getIdCase());
            
            if (documents.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(file.getName())))
            {
                if (file.isReplace())
                {
                    obj = dao.updateCaseDocument(file);
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("1|document already exists"));
                }
            } else
            {
                obj = dao.saveCaseDocument(file);
            }
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0|case"));
        }
        return obj;
    }
    
    @Override
    public File delete(File file) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        file.setUserUpdated(session);
        
        List<File> files;
        if (file.getIdCase() != null)
        {
            files = dao.list(file.getIdCase());
            if (files.stream().anyMatch(doc -> doc.getName().equalsIgnoreCase(file.getName())))
            {
                if (file.isDelete())
                {
                    dao.delete(file);
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
            throw new EnterpriseNTException(Arrays.asList("0|case"));
        }
        return file;
    }
}
