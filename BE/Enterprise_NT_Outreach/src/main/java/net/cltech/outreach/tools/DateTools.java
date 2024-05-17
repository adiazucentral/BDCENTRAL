package net.cltech.outreach.tools;

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
     * Obtiene la fecha sin horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static int dateToNumber(Date date)
    {
        SimpleDateFormat dateNumber = new SimpleDateFormat("yyyyMMdd");
        return Integer.valueOf(dateNumber.format(date));
    }
    
    /**
     * Obtiene la fecha sin horas, minutos y segundos
     *
     * @param date fecha a convertir
     *
     * @return fecha con horas, minutos y segundo a cero
     */
    public static int dateToNumberYear(Date date)
    {
        SimpleDateFormat dateNumber = new SimpleDateFormat("yyyy");
        return Integer.valueOf(dateNumber.format(date));
    }

    public static Period getAge(LocalDate dob, LocalDate now)
    {
        Period period = Period.between(dob, now);
        return period;
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
//        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
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
        LocalDateTime initTime = DateTools.localDateTime(init);
        LocalDateTime endTime = DateTools.localDateTime(end);
        return Duration.between(initTime, endTime).toMinutes();
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
        LocalDateTime initTime = DateTools.localDateTime(init);
        LocalDateTime endTime = DateTools.localDateTime(end);
        return Duration.between(initTime, endTime).toDays();
    }
}
