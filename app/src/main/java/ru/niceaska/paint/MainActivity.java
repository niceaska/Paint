package ru.niceaska.paint;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonClear;
    private Spinner modeSpinner;
    private Spinner colorSpinner;
    private ImageButton buttonBack;
    private DrawView drawView;
    private List<DrawColor> colorsList = Arrays.asList(DrawColor.values());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initModeSpinner();
        initColorSpinner();
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clear();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawView.changeBack();
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
        });
        colorSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int color = getResources().getColor(colorsList.get(position).getColor());
                drawView.setColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initColorSpinner() {
        colorSpinner.setAdapter(new ColorSpinnerAdapter(this, colorsList));
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
        colorSpinner = findViewById(R.id.spinner_draw_color);
        buttonBack = findViewById(R.id.button_back);
    }

}
