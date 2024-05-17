/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Card;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Tarjetas de Credito.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
public interface CardDao
{

    /**
     * Lista las tarjetas de credito desde la base de datos.
     *
     * @return Lista de tarjetas de credito.
     * @throws Exception Error en la base de datos.
     */
    public List<Card> list() throws Exception;

    /**
     * Registra una nueva tarjeta de credito en la base de datos.
     *
     * @param card Instancia con los datos del tarjeta de credito.
     * @return Instancia con los datos del tarjeta de credito.
     * @throws Exception Error en la base de datos.
     */
    public Card create(Card card) throws Exception;

    /**
     * Obtener información de una tarjeta de credito por un campo especifico.
     *
     * @param id ID del tarjeta de credito a ser consultada.
     * @param name Nombre del tarjeta de credito a ser consultada.
     * @return Instancia con los datos del tarjeta de credito.
     * @throws Exception Error en la base de datos.
     */
    public Card get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de una tarjeta de credito en la base de datos.
     *
     * @param card Instancia con los datos del tarjeta de credito.
     * @return Objeto del tarjeta de credito modificada.
     * @throws Exception Error en la base de datos.
     */
    public Card update(Card card) throws Exception;

    /**
     *
     * Elimina una tarjeta de credito de la base de datos.
     *
     * @param id ID del tarjeta de credito.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
}
