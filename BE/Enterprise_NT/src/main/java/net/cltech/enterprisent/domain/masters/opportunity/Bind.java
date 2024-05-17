package net.cltech.enterprisent.domain.masters.opportunity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Clase
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
 * @see Creación
 */
@ApiObject(
        group = "Oportunidad",
        name = "Clase",
        description = "Muestra informacion del maestro Clase que usa el API"
)
@JsonInclude(Include.NON_NULL)
public class Bind extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la clase", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la clase", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "minimum", description = "Valor Minimo en minutos (incluyente) ", required = true, order = 3)
    private Long minimum;
    @ApiObjectField(name = "maximum", description = "Valor maximo en minutos (excluyente)", required = true, order = 4)
    private Long maximum;
    @ApiObjectField(name = "state", description = "Estado de la clase", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "frecuency", description = "Frecuencía de la clase", required = true, order = 4)
    private Long frecuency;

    public Bind()
    {
    }

    public Bind(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Long minimum)
    {
        this.minimum = minimum;
    }

    public Long getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Long maximum)
    {
        this.maximum = maximum;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Long getFrecuency()
    {
        return frecuency;
    }

    public void setFrecuency(Long frecuency)
    {

        this.frecuency = frecuency;
    }

}
