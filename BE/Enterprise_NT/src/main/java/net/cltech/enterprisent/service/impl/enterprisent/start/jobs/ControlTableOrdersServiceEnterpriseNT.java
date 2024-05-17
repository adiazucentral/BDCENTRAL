package net.cltech.enterprisent.service.impl.enterprisent.start.jobs;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.integration.MiddlewareDao;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage;
import net.cltech.enterprisent.domain.integration.middleware.SendAstmMiddleware;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.start.jobs.ControlTableOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Clase de implementación de servicios para reenvío de órdenes al Middleware
 *
 * @version 1.0.0
 * @author oarango
 * @since 27/08/2020
 * @see Creación
 */
@Service
public class ControlTableOrdersServiceEnterpriseNT implements ControlTableOrdersService
{

    @Autowired
    private MiddlewareDao middlewareDao;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;

    @Override
    public int[] sendOrders() throws Exception
    {
        List<SendAstmMiddleware> records = middlewareDao.getOrdersControlTable();
        if (!records.isEmpty())
        {
            List<MiddlewareMessage> messages = new LinkedList<>();
            String urlService = records.get(0).getRestServiceRoute();
            records.stream().map(record ->
            {
                Date date = new Date();
                long time = date.getTime();
                MiddlewareMessage middlewareMessage = new MiddlewareMessage();
                middlewareMessage.setMessage(record.getMessageASTM());
                middlewareMessage.setDate(Long.toString(time));
                return middlewareMessage;
            }).forEachOrdered(middlewareMessage ->
            {
                messages.add(middlewareMessage);
            });
            if (integrationMiddlewareService.sendMiddleware(messages, urlService))
            {
                return middlewareDao.deleteControlTableOrders(records);
            }
        }
        return null;
    }
}
