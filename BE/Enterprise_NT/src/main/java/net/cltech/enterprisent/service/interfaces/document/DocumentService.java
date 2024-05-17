/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.document;

import java.util.List;
import net.cltech.enterprisent.domain.document.Document;

/**
 * Interfaz de servicios a la informacion de documentos de la orden o
 * resultados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/12/2017
 * @see Creación
 */
public interface DocumentService
{

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    public List<Document> list(Long idOrder) throws Exception;

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos que se encuentran activo para ver en reporte.
     * @throws Exception Error en la base de datos.
     */
    public List<Document> listattachments(Long idOrder) throws Exception;

    /**
     * Lista los documentos de una orden o un resultado.
     *
     * @param idOrder Orden.
     * @param idTest Prueba.
     *
     * @return Lista de documentos que se encuentran activo para ver en reporte.
     * @throws Exception Error en la base de datos.
     */
    public List<Document> listattachments(Long idOrder, Integer idTest) throws Exception;

    /**
     * Lista los documentos de una orden o un resultado.
     *
     * @param idOrder Orden.
     * @param idTest Prueba.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    public List<Document> list(Long idOrder, Integer idTest) throws Exception;

    /**
     * Guardar documento de una orden o un resultado.
     *
     * @param document Documento de la orden o el resultado.
     *
     * @return Documento.
     * @throws Exception Error en la base de datos.
     */
    public Document saveDocument(Document document) throws Exception;

    /**
     * Eliminar documento de una orden o un resultado.
     *
     * @param document Documento de la orden o el resultado.
     *
     * @return Documento.
     * @throws Exception Error en la base de datos.
     */
    public Document deleteDocument(Document document) throws Exception;
}
