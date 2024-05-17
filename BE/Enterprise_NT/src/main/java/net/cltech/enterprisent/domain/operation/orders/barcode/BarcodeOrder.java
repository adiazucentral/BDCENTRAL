package net.cltech.enterprisent.domain.operation.orders.barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Bean que representa la impresión de códigos de barras
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 26/09/2017
 * @see Creación
 */
public class BarcodeOrder
{

    private String order;
    private String printerIp;
    private Integer sampleId;
    private String sampleCode;
    private String sampleName;
    private String sampleRecipient;
    private String configuration;
    private String separator;
    private boolean printSample;
    private HashMap demos = new HashMap();
    ;
    private List<String> tests = new ArrayList<>();
    private List<String> areas = new ArrayList<>();
    private int printNumberSample;
    private int yearDigits;
    private int quantity;

    public BarcodeOrder()
    {
    }

    public BarcodeOrder(String order, Integer sampleId)
    {
        this.order = order;
        this.sampleId = sampleId;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public Integer getSampleId()
    {
        return sampleId;
    }

    public void setSampleId(Integer sampleId)
    {
        this.sampleId = sampleId;
    }

    public String getSampleCode()
    {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode)
    {
        this.sampleCode = sampleCode;
    }

    public String getSampleName()
    {
        return sampleName;
    }

    public void setSampleName(String sampleName)
    {
        this.sampleName = sampleName;
    }

    public String getSampleRecipient()
    {
        return sampleRecipient;
    }

    public void setSampleRecipient(String sampleRecipient)
    {
        this.sampleRecipient = sampleRecipient;
    }

    public boolean isPrintSample()
    {
        return printSample;
    }

    public void setPrintSample(boolean printSample)
    {
        this.printSample = printSample;
    }

    public HashMap getDemos()
    {
        return demos;
    }

    public void setDemos(HashMap demos)
    {
        this.demos = demos;
    }

    public List<String> getTests()
    {
        return tests;
    }

    public void setTests(List<String> tests)
    {
        this.tests = tests;
    }

    public List<String> getAreas()
    {
        return areas;
    }

    public void setAreas(List<String> areas)
    {
        this.areas = areas;
    }

    public int getPrintNumberSample()
    {
        return printNumberSample;
    }

    public void setPrintNumberSample(int printNumberSample)
    {
        this.printNumberSample = printNumberSample;
    }

    public String getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(String configuration)
    {
        this.configuration = configuration;
    }

    public int getYearDigits()
    {
        return yearDigits;
    }

    public void setYearDigits(int yearDigits)
    {
        this.yearDigits = yearDigits;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    public String getPrinterIp()
    {
        return printerIp;
    }

    public void setPrinterIp(String printerIp)
    {
        this.printerIp = printerIp;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    
    

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.order);
        hash = 37 * hash + Objects.hashCode(this.sampleId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BarcodeOrder other = (BarcodeOrder) obj;
        if (!Objects.equals(this.order, other.order))
        {
            return false;
        }
        if (!Objects.equals(this.sampleId, other.sampleId))
        {
            return false;
        }
        return true;
    }

}
