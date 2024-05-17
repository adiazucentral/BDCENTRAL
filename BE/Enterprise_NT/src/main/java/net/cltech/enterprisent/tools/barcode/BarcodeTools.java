/*
 * To change this license header, choose License Headers in Project HashMap.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools.barcode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeOrder;
import net.cltech.enterprisent.tools.Constants;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 26/09/2017
 * @see Creación
 */
public class BarcodeTools
{

    public static String sampleBarcode(String file, BarcodeOrder order) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
    {
        URLClassLoader loader = null;
        try
        {
            loader = new URLClassLoader(new URL[]
            {
                new URL("file:///" + file)
            }, BarcodeTools.class.getClassLoader());
            Class<?> clazz = Class.forName("net.cltech.printerservice.BarcodePrinter", true, loader);
            Method barcode = Stream.of(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals("barcode"))
                    .findFirst()
                    .orElse(null);
            Object obj = clazz.newInstance();

            Object ret = barcode.invoke(obj, order.getConfiguration(), order.getYearDigits(), order.getOrder(), order.getSampleCode(), order.getSampleName(), order.getSampleRecipient(), order.getSeparator(), order.isPrintSample(), order.getDemos(), "", "");
            return ret.toString();
        } finally
        {
            if (loader != null)
            {
                loader.close();
            }
        }
    }

    public static String sampleAddLabel(String file, BarcodeOrder order) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        URLClassLoader loader = new URLClassLoader(new URL[]
        {
            new URL("file:///" + file)
        }, BarcodeTools.class.getClassLoader());
        Class<?> clazz = Class.forName("net.cltech.printerservice.BarcodePrinter", true, loader);
        Method barcode = Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("additionalLabel"))
                .findFirst()
                .orElse(null);
        Object obj = clazz.newInstance();

        Object ret = barcode.invoke(obj, order.getConfiguration(), order.getYearDigits(), order.getOrder(), order.getDemos(), order.getTests().stream().collect(Collectors.joining(", ")), order.getAreas().stream().collect(Collectors.joining(", ")));
        return ret.toString();

    }

    /**
     * Mapea bean de la orden a un map para la etiqueta de barras
     *
     * @param order Informacion de la orden
     * @param dateFormat formato de fecha
     *
     * @return
     */
    public static HashMap orderToHashMap(Order order, String dateFormat)
    {
        HashMap info = new HashMap();
        info.put("" + Constants.PATIENT_LAST_NAME, order.getPatient().getLastName());
        info.put("" + Constants.PATIENT_SURNAME, order.getPatient().getSurName());
        info.put("" + Constants.PATIENT_NAME, order.getPatient().getName1());
        info.put("" + Constants.PATIENT_SECOND_NAME, order.getPatient().getName2());
        info.put("" + Constants.PATIENT_SEX, order.getPatient().getSex().getCode());
        info.put("" + Constants.PATIENT_BIRTHDAY, new SimpleDateFormat(dateFormat).format(order.getPatient().getBirthday()));
        info.put("" + Constants.ORDERTYPE, order.getType().getCode() + "." + order.getType().getName());
        info.put("" + Constants.ACCOUNT, order.getAccount().getName());
        info.put("" + Constants.BRANCH, order.getBranch().getName());
        info.put("" + Constants.DOCUMENT_TYPE, order.getPatient().getDocumentType().getAbbr());
        info.put("" + Constants.PHYSICIAN, order.getPhysician().getName());
        info.put("" + Constants.RACE, order.getPatient().getRace().getName());
        info.put("" + Constants.RATE, order.getRate().getName());
        info.put("" + Constants.SERVICE, order.getService().getName());

        //demograficos
        order.getAllDemographics().forEach(demographic ->
        {
            info.put("" + demographic.getIdDemographic(), demographic.getValue());
        });
        return info;

    }

    /**
     * Imprime código de barra para la gradilla
     *
     * @param file nombre del archivo
     * @param code C+odigo de la gradilla
     * @param name Nombre de la gradilla
     * @return EPL para imprimir código de barras
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public static String rackBarcode(String file, String code, String name) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
    {
        URLClassLoader loader = null;
        try
        {
            loader = new URLClassLoader(new URL[]
            {
                new URL("file:///" + file)
            }, BarcodeTools.class.getClassLoader());
            Class<?> clazz = Class.forName("net.cltech.printerservice.BarcodePrinter", true, loader);
            Method barcode = Stream.of(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals("barcodeRack"))
                    .findFirst()
                    .orElse(null);
            Object obj = clazz.newInstance();

            Object ret = barcode.invoke(obj, code, name, new HashMap<String, String>());
            return ret.toString();
        } finally
        {
            if (loader != null)
            {
                loader.close();
            }
        }
    }

}
