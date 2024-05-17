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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import net.cltech.enterprisent.print_nt.bussines.HttpException;
import net.cltech.enterprisent.print_nt.bussines.RESTCommunication;
import net.cltech.enterprisent.print_nt.bussines.domain.Configuration;
import net.cltech.enterprisent.print_nt.bussines.domain.Report;
import net.cltech.enterprisent.print_nt.tools.Log;

/**
 * Clase que permite el acceso a los metodos de NodeJs
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/01/2019
 * @see Creación
 */
public class NTNodePersistence
{

    private final Gson gson;
    private final RESTCommunication rest;

    public NTNodePersistence(String urlBase)
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
            getConfiguration();
            return true;
        } catch (HttpException ex)
        {
            return false;
        }
    }

    /**
     * Generar el reporte
     *
     * @param json objeto con la informacion para generar el reporte
     *
     * @return data del reporte
     * @throws IOException Error en la conexion
     * @throws HttpException Error en protocolo HTTP revisar las constantes
     */
    public Report buildReport(String json) throws IOException, HttpException
    {
        
        return gson.fromJson(rest.post("/printReportOrders", json), new TypeToken<Report>()
        {
        }.getType());
    }

    /**
     * Obtiene una lista de Configuration
     *
     * @return Lista de ConfigurationV
     * @throws IOException Error en la conexion
     * @throws HttpException Error en protocolo HTTP revisar las constantes
     */
    public List<Configuration> getConfiguration() throws IOException, HttpException
    {
        try
        {
            return gson.fromJson(rest.get("/api/configurations"), new TypeToken<List<Configuration>>()
            {
            }.getType());
        } catch (HttpException ex)
        {
            if (ex.getHttpCode() == HttpsURLConnection.HTTP_NO_CONTENT)
            {
                return new ArrayList<>(0);
            } else
            {
                throw ex;
            }
        }
    }
}
