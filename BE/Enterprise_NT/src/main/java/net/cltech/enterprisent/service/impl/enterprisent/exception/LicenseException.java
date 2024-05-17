package net.cltech.enterprisent.service.impl.enterprisent.exception;

import java.util.ArrayList;
import java.util.List;

public class LicenseException extends Exception
{

    private List<String> errorFields;

    public LicenseException(String message)
    {
        super(message);
        errorFields = new ArrayList<>(0);
    }

    public LicenseException(List<String> errorFields)
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

