/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.tools;

import java.util.HashMap;
import net.cltech.enterprisent.service.interfaces.tools.EncodeService;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios utilizados para encriptar y desencriptar un objeto de datos
 *
 * @version 1.0.0
 * @author omendez
 * @see 13/12/2021
 * @see Creación
 */
@Service
public class EncodeServiceEnterpriseNT implements EncodeService {
    
    @Override
    public HashMap<String, String> encrypt(HashMap<String, String> map) throws Exception
    {
        map.replaceAll( ( key, value ) -> Tools.encrypt(value) );
        return map;
    }
    
    @Override
    public HashMap<String, String> decrypt(HashMap<String, String> map) throws Exception
    {
        map.replaceAll( ( key, value ) -> Tools.decrypt(value) );
        return map;
    }
}
