package tr.STD17011042.OkanSeref.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tr.STD17011042.OkanSeref.Question;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.User.TABLE_NAME + " (" +
                    FeedReaderContract.User._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.User.NAME + " TEXT," +
                    FeedReaderContract.User.PHONE + " TEXT," +
                    FeedReaderContract.User.EMAIL + " TEXT," +
                    FeedReaderContract.User.BDATE + " TEXT," +
                    FeedReaderContract.User.PASSWORD + " TEXT," +
                    FeedReaderContract.User.PICUTRE + " BLOB," +
                    FeedReaderContract.User.SURNAME + " TEXT)";
    private static final String SQL_CREATE_QUESTIONS =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.Question.TABLE_NAME + " (" +
                    FeedReaderContract.Question._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.Question.TABLE_NAME+ " TEXT," +
                    FeedReaderContract.Question.QUESTION_TEXT + " TEXT," +
                    FeedReaderContract.Question.OWNER + " TEXT ," +
                    FeedReaderContract.Question.ANS1 + " TEXT," +
                    FeedReaderContract.Question.ANS2 + " TEXT," +
                    FeedReaderContract.Question.ANS3 + " TEXT," +
                    FeedReaderContract.Question.ANS4 + " TEXT," +
                    FeedReaderContract.Question.ANS5 + " TEXT," +
                    FeedReaderContract.Question.CORRECT_ANSWER + " INTEGER," +
                    FeedReaderContract.Question.MEDIATYPE + " INTEGER," +
                    FeedReaderContract.Question.PICUTRE + " BLOB," +
                    FeedReaderContract.Question.MEDIAPATH + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.User.NAME;
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Db created"+FeedReaderContract.User.TABLE_NAME);
        db.execSQL(SQL_CREATE_USERS);
    }
    public void createQuestions(SQLiteDatabase db){
        System.out.println("Db created"+FeedReaderContract.Question.TABLE_NAME);
        db.execSQL(SQL_CREATE_QUESTIONS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public  void DeleteQuestion(SQLiteDatabase db,String id){
        db.execSQL("DELETE FROM questions WHERE _ID='"+id+"'");
    }
    public void UpdateQuestion(SQLiteDatabase db, Question question){
        db.execSQL("UPDATE questions SET qtext = '"+question.getQuestionText()+"', ans1 = '"+question.getAnswer1()+"'," +
                " ans2 = '"+question.getAnswer2()+"'," +
                " ans3 = '"+question.getAnswer3()+"'," +
                " ans4 = '"+question.getAnswer4()+"'," +
                " ans5 = '"+question.getAnswer5()+"'," +
                " crts = '"+question.getCorrectAnswer()+"'" +
                " WHERE _ID='"+question.get_ID()+"'");

    }
}
