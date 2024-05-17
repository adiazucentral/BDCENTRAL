package net.cltech.enterprisent.start.jobs;

import java.util.Calendar;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.start.StartAppDao;
import net.cltech.enterprisent.service.interfaces.operation.widgets.WidgetService;
import net.cltech.enterprisent.service.interfaces.start.StartAppService;
import org.springframework.beans.factory.annotation.Autowired;
import net.cltech.enterprisent.service.interfaces.start.jobs.ControlTableOrdersService;

public class JobTask
{

    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private WidgetService serviceWidget;
    @Autowired
    private ControlTableOrdersService controlTableOrdersService;
    @Autowired
    private StartAppDao daoStartApp;
    @Autowired
    public StartAppService service;

    public void expiredSample() throws Exception
    {
        System.out.println("Current Time job 1 : " + Calendar.getInstance().getTime());
        if (!Integer.valueOf(daoConfig.get("Trazabilidad").getValue()).equals(1))
        {
            serviceWidget.expiredWidget();
        }
    }

    public void newYear() throws Exception
    {
        System.out.println("Current NEW YEAR : " + Calendar.getInstance().getTime());
        daoStartApp.execStartScriptStatAgile();
    }

    public void sendOrdersToMiddleware() throws Exception
    {
        System.out.println("Job: Reenvío de órdenes al Middleware iniciado");
        int[] rows = controlTableOrdersService.sendOrders();
        if (rows == null)
        {
            System.out.println("Job: No habían registros a eliminar");
        } else if (rows.length > 0)
        {
            System.out.println("Job: Registros de órdenes enviadoas eliminados");
        }
        System.out.println("Job: Reenvío de órdenes al Middleware finalizado");
    }

    public void maintenanceDBDaily() throws Exception
    {
        System.out.println("Job: Test de mantenimiento diario para la db SQL Server");
        service.execStartScriptMaintenanceDBDaily();
    }
}
