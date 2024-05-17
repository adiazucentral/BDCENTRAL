/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.billing;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la resolución 4505
 * 
 * @version 1.0.0
 * @author javila
 * @since 20/08/2021
 * @see Creación
 */
@ApiObject(
        name = "Resolución Singular (4505)",
        group = "Facturacion",
        description = "Representa la resolución singular (4505)"
)
@Getter
@Setter
public class SingularResolutionTest {
    
    @ApiObjectField(name = "idTest", description = "Id del examen", required = false, order = 1)
    private int idTest;
    @ApiObjectField(name = "codeArea", description = "Cóigo del area", required = true, order = 2)
    private String codeArea;
    @ApiObjectField(name = "nameArea", description = "Nombre del area", required = true, order = 3)
    private String nameArea;
    @ApiObjectField(name = "codeTest", description = "Código del examen", required = true, order = 4)
    private String codeTest;    
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", required = true, order = 5)
    private String nameTest;
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = true, order = 6)
    private Date entryDate;
    @ApiObjectField(name = "testStatus", description = "Estado del examen", required = true, order = 7)
    private int testStatus;
    @ApiObjectField(name = "result", description = "Resultado", required = false, order = 8)
    private String result;
    @ApiObjectField(name = "testComment", description = "Lista de comentarios del examen", required = false, order = 9)
    private String testComment;
    @ApiObjectField(name = "validationDate", description = "Fecha de validación", required = true, order = 10)
    private Date validationDate;
    @ApiObjectField(name = "resultDate", description = "Fecha de resultado", required = true, order = 11)
    private Date resultDate;
    @ApiObjectField(name = "cups", description = "CUPS", required = false, order = 12)
    private String cups;
    @ApiObjectField(name = "verificationDate", description = "Fecha de verificación", required = true, order = 13)
    private int verificationDate;
    @ApiObjectField(name = "userValidation", description = "Usuario validación", required = false, order = 14)
    private String userValidation;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 15)
    private Short testType;
    @ApiObjectField(name = "profile", description = "perfil", required = true, order = 16)
    private Integer profile;
    @ApiObjectField(name = "print", description = "Si se imprime", required = false, order = 17)
    private boolean print;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = false, order = 18)
    private int sampleState;
    @ApiObjectField(name = "idArea", description = "Id de area", required = false, order = 19)
    private int idArea;
    @ApiObjectField(name = "servicePrice", description = "Precio del examen (Servicio)", required = true, order = 20)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio que debe pagar el paciente", required = true, order = 21)
    private BigDecimal patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio que debe pagar el cliente (Aseguradora)", required = true, order = 22)
    private BigDecimal insurancePrice;
    @ApiObjectField(name = "resultState", description = "Estado del resultado", required = true, order = 23)
    private int resultState;
    @ApiObjectField(name = "abbrArea", description = "Abreviatura del area", required = true, order = 24)
    private String abbrArea;
}
