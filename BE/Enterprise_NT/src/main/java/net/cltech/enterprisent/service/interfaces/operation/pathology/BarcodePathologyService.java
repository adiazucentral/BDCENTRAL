/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.BarcodePathologyDesigner;

/**
 * Interfaz de servicios a la informacion barcode de patologia
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/05/2021
 * @see Creación
 */
public interface BarcodePathologyService 
{
    
    /**
     * Lista barcode desde la base de datos.
     *
     * @return Lista de barcodes.
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodePathologyDesigner> list() throws Exception;

    /**
     * Registra nueva barcode en la base de datos.
     *
     * @param barcode Instancia con los datos de la barcode.
     *
     * @return Instancia con los datos de la barcode.
     * @throws Exception Error en la base de datos.
     */
    public BarcodePathologyDesigner create(BarcodePathologyDesigner barcode) throws Exception;

    /**
     * Obtener información de una barcode por un campo especifico.
     *
     * @param id ID de la barcode a ser consultada.
     *
     * @return Instancia con los datos de la barcode.
     * @throws Exception Error en la base de datos.
     */
    public BarcodePathologyDesigner getById(Integer id) throws Exception;

    /**
     * Actualiza la información de una barcode en la base de datos.
     *
     * @param barcode Instancia con los datos de la barcode.
     *
     * @return Objeto de la barcode modificada.
     * @throws Exception Error en la base de datos.
     */
    public BarcodePathologyDesigner update(BarcodePathologyDesigner barcode) throws Exception;

    /**
     * Obtener información de una barcode por estado.
     *
     * @param state Estado de los barcodes a ser consultadas
     *
     * @return Instancia con los datos de los barcodes.
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodePathologyDesigner> list(boolean state) throws Exception;
    
}
