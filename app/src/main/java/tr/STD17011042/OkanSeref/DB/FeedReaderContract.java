package tr.STD17011042.OkanSeref.DB;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;

public class FeedReaderContract {
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class User implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "users";
        public static final String NAME = "name";
        public static final String SURNAME = "sname";
        public static final String PASSWORD = "pass";
        public static final String EMAIL = "e";
        public static final String PHONE = "phoneVal";
        public static final String BDATE = "b";
        public static final String PICUTRE = "pic";
    }
    public static class Question implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "questions";
        public static final String QUESTION_TEXT = "qtext";
        public static final String OWNER = "owner";
        public static final String ANS1 = "ans1";
        public static final String ANS2 = "ans2";
        public static final String ANS3 = "ans3";
        public static final String ANS4 = "ans4";
        public static final String ANS5 = "ans5";
        public static final String CORRECT_ANSWER = "crts";
        public static final String MEDIATYPE = "type";
        public static final String PICUTRE = "image";
        public static final String MEDIAPATH = "path";

    }

}
