package carlohoa.quizapp.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class QuizContentProvider extends ContentProvider {
    // Database and Provider related stuff
    public final static String PROVIDER = "carlohoa.quizapp";
    public final static String DB_NAME = "quiz.db";
    private final static int DB_VERSION = 1;
    private static final int QUIZ = 1;

    // Table names
    private final static String TABLE_QUIZ = "Quiz";

    // Table User columns
    public final static String QUIZ_ID = "ID";
    public final static String QUIZ_CORRECT = "Correct";
    public final static String QUIZ_WRONG = "Wrong";

    // Table User create-statement
    private static final String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
            + QUIZ_ID + " INTEGER PRIMARY KEY,"
            + QUIZ_CORRECT + " INTEGER NOT NULL,"
            + QUIZ_WRONG + " INTEGER NOT NULL);";

    QuizContentProvider.DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public static final Uri CONTENT_QUIZ_URI = Uri.parse("content://" + PROVIDER + "/Quiz");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "Quiz", QUIZ);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUIZ_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_QUIZ);
            Log.d("DatabaseHelper", "updated");
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DBHelper = new QuizContentProvider.DatabaseHelper(getContext());
        db = DBHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case QUIZ:
                queryBuilder.setTables(TABLE_QUIZ);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(DBHelper.getReadableDatabase(), projection, selection, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        long minid = 0;
        Cursor c;

        switch (uriType) {
            case QUIZ:
                db.insert(TABLE_QUIZ, null, values);
                c = db.query(TABLE_QUIZ, null, null, null, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        c.moveToLast();
        minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (uriType) {
            case QUIZ:
                rowsDeleted = db.delete(TABLE_QUIZ, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (uriType) {
            case QUIZ:
                rowsUpdated = db.update(TABLE_QUIZ, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }
}
