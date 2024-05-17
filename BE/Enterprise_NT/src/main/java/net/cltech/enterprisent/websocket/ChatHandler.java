package net.cltech.enterprisent.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthenticationSession;
import net.cltech.enterprisent.domain.common.ChatMessage;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.security.SessionService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.websocket.ChatWsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Maneja los mensajes entre usuarios
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 03/10/2017
 * @see Creación
 */
public class ChatHandler extends TextWebSocketHandler
{

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
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
        deleteSessionById(session.getId(), true);
    }

    public void deleteSessionById(String session, boolean deleteActSession)
    {
        getRegisteredUsers().stream()
                .filter((session1) -> session1.getId().equals(session))
                .findFirst()
                .ifPresent((session1) ->
                {
                    try
                    {
                        deleteSession(session1, deleteActSession);
                    } catch (Exception ex)
                    {
                        ChatWsLog.error(ex);
                    }
                });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
    {
        ChatMessage chatMessage = Tools.jsonObject(message.getPayload(), ChatMessage.class);
        switch (chatMessage.getType())
        {
            case ChatHandler.TYPE_AUTHENTICATION:
                setUserToSession(session, chatMessage.getSender(), chatMessage.getBranch());
                break;
            case ChatHandler.TYPE_USER:
                sendChatMessage(chatMessage, session);
                break;
            default:

        }
       
    }

    /**
     * Metodo de envio de mensajes
     *
     * @param chatMessage mensaje
     * @param senderSession session del usuario que envia
     */
    public void sendChatMessage(ChatMessage chatMessage, WebSocketSession senderSession)
    {
      
        if (senderSession.getAttributes().get("username") != null)
        {
            chatMessage.getReceivers().stream().forEach((receiver) ->
            {
                List<WebSocketSession> sendList = findUserSessions(receiver);
                if (!sendList.isEmpty())
                {
                    sendList.stream().forEach((session) ->
                    {
                        try
                        {
                            if (session.isOpen())
                            {
                                session.sendMessage(new TextMessage(Tools.jsonObject(chatMessage)));
                            } else
                            {
                                ChatWsLog.info("Error la enviar el mensaje a [" + receiver + "] Session:" + session.getId() + " From: " + session.getRemoteAddress());
                            }
                        } catch (JsonProcessingException ex)
                        {
                            ChatWsLog.error(ex);
                        } catch (IOException ex)
                        {
                            ChatWsLog.error(ex);
                        }
                    });
                } else
                {
                    sendMessege(senderSession, TYPE_SYSTEM, receiver, ChatMessage.SYSTEM_RECEIVER_NOT_FOUND, "user [" + receiver + "] not connected", "");
                }
            });
        } else
        {
            sendMessege(senderSession, TYPE_SYSTEM, chatMessage.getSender(), ChatMessage.SYSTEM_SENDER_NOT_FOUND, "user [" + chatMessage.getSender() + "] not authenticated", "");
        }
    }

    public void sendMessege(WebSocketSession senderSession, int type, String sender, String code, String msg, String serial)
    {
        try
        {
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setType(type);
            systemMessage.setMessage(msg);
            systemMessage.setCode(code);
            systemMessage.setSender(sender);
            systemMessage.setIdSession(serial);
            if (senderSession != null)
            {
                senderSession.sendMessage(new TextMessage(Tools.jsonObject(systemMessage)));
            } else
            {
                sendToAll(systemMessage);
            }
        } catch (JsonProcessingException ex)
        {
            ChatWsLog.error(ex);
        } catch (IOException ex)
        {
            ChatWsLog.error(ex);
        }
    }

    /**
     * Establece username a la session
     *
     * @param session session
     * @param username username
     */
    private void setUserToSession(WebSocketSession session, String username, Integer branch)
    {
        sessions.stream()
                .filter((active) -> (active.getId().equals(session.getId())))
                .findFirst()
                .ifPresent((active) ->
                {
                    try
                    {
                        active.getAttributes().put("username", username);
                       
                        sendToUser(new ChatMessage(TYPE_SYSTEM, "El usuario se conecto", ChatMessage.SYSTEM_USER_CONECTED, session.getId()));
                        sendMessege(null, TYPE_SYSTEM, username, ChatMessage.SYSTEM_USER_CONECTED, "user [" + username + "] connected", "");
                        
                        AuthenticationSession authenticationSession = new AuthenticationSession();
                        
                        User user = userService.get(null, username, null, null); 
                        User userTemp = new User();
                        userTemp.setId(user.getId());
                        authenticationSession.setUser(userTemp);
                        
                        
                        if (authenticationSession.getUser() != null)
                        {
                            authenticationSession.setIdSession(session.getId());
                            authenticationSession.setIp(session.getRemoteAddress().toString());
                            authenticationSession.setBranch(branch);
                            sessionService.create(authenticationSession);
                        }
                    } catch (Exception ex)
                    {
                        ChatWsLog.error(ex);
                    }
                });
    }

    /**
     * Busca sessiones de un usuario
     *
     * @param username usuario
     *
     * @return lista de sesiones encontradas
     */
    private List<WebSocketSession> findUserSessions(String username)
    {
        return getRegisteredUsers().stream()
                .filter(session -> session.getAttributes().get("username").toString().equalsIgnoreCase(username))
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
                .filter(session -> session.getAttributes().get("username") != null)
                .collect(Collectors.toList());
    }

    /**
     *
     * Envia mensaje a todos los usuarios
     *
     * @param message
     */
    public void sendToAll(ChatMessage message)
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
                ChatWsLog.info("Error enviando mensaje a " + user.getRemoteAddress());
                ChatWsLog.error(ex);
            }
        });
    }

    /**
     *
     * Envia mensaje por id session
     *
     * @param message
     */
    public void sendToUser(ChatMessage message)
    {
        getRegisteredUsers().stream().filter((session1) -> session1.getId().equals(message.getIdSession()))
                .findFirst()
                .ifPresent((user) ->
                {
                    try
                    {
                        if (user.isOpen())
                        {
                            user.sendMessage(new TextMessage(Tools.jsonObject(message)));
                        }
                    } catch (IOException ex)
                    {
                        ChatWsLog.info("Error enviando mensaje a " + user.getRemoteAddress());
                        ChatWsLog.error(ex);
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
                    deleteSession(ws, false);
                } catch (Exception ex)
                {
                    ChatWsLog.error(ex);
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
    private void deleteSession(WebSocketSession sessionToDelete, boolean deleteActSession) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (sessionToDelete.getAttributes() != null && sessionToDelete.getAttributes().get("username") != null && !getRegisteredUsers().isEmpty())
        {
            if (deleteActSession)
            {
                AuthenticationSession auth = new AuthenticationSession();
                auth.setIdSession(sessionToDelete.getId());
                sessionService.deleteBySession(auth, false);
            }
            sendMessege(null, TYPE_SYSTEM, sessionToDelete.getAttributes().get("username").toString(), ChatMessage.SYSTEM_USER_LOGOUT, "user [" + sessionToDelete.getAttributes().get("username").toString() + "] not connected", sessionToDelete.getId());
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

}
