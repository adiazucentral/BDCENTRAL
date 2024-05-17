/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.connectors;

import java.awt.TrayIcon;

/**
 *
 * @author dcortes
 */
public interface TrayConnector {

    public static final TrayIcon.MessageType ERROR_MESSAGE = TrayIcon.MessageType.ERROR;
    public static final TrayIcon.MessageType WARNING_MESSAGE = TrayIcon.MessageType.WARNING;
    public static final TrayIcon.MessageType INFO_MESSAGE = TrayIcon.MessageType.INFO;
    public static final TrayIcon.MessageType NONE_MESSAGE = TrayIcon.MessageType.NONE;

    public void showMessage(String message, TrayIcon.MessageType type);
}
