package net.cltech.enterprisent.service.impl.enterprisent.masters.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.DestinationDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.service.interfaces.masters.tracking.DestinationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a destinos
 *
 * @author cmartin
 * @version 1.0.0
 * @since 27/07/2017
 * @see Creación
 */
@Service
public class DestinationServiceEnterpriseNT implements DestinationService
{

    @Autowired
    private DestinationDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Destination> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Destination create(Destination destination) throws Exception
    {
        List<String> errors = validateFields(false, destination);
        if (errors.isEmpty())
        {
            Destination created = dao.create(destination);
            trackingService.registerConfigurationTracking(null, created, Destination.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Destination get(Integer id, String code, String name) throws Exception
    {
        return dao.get(id, code, name);
    }

    @Override
    public Destination update(Destination destination) throws Exception
    {
        List<String> errors = validateFields(true, destination);
        if (errors.isEmpty())
        {
            Destination destinationC = dao.get(destination.getId(), null, null);
            Destination modifited = dao.update(destination);
            trackingService.registerConfigurationTracking(destinationC, modifited, Destination.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Destination> list(boolean state) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Destination) o).isState() == state));
    }

    @Override
    public AssignmentDestination getRoute(Integer idBranch, Integer idSample, Integer idOrderType, boolean assignment) throws Exception
    {
        return dao.getAssignment(idBranch, idSample, idOrderType, assignment);
    }

    @Override
    public AssignmentDestination createAssignment(AssignmentDestination assignment) throws Exception
    {
        AssignmentDestination assigment = dao.getAssignment(assignment.getBranch().getId(), assignment.getSample().getId(), assignment.getOrderType().getId(), true);
        assigment.getDestinationRoutes().forEach((destination) ->
        {
            destination.setTests(destination.getTests().stream().filter(test -> test.isSelected() == true).collect(Collectors.toList()));
        });
        if (!assignment.getDestinationRoutes().equals(assigment.getDestinationRoutes()))
        {
            if (assigment.getId() != null)
            {
                dao.deleteAssigment(assigment.getId());
            }
            AssignmentDestination resp = dao.createAssignment(assignment);
            trackingService.registerConfigurationTracking(assigment, resp, AssignmentDestination.class);
            return resp;
        }
        return assignment;
    }

    @Override
    public int createSampleOportunity(List<DestinationRoute> destinations) throws Exception
    {
        Integer insert = dao.createSampleOportunity(destinations);
        trackingService.registerConfigurationTracking(null, destinations, DestinationRoute.class);
        return insert;
    }

    private List<String> validateFields(boolean isEdit, Destination destination) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (destination.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(destination.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (destination.getCode() != null && !destination.getCode().isEmpty())
        {
            Destination destinationC = dao.get(null, destination.getCode(), null);
            if (destinationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(destination.getId(), destinationC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if (destination.getName() != null && !destination.getName().isEmpty())
        {
            Destination destinationC = dao.get(null, null, destination.getName());
            if (destinationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(destination.getId(), destinationC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (destination.getType() != null && destination.getType().getId() != 0)
        {
            if (destination.getType().getId() < 44 || destination.getType().getId() > 48)
            {
                errors.add("3|type");
            }
        } else
        {
            errors.add("0|type");
        }

        if (destination.getUser().getId() == null || destination.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
