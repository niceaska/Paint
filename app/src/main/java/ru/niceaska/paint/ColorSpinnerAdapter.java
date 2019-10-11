package ru.niceaska.paint;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ColorSpinnerAdapter extends ArrayAdapter<DrawColor> {


    public ColorSpinnerAdapter(@NonNull Context context, @NonNull List<DrawColor> objects) {
        super(context, R.layout.color_list, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.color_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.colorName.setText(parent.getContext()
                .getResources()
                .getString(getItem(position).getId()));

        holder.colorLabel.getDrawable().mutate().setColorFilter(parent.getContext()
                .getResources()
                .getColor(getItem(position).getColor()), PorterDuff.Mode.SRC_IN);
        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private class ViewHolder {

        private TextView colorName;
        private ImageView colorLabel;

        ViewHolder(View v) {
            colorName =  v.findViewById(R.id.color_name);
            colorLabel = v.findViewById(R.id.color_image);
        }
    }
}
