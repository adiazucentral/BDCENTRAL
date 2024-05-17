/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.impl.enterprisent.exception;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.cltech.outreach.dao.interfaces.exception.ExceptionDao;
import net.cltech.outreach.domain.exception.WebException;
import net.cltech.outreach.service.interfaces.exception.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de errores para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Creacion
 */
@Service
public class ExceptionServiceEnterpriseNT implements ExceptionService
{

    @Autowired
    private ExceptionDao dao;

    @Override
    public WebException save(WebException exception) throws Exception
    {
        //No se registran errores controlados
        if (exception.getCode() != 2)
        {
            exception = dao.insert(exception);
        }
        return exception;
    }

    @Override
    public List<WebException> get(int initialDate, int finalDate) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date i = sdf.parse(String.valueOf(initialDate) + " 00:00:00");
        Date f = sdf.parse(String.valueOf(finalDate) + " 23:59:59");
        return dao.get(new Timestamp(i.getTime()), new Timestamp(f.getTime()));
    }

    @Override
    public List<WebException> get(int initialDate, int finalDate, int type) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date i = sdf.parse(String.valueOf(initialDate) + " 00:00:00");
        Date f = sdf.parse(String.valueOf(finalDate) + " 23:59:59");
        return dao.get(new Timestamp(i.getTime()), new Timestamp(f.getTime()), type);
    }
}
