package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.ServiceDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a servicio
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 11/05/2017
 * @see Creación
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/09/2017
 * @see Se agrega actualizacion de los contadores de ordenes
 */
@Service
public class ServiceServiceEnterpriseNT implements ServiceService
{

    @Autowired
    private ServiceDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ToolsDao toolsDao;

    @Override
    public ServiceLaboratory create(ServiceLaboratory create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            ServiceLaboratory newBean = dao.create(create);
            updateCounters(newBean);
            trackingService.registerConfigurationTracking(null, newBean, ServiceLaboratory.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<ServiceLaboratory> list() throws Exception
    {
        return dao.list();
    }
    
    @Override
    public List<ServiceLaboratory> listHospitalary() throws Exception
    {
        List<ServiceLaboratory> servicelaboratory = new ArrayList<>();
        servicelaboratory = dao.list();
        
        servicelaboratory =  servicelaboratory.stream()
                                              .filter(c -> c.getHospitalSampling() == true)
                                              .collect(Collectors.toList());
        return servicelaboratory;
    }
    
    

    @Override
    public List<ServiceLaboratory> list(boolean state) throws Exception
    {
        return dao.list().stream().filter(service -> service.isState() == state).collect(Collectors.toList());
    }

    @Override
    public ServiceLaboratory filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public ServiceLaboratory filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public ServiceLaboratory update(ServiceLaboratory update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            ServiceLaboratory old = dao.filterById(update.getId());
            ServiceLaboratory updated = dao.update(update);
            updateCounters(update);
            trackingService.registerConfigurationTracking(old, update, ServiceLaboratory.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(ServiceLaboratory validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");
            } else
            {
                if (dao.filterById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
        }
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            ServiceLaboratory found = dao.filterByName(validate.getName());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }
        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else
        {
            boolean found = dao.list().stream()
                    .filter(s -> s.getCode().equals(validate.getCode()))
                    .filter(s -> !isEdit || (isEdit && !s.getId().equals(validate.getId())))
                    .findAny()
                    .isPresent();
            if (found)
            {
                errors.add("1|code");//duplicado
            }
        }
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        return errors;

    }

    @Override
    public List<ServiceLaboratory> filterByState(boolean state) throws Exception
    {
        List<ServiceLaboratory> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((ServiceLaboratory) o).isState() == state));
        return filter;
    }

    /**
     * Actualiza los contadores si la numeracion es por servicio
     *
     * @param service
     * {@link net.cltech.enterprisent.domain.masters.demographic.Branch}
     * @throws Exception Error presentado en el servicio
     */
    private void updateCounters(ServiceLaboratory service) throws Exception
    {
        String branchNumbering = configurationService.getValue("TipoNumeroOrden");
        if (branchNumbering.equals("Servicio"))
        {
            toolsDao.createSequence(Constants.SEQUENCE + service.getId(), service.getMin(), 1, service.getMax());
        }
    }
    
    /**
     * Obtiene servicio por su id, y sistema central solo si este 
     * servicio se encuentra homologado retornara el mismo, de lo contrario sera null
     *
     * @param idService
     * @param centralSystem
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public ServiceLaboratory filterHomoligationById(int idService, int centralSystem) throws Exception
    {
        try
        {
            ServiceLaboratory serviceLaboratory = dao.filterHomoligationById(idService, centralSystem);
            return serviceLaboratory;
        } catch (Exception e)
        {
            return null;
        }
    }
}
