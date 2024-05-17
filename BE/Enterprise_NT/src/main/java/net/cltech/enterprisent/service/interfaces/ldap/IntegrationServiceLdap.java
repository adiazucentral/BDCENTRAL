package net.cltech.enterprisent.service.interfaces.ldap;

import net.cltech.enterprisent.domain.common.AuthenticationUser;

/**
 * Interfaz de servicios a la informacion para integrar con servidor LDAP.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 24/07/2020
 * @see Creación
 */
public interface IntegrationServiceLdap
{

    /**
     * Verifica las credenciales enviadas al servidor ldap
     *
     * @param user    
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public boolean authenticateLDAP(AuthenticationUser user) throws Exception;

}
