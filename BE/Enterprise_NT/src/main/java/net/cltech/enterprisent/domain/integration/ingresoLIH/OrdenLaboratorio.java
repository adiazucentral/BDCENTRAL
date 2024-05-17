package net.cltech.enterprisent.domain.integration.ingresoLIH;

import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "OrdenLaboratorio",
        description = "OrdenLaboratorio a enviar"
)
public class OrdenLaboratorio {
    
    @ApiObjectField(name = "datosGenerales", description = "DatosGenerales de la orden", required = true, order = 1)
    private DatosGenerales datosGenerales;
    @ApiObjectField(name = "paciente", description = "Paciente de la orden", required = true, order = 2)
    private Paciente paciente;
    @ApiObjectField(name = "items", description = "examenes de la orden", required = true, order = 3)
    private List<Items> items;

    public OrdenLaboratorio(DatosGenerales datosGenerales, Paciente paciente, List<Items> items)
    {
        this.datosGenerales = datosGenerales;
        this.paciente = paciente;
        this.items = items;
    }

    public OrdenLaboratorio() {
    }

    public DatosGenerales getDatosGenerales() {
        return datosGenerales;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.datosGenerales.getNumeroOrden());
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
        final OrdenLaboratorio other = (OrdenLaboratorio) obj;
        if (Objects.equals(this.datosGenerales.getNumeroOrden(), other.getDatosGenerales().getNumeroOrden()))
        {
            return true;
        }
        return false;
    }

    public void setDatosGenerales(DatosGenerales DatosGenerales) {
        this.datosGenerales = DatosGenerales;
    }

    public Paciente getPaciente()
    {
        return paciente;
    }

    public void setPaciente(Paciente paciente)
    {
        this.paciente = paciente;
    }

    public List<Items> getItems()
    {
        return items;
    }

    public void setItems(List<Items> items)
    {
        this.items = items;
    }
}
