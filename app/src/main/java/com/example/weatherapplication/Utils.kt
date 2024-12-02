package com.example.weatherapplication

class Utils {
    companion object {
        public fun toCelcius(kelvin: String?): String? {
            lateinit var celc: String
            var kelvInt: Int? = kelvin?.toIntOrNull()
            if (kelvInt != null) {
                var celcInt = kelvInt - 273
                celc = "" + celcInt
            }
            return celc
        }
    }
}