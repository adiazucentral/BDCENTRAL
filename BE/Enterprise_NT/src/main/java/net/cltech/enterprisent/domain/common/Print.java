package net.cltech.enterprisent.domain.common;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa La impresion
 *
 * @version 1.0.0
 * @author eaquijano
 * @since 02/05/2019
 * @see Creacion
 */
@ApiObject(
        group = "Común",
        name = "Imprimir",
        description = "Representa Mensaje de chat"
)
public class Print
{

    @ApiObjectField(name = "receivers", description = "Usuarios que recibiran el mensaje", required = false, order = 1)
    private List<String> receivers = new ArrayList<>();
    @ApiObjectField(name = "serial", description = "Serial del equipo", required = true, order = 2)
    private String serial;
    @ApiObjectField(name = "type", description = "Tipo de mensaje 0 -> Autenticacion, 1 -> Enviar Mensaje, 2-> Sistema", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "message", description = "Mensaje", required = true, order = 4)
    private String message;
    @ApiObjectField(name = "typePrinter", description = "Tipo de impresora 0-> label 1 -> report", required = true, order = 4)
    private int typePrinter;

    public List<String> getReceivers()
    {
        return receivers;
    }

    public void setReceivers(List<String> receivers)
    {
        this.receivers = receivers;
    }

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getTypePrinter()
    {
        return typePrinter;
    }

    public void setTypePrinter(int typePrinter)
    {
        this.typePrinter = typePrinter;
    }

    /**
     * Usuario desconectado.
     */
    public static final String SYSTEM_USER_LOGOUT = "0";
    /**
     * Usuario emisor no registrado en la sesion.
     */
    public static final String SYSTEM_SENDER_NOT_FOUND = "1";
    /**
     * Usuario receptor no conectado
     */
    public static final String SYSTEM_RECEIVER_NOT_FOUND = "2";
    /**
     * Usuario conectado
     */
    public static final String SYSTEM_USER_CONECTED = "3";
    /**
     * Configuracion modificada
     */
    public static final String SYSTEM_CONFIGURATION_UPDATED = "4";
    /**
     * Impresora para etiqueta
     */
    public static final int PRINTER_LABEL = 0;
    /**
     * Impresora para reporte
     */
    public static final int PRINTER_REPORT = 1;
}
