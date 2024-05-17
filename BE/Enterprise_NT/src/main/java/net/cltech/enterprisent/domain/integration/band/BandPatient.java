/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.band;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a un paciente de la aplicacion
 *
 * @version 1.0.0
 * @author omendez
 * @since 24/11/2020
 * @see Creacion
 */
@ApiObject(
    group = "Banda Transportadora",
    name = "Paciente Banda",
    description = "Representa un paciente dentro de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BandPatient 
{
    @ApiObjectField(name = "idtype", description = "Tipo de documento", required = true, order = 1)
    private String idtype;
    @ApiObjectField(name = "patientid", description = "Id paciente", required = true, order = 2)
    private String patientid;

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }
}
