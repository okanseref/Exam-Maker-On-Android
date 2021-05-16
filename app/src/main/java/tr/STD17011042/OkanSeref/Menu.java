package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class Menu extends AppCompatActivity {
    TextView username;
    ImageView userImage;
    Button addQuestion,listQuestion,newExam,examSettings;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        currentUser = (User) getIntent().getExtras().getSerializable("chosenUser");
        PairUI();

        userImage.setImageBitmap(BitmapFactory.decodeByteArray(currentUser.getImage(), 0,currentUser.getImage().length ));
        username.setText("Welcome "+currentUser.getUsername());
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, QuestionAdd.class);
                intent.putExtra("chosenUser", currentUser);
                startActivity(intent);
            }
        });
        listQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, QuestionList.class);
                intent.putExtra("chosenUser", currentUser);
                startActivity(intent);
            }
        });
        examSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, ExamSettings.class));
            }
        });
        newExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, NewExam.class);
                intent.putExtra("chosenUser", currentUser);
                startActivity(intent);
            }
        });
    }
    private void PairUI(){
        userImage=findViewById(R.id.menuImage);
        username=findViewById(R.id.menuUsername);
        addQuestion=findViewById(R.id.menuNewQuestion);
        listQuestion=findViewById(R.id.menuListQuestion);
        examSettings=findViewById(R.id.examSettings);
        newExam=findViewById(R.id.newExam);
    }
}