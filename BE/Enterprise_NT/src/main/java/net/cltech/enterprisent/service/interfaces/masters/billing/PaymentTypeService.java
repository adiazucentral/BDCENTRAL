package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;

/**
 * Interfaz de servicios Tipos de Pago
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/08/2017
 * @see Creación
 */
public interface PaymentTypeService
{

    /**
     * Lista tipos de pago desde la base de datos.
     *
     * @return Lista tipos de pago.
     * @throws Exception Error en la base de datos.
     */
    public List<PaymentType> list() throws Exception;

    /**
     * Registra tipo de pago.
     *
     * @param type Instancia con los datos de tipo de pago.
     *
     * @return Instancia con los datos de tipo de pago.
     * @throws Exception Error en la base de datos.
     */
    public PaymentType create(PaymentType type) throws Exception;

    /**
     * Obtener información tipo de pago por un campo especifico.
     *
     * @param id ID de tipo de pago a ser consultada.
     *
     * @return Instancia con los datos de tipo de pago.
     * @throws Exception Error en la base de datos.
     */
    public PaymentType findById(Integer id) throws Exception;

    /**
     * Obtener información tipo de pago por un campo especifico.
     *
     * @param name de tipo de pago .
     *
     * @return Instancia con los datos de tipo de pago.
     * @throws Exception Error en la base de datos.
     */
    public PaymentType findByName(String name) throws Exception;
    
    /**
     * Obtener información tipo de pago por un campo especifico.
     *
     * @param code
     *
     * @return Instancia con los datos de tipo de pago.
     * @throws Exception Error en la base de datos.
     */
    public PaymentType findByCode(String code) throws Exception;

    /**
     * Actualiza la información tipo de pago en la base de datos.
     *
     * @param type Instancia con los datos de tipo de pago.
     *
     * @return Objeto de tipo de pago modificada.
     * @throws Exception Error en la base de datos.
     */
    public PaymentType update(PaymentType type) throws Exception;

    /**
     * Obtener información tipo de pago por estado.
     *
     * @param state Estado
     *
     * @return Instancia con los datos de tipo de pago.
     * @throws Exception Error en la base de datos.
     */
    public List<PaymentType> list(boolean state) throws Exception;

}
