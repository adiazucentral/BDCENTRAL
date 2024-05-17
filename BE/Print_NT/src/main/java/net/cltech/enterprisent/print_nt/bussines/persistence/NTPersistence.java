/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import net.cltech.enterprisent.print_nt.bussines.HttpException;
import net.cltech.enterprisent.print_nt.bussines.RESTCommunication;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintConfiguration;

/**
 * Clase que permite el acceso a los metodos de Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/01/2019
 * @see Creación
 */
public class NTPersistence
{

    private final Gson gson;
    private final RESTCommunication rest;

    public NTPersistence(String urlBase)
    {
        JsonDeserializer<Date> deser = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> json == null ? null : new Date(json.getAsLong());
        JsonSerializer<Date> ser = (Date t, Type type, JsonSerializationContext jsc) -> new JsonPrimitive(t.getTime());
        rest = new RESTCommunication(urlBase);
        gson = new GsonBuilder().registerTypeAdapter(Date.class, deser).registerTypeAdapter(Date.class, ser).create();
    }

    /**
     * Realiza una prueba a la url del servicio
     *
     * @return True si hay conexion, False
     * @throws IOException
     */
    public boolean test() throws IOException
    {
        try
        {
            testRest();
            return true;
        } catch (HttpException ex)
        {
            return false;
        }
    }

    /**
     * Prueba la url del servidor
     *
     * @throws IOException Error en la conexion
     * @throws HttpException Error en protocolo HTTP revisar las constantes
     */
    public void testRest() throws HttpException, IOException
    {
        try
        {
            try
            {
                rest.get("/api/configuration/test");
            } catch (IOException ex)
            {
                throw ex;
            }
        } catch (HttpException ex)
        {
            throw ex;
        }
    }

    /**
     * Obtener configuraciones
     *
     * @param json objeto con la informacion para generar la configuracion
     *
     * @return data de configuracion
     * @throws IOException Error en la conexion
     * @throws HttpException Error en protocolo HTTP revisar las constantes
     */
    public PrintConfiguration generateConfig(String json) throws IOException, HttpException
    {
        return gson.fromJson(rest.post("/api/configuration/configprint", json), new TypeToken<PrintConfiguration>()
        {
        }.getType());
    }

}
