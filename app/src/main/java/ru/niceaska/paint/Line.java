package ru.niceaska.paint;

import android.graphics.PointF;

public class Line {
    private PointF origin;
    private PointF current;

    public Line(PointF origin) {
        this.origin = origin;
        this.current = origin;
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
