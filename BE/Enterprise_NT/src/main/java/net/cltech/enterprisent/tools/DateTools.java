package net.cltech.enterprisent.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilidades de fechas para la aplicación
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 26/04/2017
 * @see Creación
 */
public class DateTools
{

    /**
     * Obtiene la fecha sin horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static Date getInitialDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }
    
    /**
     * Obtiene la fecha sin horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static Date numberToDate(int date)
    {
        SimpleDateFormat dateNumber = new SimpleDateFormat("yyyyMMdd");
        try
        {
            return dateNumber.parse(String.valueOf(date));
        } catch (ParseException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene la fecha con horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo al limite del dia (23:59:59)
     */
    public static Date getFinalDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * Obtiene la fecha sin horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static Date getDateWithoutTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Obtiene la fecha sin horas, minutos y segundos y con formato yyyyMMdd
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static int dateToNumber(Date date)
    {
        return Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date));
    }

    /**
     * Obtiene el año de la fecha
     *
     * @param date fecha a convertir
     *
     * @return fecha yyyy
     */
    public static int dateToNumberYear(Date date)
    {
        return Integer.valueOf(new SimpleDateFormat("yyyy").format(date));
    }

    public static Period getAge(LocalDate dob, LocalDate now)
    {
        return Period.between(dob, now);
    }

    public static long getAgeInDays(LocalDate dob, LocalDate now)
    {
        return ChronoUnit.DAYS.between(dob, now);
    }

    public static long getAgeInMonths(LocalDate dob, LocalDate now)
    {
        return ChronoUnit.MONTHS.between(dob, now);
    }

    public static long getAgeInYears(LocalDate dob, LocalDate now)
    {
        return ChronoUnit.YEARS.between(dob, now);
    }

    public static LocalDate localDate(Date date)
    {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime localDateTime(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Calculo de los minutos transcurridos
     *
     * @param init fecha inicial
     * @param end fecha final
     * @return
     */
    public static long getElapsedMinutes(LocalDateTime init, LocalDateTime end)
    {
        return Duration.between(init, end).toMinutes();
    }

    /**
     * Retorna la diferencia en minutos de dos fechas
     *
     * @param init Fecha Inicial
     * @param end Fecha Final
     * @return Diferencia en minutos
     */
    public static long getElapsedMinutes(Date init, Date end)
    {
        return Duration.between(DateTools.localDateTime(init), DateTools.localDateTime(end)).toMinutes();
    }

    /**
     * Retorna la diferencia en dias de dos fechas
     *
     * @param init Fecha Inicial
     * @param end Fecha Final
     * @return Diferencia en minutos
     */
    public static long getElapsedDays(Date init, Date end)
    {
        return Duration.between(DateTools.localDateTime(init), DateTools.localDateTime(end)).toDays();
    }

    /**
     * Obtiene la fecha
     *
     * @param localDate
     * @return
     */
    public static Date asDate(LocalDate localDate)
    {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Sumar o restar tiempo de una fecha
     *
     * @param dateToChange
     * @param type
     * @param time
     * @return
     */
    public static Date changeDate(Date dateToChange, int type, int time)
    {
        Calendar date = Calendar.getInstance();
        date.setTime(dateToChange);
        date.add(type, time);
        return date.getTime();
    }

    /**
     * Obtiene la fecha sin día. Solo año y mes (yyyyMM)
     *
     * @param date fecha a convertir
     * @return fecha formato yyyyMM
     */
    public static String dateToString(Date date)
    {
        return String.valueOf(new SimpleDateFormat("yyyyMM").format(date));
    }

    /**
     * Obtiene la fecha con formato: yyyy-MM-dd
     *
     * @param date fecha a convertir
     * @return fecha formato yyyy-MM-dd
     */
    public static String dateFormatyyyy_MM_dd(Date date)
    {
        return String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    /**
     * Obtiene la fecha con formato: yyyy-MM-dd
     *
     * @param date fecha a convertir
     * @return fecha formato yyyy-MM-dd
     */
    public static String dateFormatyyyy_MM_dd_hh_mm_ss(Date date)
    {
        return String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date));
    }

    /**
     * Obtiene la fecha con formato: ddMMyyyy
     *
     * @param date fecha a convertir
     * @return fecha formato ddMMyyyy
     */
    public static String dateFormatddMMyyyy(Date date)
    {
        return String.valueOf(new SimpleDateFormat("ddMMyyyy").format(date));
    }

    /**
     * Obtiene la cantidad de Dias, Semana o Hora por los minutos enviados
     *
     * @param minute
     * @param isDay
     * @param isWeek
     * @param isHour
     * @return fecha cantidad de datos
     */
    public static int calculationOfTimes(int minute, boolean isDay, boolean isWeek, boolean isHour)
    {
        int count = 0;
        if (isDay)
        {
            count = minute / 1440; //Dias
        } else if (isWeek)
        {
            count = minute / 10080; //Semana
        } else if (isHour)
        {
            count = minute / 60; // Hora
        }
        return count;
    }
}
