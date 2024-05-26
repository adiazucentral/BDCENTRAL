package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Sede para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 08/05/2017
 * @see Creaciòn
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/09/2017
 * @see Se agrega actualizacion de los contadores de ordenes
 */
@Service
public class BranchServiceEnterpriseNT implements BranchService
{

    @Autowired
    private BranchDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ToolsDao toolsDao;

    @Override
    public List<Branch> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Branch create(Branch branch) throws Exception
    {
        branch.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(false, branch);
        if (errors.isEmpty())
        {
            Branch created = dao.create(branch);
            updateCounters(branch);
            trackingService.registerConfigurationTracking(null, created, Branch.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Branch get(Integer id, String name, String abbr, String code) throws Exception
    {
        return dao.get(id, name, abbr, code);
    }
    
    @Override
    public Branch getBasic(Integer id, String name, String abbr, String code) throws Exception
    {
        return dao.getBasic(id, name, abbr, code);
    }

    @Override
    public Branch update(Branch branch) throws Exception
    {
        List<String> errors = validateFields(true, branch);
        if (errors.isEmpty())
        {
            Branch branchC = dao.get(branch.getId(), null, null, null);
            Branch modifited = dao.update(branch);
            updateCounters(branch);
            trackingService.registerConfigurationTracking(branchC, modifited, Branch.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Branch> list(boolean state) throws Exception
    {
        List<Branch> list = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Branch) o).isState() == state));
        return list;
    }

    private List<String> validateFields(boolean isEdit, Branch branch) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (branch.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(branch.getId(), null, null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (branch.getCode() != null && !branch.getCode().isEmpty())
        {
            Branch branchC = dao.get(null, null, null, branch.getCode());
            if (branchC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(branch.getId(), branchC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if (branch.getAbbreviation() != null && !branch.getAbbreviation().isEmpty())
        {
            Branch branchC = dao.get(null, null, branch.getAbbreviation(), null);
            if (branchC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(branch.getId(), branchC.getId()))
                    {
                        errors.add("1|abbreviation");
                    }
                } else
                {
                    errors.add("1|abbreviation");
                }
            }
        }

        if (branch.getName() != null && !branch.getName().isEmpty())
        {
            Branch branchC = dao.get(null, branch.getName(), null, null);
            if (branchC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(branch.getId(), branchC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (branch.getUser().getId() == null || branch.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
    
     @Override
    public List<Branch> listLogin() throws Exception
    {
        return dao.listLogin();
    }

    @Override
    public List<Branch> filterByUsername(String username) throws Exception
    {
        return dao.filterByUsername(username);
    }
    
    @Override
    public List<Branch> filterByUsernameLogin(String username) throws Exception
    {
        return dao.filterByUsernameLogin(username);
    }

    /**
     * Actualiza los contadores si la numeracion es por sede o general
     *
     * @param branch
     * {@link net.cltech.enterprisent.domain.masters.demographic.Branch}
     * @throws Exception Error presentado en el servicio
     */
    private void updateCounters(Branch branch) throws Exception
    {
        String branchNumbering = configurationService.getValue(ConfigurationConstants.KEY_NUMERATION);
        String sequenceName = Constants.SEQUENCE + branch.getId();
        int maximun = 0;
        int minimun = 0;
        if (branchNumbering.equals(ConfigurationConstants.NUMERATION_BRANCH))
        {
            minimun = branch.getMinimum();
            maximun = branch.getMaximum();
        } else if (branchNumbering.equals(ConfigurationConstants.NUMERATION_GENERAL))
        {
            minimun = 1;
            maximun = Integer.parseInt(String.join("", Collections.nCopies(Integer.parseInt(configurationService.get("DigitosOrden").getValue().trim()), "9")));
        }
        if (!toolsDao.validateSequence(sequenceName))
        {
            toolsDao.createSequence(Constants.SEQUENCE + branch.getId(), minimun, 1, maximun);
        } else
        {
            toolsDao.resetSequence(Constants.SEQUENCE + branch.getId(), minimun, maximun);
        }
    }
}
