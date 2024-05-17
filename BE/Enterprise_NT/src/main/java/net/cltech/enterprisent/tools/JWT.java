package net.cltech.enterprisent.tools;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTTokenException;

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
     * @param user {@link net.cltech.enterprisent.domain.common.AuthorizedUser}
     * @param time
     * @return Token JWT
     * @throws java.lang.Exception
     */
    public static String generate(AuthorizedUser user, int time) throws Exception
    {
        try
        {
            //La fecha de generacion del token
            Calendar current = Calendar.getInstance();
            //La fecha de expiracion del token
            Calendar expiration = Calendar.getInstance();
            expiration.add(Calendar.MINUTE, time);

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
                    .withClaim("branch", user.getBranch())
                    .withClaim("confidential", user.isConfidential())
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
        user.setBranch(token.getClaim("branch").asInt());
        user.setConfidential(token.getClaim("confidential").asBoolean());
        return user;
    }

    /**
     * Valida el token enviado desde el cliente
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

            Date expireDate = token.getExpiresAt();
            Date currentDate = new Date();
            if (currentDate.after(expireDate))
            {
                //El token ha expirado
                throw new EnterpriseNTTokenException("Token has expired");
            }
        } catch (IllegalArgumentException | UnsupportedEncodingException ex)
        {
            throw new EnterpriseNTTokenException(ex.getMessage());
        }
    }
}
