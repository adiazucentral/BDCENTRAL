/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;

/**
 * Interfaz de servicios a la informacion del maestro Medio de cultivo
 *
 * @version 1.0.0
 * @author enavas
 * @since 11/08/2017
 * @see Creación
 */
public interface MediaCultureService
{

    /**
     * Lista los Medios de cultivo desde la base de datos.
     *
     * @return Lista de Medios de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public List<MediaCulture> list() throws Exception;

    /**
     * Registra un nuevo Medio de cultivo en la base de datos.
     *
     * @param mediaCulture Instancia con los datos del medio de cultivo.
     *
     * @return Instancia con los datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public MediaCulture create(MediaCulture mediaCulture) throws Exception;

    /**
     * Obtener información de un medio de cultivo por un campo especifico.
     *
     * @param id ID del medio de cultivo a ser consultada.
     * @param code Codigo del medio de cultivo a ser consultada.
     * @param name Nombre del medio de cultivo a ser consultada.
     *
     * @return Instancia con los datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public MediaCulture get(Integer id, String code, String name) throws Exception;

    /**
     * Actualiza la información de un medio de cultivo en la base de datos.
     *
     * @param mediaCulture Instancia con los datos del medio de cultivo.
     *
     * @return Objeto del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public MediaCulture update(MediaCulture mediaCulture) throws Exception;

    /**
     * Obtener información de los medios de cultivo por estado.
     *
     * @param state Estado de los medios de cultivo a ser consultadas
     *
     * @return lista de Instancia con los datos de los medios de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public List<MediaCulture> list(boolean state) throws Exception;

    /**
     * Lista los medios de cultivo por prueba desde la base de datos.
     *
     * @param idTest Id del examen
     * @return Lista de medios de cultivo por prueba.
     * @throws Exception Error en la base de datos.
     */
    public MediaCultureTest listMediaCulture(Integer idTest) throws Exception;

    /**
     * Asociar los medios de cultivos a una prueba.
     *
     * @param mediaCultureTest Instancia con los datos de la prueba y medios de
     * cultivo.
     * @return cantidad de registros insertados
     * @throws java.lang.Exception
     */
    public int createMediaCultureTest(MediaCultureTest mediaCultureTest) throws Exception;
    
    /**
     * Obtiene el examen que este asociado a la muestra.
     *
     * @param test Id del examen.
     * @return Examen asociado al medio de cultivo.
     * @throws java.lang.Exception
     */
    public TestBasic getMediaCultureTest(int test) throws Exception;

}
