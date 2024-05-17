package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.billing.DiscountRateDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import net.cltech.enterprisent.service.interfaces.masters.billing.DiscountRateService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro de los tipos de
 * descuentos para Enterprise NT
 *
 * @version 1.0.0
 * @author javila
 * @since 23/03/2021
 * @see Creación
 */
@Service
public class DiscountRateServiceEnterpriseNT implements DiscountRateService
{

    @Autowired
    private DiscountRateDao discountRateDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private HttpServletRequest request;
    
    @Override
    public DiscountRate create(DiscountRate discountRate) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        discountRate.setIdUserCreating(user.getId());
        List<String> errors = validateFields(false, discountRate);
        if (errors.isEmpty())
        {
            DiscountRate created = discountRateDao.create(discountRate);
            trackingService.registerConfigurationTracking(null, created, DiscountRate.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public DiscountRate update(DiscountRate discountRate) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        discountRate.setModifyingUserId(user.getId());
        List<String> errors = validateFields(true, discountRate);
        if (errors.isEmpty())
        {
            DiscountRate discountRateC = discountRateDao.get(discountRate.getId(), null, null);
            DiscountRate modifited = discountRateDao.update(discountRate);
            trackingService.registerConfigurationTracking(discountRateC, modifited, DiscountRate.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<DiscountRate> list() throws Exception
    {
        return discountRateDao.list();
    }

    @Override
    public DiscountRate get(Integer id, String code, String name) throws Exception
    {
        return discountRateDao.get(id, code, name);
    }

    private List<String> validateFields(boolean isEdit, DiscountRate discountRate) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (discountRate.getId() == null)
            {
                errors.add("0|id");
                return errors;
            }
            else
            {
                if (discountRateDao.get(discountRate.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        // Validación de duplicidad del codigo
        if (discountRate.getCode() != null && !discountRate.getCode().isEmpty())
        {
            DiscountRate discountRateC = discountRateDao.get(null, discountRate.getCode(), null);
            if (discountRateC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(discountRate.getId(), discountRateC.getId()))
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

        // Validación de duplicidad del nombre
        if (discountRate.getName() != null && !discountRate.getName().isEmpty())
        {
            DiscountRate discountRateC = discountRateDao.get(null, null, discountRate.getName());
            if (discountRateC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(discountRate.getId(), discountRateC.getId()))
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

        // Validación del dato de porcentaje
        if (discountRate.getPercentage() == null)
        {
            errors.add("0|percentage");
        }

        if (!isEdit)
        {
            if (discountRate.getIdUserCreating() == null || discountRate.getIdUserCreating() == 0)
            {
                errors.add("0|user creation");
            }
        }
        
        if(isEdit)
        {
            if(discountRate.getModifyingUserId() == null || discountRate.getModifyingUserId() == 0)
            {
                errors.add("0|user who modifies");
            }
        }

        return errors;
    }
}
