/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orien.dms.model;

/**
 *
 * @author thinu
 */
public class InvoiceJasp {
    
     private String invoNo;
     private String usName;
     private String prName;
     private String qty;
     private String pvalue;
     private String pvalueTot;
     private String subTot;
     private String noItems;

    public InvoiceJasp(String invoNo, String usName, String prName, String qty, String pvalue, String pvalueTot, String subTot, String noItems) {
        this.invoNo = invoNo;
        this.usName = usName;
        this.prName = prName;
        this.qty = qty;
        this.pvalue = pvalue;
        this.pvalueTot = pvalueTot;
        this.subTot = subTot;
        this.noItems = noItems;
    }

    /**
     * @return the invoNo
     */
    public String getInvoNo() {
        return invoNo;
    }

    /**
     * @param invoNo the invoNo to set
     */
    public void setInvoNo(String invoNo) {
        this.invoNo = invoNo;
    }

    /**
     * @return the usName
     */
    public String getUsName() {
        return usName;
    }

    /**
     * @param usName the usName to set
     */
    public void setUsName(String usName) {
        this.usName = usName;
    }

    /**
     * @return the prName
     */
    public String getPrName() {
        return prName;
    }

    /**
     * @param prName the prName to set
     */
    public void setPrName(String prName) {
        this.prName = prName;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the pvalue
     */
    public String getPvalue() {
        return pvalue;
    }

    /**
     * @param pvalue the pvalue to set
     */
    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    /**
     * @return the pvalueTot
     */
    public String getPvalueTot() {
        return pvalueTot;
    }

    /**
     * @param pvalueTot the pvalueTot to set
     */
    public void setPvalueTot(String pvalueTot) {
        this.pvalueTot = pvalueTot;
    }

    /**
     * @return the subTot
     */
    public String getSubTot() {
        return subTot;
    }

    /**
     * @param subTot the subTot to set
     */
    public void setSubTot(String subTot) {
        this.subTot = subTot;
    }

    /**
     * @return the noItems
     */
    public String getNoItems() {
        return noItems;
    }

    /**
     * @param noItems the noItems to set
     */
    public void setNoItems(String noItems) {
        this.noItems = noItems;
    }
     
     
}
