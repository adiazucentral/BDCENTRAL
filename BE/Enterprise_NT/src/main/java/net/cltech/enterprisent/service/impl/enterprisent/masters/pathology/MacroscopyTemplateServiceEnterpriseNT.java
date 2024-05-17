/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.FieldDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.MacroscopyTemplateDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SamplePathologyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.service.interfaces.masters.pathology.MacroscopyTemplateService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de plantillas de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 09/06/2021
 * @see Creaciòn
 */
@Service
public class MacroscopyTemplateServiceEnterpriseNT implements MacroscopyTemplateService
{
    @Autowired
    private MacroscopyTemplateDao dao;
    @Autowired
    private FieldDao fieldDao;
    @Autowired
    private SamplePathologyDao samplePathologyDao;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private OrderPathologyService orderPathologyService;
    
    @Override
    public List<Field> fields(int specimenId) throws Exception
    {
        List<Field> fields = dao.fields(specimenId);
        List<Field> list = fieldDao.list();    
        list.forEach( field -> {
            try
            {
                Field exists = fields.stream().filter( sub -> sub.getId().equals(field.getId())).findFirst().orElse(null);
                if(exists != null) {
                    field.setSelected(true);
                    field.setOrder(exists.getOrder());
                }
                field.setUserCreated(trackingPathologyDao.get(field.getUserCreated().getId()));
            } catch (Exception e) {}
        });
        return list;
    }
    
    @Override
    public int assignFields(MacroscopyTemplate template) throws Exception
    {
        List<String> errors = validateFields(template);
        if (errors.isEmpty())
        {
            MacroscopyTemplate before = new MacroscopyTemplate();
            
            before.setFields(fields(template.getSpecimen().getId()).stream()
                    .filter(selected -> selected.isSelected())
                    .collect(Collectors.toList()));

            trackingService.registerConfigurationTracking(before, template, MacroscopyTemplate.class);
            dao.deleteFields(template.getSpecimen().getId());
            return dao.insertFields(template);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<MacroscopyTemplate> templates(int caseId) throws Exception
    {
        List<Specimen> specimens = samplePathologyDao.getSpecimensByCase(caseId);
        List<MacroscopyTemplate> list = new ArrayList<>();
        
        specimens.forEach( specimen -> {
            try {
                list.add(setTemplate(specimen, caseId));
            } catch (Exception ex) {
                Logger.getLogger(MacroscopyTemplateServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return list;
    }
    
    private MacroscopyTemplate setTemplate(Specimen specimen, int idCase) throws Exception 
    {
        MacroscopyTemplate template = new MacroscopyTemplate();
        /*Informacion de la muestra*/
        Specimen specimenData = orderPathologyService.getSpecimenDataLis(specimen.getId());
        specimen.setName(specimenData.getName());
        specimen.setCode(specimenData.getCode());
        specimen.setState(specimenData.isState());
        template.setSpecimen(specimen);        
        template.setFields(dao.fieldsByCase(specimen.getId(), idCase));
        return template;
    }
    
    /**
     * Validación de campos para la asignación de campos
     *
     * @param template bean con informacion de la relación
     *
     * @return lista con errores
     * @throws Exception
    */
    private List<String> validateFields(MacroscopyTemplate template) throws Exception
    {
        List<String> errors = new ArrayList<>();
        
        if (template.getSpecimen().getId() == 0 || template.getSpecimen().getId() == null)
        {
            errors.add("0|specimen");
        }

        if (template.getFields().isEmpty()) {
            errors.add("0|fields");
        }
        return errors;
    }
}
