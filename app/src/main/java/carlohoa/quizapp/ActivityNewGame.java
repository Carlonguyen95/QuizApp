package carlohoa.quizapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityNewGame extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        textView = (TextView) findViewById(R.id.jsontekst);
        getJSON task = new getJSON();

//        task.execute(new String[]{
//                "http://student.cs.hioa.no/~s315613/jsonout.php"
//        });

        task.execute(new String[]{
                "https://opentdb.com/api.php?amount=10&type=boolean"
        });
    }

    private class getJSON extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... urls) {
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
                            String name = dataObject.getString("category");
                            retur = retur + name+ "\n";
                            System.out.println(retur);
                        }
                        return retur;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return retur;
                }catch (Exception e){
                    return "Noe gikk feil";
                }
            }
            return retur;
        }

        @Override
        protected void onPostExecute(String ss) {
            textView.setText(ss);
        }
    }
}
