/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import net.cltech.enterprisent.dao.interfaces.masters.pathology.SpecimenDao;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Implementa los servicios de el maestro de especimenes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 08/04/2021
 * @see Creaciòn
 */
@Service
public class SpecimenServiceEnterpriseNT implements SpecimenService
{
    @Autowired
    private SpecimenDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;   

    @Override
    public List<Specimen> list() throws Exception
    {        
        List<Specimen> specimens = dao.list();
        specimens.forEach( specimen -> {
            try
            {
                specimen.setUserCreated(trackingPathologyDao.get(specimen.getUserCreated().getId()));
            } catch (Exception e) {}
        });
        return specimens;
    }
    
    @Override
    public List<Specimen> subSamples(int specimenId) throws Exception
    {
        List<Integer> subSamples = dao.listSubSample(specimenId);
        List<Specimen> list = dao.list().stream().filter( specimen -> !specimen.getId().equals(specimenId)).collect(Collectors.toList());
        list.forEach( specimen -> {
            try
            {
                Integer subsample = subSamples.stream().filter( sub -> sub.equals(specimen.getId())).findFirst().orElse(null);
                if(subsample != null) {
                    specimen.setSelected(true);
                }
                specimen.setUserCreated(trackingPathologyDao.get(specimen.getUserCreated().getId()));
            } catch (Exception e) {}
        });
        return list;
    }

    @Override
    public int assignSubSamples(Specimen specimen) throws Exception
    {
        List<String> errors = validateSubSample(specimen);
        if (errors.isEmpty())
        {
            Specimen before = new Specimen(specimen.getId());
            
            before.setSubSamples(subSamples(specimen.getId()).stream()
                    .filter(selected -> selected.isSelected())
                    .collect(Collectors.toList()));

            trackingService.registerConfigurationTracking(before, specimen, Specimen.class);
            dao.deleteSubSamples(specimen.getId());
            return dao.insertSubSample(specimen);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    /**
     * Validación de campos para la asignación de submuestras
     *
     * @param specimen bean con informacion de la relación
     *
     * @return lista con errores
     * @throws Exception
    */
    private List<String> validateSubSample(Specimen specimen) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (specimen.getId() == null)
        {
            errors.add("0|id");
        }
        return errors;
    }
    
    @Override
    public List<Study> studies() throws Exception
    {
        return dao.studies();
    }
    
    @Override
    public List<Study> getStudiesBySample(Integer sample) throws Exception
    {
        List<Integer> studies = dao.getStudiesBySample(sample);
        List<Study> st = new ArrayList<>(); 
        studies.forEach( study -> {
            try
            {
                st.add(dao.getStudieById(study));
            } catch (Exception e)
            {
            }
        });
        return st;
    }
    
    @Override
    public List<Specimen> getSubsamplesBySpecimen(int specimenId) throws Exception
    {
        List<Integer> subSamples = dao.listSubSample(specimenId);
        if(subSamples.size() > 0) {
            List<Specimen> list = dao.list().stream().filter( specimen ->
                    subSamples.contains( specimen.getId())
            ).collect(Collectors.toList());
            return list;
       
        } else {
            return new ArrayList<>(0);
        }
    }
}
