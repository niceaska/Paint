package ru.niceaska.paint;

import android.graphics.PointF;

public class Line {
    private PointF origin;
    private PointF current;
    private int color;

    public Line(PointF origin, int color) {
        this.origin = origin;
        this.current = origin;
        this.color = color;
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
}
