package tr.STD17011042.OkanSeref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class ExamSettings extends AppCompatActivity {
    private EditText point,duration;
    private Spinner difficulty;
    private Button save;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_settings);
        sharedPreferences = getApplicationContext().getSharedPreferences("prefs",MODE_PRIVATE);
        PairUI();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("difficulty",Integer.parseInt(difficulty.getSelectedItem().toString()));
                editor.putInt("duration",Integer.parseInt(duration.getText().toString()));
                editor.putInt("point",Integer.parseInt(point.getText().toString()));
                editor.apply();
                Toast.makeText(ExamSettings.this, "Settings saved",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void PairUI(){
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
}