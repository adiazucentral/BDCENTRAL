/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
            if (content != null && !content.trim().isEmpty())
            {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
                return mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
            } else
            {
                return new ArrayList<>();
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
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
     * Obtiene la edad en años
     *
     * @param dob fecha de nacimiento
     * @param now fecha actual
     * @return edad en años
     */
    public static long getAgeInYears(LocalDate dob, LocalDate now)
    {
        return ChronoUnit.YEARS.between(dob, now);
    }

    /**
     * Convierte de Date a LocalDate
     *
     * @param date date
     * @return LocalDAte
     */
    public static LocalDate localDate(Date date)
    {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Obtiene un rango de fechas con formato timestamp
     *
     * @param initialDate
     * @param finalDate
     * @return
     */
    public static List<Timestamp> rangeDates(Integer initialDate, Integer finalDate)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date i = sdf.parse(String.valueOf(initialDate) + " 00:00:00");
            Date f = sdf.parse(String.valueOf(finalDate) + " 23:59:59");
            List<Timestamp> times = new ArrayList<>(0);
            times.add(new Timestamp(i.getTime()));
            times.add(new Timestamp(f.getTime()));
            return times;
        } catch (ParseException ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un rango de fechas con formato timestamp
     *
     * @param initialDate
     * @param finalDate
     * @return
     */
    public static List<Timestamp> rangeDates(String initialDate, String finalDate)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date i = sdf.parse(initialDate);
            Date f = sdf.parse(finalDate);
            List<Timestamp> times = new ArrayList<>(0);
            times.add(new Timestamp(i.getTime()));
            times.add(new Timestamp(f.getTime()));
            return times;
        } catch (ParseException ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Calcular edad en años apartir de una fecha
     *
     * @param birthday
     * @return años
     */
    public static long calculateAge(Date birthday)
    {
        return DateTools.getAgeInYears(Instant.ofEpochMilli(birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
    }

    /**
     * Generar UUID uncio
     *
     * @return valor unico
     */
    public static String generateUUID()
    {
        return String.valueOf(UUID.randomUUID());
    }
}
