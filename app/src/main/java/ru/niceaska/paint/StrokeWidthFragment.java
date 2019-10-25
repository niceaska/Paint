package ru.niceaska.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class StrokeWidthFragment extends DialogFragment implements ProgressChanger {

    private int currentProgress;

    private static final String STROKE_WIDTH = "strokeWidth";

    public static StrokeWidthFragment newInstance(int strokeWidth) {

        Bundle args = new Bundle();
        args.putInt(STROKE_WIDTH, strokeWidth);
        StrokeWidthFragment fragment = new StrokeWidthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.stroke_width_dialog, null);
        final SeekBar strokeWidth = dialogView.findViewById(R.id.dialog_seekbar);
        currentProgress = getArguments().getInt(STROKE_WIDTH);
        final int oldProgress = currentProgress;
        strokeWidth.setProgress(currentProgress);
        strokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
                changeProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        builder.setView(dialogView)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeProgress(currentProgress);
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeProgress(oldProgress);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void changeProgress(int progress) {
        Activity parent = getActivity();
        if (parent instanceof StrokeWidthChanger) {
            ((StrokeWidthChanger) parent).changeStrokeWidth(progress);
        }
    }

}
