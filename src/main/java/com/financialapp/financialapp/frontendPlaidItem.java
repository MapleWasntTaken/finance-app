package com.financialapp.financialapp;

import java.util.List;

public class frontendPlaidItem {
    private String institution;
    private Double balance;
    private String type;
    private Double creditUsed =0D;
    private List<TransactionItem> transactionItems;
    private String institutionId;
    public String getInstitutionId() {
        return institutionId;
    }
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }
    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }
    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Double getCreditUsed(){
        return this.creditUsed;
    }
    public void setCreditUsed(Double creditUsed){
        if(this.type.equals("Credit")){
            this.creditUsed = creditUsed;
        }
    }
    
}
