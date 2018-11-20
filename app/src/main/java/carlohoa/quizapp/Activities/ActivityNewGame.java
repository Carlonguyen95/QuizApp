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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;

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

    private Toolbar toolbar;
    private RelativeLayout activityNewGameLayout;
    private TextView quizScore;
    private TextView quizTimer;
    private TextView quizQuestion;
    private Button quizTrueButton;
    private Button quizFalseButton;
    private ArrayList<String> sharedArrayList;

    private CountDownTimer quizCDT;
    private List<Quiz> quizList;
    private Quiz quiz;
    private int quizCorrect;
    private int quizWrong;
    private int quizIndex;
    private int quizCounter = 1;
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

        getJSON task = new getJSON();
        quizList = new ArrayList<>();
        sharedArrayList = new ArrayList<>();
        task.execute(new String[]{
                "https://opentdb.com/api.php?amount=10&type=boolean"
        });

        activityNewGameLayout = (RelativeLayout) findViewById(R.id.activity_new_game_layout);
        quizScore = (TextView) findViewById(R.id.quiz_score);
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
        quizScore.setText("Score " + "V: " + quizCorrect + " / " + "X: " + quizWrong);
        setBackgroundColor();
        quizCounter++;
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
                activityNewGameLayout.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    /**
     * A dialog which prompts the user for a new game at the end
     **/
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

    private void gameResult(){
        quizCDT.cancel();
        savePreferencesStats();

        Intent intent = new Intent(this, ActivityResult.class);
        intent.putExtra("QuestionSize", quizList.size());
        intent.putExtra("QuestionCorrect", quizCorrect);
        startActivity(intent);
        finish();
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizCDT.cancel();
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void savePreferencesStats(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        sharedArrayList.add("Result from game: " + quizCorrect + " / " + quizWrong);
        Gson gson = new Gson();
        String json = gson.toJson(sharedArrayList);
        editor.putString("STAT_ARRAY_LIST", json);
        editor.apply();
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
                quizScore.setText("Score " + "V: " + quizCorrect + " / " + "X: " + quizWrong);
                setBackgroundColor();
                setCountDownTimer();
                Toast.makeText(ActivityNewGame.this, getResources().getString(R.string.game_started), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
