/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * clase representa pojo para actualizar estado de envio de resultado a his
 *
 * @author hpoveda
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BeanForUpdateStatusSendHis
{

    private long numberOrder;
    private String codeTest;
    private int idTest;
}
