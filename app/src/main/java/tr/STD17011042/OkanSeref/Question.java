package tr.STD17011042.OkanSeref;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String owner,questionText,answer1,answer2,answer3,answer4,answer5,mediaPath;
    private int correctAnswer,mediaType,_ID;
    private byte[] image;

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public Question() {
    }

    public Question(String owner, String questionText, String answer1, String answer2, String answer3, String answer4, String answer5, int correctAnswer, byte[] image) {
        this.owner = owner;
        this.questionText = questionText;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.correctAnswer = correctAnswer;
        this.image = image;
        mediaType=0; // default 0 , image 1 , video 2 , sound 3
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Question(String owner, String questionText, String answer1, String answer2, String answer3, String answer4, String answer5, int correctAnswer) {
        this.owner = owner;
        this.questionText = questionText;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.correctAnswer = correctAnswer;
    }
    public List<String> getWrongAnswers(){
        List<String> list = new ArrayList<>();
        switch (getCorrectAnswer()){
            case 0:
                list.add(getAnswer2());
                list.add(getAnswer3());
                list.add(getAnswer4());
                list.add(getAnswer5());
            break;
            case 1:
                list.add(getAnswer1());
                list.add(getAnswer3());
                list.add(getAnswer4());
                list.add(getAnswer5());
            break;
            case 2:
                list.add(getAnswer2());
                list.add(getAnswer1());
                list.add(getAnswer4());
                list.add(getAnswer5());
            break;
            case 3:
                list.add(getAnswer2());
                list.add(getAnswer3());
                list.add(getAnswer1());
                list.add(getAnswer5());
            break;
            case 4:
                list.add(getAnswer2());
                list.add(getAnswer3());
                list.add(getAnswer4());
                list.add(getAnswer1());
            break;
        }
        return list;
    }
    public String getCorrectAnswerText(){
        switch (getCorrectAnswer()){
            case 0:
                return getAnswer1();
            case 1:
                return getAnswer2();
            case 2:
                return getAnswer3();
            case 3:
                return getAnswer4();
            case 4:
                return getAnswer5();
        }
        return "0";
    }
    public byte[] getImage() {
        return image;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}