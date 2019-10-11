package ru.niceaska.paint;

public enum DrawColor {
    RED(R.string.red, R.color.red),
    GREEN(R.string.green, R.color.green),
    ORANGE(R.string.orange, R.color.orange),
    PURPLE(R.string.purple, R.color.purple),
    BLACK(R.string.black, R.color.black),
    YELLOW(R.string.yellow, R.color.yellow),
    BLUE(R.string.blue, R.color.blue);

    private int id;
    private int color;

    DrawColor(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
}
