/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.common;

import net.cltech.enterprisent.dao.interfaces.common.DatabaseInfoDao;
import net.cltech.enterprisent.domain.info.DatabaseInfo;
import net.cltech.enterprisent.service.interfaces.common.DatabaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de la base de datos para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author dcortes
 * @see 06/04/2017
 * @see Creaciòn
 */
@Service
public class DatabaseInfoServiceEnterpriseNT implements DatabaseInfoService
{

    @Autowired
    private DatabaseInfoDao dao;

    @Override
    public DatabaseInfo getInfo() throws Exception
    {
        return dao.getDatabaseInfo();
    }

}
