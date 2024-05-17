/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.CommentCoded;

/**
 * Representa los métodos de acceso a base de datos para la información de los Comentarios.
 * 
 * @version 1.0.0
 * @author enavas
 * @since 15/05/2017
 * @see Creación
 */
public interface CommentCodedDao
{
    /**
     * Lista los comentarios desde la base de datos
     *
     * @return Lista de comentarios
     * @throws Exception Error en base de datos
     */
    public List<CommentCoded> list() throws Exception;
    
    /**
     * Registra un nuevo comentario en la base de datos
     *
     * @param comment Instancia con los datos del comentario.
     * @return Instancia con los datos del comentario.
     * @throws Exception Error en base de datos
     */
    public CommentCoded create(CommentCoded comment) throws Exception;
    
    /**
     * Obtener informacion de los comentarios en la base de datos
     *
     * @param id Id del comentario consultado.
     * @param code del comentario consultado.
     * @param apply Aplicacion consultada.
     * @param state Estado consultado.
     * @return Instancia con los datos del Comentario.
     * @throws Exception Error en base de datos
     */
    public List<CommentCoded> get(Integer id, String code,Integer apply,Boolean state) throws Exception;
    
    /**
     * Actualiza la informacion de un comentario en la base de datos
     *
     * @param comment Instancia con los datos del comentario.
     * @return Instancia con los datos del comentario.
     * @throws Exception Error en base de datos
     */
    public CommentCoded update(CommentCoded comment) throws Exception;
    
}
