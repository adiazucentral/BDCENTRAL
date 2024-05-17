/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Clases de utilidad para pruebas
 *
 * @version 1.0.0
 * @author dcortes
 * @since 18/04/2017
 * @see Creacion
 */
public class TestTools
{

    /**
     * Representa el tipo de dato json en codificacion utf-8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static String getResponseString(ResultActions request) throws UnsupportedEncodingException
    {
        return request.andReturn().getResponse().getContentAsString();
    }
}
