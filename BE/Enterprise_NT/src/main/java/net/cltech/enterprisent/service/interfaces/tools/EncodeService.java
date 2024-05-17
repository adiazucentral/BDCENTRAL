/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.tools;

import java.util.HashMap;

/**
 * Interfaz de servicios a la informacion encode
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/12/2021
 * @see Creación
 */
public interface EncodeService {
    
     /**
     * Encripta la información enviada
     *
     * @param map Objeto con los valores a encriptar
     *
     * @return Objeto con los valores encriptados
     * @throws Exception Error en la base de datos.
     */
    public HashMap<String, String> encrypt(HashMap<String, String> map) throws Exception;
    
     /**
     * Desencripta la información enviada
     *
     * @param map Objeto con los valores a desencriptar
     *
     * @return Objeto con los valores desencriptados
     * @throws Exception Error en la base de datos.
     */
    public HashMap<String, String> decrypt(HashMap<String, String> map) throws Exception;
    
}
