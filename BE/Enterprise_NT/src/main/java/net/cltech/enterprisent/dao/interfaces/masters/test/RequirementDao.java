package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Requirement;

/**
 * Representa los metodos de acceso a base de datos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/04/2017
 * @see Creación
 */
public interface RequirementDao
{

    /**
     * Registra un nuevo registro en la base de datos.
     *
     * @param requirement Instancia con los datos del requisito.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.test.Requirement}
     * @throws Exception Error en la base de datos.
     */
    public Requirement create(Requirement requirement) throws Exception;

    /**
     * Lista los requisitos desde la base de datos.
     *
     * @return Lista de unidades de medida.
     * @throws Exception Error en la base de datos.
     */
    public List<Requirement> list() throws Exception;

    /**
     * Lee la información del requisito.
     *
     * @param id ID del requisito a ser consultada.
     *
     * @return Instancia con los datos del requisito.
     * @throws Exception Error en la base de datos.
     */
    public Requirement findById(Integer id) throws Exception;

    /**
     * Lee la información del requisito.
     *
     *
     * @param code código del requisito
     *
     * @return Instancia con los datos del requisito encontrado.
     * @throws Exception Error en la base de datos.
     */
    public Requirement findByCode(String code) throws Exception;

    /**
     * Actualiza la información del requisito en la base de datos.
     *
     *
     * @param requirement Instancia con los datos del requisito.
     *
     * @return requisito actualizado
     *
     * @throws Exception Error en la base de datos.
     */
    public Requirement update(Requirement requirement) throws Exception;

}
