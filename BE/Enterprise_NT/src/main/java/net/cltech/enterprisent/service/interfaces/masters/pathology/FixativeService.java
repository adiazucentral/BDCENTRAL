/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Fixative;

/**
 * Interface de servicios para el maestro de fijadores de muestras de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/04/2021
 * @see Creacion
 */
public interface FixativeService {
    
    /**
    * lista de fijadores de patologia.
    *
    *
    * @return la lista de fijadores.
    * @throws Exception Error en la base de datos.
    */
    public List<Fixative> list() throws Exception;

    /**
    * Registra un nuevo fijador en la base de datos.
    *
    * @param fixative Instancia con los datos del fijador.
    * @return Instancia con los datos del fijador.
    * @throws Exception Error en la base de datos.
    */
    public Fixative create(Fixative fixative) throws Exception;

    /**
    * Obtener información de un fijador por un campo especifico.
    *
    * @param id ID del fijador a ser consultado.
    * @param name Nombre del fijador a ser consultado.
    * @param code Codigo del fijador a ser consultado.
    * @return Instancia con los datos del fijador.
    * @throws Exception Error en la base de datos.
    */
    public Fixative get(Integer id, String name, String code) throws Exception;

    /**
    * Actualiza un fijador de la base de datos.
    *
    * @param fixative Instancia con los datos del fijador.
    * @return la instancia del objeto fijador.
    * @throws Exception Error en la base de datos.
    */
    public Fixative update(Fixative fixative) throws Exception;
    
    /**
    * Obtener información de un fijador por estado.
    *
    * @param state Estado de los fijadores a ser consultados
    * @return Instancia con los datos del fijador.
    * @throws Exception Error en la base de datos.
    */
    public List<Fixative> list(int state) throws Exception;
}
