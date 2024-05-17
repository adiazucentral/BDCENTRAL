package net.cltech.enterprisent.domain.integration.HL7Socket;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Representa el log de entre la impresion del codigo de barras en homebound 
 * y la verificacion de la muestra en el LIS
 * 
 * @version 1.0.0
 * @author bvalero
 * @since 10/08/2020
 * @see Creación
 */
public class LoggerInterface 
{    
    public void writeToFile(String path, String textLine) throws IOException
    {
        try
        {
            FileWriter write = new FileWriter(path, true);
            PrintWriter print_line = new PrintWriter(write);
            print_line.printf("%s" + "%n", textLine);
            print_line.close();
        }
        catch (IOException e)
        {
            e.getMessage();
        }
    }
}