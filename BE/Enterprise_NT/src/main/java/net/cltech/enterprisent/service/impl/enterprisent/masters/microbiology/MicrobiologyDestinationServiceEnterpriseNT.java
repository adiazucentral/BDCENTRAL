package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.MicrobiologyDestinationDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.AnalyzerMicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicrobiologyDestinationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a destinos de microbiologia
 *
 * @author cmartin
 * @version 1.0.0
 * @since 14/02/2017
 * @see Creación
 */
@Service
public class MicrobiologyDestinationServiceEnterpriseNT implements MicrobiologyDestinationService
{

    @Autowired
    private MicrobiologyDestinationDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<MicrobiologyDestination> list() throws Exception
    {
        List<AnalyzerMicrobiologyDestination> listAnalyzers = new ArrayList<>();
        List<MicrobiologyDestination> listMicrobiology = dao.list();
        for (MicrobiologyDestination microbiologyDestination : listMicrobiology)
        {
            listAnalyzers = dao.getMicrobiologyAnalyzersDestinations(microbiologyDestination.getId());
            if (listAnalyzers != null)
            {
                microbiologyDestination.setAnalyzersMicrobiologyDestinations(listAnalyzers);
            }
        }
        return listMicrobiology;
    }

    @Override
    public MicrobiologyDestination create(MicrobiologyDestination destination) throws Exception
    {
        List<String> errors = validateFields(false, destination);
        if (errors.isEmpty())
        {
            MicrobiologyDestination created = dao.create(destination);
            // Le cargo a la lista de analizadores el id del objeto que se acabo de crear
            for (AnalyzerMicrobiologyDestination item : destination.getAnalyzersMicrobiologyDestinations())
            {
                item.setIdMicrobiologyDestination(created.getId());
            }
            List<AnalyzerMicrobiologyDestination> listOldAnalyzers = dao.getMicrobiologyAnalyzersDestinations(created.getId());
            // Elimino todo registro relacionado con ese destino en analizadores en destinos de microbiologia
            int rowsAffected = dao.deleteMicrobiologyAnalyzersDestinations(listOldAnalyzers);
            // Inserción de la nueva lista de usuarios para un destino
            dao.saveMicrobiologyAnalyzersDestinations(destination.getAnalyzersMicrobiologyDestinations());
            // Insercion en auditoria - analizadores en destinos de microbiologia
            trackingService.registerConfigurationTracking(null, destination, MicrobiologyDestination.class);
            return created;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public MicrobiologyDestination get(Integer id, String code, String name) throws Exception
    {
        try
        {
            MicrobiologyDestination microbiology = dao.get(id, code, name);
            List<AnalyzerMicrobiologyDestination> listAnalyzers = dao.getMicrobiologyAnalyzersDestinations(microbiology.getId());
            if (listAnalyzers != null)
            {
                microbiology.setAnalyzersMicrobiologyDestinations(listAnalyzers);
            }
            return microbiology;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public MicrobiologyDestination update(MicrobiologyDestination destination) throws Exception
    {
        List<String> errors = validateFields(true, destination);
        if (errors.isEmpty())
        {
            MicrobiologyDestination destinationC = get(destination.getId(), null, null);
            MicrobiologyDestination modifited = dao.update(destination);
            dao.deleteMicrobiologyAnalyzersDestinations(destination.getId());
            
            
            for (AnalyzerMicrobiologyDestination item : destination.getAnalyzersMicrobiologyDestinations())
            {
                item.setIdMicrobiologyDestination(destination.getId());
            }
            
            dao.saveMicrobiologyAnalyzersDestinations(destination.getAnalyzersMicrobiologyDestinations());
            trackingService.registerConfigurationTracking(destinationC, destination, MicrobiologyDestination.class);
            return modifited;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.deleteMicrobiologyAnalyzersDestinations(id);
        dao.delete(id);
    }

    @Override
    public List<MicrobiologyDestination> list(boolean state) throws Exception
    {
        List<AnalyzerMicrobiologyDestination> listAnalyzers = new ArrayList<>();
        List<MicrobiologyDestination> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((MicrobiologyDestination) o).isState() == state));
        for (MicrobiologyDestination microbiologyDestination : filter)
        {
            listAnalyzers = dao.getMicrobiologyAnalyzersDestinations(microbiologyDestination.getId());
            if (listAnalyzers != null)
            {
                microbiologyDestination.setAnalyzersMicrobiologyDestinations(listAnalyzers);
            }
        }
        return filter;
    }

    /**
     * Identifica los errores del objeto enviado
     *
     * @param isEdit Indica si se va actualizar la información
     * @param destination Destino de Microbiologia
     * @return Lista de errores
     * @throws Exception Error
     */
    private List<String> validateFields(boolean isEdit, MicrobiologyDestination destination) throws Exception
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
            }
            else
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
            MicrobiologyDestination destinationC = dao.get(null, destination.getCode(), null);
            if (destinationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(destination.getId(), destinationC.getId()))
                    {
                        errors.add("1|code");
                    }
                }
                else
                {
                    errors.add("1|code");
                }
            }
        }
        else
        {
            errors.add("0|code");
        }

        if (destination.getName() != null && !destination.getName().isEmpty())
        {
            MicrobiologyDestination destinationC = dao.get(null, null, destination.getName());
            if (destinationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(destination.getId(), destinationC.getId()))
                    {
                        errors.add("1|name");
                    }
                }
                else
                {
                    errors.add("1|name");
                }
            }
        }
        else
        {
            errors.add("0|name");
        }

        if (destination.getUser().getId() == null || destination.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
