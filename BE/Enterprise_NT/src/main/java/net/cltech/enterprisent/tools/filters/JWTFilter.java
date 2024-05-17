package net.cltech.enterprisent.tools.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cltech.enterprisent.domain.exception.EnterpriseNTTokenException;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.log.integration.SecurityLog;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filtro que intercepta las peticiones y evalua el token de autorizacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creación
 */
public class JWTFilter extends GenericFilterBean
{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        try
        {
            if (req.getMethod().equals("OPTIONS"))
            {
                chain.doFilter(request, response);
            } else
            {
                //Si el que invoca no es el metodo de autenticación lo deja seguir
                if (urlValidate(req.getServletPath()))
                {
                    //Validamos que la cabecera tenga el token de autenticación.
                    String token = req.getHeader("Authorization");
                    if (token == null || token.trim().isEmpty())
                    {
                        res.getOutputStream().print("Not token provided in header request");
                        res.sendError(401, "Not token provided in header request");
                        return;
                    }
                    if (token.startsWith("JWT "))
                    {
                        token = token.substring(4);
                    }
                    JWT.validateToken(token);
                }
                chain.doFilter(request, response);
            }
        } catch (JWTVerificationException ex)
        {
            res.getOutputStream().print("Token not valid");
            res.sendError(401, "Token not valid");
        } catch (EnterpriseNTTokenException ex)
        {
            res.getOutputStream().print(ex.getMessage());
            res.sendError(403, ex.getMessage());
        }
    }

    private boolean urlValidate(String url)
    {
        SecurityLog.info(url);
        List<String> urlsExcluded = new ArrayList<>();
        urlsExcluded.add("/api/authentication");
        urlsExcluded.add("/api/integration/branches");
        urlsExcluded.add("/api/integration/users");
        urlsExcluded.add("/api/integration/authentications/laboratory");
        urlsExcluded.add("/api/dashboard");
        urlsExcluded.add("/api/configuration/test");
        urlsExcluded.add("/api/configuration/configprint");
        urlsExcluded.add("/api/configuration/bodyemail");
        urlsExcluded.add("/api/configuration/tokenexpiration");
        urlsExcluded.add("/api/configuration/securitypolitics");
        urlsExcluded.add("/api/configuration/inbranch");
        urlsExcluded.add("/api/authentication/updatepassword");
        urlsExcluded.add("/authentication/recoverpassword");
        urlsExcluded.add("/api/configuration/passwordrecovery");
        urlsExcluded.add("/api/configuration/updatesecurity");
        urlsExcluded.add("/api/middleware");
        urlsExcluded.add("/api/reports/getBase64/idOrderHis/");
        urlsExcluded.add("/api/mobile/");
        urlsExcluded.add("/api/info/version");
        urlsExcluded.add("/api/orders/orderExistsByExternalSystemOrder/");
        urlsExcluded.add("/api/orders/getUserValidate/idOrder/");
        urlsExcluded.add("/api/his");
        urlsExcluded.add("/api/configuration/getBranchConfiguration");
        urlsExcluded.add("/api/configuration/getorderdigits");
        urlsExcluded.add("/api/encode/encrypt");
        urlsExcluded.add("/api/encode/decrypt");
        urlsExcluded.add("/api/reports/orders/");
        urlsExcluded.add("/api/reports/ordersFacturation/");
        urlsExcluded.add("/api/ingresoNT/StatusNT");
        urlsExcluded.add("/api/ingresoNT/orderNT");
        urlsExcluded.add("/api/ingresoNT/getOrderNT");
        urlsExcluded.add("/api/integration/orderBilling/");
        urlsExcluded.add("/api/laboratories/filter/name/sendExternal/");
        urlsExcluded.add("/api/integration/web/");
        urlsExcluded.add("/api/integration/web/updatestatus/");
        urlsExcluded.add("/api/integration/ingreso/getMessageChangeStateTest/idOrder/");
        urlsExcluded.add("/api/users/integration/");
        urlsExcluded.add("/api/integration/ingreso/updateCentralCode");
        urlsExcluded.add("/api/centralsystems/filter/nameid/");
        urlsExcluded.add("/api/configuration/not/");
        urlsExcluded.add("/api/printLog");
        urlsExcluded.add("/api/integration/resultados/updateSentCentralSystem");
        urlsExcluded.add("/api/reports/billing/");
        urlsExcluded.add("/api/integration/resultados/getOrderByCentralSystem");
        urlsExcluded.add("/api/integration/resultados/resultsByCentralSystemByOrder");
        urlsExcluded.add("/api/results/header");
        urlsExcluded.add("/api/results/detail");
        urlsExcluded.add("/api/results/status");
        urlsExcluded.add("/api/dashboard/getTestValid");
        urlsExcluded.add("/api/integration/resultados/updateSentDashboard");
        urlsExcluded.add("/api/tools/validateToken");

        return !urlsExcluded.stream().filter((t) -> url.startsWith(t)).findAny().isPresent();
    }

    @Override
    public void destroy()
    {

    }
}
