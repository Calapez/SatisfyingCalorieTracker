package pt.brunoponte.satisfyingcalorietracker

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import pt.brunoponte.satisfyingcalorietracker.util.AuxMethods

class DashboardFragment : Fragment(), MediaPlayer.OnCompletionListener {

    private val TAG = "DashboardFragment"

    private var dialogSetCalsGoal: AlertDialog? = null
    private var dialogAddCals: AlertDialog? = null

    // Context Activity
    private lateinit var mActivity : MainActivity

    // Audio vars
    private val successAudio: MediaPlayer by lazy { MediaPlayer.create(mActivity, R.raw.success) }
    private val failAudio: MediaPlayer by lazy { MediaPlayer.create(mActivity, R.raw.fail) }

    // Animation vars
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(mActivity, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(mActivity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(mActivity, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(mActivity, R.anim.to_bottom_anim) }

    private var clicked: Boolean = false

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = context as MainActivity  // Set context activity

        setHasOptionsMenu(true)  // Enable options menu
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClick listeners
        btnEndDay.setOnClickListener { endDay() }
        btnAdd.setOnClickListener { onAddButtonClicked() }
        btnAddCals.setOnClickListener { onAddCalsButtonClicked(); }
        btnAddFood.setOnClickListener { onAddFoodButtonClicked() }

        updateUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_set_calories_goal) {
            setCalsGoalDialog()
            return true
        }
        return false
    }

    private fun updateUi() {
        textCals.text = mActivity.appData.currentCals.toString()
        textCalsGoal.text = mActivity.appData.targetCals.toString()
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
        mActivity.openFoodsFragment()
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

    private fun addCalsDialog() {
        // Create a popup dialog to input name
        val builder = AlertDialog.Builder(mActivity)

        // View to inflate
        val dialogView = View.inflate(mActivity, R.layout.dialog_add_calories, null)
        builder.setView(dialogView)

        val buttonSave = dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener {
            val editCals = dialogView.findViewById<View>(R.id.editCals) as EditText

            // Set new current calories
            val newCals = editCals.text.toString().toInt() + mActivity.appData.currentCals
            mActivity.appData.currentCals = newCals
            textCals.text = newCals.toString()

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
        val builder = AlertDialog.Builder(mActivity)

        // View to inflate
        val dialogView = View.inflate(mActivity, R.layout.dialog_set_calories_goal, null)
        builder.setView(dialogView)

        val buttonSave = dialogView.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener {
            val editCals = dialogView.findViewById<View>(R.id.editCals) as EditText

            // Set new goal calories
            val newGoalCals = editCals.text.toString().toInt()
            mActivity.appData.targetCals = newGoalCals
            textCalsGoal.text = newGoalCals.toString()

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
        if (mActivity.appData.currentCals <= mActivity.appData.targetCals) {
            textCals.setTextColor(mActivity.getColor(R.color.colorGreen))
            textCalsGoal.setTextColor(mActivity.getColor(R.color.colorGreen))
            viewSplit.setBackgroundColor(mActivity.getColor(R.color.colorGreen))
            successAudio.start()
            successAudio.setOnCompletionListener(this)
        } else {
            textCals.setTextColor(mActivity.getColor(android.R.color.holo_red_dark))
            textCalsGoal.setTextColor(mActivity.getColor(android.R.color.holo_red_dark))
            viewSplit.setBackgroundColor(mActivity.getColor(android.R.color.holo_red_dark))
            failAudio.start()
            failAudio.setOnCompletionListener(this)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mActivity.appData.currentCals = 0
        mActivity.appData.targetCals = 0

        textCals.setTextColor(mActivity.getColor(android.R.color.black))
        textCalsGoal.setTextColor(mActivity.getColor(android.R.color.black))
        viewSplit.setBackgroundColor(mActivity.getColor(android.R.color.black))

        updateUi()
    }

}