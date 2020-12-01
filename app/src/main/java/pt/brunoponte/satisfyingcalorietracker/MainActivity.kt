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
    private var dialogSetCalsGoal: AlertDialog? = null
    private var dialogAddCals: AlertDialog? = null

    private lateinit var btnEndDay: Button
    private lateinit var textCals: TextView
    private lateinit var textCalsGoal: TextView
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
        textCals = findViewById<View>(R.id.textCals) as TextView
        textCalsGoal = findViewById<View>(R.id.textCalsGoal) as TextView
        viewSplit = findViewById<View>(R.id.viewSplit) as View

        // Click listeners
        btnEndDay.setOnClickListener { endDay() }
        btnAdd.setOnClickListener { onAddButtonClicked() }
        btnAddCals.setOnClickListener { onAddCalsButtonClicked(); }
        btnAddFood.setOnClickListener { onAddFoodButtonClicked() }
    }

    private fun onAddButtonClicked() {
        clicked = !clicked

        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
    }

    private fun onAddCalsButtonClicked() {
        addCalsDialog()
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
            setCalsGoalDialog()
            return true
        }
        return false
    }

    private fun addCalsDialog() {
        // Create a popup dialog to input name
        val builder = AlertDialog.Builder(this)

        // View to inflate
        val dialogView = View.inflate(this, R.layout.dialog_add_calories, null)
        builder.setView(dialogView)

        val buttonSave = dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener {
            val editCals = dialogView.findViewById<View>(R.id.editCals) as EditText
            textCals.text = (editCals.text.toString().toInt() + textCals.text.toString().toInt())
                .toString()

            dialogAddCals!!.dismiss()
        }

        // Crete & show the dialog
        builder.create()
        dialogAddCals = builder.show()
        dialogAddCals!!.setOnDismissListener{
            dialogAddCals = null
        }
    }

    private fun setCalsGoalDialog() {
        // Create a popup dialog to input name
        val builder = AlertDialog.Builder(this)

        // View to inflate
        val dialogView = View.inflate(this, R.layout.dialog_set_calories_goal, null)
        builder.setView(dialogView)

        val buttonSave = dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener {
            val editCals = dialogView.findViewById<View>(R.id.editCals) as EditText
            textCalsGoal.text = editCals.text.toString()

            dialogSetCalsGoal!!.dismiss()
        }

        // Crete & show the dialog
        builder.create()
        dialogSetCalsGoal = builder.show()
        dialogSetCalsGoal!!.setOnDismissListener{
            dialogSetCalsGoal = null
        }
    }

    private fun endDay() {
        val currentCalsTxt = textCals.text.toString()
        val goalCalsTxt = textCalsGoal.text.toString()

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
                    textCals.setTextColor(getColor(R.color.colorGreen))
                    textCalsGoal.setTextColor(getColor(R.color.colorGreen))
                    viewSplit.setBackgroundColor(getColor(R.color.colorGreen))
                    succesCalories.start()
                } else {
                    textCals.setTextColor(getColor(android.R.color.holo_red_dark))
                    textCalsGoal.setTextColor(getColor(android.R.color.holo_red_dark))
                    viewSplit.setBackgroundColor(getColor(android.R.color.holo_red_dark))
                    failCalories.start()
                }

                //buttonEndDay!!.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(mActivity, "Unexpected Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}