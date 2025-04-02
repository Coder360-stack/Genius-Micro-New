package com.example.geniousmicro.mssql.model;

public class SetGetPolicyDueReport {
    String loanCode;
    String ApplicantName;
    String phNo;
    String totalDueEmiNo;
    String totalDueEmiAmnt;

    public String getNoofPendingRenewal() {
        return NoofPendingRenewal;
    }

    public void setNoofPendingRenewal(String noofPendingRenewal) {
        NoofPendingRenewal = noofPendingRenewal;
    }

    String NoofPendingRenewal;

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getTotalDueEmiNo() {
        return totalDueEmiNo;
    }

    public void setTotalDueEmiNo(String totalDueEmiNo) {
        this.totalDueEmiNo = totalDueEmiNo;
    }

    public String getTotalDueEmiAmnt() {
        return totalDueEmiAmnt;
    }

    public void setTotalDueEmiAmnt(String totalDueEmiAmnt) {
        this.totalDueEmiAmnt = totalDueEmiAmnt;
    }
}
