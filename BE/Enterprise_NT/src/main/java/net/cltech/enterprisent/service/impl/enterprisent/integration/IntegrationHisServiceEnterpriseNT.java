package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationHisDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.his.AuthorizedBranch;
import net.cltech.enterprisent.domain.integration.his.BranchHis;
import net.cltech.enterprisent.domain.integration.his.BranchHisState;
import net.cltech.enterprisent.domain.integration.his.UserHis;
import net.cltech.enterprisent.domain.integration.his.UserStatus;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHisService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de los servicios para el his y su integración con NT
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2021
 * @see Creación
 */
@Service
public class IntegrationHisServiceEnterpriseNT implements IntegrationHisService
{

    @Autowired
    private UserDao userDao;
    @Autowired
    private IntegrationHisDao dao;
    @Autowired
    private BranchDao branchDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private UserService userService;

    @Override
    public UserHis create(UserHis userhis) throws Exception
    {
        User user = userDao.get(null, userhis.getUsuario(), null, null);
        List<Integer> branches = new ArrayList<>();
        if (user == null)
        {
            List<String> errors = validateFields(false, userhis);

            if (errors.isEmpty())
            {
                UserHis created = dao.createUser(userhis);

                userhis.getSedes().forEach(branch ->
                {
                    {
                        try
                        {
                            int branchId = dao.getBranchId(branch.getCodigo());
                            if (branchId > 0 && !branches.contains(branchId))
                            {
                                branches.add(branchId);
                                dao.insertBranches(created.getId(), branchId);
                            }
                        } catch (Exception ex)
                        {
                            Logger.getLogger(IntegrationHisServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                return created;
            } else
            {
                throw new EnterpriseNTException(errors);
            }

        } else
        {
            userhis.setId(user.getId());
            List<String> errors = validateFields(true, userhis);
            if (errors.isEmpty())
            {
                UserHis update = dao.updateUser(userhis);
                if (userhis.getSedes().size() > 0)
                {
                    dao.deleteBranchByUser(update.getId());
                    userhis.getSedes().forEach(branch ->
                    {
                        {
                            try
                            {
                                int branchId = dao.getBranchId(branch.getCodigo());
                                if (branchId > 0 && !branches.contains(branchId))
                                {
                                    branches.add(branchId);
                                    dao.insertBranches(user.getId(), branchId);
                                }
                            } catch (Exception ex)
                            {
                                Logger.getLogger(IntegrationHisServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                }
                return update;
            } else
            {
                throw new EnterpriseNTException(errors);
            }
        }
    }

    @Override
    public UserStatus changeStateUser(UserStatus user) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (user.getUsuario() == null)
        {
            errors.add("0|userName");
        }
        if (user.getEstado() != 0 && user.getEstado() != 1)
        {
            errors.add("0|status");
        }
        if (errors.isEmpty())
        {
            dao.changeStateUser(user);
            return user;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateFields(boolean isEdit, UserHis user) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (user.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (user.getUsuario().equals("lismanager") || user.getUsuario().equals("integration") || user.getUsuario().equals("system"))
                {
                    errors.add("6|user");
                    return errors;
                }
            }

        }

        if (user.getUsuario() != null && !user.getUsuario().isEmpty())
        {
            User userC = userDao.get(null, user.getUsuario(), null, null);
            if (userC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(user.getId(), userC.getId()))
                    {
                        errors.add("1|username");
                    }
                } else
                {
                    errors.add("1|username");
                }
            }
        }

        if ((user.getContraseña() == null || user.getContraseña().isEmpty()) && !isEdit)
        {
            errors.add("0|password");
        }

        if ((user.getNombres() == null || user.getNombres().isEmpty()))
        {
            errors.add("0|name");
        }

        if ((user.getApellidos() == null || user.getApellidos().isEmpty()))
        {
            errors.add("0|last name");
        }

        if (user.getSedes() != null && !user.getSedes().isEmpty())
        {
            for (AuthorizedBranch branch : user.getSedes())
            {

                if (branch.getCodigo().isEmpty())
                {
                    errors.add("2|Branch code");
                }
            }
        } else
        {
            errors.add("3|No access branches");
        }

        return errors;
    }

    /**
     * Crea una sede desde el HIS
     *
     * @param branchhis
     *
     *
     * @return Instancia con los datos de la sede
     * @throws Exception Error en la base de datos.
     */
    @Override
    public BranchHis createBranch(BranchHis branchhis) throws Exception
    {
        // Obtenemos el usuario configurado para la creacion de sedes desde el HIS
        Integer userId = 1;//configurationService.get("IdUsuarioHis").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("IdUsuarioHis").getValue());
        User user = userService.get(userId, null, null, null);
        // Cargamos el objeto de tipo usuario que recibe la instancia de la sede
        AuthorizedUser authorizedUser = new AuthorizedUser();
        authorizedUser.setId(user.getId());
        authorizedUser.setLastName(user.getLastName());
        authorizedUser.setName(user.getName());
        authorizedUser.setUserName(user.getUserName());

        Branch branch = branchDao.get(null, null, null, branchhis.getCodigo());
        if (branch == null)
        {
            branch = new Branch();
            // Asignamos el usuario a la sede
            branch.setUser(authorizedUser);
            branch.setState(true);
            branch.setCode(branchhis.getCodigo());
            branch.setName(branchhis.getNombre());
            branch.setAbbreviation(branchhis.getAbreviatura());
            branch.setAddress(branchhis.getDireccion());

            List<String> errors = validateBranchFields(false, branch);
            if (errors.isEmpty())
            {
                Branch created = branchDao.create(branch);
                updateCounters(branch);
                branchhis.setId(created.getId());
                trackingService.registerConfigurationTracking(null, created, Branch.class);
                return branchhis;
            } else
            {
                throw new EnterpriseNTException(errors);
            }
        } else
        {
            // Asignamos el usuario a la sede
            branch.setUser(authorizedUser);
            branch.setCode(branchhis.getCodigo());
            branch.setName(branchhis.getNombre());
            branch.setAbbreviation(branchhis.getAbreviatura());
            branch.setAddress(branchhis.getDireccion());
            branch.setAddress(branchhis.getDireccion());

            List<String> errors = validateToUpdateBranchFields(true, branch);
            if (errors.isEmpty())
            {
                Branch branchC = branchService.get(null, null, null, branch.getCode());
                Branch modifited = new Branch();
                if (branchC != null)
                {
                    branch.setId(branchC.getId());
                    modifited = branchDao.update(branch);
                    updateCounters(branch);
                    branchhis.setId(modifited.getId());
                    trackingService.registerConfigurationTracking(branchC, modifited, Branch.class);
                }
                return branchhis;
            } else
            {
                throw new EnterpriseNTException(errors);
            }
        }
    }

    /**
     * Actualiza una sede desde el HIS
     *
     * @param branch Instancia de sede con los datos para su actualización
     *
     * @return Instancia de la sede con los datos de su modificación
     * @throws Exception Error en la base de datos.
     */
    @Override
    public BranchHisState updateBranch(BranchHisState branchstate) throws Exception
    {
        // Obtenemos el usuario configurado para la creacion de sedes desde el HIS
        Integer userId = 1;// configurationService.get("IdUsuarioHis").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("IdUsuarioHis").getValue());
        User user = userService.get(userId, null, null, null);
        // Cargamos el objeto de tipo usuario que recibe la instancia de la sede
        AuthorizedUser authorizedUser = new AuthorizedUser();
        authorizedUser.setId(user.getId());
        authorizedUser.setLastName(user.getLastName());
        authorizedUser.setName(user.getName());
        authorizedUser.setUserName(user.getUserName());

        Branch branch = branchDao.get(null, null, null, branchstate.getCodigo());
        if (branch != null)
        {
            List<String> errors = validateToUpdateBranchFields(true, branch);
            if (errors.isEmpty())
            {
                Branch branchC = branchService.get(null, null, null, branch.getCode());
                Branch modifited = new Branch();
                if (branchC != null)
                {
                    branch.setId(branchC.getId());
                    branch.setState(branchstate.getState() == 1);
                    modifited = branchDao.update(branch);
                    updateCounters(branch);
                    trackingService.registerConfigurationTracking(branchC, modifited, Branch.class);
                }
                return branchstate;
            } else
            {
                throw new EnterpriseNTException(errors);
            }
        } else
        {
            List<String> errorsbranch = new ArrayList<>();
            errorsbranch.add("0|branch not found");
            throw new EnterpriseNTException(errorsbranch);
        }
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

    private List<String> validateBranchFields(boolean isEdit, Branch branch) throws Exception
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
                if (branchService.get(branch.getId(), null, null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (branch.getCode() != null && !branch.getCode().isEmpty())
        {
            Branch branchC = branchService.get(null, null, null, branch.getCode());
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
            Branch branchC = branchService.get(null, null, branch.getAbbreviation(), null);
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
            Branch branchC = branchService.get(null, branch.getName(), null, null);
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

    private List<String> validateToUpdateBranchFields(boolean isEdit, Branch branch) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        if (branch.getCode() == null || branch.getCode().isEmpty())
        {
            errors.add("0|code");
        }

        if (branch.getAbbreviation() != null && !branch.getAbbreviation().isEmpty())
        {
            Branch branchC = branchService.get(null, null, branch.getAbbreviation(), null);
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
            Branch branchC = branchService.get(null, branch.getName(), null, null);
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

    /**
     * obtener getItemDemografic de item demografic
     *
     * @param branch
     * {@link net.cltech.enterprisent.domain.masters.demographic.Branch}
     * @throws Exception Error presentado en el servicio
     */
    private List<Integer> listOrdersClient(String listStr)
    {
        List<Integer> listOrderClient = new ArrayList<>();

        String[] listOrderClientStr = listStr.trim().split(",");
        for (String id : listOrderClientStr)
        {
            listOrderClient.add(Integer.parseInt(id));
        }

        return listOrderClient;
    }

   

}
