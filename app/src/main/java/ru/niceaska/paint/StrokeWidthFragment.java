package ru.niceaska.paint;

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

public class StrokeWidthFragment extends DialogFragment {

    private DrawView drawView;

    public StrokeWidthFragment(DrawView drawView) {
        this.drawView = drawView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.stroke_width_dialog, null);
        final SeekBar strokeWidth = dialogView.findViewById(R.id.dialog_seekbar);

        strokeWidth.setProgress(drawView.getStrokeWidth());
        builder.setView(dialogView)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawView.setStrokeWidth(strokeWidth.getProgress());
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }
}
