/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.CommentCodedDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.CommentCoded;
import net.cltech.enterprisent.service.interfaces.masters.test.CommentCodedService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro de Comentarios para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 15/05/2017
 * @see Creaciòn
 */
@Service
public class CommentCodedServiceEnterpriseNT implements CommentCodedService
{

    @Autowired
    private CommentCodedDao commentDao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<CommentCoded> list() throws Exception
    {
        return commentDao.list();
    }

    @Override
    public CommentCoded create(CommentCoded comment) throws Exception
    {
        List<String> errors = validateFields(false, comment);
        if (errors.isEmpty())
        {
            trackingService.registerConfigurationTracking(null, comment, CommentCoded.class);
            return commentDao.create(comment);
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public List<CommentCoded> get(Integer id, String code, Integer apply, Boolean state) throws Exception
    {
        return commentDao.get(id, code, apply, state);
    }

    @Override
    public CommentCoded update(CommentCoded comment) throws Exception
    {
        List<String> errors = validateFields(true, comment);
        if (errors.isEmpty())
        {
            CommentCoded commentval = commentDao.get(comment.getId(), null, null, null).get(0);
            trackingService.registerConfigurationTracking(commentval, comment, CommentCoded.class);
            return commentDao.update(comment);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateFields(boolean isEdit, CommentCoded comment) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        //si Esta editando
        if (isEdit)
        {
            if (comment.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (commentDao.get(comment.getId(), null, null, null).isEmpty())
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        // validamos campos obligatorios , y que no pueden repetirse 
        if (comment.getCode() != null && !comment.getCode().isEmpty())
        {
            List<CommentCoded> list = commentDao.get(null, comment.getCode(), null, null);
            CommentCoded commentrival = null;

            if (!list.isEmpty())
            {
                commentrival = list.get(0);
                if (isEdit)
                {
                    //comparamos que corresponda al mismo ID
                    if (comment.getId() != commentrival.getId())
                    {
                        errors.add("1|Code");
                    }
                } else
                {
                    //guardar
                    errors.add("1|Code");
                }
            }
        } else
        {
            errors.add("0|Code");
        }

        if (comment.getMessage() == null || comment.getMessage().isEmpty())
        {
            errors.add("0|Message");
        }

        if (comment.getApply() != null && comment.getApply() != 0)
        {
            if (comment.getApply() != 1 && comment.getApply() != 2 && comment.getApply() != 3)
            {
                errors.add("3|Apply");
            }
        } else
        {
            errors.add("0|Apply");
        }

        if (comment.getDiagnostic() != null && comment.getDiagnostic() != 0)
        {
            if (comment.getDiagnostic() != 1 && comment.getDiagnostic() != 2 && comment.getDiagnostic() != 3)
            {
                errors.add("3|Diagnostic");
            }
        } else
        {
            errors.add("0|Diagnostic");
        }

        return errors;
    }

}
