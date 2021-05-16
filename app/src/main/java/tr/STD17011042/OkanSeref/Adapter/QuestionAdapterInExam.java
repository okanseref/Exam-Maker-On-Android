package tr.STD17011042.OkanSeref.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.List;

import tr.STD17011042.OkanSeref.Question;
import tr.STD17011042.OkanSeref.R;

import static tr.STD17011042.OkanSeref.NewExam.selectedQuestions;


public class QuestionAdapterInExam extends RecyclerView.Adapter<QuestionAdapterInExam.ViewHolder> {

    private final List<Question> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView rowText,rightAnswer;
        private final ImageView rowImage;
        private final VideoView videoView;
        private final Button rowUpdate,rowDelete,soundButton;
        private final LinearLayout background;
        private final TextView[] answerTexts = new TextView[5];
        private boolean videoSwitch,soundSwitch;
        private MediaPlayer mp;

        public ViewHolder(View view) {
            super(view);
            background = (LinearLayout) view.findViewById(R.id.row_background);
            rowUpdate = (Button) view.findViewById(R.id.rowUpdate);
            rowDelete = (Button) view.findViewById(R.id.rowDelete);
            rowText = (TextView) view.findViewById(R.id.rowText);
            videoView = (VideoView) view.findViewById(R.id.rowVideo);
            soundButton = (Button) view.findViewById(R.id.rowSound);
            rightAnswer = (TextView) view.findViewById(R.id.rowRightAnswer);
            rowImage = (ImageView) view.findViewById(R.id.rowImage);
            answerTexts[0]=(TextView) view.findViewById(R.id.rowAns1);
            answerTexts[1]=(TextView) view.findViewById(R.id.rowAns2);
            answerTexts[2]=(TextView) view.findViewById(R.id.rowAns3);
            answerTexts[3]=(TextView) view.findViewById(R.id.rowAns4);
            answerTexts[4]=(TextView) view.findViewById(R.id.rowAns5);
            videoSwitch=false;
            soundSwitch=false;
            mp=new MediaPlayer();


        }


        public void setSoundSwitch(boolean soundSwitch) {
            this.soundSwitch = soundSwitch;
        }

        public MediaPlayer getMediaPlayer() {
            return mp;
        }


        public Button getSoundButton() {
            return soundButton;
        }

        public boolean isSoundSwitch() {
            return soundSwitch;
        }

        public void setVideoSwitch(boolean videoSwitch) {
            this.videoSwitch = videoSwitch;
        }

        public boolean isVideoSwitch() {
            return videoSwitch;
        }
        public VideoView getVideoView() {
            return videoView;
        }
        public LinearLayout getBackground() {
            return background;
        }

        public TextView[] getAnswerTexts() {
            return answerTexts;
        }

        public Button getRowUpdate() {
            return rowUpdate;
        }

        public Button getRowDelete() {
            return rowDelete;
        }

        public TextView getRightAnswer() {
            return rightAnswer;
        }

        public ImageView getRowImage() {
            return rowImage;
        }

        public TextView getTextView() {
            return rowText;
        }
    }

    public QuestionAdapterInExam(List<Question> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTextView().setText(localDataSet.get(position).getQuestionText());
        viewHolder.getAnswerTexts()[0].setText("A) "+localDataSet.get(position).getAnswer1());
        viewHolder.getAnswerTexts()[1].setText("B) "+localDataSet.get(position).getAnswer2());
        viewHolder.getAnswerTexts()[2].setText("C) "+localDataSet.get(position).getAnswer3());
        viewHolder.getAnswerTexts()[3].setText("D) "+localDataSet.get(position).getAnswer4());
        viewHolder.getAnswerTexts()[4].setText("E) "+localDataSet.get(position).getAnswer5());
        viewHolder.getRightAnswer().setText(viewHolder.getAnswerTexts()[localDataSet.get(position).getCorrectAnswer()].getText());

        switch (localDataSet.get(position).getMediaType()) {
            case 0:
                viewHolder.getRowImage().setVisibility(View.INVISIBLE);
                break;
            case 1:
                Bitmap bitmap = BitmapFactory.decodeByteArray(localDataSet.get(position).getImage(), 0,localDataSet.get(position).getImage().length );
                viewHolder.getRowImage().setImageBitmap(bitmap);
                break;
            case 2:
                viewHolder.getVideoView().setVisibility(View.VISIBLE);
                viewHolder.getRowImage().setVisibility(View.GONE);
                viewHolder.getVideoView().setVideoPath(localDataSet.get(position).getMediaPath());
                viewHolder.getVideoView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewHolder.isVideoSwitch()){
                            viewHolder.getVideoView().start();
                            viewHolder.setVideoSwitch(false);
                        }else {
                            viewHolder.getVideoView().pause();
                            viewHolder.setVideoSwitch(true);
                        }
                    }
                });
                break;
            case 3:
                viewHolder.getVideoView().setVisibility(View.GONE);
                viewHolder.getRowImage().setVisibility(View.GONE);
                viewHolder.getSoundButton().setVisibility(View.VISIBLE);

                try {
                    viewHolder.getMediaPlayer().setDataSource(localDataSet.get(position).getMediaPath());

                } catch (IOException e) {

                    e.printStackTrace();
                }
                viewHolder.getMediaPlayer().prepareAsync();

                viewHolder.getSoundButton().setClickable(false);

                viewHolder.getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        viewHolder.getSoundButton().setClickable(true);
                        //viewHolder.getMediaPlayer().start();
                    }
                });
                viewHolder.getSoundButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!viewHolder.isSoundSwitch()){
                            viewHolder.getSoundButton().setText("PAUSE");
                            viewHolder.getMediaPlayer().start();
                            viewHolder.setSoundSwitch(true);
                        }else{
                            viewHolder.getSoundButton().setText("START");
                            viewHolder.getMediaPlayer().pause();
                            viewHolder.setSoundSwitch(false);
                        }
                    }
                });
                break;
        }
        viewHolder.getRowUpdate().setVisibility(View.INVISIBLE);
        viewHolder.getRowDelete().setText("Add");
        viewHolder.getRowDelete().setBackground(ResourcesCompat.getDrawable(viewHolder.getRightAnswer().getContext().getResources(),R.drawable.buttongreen,null));
        viewHolder.getRowDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.getRowDelete().getText().equals("Add")){
                    selectedQuestions.add(localDataSet.get(position).get_ID());
                    viewHolder.getRowDelete().setText("Remove");
                    viewHolder.getRowDelete().setBackground(ResourcesCompat.getDrawable(viewHolder.getRightAnswer().getContext().getResources(),R.drawable.buttonred,null));
                    //viewHolder.getRowDelete().setBackgroundColor(ContextCompat.getColor(viewHolder.getRightAnswer().getContext(),R.color.red));
                }else{
                    selectedQuestions.remove((Object)localDataSet.get(position).get_ID());
                    viewHolder.getRowDelete().setText("Add");
                    viewHolder.getRowDelete().setBackground(ResourcesCompat.getDrawable(viewHolder.getRightAnswer().getContext().getResources(),R.drawable.buttongreen,null));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}