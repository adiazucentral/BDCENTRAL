/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.BarcodePathologyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.pathology.BarcodePathologyDesigner;
import net.cltech.enterprisent.service.interfaces.operation.pathology.BarcodePathologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion codigo de barras para Patologia
 *
 * @version 1.0.0
 * @author eacuna
 * @see 11/05/2021
 * @see Creación
 */
@Service
public class BarcodePathologyServiceEnterpriseNT implements BarcodePathologyService
{
    @Autowired
    private BarcodePathologyDao dao;

    @Override
    public List<BarcodePathologyDesigner> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public BarcodePathologyDesigner create(BarcodePathologyDesigner create) throws Exception
    {
        List<String> errors = validateFields(false, create);
        if (errors.isEmpty())
        {
            return dao.create(create);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public BarcodePathologyDesigner update(BarcodePathologyDesigner barcode) throws Exception
    {
        List<String> errors = validateFields(true, barcode);
        if (errors.isEmpty())
        {
            BarcodePathologyDesigner updated = dao.update(barcode);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<BarcodePathologyDesigner> list(boolean state) throws Exception
    {
        return list().stream()
                .filter(barcode -> barcode.isActive() == state)
                .collect(Collectors.toList());
    }

    private List<String> validateFields(boolean isEdit, BarcodePathologyDesigner bean) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (bean.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (getById(bean.getId()) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (bean.getTemplate() == null || bean.getTemplate().isEmpty())
        {
            errors.add("0|template");
        }
        if (bean.getCommand() == null || bean.getCommand().trim().isEmpty())
        {
            errors.add("0|command");
        }

        return errors;
    }

    @Override
    public BarcodePathologyDesigner getById(Integer id) throws Exception
    {
        return list().stream()
                .filter(barcode -> Objects.equals(barcode.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
