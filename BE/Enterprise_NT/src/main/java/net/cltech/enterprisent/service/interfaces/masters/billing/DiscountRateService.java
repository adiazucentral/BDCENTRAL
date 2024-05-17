package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;

/**
 * Interfaz de servicios de los tipos de descuentos
 *
 * @version 1.0.0
 * @author javila
 * @since 23/03/2021
 * @see Creación
 */
public interface DiscountRateService
{
    /**
     * Registra un nuevo tipo de descuento en la base de datos.
     *
     * @param discountRate Instancia con los datos del tipo de descuento.
     * @return Instancia con los datos del banco.
     * @throws Exception Error en la base de datos.
     */
    public DiscountRate create(DiscountRate discountRate) throws Exception;
    
    /**
     * Actualiza la información de un tipo de descuento en la base de datos.
     *
     * @param discountRate Instancia con los datos del tipo de descuento.
     * @return Objeto del tipo de descuento modificado.
     * @throws Exception Error en la base de datos.
     */
    public DiscountRate update(DiscountRate discountRate) throws Exception;
    
    /**
     * Lista los tipos de descuento desde la base de datos.
     *
     * @return Lista de tipos de descuento.
     * @throws Exception Error en la base de datos.
     */
    public List<DiscountRate> list() throws Exception;
    
    /**
     * Obtener información de un tipo de descuento por un campo especifico.
     *
     * @param id
     * @param code
     * @param name 
     * 
     * @return Instancia con los datos del tipo de descuento.
     * @throws Exception Error en la base de datos.
     */
    public DiscountRate get(Integer id, String code, String name) throws Exception;
}
