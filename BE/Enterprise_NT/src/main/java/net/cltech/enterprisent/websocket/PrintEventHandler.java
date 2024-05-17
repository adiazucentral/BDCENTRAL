package net.cltech.enterprisent.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeRequest;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Maneja las impresiones de clientes
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 22/09/2017
 * @see Creación
 */
public class PrintEventHandler extends TextWebSocketHandler
{

    private final List<WebSocketSession> sessions = new ArrayList<>(0);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] - Connected - host - " + session.getRemoteAddress().getHostString());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] - Disconnected - host - " + session.getRemoteAddress().getHostString());
        WebSocketSession sessionToDelete = null;
        for (WebSocketSession session1 : sessions)
        {
            if (session1.getId().equals(session.getId()))
            {
                sessionToDelete = session1;
                break;
            }
        }

        if (sessionToDelete != null)
        {
            sessions.remove(sessionToDelete);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
    {
        org.springframework.web.socket.TextMessage tm = new TextMessage("22");
        System.out.println("Mensaje:" + message.getPayload() + "\nFrom:" + session.getRemoteAddress());
        session.sendMessage(tm);

    }

    /**
     * Metodo de envio de impresion
     *
     * @param printRequest peticion de impresion con informacion de la impresion
     *
     * @return si envia a imprimir
     * @throws JsonProcessingException error generando json
     * @throws IOException Error
     */
    public boolean sendToPrint(BarcodeRequest printRequest) throws JsonProcessingException, IOException
    {

        for (WebSocketSession session : sessions)
        {
            if (session.getRemoteAddress().getAddress().getHostAddress().equalsIgnoreCase(printRequest.getId()))
            {
                session.sendMessage(new TextMessage(Tools.jsonObject(printRequest)));
                return true;
            }
        }
        return false;
    }
}
