package carlohoa.quizapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DBHandler extends Activity{

    public static String PROVIDER = "carlohoa.quizapp" ;
    public static final Uri CONTENT_QUIZ_URI = Uri.parse("content://"+ PROVIDER + "/Quiz");

    public void addQuizStat(Context context, int Correct, int Wrong){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.QUIZ_CORRECT), Correct);
            values.put(context.getString(R.string.QUIZ_WRONG), Wrong);
            context.getContentResolver().insert(CONTENT_QUIZ_URI, values);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteQuizStat(Context context, String id){
        try {
            String selection = context.getString(R.string.QUIZ_ID) + "=" + "'" + id + "'";
            context.getContentResolver().delete(CONTENT_QUIZ_URI, selection, null);
        }catch (Exception e){}
    }
}
