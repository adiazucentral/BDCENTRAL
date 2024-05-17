/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import lombok.Data;

/**
 * Entidad de un comentario
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creacion
 */
@Data
public class CommentIngresoOrder
{

    private int id;
    private int type; //Tipo: 1 -> Orden, 2 -> Paciente
    private long recordId; //Numero de la Orden o Id del paciente
    private String comment;

    public CommentIngresoOrder()
    {
    }

    public CommentIngresoOrder(int type, String comment)
    {
        this.type = type;
        this.comment = comment;
    }
}
