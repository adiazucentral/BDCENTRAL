package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el un detino a verificar por el middleware
 *
 * @author adiaz
 * @version 1.0.0
 * @since 13/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Orden, muestra y siguiente destino a verificar",
        description = "Representa una orden con muestra y destino"
)
public class CheckDestination {
    @ApiObjectField(name = "order", description = "Numero de orden", order = 1)
    private String order;
    @ApiObjectField(name = "sample", description = "Codigo de la muestra", order = 2)
    private String sample;
    @ApiObjectField(name = "destination", description = "Destino a verificar", order = 3)
    private String destination;
    @ApiObjectField(name = "error", description = "Error controlado", order = 3)
    private String error;
    
    public CheckDestination() {
               
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }
}
