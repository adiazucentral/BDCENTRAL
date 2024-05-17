package net.cltech.enterprisent.service.impl.enterprisent.masters.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.common.MotiveDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.service.interfaces.masters.common.MotiveService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Motivos para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 06/06/2017
 * @see Creaciòn
 */
@Service
public class MotiveServiceEnterpriseNT implements MotiveService
{

    @Autowired
    private MotiveDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Motive> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Motive create(Motive motive) throws Exception
    {
        List<String> errors = validateFields(false, motive);
        if (errors.isEmpty())
        {
            Motive created = dao.create(motive);
            trackingService.registerConfigurationTracking(null, created, Motive.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Motive get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Motive update(Motive motive) throws Exception
    {
        List<String> errors = validateFields(true, motive);
        if (errors.isEmpty())
        {
            Motive motiveC = dao.get(motive.getId(), null);
            
            Motive modifited = dao.update(motive);
            if (motiveC!=null){
             trackingService.registerConfigurationTracking(motiveC, modifited, Motive.class);
            
            }
           
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
    public List<Motive> list(boolean state) throws Exception
    {
        List<Motive> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Motive) o).isState() == state));
        return filter;
    }
    
    @Override
    public List<Motive> listMotivePathology(boolean state) throws Exception
    {
        List<Motive> filter = new ArrayList<>(CollectionUtils.filter(dao.listMotivePathology(), (Object o) -> ((Motive) o).isState() == state));
        return filter;
    }

    @Override
    public List<Motive> listMotivePendingTest() throws Exception
    {

        List<Motive> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Motive) o).getType().getId() == 30));

        return filter;
    }

    private List<String> validateFields(boolean isEdit, Motive motive) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (motive.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(motive.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (motive.getName() != null && !motive.getName().isEmpty())
        {
            Motive motiveC = dao.get(null, motive.getName());
            if (motiveC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(motive.getId(), motiveC.getId()))
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

        if (motive.getDescription() == null || motive.getDescription().isEmpty())
        {
            errors.add("0|description");
        }

        if (motive.getType().getId() != null && motive.getType().getId() != 0 )
        {
            if ( (motive.getType().getId() < 15 || motive.getType().getId() > 31) && motive.getType().getId() !=  63 && motive.getType().getId() !=  64 && motive.getType().getId() !=  66 && motive.getType().getId() !=  67)
            {
                errors.add("3|type");
            }
        } else
        {
            errors.add("0|type");
        }

        if (motive.getUser().getId() == null || motive.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
    
    /**
     * Obtener lista de motivos por el tipo de motivo 
     *
     * @param type
     * 
     * @return Lista de motivos
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<Motive> getListMotivesByType(Integer type) throws Exception
    {
        return dao.getListMotivesByType(type);
    }
}
