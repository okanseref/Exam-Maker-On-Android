package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;


import java.util.ArrayList;
import java.util.List;

import tr.STD17011042.OkanSeref.DB.FeedReaderContract;
import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;
import tr.STD17011042.OkanSeref.Adapter.QuestionAdapter;

public class QuestionList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private User currentUser;
    private final int  QUESTION_UPDATE =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        currentUser = (User) getIntent().getExtras().getSerializable("chosenUser");
        PairUI();
    }
    private void PairUI(){
        recyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new QuestionAdapter(GetQuestions());
        recyclerView.setAdapter(adapter);
    }
    private List<Question> GetQuestions(){
        List<Question> questions = new ArrayList<>();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.createQuestions(db);
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.Question.QUESTION_TEXT,
                FeedReaderContract.Question.OWNER,
                FeedReaderContract.Question.ANS1,
                FeedReaderContract.Question.ANS2,
                FeedReaderContract.Question.ANS3,
                FeedReaderContract.Question.ANS4,
                FeedReaderContract.Question.ANS5,
                FeedReaderContract.Question.PICUTRE,
                FeedReaderContract.Question.CORRECT_ANSWER,
                FeedReaderContract.Question.MEDIATYPE,
                FeedReaderContract.Question.MEDIAPATH,
                FeedReaderContract.Question._ID
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.Question.OWNER + " = ?";
        String[] selectionArgs = { currentUser.getUsername() };

        String sortOrder =
                FeedReaderContract.Question.OWNER + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.Question.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {
           Question q = new Question(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.OWNER)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.QUESTION_TEXT)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS1)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS2)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS3)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS4)),
                   cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS5)),
                   cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.CORRECT_ANSWER)));
           q.setMediaType(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.MEDIATYPE)));
           q.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.PICUTRE)));
           q.setMediaPath(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.MEDIAPATH)));
           q.set_ID(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.Question._ID)));
           questions.add(q);
            System.out.println(q.get_ID()+" Q ID BU");

        }
        cursor.close();
        db.close();
        return questions;
    }
    public void RefreshQuestionList(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void UpdateQuestion(Question q){
        Intent intent = new Intent(QuestionList.this, QuestionUpdate.class);
        intent.putExtra("questionToUpdate", q);
        startActivityForResult(intent, QUESTION_UPDATE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUESTION_UPDATE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if(result.equals("1")){
                    RefreshQuestionList(); //Değişiklik olunca sayfayı güncelliyor
                }
            }
        }
    }
}