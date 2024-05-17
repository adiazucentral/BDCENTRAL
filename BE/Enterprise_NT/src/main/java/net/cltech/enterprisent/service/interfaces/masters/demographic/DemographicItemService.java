package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFatherSons;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;

/**
 * Interfaz de servicios a la informacion del maestro de Demografico Item
 *
 * @version 1.0.0
 * @author enavas
 * @since 09/05/2017
 * @see Creación
 */
public interface DemographicItemService
{

    /**
     * Lista los demograficos Item desde la base de datos
     *
     * @return Lista de Demograficos Item
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> list() throws Exception;

    /**
     * Registra un nuevo Demografico Item en la base de datos
     *
     * @param demographicItem Instancia con los datos del Demografico Item.
     * @return Instancia con los datos del Demografico Item.
     * @throws Exception Error en base de datos
     */
    public DemographicItem create(DemographicItem demographicItem) throws Exception;

    /**
     * Obtener informacion del demografico Items en la base de datos
     *
     * @param id Id del demografico Item.
     * @param code Codigo del demografico Item.
     * @param name Nombre del demografico Item.
     * @param demographic Id del demografico padre.
     * @param state Estado del demografico Item.
     * @return Instancia con los datos del demografico Items.
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> get(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception;
     /**
     * Obtener informacion del demografico Items y usuarios de la consulta web en la base de datos
     *
     * @param id Id del demografico Item.
     * @param code Codigo del demografico Item.
     * @param name Nombre del demografico Item.
     * @param demographic Id del demografico padre.
     * @param state Estado del demografico Item.
     * @return Instancia con los datos del demografico Items.
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> getwebquery(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception;

    /**
     * Actualiza la informacion del Demografico Item en la base de datos
     *
     * @param demographicItem Instancia con los datos del Demografico Item.
     * @return
     * @throws Exception Error en base de datos
     */
    public DemographicItem update(DemographicItem demographicItem) throws Exception;

    /**
     * Consulta los demograficos item por sede y demografico.
     *
     * @param branch
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    public List<BranchDemographic> demographicsItemList(int branch, int idDemographic) throws Exception;

    /**
     * Insertas demograficos con sede e item demografico
     *
     * @param demographics
     *
     * @throws Exception Error en la base de datos.
     */
    public void saveDemographicBranch(List<BranchDemographic> demographics) throws Exception;

    /**
     * Consulta los demograficos por sede y demografico.
     *
     * @param branch
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    public List<DemographicBranch> demographics(int branch) throws Exception;

    /**
     * Insertas demograficos con sede
     *
     * @param demographics
     *
     * @throws Exception Error en la base de datos.
     */
    public void demographicBranchSave(List<DemographicBranch> demographics) throws Exception;

    /**
     * Actualiza demograficos con valor requerido
     *
     * @param demographics
     *
     * @throws Exception Error en la base de datos.
     */
    public void demographicValueRequired(List<DemographicRequired> demographics) throws Exception;

    /**
     * Lista los demograficos Item hijo desde la base de datos
     *
     * @param father
     * @param idFatherItem
     * @param idSon
     * @return Lista de Demograficos Item hijo
     * @throws Exception Error en base de datos
     */
    public List<ItemDemographicSon> listSons(int father, int idFatherItem, int idSon) throws Exception;

    /**
     * Actualiza la informacion del Demografico Item en la base de datos
     *
     * @param demographicFather Instancia con los datos del Demografico Padre
     * @return
     * @throws Exception Error en base de datos
     */
    public DemographicFather updateDemographicFather(DemographicFather demographicFather) throws Exception;

    /**
     * Retorna el id Demografico hijo y sus demograficos Item hijo desde la base
     * de datos
     *
     * @param father
     * @param idFatherItem
     * @return Demografico hijo con Lista de Demograficos Item hijo
     * @throws Exception Error en base de datos
     */
    public DemographicFatherSons listSon(int father, int idFatherItem) throws Exception;

    /**
     * Retorna el id Demografico hijo
     *
     * @param father
     * @return Demografico hijo
     * @throws Exception Error en base de datos
     */
    public Integer getIdDemographicSon(int father) throws Exception;

}
