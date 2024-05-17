/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import javax.swing.Timer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import net.cltech.enterprisent.print_nt.bussines.connectors.TrayConnector;
import net.cltech.enterprisent.print_nt.bussines.storage.Storage;
import net.cltech.enterprisent.print_nt.tools.Cache;
import net.cltech.enterprisent.print_nt.tools.Log;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

/**
 *
 * @author cmartin
 */
@ClientEndpoint
public class TestEndpoint
{

    private Session userSession = null;
    private MessageHandler messageHandler;
    private Session session = null;
    private int count = 1;

    public TestEndpoint(final URI endpointURI, MessageHandler messageHandler)
    {
        try
        {
            this.messageHandler = messageHandler;
            //WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientManager client = ClientManager.createClient();
            client.getProperties().put(ClientProperties.RETRY_AFTER_SERVICE_UNAVAILABLE, true);
            ClientManager.ReconnectHandler reconnectHandler = new ClientManager.ReconnectHandler()
            {
                private int counter = 0;

                @Override
                public boolean onDisconnect(CloseReason closeReason)
                {
                    Log.info("onDisconnect");
                    Log.info("### Reconnecting... (reconnect count: " + counter + ")");
                    return true;
                }

                @Override
                public boolean onConnectFailure(Exception exception)
                {
                    Log.info("onConnectFailure");
                    Log.info("### Reconnecting... (reconnect count: " + counter + ") " + exception.getMessage());
                    Log.info("### Reconnecting... (reconnect count: EXCEPTION  " + exception);
                    return true;
                }

                @Override
                public long getDelay()
                {
                    return 300;
                }
            };

            client.getProperties().put(ClientProperties.RECONNECT_HANDLER, reconnectHandler);

            this.setSession(client.connectToServer(this, endpointURI));

        } catch (Exception ex)
        {
            Log.info("error is " + ex);
            Log.error(ex);
            Cache.getTrayConnector().showMessage("Conexion con websocket fallida", TrayConnector.ERROR_MESSAGE);
        }
    }

    @OnOpen
    public void onOpen(final Session userSession) throws IOException
    {

        this.userSession = userSession;
        Properties properties = Storage.loadConfigurationCredentials();
        this.sendMessage("{\"receivers\":[],\"serial\":\"" + properties.getProperty(Storage.SERIAL).trim() + "\",\"type\":0,\"message\":\"\"}");
        Log.info("conexion abierta");
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason)
    {
        Log.info("conexion cerrada");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(final String message)
    {
        Log.info("Llego mensaje" + message);
        this.messageHandler.handleMessage(message);
    }

    public void addMessageHandler(final MessageHandler msgHandler)
    {
        messageHandler = msgHandler;
    }

    public void sendMessage(final String message)
    {
        if (count == 1)
        {
            userSession.getAsyncRemote().sendText(message);
            count = count + 1;

            Timer timer = new Timer(10000, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    count = 1;
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

    }

    public static interface MessageHandler
    {

        public void handleMessage(String message);
    }

    public Session getUserSession()
    {
        return userSession;
    }

    public void setUserSession(Session userSession)
    {
        this.userSession = userSession;
    }

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

}
