/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author hpoveda
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ResponseOrderNT
{

    private String type;
    private String digitosOrden;
    private String SeparadorMuestra;
    private List<SampleNT> sampleNT;
    private PatientNT patientNT;

}
