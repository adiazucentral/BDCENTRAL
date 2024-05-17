/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.connectors;

/**
 * @version 1.0
 * @author hhurtado
 * @since 19-06-2018
 * @see Creacion de excepxion HTTP para la conexion a webService
 */
public class HttpException extends Exception
{

    private int httpCode;

    public HttpException(int httpCode)
    {
        this.httpCode = httpCode;
    }

    public int getHttpCode()
    {
        return httpCode;
    }

    public void setHttpCode(int httpCode)
    {
        this.httpCode = httpCode;
    }
}
