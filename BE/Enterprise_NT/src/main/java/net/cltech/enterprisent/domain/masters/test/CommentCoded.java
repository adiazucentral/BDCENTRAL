/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *Clase que representa la informacion del maestro de comentario
 * @author enavas
 * @version 1.0.0
 * @since 15/05/2017
 * @see Creación
 */

@ApiObject(
        group = "Prueba",
        name = "Comentario",
        description = "Representa un comentario"
)
public class CommentCoded extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id del comentario", order = 1) 
    private Integer id ;
    @ApiObjectField(name = "code", description = "Codigo del comentario", order = 2)
    private String code;
    @ApiObjectField(name = "message", description = "Mensaje del comentario", order = 3)
    private String message;
    @ApiObjectField(name = "state", description = "Id del estado", order = 4)
    private boolean state ;
    @ApiObjectField(name = "apply", description = "Aplica Para. 1-Aplicacion; 2-Analizadores; 3-Ambo", order = 5)
    private Integer apply ;
    @ApiObjectField(name = "diagnostic", description = "pertenece a un resultado : 1-normal,2- anormal o 3-maligno", order = 6)
    private Integer diagnostic ;
    @ApiObjectField(name = "messageEnglish", description = "Mensaje del comentario en ingles", order = 7)
    private String messageEnglish;


    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Integer getApply()
    {
        return apply;
    }

    public void setApply(Integer apply)
    {
        this.apply = apply;
    }
        public Integer getDiagnostic()
    {
        return diagnostic;
    }

    public void setDiagnostic(Integer diagnostic)
    {
        this.diagnostic = diagnostic;
    }

    public String getMessageEnglish() {
        return messageEnglish;
    }

    public void setMessageEnglish(String messageEnglish) {
        this.messageEnglish = messageEnglish;
    }
    
}
