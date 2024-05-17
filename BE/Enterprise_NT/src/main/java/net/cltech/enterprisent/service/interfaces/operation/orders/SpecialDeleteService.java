/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderBasic;

/**
 * Interfaz de servicios a la informacion de borrados especiales.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 25/10/2017
 * @see Creación
 */
public interface SpecialDeleteService
{

    /**
     * Realiza el borrado especial por razon de ordenes.
     *
     * @param reason Razon por la cual se realizo el borrado especial.
     *
     * @return Lista de Ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> specialDelete(Reason reason) throws Exception;

    /**
     * Realiza la consulta de cuantas ordenes de van a ver afectadas por los
     * borrados especiales.
     *
     * @param reason Razon por la cual se va realizar el borrado especial.
     *
     * @return Numero de Ordenes Afectadas.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderBasic> specialDeleteTest(Reason reason) throws Exception;
    
         /**
     * Realiza el borrado especial por razon de ordenes.
     *
     * @param reason Razon por la cual se realizo el borrado especial.
     *
     * @return Lista de Ordenes.
     * @throws Exception Error en la base de datos.
     */
    public Integer specialDeletebyorder(Reason reason) throws Exception;

}
