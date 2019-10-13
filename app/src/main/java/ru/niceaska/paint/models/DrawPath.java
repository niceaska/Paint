package ru.niceaska.paint.models;

import android.graphics.Path;

import ru.niceaska.paint.CustomDrawable;

public class DrawPath implements CustomDrawable {
    private Path path;
    private int color;
    private int stroke;


    public DrawPath(Path path, int stroke) {
        this.path = path;
        this.stroke = stroke;
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

    public int getStroke() {
        return stroke;
    }
}
