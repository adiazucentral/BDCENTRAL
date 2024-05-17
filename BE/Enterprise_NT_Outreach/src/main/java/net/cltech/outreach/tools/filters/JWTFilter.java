/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.tools.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cltech.outreach.domain.exception.EnterpriseNTTokenException;
import net.cltech.outreach.tools.JWT;
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
                String token = req.getHeader("Authorization");
                //Si el que invoca no es el metodo de autenticación lo deja seguir
                JWT.validateToken(token, req.getServletPath(), req.getMethod());
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

    @Override
    public void destroy()
    {

    }
}
