/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.OrganDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.StudyTypeDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.OrderPathologyDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.OrderPathology;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenService;


/**
 * Implementa los servicios de informacion de las ordenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 06/10/2020
 * @see Creaciòn
 */
@Service
public class OrderPathologyServiceEnterpriseNT implements OrderPathologyService 
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderPathologyDao dao;
    @Autowired
    private StudyTypeDao studyTypeDao;
    @Autowired
    private OrganDao organDao;
    @Autowired
    private SpecimenService specimenService;
    
    @Override
    public List<OrderPathology> list() throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        return dao.list(null, true, session.getBranch());
    }
    
    @Override
    public List<OrderPathology> listByFilters(ResultFilter filter) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        return dao.list(filter, true, session.getBranch());
    }
    
    @Override
    public List<Specimen> specimenByOrden(long idOrder) throws Exception
    {
        List<Specimen> samplesOrder = dao.samplesByOrder(idOrder);
        List<Specimen> list = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        samplesOrder.forEach( sample -> {
            try {
                List<Study> studies = dao.testsBySample(idOrder, sample.getId());
                sample.setOrgan(organDao.findOrganBySpecimen(sample.getId()));
                sample.setSubSamples(specimenService.getSubsamplesBySpecimen(sample.getId()));
                Specimen newSpecimen;
                for (int i = 0; i < studies.size(); i++) {
                    try {
                        StudyType studyType = studyTypeDao.findstudyTypeByStudy(studies.get(i).getId());
                        if(studyType == null) {
                            errors.add("0|study| " + studies.get(i).getName());
                        } else {
                            if(list.size() > 0) {
                                Specimen foundSpecimen = list.stream().filter( specimen -> 
                                    specimen.getId().equals(sample.getId()) && specimen.getStudyType().getId().equals( studyType.getId()))
                                    .findAny().orElse(null);

                                if(foundSpecimen == null) {
                                    newSpecimen = new Specimen();
                                    newSpecimen.setId(sample.getId());
                                    newSpecimen.setCode(sample.getCode());
                                    newSpecimen.setName(sample.getName());
                                    newSpecimen.setContainer(sample.getContainer());
                                    newSpecimen.setStudyType(studyType);
                                    newSpecimen.getStudies().add(studies.get(i));
                                    newSpecimen.setOrgan(sample.getOrgan());
                                    newSpecimen.setSubSamples(sample.getSubSamples());
                                    list.add(newSpecimen);
                                } else {
                                    foundSpecimen.getStudies().add(studies.get(i));
                                }
                            } else {
                                newSpecimen = new Specimen();
                                newSpecimen.setId(sample.getId());
                                newSpecimen.setCode(sample.getCode());
                                newSpecimen.setName(sample.getName());
                                newSpecimen.setContainer(sample.getContainer());
                                newSpecimen.setStudyType(studyType);
                                newSpecimen.getStudies().add(studies.get(i));
                                newSpecimen.setOrgan(sample.getOrgan());
                                newSpecimen.setSubSamples(sample.getSubSamples());
                                list.add(newSpecimen);
                            } 
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(OrderPathologyServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(OrderPathologyServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        if (errors.isEmpty())
        {
            return list;
        } else
        {
            throw new EnterpriseNTException(errors);   
        }
    }
    
    @Override
    public Specimen getSpecimenDataLis(Integer specimen) throws Exception
    {
        return dao.getSpecimenDataLis(specimen);
    }
}
