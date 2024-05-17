package net.cltech.enterprisent.service.impl.enterprisent.operation.reports;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.reports.ServicePrintDao;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import net.cltech.enterprisent.service.interfaces.operation.reports.ServicePrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de informes para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 20/06/2019
 * @see Creacion
 */
@Service
public class ServicePrintServiceEnterpriseNT implements ServicePrintService
{

    @Autowired
    private ServicePrintDao servicePrintDao;

    @Override
    public List<SerialPrint> list() throws Exception
    {
        return servicePrintDao.list();
    }

    @Override
    public SerialPrint getByService(int idBranch, int idService) throws Exception
    {
        return servicePrintDao.getByService(idBranch, idService);
    }

    @Override
    public void create(SerialPrint serialPrint) throws Exception
    {
        servicePrintDao.create(serialPrint);
    }

    @Override
    public int createAll(List<SerialPrint> list) throws Exception
    {
        return servicePrintDao.createAll(list);
    }

    @Override
    public void delete(SerialPrint serialPrint) throws Exception
    {
        servicePrintDao.delete(serialPrint);
    }

    @Override
    public void deleteAll() throws Exception
    {
        servicePrintDao.deleteAll();
    }

}
