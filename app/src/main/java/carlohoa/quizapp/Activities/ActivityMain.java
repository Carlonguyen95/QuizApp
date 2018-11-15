package carlohoa.quizapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import carlohoa.quizapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button gameStartButton = (Button)findViewById(R.id.game_start_button);
        gameStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                openNewGame();
            }
        });

//        final ImageButton gameSettingsBtn = (ImageButton)findViewById(R.id.gameSettingsBtn);
//        gameSettingsBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                openSettingActivity();
//            }
//        });
//
//        final ImageButton gameStatsBtn = (ImageButton)findViewById(R.id.gameStatsBtn);
//        gameStatsBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                openStatsActivity();
//            }
//        });
    }

    public void openNewGame(){
        Intent intent = new Intent(this, ActivityNewGame.class);
        startActivity(intent);
    }

//    public void openSettingActivity(){
//        Intent intent = new Intent(this, Settings.class);
//        startActivity(intent);
//    }
//
//    public void openStatsActivity(){
//        Intent intent = new Intent(this, Stats.class);
//        startActivity(intent);
//    }
}
