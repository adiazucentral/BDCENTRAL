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
 * Clase que represeta objeto a grudar en base de datos para log de envio de
 * correos
 *
 * @author hpoveda
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrintLog
{

    private long order;
    private int branch;
    private String message;
    private String correos;
    private String branchName;
}
