/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author dcortes
 * @since 11/04/2017
 * @see Para cuando se crea una clase incluir
 */
public class EnterpriseNTException extends Exception
{

    private List<String> errorFields;

    public EnterpriseNTException(String message)
    {
        super(message);
        errorFields = new ArrayList<>(0);
    }
    
    public EnterpriseNTException(List<String> errorFields)
    {
        super("Data Error");
        this.errorFields = errorFields;
    }

    public List<String> getErrorFields()
    {
        return errorFields;
    }

    public void setErrorFields(List<String> errorFields)
    {
        this.errorFields = errorFields;
    }
}
