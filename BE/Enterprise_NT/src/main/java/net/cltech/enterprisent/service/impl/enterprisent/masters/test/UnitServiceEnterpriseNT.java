package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.UnitDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Unit;
import net.cltech.enterprisent.service.interfaces.masters.test.UnitService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a las unidades de examenes
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 17/04/2017
 * @see Creación
 */
@Service
public class UnitServiceEnterpriseNT implements UnitService
{

    @Autowired
    private UnitDao unitDao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Unit create(Unit unit) throws Exception
    {
        List<String> errors = validateFields(unit, false);
        if (errors.isEmpty())
        {
            Unit newUnit = unitDao.create(unit);
            trackingService.registerConfigurationTracking(null, newUnit, Unit.class);
            return newUnit;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Unit> list() throws Exception
    {
        return unitDao.list();
    }

    @Override
    public Unit filterById(Integer id) throws Exception
    {
        return unitDao.findById(id);
    }

    @Override
    public Unit filterByName(String name) throws Exception
    {
        return unitDao.findByName(name);
    }

    @Override
    public Unit update(Unit unit) throws Exception
    {
        List<String> errors = validateFields(unit, true);
        if (errors.isEmpty())
        {
            Unit oldUnit = unitDao.findById(unit.getId());
            Unit updated = unitDao.update(unit);
            trackingService.registerConfigurationTracking(oldUnit, unit, Unit.class);
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
     * @param unit entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Unit unit, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (unit.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (unitDao.findById(unit.getId()) == null)//Si existe
                {
                    errors.add("2|id");
                }
            }
        }
        if (unit.getName() == null || unit.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            Unit unitFromDB = unitDao.findByName(unit.getName());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !unit.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|name");
                }
            }
        }
        if (unit.getUser().getId() == null)
        {
            errors.add("0|userId");
        }
        return errors;

    }

    @Override
    public List<Unit> filterByState(boolean state) throws Exception
    {
        List<Unit> filter = new ArrayList<>(CollectionUtils.filter(unitDao.list(), (Object o) -> ((Unit) o).isState() == state));
        return filter;
    }

}
