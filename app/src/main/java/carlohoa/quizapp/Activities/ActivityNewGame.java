package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    TextView quizCategory;
    TextView quizQuestion;
    Button quizNextQuestion;
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

        getJSON task = new getJSON();
        quizList = new ArrayList<>();
        task.execute(new String[]{
                "https://opentdb.com/api.php?amount=10&type=boolean"
        });

        quizCategory = (TextView) findViewById(R.id.quiz_category);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        quizNextQuestion = (Button) findViewById(R.id.quiz_next_question);
        quizTrueButton = (Button) findViewById(R.id.quiz_true_button);
        quizFalseButton = (Button) findViewById(R.id.quiz_false_button);

        setListener();
    }

    private void setListener(){
        quizNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizCategory.setText(quizList.get(quizCounter).getCategory());
                quizQuestion.setText(quizList.get(quizCounter).getQuestion());
                quizCounter--;
            }
        });

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
        quizCategory.setText(quizList.get(quizCounter).getCategory());
        quizQuestion.setText(quizList.get(quizCounter).getQuestion());
        quizCounter--;
    }

    private void updateActivityView(){

    }

    private class getJSON extends AsyncTask<String, Void, List<Quiz>> {
        JSONObject jsonObject;

        @Override
        protected List doInBackground(String... urls) {
            String retur = "";
            String s = "";
            String result = "";

            for(String url : urls){
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if(conn.getResponseCode() != 200){
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Output from Server .... \n");
                    while((s = br.readLine()) != null) {
                        result = result + s;
                    }
                    System.out.println("Output: " + result);
                    conn.disconnect();

                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray dataArray = jsonObject.getJSONArray("results");
//                        JSONArray mat = new JSONArray(result);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
//                            String name = dataObject.getString("difficulty");
                            quiz = new Quiz();

                            quiz.setID(i);
                            quiz.setCategory(dataObject.getString("category"));;
                            quiz.setType(dataObject.getString("type"));
                            quiz.setDifficulty(dataObject.getString("difficulty"));
                            quiz.setQuestion(dataObject.getString("question"));
                            quiz.setCorrectAnswer(dataObject.getString("correct_answer"));

                            quizList.add(quiz);
//                            retur = retur + name+ "\n";
//                            System.out.println("Data fra quiz-objekt: " + quiz.getCategory());
                        }
                        return quizList;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return quizList;
                }catch (Exception e){}
            }
            return quizList;
        }

        @Override
        protected void onPostExecute(List<Quiz> quiz) {
            quizCounter = quizList.size()-1;

            quizCategory.setText(quiz.get(quizCounter).getCategory());
            quizQuestion.setText(quiz.get(quizCounter).getQuestion());
        }
    }
}
