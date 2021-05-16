package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;

import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;


public class QuestionUpdate extends AppCompatActivity {
    EditText question,answer1,answer2,answer3,answer4,answer5;
    RadioGroup radioGroup;
    Button updateButton;
    Question qst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_update);
        qst = (Question) getIntent().getExtras().getSerializable("questionToUpdate");
        PairUI();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);//Hangi cevap olduğunun indeksi
                System.out.println("CEVAP INDESKSI:"+idx);
                if(question.getText()==null){
                    Toast.makeText(QuestionUpdate.this, "Question cannot be empty",Toast.LENGTH_SHORT).show();
                }else if(answer1.getText()==null||answer2.getText()==null||answer3.getText()==null||answer4.getText()==null||answer5.getText()==null){
                    Toast.makeText(QuestionUpdate.this, "Please fill all the answers",Toast.LENGTH_SHORT).show();
                }else if(radioGroup.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(QuestionUpdate.this, "Please select correct answer",Toast.LENGTH_SHORT).show();
                }else{
                    Question q = new Question(qst.getOwner(),question.getText().toString(),answer1.getText().toString(),answer2.getText().toString(),answer3.getText().toString(),answer4.getText().toString(),answer5.getText().toString(),idx,qst.getImage());
                    q.set_ID(qst.get_ID());
                    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    dbHelper.UpdateQuestion(db,q);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","1");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });
    }
    private void PairUI(){
        answer1=findViewById(R.id.Answer1Update);
        answer2=findViewById(R.id.Answer2Update);
        answer3=findViewById(R.id.Answer3Update);
        answer4=findViewById(R.id.Answer4Update);
        answer5=findViewById(R.id.Answer5Update);
        question=findViewById(R.id.questionTextUpdate);
        updateButton=findViewById(R.id.submitUpdate);
        radioGroup=findViewById(R.id.answerGroupUpdate);

        answer1.setText(qst.getAnswer1());
        answer2.setText(qst.getAnswer2());
        answer3.setText(qst.getAnswer3());
        answer4.setText(qst.getAnswer4());
        answer5.setText(qst.getAnswer5());
        question.setText(qst.getQuestionText());
        ((RadioButton)radioGroup.getChildAt(qst.getCorrectAnswer())).setChecked(true);
    }
    private byte[] DrawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}