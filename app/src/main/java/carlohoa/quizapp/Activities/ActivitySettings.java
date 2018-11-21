package carlohoa.quizapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivitySettings extends Activity {

    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private final String KEY_SAVED_RADIO_INDEX = "SAVED_RADIO_INDEX";
    private int questions;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();

        radioGroup = (RadioGroup)findViewById(R.id.settings_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

                switch(checkedId){
                    case R.id.questions_10:
                        questions = 10;
                        savePreferences(KEY_SAVED_RADIO_INDEX, checkedIndex);
                        break;
                    case R.id.questions_20:
                        questions = 20;
                        savePreferences(KEY_SAVED_RADIO_INDEX, checkedIndex);
                        break;
                    case R.id.questions_30:
                        questions = 30;
                        savePreferences(KEY_SAVED_RADIO_INDEX, checkedIndex);
                        break;
                }
            }
        });

        loadPreferences();
    }

    private void savePreferences(String key, int value){
        SharedPreferences.Editor editor = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();

        // Save the number of questions to shared-pref
        getSharedPreferences("Questions", MODE_PRIVATE)
                .edit()
                .putInt("Questions", questions)
                .apply();
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_settings);
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
            Intent intent = new Intent(ActivitySettings.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
