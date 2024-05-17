/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import net.cltech.enterprisent.dao.interfaces.masters.test.TestAlarmDao;
import net.cltech.enterprisent.domain.masters.test.TestAlarm;
import net.cltech.enterprisent.service.interfaces.masters.test.TestAlarmService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Alerta de Pruebas desde ingreso de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/12/2021
 * @see Creaciòn
 */
@Service
public class TestAlarmServiceEnterpriseNT implements TestAlarmService {
    
    @Autowired
    private TestAlarmDao dao;
    @Autowired
    private TrackingService trackingService;
    
    @Override
    public TestAlarm create(TestAlarm testAlarm) throws Exception
    {
        dao.createTestAlarm(testAlarm);
        trackingService.registerConfigurationTracking(null, testAlarm, TestAlarm.class);
        return testAlarm;
    }
    
}
