package net.cltech.enterprisent.controllers.tools;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.exception.EnterpriseNTTokenException;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;

/**
 * Clase que genera y administra los token de seguridad de la aplicacion
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/08/2020
 * @see Creacion
 */
public class JWTPatient
{

    private static final String JWT_KEY = "104F";
    private static final String JWT_AUDIENCE = "Enterprise NT";
    private static final String JWT_KEY_LICENSE = "CLTech0#{Year}/{Month}/{Day}SKLSEC";
    private static final String JWT_ISSUER = "CLTech";

    /**
     * Genera un token a partir de un usuario autenticado
     *
     * @param user {@link domain.integration.mobile.AuthorizedPatient}
     * @param tokentype
     * @return Token JWT
     * @throws java.lang.Exception
     */
    public static String generate(AuthorizedPatient user, int tokentype) throws Exception
    {
        try
        {
            //La fecha de generacion del token
            Calendar current = Calendar.getInstance();

            Algorithm algorithm = Algorithm.HMAC256(JWTPatient.JWT_KEY);
            String token = com.auth0.jwt.JWT.create()
                    .withIssuer(JWTPatient.JWT_ISSUER)
                    .withAudience(JWTPatient.JWT_AUDIENCE)
                    .withIssuedAt(current.getTime())
                    .withSubject(Base64.getEncoder().encodeToString(String.valueOf(user.getId()).getBytes()))
                    .withClaim("id", user.getId())
                    .withClaim("lastName", user.getLastName())
                    .withClaim("name", user.getName1())
                    .withClaim("user", user.getEmail())
                    .withClaim("tokenType", tokentype)
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
     * @return {@link net.cltech.enterprisent.domain.common.AuthorizedUser}
     * @throws IllegalArgumentException Error en la llave del algoritmo de
     * encriptacion del token
     * @throws UnsupportedEncodingException El sistema operativo no soporta el
     * metodo de encriptacion
     */
    public static AuthorizedPatient decode(HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException
    {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken.startsWith("JWTPatient "))
        {
            jwtToken = jwtToken.substring(11);
        }
        Algorithm algorithm = Algorithm.HMAC256(JWTPatient.JWT_KEY);
        JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(JWTPatient.JWT_ISSUER)
                .withAudience(JWTPatient.JWT_AUDIENCE)
                .build();
        DecodedJWT token = verifier.verify(jwtToken);
        AuthorizedPatient user = new AuthorizedPatient();
        user.setId(token.getClaim("id").as(Integer.class));
        user.setLastName(token.getClaim("lastName").asString());
        user.setName1(token.getClaim("name1").asString());
        user.setEmail(token.getClaim("user").asString());

        return user;
    }

    /**
     * Valida el token enviado desde la app por el paciente
     *
     * @param jwtToken Token
     * @throws JWTVerificationException Error validando la llave y origen del
     * token
     * @throws EnterpriseNTTokenException Error en la validación de los claims
     * del token o la expiracion
     */
    public static void validateToken(String jwtToken) throws JWTVerificationException, EnterpriseNTTokenException
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(JWTPatient.JWT_KEY);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                    .withIssuer(JWTPatient.JWT_ISSUER)
                    .withAudience(JWTPatient.JWT_AUDIENCE)
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
            claim = token.getClaim("name1");
            if (claim == null)
            {
                throw new EnterpriseNTTokenException("Claim name not found in token");
            }
            claim = token.getClaim("user");
            if (claim == null)
            {
                throw new EnterpriseNTTokenException("Claim user not found in token");
            }

        } catch (IllegalArgumentException | UnsupportedEncodingException ex)
        {
            throw new EnterpriseNTTokenException(ex.getMessage());
        }
    }

}
