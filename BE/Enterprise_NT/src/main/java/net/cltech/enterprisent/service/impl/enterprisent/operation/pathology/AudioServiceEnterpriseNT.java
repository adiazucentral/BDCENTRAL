/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.AudioDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.pathology.Audio;
import net.cltech.enterprisent.service.interfaces.operation.pathology.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de los audios de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/06/2021
 * @see Creaciòn
 */
@Service
public class AudioServiceEnterpriseNT implements AudioService 
{
    @Autowired
    private AudioDao dao;
    
    @Override
    public Audio create(Audio audio) throws Exception {

        List<String> errors = validateFields(false, audio);
        if (errors.isEmpty()) {
            Audio created = dao.create(audio);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public Audio update(Audio audio) throws Exception 
    {

        List<String> errors = validateFields(true, audio);
        if (errors.isEmpty()) 
        {            
            Audio modifited = dao.update(audio);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    private List<String> validateFields(boolean isEdit, Audio audio) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (audio.getUserUpdated().getId() == null || audio.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (audio.getUserCreated().getId() == null || audio.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }
        
        if(audio.getName() == null || audio.getName().isEmpty() )
        {
           errors.add("0|name");
        }
        
        if(audio.getExtension()== null || audio.getExtension().isEmpty()) 
        {
           errors.add("0|extension");
        }
        
        if(audio.getUrl()== null || audio.getUrl().isEmpty()) 
        {
           errors.add("0|url");
        }
        
        return errors;
    }
}
