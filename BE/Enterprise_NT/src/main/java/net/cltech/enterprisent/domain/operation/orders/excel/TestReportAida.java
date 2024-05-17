/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author omendez
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestReportAida {
    
    @ApiObjectField(name = "priceAccount", description = "Precio Empresa", required = true, order = 1)
    private Float priceAccount;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del examen", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "nameStadistic", description = "Nombre Estadistico del examen", required = true, order = 4)
    private String nameStadistic;
    @ApiObjectField(name = "codeCups", description = "Indica el codigo cups del examen", required = true, order = 5)
    private String codeCups;
    @ApiObjectField(name = "codeArea", description = "Codigo del area del examen", required = true, order = 6)
    private String codeArea;
    @ApiObjectField(name = "nameArea", description = "Nombre del area", required = true, order = 7)
    private String nameArea;
    @ApiObjectField(name = "codeNivel", description = "Codigo del nivel", required = true, order = 8)
    private String codeNivel;
    @ApiObjectField(name = "level", description = "Nivel", required = true, order = 9)
    private String level;
    @ApiObjectField(name = "codeLaboratory", description = "Codigo del laboratorio", required = true, order = 10)
    private String codeLaboratory;
    @ApiObjectField(name = "laboratory", description = "Laboratorio", required = true, order = 11)
    private String laboratory;
    @ApiObjectField(name = "dateOrdered", description = "Fecha Ingreso", required = false, order = 12)
    private Date dateOrdered;
    @ApiObjectField(name = "dateResult", description = "Fecha Resultado", required = false, order = 13)
    private Date dateResult;
    @ApiObjectField(name = "dateValidation", description = "Fecha Validación", required = false, order = 14)
    private Date dateValidation;
    @ApiObjectField(name = "datePrint", description = "Fecha Impresion", required = false, order = 15)
    private Date datePrint;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 16)
    private int pathology;
    @ApiObjectField(name = "antibiogram", description = "Indica si tiene antibiograma", required = true, order = 17)
    private int antibiogram;
    @ApiObjectField(name = "dateTake", description = "Fecha de toma", required = false, order = 18)
    private Date dateTake;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 19)
    private String result;
    @ApiObjectField(name = "status", description = "Estado del examen", required = true, order = 20)
    private int status;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 21)
    private String comment;
    @ApiObjectField(name = "dateVerification", description = "Fecha de verificacion", required = true, order = 22)
    private int dateVerification;
    @ApiObjectField(name = "userValidation", description = "Usuario que valida", required = true, order = 23)
    private String userValidation;
    @ApiObjectField(name = "profileId", description = "Identificador del perfil", required = true, order = 24)
    private int profileId;
    @ApiObjectField(name = "profileName", description = "Nombre del perfil", required = true, order = 25)
    private String profileName;
    @ApiObjectField(name = "profileCode", description = "Código del perfil", required = true, order = 26)
    private String profileCode;
    @ApiObjectField(name = "profileAbbr", description = "Abreviatura del perfil", required = true, order = 27)
    private String profileAbbr;
}
