package net.cltech.enterprisent.service.interfaces.masters.common;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Holiday;

/**
 * Interfaz de servicios a la informacion del maestro Tipo de Documento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
 * @see Creación
 */
public interface HolidayService
{
    /**
     * Lista los festivos desde la base de datos.
     *
     * @return Lista de festivos.
     * @throws Exception Error en la base de datos.
     */
    public List<Holiday> list() throws Exception;
    
    /**
     * Lista los festivos desde la base de datos.
     *
     * @return Lista de festivos.
     * @throws Exception Error en la base de datos.
     */
    public List<String> listBasic() throws Exception;
    
    /**
     * Registra uno nuevo festivo en la base de datos.
     *
     * @param holiday Instancia con los datos del festivo.
     *
     * @return Instancia con los datos del festivo.
     * @throws Exception Error en la base de datos.
     */
    public Holiday create(Holiday holiday) throws Exception;
    
    /**
     * Obtener información de un festivo por una campo especifico.
     *
     * @param id ID del festivo a ser consultado.
     * @param name Nombre del festivo a ser consultado.
     * @param date Fecha del festivo a ser consultado.
     *
     * @return Instancia con los datos del festivo.
     * @throws Exception Error en la base de datos.
     */
    public Holiday get(Integer id, String name, Date date) throws Exception;
    
    /**
     * Actualiza la información de una festivo en la base de datos.
     *
     * @param holiday Instancia con los datos de la festivo.
     *
     * @return Objeto de la festivo modificado.
     * @throws Exception Error en la base de datos.
     */
    public Holiday update(Holiday holiday) throws Exception;
    
    /**
     *
     * Elimina un festivo de la base de datos.
     *
     * @param id ID del festivo.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de festivos por estado.
     *
     * @param state Estado de los festivos a ser consultados
     *
     * @return Instancia con los datos del festivo.
     * @throws Exception Error en la base de datos.
     */
    public List<Holiday> list(boolean state) throws Exception;
}
