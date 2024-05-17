package net.cltech.enterprisent.service.impl.enterprisent.masters.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.ModuleDao;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.service.interfaces.masters.user.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro de modulos para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 30/05/2017
 * @see Creaciòn
 */
@Service
public class ModuleServiceEnterpriseNT implements ModuleService
{

    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private ConfigurationDao configDao;

    @Override
    public List<Module> list() throws Exception
    {
        List<Module> items = removeInactiveModules(moduleDao.list());
        List<Module> padres = items.stream()
                .filter(module -> module.getIdFather() == 0)
                .collect(Collectors.toList());
        List<Module> resultado = new ArrayList<>();

        for (Module padre : padres)
        {
            items.remove(padre);
            padre.setSubmodules(subModules(padre.getId(), items));
            resultado.add(padre);
        }
        return resultado;
    }

    @Override
    public List<Module> filterByUser(Integer id) throws Exception
    {
        return moduleDao.findByUser(id).stream().distinct().collect(Collectors.toList());
    }

    public List<Module> subModules(Integer fatherId, List<Module> data)
    {
        List<Module> resultado = new ArrayList<>();

        for (Module children : data)
        {
            if (Objects.equals(children.getIdFather(), fatherId))
            {
                children.setSubmodules(subModules(children.getId(), data));
                resultado.add(children);
            }
        }

        return resultado;
    }

    /**
     * Remueve los modulos que se encuentra inactivos por configuración
     *
     * @param modules Lista de módulos del sistema
     *
     * @return Lista de modulos permitidos
     * @throws Exception Error
     */
    public List<Module> removeInactiveModules(List<Module> modules) throws Exception
    {
        boolean service = configDao.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean phisician = configDao.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = configDao.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean account = configDao.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean pyp = !configDao.get("DemograficoPyP").getValue().equalsIgnoreCase("");
        boolean race = configDao.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean billingUsa = configDao.get("Facturacion").getValue().equalsIgnoreCase("2");

        return modules.stream()
                .filter(f -> (service || !f.getId().equals(107)))
                .filter(f -> (phisician || !f.getId().equals(96)))
                .filter(f -> (phisician || !f.getId().equals(97)))
                .filter(f -> (race || !f.getId().equals(101)))
                .filter(f -> (billingUsa || !f.getId().equals(17)))
                .filter(f -> (billingUsa || !f.getId().equals(18)))
                .filter(f -> (billingUsa || !f.getId().equals(19)))
                .filter(f -> (rate || !f.getId().equals(3)))
                .filter(f -> (rate || !f.getIdFather().equals(3)))
                .filter(f -> (account || !f.getId().equals(23)))
                .filter(f -> (account || !f.getId().equals(20)))
                .filter(f -> (pyp || !f.getId().equals(58)))
                .collect(Collectors.toList());

    }

}
