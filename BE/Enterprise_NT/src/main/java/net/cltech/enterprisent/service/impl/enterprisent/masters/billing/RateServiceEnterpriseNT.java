package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.RateDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.service.interfaces.masters.billing.RateService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tarifas para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 14/06/2017
 * @see Creación
 */
@Service
public class RateServiceEnterpriseNT implements RateService
{

    @Autowired
    private RateDao dao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Rate> list() throws Exception
    {
        Integer idDefault = Integer.valueOf(daoConfig.get("TarifaPorDefecto").getValue());
        List<Rate> rates = dao.list();
        try
        {
            Rate rate = rates.get(rates.indexOf(new Rate(idDefault)));
            rate.setDefaultItem(true);
        } catch (IndexOutOfBoundsException ex)
        {
        }
        return rates;
    }

    @Override
    public Rate create(Rate rate) throws Exception
    {
        List<String> errors = validateFields(false, rate);
        if (errors.isEmpty())
        {
            Rate created = dao.create(rate);
            if (rate.isDefaultItem())
            {
                daoConfig.update(new Configuration("TarifaPorDefecto", created.getId().toString()));
            }
            trackingService.registerConfigurationTracking(null, created, Rate.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Rate get(Integer id, String code, String name) throws Exception
    {
        Integer idDefault = Integer.valueOf(daoConfig.get("TarifaPorDefecto").getValue());
        Rate rate = dao.get(id, code, name);
        if (rate != null)
        {
            rate.setDefaultItem(Objects.equals(rate.getId(), idDefault));
        }
        return rate;
    }

    @Override
    public Rate update(Rate rate) throws Exception
    {
        List<String> errors = validateFields(true, rate);
        if (errors.isEmpty())
        {
            Rate rateC = dao.get(rate.getId(), null, null);
            Rate modifited = dao.update(rate);
            if (rate.isDefaultItem())
            {
                daoConfig.update(new Configuration("TarifaPorDefecto", modifited.getId().toString()));
            }
            trackingService.registerConfigurationTracking(rateC, modifited, Rate.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Rate> list(boolean state) throws Exception
    {
        Integer idDefault = Integer.valueOf(daoConfig.get("TarifaPorDefecto").getValue());
        List<Rate> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Rate) o).isState() == state));
        try
        {
            Rate rate = filter.get(filter.indexOf(new Rate(idDefault)));
            rate.setDefaultItem(true);
        } catch (IndexOutOfBoundsException ex)
        {
        }
        return filter;
    }

    @Override
    public List<Rate> listPayers() throws Exception
    {
        Integer idDefault = Integer.valueOf(daoConfig.get("TarifaPorDefecto").getValue());
        List<Rate> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Rate) o).isState() == true && (((Rate) o).getTypePayer() == 1 || ((Rate) o).getTypePayer() == 2)));
        try
        {
            Rate rate = filter.get(filter.indexOf(new Rate(idDefault)));
            rate.setDefaultItem(true);
        } catch (IndexOutOfBoundsException ex)
        {
        }
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Rate rate) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (rate.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(rate.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (rate.getCode() != null && !rate.getCode().isEmpty())
        {
            Rate rateC = dao.get(null, rate.getCode(), null);
            if (rateC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(rate.getId(), rateC.getId()))
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

        if (rate.getName() != null && !rate.getName().isEmpty())
        {
            Rate rateC = dao.get(null, null, rate.getName());
            if (rateC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(rate.getId(), rateC.getId()))
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

        if (rate.getTypePayer() != null && rate.getTypePayer() != 0)
        {
            if (rate.getTypePayer() < 1 || rate.getTypePayer() > 5)
            {
                errors.add("3|type payer");
            }
        }

        if (rate.getClaimType() != null && rate.getClaimType() != 0)
        {
            if (rate.getClaimType() < 1 || rate.getClaimType() > 3)
            {
                errors.add("3|claim type");
            }
        }

        if (rate.getTransactionType() != null && rate.getTransactionType() != 0)
        {
            if (rate.getTransactionType() != 1 && rate.getTransactionType() != 2)
            {
                errors.add("3|transaction type");
            }
        }

        if (rate.getUser().getId() == null || rate.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
