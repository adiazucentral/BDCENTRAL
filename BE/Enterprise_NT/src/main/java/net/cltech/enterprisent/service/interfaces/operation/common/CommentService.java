/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.common;

import java.util.List;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;

/**
 * Interfaz de servicios a la informacion de microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creación
 */
public interface CommentService
{

    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idOrder Numero de la Orden.
     * @param idPatient Id del paciente.
     * @return Lista de comentarios.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<CommentOrder> listCommentOrder(Long idOrder, Integer idPatient) throws Exception;

    /**
     * Obtiene los comentarios de microbiologia de una orden y muestra o examen.
     *
     * @param idOrder Numero de la Orden.
     * @param idTest Id del examen.
     * @param idSample Id de la muestra
     * @return Lista de Comentarios.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<CommentMicrobiology> listCommentMicrobiology(long idOrder, Integer idTest, Integer idSample) throws Exception;

    /**
     * Inserta, actualiza y elimina comentarios de microbiologia.
     *
     * @param commentsOrder Comentarios de la orden o diagnositco del paciente.
     * @return Cantidad de datos afectados.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public int commentOrder(List<CommentOrder> commentsOrder) throws Exception;

    /**
     * Inserta, actualiza y elimina comentarios de microbiologia.
     *
     * @param commentsMicrobiology Comentarios de Microbiologia.
     * @return Cantidad de datos afectados.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public int commentMicrobiology(List<CommentMicrobiology> commentsMicrobiology) throws Exception;
    
    /**
     * Consulta la trazabilidad del comentario de microbiologia
     *
     * @param idOrder Numero de orden.
     * @param idTest Id del examen.
     * @return Trazabilidad del comentario de microbiologia.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<CommentMicrobiology> listCommentMicrobiologyTracking(long idOrder, int idTest) throws Exception;
    
    /**
     * Consulta la trazabilidad del comentario de la orden o diagnostico permanente del paciente
     *
     * @param idRecord Numero de orden o id paciente.
     * @param type Indica si la consulta es de la orden o el paciente.
     * @return Trazabilidad del comentario de la orden.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<CommentOrder> listCommentOrderTracking(long idRecord, int type) throws Exception;
}
