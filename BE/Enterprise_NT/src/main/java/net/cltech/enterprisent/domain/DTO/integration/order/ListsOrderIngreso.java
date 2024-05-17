/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import lombok.Data;

/**
 * Entidad para las listas
 *
 * @author oarango
 * @since 2022-04-17
 * @see Creacion
 */
@Data
public class ListsOrderIngreso
{

    private int id;
    private String code;
    private String name;
    private boolean status;
}
