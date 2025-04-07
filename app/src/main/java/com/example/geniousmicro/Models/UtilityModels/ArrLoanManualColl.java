package com.example.geniousmicro.Models.UtilityModels;

public class ArrLoanManualColl {

    public String getLoanCode() {
        return LoanCode;
    }

    public void setLoanCode(String loanCode) {
        LoanCode = loanCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEMIAmount() {
        return EMIAmount;
    }

    public void setEMIAmount(String EMIAmount) {
        this.EMIAmount = EMIAmount;
    }

    public String getDateofEntry() {
        return DateofEntry;
    }

    public void setDateofEntry(String dateofEntry) {
        DateofEntry = dateofEntry;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String LoanCode,UserName,EMIAmount,DateofEntry,Status;
}
