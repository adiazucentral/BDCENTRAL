/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.common.MotiveDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleRejectionDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.pathology.SampleRejection;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SampleRejectionService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de las muestras rechazadas de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/02/2020
 * @see Creaciòn
 */
@Service
public class SampleRejectionServiceEnterpriseNT implements SampleRejectionService
{
    @Autowired
    private SampleRejectionDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    @Autowired
    private MotiveDao motiveDao;
    @Autowired
    private OrderListService orderListService;
    
    @Override
    public List<SampleRejection> listByFilters(Filter filter) throws Exception
    {
        filter.setBasic(true);
        List<OrderList> orders = orderListService.listFilters(filter);
        List<SampleRejection> rejectList = dao.list();
        
        rejectList = rejectList.stream().filter( reject ->
                orders.stream().map(OrderList::getOrderNumber).anyMatch( order -> order.equals(reject.getOrderNumber())))
                .collect(Collectors.toList());
        
        if(rejectList.size() > 0) {
            rejectList.forEach( reject -> {
                try {
                    reject.setUserCreated(trackingPathologyDao.get(reject.getUserCreated().getId()));
                    reject.setUserUpdated(trackingPathologyDao.get(reject.getUserUpdated().getId()));
                    List<Motive> motives = new ArrayList<>(CollectionUtils.filter(motiveDao.listMotivePathology(), (Object o) -> ((Motive) o).isState() == true));
                    reject.setMotive(motives.stream().filter(motive -> Objects.equals(motive.getId(), reject.getMotive().getId())).findFirst().orElse(null)); 
                    OrderList orderPatient = orders.stream().filter( order -> order.getOrderNumber().equals( reject.getOrderNumber())).findAny().orElse(null);
                    if(orderPatient != null) {
                        reject.setPatientId(orderPatient.getPatient().getPatientId());
                        reject.setSurName(orderPatient.getPatient().getSurName());
                        reject.setLastName(orderPatient.getPatient().getLastName());
                        reject.setName1(orderPatient.getPatient().getName1());
                        reject.setName2(orderPatient.getPatient().getName2());
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SampleRejectionServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        return rejectList;
    }
    
    @Override
    public SampleRejection get(Integer studyType, Long order) throws Exception
    {
        SampleRejection reject = dao.get(studyType, order);
        if( reject != null ) {
            
            reject.setUserCreated(trackingPathologyDao.get(reject.getUserCreated().getId()));
            reject.setUserUpdated(trackingPathologyDao.get(reject.getUserUpdated().getId()));
            List<Motive> motives = new ArrayList<>(CollectionUtils.filter(motiveDao.listMotivePathology(), (Object o) -> ((Motive) o).isState() == true));
            reject.setMotive(motives.stream().filter(motive -> motive.getId() == reject.getMotive().getId()).findFirst().orElse(null)); 
        }
        return reject;
    }
    
    @Override
    public SampleRejection create(SampleRejection rejection) throws Exception {

        List<String> errors = validateFields(false, rejection);
        if (errors.isEmpty()) {
            SampleRejection created = dao.create(rejection);
            trackingService.registerConfigurationTracking(null, created, SampleRejection.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<SampleRejection> activeSamples(List<SampleRejection> rejectList) throws Exception
    {
        return dao.activeSamples(rejectList);
    }
    
    private List<String> validateFields(boolean isEdit, SampleRejection rejection) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (rejection.getId() == null)
            {
                errors.add("0|id");
                return errors;
            }
            
            if (rejection.getUserUpdated().getId() == null || rejection.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (rejection.getUserCreated().getId() == null || rejection.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }
        
        if(rejection.getStudyType().getId() == null || rejection.getStudyType().getId() == 0) 
        {
           errors.add("0|studyType");
        }
        
        if(rejection.getMotive().getId() == null || rejection.getMotive().getId() == 0) 
        {
           errors.add("0|motive");
        }
        return errors;
    }
}
