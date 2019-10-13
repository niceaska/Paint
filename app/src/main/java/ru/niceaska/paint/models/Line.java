package ru.niceaska.paint.models;

import android.graphics.PointF;

import ru.niceaska.paint.CustomDrawable;

public class Line implements CustomDrawable {
    private PointF origin;
    private PointF current;
    private int color;
    private int stroke;

    public Line(PointF origin, int color, int strokeWidth) {
        this.origin = origin;
        this.current = origin;
        this.color = color;
        this.stroke = strokeWidth;
    }

    public int getColor() {
        return color;
    }

    public PointF getOrigin() {
        return origin;
    }

    public PointF getCurrent() {
        return current;
    }

    public void setCurrent(PointF current) {
        this.current = current;
    }

    public int getStroke() {
        return stroke;
    }
}
