package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.billing.HomeboundBoxDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.HomeboundBox;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.service.interfaces.masters.billing.HomeboundBoxService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.JWT;

/**
 * Implementación de los servicios para la información del maestro caja
 * homebound
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
@Service
public class HomeboundBoxServiceEnterpriseNT implements HomeboundBoxService
{

    @Autowired
    private HomeboundBoxDao dao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderService orderService;

    @Override
    public List<HomeboundBox> get(long order) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            return dao.get(order, user.getId());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public HomeboundBox create(HomeboundBox homeboundBox) throws Exception
    {
        try
        {
            // Consultamos la orden para poder obtener la taqrifa de esta
            // Y asi poderla registrar junto con la caja
            AuthorizedUser user = JWT.decode(request);
            List<HomeboundBox> listCashBox = dao.get(homeboundBox.getOrder(), user.getId());
            if (listCashBox.isEmpty())
            {
                Order order = orderService.get(homeboundBox.getOrder());
                return dao.create(homeboundBox, order.getRate().getId());
            }
            else
            {
                homeboundBox.setId(listCashBox.get(0).getId());
                return dao.updateBox(homeboundBox);
            }
        }
        catch (Exception e)
        {
            System.out.println("e" + e);
            return null;
        }
    }
}
