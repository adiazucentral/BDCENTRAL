/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 *
 * @author equijano
 */
public class ReportOrder
{

    private Long order;
    private String report;
    private String name;

    public ReportOrder(Long order, String report) {
        this.order = order;
        this.report = report;
    }
    
    public ReportOrder(Long order, String report, String name)
    {
        this.order = order;
        this.report = report;
        this.name = name;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public String getReport()
    {
        return report;
    }

    public void setReport(String report)
    {
        this.report = report;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
