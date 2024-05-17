/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la foto de un paciente
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/11/2017
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Foto Paciente",
        description = "Representa la foto tomada a un paciente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientPhoto
{

    @ApiObjectField(name = "id", description = "Id paciente de base de datos", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "photoInBase64", description = "Foto del paciente en base64", required = false, order = 2)
    private String photoInBase64;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getPhotoInBase64()
    {
        return photoInBase64;
    }

    public void setPhotoInBase64(String photoInBase64)
    {
        this.photoInBase64 = photoInBase64;
    }
}
