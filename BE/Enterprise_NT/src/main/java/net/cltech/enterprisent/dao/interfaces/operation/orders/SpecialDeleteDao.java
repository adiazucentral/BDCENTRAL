/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * borrados especiales.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 25/10/2017
 * @see Creación
 */
public interface SpecialDeleteDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Anula ordenes por rango.
     *
     * @param orders Lista de Ordenes a ser anualadas.
     *
     * @return listado ordenes eliminadas
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Order> deleteOrder(List<Order> orders) throws Exception
    {
        List<Object[]> parameters = new ArrayList<Object[]>();

        String query = "UPDATE lab22 SET lab07c1 = ? WHERE lab22c1 = ?";

        for (Order order : orders)
        {
            parameters.add(new Object[]
            {
                LISEnum.ResultOrderState.CANCELED.getValue(),
                order.getOrderNumber()
            });
            order.setState(LISEnum.ResultOrderState.CANCELED.getValue());
        }
        getConnection().batchUpdate(query, parameters);

        return orders;
    }

    /**
     * Elimina examenes por rango de ordenes.
     *
     * @param orders Lista de Ordenes con examenes a ser eliminadosF.
     *
     * @return Lista de ordnees con
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Order> deleteTest(List<Order> orders) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();

        String query = "DELETE FROM lab57 WHERE lab22c1 = ? AND lab39c1 = ?";

        for (Order order : orders)
        {
            for (Test test : order.getTests())
            {
                parameters.add(new Object[]
                {
                    order.getOrderNumber(),
                    test.getId()
                });
            }

        }
        getConnection().batchUpdate(query, parameters);

        return orders;
    }
    
     /**
     * Elimina examenes por rango de ordenes.
     *
     * @param orders Lista de Ordenes con examenes a ser eliminadosF.
     *
     * @return Lista de ordnees con
     *
     * @throws Exception Error en la base de datos.
     */
    default int deleteTestbyOrder(Reason orders) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();
        String query = "DELETE FROM lab57 WHERE lab22c1 = ? AND lab39c1 = ?";
    
        for (Test test : orders.getTests())
        {
            parameters.add(new Object[]
            {
                orders.getOrderNumber(),
                test.getId()
            });
        }
        getConnection().batchUpdate(query, parameters);
        return 1;
    }

}
