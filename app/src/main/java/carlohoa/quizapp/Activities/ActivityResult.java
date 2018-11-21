package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivityResult extends Activity {

    private Toolbar toolbar;
    private TextView quizResult;
    private ImageView quizFinishImage;
    private Button playAgainButton;

    private int quizCorrect;
    private int quizSize;
    private double quizResultPercentage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_on_finish);

        playAgainButton = (Button) findViewById(R.id.play_again_button);
        quizResult = (TextView) findViewById(R.id.quiz_result);
        quizFinishImage = (ImageView) findViewById(R.id.quiz_finish_image);
        setListener();
        showResult();
        setupToolbar();
    }

    /**
     * Initialize listener for buttons
     **/
    private void setListener(){
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgainDialog();
            }
        });
    }

    private void showResult(){
        quizCorrect = getIntent().getExtras().getInt("QuestionCorrect");
        quizSize = getIntent().getExtras().getInt("QuestionSize");
        quizResultPercentage = Math.round(((double)quizCorrect/quizSize)*100);

        quizFinishImage.setImageResource(R.drawable.app_result_medal);
        quizResult.setText("Your Score " + quizResultPercentage + "%");
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityResult.this, ActivityMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(ActivityResult.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playAgainDialog(){
        AlertDialog.Builder box = new AlertDialog.Builder(ActivityResult.this);
        box.setMessage(getResources().getString(R.string.play_again_text));

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(ActivityResult.this, ActivityNewGame.class);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        onBackPressed();
                        break;
                }
            }
        };
        box.setPositiveButton(getResources().getString(R.string.dialogYes), dialogClickListener);
        box.setNegativeButton(getResources().getString(R.string.dialogNo), dialogClickListener);
        box.setCancelable(false);
        AlertDialog dialog = box.create();
        dialog.show();
    }
}
