package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.AnatomicalSiteDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.AnatomicalSiteService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Sitio Anatomico para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 08/06/2017
 * @see Creaciòn
 */
@Service
public class AnatomicalSiteServiceEnterpriseNT implements AnatomicalSiteService
{

    @Autowired
    private AnatomicalSiteDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<AnatomicalSite> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public AnatomicalSite create(AnatomicalSite anatomicalSite) throws Exception
    {
        List<String> errors = validateFields(false, anatomicalSite);
        if (errors.isEmpty())
        {
            AnatomicalSite created = dao.create(anatomicalSite);
            trackingService.registerConfigurationTracking(null, created, AnatomicalSite.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public AnatomicalSite get(Integer id, String name, String abbr) throws Exception
    {
        return dao.get(id, name, abbr);
    }

    @Override
    public AnatomicalSite update(AnatomicalSite anatomicalSite) throws Exception
    {
        List<String> errors = validateFields(true, anatomicalSite);
        if (errors.isEmpty())
        {
            AnatomicalSite anatomicalSiteC = dao.get(anatomicalSite.getId(), null, null);
            AnatomicalSite modifited = dao.update(anatomicalSite);
            trackingService.registerConfigurationTracking(anatomicalSiteC, modifited, AnatomicalSite.class);
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
    public List<AnatomicalSite> list(boolean state) throws Exception
    {
        List<AnatomicalSite> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((AnatomicalSite) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, AnatomicalSite anatomicalSite) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (anatomicalSite.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(anatomicalSite.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (anatomicalSite.getName() != null && !anatomicalSite.getName().isEmpty())
        {
            AnatomicalSite anatomicalSiteC = dao.get(null, anatomicalSite.getName(), null);
            if (anatomicalSiteC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(anatomicalSite.getId(), anatomicalSiteC.getId()))
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

        if (anatomicalSite.getAbbr() != null && !anatomicalSite.getAbbr().isEmpty())
        {
            AnatomicalSite anatomicalSiteC = dao.get(null, null, anatomicalSite.getAbbr());
            if (anatomicalSiteC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(anatomicalSite.getId(), anatomicalSiteC.getId()))
                    {
                        errors.add("1|abbr");
                    }
                } else
                {
                    errors.add("1|abbr");
                }
            }
        } else
        {
            errors.add("0|abbr");
        }

        if (anatomicalSite.getUser().getId() == null || anatomicalSite.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
