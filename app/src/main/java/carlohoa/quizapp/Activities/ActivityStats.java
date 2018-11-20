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

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> statsArrayList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setupToolbar();

        listView = (ListView) findViewById(R.id.stats_listview);

        loadPreferencesStats();
    }

    private void loadPreferencesStats(){
        statsArrayList = new ArrayList<>();
        try{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("STAT_ARRAY_LIST", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();

            statsArrayList = gson.fromJson(json, type);

            arrayAdapter = new ArrayAdapter<>(this, R.layout.stats_listview_item, R.id.stats_textview, statsArrayList);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

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
