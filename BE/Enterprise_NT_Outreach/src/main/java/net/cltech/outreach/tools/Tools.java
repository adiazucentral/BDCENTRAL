/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.aston.services.Encrypter;
import org.springframework.http.MediaType;

/**
 * Metodos de utilidad para toda la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 21/04/2017
 * @see Creación
 */
public class Tools
{

    /**
     * Representa el tipo de dato json en codificacion utf-8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    /**
     * Objeto de encriptacion
     */
    private static final Encrypter encrypter = new Encrypter("104F");

    /**
     * Encripta el texto enviado
     *
     * @param normalString Texto para encriptar
     *
     * @return Tiempo encriptado
     */
    public static String encrypt(String normalString)
    {
        if (normalString == null)
        {
            return null;
        }
        return encrypter.encrypt(normalString);
    }

    /**
     * Desencripta el texto enviado
     *
     * @param encryptString Texto encriptado
     *
     * @return Texto desencriptado
     */
    public static String decrypt(String encryptString)
    {
        if (encryptString == null)
        {
            return null;
        }
        return encrypter.decrypt(encryptString);
    }

    /**
     * *
     *
     * @param object Ojeto a transformar a JSON
     *
     * @return JSON del objeto enviado.
     * @throws JsonProcessingException
     */
    public static String jsonObject(Object object) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(object);
        return jsonContent;
    }

    /**
     * *
     *
     * @param <T> Objeto transformado del JSON enviado.
     * @param content JSON
     * @param valueType Clase a la cual se quiere transformar el JSON.
     *
     * @return Objeto transformado del JSON enviado.
     */
    public static <T> T jsonObject(String content, Class<T> valueType)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(content, valueType);
        } catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * *
     *
     * @param <T> Objeto transformado del JSON enviado.
     * @param content JSON
     * @param valueType Clase a la cual se quiere transformar el JSON.
     *
     * @return Objeto transformado del JSON enviado.
     */
    public static <T> List<T> jsonList(String content, Class<T> valueType)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Arma el numero de la orden
     *
     * @param date Fecha en formato yyyyMMdd
     * @param orderDigits Digitos de la orden
     * @param orderNumber Numero de la orden
     * @return Orden completa del LIS
     */
    public static long getCompleteOrderNumber(int date, int orderDigits, int orderNumber)
    {
        return Long.parseLong("" + date + repeatChar("0", orderDigits - String.valueOf(orderNumber).length()) + orderNumber);
    }

    /**
     * Repite un caracter las veces enviadas
     *
     * @param chars Caracter a repetir
     * @param repeat Veces a repetir el caracter
     * @return String resultado
     */
    public static String repeatChar(String chars, int repeat)
    {
        String repeatString = "";
        for (int i = 0; i < repeat; i++)
        {
            repeatString += chars;
        }
        return repeatString;
    }

    public static byte[] loadFileAsBytesArray(String fileName) throws Exception
    {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;
    }

    public static void createFile(String encodedBytes, String filePath)
    {

        try
        {
            byte[] decodedBytes;
            FileOutputStream fop;
            decodedBytes = Base64.getDecoder().decode(encodedBytes);
            File file = new File(filePath);
            fop = new FileOutputStream(file);
            fop.write(decodedBytes);
            fop.flush();
            fop.close();
            System.out.println("Created");
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el numero de orden con el numero de digitos del año enviados
     *
     * @param orderNumber numero de la orden
     * @param digits numero de digitos del año
     * @return orden con nuevo formato
     */
    public static String getOrderNumberToPrint(Long orderNumber, int digits)
    {
        if (digits <= 4)
        {
            return orderNumber.toString().substring(4 - digits);
        } else
        {
            return orderNumber.toString();
        }
    }

    /**
     * Genera una lista con los años de busqueda de manera consecutiva
     *
     * @param start año con el que se iniciara
     * @param end año con el que se finalizara
     *
     * @return Lista con los años de busqueda
     */
    public static List<Integer> listOfConsecutiveYears(String start, String end)
    {
        Integer startYear;
        Integer endYear;
        List<Integer> years = new ArrayList<>();

        startYear = Integer.parseInt(start.substring(0, 4));
        endYear = Integer.parseInt(end.substring(0, 4));

        int yearTraveled = startYear;
        while (yearTraveled <= endYear)
        {
            years.add(yearTraveled);
            yearTraveled++;
        }

        return years;
    }
}
