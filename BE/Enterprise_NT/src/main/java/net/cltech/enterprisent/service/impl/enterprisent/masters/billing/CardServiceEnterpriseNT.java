/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.CardDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Card;
import net.cltech.enterprisent.service.interfaces.masters.billing.CardService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tarjetas de Credito para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 07/06/2017
 * @see Creaciòn
 */
@Service
public class CardServiceEnterpriseNT implements CardService
{

    @Autowired
    private CardDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Card> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Card create(Card card) throws Exception
    {
        List<String> errors = validateFields(false, card);
        if (errors.isEmpty())
        {
            Card created = dao.create(card);
            trackingService.registerConfigurationTracking(null, created, Card.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Card get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Card update(Card card) throws Exception
    {
        List<String> errors = validateFields(true, card);
        if (errors.isEmpty())
        {
            Card cardC = dao.get(card.getId(), null);
            Card modifited = dao.update(card);
            trackingService.registerConfigurationTracking(cardC, modifited, Card.class);
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
    public List<Card> list(boolean state) throws Exception
    {
        List<Card> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Card) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Card card) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (card.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(card.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (card.getName() != null && !card.getName().isEmpty())
        {
            Card cardC = dao.get(null, card.getName());
            if (cardC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(card.getId(), cardC.getId()))
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

        if (card.getUser().getId() == null || card.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
