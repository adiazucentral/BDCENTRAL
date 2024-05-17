/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.orderForExternal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.Order;

/**
 *
 * @author hpoveda
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderForExternal implements Serializable
{

    private long orderIdField;
    private PatientForExternal patient;
    private List<DemographicForExternal> demograficos = new ArrayList<>();
    private List<TestForExternal> tests = new ArrayList<>();
    private int userIdField;
    private String userNameField;
    private List<String> commentField = new ArrayList<>();
    private String testsRateField;
    private int testsRateIdField;
    //campos para demos
    private int serviceId;
    private String serviceName;
    private Date orderDateField;

    public OrderForExternal(Order order)
    {
        this.orderIdField = order.getOrderNumber();
        this.orderDateField = order.getCreatedDate();
        this.serviceId = order.getService().getId();
        this.serviceName = order.getService().getName();
        order.getDemographics()
                .stream()
                .filter(demo -> !Objects.equals(demo.getValue(), null))
                .filter(demo -> !demo.getValue().isEmpty() && !demo.getValue().equals("."))
                .forEach(demo ->
                {
                    DemographicForExternal demographic = new DemographicForExternal(demo);
                    demographic.setType("O");
                    this.demograficos.add(demographic);
                });

        this.userIdField = order.getCreateUser().getId();
        this.userNameField = order.getCreateUser().getName() + "" + order.getCreateUser().getLastName();
        order.getComments().forEach(comm ->
        {
            commentField.add(comm.getComment().substring(comm.getComment().indexOf(">", 0), comm.getComment().indexOf("</", 0)).replaceFirst(">", ""));
        });
        this.testsRateIdField = order.getRate().getId();
        this.testsRateField = order.getRate().getName();
    }

}
