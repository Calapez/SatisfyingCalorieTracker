package pt.brunoponte.satisfyingcalorietracker.util

import java.lang.NumberFormatException

open class AuxMethods {

    companion object {

        fun isInt(str: String) : Boolean  {
            return try {
                str.toInt()
                true
            } catch (e: NumberFormatException) {
                false
            }
        }

    }

}