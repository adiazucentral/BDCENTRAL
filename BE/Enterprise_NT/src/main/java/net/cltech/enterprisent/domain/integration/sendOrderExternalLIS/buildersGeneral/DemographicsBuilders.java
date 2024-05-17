/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral;

import java.util.List;

/**
 *
 * @author hpoveda
 */
public interface DemographicsBuilders
{
    //atributos comunes a todos los pacientes

    DemographicsBuilders setIdDemographic(int idDemographic);

    DemographicsBuilders setDemographic(String demographic);

    DemographicsBuilders setEncoded(boolean encoded);

    DemographicsBuilders setNotCodifiedValue(String notCodifiedValue);

    DemographicsBuilders setCodifiedId(Integer codifiedId);

    DemographicsBuilders setCodifiedCode(String codifiedCode);

    DemographicsBuilders setHomologationCodeCentralSystem(List<String> homologationCodeCentralSystem);

}
