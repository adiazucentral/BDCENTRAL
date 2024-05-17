package net.cltech.enterprisent.dao.interfaces.exception;

import java.sql.Timestamp;
import java.util.List;
import net.cltech.enterprisent.domain.exception.WebException;

/**
 * Interfaz con los metodos de acceso a datos para el registro y consulta de
 * errores
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Creacion
 */
public interface ExceptionDao
{

    /**
     * Inserta un nuevo error en la tabla de errores
     *
     * @param exception
     * {@link net.cltech.enterprisent.domain.exception.WebException}
     * @return {@link net.cltech.enterprisent.domain.exception.WebException}
     * generado con fecha y id
     * @throws Exception Error presentado en base de datos
     */
    public WebException insert(WebException exception) throws Exception;

    /**
     * Obtiene todos los errores registrados en el rango de fechas enviado
     *
     * @param initialDate Fecha inicial
     * @param finalDate Fecha final
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.exception.WebException}
     * @throws Exception Error presentado en base de datos
     */
    public List<WebException> get(Timestamp initialDate, Timestamp finalDate) throws Exception;

    /**
     * Obtiene todos los errores registrados en un rango de fechas y filtrando
     * por el tipo de error
     *
     * @param initialDate Fecha inicial
     * @param finalDate Fecha final
     * @param type 0->Error en base de datos, 1->Error no controlador
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.exception.WebException}
     * @throws Exception Error en base de datos
     */
    public List<WebException> get(Timestamp initialDate, Timestamp finalDate, int type) throws Exception;
}
