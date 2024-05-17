/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author hpoveda
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdsPatientOrderTest
{

    private Long orderNumber;
    private long idPatient;
    private List<Long> idTests;

}
