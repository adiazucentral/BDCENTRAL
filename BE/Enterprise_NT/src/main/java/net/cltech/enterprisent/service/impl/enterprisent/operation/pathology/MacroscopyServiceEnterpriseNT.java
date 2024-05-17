/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.MacroscopyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.operation.pathology.Macroscopy;
import net.cltech.enterprisent.service.interfaces.masters.pathology.MacroscopyTemplateService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.MacrocopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.service.interfaces.operation.pathology.CaseService;
import net.cltech.enterprisent.tools.Constants;

/**
 * Implementa los servicios de informacion de las descripciones macroscopicas de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/06/2021
 * @see Creaciòn
 */
@Service
public class MacroscopyServiceEnterpriseNT implements MacrocopyService
{
    @Autowired
    private MacroscopyDao dao;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    @Autowired
    private MacroscopyTemplateService macroscopyTemplateService;
    @Autowired
    private CaseService caseService;
        
    @Override
    public Macroscopy getByCase(int idCase) throws Exception
    {
        Macroscopy macroscopy = dao.get(idCase);
        if( macroscopy != null ) {
            macroscopy.setPathologist(trackingPathologyDao.getUser(macroscopy.getPathologist().getId()));
        }
        else {
            macroscopy = new Macroscopy();
        }
        macroscopy.setTemplates(macroscopyTemplateService.templates(idCase));
        return macroscopy;
    }

    @Override
    public Macroscopy create(Macroscopy macroscopy) throws Exception {

        List<String> errors = validateFields(false, macroscopy);
        if (errors.isEmpty()) {
            Macroscopy created = dao.create(macroscopy);
            if(created.getTemplates().size() > 0) {
                saveValues(created);
            }
            if(created.getTranscription() == 1) {
                Case casePat = new Case();
                casePat.setId(macroscopy.getCasePat());
                casePat.setStatus(Constants.TRANSCRIPTION);
                caseService.changeStatus(casePat);
            } else {
                if(macroscopy.getDraft() == 0) {
                    Case casePat = new Case();
                    casePat.setId(macroscopy.getCasePat());
                    casePat.setStatus(Constants.TISSUEPROCESSOR);
                    caseService.changeStatus(casePat);
                }
            }
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public Macroscopy update(Macroscopy macroscopy) throws Exception
    {
        List<String> errors = validateFields(true, macroscopy);
        if (errors.isEmpty())
        {
            macroscopy = dao.update(macroscopy);
            saveValues(macroscopy);
            if(macroscopy.getTranscription() == 1) {
                Case casePat = new Case();
                casePat.setId(macroscopy.getCasePat());
                casePat.setStatus(Constants.TRANSCRIPTION);
                caseService.changeStatus(casePat);
            } else {
                if(macroscopy.getDraft() == 0) {
                    Case casePat = new Case();
                    casePat.setId(macroscopy.getCasePat());
                    casePat.setStatus(Constants.TISSUEPROCESSOR);
                    caseService.changeStatus(casePat);
                }
            }
            return macroscopy;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    private void saveValues(Macroscopy macroscopy) throws Exception
    {
        dao.deleteValues(macroscopy.getId());
        for (MacroscopyTemplate template : macroscopy.getTemplates())
        {
            dao.insertValues(template.getFields(), macroscopy.getId());
        }
    }
    
    @Override
    public List<Macroscopy> getPendingTranscripts() throws Exception
    {
        List<Macroscopy> list = dao.getPendingTranscripts();
        list.forEach( macroscopy -> {
            try
            {
                macroscopy.setPathologist(trackingPathologyDao.getUser(macroscopy.getPathologist().getId()));
            } catch (Exception e)
            {
            }
        });
        return list;
    }
    
    @Override
    public Macroscopy transcript(Macroscopy macroscopy) throws Exception
    {
        List<String> errors = validateFieldsTranscript(macroscopy);
        if (errors.isEmpty())
        {
            macroscopy = dao.transcript(macroscopy);
            saveValues(macroscopy);
            if(macroscopy.getDraft() == 0 && macroscopy.getAuthorization() == 0) {
                Case casePat = new Case();
                casePat.setId(macroscopy.getCasePat());
                casePat.setStatus(Constants.TISSUEPROCESSOR);
                caseService.changeStatus(casePat);
            }
            return macroscopy;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public Macroscopy authorization(Macroscopy macroscopy) throws Exception
    {
        List<String> errors = validateFieldsAuthorization(macroscopy);
        if (errors.isEmpty())
        {
            macroscopy = dao.authorization(macroscopy);
            saveValues(macroscopy);
            if(macroscopy.getDraft() == 0) {
                Case casePat = new Case();
                casePat.setId(macroscopy.getCasePat());
                casePat.setStatus(Constants.TISSUEPROCESSOR);
                caseService.changeStatus(casePat);
            }
            return macroscopy;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Macroscopy> getPendingAuthorizations(Integer idUser) throws Exception
    {
        List<Macroscopy> list = dao.getPendingAuthorizations(idUser);
        list.forEach( macroscopy -> {
            try
            {
                macroscopy.setPathologist(trackingPathologyDao.getUser(macroscopy.getPathologist().getId()));
                macroscopy.setTranscriber(trackingPathologyDao.getUser(macroscopy.getTranscriber().getId()));
            } catch (Exception e)   
            {
            }
        });
        return list;
    }
    
    private List<String> validateFields(boolean isEdit, Macroscopy macroscopy) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        
        if(macroscopy.getCasePat() == null || macroscopy.getCasePat() == 0 )
        {
           errors.add("0|case");
        }
        
        if(macroscopy.getPathologist().getId() == null || macroscopy.getPathologist().getId() == 0) 
        {
           errors.add("0|pathologist");
        }
        
        if(macroscopy.getTranscription() == null || macroscopy.getTranscription() > 1) 
        {
           errors.add("0|transcription");
        }
        
        if( macroscopy.getTemplates().size() > 0 ) {
           errors.addAll(validateValues(macroscopy));
        } else {
            if(macroscopy.getAudio().getId() == null || macroscopy.getAudio().getId() == 0) 
            {
               errors.add("0|description");
            }
        }
        
        return errors;
    }
    
    private List<String> validateFieldsTranscript(Macroscopy macroscopy) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        
        if(macroscopy.getId() == null || macroscopy.getId() == 0 )
        {
           errors.add("0|description");
        }
        
        if(macroscopy.getCasePat() == null || macroscopy.getCasePat() == 0 )
        {
           errors.add("0|case");
        }
        
        if(macroscopy.getTranscriber().getId() == null || macroscopy.getTranscriber().getId() == 0) 
        {
           errors.add("0|transcriber");
        }
        
        errors.addAll(validateValues(macroscopy));
                  
        return errors;
    }
    
    private List<String> validateFieldsAuthorization(Macroscopy macroscopy) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        
        if(macroscopy.getId() == null || macroscopy.getId() == 0 )
        {
           errors.add("0|description");
        }
        
        if(macroscopy.getCasePat() == null || macroscopy.getCasePat() == 0 )
        {
           errors.add("0|case");
        }
        
        errors.addAll(validateValues(macroscopy));
                  
        return errors;
    }
    
    /**
    * Valida los valores de los campos de las plantillas
    *
    * @param validate entidad a validar
    *
    * @return lista de campos que son requeridos y no estan establecidos
    */
    private List<String> validateValues(Macroscopy macroscopy) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (macroscopy.getTemplates().size() > 0)
        {
            for (MacroscopyTemplate template : macroscopy.getTemplates())
            {
                int row = 0;
                for(Field field : template.getFields()) {
                    if(field.getValue() == null || field.getValue().isEmpty()) {
                    errors.add("0|value|" + row);
                    }
                    row++;
                }
            }
        } else
        {
            errors.add("0|templates");
        }
        return errors;
    }
}
