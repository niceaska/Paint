package ru.niceaska.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DrawView extends View {

    private int color;
    private int mode = 0;

    private static final int SIMPLE_MODE = 0;
    private static final int BOX_MODE = 1;
    private static final int LINE_MODE = 2;

    private Paint drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bgPaint = new Paint();

    private List<Box> boxList = new ArrayList<>();
    private List<Line> lineList = new ArrayList<>();
    private List<DrawPath> drawPaths = new ArrayList<>();

    private Box currentBox;
    private Path currentPath;
    private DrawPath currentDrawPath;
    private Line currentLine;

    public DrawView(Context context) {
        super(context, null);
        setUpPaint();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (mode) {
            case SIMPLE_MODE:
                return simpleDrawEvent(event);
            case BOX_MODE:
                return boxDrawEvent(event);
            case LINE_MODE:
                return lineDrawEvent(event);
            default:
                return super.onTouchEvent(event);
        }
    }

    private boolean lineDrawEvent(MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        PointF current = new PointF(x, y);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                this.currentLine = new Line(current, color);
                lineList.add(currentLine);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentLine != null) {
                    currentLine.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentLine = null;
                break;
            default:
                return super.onTouchEvent(event);
        }
        invalidate();
        return true;
    }

    private boolean simpleDrawEvent(MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if (this.currentPath == null) {
                    this.currentPath = new Path();
                } else {
                    this.currentPath.reset();
                }
                currentPath.moveTo(x, y);
                if (currentDrawPath == null) {
                    this.currentDrawPath = new DrawPath(currentPath);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (currentDrawPath != null) {
                    currentDrawPath.getPath().lineTo(x, y);
                    currentDrawPath.setColor(color);
                }
                drawPaths.add(currentDrawPath);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentDrawPath = null;
                currentPath = null;
                break;
            default:
                return super.onTouchEvent(event);
        }
        invalidate();
        return true;
    }

    private boolean boxDrawEvent(MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        PointF current = new PointF(x, y);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                this.currentBox = new Box(current, color);
                boxList.add(currentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentBox != null) {
                    currentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentBox = null;
                break;
            default:
                return super.onTouchEvent(event);
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(bgPaint);
        setUpPaint();
        drawPath(canvas);
        drawRect(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        for (Line line : lineList) {
            float left = line.getOrigin().x;
            float top = line.getOrigin().y;

            float right = (line.getCurrent().x < left) ? Math.min(line.getCurrent().x, line.getOrigin().x) :
                    Math.max(line.getCurrent().x, line.getOrigin().x);
            float bottom = (line.getCurrent().y < top) ? Math.min(line.getCurrent().y, line.getOrigin().y) :
                    Math.max(line.getCurrent().y, line.getOrigin().y);
            drawPaint.setColor(line.getColor());
            canvas.drawLine(left, top, right, bottom, drawPaint);
        }
    }

    private void drawPath(Canvas canvas) {
        for (DrawPath drawPath : drawPaths) {
            Path path = drawPath.getPath();
            drawPaint.setColor(drawPath.getColor());
            canvas.drawPath(path, drawPaint);
        }
    }

    private void drawRect(Canvas canvas) {
        for (Box box : boxList) {
            float left = Math.min(box.getCurrent().x, box.getOrigin().x);
            float right = Math.max(box.getCurrent().x, box.getOrigin().x);
            float top = Math.min(box.getCurrent().y, box.getOrigin().y);
            float bottom = Math.max(box.getCurrent().y, box.getOrigin().y);
            boxPaint.setColor(box.getColor());
            canvas.drawRect(left, top, right, bottom, boxPaint);
        }
    }

    private void setUpPaint() {
        bgPaint.setColor(Color.WHITE);
        drawPaint.setColor(color);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setColor(color);
    }

    public void clear() {
        drawPaths.clear();
        lineList.clear();
        boxList.clear();
        bgPaint.setColor(Color.WHITE);
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
