/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintLog;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Funcionalidades varias
 *
 * @author dcortes
 *
 * @version 1.1.0
 * @author Eacuna
 * @since 07/07/2015
 * @see ENTTS-281 metodo para calcular la edad
 */
public class Tools
{

    /**
     * Da formato de lis a la orden enviada la retorna en formato (yyyyMMddXXXX)
     *
     * @param order Orden sin formato
     * @param digits numero de digitos de la orden
     * @return Orden con formato LIS
     */
    public static String formatOrderComplete(String order, int digits)
    {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if (order.length() == (digits + 3))
        { //Orden sin año y con solo un digito del mes
            return year + "0" + order;
        } else if (order.length() == (digits + 4))
        { //Orden sin año con dos digitos del mes
            return year + order;
        } else if (order.length() == (digits + 5))
        { //Orden con un digito del año
            return year.substring(0, 3) + order;
        } else if (order.length() == (digits + 6))
        { //Orden con dos digitos del año
            return year.substring(0, 2) + order;
        } else if (order.length() == (digits + 7))
        { //Orden con tres digitos del año
            return year.substring(0, 1) + order;
        } else if (order.length() == (digits + 8))
        { //Orden con cuatro digitos del año
            return order;
        } else if (order.length() <= digits)
        {
            String x = repeatChars("0", digits - order.length()) + order;
            String month = (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10) ? "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1) : "" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
            String day = (Calendar.getInstance().get(Calendar.DATE) < 10) ? "0" + Calendar.getInstance().get(Calendar.DATE) : "" + Calendar.getInstance().get(Calendar.DATE);
            return year + month + day + x;
        } else
        {
            return order;
        }
    }

    /**
     * Da formato de lis a la orden enviada la retorna en formato (MMddXXXX)
     *
     * @param order Orden sin formato
     * @param digits Digitos de la orden
     * @return Orden con formato (MMddXXXX)
     */
    public static String getFormatOrder(String order, int digits)
    {
        String orderComplete = Tools.formatOrderComplete(order, digits);
        orderComplete = orderComplete.substring(4).trim();
        orderComplete = String.valueOf(Long.parseLong(orderComplete));
        return orderComplete;
    }

    /**
     * Obtiene la orden completa agregandole el año
     *
     * @param order ORden
     * @param year Año
     * @param digits Digitos de la orden
     * @return
     */
    public static String getFormatComplete(String order, int year, int digits)
    {
        if (order.length() < (digits + 4))
        {
            return "" + year + "0" + order;
        } else
        {
            return "" + year + order;
        }
    }

    /**
     * Repite un caracter la veces especificadas
     *
     * @param chart Caracter
     * @param spaces Repeticiones
     * @return Cadena de caracteres repetidos
     */
    public static String repeatChars(String chart, int spaces)
    {
        int i = 0;
        String string = "";
        while (i < spaces)
        {
            string += chart;
            i++;
        }
        return string;
    }

    /**
     * Calcula la edad del paciente
     *
     * @param birthDay fecha de nacimiento
     * @return
     * @throws Exception
     */
    public static String getAge(java.util.Date birthDay) throws Exception

    {
        Calendar fechaActual = Calendar.getInstance();
        Date date = new Date();
        fechaActual.setTime(date);
        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.setTime(birthDay);
        fechaActual.setLenient(true);
        fechaInicio.setLenient(true);
        int diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mesActual = fechaActual.get(Calendar.MONTH) + 1;
        int anioActual = fechaActual.get(Calendar.YEAR);
        int diaInicio = fechaInicio.get(Calendar.DAY_OF_MONTH);
        int mesInicio = fechaInicio.get(Calendar.MONTH) + 1;
        int anioInicio = fechaInicio.get(Calendar.YEAR);
        int b = 0;
        int mes = mesInicio;
        if (mes == 2)
        {
            if ((anioActual % 4 == 0 && anioActual % 100 != 0) || anioActual % 400 == 0)
            {
                b = 29;
            } else
            {
                b = 28;
            }
        } else if (mes <= 7)
        {
            if (mes == 0)
            {
                b = 31;
            } else if (mes % 2 == 0)
            {
                b = 30;
            } else
            {
                b = 31;
            }
        } else if (mes > 7)
        {
            if (mes % 2 == 0)
            {
                b = 31;
            } else
            {
                b = 30;
            }
        }
        int anios = -1;
        int meses = -1;
        int dies = -1;

        if ((anioInicio > anioActual) || (anioInicio == anioActual && mesInicio > mesActual)
                || (anioInicio == anioActual && mesInicio == mesActual && diaInicio > diaActual))
        {
            return "";
        } else
        {
            if (mesInicio <= mesActual)
            {
                anios = anioActual - anioInicio;
                if (diaInicio <= diaActual)
                {
                    meses = mesActual - mesInicio;
                    dies = diaActual - diaInicio;
                } else
                {
                    if (mesActual == mesInicio)
                    {
                        anios = anios - 1;
                    }
                    meses = (mesActual - mesInicio - 1 + 12) % 12;
                    dies = b - (diaInicio - diaActual);
                }
            } else
            {
                anios = anioActual - anioInicio - 1;
                if (diaInicio > diaActual)
                {
                    meses = mesActual - mesInicio - 1 + 12;
                    dies = b - (diaInicio - diaActual);
                } else
                {
                    meses = mesActual - mesInicio + 12;
                    dies = diaActual - diaInicio;
                }
            }
        }
        return anios + "." + meses + "." + dies;
    }

    /**
     * llave de configuracion para validar si desea guardar losg del cleinte de
     * impresion por correo enviado crearArchivoPorEmail 0->no activa, 1->activa
     * solo para guardar en base de datos. 2->activo crear logs de todas las
     * sedes
     *
     * @param birthDay fecha de nacimiento
     * @return
     * @throws Exception
     */
    public static int createFileByEmail(String PATH)
    {
        try
        {
            String url = PATH + "api/configuration/not/crearArchivoPorEmail";
            //crearArchivoPorEmail 0->no activa,1->activa solo para guardar en base de datos.2->activo crear logs de todas las sedes 
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> ordess = restTemplate.getForEntity(url, String.class);

            return Integer.parseInt(ordess.getBody());

        } catch (Exception e)
        {
            return 0;
        }

    }

    /**
     * Guardad log de cliente en base de datos
     *
     * @param PATH ruta NT
     * @param print OBJAETO A PERSITIR
     * @return
     * @throws Exception
     */
    public static boolean saveLogPrint(String PATH, PrintLog print)
    {
        try
        {
            String url = PATH + "api/printLog";
            RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<PrintLog> entity = new HttpEntity<PrintLog>(print, headers);
            return restTemplate.postForObject(url, entity, Boolean.class);
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * consulta los logs guardados en base de datos de los ultimos 10 dias
     *
     * @param PATH ruta NT
     * @return lista PrintLog objetos guadados en base de datos
     * @throws Exception
     */
    public static List<PrintLog> listLogsPrint(String PATH)
    {
        try
        {
            String url = PATH + "api/printLog";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PrintLog[]> logsPrint = restTemplate.getForEntity(url, PrintLog[].class);
            List<PrintLog> logsPrints = new ArrayList<>(Arrays.asList(logsPrint.getBody()));
            return logsPrints;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

}
