package tr.STD17011042.OkanSeref.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.List;

import tr.STD17011042.OkanSeref.DB.FeedReaderDbHelper;
import tr.STD17011042.OkanSeref.Question;
import tr.STD17011042.OkanSeref.QuestionList;
import tr.STD17011042.OkanSeref.R;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<Question> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView rowText,rightAnswer;
        private final ImageView rowImage;
        private final VideoView videoView;
        private final Button rowUpdate,rowDelete,soundButton;
        private final LinearLayout background;
        private final TextView[] answerTexts = new TextView[5];
        boolean videoSwitch,soundSwitch;
        private MediaPlayer mediaPlayer;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            background = (LinearLayout) view.findViewById(R.id.row_background);
            videoView = (VideoView) view.findViewById(R.id.rowVideo);
            rowUpdate = (Button) view.findViewById(R.id.rowUpdate);
            rowDelete = (Button) view.findViewById(R.id.rowDelete);
            soundButton = (Button) view.findViewById(R.id.rowSound);
            rowText = (TextView) view.findViewById(R.id.rowText);
            rightAnswer = (TextView) view.findViewById(R.id.rowRightAnswer);
            rowImage = (ImageView) view.findViewById(R.id.rowImage);
            answerTexts[0]=(TextView) view.findViewById(R.id.rowAns1);
            answerTexts[1]=(TextView) view.findViewById(R.id.rowAns2);
            answerTexts[2]=(TextView) view.findViewById(R.id.rowAns3);
            answerTexts[3]=(TextView) view.findViewById(R.id.rowAns4);
            answerTexts[4]=(TextView) view.findViewById(R.id.rowAns5);
            videoSwitch=false;
            soundSwitch=false;
            mediaPlayer=new MediaPlayer();
        }

        public void setSoundSwitch(boolean soundSwitch) {
            this.soundSwitch = soundSwitch;
        }

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
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

    public QuestionAdapter(List<Question> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
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
                viewHolder.getVideoView().setVisibility(View.GONE);
                viewHolder.getRowImage().setVisibility(View.VISIBLE);
                viewHolder.getSoundButton().setVisibility(View.GONE);
                viewHolder.getRowImage().setImageBitmap(BitmapFactory.decodeByteArray(localDataSet.get(position).getImage(), 0,localDataSet.get(position).getImage().length ));
                //Bitmap bitmap = BitmapFactory.decodeByteArray(localDataSet.get(position).getImage(), 0,localDataSet.get(position).getImage().length );
                //viewHolder.getRowImage().setImageBitmap(bitmap);

                break;
            case 2:
                viewHolder.getVideoView().setVisibility(View.VISIBLE);
                viewHolder.getRowImage().setVisibility(View.GONE);
                viewHolder.getSoundButton().setVisibility(View.GONE);

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
                    System.out.println("ASYNC başalamadı");

                    e.printStackTrace();
                }
                viewHolder.getMediaPlayer().prepareAsync();
                System.out.println("ASYNC BAŞLADIII");

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

        viewHolder.getRowDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewHolder.getRightAnswer().getContext());
                builder1.setMessage("Question will be deleted. Are you sure?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //put your code that needed to be executed when okay is clicked
                                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(viewHolder.getRightAnswer().getContext());
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.DeleteQuestion(db,String.valueOf(localDataSet.get(position).get_ID()));
                                ((QuestionList)viewHolder.getRightAnswer().getContext()).RefreshQuestionList();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        viewHolder.getRowUpdate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuestionList)viewHolder.getRightAnswer().getContext()).UpdateQuestion(localDataSet.get(position));
            }
        });

    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}