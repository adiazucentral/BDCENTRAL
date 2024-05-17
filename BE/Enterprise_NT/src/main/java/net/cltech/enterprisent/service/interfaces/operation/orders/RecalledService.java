/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Recalled;

/**
 * Servicios sobre rellamado de ordenes
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/08/2019
 * @see Creacion
 */
public interface RecalledService
{

    /**
     * Obtiene un listado de rellamados de ordenes
     *
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public List<Recalled> list() throws Exception;

    /**
     * Crea registro de rellamado
     *
     * @param recalled
     * {@link net.cltech.enterprisent.domain.operation.orders.Recalled}
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Recalled},
     * vacio en caso de no crearlo
     * @throws Exception Error en el servicio
     */
    public Recalled create(Recalled recalled) throws Exception;

}
