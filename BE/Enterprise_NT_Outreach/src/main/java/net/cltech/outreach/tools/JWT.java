/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.tools;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.exception.EnterpriseNTTokenException;

/**
 * Clase que genera y administra los token de seguridad de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creacion
 */
public class JWT
{

    private static final String JWT_KEY = "104F";
    private static final String JWT_AUDIENCE = "Enterprise NT";
    private static final String JWT_ISSUER = "CLTech";

    /**
     * Genera un token a partir de un usuario autenticado
     *
     * @param user {@link net.cltech.outreach.domain.common.AuthorizedUser}
     * @param tokenType Tipo de Token: 1 -> Usuario, 2 -> Recuperar contraseña
     * @return Token JWT
     */
    public static String generate(AuthorizedUser user, int tokenType)
    {
        try
        {
            //La fecha de generacion del token
            Calendar current = Calendar.getInstance();
            //La fecha de expiracion del token
            Calendar expiration = Calendar.getInstance();
            expiration.add(Calendar.MINUTE, 120);

            Algorithm algorithm = Algorithm.HMAC256(JWT.JWT_KEY);
            String token = com.auth0.jwt.JWT.create()
                    .withIssuer(JWT.JWT_ISSUER)
                    .withAudience(JWT.JWT_AUDIENCE)
                    .withExpiresAt(expiration.getTime())
                    .withIssuedAt(current.getTime())
                    .withSubject(Base64.getEncoder().encodeToString(String.valueOf(user.getId()).getBytes()))
                    .withClaim("id", user.getId())
                    .withClaim("lastName", user.getLastName())
                    .withClaim("name", user.getName())
                    .withClaim("user", user.getUserName())
                    .withClaim("type", user.getType())
                    .withClaim("tokenType", tokenType)
                    .withClaim("mail", user.getEmail())
                    .sign(algorithm);
            return token;
        } catch (IllegalArgumentException | UnsupportedEncodingException ex)
        {
            return null;
        }
    }

    /**
     * Decodifica el token de autenticacion y obtiene la informacion del usuario
     *
     * @param request Objeto de la peticion
     * @return {@link net.cltech.outreach.domain.common.AuthorizedUser}
     * @throws IllegalArgumentException Error en la llave del algoritmo de
     * encriptacion del token
     * @throws UnsupportedEncodingException El sistema operativo no soporta el
     * metodo de encriptacion
     */
    public static AuthorizedUser decode(HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException
    {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken.startsWith("JWT "))
        {
            jwtToken = jwtToken.substring(4);
        }
        Algorithm algorithm = Algorithm.HMAC256(JWT.JWT_KEY);
        JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(JWT.JWT_ISSUER)
                .withAudience(JWT.JWT_AUDIENCE)
                .build();
        DecodedJWT token = verifier.verify(jwtToken);
        AuthorizedUser user = new AuthorizedUser();
        user.setId(token.getClaim("id").as(Integer.class));
        user.setLastName(token.getClaim("lastName").asString());
        user.setName(token.getClaim("name").asString());
        user.setUserName(token.getClaim("user").asString());
        user.setType(token.getClaim("type").asInt());
        user.setEmail(token.getClaim("mail").asString());
        return user;
    }

    /**
     * Valida el token enviado desde el cliente
     *
     * @param jwtToken Token
     * @param url Indica la url del servicio.
     * @param method metodo http
     * @throws JWTVerificationException Error validando la llave y origen del
     * token
     * @throws EnterpriseNTTokenException Error en la validación de los claims
     * del token o la expiracion
     */
    public static void validateToken(String jwtToken, String url, String method) throws JWTVerificationException, EnterpriseNTTokenException
    {
        try
        {
            List<String> skipApiServices = Arrays.asList("/api/configuration", "/api/usertypes");
            if (doTokenValidation(url, method))
            {
                if (jwtToken == null || jwtToken.trim().isEmpty())
                {
                    throw new EnterpriseNTTokenException("Token not found");
                }
                if (jwtToken.startsWith("JWT "))
                {
                    jwtToken = jwtToken.substring(4);
                }
                Algorithm algorithm = Algorithm.HMAC256(JWT.JWT_KEY);
                JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                        .withIssuer(JWT.JWT_ISSUER)
                        .withAudience(JWT.JWT_AUDIENCE)
                        .build();
                DecodedJWT token = verifier.verify(jwtToken);
                Claim claim = token.getClaim("id");
                if (claim == null)
                {
                    throw new EnterpriseNTTokenException("Claim id not found in token");
                }
                claim = token.getClaim("lastName");
                if (claim == null)
                {
                    throw new EnterpriseNTTokenException("Claim lastName not found in token");
                }
                claim = token.getClaim("name");
                if (claim == null)
                {
                    throw new EnterpriseNTTokenException("Claim name not found in token");
                }
                claim = token.getClaim("user");
                if (claim == null)
                {
                    throw new EnterpriseNTTokenException("Claim user not found in token");
                }
                claim = token.getClaim("tokenType");
                if (claim != null)
                {
                    if (claim.asInt() == Constants.TOKEN_AUTH_USER && url.equals("/api/authentication/passwordreset"))
                    {
                        throw new EnterpriseNTTokenException("Token not valid");
                    } else if (claim.asInt() == Constants.TOKEN_RESET_USER && !url.equals("/api/authentication/passwordreset"))
                    {
                        throw new EnterpriseNTTokenException("Token not valid");
                    }
                } else
                {
                    throw new EnterpriseNTTokenException("Claim token type not found in token");
                }

                Date expireDate = token.getExpiresAt();
                Date currentDate = new Date();
                if (currentDate.after(expireDate))
                {
                    //El token ha expirado
                    throw new EnterpriseNTTokenException("Token has expired");
                }
            }
        } catch (IllegalArgumentException | UnsupportedEncodingException ex)
        {
            throw new EnterpriseNTTokenException(ex.getMessage());
        }
    }

    /**
     * Valida si el servicio requiere autheticacion con token
     *
     * @param url url del servicio
     * @param method Metodo HTTP
     * @return true si requiere validacion
     */
    public static boolean doTokenValidation(String url, String method)
    {
        if (url.startsWith("/api/authentication"))
        {
            return false;
        } else if (url.equals("/api/configuration") && method.equalsIgnoreCase("GET")) 
        {
            return false;
        }
//        else if (url.startsWith("/api/configuration") && method.equalsIgnoreCase("GET"))
//        {
//            return false;
//        }
//        else if (url.startsWith("/api/configuration/encrypted") && method.equalsIgnoreCase("GET"))
//        {
//            return false;
//        } 
//        else if (url.startsWith("/api/usertypes") && method.equalsIgnoreCase("GET"))
//        {
//            return false;
//        } 
//        else if (url.startsWith("/api/configuration/documenttypes") && method.equalsIgnoreCase("GET"))
//        {
//            return false;
//        } 
        else if (url.startsWith("/api/authentication/email") && method.equalsIgnoreCase("POST"))
        {
            return false;
        }
//        else if (url.startsWith("/api/demographic/webquery") && method.equalsIgnoreCase("GET"))
//        {
//            return false;
//        }
        return true;

    }
}
