package com.example.geniousmicro.Models.UtilityModels

data class SBStatementModel(
    var TransactionNo:String,
    //var ApplicantName:String,
    var TDate:String,
    var DepositAmount:String,
    var WithdrawlAmount:String,
    var Remarks:String,
    var PayMode:String
)
