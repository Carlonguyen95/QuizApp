package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivityStats extends Activity {

    private Toolbar toolbar;
    private Button statsClearButton;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> statsArrayList;
    private ArrayList<String> tempList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setupToolbar();
        statsClearButton = (Button) findViewById(R.id.stats_clear_button);
        listView = (ListView) findViewById(R.id.stats_listview);

        setListener();
        loadPreferencesStats();
    }

    private void setListener(){
        statsClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                statsArrayList.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadPreferencesStats(){
        statsArrayList = new ArrayList<>();
        tempList = new ArrayList<>();
        try{
            SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = sharedPreferences.getString("STAT_LIST", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();

            tempList = gson.fromJson(json, type);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.stats_listview_item, R.id.stats_textview, statsArrayList);
            listView.setAdapter(arrayAdapter);
            for(int i = 0; i < tempList.size(); i++){
                statsArrayList.add(tempList.get(i));
                arrayAdapter.notifyDataSetChanged();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(ActivityStats.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
