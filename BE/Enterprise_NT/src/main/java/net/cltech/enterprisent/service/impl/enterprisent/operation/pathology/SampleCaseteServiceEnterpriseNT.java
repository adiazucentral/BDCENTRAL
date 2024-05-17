/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.CaseDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleCaseteDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.domain.operation.pathology.SampleCasete;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenProtocolService;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SampleCaseteService;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de los casetes de las muestras de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 05/05/2021
 * @see Creaciòn
 */
@Service
public class SampleCaseteServiceEnterpriseNT implements SampleCaseteService
{
    @Autowired
    private SampleCaseteDao dao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SpecimenService specimenService;
    @Autowired
    private OrderPathologyService orderPathologyService;
    @Autowired
    private SpecimenProtocolService specimenProtocolService;
    @Autowired
    private CaseDao caseDao;
    
    @Override
    public List<SampleCasete> getBySample(int idSample, int idCase) throws Exception
    {
        return dao.getBySample(idSample, idCase);
    }
    
    @Override
    public List<SampleCasete> create(List<SampleCasete> samples) throws Exception 
    {

        List<String> errors = validateFields(samples);
        if (errors.isEmpty()) {
            dao.create(samples);   
            return samples;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<SampleCasete> getCasetesByFilterCases(FilterPathology filter) throws Exception
    {
        List<SampleCasete> casetes = dao.getCasetesByFilterCases(filter, JWT.decode(request).getBranch());
        casetes.forEach( casete -> {
            try
            {
                /*Estudios de la muestra*/
                casete.getSpecimen().setStudies(specimenService.getStudiesBySample(casete.getSpecimen().getSample()));
                /*Informacion de la muestra*/
                Specimen specimenData = orderPathologyService.getSpecimenDataLis(casete.getSpecimen().getId());
                casete.getSpecimen().setName(specimenData.getName());
                casete.getSpecimen().setCode(specimenData.getCode());
                casete.getSpecimen().setState(specimenData.isState());
                /*Protocolo*/
                casete.setProtocol(specimenProtocolService.get(null, casete.getSpecimen().getId()));
                /*Informacion del paciente*/
                caseDao.getDataPatient(casete.getSpecimen().getCasePat(), casete.getSpecimen().getCasePat().getOrder().getNumberOrder());
                
            } catch (Exception e)
            {
            }
        });
        return casetes;
    }
    
    private List<String> validateFields(List<SampleCasete> samples) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        
        if (samples.size() > 0)
        {
            int row = 0;
            for (SampleCasete sample : samples)
            {
                if(sample.getSample() == null || sample.getSample() == 0) {
                    errors.add("0|sample|" + row);
                }
                
                if(sample.getQuantity() == null || sample.getQuantity() == 0) {
                    errors.add("0|quantity|" + row);
                }
                
                if(sample.getConsecutive().isEmpty()) {
                    errors.add("0|consecutive|" + row);
                }
                
                if(sample.getCasete().getId() == null || sample.getCasete().getId() == 0) {
                    errors.add("0|casete|" + row);
                }
                row++;
            }
        } else
        {
            errors.add("0|specimens");
        }
        return errors;
    }
    
    @Override
    public List<SampleCasete> changeStatus(List<SampleCasete> list) throws Exception 
    {
        list.forEach( casete -> {
            try {
                dao.changeCaseteStatus(casete.getCasete().getId(), casete.getStatus());
            } catch (Exception ex) {
                Logger.getLogger(SampleCaseteServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return list;
    }
}
