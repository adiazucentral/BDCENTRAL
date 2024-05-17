/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleCaseteDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SamplePathologyDao;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.SamplePathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SamplePathologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de las muestras de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 12/04/2021
 * @see Creaciòn
 */
@Service
public class SamplePathologyServiceEnterpriseNT implements SamplePathologyService 
{
    
    @Autowired
    private SamplePathologyDao dao;
    @Autowired
    private SpecimenService specimenService;
    @Autowired
    private OrderPathologyService orderPathologyService;
    @Autowired
    private SampleCaseteDao sampleCaseteDao;
    
    
    @Override
    public List<Specimen> getByCase(Integer id) throws Exception
    {
        List<Specimen> samples = dao.getSpecimensByCase(id);
        Specimen specimenData;
        Specimen subSample;
        for(int i = 0; i < samples.size(); i++) {
            try
            {
                List<SamplePathology> dataSamples = dao.getSampleContent(samples.get(i).getSample());
                /*Casetes*/
                dataSamples.forEach( sample -> {
                    try {
                        sample.setCasetes( sampleCaseteDao.getById( sample.getId() ) );
                    } catch (Exception ex) {
                        Logger.getLogger(SamplePathologyServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
                /*Contenido de la muestra*/
                samples.get(i).setSamples(dataSamples);
                
                /*Estudios de la muestra*/
                samples.get(i).setStudies(specimenService.getStudiesBySample(samples.get(i).getSample()));
                
                /*Informacion de la muestra*/
                specimenData = orderPathologyService.getSpecimenDataLis(samples.get(i).getId());
                samples.get(i).setName(specimenData.getName());
                samples.get(i).setCode(specimenData.getCode());
                samples.get(i).setState(specimenData.isState());
                
                /*Datos de la submuestra*/
                subSample = orderPathologyService.getSpecimenDataLis(samples.get(i).getSubSample());
                samples.get(i).setSubSampleName(subSample.getName());

            } catch (Exception e)
            {
            }
        }
        return samples;
    }
}
