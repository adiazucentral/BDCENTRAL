package net.cltech.enterprisent.print_nt.bussines.domain;

import java.util.List;

/**
 * Representa clase para tener informacion de la orden para imprimir
 *
 * @version 1.0.0
 * @author equijano
 * @since 27/05/2019
 * @see Creación
 */
public class PrintOrder
{

    private Physician physician;
    private List<PrintOrderInfo> listOrders;

    public Physician getPhysician()
    {
        return physician;
    }

    public void setPhysician(Physician physician)
    {
        this.physician = physician;
    }

    public List<PrintOrderInfo> getListOrders()
    {
        return listOrders;
    }

    public void setListOrders(List<PrintOrderInfo> listOrders)
    {
        this.listOrders = listOrders;
    }

}
