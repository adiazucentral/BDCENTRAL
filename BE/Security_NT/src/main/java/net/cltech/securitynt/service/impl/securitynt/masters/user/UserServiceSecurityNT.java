package net.cltech.securitynt.service.impl.securitynt.masters.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.securitynt.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.securitynt.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.securitynt.dao.interfaces.masters.user.UserDao;
import net.cltech.securitynt.domain.common.AuthenticationUserType;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.exception.EnterpriseNTException;
import net.cltech.securitynt.domain.masters.demographic.Branch;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.user.UserPassword;
import net.cltech.securitynt.service.interfaces.integration.IntegrationService;
import net.cltech.securitynt.service.interfaces.masters.tracking.TrackingService;
import net.cltech.securitynt.service.interfaces.masters.user.UserService;
import net.cltech.securitynt.service.interfaces.security.LicenseService;
import net.cltech.securitynt.tools.AuthenticationConstants;
import net.cltech.securitynt.tools.Constants;
import net.cltech.securitynt.tools.DateTools;
import net.cltech.securitynt.tools.JWT;
import net.cltech.securitynt.tools.Tools;
import net.cltech.securitynt.tools.log.jobs.SchedulerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a usuarios del sistema
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 25/04/2017
 * @see Creación
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
@Service
public class UserServiceSecurityNT implements UserService
{

    @Autowired
    private UserDao dao;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private BranchDao branchDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private IntegrationService integrationService;

    @Override
    public AuthorizedUser authenticate(String userName, String password, Integer branch) throws Exception
    {
        User user = dao.findByUserName(userName);
        ArrayList<String> errors = new ArrayList<>();
        if (user != null && user.isState())
        {
            //valida contraseña
            if (user.getPassword().equals(Tools.encrypt(password)))
            {
                //validacion fechas expiracion y activación
                if (!user.getUserName().equals("lismanager") && !user.getUserName().equals("system") && !user.getUserName().equals("integration"))
                {
                    Date currentDate = DateTools.getDateWithoutTime(new Date());
                    boolean administrator = (user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));

                    if (DateTools.getDateWithoutTime(user.getActivation()).after(currentDate))
                    {
                        errors.add("6|activation date");
                        throw new EnterpriseNTException(errors);
                    }

                    if (user.getExpiration() != null && DateTools.getDateWithoutTime(user.getExpiration()).before(currentDate))
                    {
                        errors.add("6|user expiration date");
                        throw new EnterpriseNTException(errors);
                    }
                    if (Boolean.parseBoolean(configurationDao.get("SecurityPolitics").getValue()))
                    {
                        if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1))
                                || DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1)))
                        {
                            errors.add("7|change password|" + user.getId() + "|" + administrator);
                            throw new EnterpriseNTException(errors);
                        }
                    }
                    if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(currentDate)
                            || DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(currentDate))
                    {
                        errors.add("6|password expiration date|" + user.getId() + "|" + administrator);
                        //updateCountFail(errors, user);
                        throw new EnterpriseNTException(errors);
                    }
                    if (branch != null && hasAccessToBranch(userName, branch))
                    {
                        errors.add("7|invalid branche");
                        throw new EnterpriseNTException(errors);
                    }
                    if (user.isValidatedRecove())
                    {
                        errors.add("7|recovery password|" + user.getId() + "|" + administrator);
                        throw new EnterpriseNTException(errors);
                    }
                    if (user.getDateLastLogin() == null && !administrator)
                    {
                        errors.add("9|First time login|" + user.getId() + "|" + administrator);
                        throw new EnterpriseNTException(errors);
                    }
                }
                dao.rolesByBranch(user);
                dao.rolesByUser(user);
                AuthorizedUser authorized = new AuthorizedUser();
                authorized.setId(user.getId());
                authorized.setName(user.getName());
                authorized.setLastName(user.getLastName());
                authorized.setUserName(user.getUserName());
                authorized.setPasswordExpiration(user.getPasswordExpiration());
                authorized.setBranch(branch);
                authorized.setPhoto(user.getPhoto());
                authorized.setConfidential(user.isConfidential());
                authorized.setAdministrator(user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));
                authorized.setMaxDiscount(user.getMaxDiscount());
                authorized.setDiscount(user.getMaxDiscount().toString());
                authorized.setCreatingItems(user.isCreatingItems());
                authorized.setOrderType(user.getOrderType().getId());
                authorized.setEditOrderCash(user.isEditOrderCash());
                authorized.setRemoveCashBox(user.isRemoveCashBox());
                authorized.setDateEntryLogin(user.getDateLastLogin());

                Branch branchAux = branch != null ? branchDao.get(branch, null, null, null) : null;
                authorized.setLicenses(branchAux != null ? licenseService.licences(branchAux.getCode()) : null);
                dao.countFail(user.getId(), true);
                dao.changeDateLastLogin(user.getId());
                return authorized;
            } else
            {
                errors.add("5|invalid password|" + (user.getCountFail() + 1));
                updateCountFail(errors, user);
                throw new EnterpriseNTException(errors);
            }
        } else if (user != null && !user.isState())
        {
            errors.add("4|inactive user");
            throw new EnterpriseNTException(errors);
        } else
        {
            errors.add("4|invalid user");
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public AuthorizedUser authenticateWeb(AuthenticationUserType authenticatingUser) throws Exception
    {
        User user = null;
        ArrayList<String> errors = new ArrayList<>();

        Integer value = authenticatingUser.getType();
        switch (value)
        {
            case AuthenticationConstants.PHYSICIAN:
                user = dao.authenticationPhysician(authenticatingUser.getUser());
                break;
            case AuthenticationConstants.PATIENT:
                user = dao.authenticationPatient(Tools.encrypt(authenticatingUser.getUser()), authenticatingUser.getHistoryType());
                break;
            case AuthenticationConstants.ACCOUNT:
                user = dao.authenticationAccount(authenticatingUser.getUser());
                break;
            case AuthenticationConstants.USERLIS:
                user = dao.findByUserName(authenticatingUser.getUser());
                errors = new ArrayList<>();
                break;
            case AuthenticationConstants.DEMOGRAPHIC:
                user = dao.findByUserNameWeb(authenticatingUser.getUser());
                errors = new ArrayList<>();
                break;
        }

        if (user != null && user.isState())
        {
            if (user.getPassword() == null)
            {
                errors.add("8|password empty");
                throw new EnterpriseNTException(errors);
            }
            //valida contraseña
            if (user.getPassword().equals(Tools.encrypt(authenticatingUser.getPassword())))
            {
                // Las validaciones de fechas de expiración 
                // Solo se llevaran a cabo con los tipos de usuario 4 y 5
                if (authenticatingUser.getType() >= AuthenticationConstants.USERLIS)
                {
                    //validacion fechas expiracion y activación
                    if (!user.getUserName().equals("lismanager") && !user.getUserName().equals("system") && !user.getUserName().equals("integration"))
                    {
                        Date currentDate = DateTools.getDateWithoutTime(new Date());
                        boolean administrator = (user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));

                        if (Boolean.parseBoolean(configurationDao.get("SecurityPolitics").getValue()))
                        {
                            if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1))
                                    || DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1)))
                            {
                                errors.add("7|change password|" + user.getId() + "|" + administrator);
                                throw new EnterpriseNTException(errors);
                            }
                        }

                        if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(currentDate)
                                || DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(currentDate))
                        {
                            errors.add("6|password expiration date|" + user.getId() + "|" + administrator);
                            updateCountFail(errors, user);
                            throw new EnterpriseNTException(errors);
                        }

                    }
                }

                AuthorizedUser authorized = new AuthorizedUser();
                authorized.setId(user.getId());
                authorized.setName(user.getName());
                authorized.setLastName(user.getLastName());
                authorized.setUserName(user.getUserName());
                authorized.setPasswordExpiration(user.getPasswordExpiration());
                authorized.setPhoto(user.getPhoto());
                authorized.setMaxDiscount(user.getMaxDiscount());
                authorized.setDiscount(user.getMaxDiscount() != null ? user.getMaxDiscount().toString() : "");
                authorized.setOrderType(user.getOrderType().getId());
                authorized.setType(user.getUserTypeLogin());
                authorized.setLicenses(licenseService.licences("01"));
                authorized.setUserTypeLogin(user.getUserTypeLogin());
                switch (value)
                {
                    case AuthenticationConstants.USERLIS:
                        dao.rolesByBranch(user);
                        dao.rolesByUser(user);

                        authorized.setConfidential(user.isConfidential());
                        authorized.setAdministrator(user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));

                        dao.changeDateLastLogin(user.getId());
                        dao.countFail(user.getId(), true);
                        break;
                    case AuthenticationConstants.DEMOGRAPHIC:
                        dao.changeDateLastLoginWeb(user.getId());
                        dao.countFailWeb(user.getId(), true);
                        break;
                    case AuthenticationConstants.PATIENT:
                        if (user.getDateEntryLogin() == null)
                        {
                            authorized.setChangePassword(true);
                        } else
                        {
                            authorized.setChangePassword(false);
                            dao.updateDateEntry(user.getId());
                        }

                        break;
                }

                return authorized;

            } else
            {

                if (!user.getPassword().equals(Tools.encrypt(authenticatingUser.getPassword())) && user.getCountFail() >= 2)
                {
                    switch (value)
                    {
                        case AuthenticationConstants.DEMOGRAPHIC:
                            changeStateUser(errors, user);
                            errors.add("6|desactived user");
                            break;
                    }
                }

                switch (value)
                {
                    case AuthenticationConstants.PHYSICIAN:
                        errors.add("5|invalid password");
                        break;
                    case AuthenticationConstants.PATIENT:
                        errors.add("5|invalid password");
                        break;
                    case AuthenticationConstants.ACCOUNT:
                        errors.add("5|invalid password");
                        break;
                    case AuthenticationConstants.USERLIS:
                        errors.add("5|invalid password|" + (user.getCountFail() + 1));
                        updateCountFail(errors, user);
                        break;
                    case AuthenticationConstants.DEMOGRAPHIC:
                        errors.add("5|invalid password" + (user.getCountFail() + 1));
                        updateCountFailWeb(errors, user);
                }

                throw new EnterpriseNTException(errors);
            }
        } else if (user != null && !user.isState())
        {
            errors.add("4|inactive user");
            throw new EnterpriseNTException(errors);
        } else
        {
            errors.add("4|invalid user");
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public boolean updateCountFail() throws Exception
    {
        return dao.updateCountFail();
    }

    @Override
    public boolean updateCountFailWeb() throws Exception
    {
        return dao.updateCountFailWeb();
    }

    /////////////////////////
    ///////////////
    /**
     * Actualizacion de contador de usuarios
     *
     * @param errors
     * @param user
     */
    private void updateCountFail(List<String> errors, User user) throws Exception
    {
        dao.countFail(user.getId(), false);
        if ((user.getCountFail() + 1) >= 3)
        {
            user.setState(false);
            integrationService.putVoid(Tools.jsonObject(user), configurationDao.get("UrlLIS").getValue() + "/api/users/changestate", generateIntegration());
            dao.countFail(user.getId(), true);
            errors.add("3|Maximum number of failed attempts");
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Actualizacion de contador de usuarios
     *
     * @param errors
     * @param user
     */
    private void updateCountFailWeb(List<String> errors, User user) throws Exception
    {
        dao.countFailWeb(user.getId(), false);
        if ((user.getCountFail() + 1) == 3)
        {
            user.setState(false);
            integrationService.putVoid(Tools.jsonObject(user), configurationDao.get("UrlLIS").getValue() + "/api/users/changestate", generateIntegration());
            dao.countFail(user.getId(), true);
            errors.add("3|Maximum number of failed attempts");
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Cambia el estado del usuario
     *
     * @param userName nombre de usuario
     *
     * @return
     * @throws Exception
     */
    public void changeStateUser(List<String> errors, User user) throws Exception
    {
        dao.changeStateUser(user);
        user.setState(false);
        errors.add("3|Maximum number of failed attempts");
        throw new EnterpriseNTException(errors);

    }

    /**
     * Determina si tiene acceso a la sede
     *
     * @param userName nombre de usuario
     * @param brach id de la sede
     *
     * @return
     * @throws Exception
     */
    private boolean hasAccessToBranch(String userName, Integer brach) throws Exception
    {
        return branchDao.filterByUsername(userName).stream()
                .noneMatch((access) -> access.getId().equals(brach));
    }

    @Override
    public User get(Integer id, String username, String identification, String signatureCode) throws Exception
    {
        return dao.get(id, username, identification, signatureCode);
    }

    @Override
    public boolean updateProfile(User user) throws Exception
    {
        List<String> errors = validateUpdateProfile(true, user);
        if (errors.isEmpty())
        {
            User userC = dao.get(null, user.getUserName(), null, null);
            User modifited = (User) userC.clone();
            modifited.setIdentification(user.getIdentification());
            modifited.setName(user.getName());
            modifited.setLastName(user.getLastName());
            modifited.setEmail(user.getEmail());
            modifited.setPhoto(user.getPhoto());
            trackingService.registerConfigurationTracking(userC, modifited, User.class);
            return dao.updatePassword(user, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean updatePassword(UserPassword userPassword) throws Exception
    {
        List<String> errors = validateUpdatePassword(userPassword);
        if (errors.isEmpty())
        {
//            User userC = dao.get(null, userPassword.getUserName(), null, null);
//            User modifited = (User) userC.clone();
//            modifited.setPassword(Tools.encrypt(userPassword.getPasswordNew()));
//            trackingService.registerConfigurationTracking(userC, modifited, User.class);
            User userC = dao.getPasswordHistory(userPassword.getIdUser());
            userPassword.setPasswordOldSecond(userC.getPenultimatePassword());
            userPassword.setPasswordOld(userC.getPassword());
            userPassword.setPasswordNew(Tools.encrypt(userPassword.getPasswordNew()));
            return dao.changePassword(userPassword, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean updatePasswordWeb(UserPassword userPassword) throws Exception
    {
        List<String> errors = validateUpdatePasswordWeb(userPassword);
        if (errors.isEmpty())
        {
            User userC = dao.getPasswordHistoryWeb(userPassword.getIdUser());
            userPassword.setPasswordOldSecond(userC.getPenultimatePassword());
            userPassword.setPasswordOld(userC.getPassword());
            userPassword.setPasswordNew(Tools.encrypt(userPassword.getPasswordNew()));
            return dao.changePasswordWeb(userPassword, Integer.valueOf(configurationDao.get("DiasClave").getValue()));
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    public String generateIntegration() throws Exception
    {
        String tokenExpiration = configurationDao.get(Constants.TOKEN_EXPIRATION_TIME).getValue();
        return JWT.generate(authenticate("lismanager", "cltechmanager", null), tokenExpiration.equals("") ? 60 : Integer.parseInt(tokenExpiration), Constants.TOKEN_AUTH_USER);
    }

    @Override
    public void deactivateUsers(List<User> users, String token) throws Exception
    {
        final Date currentDate = DateTools.getDateWithoutTime(new Date());
        final int daysValidationInactivity = Integer.parseInt(configurationDao.get("DaysValidationInactivity").getValue());
        final String url = configurationDao.get("UrlLIS").getValue() + "/api/users/deactivate";
        users.stream()
                .filter(u -> u.getDateLastLogin() != null)
                .forEach(u
                        ->
                {
                    if (DateTools.getDateWithoutTime(DateTools.changeDate(u.getDateLastLogin(), Calendar.DAY_OF_YEAR, daysValidationInactivity)).before(currentDate))
                    {
                        try
                        {
                            integrationService.putVoid(Tools.jsonObject(u), url, token);
                        } catch (JsonProcessingException ex)
                        {
                            SchedulerLog.error("No se pudo desactivar usaurio");
                            SchedulerLog.error(ex);
                        } catch (Exception ex)
                        {
                            SchedulerLog.error("No se pudo desactivar usaurio");
                            SchedulerLog.error(ex);
                        }
                    }
                });
    }

    private List<String> validateUpdateProfile(boolean isEdit, User user) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        if (user.getUserName() != null && !user.getUserName().isEmpty())
        {
            User userC = dao.get(null, user.getUserName(), null, null);
            if (userC == null)
            {
                errors.add("2|user");
            }
        } else
        {
            errors.add("0|username");
        }

        if ((user.getName() == null || user.getName().isEmpty()))
        {
            errors.add("0|name");
        }

        if ((user.getLastName() == null || user.getLastName().isEmpty()))
        {
            errors.add("0|last name");
        }

        if (user.getIdentification() != null && !user.getIdentification().isEmpty())
        {
            User userC = dao.get(null, null, user.getIdentification(), null);
            if (userC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(user.getUserName(), userC.getUserName()))
                    {
                        errors.add("1|identification");
                    }
                }
            }
        } else
        {
            errors.add("0|identification");
        }

        if (user.getUser().getId() == null || user.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    private List<String> validateUpdatePassword(UserPassword userPassword) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (userPassword.getUserName() != null && !userPassword.getUserName().isEmpty())
        {
            User userC = dao.get(null, userPassword.getUserName(), null, null);
            if (userC == null)
            {
                errors.add("2|user");
            }
        } else
        {
            errors.add("0|username");
        }

        if (userPassword.getPasswordOld() != null || !userPassword.getPasswordOld().isEmpty())
        {
            User userC = dao.get(userPassword.getUserName(), userPassword.getPasswordOld());
            if (userC == null)
            {
                errors.add("2|password old");
            } else
            {
                String encrypt = Tools.encrypt(userPassword.getPasswordNew());
                if (encrypt.equals(userC.getPassword()) || encrypt.equals(userC.getPenultimatePassword()) || encrypt.equals(userC.getAntepenultimatePassword()))
                {
                    errors.add("1|New password is in the history");
                }
            }
        } else
        {
            errors.add("0|password old");
        }

        if (userPassword.getPasswordNew() == null || userPassword.getPasswordNew().isEmpty())
        {
            errors.add("0|password new");
        }

        if (userPassword.getPasswordOld().equals(userPassword.getPasswordNew()))
        {
            errors.add("3|password old and password new equals");
        }
        return errors;
    }

    private List<String> validateUpdatePasswordWeb(UserPassword userPassword) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (userPassword.getUserName() != null && !userPassword.getUserName().isEmpty())
        {
            User userC = dao.getWeb(null, userPassword.getUserName(), null, null);
            if (userC == null)
            {
                errors.add("2|user");
            }
        } else
        {
            errors.add("0|username");
        }

        if (userPassword.getPasswordOld() != null || !userPassword.getPasswordOld().isEmpty())
        {
            User userC = dao.getWeb(userPassword.getUserName(), userPassword.getPasswordOld());
            if (userC == null)
            {
                errors.add("2|password old");
            } else
            {
                String encrypt = Tools.encrypt(userPassword.getPasswordNew());
                if (encrypt.equals(userC.getPassword()) || encrypt.equals(userC.getPenultimatePassword()) || encrypt.equals(userC.getAntepenultimatePassword()))
                {
                    errors.add("1|New password is in the history");
                }
            }
        } else
        {
            errors.add("0|password old");
        }

        if (userPassword.getPasswordNew() == null || userPassword.getPasswordNew().isEmpty())
        {
            errors.add("0|password new");
        }

        if (userPassword.getPasswordOld().equals(userPassword.getPasswordNew()))
        {
            errors.add("3|password old and password new equals");
        }
        return errors;
    }

    @Override
    public void changeStateUser(User user) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AuthorizedUser integrationAuthentication(String userName, String password, Integer branch) throws Exception
    {
        User user = dao.findByUserName(userName);
        ArrayList<String> errors = new ArrayList<>();
        if (user != null && user.isState() && user.getType().getId() == 13)
        {
            //valida contraseña
            if (user.getPassword().equals(Tools.encrypt(password)))
            {
                //validacion fechas expiracion y activación
                if (!user.getUserName().equals("lismanager") && !user.getUserName().equals("system") && !user.getUserName().equals("integration"))
                {
                    Date currentDate = DateTools.getDateWithoutTime(new Date());
                    boolean administrator = (user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));

                    if (DateTools.getDateWithoutTime(user.getActivation()).after(currentDate))
                    {
                        errors.add("6|activation date");
                        throw new EnterpriseNTException(errors);
                    }
                    /*if (user.getExpiration() != null && DateTools.getDateWithoutTime(user.getExpiration()).before(currentDate))
                    {
                        errors.add("6|user expiration date");
                        throw new EnterpriseNTException(errors);
                    }
                    if (Boolean.parseBoolean(configurationDao.get("SecurityPolitics").getValue()))
                    {
                        if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1))
                                || DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(DateTools.changeDate(currentDate, Calendar.DAY_OF_YEAR, +1)))
                        {
                            errors.add("7|change password|" + user.getId() + "|" + administrator);
                            throw new EnterpriseNTException(errors);
                        }
                    }
                    if (DateTools.getDateWithoutTime(user.getPasswordExpiration()).equals(currentDate)
                            || DateTools.getDateWithoutTime(user.getPasswordExpiration()).before(currentDate))
                    {
                        errors.add("6|password expiration date|" + user.getId() + "|" + administrator);
                        updateCountFail(errors, user);
                        throw new EnterpriseNTException(errors);
                    }
                    if (branch != null && hasAccessToBranch(userName, branch))
                    {
                        errors.add("7|invalid branche");
                        throw new EnterpriseNTException(errors);
                    }*/
                }
                //dao.rolesByBranch(user);
                //dao.rolesByUser(user);
                AuthorizedUser authorized = new AuthorizedUser();
                authorized.setId(user.getId());
                authorized.setName(user.getName());
                authorized.setLastName(user.getLastName());
                authorized.setUserName(user.getUserName());
                //authorized.setPasswordExpiration(user.getPasswordExpiration());
                //authorized.setBranch(branch);
                authorized.setPhoto(user.getPhoto());
                //authorized.setConfidential(user.isConfidential());
                //authorized.setAdministrator(user.getUserName().equals("lismanager") || user.getUserName().equals("integration") || user.getRoles().stream().anyMatch(rol -> rol.getRole().isAdministrator()));
                //authorized.setMaxDiscount(user.getMaxDiscount());
                //authorized.setOrderType(user.getOrderType().getId());
                //dao.countFail(user.getId(), true);
                //dao.changeDateLastLogin(user.getId());
                return authorized;
            } else
            {
                errors.add("5|invalid password");
                updateCountFail(errors, user);
                throw new EnterpriseNTException(errors);
            }
        } else if (user != null && !user.isState())
        {
            errors.add("4|inactive user");
            throw new EnterpriseNTException(errors);
        } else
        {
            errors.add("4|invalid user");
            throw new EnterpriseNTException(errors);
        }
    }
}
