package net.cltech.enterprisent.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.Print;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.websocket.PrintWsLog;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Maneja la impresion
 *
 * @author equijano
 * @version 1.0.0
 * @since 02/05/2019
 * @see Creación
 */
public class PrintHandler extends TextWebSocketHandler
{

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        deleteSessionById(session.getId());
    }

    public void deleteSessionById(String session)
    {
        getRegisteredUsers().stream()
                .filter((s) -> s.getId().equals(session))
                .findFirst()
                .ifPresent((s) ->
                {
                    try
                    {
                        deleteSession(s);
                    } catch (Exception ex)
                    {
                        //PrintWsLog.error(ex);
                    }
                });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
    {
        Print chatMessage = Tools.jsonObject(message.getPayload(), Print.class);
        switch (chatMessage.getType())
        {
            case PrintHandler.TYPE_AUTHENTICATION:
                setUserToSession(session, chatMessage.getSerial());
                break;
            case PrintHandler.TYPE_USER:
                sendPrint(chatMessage, session);
                break;
            case PrintHandler.TYPE_SYSTEM:
                break;
            default:

        }
    }

    /**
     * Metodo de envio de mensajes
     *
     * @param chatMessage mensaje
     * @param senderSession session del usuario que envia
     * @return
     */
    public boolean sendPrint(Print chatMessage, WebSocketSession senderSession)
    {
        final List<Boolean> send = new ArrayList<>(0);
        if (senderSession.getAttributes().get("serial") != null)
        {
            chatMessage.getReceivers().stream().forEach((receiver) ->
            {
                List<WebSocketSession> sendList = findSerialSessions(receiver);
                if (!sendList.isEmpty())
                {
                    sendList.stream().forEach((session) ->
                    {
                        try
                        {
                            if (session.isOpen())
                            {
                                session.sendMessage(new TextMessage(Tools.jsonObject(chatMessage)));
                            } 
                        } catch (JsonProcessingException ex)
                        {
                            send.add(false);
                            //PrintWsLog.error(ex);
                        } catch (IOException ex)
                        {
                            send.add(false);
                            //PrintWsLog.error(ex);
                        }
                    });
                } else
                {
                   
                    send.add(false);
                }
            });
        } else
        {
            PrintWsLog.error("It's about sending to print without serial");
            send.add(false);
        }
        return send.isEmpty();
    }

    public void sendMessege(WebSocketSession senderSession, int type, String serial, String msg)
    {
        try
        {
           
            Print systemMessage = new Print();
            systemMessage.setType(type);
            systemMessage.setMessage(msg);
            systemMessage.setSerial(serial);
            if (senderSession != null)
            {
                senderSession.sendMessage(new TextMessage(Tools.jsonObject(systemMessage)));
            } else
            {
                sendToAll(systemMessage);
            }
        } catch (JsonProcessingException ex)
        {
           // PrintWsLog.error(ex);
        } catch (IOException ex)
        {
            //PrintWsLog.error(ex);
        }
    }

    /**
     * Establece serial a la session
     *
     * @param session session
     * @param serial serial
     */
    private void setUserToSession(WebSocketSession session, String serial)
    {
        sessions.stream()
                .filter((active) -> (active.getId().equals(session.getId())))
                .findFirst()
                .ifPresent((active) ->
                {
                    try
                    {
                        active.getAttributes().put("serial", serial);
                    } catch (Exception ex)
                    {
                        //PrintWsLog.error(ex);
                    }
                });
    }

    /**
     * Busca sessiones de un usuario
     *
     * @param serial usuario
     *
     * @return lista de sesiones encontradas
     */
    public List<WebSocketSession> findSerialSessions(String serial)
    {
        return getRegisteredUsers().stream()
                .filter(session -> session.getAttributes().get("serial").toString().equalsIgnoreCase(serial))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de las sesiones con usuarios registrados
     *
     * @return
     */
    public List<WebSocketSession> getRegisteredUsers()
    {
        return sessions.stream()
                .filter(session -> session.getAttributes().get("serial") != null)
                .collect(Collectors.toList());
    }

    /**
     *
     * Envia mensaje a todos los usuarios
     *
     * @param message
     */
    public void sendToAll(Print message)
    {
        getRegisteredUsers().forEach((user) ->
        {
            try
            {
                if (user.isOpen())
                {
                    user.sendMessage(new TextMessage(Tools.jsonObject(message)));
                }
            } catch (IOException ex)
            {
                //PrintWsLog.info("Error enviando mensaje a " + user.getRemoteAddress());
               // PrintWsLog.error(ex);
            }
        });
    }

    /**
     * Borrar todas las sessiones
     *
     */
    public void deleteAll()
    {
        if (sessions != null && sessions.isEmpty() == false)
        {
            getRegisteredUsers().stream().forEach(ws ->
            {
                try
                {
                    deleteSession(ws);
                } catch (Exception ex)
                {
                    //PrintWsLog.error(ex);
                }
            });
        }
    }

    /**
     *
     * Elimina una session activa
     *
     * @param sessionToDelete
     */
    private void deleteSession(WebSocketSession sessionToDelete) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (sessionToDelete.getAttributes() != null && sessionToDelete.getAttributes().get("serial") != null && !getRegisteredUsers().isEmpty())
        {
           // PrintWsLog.info("[" + sdf.format(new Date()) + "] - user [" + sessionToDelete.getAttributes().get("serial").toString() + "] not connected");
        }
        
        if (sessionToDelete.isOpen())
        {
            sessionToDelete.close();
        }
        sessions.remove(sessionToDelete);
    }

    public static final int TYPE_AUTHENTICATION = 0;
    public static final int TYPE_USER = 1;
    public static final int TYPE_SYSTEM = 2;
    public static final int TYPE_CLOSE = 3;

}
