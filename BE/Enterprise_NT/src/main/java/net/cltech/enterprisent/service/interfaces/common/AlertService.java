package net.cltech.enterprisent.service.interfaces.common;

import java.util.List;
import net.cltech.enterprisent.domain.common.Alert;

/**
 * Interfaz de servicios a la informacion
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/09/2017
 * @see Creación
 */
public interface AlertService
{

    /**
     * Lista alertas de un formulario
     *
     * @param form id formulario
     *
     * @return Modulos.
     * @throws Exception Error en la base de datos.
     */
    public List<Alert> list(String form) throws Exception;

    /**
     * Adiciona alert al usuario.
     *
     * @param alert
     *
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int add(Alert alert) throws Exception;

    /**
     * Elimina alert al usuario.
     *
     * @param alert
     *
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int delete(Alert alert) throws Exception;
}
