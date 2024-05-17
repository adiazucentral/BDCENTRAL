package net.cltech.enterprisent.domain.operation.statistic;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa un demografico para las estadisticas
 *
 * @author eacuña
 */
public class StatisticDemographic
{

    @ApiObjectField(name = "idOrder", description = "Numero de la orden", required = true, order = 1)
    private Long idOrder;
    @ApiObjectField(name = "idDemographic", description = "Id Demografico", required = true, order = 1)
    private int idDemographic;
    @ApiObjectField(name = "demographic", description = "Nombre Demografico", required = true, order = 2)
    private String demographic;
    @ApiObjectField(name = "encoded", description = "Es codificado", required = true, order = 3)
    private boolean encoded;
    @ApiObjectField(name = "notCodifiedValue", description = "Valor del demografico si es no codificado", required = false, order = 5)
    private String notCodifiedValue;
    @ApiObjectField(name = "codifiedId", description = "Si es codificado, envia el id del item seleccionado", required = false, order = 6)
    private Integer codifiedId;
    @ApiObjectField(name = "codifiedCode", description = "Si es codificado, envia el codigo del item seleccionado", required = false, order = 7)
    private String codifiedCode;
    @ApiObjectField(name = "codifiedName", description = "Si es codificado, envia el nombre del item seleccionado", required = false, order = 8)
    private String codifiedName;

    public StatisticDemographic()
    {

    }

    public Long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(Long idOrder)
    {
        this.idOrder = idOrder;
    }

    public StatisticDemographic(Integer idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public int getIdDemographic()
    {
        return idDemographic;
    }

    public void setIdDemographic(int idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public String getDemographic()
    {
        return demographic;
    }

    public void setDemographic(String demographic)
    {
        this.demographic = demographic;
    }

    public boolean isEncoded()
    {
        return encoded;
    }

    public void setEncoded(boolean encoded)
    {
        this.encoded = encoded;
    }

    public String getNotCodifiedValue()
    {
        return notCodifiedValue;
    }

    public void setNotCodifiedValue(String notCodifiedValue)
    {
        this.notCodifiedValue = notCodifiedValue;
    }

    public Integer getCodifiedId()
    {
        return codifiedId;
    }

    public void setCodifiedId(Integer codifiedId)
    {
        this.codifiedId = codifiedId;
    }

    public String getCodifiedCode()
    {
        return codifiedCode;
    }

    public void setCodifiedCode(String codifiedCode)
    {
        this.codifiedCode = codifiedCode;
    }

    public String getCodifiedName()
    {
        return codifiedName;
    }

    public void setCodifiedName(String codifiedName)
    {
        this.codifiedName = codifiedName;
    }

    public String getValue()
    {
        if (encoded)
        {
            return codifiedCode == null ? null : codifiedCode + "." + getCodifiedName();
        }
        return notCodifiedValue;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + this.idDemographic;
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
        final StatisticDemographic other = (StatisticDemographic) obj;
        if (this.idDemographic != other.idDemographic)
        {
            return false;
        }
        return true;
    }

}
