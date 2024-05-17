/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.tools.filters;

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
import net.cltech.securitynt.domain.exception.EnterpriseNTTokenException;
import net.cltech.securitynt.tools.JWT;
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
        List<String> urlsExcluded = new ArrayList<>();
        urlsExcluded.add("/api/authentication");
        urlsExcluded.add("/api/configuration/test");
        urlsExcluded.add("/api/configuration/tokenexpiration");
        urlsExcluded.add("/api/authentication/updatepassword");
        urlsExcluded.add("/api/license");
        urlsExcluded.add("/api/sessionviewer/create");
        return !urlsExcluded.stream().filter((t) -> url.startsWith(t)).findAny().isPresent();
    }

    @Override
    public void destroy()
    {

    }
}
