package net.cltech.enterprisent.service.impl.enterprisent.masters.configuration;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.OrderGroupingDao;
import net.cltech.enterprisent.domain.masters.configuration.OrderGrouping;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.OrderGroupingService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a agrupacion de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/03/2019
 * @see Creación
 */
@Service
public class OrderGroupingServiceEnterpriseNT implements OrderGroupingService
{

    @Autowired
    private OrderGroupingDao orderGroupingDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private TrackingService trackingService;

    private static final String ORDER_GROUPING = "AgrupacionOrdenes";

    @Override
    public List<OrderGrouping> list() throws Exception
    {
        List<OrderGrouping> list = new ArrayList<>(0);
        if (configurationService.get(ORDER_GROUPING).getValue().equals(Constants.ORDER_GROUPING_SERVICE))
        {
            list = orderGroupingDao.listByService();
        } else
        {
            list = orderGroupingDao.listByOrderType();
        }
        return list;
    }

    @Override
    public Integer insertGroups(List<OrderGrouping> inserts) throws Exception
    {
        String groupType = configurationService.get(ORDER_GROUPING).getValue();
        if (groupType != null)
        {
            List<OrderGrouping> previous = list();
            orderGroupingDao.deleteGroups();
            trackingService.registerConfigurationTracking(previous, inserts, OrderGrouping.class);
            return orderGroupingDao.insertGroups(inserts, groupType);
        } else
        {
            return 0;
        }
    }

    @Override
    public void deleteGroups() throws Exception
    {
        orderGroupingDao.deleteGroups();
    }

}
