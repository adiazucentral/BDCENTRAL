package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;

/**
 * Interfaz de servicios a la informacion del maestro Tipo de Documento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
 * @see Creación
 */
public interface DocumentTypeService
{

    /**
     * Lista los tipos de documento desde la base de datos.
     *
     * @return Lista de tipos de documento.
     * @throws Exception Error en la base de datos.
     */
    public List<DocumentType> list() throws Exception;

    /**
     * Registra uno nuevo tipo de documento en la base de datos.
     *
     * @param documentType Instancia con los datos del tipo de documento.
     *
     * @return Instancia con los datos del tipo de documento.
     * @throws Exception Error en la base de datos.
     */
    public DocumentType create(DocumentType documentType) throws Exception;

    /**
     * Obtener información de un tipo de documento por una campo especifico.
     *
     * @param id   ID del tipo de documento a ser consultado.
     * @param abrr Descripcion del tipo de documento a ser consultado.
     * @param name Descripcion del tipo de documento a ser consultado.
     *
     * @return Instancia con los datos del tipo de documento.
     * @throws Exception Error en la base de datos.
     */
    public DocumentType get(Integer id, String abrr, String name) throws Exception;

    /**
     * Actualiza la información de una tipo de documento en la base de datos.
     *
     * @param documentType Instancia con los datos de la tipo de documento.
     *
     * @return Objeto de la tipo de documento modificado.
     * @throws Exception Error en la base de datos.
     */
    public DocumentType update(DocumentType documentType) throws Exception;

    /**
     *
     * Elimina un tipo de documento de la base de datos.
     *
     * @param id ID del tipo de documento.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Obtener información de tipos de documento por estado.
     *
     * @param state Estado de los tipos de documentos a ser consultados
     *
     * @return Instancia con los datos.
     * @throws Exception Error en la base de datos.
     */
    public List<DocumentType> list(boolean state) throws Exception;
}
