package net.cltech.enterprisent.domain.common;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Alarmas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 03/10/2017
 * @see Creacion
 */
@ApiObject(
        group = "Común",
        name = "Chat",
        description = "Representa Mensaje de chat"
)
public class ChatMessage
{

    @ApiObjectField(name = "receivers", description = "Usuarios que recibiran el mensaje", required = false, order = 1)
    private List<String> receivers = new ArrayList<>();
    @ApiObjectField(name = "sender", description = "Usuario que envia el mensaje", required = true, order = 2)
    private String sender;
    @ApiObjectField(name = "type", description = "Tipo de mensaje", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "message", description = "Mensaje", required = true, order = 4)
    private String message;
    @ApiObjectField(name = "code", description = "Códificación de mensaje<b> 0 - Desconexion de usuario<b> 1 - Usuario emisor no autenticado<b> 2 - Usuario receptor no conectado<b> 3 - Usuario conectado"
            + "<b> 4 - Cambio en configuracion", required = true, order = 5)
    private String code;
    @ApiObjectField(name = "branch", description = "Id de la sede donde se conecta", required = true, order = 6)
    private Integer branch;
    @ApiObjectField(name = "idSession", description = "Id de la sesion", required = false, order = 7)
    private String idSession;

    public ChatMessage()
    {

    }

    public ChatMessage(Integer type, String message, String code, String idSession)
    {
        this.type = type;
        this.message = message;
        this.code = code;
        this.idSession = idSession;
    }

    public List<String> getReceivers()
    {
        return receivers;
    }

    public void setReceivers(List<String> receivers)
    {
        this.receivers = receivers;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }

    public String getIdSession()
    {
        return idSession;
    }

    public void setIdSession(String idSession)
    {
        this.idSession = idSession;
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
}
