package net.cltech.enterprisent.domain.integration.band;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el Objeto de respuesta de la muestra verificada en NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 22/05/2020
 * @see Creación
 */
@ApiObject(
        name = "Muestra verificada",
        group = "Banda Transportadora",
        description = "Objeto de respuesta de la muestra verificada en NT"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BandVerifiedSample
{
    @ApiObjectField(name = "order", description = "Id de la orden de NT", required = true, order = 1)
    private String order;
    @ApiObjectField(name = "type", description = "Tipo de orden", required = true, order = 2)
    private String type;
    @ApiObjectField(name = "patient", description = "Paciente", required = true, order = 3)
    private BandPatient patient;
    
    public BandVerifiedSample()
    {
        patient = new BandPatient();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BandPatient getPatient() {
        return patient;
    }

    public void setPatient(BandPatient patient) {
        this.patient = patient;
    }
}
