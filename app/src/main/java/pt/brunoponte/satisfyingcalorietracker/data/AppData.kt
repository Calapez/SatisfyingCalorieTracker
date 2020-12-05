package pt.brunoponte.satisfyingcalorietracker.data

import android.content.Context
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class AppData {

    companion object {
        private const val VERSION = 1 // The latest version of the appData
    }

    var currentCals : Int = 0
    var targetCals: Int = 0

    fun save(ctx: Context) {
        try {
            // Opens app-private file for saved data
            val outFile = ctx.openFileOutput("saved_data", Context.MODE_PRIVATE)
            // Create output stream that targets the app-private file
            val objOut = ObjectOutputStream(outFile)
            writeSaveData(objOut)
            // Close IO objects
            objOut.close()
            outFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** Writes appData content to specific ObjectOutputStream  */
    @Throws(IOException::class)
    private fun writeSaveData(objOut: ObjectOutputStream) {
        objOut.writeInt(VERSION)
        objOut.writeInt(currentCals)
        objOut.writeInt(targetCals)
    }

    fun load(ctx: Context) {
        try {
            val inFile = ctx.openFileInput("saved_data")
            val objIn = ObjectInputStream(inFile)

            // Read save data differently depending on version
            when (objIn.readInt()) {
                1 -> readSaveDataV1(objIn)
                2 -> readSaveDataV2(objIn)
            }
            // Close IO objects
            objIn.close()
            inFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun readSaveDataV1(objIn: ObjectInputStream) {
        currentCals = objIn.readInt()
        targetCals = objIn.readInt()
    }

    @Throws(IOException::class)
    private fun readSaveDataV2(objIn: ObjectInputStream) {
        currentCals = objIn.readInt()
        targetCals = objIn.readInt()
    }
}