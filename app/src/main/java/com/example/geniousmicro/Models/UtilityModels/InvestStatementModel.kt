package com.example.geniousmicro.Models.UtilityModels

data class InvestStatementModel(
    var PolicyCode:String,
    var InstNo:String,
    var DueDate:String,
    var DateofRenewal:String,
    var Amount:String,


    var LateFine:String,
    var VoucherNo:String,
    var Remarks:String
)
