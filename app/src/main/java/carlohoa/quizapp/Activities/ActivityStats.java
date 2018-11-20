package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

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

        loadStats();
    }

    private void loadStats(){
        statsArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.stats_listview_item, R.id.stats_textview, statsArrayList);
        listView.setAdapter(arrayAdapter);

        for(int i = 0; i < 10; i++){
            statsArrayList.add("Item: " + i);
            arrayAdapter.notifyDataSetChanged();
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
