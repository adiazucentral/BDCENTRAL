/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import java.sql.ResultSet;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.integration.OperationDaoIngresoOrder;
import net.cltech.enterprisent.domain.DTO.integration.order.ListsOrderIngreso;

/**
 *
 * @author oarango
 * @since 2022-04-17
 * @see Creacion
 */
public class ListsDaoIngresoOrder extends OperationDaoIngresoOrder
{

    public List<ListsOrderIngreso> findAll()
    {
        return jdbc.query(""
                + "SELECT lab103c1, lab103c2, lab103c3, lab07c1 "
                + "FROM lab103 ",
                (ResultSet rs, int i) ->
        {
            ListsOrderIngreso list = new ListsOrderIngreso();
            list.setId(rs.getInt("lab103c1"));
            list.setCode(rs.getString("lab103c2"));
            list.setName(rs.getString("lab103c3"));
            list.setStatus(rs.getInt("lab07c1") == 1);
            return list;
        });
    }
}
