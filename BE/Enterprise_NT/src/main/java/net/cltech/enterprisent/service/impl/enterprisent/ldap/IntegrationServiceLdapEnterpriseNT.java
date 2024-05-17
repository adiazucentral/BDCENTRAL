package net.cltech.enterprisent.service.impl.enterprisent.ldap;

import java.util.*;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.service.interfaces.ldap.IntegrationServiceLdap;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de servicio Ldap
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 24/07/2020
 * @see Creacion
 */
@Service
public class IntegrationServiceLdapEnterpriseNT implements IntegrationServiceLdap
{

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public boolean authenticateLDAP(AuthenticationUser user) throws Exception
    {

        final String domain = configurationService.getValue("DomainLDAP");
        boolean value;
        value = loginLDAP(user.getUser(), user.getPassword(), domain);
        return value;
    }

    /**
     * Verifica las credenciales enviadas al servidor ldap ya con el servidor
     *
     * @param user
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public static boolean loginLDAP(String user, String password, String domain) throws Exception
    {

        Hashtable env = new Hashtable();
        String dnComplement = toDC(domain);

        try
        {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + domain + ":389/" + dnComplement);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);//DN es el nombre distinguido a usar como base de búsqueda.
            env.put(Context.SECURITY_CREDENTIALS, password);

        } catch (Exception e)
        {
            throw new Exception(e);
        }

        try
        {
            DirContext ctx = new InitialDirContext(env);
            return true;
        } catch (AuthenticationNotSupportedException ex)
        {
            List<String> errors = new ArrayList<>();
            errors.add("1|LDAP The authentication is not supported by the server");
            throw new EnterpriseNTException(errors);
        } catch (AuthenticationException ex)
        {
            List<String> errors = new ArrayList<>();
            errors.add("2|Incorrect password or username LDAP");
            throw new EnterpriseNTException(errors);
        } catch (NamingException ex)
        {
            List<String> errors = new ArrayList<>();
            errors.add("3|LDAP fail conection");
            throw new EnterpriseNTException(errors);
        }
    }
    
    private static String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if(token.length()==0) continue;
            if(buf.length()>0)
            buf.append("dc=").append(token);
        }
        return buf.toString();
    }


}
