package net.cltech.enterprisent.domain.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una excepción controlada para el proyecto de NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 11/04/2017
 * @see Creación
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
