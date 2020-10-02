package pt.brunoponte.satisfyingcalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Dialog dialogSetCaloriesGoal;
    private Button buttonEndDay;
    private EditText editCurrentCalories;
    private TextView textCaloriesGoal;
    private View viewSplit;

    private MediaPlayer succesCalories;
    private MediaPlayer failCalories;

    // Reference to self
    private MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        buttonEndDay = (Button) findViewById(R.id.buttonEndDay);
        editCurrentCalories = (EditText) findViewById(R.id.editCurrentCalories);
        textCaloriesGoal = (TextView) findViewById(R.id.textCalorieGoal);
        viewSplit = (View) findViewById(R.id.viewSplit);

        succesCalories = MediaPlayer.create(mActivity, R.raw.success);
        failCalories = MediaPlayer.create(mActivity, R.raw.fail);

        buttonEndDay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDay();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_set_calories_goal) {
            showSetCaloriesDialog();
            return true;
        }

        return false;
    }

    private void showSetCaloriesDialog() {
        // Create a popup dialog to input name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // View to inflate
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_calories_goal, null);
        builder.setView(dialogView);

        // The arguments (editable values) from the view
        final Button buttonApply = (Button) dialogView.findViewById(R.id.buttonSave);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editCaloriesGoal = (EditText) dialogView.findViewById(R.id.editCaloriesGoal);

                try {
                    textCaloriesGoal.setText(editCaloriesGoal.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, "Calories must be a number", Toast.LENGTH_SHORT).show();
                }

                dialogSetCaloriesGoal.dismiss();
            }
        });

        // Crete & show the dialog
        builder.create();
        dialogSetCaloriesGoal = builder.show();
        dialogSetCaloriesGoal.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogSetCaloriesGoal = null;
            }
        });
    }

    private void endDay() {
        try {
            int currentCals = Integer.parseInt(editCurrentCalories.getText().toString());
            int goalCals = Integer.parseInt(textCaloriesGoal.getText().toString());

            if (currentCals <= goalCals) {
                editCurrentCalories.setTextColor(getColor(R.color.colorGreen));
                textCaloriesGoal.setTextColor(getColor(R.color.colorGreen));
                viewSplit.setBackgroundColor(getColor(R.color.colorGreen));
                succesCalories.start();
            } else {
                editCurrentCalories.setTextColor(getColor(android.R.color.holo_red_dark));
                textCaloriesGoal.setTextColor(getColor(android.R.color.holo_red_dark));
                viewSplit.setBackgroundColor(getColor(android.R.color.holo_red_dark));
                failCalories.start();
            }

            editCurrentCalories.clearFocus();
            buttonEndDay.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "Unexpected Error", Toast.LENGTH_SHORT).show();
        }
    }
}
