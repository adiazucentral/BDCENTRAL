/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un obejto para realizar la actulizacion de historicos
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 07/06/2022
 * @see Creacion
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTestPatientHistory
{

    private Long numberOrderSinDate;
    private Integer idTest;
    private Integer idPatient;
    private Long numberOrderLastValidate;

    public OrderTestPatientHistory(Long numberOrderSinDate, Integer idTest, Integer idPatient)
    {
        this.numberOrderSinDate = numberOrderSinDate;
        this.idTest = idTest;
        this.idPatient = idPatient;
    }

}
