/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controlador para acceso a la pagina de bienvenida de la api
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/03/2017
 * @see Creacion
 */
@Controller
@RequestMapping("/")
public class IndexController
{

    @RequestMapping(method = RequestMethod.GET)
    public String initPage()
    {
        return "index";
    }
}
