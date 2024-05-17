/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto con la información de la calificación de turnos para
 * widget del LIS
 *
 * @version 1.0.0
 * @author equijano
 * @since 23/07/2019
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Calificacion del turno",
        description = "Representa la informacion de calificacion del turno para el widget"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TurnsRatingWidgetInfo
{

    @ApiObjectField(name = "ratingCount", description = "Total turnos calificados", required = false, order = 1)
    private int ratingCount;
    @ApiObjectField(name = "rating", description = "Calificación", required = false, order = 2)
    private int rating;
    @ApiObjectField(name = "ratingString", description = "String de la calificación", required = false, order = 3)
    private String ratingString;

    public int getRatingCount()
    {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount)
    {
        this.ratingCount = ratingCount;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getRatingString()
    {
        return ratingString;
    }

    public void setRatingString(String ratingString)
    {
        this.ratingString = ratingString;
    }

}
