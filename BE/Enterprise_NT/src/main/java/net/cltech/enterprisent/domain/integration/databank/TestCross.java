/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.databank;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa las pruebas cruzadas enviadas desde databank  
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/11/2020
 * @see Creación
 */
@ApiObject(
        group = "Integracion",
        name = "Pruebas Cruzadas",
        description = "Representa las pruebas cruzadas enviadas desde databank"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCross 
{
    @ApiObjectField(name = "code", description = "Código", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 2)
    private String result;
    @ApiObjectField(name = "bag", description = "Descripción", required = true, order = 3)
    private String bag;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 4)
    private String sample;
    @ApiObjectField(name = "validateDate", description = "Fecha de validación", required = true, order = 5)
    private String validateDate;
    @ApiObjectField(name = "usertest", description = "Usuario", required = true, order = 6)
    private UserDatabank usertest;
    @ApiObjectField(name = "hemocomponentCode", description = "Código Hemocomponente", required = true, order = 7)
    private String hemocomponentCode;
    @ApiObjectField(name = "bagExternalNumber", description = "Código Hemocomponente", required = true, order = 7)
    private String bagExternalNumber;
    @ApiObjectField(name = "bagSeal", description = "Código Hemocomponente", required = true, order = 7)
    private String seal;
    

    public TestCross() {
        usertest = new UserDatabank();
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(String validateDate) {
        this.validateDate = validateDate;
    }

    public UserDatabank getUsertest() {
        return usertest;
    }

    public void setUsertest(UserDatabank usertest) {
        this.usertest = usertest;
    }

    public String getHemocomponentCode() {
        return hemocomponentCode;
    }

    public void setHemocomponentCode(String hemocomponentCode) {
        this.hemocomponentCode = hemocomponentCode;
    }

    public String getBagExternalNumber() {
        return bagExternalNumber;
    }

    public void setBagExternalNumber(String bagExternalNumber) {
        this.bagExternalNumber = bagExternalNumber;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }
    
    
}
