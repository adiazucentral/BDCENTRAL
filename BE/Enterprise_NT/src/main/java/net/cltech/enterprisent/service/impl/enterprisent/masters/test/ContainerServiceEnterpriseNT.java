package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.test.ContainerDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.service.interfaces.masters.test.ContainerService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.JWT;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Recipientes para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 12/04/2017
 * @see Creaciòn
 */
@Service
public class ContainerServiceEnterpriseNT implements ContainerService {

    @Autowired
    private ContainerDao containerDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public List<Container> list() throws Exception {
        return containerDao.list();
    }

    @Override
    public Container create(Container container) throws Exception {
        List<String> errors = validateFields(false, container);
        if (errors.isEmpty()) {
            trackingService.registerConfigurationTracking(null, container, Container.class);
            return containerDao.create(container);
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Container get(Integer id, String name, Integer priority) throws Exception {
        return containerDao.get(id, name, priority);
    }

    @Override
    public Container update(Container container) throws Exception {
        List<String> errors = validateFields(true, container);
        if (errors.isEmpty()) {
            Container containerval = containerDao.get(container.getId(), null, null);
            trackingService.registerConfigurationTracking(containerval, container, Container.class);
            return containerDao.update(container);
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) {

    }

    private List<String> validateFields(boolean isEdit, Container container) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        //si Esta editando
        if (isEdit) {
            if (container.getId() == null) {
                errors.add("0|id");
                return errors;
            } else {
                if (containerDao.get(container.getId(), null, null) == null) {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        // validamos campos obligatorios , y que no pueden repetirse 
        if (container.getName() != null && !container.getName().isEmpty()) {
            Container contanerival = containerDao.get(null, container.getName(), null);

            if (contanerival != null) {
                if (isEdit) {
                    //comparamos que corresponda al mismo ID
                    if (container.getId() != contanerival.getId()) {
                        errors.add("1|Name");
                    }
                } else {
                    //guardar
                    errors.add("1|Name");
                }
            }

        } else {
            errors.add("0|Name");
        }

        if (container.getPriority() != null && container.getPriority() > 0) {
//            Container contanerival = containerDao.get(null, null, container.getPriority());
//            
//            if (contanerival != null)
//            {
//                if (isEdit)
//                {
//                    //comparamos que corresponda al mismo ID
//                    if (container.getId() != contanerival.getId())
//                    {
//                        errors.add("1|Priority");
//                    }
//                } else
//                {
//                    //guardar
//                    errors.add("1|Priority");
//                }
//            }
        } else {
            errors.add("0|Priority");
        }
        return errors;
    }

    @Override
    public List<Container> list(boolean state) throws Exception {
        List<Container> filter = new ArrayList<>(CollectionUtils.filter(containerDao.list(), (Object o) -> ((Container) o).isState() == state));
        return filter;
    }

    @Override
    public void updatePriority(List<Container> containers) throws Exception {
        final AuthorizedUser user = JWT.decode(request);
        containers.stream().forEach((Container c)
                -> {
            try {
                c.setUser(user);
                Container containerval = containerDao.get(c.getId(), null, null);
                trackingService.registerConfigurationTracking(containerval, c, Container.class);
                containerDao.updatePriority(c);
            } catch (Exception ex) {
                Logger.getLogger(ContainerServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }



}
