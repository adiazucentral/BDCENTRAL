/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion utilizado para consultar la licencia
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/10/2019
 * @see Creación
 */
@ApiObject(
        group = "Licencia",
        name = "AuthorizedUser",
        description = "Representa la informacion de la licencia en el sistema",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class License
{

    @ApiObjectField(name = "product", description = "Codigo del producto", required = true, order = 1)
    private String product;
    @ApiObjectField(name = "path", description = "Ruta donde se encuentra ubicada la licencia", required = true, order = 2)
    private String path;
    @ApiObjectField(name = "subProduct", description = "Codigo del subproducto: sede o modulo de la aplicacion", required = false, order = 3)
    private String subProduct;
    @ApiObjectField(name = "users", description = "Cantidad de usuarios registrados en el sistema", required = false, order = 4)
    private String users;
    @ApiObjectField(name = "language", description = "Lenguaje en el cual se va a responder el mensaje", required = true, order = 5)
    private String language;

    public License()
    {
    }

    public License(String product, String path, String subProduct, String users, String language)
    {
        this.product = product;
        this.path = path;
        this.subProduct = subProduct;
        this.users = users;
        this.language = language;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getSubProduct()
    {
        return subProduct;
    }

    public void setSubProduct(String subProduct)
    {
        this.subProduct = subProduct;
    }

    public String getUsers()
    {
        return users;
    }

    public void setUsers(String users)
    {
        this.users = users;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

}
