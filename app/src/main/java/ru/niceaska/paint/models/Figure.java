package ru.niceaska.paint.models;

import android.graphics.PointF;
import android.util.SparseArray;

import ru.niceaska.paint.CustomDrawable;

public class Figure implements CustomDrawable {

    private SparseArray<PointF> points = new SparseArray<>();
    private int color;
    private int stroke;

    public Figure(int color, int stroke) {
        this.color = color;
        this.stroke = stroke;
    }

    public int getColor() {
        return color;
    }


    public SparseArray<PointF> getPoints() {
        return points;
    }

    public int getStroke() {
        return stroke;
    }
}
