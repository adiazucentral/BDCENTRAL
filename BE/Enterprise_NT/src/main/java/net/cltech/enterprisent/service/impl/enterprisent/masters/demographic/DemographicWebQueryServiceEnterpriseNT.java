package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicWebQueryDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.DemographicWebQuery;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicWebQueryService;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios para la configuracion adicional del demografico con
 * gestion de la consulta web
 *
 * @version 1.0.0
 * @author javila
 * @since 23/01/2020
 * @see Creación
 */
@Service
public class DemographicWebQueryServiceEnterpriseNT implements DemographicWebQueryService
{

    @Autowired
    private DemographicWebQueryDao demographicWebQueryDao;
    @Autowired
    private TrackingService trackingService;

    /**
     * Lista todos los demograficos con consulta web de la base de datos
     *
     * @throws java.lang.Exception
     */
    @Override
    public List<DemographicWebQuery> list() throws Exception
    {
        return demographicWebQueryDao.list();
    }

    /**
     * Crear nuevo registro de demografico consulta web
     *
     * @throws Exception Error en la base de datos
     */
    @Override
    public DemographicWebQuery create(DemographicWebQuery demographicWebQuery) throws Exception
    {

        List<String> errors = validateUsername(false, demographicWebQuery);
        if (errors.isEmpty())
        {
            DemographicWebQuery created = demographicWebQueryDao.create(demographicWebQuery);
            trackingService.registerConfigurationTracking(null, created, DemographicWebQuery.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    /**
     * Modificar o Actualizar datos de demograficos consulta web
     *
     * @param demographicWebQuery
     * @return Demografico consulta web
     * @throws Exception Error en la base de datos
     */
    @Override
    public DemographicWebQuery update(DemographicWebQuery demographicWebQuery) throws Exception
    {

        DemographicWebQuery modified = new DemographicWebQuery();
        List<String> errors = validateUsername(true, demographicWebQuery);
        if (errors.isEmpty())
        {
            DemographicWebQuery demographicWebQueryC = demographicWebQueryDao.getById(demographicWebQuery.getId());
            demographicWebQuery.setAntepenultimatePassword(demographicWebQueryC.getPenultimatePassword());
            demographicWebQuery.setPenultimatePassword(demographicWebQueryC.getPassword());
            demographicWebQuery.setPassword(Tools.encrypt(demographicWebQuery.getPassword()));
            demographicWebQueryC.setAntepenultimatePassword(null);
            demographicWebQueryC.setPenultimatePassword(null);
            demographicWebQueryC.setPassword(null);
            demographicWebQueryC.setEmail(demographicWebQuery.getEmail());
            modified = demographicWebQueryDao.update(demographicWebQuery);
            trackingService.registerConfigurationTracking(demographicWebQueryC, modified, DemographicWebQuery.class);
            return modified;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Validación de nombre de usuario, encontrando si este nombre de usuario ya
     * a sido registrado retornara nulo, de lo contrario permitira hacer la
     * insercion
     *
     * @param isEdit
     * @param demographicWebQuery
     * @return Nombre de usuario
     * @throws Exception Error en la base de datos
     */
    public List<String> validateUsername(boolean isEdit, DemographicWebQuery demographicWebQuery) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (demographicWebQuery.getUser() == null || demographicWebQuery.getUser().isEmpty())
            {
                errors.add("0|username");
            }

            if (demographicWebQuery.getPassword() != null || demographicWebQuery.getPasswordExpirationDate() != null)
            {
                try
                {
                    DemographicWebQuery demographicWebQuery1 = demographicWebQueryDao.getById(demographicWebQuery.getId());
                    if (demographicWebQuery1 != null)
                    {
                        if (!Objects.equals(demographicWebQuery.getId(), demographicWebQuery1.getId()))
                        {
                            errors.add("1|username");
                        }
                    }

                    if (demographicWebQuery.getPassword() != null)
                    {
                        DemographicWebQuery demographicWebQueryC = demographicWebQueryDao.getById(demographicWebQuery.getId());
                        String encrypt = Tools.encrypt(demographicWebQuery.getPassword());
                        if (encrypt.equals(demographicWebQueryC.getPassword()) || encrypt.equals(demographicWebQueryC.getPenultimatePassword()) || encrypt.equals(demographicWebQueryC.getAntepenultimatePassword()))
                        {
                            errors.add("1|New password is in the history");
                        }
                    }
                } catch (EmptyResultDataAccessException e)
                {
                }
            }
        } else
        {
            if (demographicWebQuery.getUser() == null || demographicWebQuery.getUser().isEmpty())
            {

                errors.add("0|username");
            } else
            {

                DemographicWebQuery demographicWebQuery1 = demographicWebQueryDao.get(demographicWebQuery.getUser());
                if (demographicWebQuery1 != null && !demographicWebQuery1.getUser().isEmpty())
                {
                    if (!Objects.equals(demographicWebQuery.getId(), demographicWebQuery1.getId()))
                    {
                        errors.add("1|username");
                    }
                }

            }

            if (demographicWebQuery.getPassword() == null || demographicWebQuery.getPassword().isEmpty())
            {
                errors.add("0|password");
            }

            if (demographicWebQuery.getDemographic() == 0)
            {
                errors.add("0|demographic");
            }

            if (demographicWebQuery.getIdDemographicItem() == 0)
            {
                errors.add("0|idDemographicItem");
            }

            if (!demographicWebQuery.isState())
            {
                errors.add("0|state");
            }
        }
        
        if (demographicWebQuery.getUserLastTransaction().getId() == null || demographicWebQuery.getUserLastTransaction().getId() == 0)
        {
            errors.add("0|userlasttransaction");
        }

        return errors;
    }

    /**
     * Retorna el objeto cargado encontrado con el mismo nombre de usuario para
     * validar si son el mismo objeto
     *
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public DemographicWebQuery get(String userName) throws Exception
    {
        return demographicWebQueryDao.get(userName);
    }

    /**
     * Obtiene una lista de demograficos consulta web según sea su id
     * demografico y su demografico
     *
     * @param idDemographicItem
     * @return
     * @throws Exception Error de base de datos
     */
    @Override
    public DemographicWebQuery filterByIdDemographic(int idDemographicItem, int demographicwebquery) throws Exception
    {
        return demographicWebQueryDao.filterByIdDemographic(idDemographicItem, demographicwebquery);
    }

    /**
     * Obtiene una lista de demograficos consulta web activos demografico y su
     * demografico
     *
     * @return
     * @throws Exception Error de base de datos
     */
    @Override
    public List<DemographicWebQuery> listDeactivate() throws Exception
    {
        return demographicWebQueryDao.listDeactivate();
    }

    @Override
    public DemographicWebQuery changeStateUser(DemographicWebQuery userDemographicWebQuery) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (userDemographicWebQuery.getId() == null)
        {
            errors.add("0|id");
        }
        if (errors.isEmpty())
        {
            boolean state = userDemographicWebQuery.isState();
            DemographicWebQuery demographicWebQueryC = demographicWebQueryDao.get(userDemographicWebQuery.getUser());
            userDemographicWebQuery = demographicWebQueryC;
            userDemographicWebQuery.setState(state);
            userDemographicWebQuery.setState(state);
            demographicWebQueryDao.changeStateUserDemographicWebQuery(userDemographicWebQuery);
            trackingService.registerConfigurationTracking(demographicWebQueryC, userDemographicWebQuery, DemographicWebQuery.class);
            userDemographicWebQuery.setPassword("");
            return userDemographicWebQuery;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

}
