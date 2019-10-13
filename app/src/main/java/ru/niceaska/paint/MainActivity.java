package ru.niceaska.paint;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import ru.niceaska.paint.models.DrawColor;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonClear;
    private Spinner modeSpinner;
    private Spinner colorSpinner;
    private ImageButton buttonBack;
    private ImageButton buttonScroll;
    private ImageButton buttonStroke;
    private ImageButton buttonHideTools;
    private ImageButton buttonShowTools;
    private StrokeWidthFragment strokeWidthFragment;
    private DrawView drawView;
    private View toolsView;
    private boolean isToolsVisible;
    private List<DrawColor> colorsList = Arrays.asList(DrawColor.values());

    private static final String WIDTH_DIALOG = "WIDTH_DIALOG";
    private static final String TOOLS_VISIBLE = "TOOLS_VISIBLE";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initModeSpinner();
        initColorSpinner();

        setButtonsListeners();
        setSpinnersListeners();
    }

    private void setSpinnersListeners() {
        modeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    buttonScroll.getDrawable().mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                    drawView.setScrollGesture(false);
                    buttonScroll.setEnabled(false);
                } else {
                    buttonScroll.setEnabled(true);
                }
                drawView.setMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


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

    private void setButtonsListeners() {
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

        buttonStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strokeWidthFragment.show(getSupportFragmentManager(), WIDTH_DIALOG);
            }
        });

        buttonHideTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsView.setVisibility(View.GONE);
                buttonShowTools.setVisibility(View.VISIBLE);
                isToolsVisible = false;

            }
        });

        buttonShowTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsView.setVisibility(View.VISIBLE);
                buttonShowTools.setVisibility(View.GONE);
                isToolsVisible = true;
            }
        });

        buttonScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isScrollGesture = drawView.isScrollGesture();
                if (isScrollGesture) {
                    buttonScroll.getDrawable().mutate()
                            .setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN);
                } else {
                    buttonScroll.getDrawable().mutate()
                            .setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                }
                drawView.setScrollGesture(!isScrollGesture);
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
        isToolsVisible = true;
        buttonClear = findViewById(R.id.button_drawview);
        modeSpinner = findViewById(R.id.spinner_draw_mode);
        drawView = findViewById(R.id.drawview);
        toolsView = findViewById(R.id.draw_tools);
        modeSpinner = findViewById(R.id.spinner_draw_mode);
        colorSpinner = findViewById(R.id.spinner_draw_color);
        buttonBack = findViewById(R.id.button_back);
        buttonScroll = findViewById(R.id.button_scroll_mode);
        strokeWidthFragment = new StrokeWidthFragment(drawView);
        buttonStroke = findViewById(R.id.button_stroke_width);
        buttonHideTools = findViewById(R.id.button_hide_tools);
        buttonShowTools = findViewById(R.id.button_drop_down);
        buttonScroll.setEnabled(false);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(TOOLS_VISIBLE, isToolsVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.isToolsVisible = savedInstanceState.getBoolean(TOOLS_VISIBLE);
        if (!this.isToolsVisible) {
            toolsView.setVisibility(View.GONE);
            buttonShowTools.setVisibility(View.VISIBLE);
        }
    }
}
