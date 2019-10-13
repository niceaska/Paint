package ru.niceaska.paint.models;

import android.graphics.PointF;
import android.util.SparseArray;

import ru.niceaska.paint.CustomDrawable;

public class Figure implements CustomDrawable {

    private SparseArray<PointF> points = new SparseArray<>();
    private int color;

    public Figure(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }


    public SparseArray<PointF> getPoints() {
        return points;
    }
}
