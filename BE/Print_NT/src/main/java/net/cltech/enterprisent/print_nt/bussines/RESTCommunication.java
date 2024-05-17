/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.cltech.enterprisent.print_nt.tools.Log;

/**
 * @version 1.0
 * @author hhurtado
 * @since 19-06-2018
 * @see Creacion de clase para la comunicacion REST mediante GET POST PUT
 */
public class RESTCommunication
{

    private final String urlBase;
    private HttpURLConnection connection;

    public RESTCommunication(String urlBase)
    {
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.lastIndexOf("/")) : urlBase;
    }

    /**
     * Obtiene una respuesta por peticion POST
     *
     * @param path url
     * @param json objeto en formato json
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    public String post(String path, String json) throws IOException, HttpException
    {
        boolean parts = urlBase.contains("https");
            
        // Install the all-trusting trust manager
        try {
          
            if(parts == true)
            {
                 // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                    public X509Certificate[] getAcceptedIssuers(){return null;}
                    public void checkClientTrusted(X509Certificate[] certs, String authType){}
                    public void checkServerTrusted(X509Certificate[] certs, String authType){}
                }};
                
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            }
            
            URL url = new URL(urlBase + path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(json);
                writer.flush();
                writer.close();
                os.close();
                return getResponse();
            } catch (Exception e) {
                Log.error(e);
                return "";
            }
    }

    /**
     * Obtiene una respuesta por peticion PUT
     *
     * @param path url
     * @param json objeto en formato json
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    public String put(String path, String json) throws IOException, HttpException
    {
        URL url = new URL(urlBase + path);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(json);
        writer.flush();
        writer.close();
        os.close();
        return getResponse();
    }

    /**
     * Obtiene una respuesta por peticion GET con parametros
     *
     * @param path url
     * @param params parametros
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    public String get(String path, HashMap<String, String> params) throws IOException, HttpException
    {
        String formattedParams = UrlDataString(params);
        URL url = new URL(urlBase + path + "?" + formattedParams);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return getResponse();
    }

    /**
     * Obtiene una respuesta por peticion GET
     *
     * @param path url
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    public String get(String path) throws IOException, HttpException
    {
        URL url = new URL(urlBase + path);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return getResponse();
    }

    /**
     * Eliminacion de un recurso por DELETE
     *
     * @param path url
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    public String delete(String path) throws IOException, HttpException
    {
        String urlString = URLEncoder.encode(path, "UTF-8");
        URL url = new URL(urlBase + urlString);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("DELETE");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return getResponse();
    }

    /**
     * Obtiene la respuesta de la conexion
     *
     * @return String información
     * @throws IOException Error en la conexion
     * @throws HttpException Error HTTP
     */
    private String getResponse() throws IOException, HttpException
    {
        String response = "";
        int responseCode = connection.getResponseCode();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = br.readLine()) != null)
        {
            response += line;
        }
        if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED || responseCode == HttpsURLConnection.HTTP_NO_CONTENT)
        {
            return response;
        } else
        {
            Log.error(response);
            throw new HttpException(responseCode);
        }
    }

    /**
     * Obtiene el url para conexion
     *
     * @param params parametros del path
     *
     * @return String información
     * @throws UnsupportedEncodingException Error con la codificacion
     */
    private String UrlDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
            {
                first = false;
            } else
            {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
