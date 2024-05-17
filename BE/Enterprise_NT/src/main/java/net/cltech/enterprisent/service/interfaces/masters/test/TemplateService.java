package net.cltech.enterprisent.service.interfaces.masters.test;

import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;

/**
 * Interfaz de servicios a la informacion del maestro Plantilla
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/07/2017
 * @see Creación
 */
public interface TemplateService
{

    /**
     * Obtiene la opción de plantilla general
     *
     * @param id id del examen
     *
     * @return Plantilla general de un examen
     * @throws Exception Error en la base de datos.
     */
    public GeneralTemplateOption getById(int id) throws Exception;

    /**
     * Registra plantillas en la base de datos.
     *
     * @param general Opcion de plantilla general
     *
     * @throws Exception Error en la base de datos.
     */
    public void create(GeneralTemplateOption general) throws Exception;

    /**
     * Elimina todas las plantillas de un exámen
     *
     * @param id id examen
     *
     * @throws Exception Error en base de datos
     */
    public void delete(Integer id) throws Exception;
}
