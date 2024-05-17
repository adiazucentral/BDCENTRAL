package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.domain.masters.test.ResultRelationship;

/**
 * Interfaz de servicios a la informacion del maestro relacion resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/07/2017
 * @see Creación
 */
public interface ResultRelationshipService
{

    /**
     * Lista relación desde la base de datos.
     *
     * @param id id de la alarma
     *
     * @return Lista de reglas.
     * @throws Exception Error en la base de datos.
     */
    public List<ResultRelationship> list(int id) throws Exception;

    /**
     * Registra reglas en la base de datos.
     *
     * @param create Instancia con las reglas para la alarma.
     *
     * @return Instancia con los datos del relación.
     * @throws Exception Error en la base de datos.
     */
    public int create(Alarm create) throws Exception;

}
