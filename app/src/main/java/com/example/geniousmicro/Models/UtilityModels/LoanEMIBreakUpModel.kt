package com.example.geniousmicro.Models.UtilityModels

data class LoanEMIBreakUpModel(
    var period: String = "",
    var payDate: String = "",
    var payment: String = "",
    var interest: String = "",
    var principle: String = "",
    var currentBalance: String = "",
    var lateFine: Int = 0
)

