package ru.niceaska.paint;

import android.graphics.Path;

public class DrawPath {
    private Path path;
    private int color;


    public DrawPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public int getColor() {
        return color;
    }


    public void setColor(int color) {
        this.color = color;
    }
}
