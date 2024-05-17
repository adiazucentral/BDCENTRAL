package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LaboratoryByBranch;
import net.cltech.enterprisent.domain.masters.test.LaboratorysByBranch;

/**
 * Interfaz de servicios a la informacion del maestro Laboratorio
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 31/01/2020
 * @see Creación
 */
public interface LaboratorysByBranchesService
{
    /**
     * Obtener lista de laboratorios pertenecientes a una sede.
     *
     * @param id ID de Branch a consultar.
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public List<LaboratoryByBranch> listLaboratorysByBranches(int id) throws Exception;
    
        
    /**
     * Obtener lista de laboratorios pertenecientes a una sede.
     *
     * @param idBranch
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
     public List<Integer> getidLaboratorybyBranch(int idBranch) throws Exception;

    /**
     * Actualizar los laboratorios de una sede.
     *
     * @param id ID de sede 
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public int assignLaboratoriesByBranch(LaboratorysByBranch assign) throws Exception;

}
