package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import carlohoa.quizapp.Model.Quiz;
import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivityNewGame extends Activity {

    RelativeLayout activityNewGameLayout;
    ImageView quizFinishImage;
    TextView quizScore;
    TextView quizCategory;
    TextView quizQuestion;
    Button quizTrueButton;
    Button quizFalseButton;

    private List<Quiz> quizList;
    private Quiz quiz;
    private Integer quizCounter;
    private String answer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        activityNewGameLayout = (RelativeLayout) findViewById(R.id.activity_new_game_layout);
        getJSON task = new getJSON();
        quizList = new ArrayList<>();
        task.execute(new String[]{
                "https://opentdb.com/api.php?amount=10&type=boolean"
        });

        quizFinishImage = (ImageView) findViewById(R.id.quiz_finish_image);
        quizScore = (TextView) findViewById(R.id.quiz_score);
        quizCategory = (TextView) findViewById(R.id.quiz_category);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        quizTrueButton = (Button) findViewById(R.id.quiz_true_button);
        quizFalseButton = (Button) findViewById(R.id.quiz_false_button);

        setListener();
    }

    private void setListener(){
        quizTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = "True";
                checkAnswer(answer);
            }
        });

        quizFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = "False";
                checkAnswer(answer);
            }
        });
    }

    private void checkAnswer(String answer){
        if(quizList.get(quizCounter).getCorrectAnswer().equals(answer)){
            loadNextQuestion();
            updateActivityView();
        }
    }

    private void loadNextQuestion(){
        if(quizCounter > 0){
            quizCounter--;
            quizCategory.setText(quizList.get(quizCounter).getCategory());
            quizQuestion.setText(quizList.get(quizCounter).getQuestion());
        }else{
            endGameDialog();
            resetGame();
        }
    }

    private void updateActivityView(){
        quizScore.setText(quizCounter+1 + " / " + quizList.size());
        setBackgroundColor();
    }

    private void resetGame(){
        quizQuestion.setText("");
        quizFinishImage.setImageResource(R.drawable.app_quiz_finish_image);
    }

    private void setBackgroundColor( ){
        String category = quizList.get(quizCounter).getCategory();

        switch(category){
            case "Mythology":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorMythology));
                break;
            case "Sports":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorMythology));
                break;
            case "Geography":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorGeography));
                break;
            case "History":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorHistory));
                break;
            case "Politics":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorPolitics));
                break;
            case "Art":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorArt));
                break;
            case "Celebrities":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorCelebrities));
                break;
            case "Animals":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorAnimals));
                break;
            default:
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void endGameDialog(){
        AlertDialog.Builder box = new AlertDialog.Builder(ActivityNewGame.this);
        box.setMessage(getResources().getString(R.string.finishGameText));

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //newGame();
                        //recreate();
                        Toast.makeText(ActivityNewGame.this, getResources().getString(R.string.newGameText), Toast.LENGTH_SHORT).show();
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

    private class getJSON extends AsyncTask<String, Void, List<Quiz>> {

        @Override
        protected List doInBackground(String... urls) {
            String retur = "";
            String s = "";
            String result = "";

            for(String url : urls){
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if(conn.getResponseCode() != 200){
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while((s = br.readLine()) != null) {
                        result = result + s;
                    }
                    System.out.println("Result from URL: " + result);
                    conn.disconnect();

                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray dataArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);

                            quiz = new Quiz();
                            quiz.setID(i);
                            quiz.setCategory(dataObject.getString("category"));;
                            quiz.setType(dataObject.getString("type"));
                            quiz.setDifficulty(dataObject.getString("difficulty"));
                            quiz.setQuestion(dataObject.getString("question"));
                            quiz.setCorrectAnswer(dataObject.getString("correct_answer"));
                            quizList.add(quiz);
//                            retur = retur + name+ "\n";
                        }
                        return quizList;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return quizList;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return quizList;
        }

        @Override
        protected void onPostExecute(List<Quiz> quiz) {
            quizCounter = quizList.size()-1;
            quizCategory.setText(quiz.get(quizCounter).getCategory());
            quizQuestion.setText(quiz.get(quizCounter).getQuestion());
            updateActivityView();
        }
    }
}
