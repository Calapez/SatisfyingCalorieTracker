package pt.brunoponte.satisfyingcalorietracker

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var dialogSetCaloriesGoal: AlertDialog? = null
    private var buttonEndDay: Button? = null
    private var editCurrentCalories: EditText? = null
    private var textCaloriesGoal: TextView? = null
    private var viewSplit: View? = null
    private var succesCalories: MediaPlayer? = null
    private var failCalories: MediaPlayer? = null

    // Reference to self
    private var mActivity: MainActivity? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this

        // Init UI elements
        buttonEndDay = findViewById<View>(R.id.buttonEndDay) as Button
        buttonEndDay!!.setOnClickListener { endDay() }
        editCurrentCalories = findViewById<View>(R.id.editCurrentCalories) as EditText
        textCaloriesGoal = findViewById<View>(R.id.textCalorieGoal) as TextView
        viewSplit = findViewById(R.id.viewSplit) as View

        // Create success/fail sounds
        succesCalories = MediaPlayer.create(mActivity, R.raw.success)
        failCalories = MediaPlayer.create(mActivity, R.raw.fail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_set_calories_goal) {
            showSetCaloriesDialog()
            return true
        }
        return false
    }

    private fun showSetCaloriesDialog() {
        // Create a popup dialog to input name
        val builder = AlertDialog.Builder(this)

        // View to inflate
        val dialogView =
            layoutInflater.inflate(R.layout.dialog_set_calories_goal, null)
        builder.setView(dialogView)

        // The arguments (editable values) from the view
        val buttonApply =
            dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonApply.setOnClickListener {
            val editCaloriesGoal =
                dialogView.findViewById<View>(R.id.editCaloriesGoal) as EditText
            try {
                textCaloriesGoal!!.text = editCaloriesGoal.text.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(mActivity, "Calories must be a number", Toast.LENGTH_SHORT)
                    .show()
            }
            dialogSetCaloriesGoal!!.dismiss()
        }

        // Crete & show the dialog
        builder.create()
        dialogSetCaloriesGoal = builder.show()
        dialogSetCaloriesGoal!!.setOnDismissListener{
            dialogSetCaloriesGoal = null
        }
    }

    private fun endDay() {
        try {
            val currentCals = editCurrentCalories!!.text.toString().toInt()
            val goalCals = textCaloriesGoal!!.text.toString().toInt()
            if (currentCals <= goalCals) {
                editCurrentCalories!!.setTextColor(getColor(R.color.colorGreen))
                textCaloriesGoal!!.setTextColor(getColor(R.color.colorGreen))
                viewSplit!!.setBackgroundColor(getColor(R.color.colorGreen))
                succesCalories!!.start()
            } else {
                editCurrentCalories!!.setTextColor(getColor(android.R.color.holo_red_dark))
                textCaloriesGoal!!.setTextColor(getColor(android.R.color.holo_red_dark))
                viewSplit!!.setBackgroundColor(getColor(android.R.color.holo_red_dark))
                failCalories!!.start()
            }
            editCurrentCalories!!.clearFocus()
            buttonEndDay!!.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(mActivity, "Unexpected Error", Toast.LENGTH_SHORT).show()
        }
    }
}