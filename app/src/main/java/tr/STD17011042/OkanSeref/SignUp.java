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
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import tr.STD17011042.OkanSeref.DB.FeedReaderContract;
import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;


public class SignUp extends AppCompatActivity {
    EditText nameEditText,passwordEditText,passwordAgainEditText,emailEditTexxt,phoneEditText,bDateEditText,surnameEditText;
    Button signUpButton;
    ImageButton addImage;
    TextView signUpError;
    public static final int PICK_PHOTO_FOR_AVATAR = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FillUI();
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent .setType("image/*");
                startActivityForResult(intent , PICK_PHOTO_FOR_AVATAR);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = nameEditText.getText().toString(),password=passwordEditText.getText().toString(),password2=passwordAgainEditText.getText().toString();
                String email=emailEditTexxt.getText().toString();
                String phone=phoneEditText.getText().toString();
                String date=bDateEditText.getText().toString();
                String surname=surnameEditText.getText().toString();
                if(!CheckUsername(username)){
                    Toast.makeText(SignUp.this, "Username is already taken",Toast.LENGTH_LONG).show();
                }else if(!password.equals(password2)){
                    Toast.makeText(SignUp.this, (String)"Passwords does not match",Toast.LENGTH_LONG).show();
                }else if(!isValidEmail(email)){
                    Toast.makeText(SignUp.this, (String)"Wrong email address",Toast.LENGTH_LONG).show();
                }else if(!isValidDate(date)){
                    Toast.makeText(SignUp.this, (String)"Wrong date format (dd-MM-yyyy)",Toast.LENGTH_LONG).show();
                }else if(!isValidPhone(phone)){
                    Toast.makeText(SignUp.this, (String)"Wrong phone",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(SignUp.this, (String)"Sign Up Completed",Toast.LENGTH_LONG).show();

                    User newUser= new User(username,surname,password,email,phone,date,DrawableToByte(addImage.getDrawable()));
                    InsertUser(newUser);
                    //database.add(new User(username,surname,password,email,phone,date,image));
                    finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (reqCode == PICK_PHOTO_FOR_AVATAR) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    addImage.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                }

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
    private boolean CheckUsername(String username){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.User.NAME,
        };

        String selection = FeedReaderContract.User.NAME + " = ?";
        String[] selectionArgs = { username  };

        String sortOrder = FeedReaderContract.User.NAME + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {
            if(username.equals(cursor.getString( cursor.getColumnIndexOrThrow(FeedReaderContract.User.NAME)))){
                cursor.close();
                return false; //Kullanıcı adı varsa false dönüyor
            }
        }
        cursor.close();
        db.close();
        return true;
    }
    private void FillUI(){
        nameEditText=findViewById(R.id.signUpName);
        passwordEditText=findViewById(R.id.signUpPass);
        signUpButton=findViewById(R.id.signUpButton);
        addImage=findViewById(R.id.signUpAddImage);
        passwordAgainEditText=findViewById(R.id.signUpPassAgain);
        emailEditTexxt=findViewById(R.id.signUpEmail);
        phoneEditText=findViewById(R.id.signUpPhone);
        surnameEditText=findViewById(R.id.signUpSurname);
        bDateEditText=findViewById(R.id.signUpBirthDate);
    }

    private void InsertUser(User user){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.User.NAME, user.getUsername());
        values.put(FeedReaderContract.User.SURNAME, user.getSurname());
        values.put(FeedReaderContract.User.BDATE, user.getBdate());
        values.put(FeedReaderContract.User.EMAIL, user.getEmail());
        values.put(FeedReaderContract.User.PASSWORD, user.getPassword());
        values.put(FeedReaderContract.User.PHONE, user.getPhone());

        values.put(FeedReaderContract.User.PICUTRE, user.getImage());
        long newRowId = db.insert(FeedReaderContract.User.TABLE_NAME, null, values);
        //dbHelper.onCreate(db);
    }
    private byte[] DrawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        bitmap=makeSmallerImage(bitmap,480);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
        return stream.toByteArray();
    }
    private boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private  boolean isValidDate(String target) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(target.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    private boolean isValidPhone(String target) {
        if(target != null && target.matches("[0-9]{10}")){ //10 karakter 0-9 arası
            return  true;
        }else{
            return false;
        }
    }
}