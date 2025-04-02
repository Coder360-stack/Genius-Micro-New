package com.example.geniousmicro.Models.UtilityModels

data class LoanStatementModel(
    var LoanCode:String,
    var EMINO:String,
    var EMIDueDate:String,
    var PaymentDate:String,
    var EMIAmount:String,
    var CurrantBalance:String,
    var PayMode:String,
    var LateFine:String,
    var VoucherNo:String,
    var Remarks:String
)
