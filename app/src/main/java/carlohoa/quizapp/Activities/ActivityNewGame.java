package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import carlohoa.quizapp.DBHandler;
import carlohoa.quizapp.Model.Quiz;
import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivityNewGame extends Activity {

    private Toolbar toolbar;
    private DBHandler DB;

    private RelativeLayout activityNewGameLayout;
    private TextView quizCorrectCounter;
    private TextView quizWrongCounter;
    private TextView quizCounter;
    private TextView quizTimer;
    private TextView quizQuestion;
    private Button quizTrueButton;
    private Button quizFalseButton;

    private CountDownTimer quizCDT;
    private List<Quiz> quizList;
    private Quiz quiz;

    private int questions;
    private int quizCorrect;
    private int quizWrong;
    private int quizIndex;
    private int quizNumberCounter = 1;
    private int quizCountDownCounter = 15;
    private String answer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        getNumberOfQuestions();
        getJSON task = new getJSON();
        DB = new DBHandler();
        quizList = new ArrayList<>();

        task.execute(new String[]{
                "https://opentdb.com/api.php?amount="+questions+"&type=boolean"
        });

        activityNewGameLayout = (RelativeLayout) findViewById(R.id.activity_new_game_layout);
        quizCorrectCounter = (TextView) findViewById(R.id.quiz_correct_counter);
        quizWrongCounter = (TextView) findViewById(R.id.quiz_wrong_counter);
        quizCounter = (TextView) findViewById(R.id.quiz_counter);
        quizTimer = (TextView) findViewById(R.id.quiz_timer);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        quizTrueButton = (Button) findViewById(R.id.quiz_true_button);
        quizFalseButton = (Button) findViewById(R.id.quiz_false_button);

        setListener();
        setupToolbar();
    }

    /**
     * Initialize listener for buttons
     **/
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

    /**
     * Set the timer for each question (15 seconds)
     **/
    private void setCountDownTimer(){
        if(quizCDT != null){
            quizCDT.cancel();
        }
        quizCountDownCounter = 15;
        quizCDT = new CountDownTimer(15000, 1000){
            public void onTick(long millisUntilFinished){
                quizTimer.setText(String.valueOf(quizCountDownCounter));
                quizCountDownCounter--;
            }
            public void onFinish(){
                gameResult();
            }
        }.start();
    }

    private void checkAnswer(String answer){
        if(!quizList.isEmpty()){
            if(quizList.get(quizIndex).getCorrectAnswer().equals(answer)){
                quizCorrect++;
            }else {
                quizWrong++;
            }
            loadNextQuestion();
            updateActivityView();
        }
    }

    private void loadNextQuestion(){
        if(quizIndex > 0){
            quizIndex--;
            quizQuestion.setText(quizList.get(quizIndex).getQuestion());
            setCountDownTimer();
        }else{
            gameResult();
        }
    }

    private void updateActivityView(){
        quizCorrectCounter.setText("  Correct:   " + quizCorrect);
        quizWrongCounter.setText("  Incorrect: " + quizWrong);
        quizCounter.setText("Question " + quizNumberCounter + " / " + quizList.size());
        setBackgroundColor();
        quizNumberCounter++;
    }

    private void resetGame(){
        quizQuestion.setText("");
        quizTimer.setText("");
    }

    /**
     * Set the background-color relative to the Quiz's category that is being displayed
     **/
    private void setBackgroundColor( ){
        String category = quizList.get(quizIndex).getCategory();

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
            case "General Knowledge":
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            default:
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }

    private void gameResult(){
        quizCDT.cancel();
        saveStats();

        Intent intent = new Intent(this, ActivityResult.class);
        intent.putExtra("QuestionSize", quizList.size());
        intent.putExtra("QuestionCorrect", quizCorrect);
        startActivity(intent);
        finish();
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_new_game);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizCDT.cancel();
                finish();
            }
        });
    }

    private void getNumberOfQuestions(){
        questions = getSharedPreferences("Questions", MODE_PRIVATE)
                .getInt("Questions", 10);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveStats(){
        DB.addQuizStat(this, quizCorrect, quizWrong);
    }

    /**
    * JSON-class
    **/
    private class getJSON extends AsyncTask<String, Void, List<Quiz>> {

        /**
         * Getting JSON-object
         **/
        @Override
        protected List doInBackground(String... urls) {
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

                            /**
                             * For every quiz from URL, create a quiz-object with the respective quiz-data and push to the Arraylist
                             **/
                            quiz = new Quiz();
                            quiz.setID(i);
                            quiz.setCategory(dataObject.getString("category"));;
                            quiz.setType(dataObject.getString("type"));
                            quiz.setDifficulty(dataObject.getString("difficulty"));
                            quiz.setQuestion(dataObject.getString("question"));
                            quiz.setCorrectAnswer(dataObject.getString("correct_answer"));
                            quizList.add(quiz);
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

        /**
         * Initialize first question
         **/
        @Override
        protected void onPostExecute(List<Quiz> quiz) {
            if(!quizList.isEmpty()){
                quizIndex = quizList.size()-1;
                quizQuestion.setText(quiz.get(quizIndex).getQuestion());
                quizCorrectCounter.setText("  Correct:   " + quizCorrect);
                quizWrongCounter.setText("  Incorrect: " + quizWrong);
                quizCounter.setText("Question " + quizNumberCounter + " / " + quizList.size());
                setBackgroundColor();
                setCountDownTimer();
            }
        }
    }
}
