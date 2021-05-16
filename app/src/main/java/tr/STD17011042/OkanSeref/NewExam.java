package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import tr.STD17011042.OkanSeref.Adapter.QuestionAdapterInExam;
import tr.STD17011042.OkanSeref.DB.FeedReaderContract;
import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;

public class NewExam extends AppCompatActivity {
    private RecyclerView questions;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private User currentUser;
    private EditText point,duration;
    private Spinner difficulty;
    private Button save,share;
    SharedPreferences sharedPreferences;
    public static ArrayList<Integer> selectedQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);
        sharedPreferences=getApplicationContext().getSharedPreferences("prefs",MODE_PRIVATE);
        selectedQuestions=new ArrayList<>();
        PairUI();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share(CreateOutputText());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder str= new StringBuilder();
                str.append(duration.getText().toString()).append(point.getText().toString());
                for (int i:selectedQuestions) {
                    Question q = getQuestion(i);
                    str.append(q.get_ID());
                }
                writeToFile(CreateOutputText(),str.toString());
            }
        });
    }
    private String CreateOutputText(){
        int val,counter=1;
        StringBuilder str= new StringBuilder();
        str.append("Time: ").append(duration.getText().toString()).append(" Point per question: ").append(point.getText().toString()).append("\n");
        for (int i:selectedQuestions) {
            Question q = getQuestion(i);
            List<String> answers = q.getWrongAnswers();
            List<String> chosenAnswers = new ArrayList<>();
            Stack<String> output = new Stack<>();
            output.push("E) ");
            output.push("D) ");
            output.push("C) ");
            output.push("B) ");
            output.push("A) ");
            Random r = new Random();
            str.append("Question").append(counter).append(") ");
            counter++;
            str.append(q.getQuestionText()).append("\n");
            for (int j=0;j<Integer.parseInt(difficulty.getSelectedItem().toString())-1;j++){
                val=r.nextInt(answers.size());
                //output.add(answers.get(val));
                chosenAnswers.add(answers.get(val));
                answers.remove(val);
            }
            chosenAnswers.add(q.getCorrectAnswerText());
            int size=chosenAnswers.size();
            for (int j=0;j<size;j++){
                val=r.nextInt(chosenAnswers.size());
                str.append(output.pop()).append(chosenAnswers.get(val)).append("\n");
                chosenAnswers.remove(val);
            }
        }
        return str.toString();
    }
    private void writeToFile(String data,String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(fileName+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            Toast.makeText(NewExam.this, "File saved as"+fileName,Toast.LENGTH_SHORT).show();
            System.out.println(getApplicationContext().getFilesDir());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private void Share(String str){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void PairUI(){
        currentUser = (User) getIntent().getExtras().getSerializable("chosenUser");

        questions = findViewById(R.id.newExamList);
        share = findViewById(R.id.share);
        layoutManager = new LinearLayoutManager(this);
        questions.setLayoutManager(layoutManager);
        adapter= new QuestionAdapterInExam(GetQuestions());
        questions.setAdapter(adapter);

        point=findViewById(R.id.defaultPoint);
        duration=findViewById(R.id.defaultDuration);
        difficulty=findViewById(R.id.defaultDifficultySpinner);
        save=findViewById(R.id.saveDefaults);

        point.setText(String.valueOf(sharedPreferences.getInt("point",5)));
        duration.setText(String.valueOf(sharedPreferences.getInt("duration",60)));
        String[] arraySpinner = new String[] {
                "2","3","4","5"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter);
        difficulty.setSelection(sharedPreferences.getInt("difficulty",5)-2);
    }
    private Question getQuestion(int selectedId){ //Textler yeterli outputa veriyoruz
        Question q = new Question();

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
                FeedReaderContract.Question._ID,
                FeedReaderContract.Question.CORRECT_ANSWER,
        };

        String selection = FeedReaderContract.Question._ID + " = ?";
        String[] selectionArgs = { String.valueOf(selectedId) };

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
            q = new Question(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.OWNER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.QUESTION_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS1)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS3)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS4)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.ANS5)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.Question.CORRECT_ANSWER)));
            q.set_ID(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.Question._ID)));

        }
        cursor.close();
        return q;
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
                FeedReaderContract.Question._ID,
                FeedReaderContract.Question.MEDIAPATH,
        };

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
}