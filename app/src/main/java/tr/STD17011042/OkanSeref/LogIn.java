package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import tr.STD17011042.OkanSeref.DB.FeedReaderContract;
import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;

public class LogIn extends AppCompatActivity {
    Button loginButton, signUpButton;
    EditText usernameEditText, passwordEditText;
    int attempts = 0;
    //public static ArrayList<User> database;
    public User chosenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PairUI();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString(), password = passwordEditText.getText().toString();
                if(!LogInAttempt(username, password)){
                    attempts++;
                    if(attempts>2){
                        Toast.makeText(LogIn.this, "Too many login attempts!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LogIn.this, SignUp.class));
                        attempts=0;
                    }else{
                        Toast.makeText(LogIn.this, "Wrong username or password",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        ActivityCompat.requestPermissions(LogIn.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }
    private boolean LogInAttempt(String username, String password) {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.User.NAME,
                FeedReaderContract.User.SURNAME,
                FeedReaderContract.User.BDATE,
                FeedReaderContract.User.EMAIL,
                FeedReaderContract.User.PASSWORD,
                FeedReaderContract.User.PHONE,
                FeedReaderContract.User.PICUTRE
        };
        String selection = FeedReaderContract.User.NAME + " = ? AND "+  FeedReaderContract.User.PASSWORD + "= ?";
        String[] selectionArgs = { username , password };

        String sortOrder =
                FeedReaderContract.User.NAME + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.User._ID));
            itemIds.add(itemId);
        }
        if(itemIds.size()>0){ //kullanıcı bulundu
            cursor.moveToPosition(0);
            chosenUser = new User(cursor.getString( cursor.getColumnIndexOrThrow(FeedReaderContract.User.NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.User.SURNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.User.PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.User.EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.User.PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.User.BDATE)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.User.PICUTRE)));
            Toast.makeText(LogIn.this, "Logged In Successfuly",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LogIn.this, Menu.class);
            intent.putExtra("chosenUser", chosenUser);
            startActivity(intent);
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void PairUI() {
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUp);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

    }

}