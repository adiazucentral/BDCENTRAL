package net.cltech.enterprisent.tools;

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
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.cltech.enterprisent.domain.integration.resultados.DemoHeader;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.jobs.SchedulerLog;
import net.cltech.enterprisent.tools.log.patient.PatientLog;
import net.cltech.services.Encrypter;
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
    private static Encrypter encrypter;

    public static Encrypter getEncripter()
    {
        encrypter = new Encrypter("104F");
        return encrypter;

    }

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
        } else if (normalString.isEmpty())
        {
            return normalString;
        }
        SchedulerLog.info("palabra que llega " + normalString);
        //return normalString;
        return getEncripter().encrypt(normalString);
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
        } else if (encryptString.isEmpty())
        {
            return encryptString;
        }
        //return encryptString;
        return getEncripter().decrypt(encryptString);
    }
    
        /**
     * Desencripta el texto enviado de la información de los pacientes
     *
     * @param encryptString Texto encriptado
     *
     * @return Texto desencriptado
     */
    public static String decryptResult(String encryptString)
    {
        try
        {
            String resultDecrypt = "";
            if (encryptString == null || encryptString.isEmpty())
            {
                return resultDecrypt;
            }
            resultDecrypt = encrypter.decrypt(encryptString);
            return resultDecrypt;

        } catch (Exception ex)
        {
            IntegrationHisLog.error("Excepcion en función descriptar " + ex);
            return "";
        }
    }

    /**
     * Desencripta el texto enviado de la información de los pacientes
     *
     * @param encryptString Texto encriptado
     *
     * @return Texto desencriptado
     */
    public static String decryptPatient(String encryptString)
    {
        try
        {
            String resultDecrypt = "";
            if (encryptString == null || encryptString.isEmpty())
            {
                return resultDecrypt;
            }
            resultDecrypt = encrypter.decrypt(encryptString);
            return resultDecrypt;

        } catch (Exception ex)
        {
            PatientLog.error("Excepcion en función descriptar " + ex);
            return "";
        }
    }

    /**
     * *
     *
     * @param object Objeto a transformar a JSON
     *
     * @return JSON del objeto enviado.
     * @throws JsonProcessingException
     */
    public static String jsonObject(Object object) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
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
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
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

    public static String getOrderNumberYear(String Order, int digits)
    {

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);

        String yearparse = yearInString.substring(0, 4 - digits);
        return yearparse + "" + Order;
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
     * Obtiene las diferencias entre dos arreglos de demograficos
     *
     * @param array1
     * @param array2
     * @return
     */
    public static List<DemographicValue> compareDemographics(List<DemographicValue> array1, List<DemographicValue> array2) throws JsonProcessingException
    {
        List<DemographicValue> array = new ArrayList<>();
        for (int i = 0; i < array1.size(); i++)
        {
            DemographicValue e = new DemographicValue();
            int id = array1.get(i).getIdDemographic();
            e = array2.stream().filter(d -> d.getIdDemographic() == id).findAny().orElse(null);
            if (!(jsonObject(e).equals(jsonObject(array1.get(i)))))
            {
                array.add(e);
            }
        }
        return array;
    }

    /**
     * Obtiene una cadena con los demograficos a consultar de una orden
     *
     * @param demographic
     * @return Cadena con demograficos a consultar de una orden.
     */
    public static List<String> createDemographicsQuery(Demographic demographic)
    {
        List<String> querys = new ArrayList<>();
        switch (demographic.getOrigin())
        {
            case "O":
                querys = createEncodedDemographicQuery(demographic, "lab22.lab_demo_", "lab22");
                break;
            case "H":
                querys = createEncodedDemographicQuery(demographic, "Lab21.lab_demo_", "lab21");
                break;
        }
        return querys;
    }

    /**
     *
     * @param demographic
     * @param type
     * @param table
     * @return
     */
    public static List<String> createEncodedDemographicQuery(Demographic demographic, String type, String table)
    {
        StringBuilder query = new StringBuilder();
        StringBuilder from = new StringBuilder();
        if (demographic.isEncoded())
        {
            query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
            query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
            query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON {type}".replace("{type}", type)).append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
        } else
        {
            query.append(", {table}.lab_demo_".replace("{table}", table)).append(demographic.getId());
        }

        return new LinkedList<>(Arrays.asList(new String[]
        {
            query.toString(), from.toString()
        }));
    }

    public static DemographicValue getDemographicsValue(Demographic demographic, String[] data)
    {

        DemographicValue demoValue = new DemographicValue();

        demoValue.setIdDemographic(demographic.getId());
        demoValue.setDemographic(demographic.getName());
        demoValue.setEncoded(demographic.isEncoded());

        if (demographic.isEncoded())
        {
            if (data[0] != null)
            {
                demoValue.setCodifiedId(Integer.parseInt(data[0]));
                demoValue.setCodifiedCode(data[1]);
                demoValue.setCodifiedName(data[2]);
            }
        } else
        {
            demoValue.setNotCodifiedValue(data[0]);
        }

        return demoValue;
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

    /**
     * Generar UUID uncio
     *
     * @return valor unico
     */
    public static String generatePassword()
    {

        int numero;
        String numeros = "";
        for (int i = 1; i <= 5; i++)
        {
            numero = (int) (Math.random() * 50 + 1);
            numeros = numeros + "" + numero;
        }
        return encrypt(numeros);
    }

    /**
     * Arma el numero del caso
     *
     * @param date Fecha
     * @param caseDigits Digitos del caso
     * @param caseNumber Numero del caso
     * @return Caso completo del LIS
     */
    public static long getCompleteCaseNumber(String date, int caseDigits, int caseNumber)
    {
        return Long.parseLong("" + date + repeatChar("0", caseDigits - String.valueOf(caseNumber).length()) + caseNumber);
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

    /**
     * Tomamos los cuatro primeros digitos del número de la orden, entendiendo
     * que estos son referentes al número del año
     *
     * @param orderNumber
     *
     * @return Número del año
     */
    public static Integer YearOfOrder(String orderNumber)
    {
        return Integer.parseInt(orderNumber.substring(0, 4));
    }

    /*
     * Valida si una cadena de string puede ser totalmente convertida a Entero
     * 
     * @param str cadena a validar
     * @return True - si es posible castear esa cadena en un entero
     * False - No se puede castear esta cadena en un entero
     */
    public static boolean isInteger(String str)
    {
        if (str == null)
        {
            return false;
        }
        int length = str.length();
        if (length == 0)
        {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-')
        {
            if (length == 1)
            {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++)
        {
            char c = str.charAt(i);
            if (c < '0' || c > '9')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Crea una orden inicial de referencia
     *
     * @param orderDigits Digitos de orden
     * @param days Dias hacia atras para generar la orden
     * @return
     */
    public static long createInitialOrder(int orderDigits, int days)
    {
        Calendar order = Calendar.getInstance();
        order.setTime(new Date());
        int year = order.get(Calendar.DAY_OF_YEAR) < days ? order.get(Calendar.YEAR) - 1 : order.get(Calendar.YEAR);
        int month = (order.get(Calendar.DAY_OF_YEAR) < days ? 12 - order.get(Calendar.MONTH - 2) + 1 : order.get(Calendar.MONTH) - 1) + 1;
        int day = order.get(Calendar.DAY_OF_MONTH);
        String orderI = "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day);
        orderI += Tools.repeatChar("0", orderDigits - 1) + "1";
        return Long.parseLong(orderI);
    }

    /**
     * Crea una orden final de referencia
     *
     * @param orderDigits Digitos de orden
     * @param days Dias hacia atras para generar la orden
     * @return
     */
    public static long createFinalOrder(int orderDigits, int days)
    {
        Calendar order = Calendar.getInstance();
        order.setTime(new Date());
        int year = order.get(Calendar.DAY_OF_YEAR) < days ? order.get(Calendar.YEAR) - 1 : order.get(Calendar.YEAR);
        int month = order.get(Calendar.MONTH) + 1;
        int day = order.get(Calendar.DAY_OF_MONTH);
        String orderF = "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day);
        orderF += Tools.repeatChar("9", orderDigits - 1) + "9";
        return Long.parseLong(orderF);
    }

    public static long formatOrder(Long order, int digitsOrder) throws Exception
    {
        System.out.println("[Digitos Orden] : " + digitsOrder);
        String month = (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10) ? "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1) : "" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        String day = (Calendar.getInstance().get(Calendar.DATE) < 10) ? "0" + Calendar.getInstance().get(Calendar.DATE) : "" + Calendar.getInstance().get(Calendar.DATE);
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        String orderFormat = String.valueOf(order);

        int orderR = orderFormat.length() - digitsOrder;
        if (orderR > 0)
        {
            switch (orderR)
            {
                case 1:
                    orderFormat = year + month + "0" + order;
                    break;
                case 2:
                    orderFormat = year + month + order;
                    break;
                case 3:
                    orderFormat = year + "0" + order;
                    break;
                case 4:
                    orderFormat = year + order;
                    break;
                case 5:
                    orderFormat = year.substring(0, 3) + order;
                    break;
                case 6:
                    orderFormat = year.substring(0, 2) + order;
                    break;
                case 7:
                    orderFormat = year.substring(0, 1) + order;
                    break;
                default:
            }
            return Long.parseLong(orderFormat);
        } else
        {
            return Long.parseLong(year + month + day + order);
        }
    }

    //metodos perimten crear un a orden inicial y un afinal dado un dia 
    /**
     * Arma el numero de la orden desde la fecha actual tantos dias hacia tras
     * pasados
     *
     * @param date Fecha en formato yyyyMMdd
     * @param orderDigits Digitos de la orden
     * @param orderNumber Numero de la orden
     * @return Orden completa del LIS
     */
    public static long buildInitialOrder(int days, int orderDigits)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        Integer orderStart = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(c.getTime()));
        return getCompleteOrderNumber(orderStart, orderDigits);
    }

    public static long buildFinalOrder(int orderDigits)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        Integer orderStart = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(c.getTime()));
        return getCompleteOrderNumber(orderStart, orderDigits);

    }

    /**
     * Arma el numero de la orden
     *
     * @param date Fecha en formato yyyyMMdd
     * @param orderDigits Digitos de la orden
     * @param orderNumber Numero de la orden
     * @return Orden completa del LIS
     */
    public static long getCompleteOrderNumber(int date, int orderDigits)
    {
        return Long.parseLong("" + date + repeatChar("0", orderDigits));
    }
    
    public static DemoHeader getDemographicHeader(Demographic demographic, String[] data)
    {
        DemoHeader demoValue = new DemoHeader();
        demoValue.setId(demographic.getId());
        demoValue.setName(demographic.getName());
        if( demographic.getId() > -1 ) {
            if (demographic.isEncoded())
            {
                if (data[0] != null)
                {
                    demoValue.setIdItem(Integer.parseInt(data[0]));
                    demoValue.setValue(data[2]);
                }
            } else
            {
                demoValue.setValue(data[0]);
            }
        } else {
            if(data[0] != null) {
                demoValue.setIdItem(Integer.parseInt(data[0]));
                demoValue.setValue(data[2]);
            }
            
        }
        
        return demoValue;
    }
}
