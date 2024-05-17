package net.cltech.enterprisent.controllers.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.exception.WebException;
import net.cltech.enterprisent.service.interfaces.exception.ExceptionService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase encargada de mapear los errores generados por la api transformadolos en
 * un json con la exception
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creación
 */
@ControllerAdvice
public class ExceptionHandlerController
{

    @Autowired
    private ExceptionService service;

    @ExceptionHandler(value =
    {
        SQLException.class, EnterpriseNTException.class, Exception.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<WebException> generalExceptionHandler(HttpServletResponse response, HttpServletRequest request, Exception ex)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        String message = stringWriter.toString();
        message = (message.length() >= 4000) ? message.substring(0, 3999) : message;
        WebException we = new WebException();
        we.setId(null);
        we.setDate(new Date());
        we.setMessage(ex.getMessage());
        we.setUrl(request.getRequestURI());
        we.setHost(request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI())));
        we.setDetail(message);
        we.setType(Constants.ERROR_BACKEND);
        if (request.getHeader("Authorization") != null && !request.getHeader("Authorization").trim().isEmpty())
        {
            try
            {
                AuthorizedUser userA = JWT.decode(request);
                we.setId(userA.getId());
                we.setUser(userA.getUserName());
            } catch (IllegalArgumentException | UnsupportedEncodingException ex1)
            {
                we.setId(null);
                we.setUser(null);
            }
        } else
        {
            we.setId(null);
            we.setUser(null);
        }
        if (ex instanceof SQLException) //SQLException: Error en base de datos
        {
            we.setCode(1);
            Log.error(ExceptionHandlerController.class, ex);
        } else if (ex instanceof EnterpriseNTException) //Exception: Error interno de la aplicacion
        {
            we.setCode(2);
            we.setErrorFields(((EnterpriseNTException) ex).getErrorFields());
        } else if (ex instanceof Exception) //Exception: Error no controlado
        {
            we.setCode(0);
            Log.error(ExceptionHandlerController.class, ex);
        }
        try
        {
            we = service.save(we);
        } catch (Exception ex1)
        {
            Log.error(ExceptionHandlerController.class, ex1);
        }
        return new ResponseEntity(we, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
