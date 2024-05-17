package net.cltech.enterprisent.domain.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto para realizar la configuracion inicial
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/05/2018
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "Inicial",
        description = "Objeto para establecer la configuración inicial"
)
public class InitialConfiguration
{

    @ApiObjectField(name = "configuration", description = "Llaves de configuracion", order = 1)
    private List<Configuration> config;
    @ApiObjectField(name = "user", description = "Datos de usuario de administracion", order = 2)
    private User user;
    @ApiObjectField(name = "branch", description = "Sede inicial", order = 3)
    private Branch branch;

    public InitialConfiguration()
    {
    }

    public List<Configuration> getConfig()
    {
        return config;
    }

    public void setConfig(List<Configuration> config)
    {
        this.config = config;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

}
