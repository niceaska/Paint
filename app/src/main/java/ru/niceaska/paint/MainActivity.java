package ru.niceaska.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button buttonClear;
    private Spinner modeSpinner;
    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initModeSpinner();
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clear();
            }
        });
        modeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drawView.setMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );

    }

    private void initModeSpinner() {
        modeSpinner.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.draw_modes)))
        );
    }

    private void initViews() {
        buttonClear = findViewById(R.id.button_drawview);
        modeSpinner = findViewById(R.id.spinner_draw_mode);
        drawView = findViewById(R.id.drawview);
        modeSpinner = findViewById(R.id.spinner_draw_mode);

    }


}
