package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de respuesta que se le enviara al middleware
 * con los respectivos valores de referencia delta
 * 
 * @version 1.0.0
 * @author Julian
 * @since 05/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Middleware",
        name = "DeltaCheckMiddleware",
        description = "Representa un objeto que contendra valores delta solicitados por el middleware"
)
public class DeltaCheckMiddleware
{
    @ApiObjectField(name = "min", description = "Nivel mínimo de delta check", required = false, order = 1)
    private float min;
    @ApiObjectField(name = "max", description = "Nivel máximo de delta check", required = false, order = 2)
    private float max;
    @ApiObjectField(name = "days", description = "Días de delta check", required = false, order = 3)
    private Integer days;

    public DeltaCheckMiddleware()
    {
    }

    public float getMin()
    {
        return min;
    }

    public void setMin(float min)
    {
        this.min = min;
    }

    public float getMax()
    {
        return max;
    }

    public void setMax(float max)
    {
        this.max = max;
    }

    public Integer getDays()
    {
        return days;
    }

    public void setDays(Integer days)
    {
        this.days = days;
    }
}
