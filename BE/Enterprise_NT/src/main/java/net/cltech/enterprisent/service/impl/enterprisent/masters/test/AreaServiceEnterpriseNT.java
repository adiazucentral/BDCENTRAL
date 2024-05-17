/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.test.AreaDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.service.interfaces.masters.test.AreaService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Áreas para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 12/04/2017
 * @see Creaciòn
 */
@Service
public class AreaServiceEnterpriseNT implements AreaService
{

    @Autowired
    private AreaDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Area> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Area create(Area area) throws Exception
    {
        List<String> errors = validateFields(false, area);
        if (errors.isEmpty())
        {
            Area created = dao.create(area);
            trackingService.registerConfigurationTracking(null, created, Area.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Area get(Integer id, Integer ordering, String name, String abbr) throws Exception
    {
        return dao.get(id, ordering, name, abbr);
    }

    @Override
    public Area update(Area area) throws Exception
    {
        List<String> errors = validateFields(true, area);
        if (errors.isEmpty())
        {
            Area areaC = dao.get(area.getId(), null, null, null);
            Area modifited = dao.update(area);
            trackingService.registerConfigurationTracking(areaC, modifited, Area.class);
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
    public List<Area> list(boolean state) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Area) o).isState() == state));
    }

    private List<String> validateFields(boolean isEdit, Area area) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (area.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (area.getOrdering() == 0)
                {
                    errors.add("6|seccion cero");
                    return errors;
                } else
                {
                    if (dao.get(area.getId(), null, null, null) == null)
                    {
                        errors.add("2|id");
                        return errors;
                    }
                }
            }
        }

        if (area.getOrdering() != null)
        {
            Area areaC = dao.get(null, Integer.valueOf(area.getOrdering()), null, null);
            if (areaC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(area.getId(), areaC.getId()))
                    {
                        errors.add("1|ordering");
                    }
                } else
                {
                    errors.add("1|ordering");
                }
            }
        } else
        {
            errors.add("0|ordering");
        }

        if (area.getAbbreviation() != null && !area.getAbbreviation().isEmpty())
        {
            Area areaC = dao.get(null, null, null, area.getAbbreviation());
            if (areaC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(area.getId(), areaC.getId()))
                    {
                        errors.add("1|abbreviation");
                    }
                } else
                {
                    errors.add("1|abbreviation");
                }
            }
        } else
        {
            errors.add("0|abbreviation");
        }

        if (area.getName() != null && !area.getName().isEmpty())
        {
            Area areaC = dao.get(null, null, area.getName(), null);
            if (areaC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(area.getId(), areaC.getId()))
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

        if (area.getColor() == null || area.getColor().isEmpty())
        {
            errors.add("0|color");
        }

        if (area.getType().getId() != null && area.getType().getId() != 0)
        {
            if (area.getType().getId() != 2 && area.getType().getId() != 3 && area.getType().getId() != 4 && area.getType().getId() != 5)
            {
                errors.add("3|type");
            }
        } else
        {
            errors.add("0|type");
        }

        if (area.getUser().getId() == null || area.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
