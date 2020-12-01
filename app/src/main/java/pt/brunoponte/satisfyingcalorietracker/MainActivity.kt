package pt.brunoponte.satisfyingcalorietracker

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pt.brunoponte.satisfyingcalorietracker.util.AuxMethods

class MainActivity : AppCompatActivity() {
    private var dialogSetCaloriesGoal: AlertDialog? = null

    private lateinit var btnEndDay: Button
    private lateinit var editCurrCals: EditText
    private lateinit var textGoalCals: TextView
    private lateinit var viewSplit: View

    // Audio vars
    private val succesCalories: MediaPlayer by lazy { MediaPlayer.create(mActivity, R.raw.success) }
    private val failCalories: MediaPlayer by lazy { MediaPlayer.create(mActivity, R.raw.fail) }

    // Animation vars
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var clicked: Boolean = false

    // Reference to self
    private lateinit var mActivity: MainActivity

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this

        // Init UI elements
        btnEndDay = findViewById<View>(R.id.buttonEndDay) as Button
        editCurrCals = findViewById<View>(R.id.editCurrentCalories) as EditText
        textGoalCals = findViewById<View>(R.id.textCalorieGoal) as TextView
        viewSplit = findViewById<View>(R.id.viewSplit) as View

        // Click listeners
        btnEndDay.setOnClickListener { endDay() }
        btnAdd.setOnClickListener { onAddButtonClicked() }
        btnAddCals.setOnClickListener { onAddCalsButtonClicked() }
        btnAddFood.setOnClickListener { onAddFoodButtonClicked() }
    }

    private fun onAddButtonClicked() {
        clicked = !clicked

        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
    }

    private fun onAddCalsButtonClicked() {
        Toast.makeText(this, "Add calories", Toast.LENGTH_SHORT).show()
    }

    private fun onAddFoodButtonClicked() {
        Toast.makeText(this, "Add food", Toast.LENGTH_SHORT).show()
    }

    private fun setVisibility(clicked: Boolean) {
        if (clicked) {
            btnAddCals.visibility = View.VISIBLE
            btnAddFood.visibility = View.VISIBLE
        } else {
            btnAddCals.visibility = View.VISIBLE
            btnAddFood.visibility = View.VISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (clicked) {
            btnAdd.startAnimation(rotateOpen)
            btnAddCals.startAnimation(fromBottom)
            btnAddFood.startAnimation(fromBottom)
        } else {
            btnAdd.startAnimation(rotateClose)
            btnAddCals.startAnimation(toBottom)
            btnAddFood.startAnimation(toBottom)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (clicked) {
            btnAddCals.isClickable = true
            btnAddFood.isClickable = true
        } else {
            btnAddCals.isClickable = false
            btnAddFood.isClickable = false
        }
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
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_calories_goal, null)
        builder.setView(dialogView)

        // The arguments (editable values) from the view
        val buttonApply = dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonApply.setOnClickListener {
            val editCaloriesGoal =
                dialogView.findViewById<View>(R.id.editCaloriesGoal) as EditText
            try {
                textGoalCals.text = editCaloriesGoal.text.toString()
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
        val currentCalsTxt = editCurrCals.text.toString()
        val goalCalsTxt = textGoalCals.text.toString()

        if (currentCalsTxt.isBlank()) {
            Toast.makeText(mActivity, "Insert your current calories",
                Toast.LENGTH_SHORT).show()
        } else if (!AuxMethods.isInt(currentCalsTxt)) {
            Toast.makeText(mActivity, "Your current calories must be a number",
                Toast.LENGTH_SHORT).show()
        } else if (goalCalsTxt.isBlank()) {
            Toast.makeText(mActivity, "Insert your goal calories",
                Toast.LENGTH_SHORT).show()
        } else if (!AuxMethods.isInt(goalCalsTxt)) {
            Toast.makeText(mActivity, "Your goal calories must be a number" ,
                Toast.LENGTH_SHORT).show()
        } else {
            try {
                val currentCals = currentCalsTxt.toInt()
                val goalCals = goalCalsTxt.toInt()

                if (currentCals <= goalCals) {
                    editCurrCals.setTextColor(getColor(R.color.colorGreen))
                    textGoalCals.setTextColor(getColor(R.color.colorGreen))
                    viewSplit.setBackgroundColor(getColor(R.color.colorGreen))
                    succesCalories.start()
                } else {
                    editCurrCals.setTextColor(getColor(android.R.color.holo_red_dark))
                    textGoalCals.setTextColor(getColor(android.R.color.holo_red_dark))
                    viewSplit.setBackgroundColor(getColor(android.R.color.holo_red_dark))
                    failCalories.start()
                }
                editCurrCals.clearFocus()

                //buttonEndDay!!.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(mActivity, "Unexpected Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}