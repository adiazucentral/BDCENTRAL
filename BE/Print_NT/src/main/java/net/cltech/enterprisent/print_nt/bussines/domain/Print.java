package net.cltech.enterprisent.print_nt.bussines.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa La impresion
 *
 * @version 1.0.0
 * @author eaquijano
 * @since 02/05/2019
 * @see Creacion
 */
public class Print
{

    private List<String> receivers = new ArrayList<>();
    private String serial;
    private Integer type;
    private String message;
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
