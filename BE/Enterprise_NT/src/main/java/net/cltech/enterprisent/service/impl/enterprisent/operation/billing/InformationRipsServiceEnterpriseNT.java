/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.billing;

import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InformationRipsDao;
import net.cltech.enterprisent.domain.operation.billing.InformationRips;
import net.cltech.enterprisent.domain.operation.billing.RipsFilter;
import net.cltech.enterprisent.service.interfaces.operation.billing.InformationRipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.RipsDao;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;

/**
 * Implementacion de información rips para Enterprise NT
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creacion
 */
@Service
public class InformationRipsServiceEnterpriseNT implements InformationRipsService
{
    @Autowired
    private InformationRipsDao dao;
    @Autowired
    private RipsDao ripsDao;
    
    @Override
    public List<InformationRips> information(RipsFilter search) throws Exception
    {        
        
        List<RIPS> rips = ripsDao.getDemographic();
        RIPS centralSystem = ripsDao.get("CodigosPruebas");
        List<InformationRips> informationRips = dao.list(search, rips, centralSystem);           
        return informationRips.stream().filter( info -> info.getTests().size() > 0 ).collect(Collectors.toList());
    }
}
