package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.LaboratorysByBranchesDao;
import net.cltech.enterprisent.domain.masters.test.LaboratoryByBranch;
import net.cltech.enterprisent.domain.masters.test.LaboratorysByBranch;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de los laboratorios de una sede
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 31/01/2020
 * @see Creación
 */
@Service
public class LaboratorysByBranchesServiceEnterpriseNT implements LaboratorysByBranchesService
{

    @Autowired
    LaboratorysByBranchesDao laboratorysByBranchesDao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<LaboratoryByBranch> listLaboratorysByBranches(int id) throws Exception
    {
        return laboratorysByBranchesDao.listLaboratorysByBranches(id);
    }
    
    @Override
    public List<Integer> getidLaboratorybyBranch(int idBranch) throws Exception
    {
        return laboratorysByBranchesDao.getidLaboratorybyBranch(idBranch);
    }

    @Override
    public int assignLaboratoriesByBranch(LaboratorysByBranch assign) throws Exception
    {
        laboratorysByBranchesDao.deleteLaboratoriesByBranch(assign.getBranch_id());
        trackingService.registerConfigurationTracking(null, assign, LaboratorysByBranch.class);
        
        return laboratorysByBranchesDao.insertLaboratoriesByBranch(assign.getLaboratories(), assign.getBranch_id());
        
    }

}
