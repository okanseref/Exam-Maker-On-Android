package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import tr.STD17011042.OkanSeref.DB.FeedReaderContract;
import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;

public class QuestionAdd extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText question,answer1,answer2,answer3,answer4,answer5;
    Spinner mediaSelector;
    TableRow imageRow,videoRow,soundRow;
    VideoView videoView;
    Button addQuestion,soundButton;
    User currentUser;

    ImageButton questionImage;
    public static final int PICK_PHOTO_FOR_QUESTION = 2;
    public static final int PICK_VIDEO_FOR_QUESTION = 3;
    public static final int PICK_SOUND_FOR_QUESTION = 4;
    String mediaPath= "";
    int mediaType=0;
    MediaPlayer mp;
    boolean soundBool=false;
    boolean videoBool=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);
        PairUI();
        currentUser = (User) getIntent().getExtras().getSerializable("chosenUser");

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);//Hangi cevap olduğunun indeksi
                System.out.println("CEVAP INDESKSI:"+idx);
                if(question.getText()==null){
                    Toast.makeText(QuestionAdd.this, "Question cannot be empty",Toast.LENGTH_SHORT).show();
                }else if(answer1.getText()==null||answer2.getText()==null||answer3.getText()==null||answer4.getText()==null||answer5.getText()==null){
                    Toast.makeText(QuestionAdd.this, "Please fill all the answers",Toast.LENGTH_SHORT).show();
                }else if(radioGroup.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(QuestionAdd.this, "Please select correct answer",Toast.LENGTH_SHORT).show();
                }else{
                    Question q = new Question(currentUser.getUsername(),question.getText().toString(),answer1.getText().toString(),answer2.getText().toString(),answer3.getText().toString(),answer4.getText().toString(),answer5.getText().toString(),idx,DrawableToByte(questionImage.getDrawable()));
                    InsertQuestion(q);
                    finish();
                }
            }
        });
        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundBool){
                    soundButton.setText("PAUSE");
                    mp.start();
                    soundBool=false;
                }else{
                    soundButton.setText("PLAY");
                    mp.pause();
                    soundBool=true;
                }
            }
        });
        mediaSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case 0:
                        mediaType= 0;
                        SwitchLayout(mediaType);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK);
                        intent .setType("image/*");
                        startActivityForResult(intent , PICK_PHOTO_FOR_QUESTION);
                        break;
                    case 2:
                        intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent , PICK_VIDEO_FOR_QUESTION);
                        break;
                    case 3:
                        Intent audio_picker_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(audio_picker_intent, PICK_SOUND_FOR_QUESTION);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoBool){
                    videoView.pause();
                    videoBool=false;
                }else{
                    videoView.start();
                    videoBool=true;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (reqCode == PICK_PHOTO_FOR_QUESTION) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    questionImage.setImageBitmap(selectedImage);

                    mediaType= 1;
                    SwitchLayout(mediaType);


                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                }
            }
            if (reqCode == PICK_VIDEO_FOR_QUESTION) {

                final Uri videoUri = data.getData();
                mediaPath=getVideoPath(videoUri);
                mediaType= 2;
                videoView.setVideoPath(mediaPath);
                videoView.start();
                videoBool=true;
                SwitchLayout(mediaType);
            }
            if (reqCode == PICK_SOUND_FOR_QUESTION) {
                final Uri soundUri = data.getData();
                mediaPath=getSoundPath(soundUri);
                mediaType= 3;
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(mediaPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });


                SwitchLayout(mediaType);
            }
        }
    }
    public Bitmap makeSmallerImage(Bitmap image,int maxSize){
        int width=image.getWidth();
        int height=image.getHeight();
        float bitmapRatio=(float) width/(float)height;
        if(bitmapRatio>1){
            width=maxSize;
            height=(int)(width/bitmapRatio);
        }else{
            height=maxSize;
            width=(int)(height*bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }
    public String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    public String getSoundPath(Uri uri) {
        String[] projection = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    private byte[] DrawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        bitmap=makeSmallerImage(bitmap,480);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
        return stream.toByteArray();
    }
    private void PairUI(){
        answer1=findViewById(R.id.Answer1);
        answer2=findViewById(R.id.Answer2);
        answer3=findViewById(R.id.Answer3);
        answer4=findViewById(R.id.Answer4);
        answer5=findViewById(R.id.Answer5);
        question=findViewById(R.id.QuestionText);
        addQuestion=findViewById(R.id.saveQuestion);
        questionImage=findViewById(R.id.addQuestionImage);
        radioGroup=findViewById(R.id.answerGroup);
        mediaSelector=findViewById(R.id.mediaSpinner);
        imageRow=findViewById(R.id.IMAGE);
        videoRow=findViewById(R.id.VIDEO);
        soundRow=findViewById(R.id.SOUND);
        videoView=findViewById(R.id.videoView);
        soundButton=findViewById(R.id.soundButton);

        String[] arraySpinner = new String[] {
                "NONE","IMAGE","VIDEO","SOUND"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mediaSelector.setAdapter(adapter);
    }
    private void InsertQuestion(Question q){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.createQuestions(db);
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.Question.QUESTION_TEXT ,q.getQuestionText());
        values.put(FeedReaderContract.Question.OWNER ,q.getOwner());
        values.put(FeedReaderContract.Question.ANS1 ,q.getAnswer1());
        values.put(FeedReaderContract.Question.ANS2 , q.getAnswer2());
        values.put(FeedReaderContract.Question.ANS3 , q.getAnswer3());
        values.put(FeedReaderContract.Question.ANS4 , q.getAnswer4());
        values.put(FeedReaderContract.Question.ANS5 , q.getAnswer5());
        values.put(FeedReaderContract.Question.CORRECT_ANSWER , q.getCorrectAnswer());
        values.put(FeedReaderContract.Question.PICUTRE , q.getImage());
        values.put(FeedReaderContract.Question.MEDIATYPE , mediaType);
        values.put(FeedReaderContract.Question.MEDIAPATH , mediaPath);

        db.insert(FeedReaderContract.Question.TABLE_NAME, null, values);
        db.close();
    }
    private void SwitchLayout(int i){
        switch(i){
            case 0:
                imageRow.setVisibility(View.GONE);
                videoRow.setVisibility(View.GONE);
                soundRow.setVisibility(View.GONE);
                break;
            case 1:
                imageRow.setVisibility(View.VISIBLE);
                videoRow.setVisibility(View.GONE);
                soundRow.setVisibility(View.GONE);
                break;
            case 2:
                imageRow.setVisibility(View.GONE);
                videoRow.setVisibility(View.VISIBLE);
                soundRow.setVisibility(View.GONE);
                break;
            case 3:
                imageRow.setVisibility(View.GONE);
                videoRow.setVisibility(View.GONE);
                soundRow.setVisibility(View.VISIBLE);
                break;
        }
    }
}