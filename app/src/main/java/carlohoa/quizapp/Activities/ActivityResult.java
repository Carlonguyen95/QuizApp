package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import carlohoa.quizapp.R;

public class ActivityResult extends Activity {

    TextView quizResult;
    ImageView quizFinishImage;

    private int quizCorrect;
    private int quizSize;
    private double quizResultPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_on_finish);

        quizResult = (TextView) findViewById(R.id.quiz_result);
        quizFinishImage = (ImageView) findViewById(R.id.quiz_finish_image);
        showResult();
    }

    private void showResult(){
        quizCorrect = getIntent().getExtras().getInt("QuestionCorrect");
        quizSize = getIntent().getExtras().getInt("QuestionSize");
        quizResultPercentage = ((double)quizCorrect/quizSize)*100;

        if(quizResultPercentage > 50){
            quizFinishImage.setImageResource(R.drawable.app_result_medal);
        }else{
            quizFinishImage.setImageResource(R.drawable.app_quiz_finish_image);
        }
        quizResult.setText("Result: " + quizResultPercentage + "%");
    }
}
