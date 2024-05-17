/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.SpecimenProtocolDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;
import net.cltech.enterprisent.domain.masters.pathology.Sheet;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenProtocolService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de el maestro de protocolos de los especimenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/04/2021
 * @see Creaciòn
 */
@Service
public class SpecimenProtocolServiceEnterpriseNT implements SpecimenProtocolService
{
    @Autowired
    private SpecimenProtocolDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    @Autowired
    private OrderPathologyService orderPathologyService;
    
    @Override
    public SpecimenProtocol create(SpecimenProtocol protocol) throws Exception {

        List<String> errors = validateFields(false, protocol);
        if (errors.isEmpty()) {
            SpecimenProtocol created = dao.create(protocol);
            trackingService.registerConfigurationTracking(null, created, SpecimenProtocol.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<SpecimenProtocol> list() throws Exception {
        List<SpecimenProtocol> protocols = dao.list();
        protocols.forEach( protocol -> {
            try
            {
                protocol.setUserCreated(trackingPathologyDao.get(protocol.getUserCreated().getId()));
                protocol.setUserUpdated(trackingPathologyDao.get(protocol.getUserUpdated().getId()));
                protocol.setSpecimen(orderPathologyService.getSpecimenDataLis(protocol.getSpecimen().getId()));
            } catch (Exception e) {}
        });
        return protocols;
    }

    @Override
    public SpecimenProtocol get(Integer id, Integer specimen) throws Exception
    {
        SpecimenProtocol protocol = dao.get(id, specimen);
        try
        {
            if (protocol != null)
            {
                protocol.setSheets(dao.listSheetsByProtocol(id));
                protocol.setSpecimen(orderPathologyService.getSpecimenDataLis(protocol.getSpecimen().getId()));
                protocol.setUserCreated(trackingPathologyDao.get(protocol.getUserCreated().getId()));
                protocol.setUserUpdated(trackingPathologyDao.get(protocol.getUserUpdated().getId()));
            }
        } catch (Exception e) {}
        return protocol;
    }

    @Override
    public SpecimenProtocol update(SpecimenProtocol protocol) throws Exception {

        List<String> errors = validateFields(true, protocol);
        if (errors.isEmpty()) 
        {            
            SpecimenProtocol protocolC = dao.get(protocol.getId(), null);
            SpecimenProtocol modifited = dao.update(protocol);
            trackingService.registerConfigurationTracking(protocolC, modifited, SpecimenProtocol.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    private List<String> validateFields(boolean isEdit, SpecimenProtocol protocol) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (protocol.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(protocol.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (protocol.getUserUpdated().getId() == null || protocol.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (protocol.getUserCreated().getId() == null || protocol.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }
        
        if (protocol.getSpecimen().getId() != null && protocol.getSpecimen().getId() != 0)
        {
            SpecimenProtocol protocolC = dao.get(null, protocol.getSpecimen().getId());
            if (protocolC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(protocol.getId(), protocolC.getId()))
                    {
                        errors.add("1|specimen");
                    }
                } else
                {
                    errors.add("1|specimen");
                }
            }
        } else
        {
            errors.add("0|specimen");
        }
        
        if(protocol.getCasete().getId() == 0 || protocol.getCasete().getId() == null) 
        {
           errors.add("0|casete");
        }
        
        if(protocol.getQuantity()== 0 || protocol.getQuantity() == null) 
        {
           errors.add("0|quantity");
        }
        
        if(protocol.getProcessingHours()== 0 || protocol.getProcessingHours() == null) 
        {
           errors.add("0|processingHours");
        }
        
        errors.addAll(validateFieldsSheets(protocol));

        return errors;
    }
    
    /**
     * Valida los campos enviados de las laminas
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFieldsSheets(SpecimenProtocol validate) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (validate.getSheets().size() > 0)
        {
            int row = 0;
            for (Sheet sheet : validate.getSheets())
            {
                if (sheet.getColoration().getId() == null || sheet.getColoration().getId() == 0)
                {
                    errors.add("0|coloration|" + row);
                }
                if (sheet.getQuantity() == null || sheet.getQuantity() == 0)
                {
                    errors.add("0|quantity|" + row);
                }
                row++;
            }
        } else
        {
            errors.add("0|Sheets");
        }

        return errors;
    }
    
    @Override
    public int createSheets(SpecimenProtocol protocol) throws Exception
    {
        List<String> errors = validateFieldsSheets(protocol);
        if (errors.isEmpty())
        {
            return dao.createSheetsByProtocol(protocol);

        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
}
