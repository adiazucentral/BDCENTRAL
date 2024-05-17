/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

/**
 * Entidad del demografico
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creacion
 */
@Data
public class DemographicIngresoOrder
{
    private int id;
    private String name;
    private String code;
    private boolean encoded;
    private boolean selected;
    private boolean required;
    private String origin;
    private String value;
    private String defaultValue;
    private int demographicsItemId;
    private String demographicsItemCode;
    private String demographicsItemName;
    private boolean status;
    private List<DemographicItemIngresoOrder> demographicsItem;

    public DemographicIngresoOrder()
    {
        demographicsItem = new LinkedList<>();
    }

    public DemographicIngresoOrder(int id)
    {
        this();
        this.id = id;
    }

    public DemographicIngresoOrder(int id, String name, boolean encoded)
    {
        this();
        this.id = id;
        this.name = name;
        this.encoded = encoded;
    }

    public DemographicIngresoOrder(int id, String code, String name, boolean encoded)
    {
        this();
        this.id = id;
        this.name = name;
        this.code = code;
        this.encoded = encoded;
    }
}
