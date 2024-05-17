/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.socket;

import javax.websocket.CloseReason;
import org.glassfish.tyrus.client.ClientManager;

/**
 *
 * @author User
 */
public class ReconnectHandler {
    ClientManager client = ClientManager.createClient();
    ClientManager.ReconnectHandler reconnectHandler = new ClientManager.ReconnectHandler() {
        
        private int counter = 0;

        @Override
        public boolean onDisconnect(CloseReason closeReason) {
          counter++;
          if (counter <= 3) {
            System.out.println("### Reconnecting... (reconnect count: " + counter + ")");
            return true;
          } else {
            return false;
          }
        }

        @Override
        public boolean onConnectFailure(Exception exception) {
          counter++;
          if (counter <= 3) {
            System.out.println("### Reconnecting... (reconnect count: " + counter + ") " + exception.getMessage());

            // Thread.sleep(...) or something other "sleep-like" expression can be put here - you might want
            // to do it here to avoid potential DDoS when you don't limit number of reconnects.
            return true;
          } else {
            return false;
          }
        }

        @Override
        public long getDelay() {
          return 1;
        }

    };
}

