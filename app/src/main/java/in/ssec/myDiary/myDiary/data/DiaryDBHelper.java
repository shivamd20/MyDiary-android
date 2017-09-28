package in.ssec.myDiary.myDiary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by shiva on 21-12-2016.
 */

public class DiaryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 4;




    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_USER_TABLE =  "CREATE TABLE " + DiaryContract.User.TABLE_NAME + " ("
                + DiaryContract.User.USER_NAME + " TEXT PRIMERY KEY, "
                + DiaryContract.User.USER_PASSWORD + " TEXT NOT NULL);";

        String SQL_CREATE_NOTE_TABLE =  "CREATE TABLE " + DiaryContract.Notes.TABLE_NAME + " ("
                + DiaryContract.Notes.HEAD + " TEXT  , "
                + DiaryContract.Notes.DATE+ " DATETIME  DEFAULT CURRENT_TIMESTAMP, " +
                 DiaryContract.Notes.NOTE+ " TEXT NOT NULL,"+DiaryContract.Notes._id +" INTEGER  PRIMARY KEY AUTOINCREMENT,"+
                DiaryContract.Notes.IMAGE +" BLOB "+");";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_USER_TABLE);

        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
