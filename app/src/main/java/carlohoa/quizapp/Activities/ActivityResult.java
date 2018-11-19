package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
                Intent intent = new Intent(ActivityResult.this, ActivityNewGame.class);
                startActivity(intent);
            }
        });
    }

    private void showResult(){
        quizCorrect = getIntent().getExtras().getInt("QuestionCorrect");
        quizSize = getIntent().getExtras().getInt("QuestionSize");
        quizResultPercentage = ((double)quizCorrect/quizSize)*100;

        quizFinishImage.setImageResource(R.drawable.app_result_medal);
        quizResult.setText(quizResultPercentage + "%");
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
}
