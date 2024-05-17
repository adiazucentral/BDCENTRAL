package net.cltech.enterprisent.domain.integration.ingreso;

import org.joda.time.DateTime;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la respuesta de la tabla de control de la busqueda por su id de orden
 *
 * @version 1.0.0
 * @author bvalero
 * @since 19/03/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "SonControl",
        description = "respuesta de la tabla de control por id orden"
)
public class ResponsSonControl
{
    @ApiObjectField(name = "idOrden", description = "Id de la orden", required = true, order = 1)
    private long idOrder;
    @ApiObjectField(name = "cuenta", description = "cuenta key santafe", required = true, order = 2)
    private String cuenta;
    @ApiObjectField(name = "mensaje", description = "Mensaje de creacion de la orden", required = true, order = 3)
    private String mensaje;
    @ApiObjectField(name = "estado", description = "estado de la orden 0-Mensaje ack no recibido"
            + "1-Orden recibida y creada en el LIS"
            + "2-Impresion del codigo de barras de esa orden en Homebound"
            + "3-Verificación de la muestra en el LIS"
            + "4-Muestra pendiente"
            + "5-Retoma de muestra"
            + "6-Muestra rechazada"
            + "7-Eliminación de la muestra" , required = true, order = 4)
    private int estado;
    @ApiObjectField(name = "fecha", description = "Fecha de actualizacion de la orden", required = true, order = 5)
    private DateTime fecha;
    @ApiObjectField(name = "cantidad", description = "Cantidad correspondiente para un estudio de una orden", required = false, order = 6)
    private String cantidad;
    @ApiObjectField(name = "externalPrinting", description = "Impresión desde Homebound", required = false, order = 7)
    private String externalPrinting;
    @ApiObjectField(name = "state", description = "Estado a cambiar en la tabla de control", required = false, order = 8)
    private int state;
    @ApiObjectField(name = "perfiles", description = "Perfiles de la orden", required = false, order = 9)
    private String perfiles;
    
    public ResponsSonControl()
    {
        this.externalPrinting = "";
        this.cantidad = "";
    }

    public ResponsSonControl(long idOrder, String cuenta, String mensaje, int estado, DateTime fecha)
    {
        this.idOrder = idOrder;
        this.cuenta = cuenta;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fecha = fecha;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }

    public String getCuenta()
    {
        return cuenta;
    }

    public void setCuenta(String cuenta)
    {
        this.cuenta = cuenta;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public int getEstado()
    {
        return estado;
    }

    public void setEstado(int estado)
    {
        this.estado = estado;
    }

    public DateTime getFecha()
    {
        return fecha;
    }

    public void setFecha(DateTime fecha)
    {
        this.fecha = fecha;
    }

    public String getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(String cantidad)
    {
        this.cantidad = cantidad;
    }

    public String getExternalPrinting()
    {
        return externalPrinting;
    }

    public void setExternalPrinting(String externalPrinting)
    {
        this.externalPrinting = externalPrinting;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getPerfiles()
    {
        return perfiles;
    }

    public void setPerfiles(String perfiles)
    {
        this.perfiles = perfiles;
    }
}
