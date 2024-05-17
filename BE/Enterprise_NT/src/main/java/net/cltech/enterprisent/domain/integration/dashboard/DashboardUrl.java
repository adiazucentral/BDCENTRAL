/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un el objeto para la prueba de url.
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/12/2018
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Url del tablero dashboard",
        description = "Representa el objeto de la url del tablero dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardUrl
{

    @ApiObjectField(name = "url", description = "url del tablero", order = 1)
    private String url;
    @ApiObjectField(name = "board", description = "Tablero a consultar", order = 2)
    private String board;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getBoard()
    {
        return board;
    }

    public void setBoard(String board)
    {
        this.board = board;
    }

}
