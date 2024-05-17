package net.cltech.enterprisent.domain.integration.ingresoLIH;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 22/04/2020
 * @see Creación
 */
@ApiObject(
    group = "Integración",
    name = "DatosGenerales",
    description = "DatosGenerales del LIH"
)
public class DatosGenerales {
    
    @ApiObjectField(name = "numeroOrden", description = "Numero de Orden", required = true, order = 1)
    private String numeroOrden;
    @ApiObjectField(name = "codCliente", description = "Codigo del cliente", required = true, order = 2)
    private String codCliente;
    @ApiObjectField(name = "nombreCliente", description = "Nombre del cliente", required = true, order = 3)
    private String nombreCliente;
    @ApiObjectField(name = "codMedico", description = "Codigo del Medico", required = true, order = 4)
    private String codMedico;
    @ApiObjectField(name = "observ", description = "Observacion", required = true, order = 5)
    private String observ;

    public DatosGenerales(String numeroOrden, String codCliente, String nombreCliente, String codMedico, String observ)
    {
        this.numeroOrden = numeroOrden;
        this.codCliente = codCliente;
        this.nombreCliente = nombreCliente;
        this.codMedico = codMedico;
        this.observ = observ;
    }

    public DatosGenerales() {
    }

    public String getObserv() {
        return observ;
    }

    public void setObserv(String observ) {
        this.observ = observ;
    }

    public String getNumeroOrden()
    {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden)
    {
        this.numeroOrden = numeroOrden;
    }

    public String getCodCliente()
    {
        return codCliente;
    }

    public void setCodCliente(String codCliente)
    {
        this.codCliente = codCliente;
    }

    public String getNombreCliente()
    {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente)
    {
        this.nombreCliente = nombreCliente;
    }

    public String getCodMedico()
    {
        return codMedico;
    }

    public void setCodMedico(String codMedico)
    {
        this.codMedico = codMedico;
    }
    
    
    
    
}
