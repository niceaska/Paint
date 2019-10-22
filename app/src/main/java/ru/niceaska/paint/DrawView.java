package ru.niceaska.paint;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.paint.models.Box;
import ru.niceaska.paint.models.DrawPath;
import ru.niceaska.paint.models.Figure;
import ru.niceaska.paint.models.Line;


public class DrawView extends View {

    private int color;
    private int strokeWidth;
    private int mode;

    private static final int SIMPLE_MODE = 0;
    private static final int BOX_MODE = 1;
    private static final int LINE_MODE = 2;
    private static final int FIGURE_MODE = 3;

    private static final int DEFAULT_STROKE_SIZE = 10;

    private Paint drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bgPaint = new Paint();
    private boolean scrollGesture = false;
    private GestureDetector gestureDetector;

    private List<CustomDrawable> allDrawItems = new ArrayList<>();

    private Box currentBox;
    private Path currentPath;
    private DrawPath currentDrawPath;
    private Line currentLine;
    private Figure currentFigure;

    public DrawView(Context context) {
        super(context, null);
        obtainAttrs(context, null);
        setUpPaint();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        obtainAttrs(context, attrs);
        setUpPaint();
    }

    private void obtainAttrs(Context context, AttributeSet attrs) {
        final Resources.Theme theme  = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.DrawView,
                0, 0);
        try {
            color = typedArray.getInteger( R.styleable.DrawView_color,
                        context.getResources().getColor(R.color.black));
            strokeWidth = typedArray.getInteger( R.styleable.DrawView_stroke_width, DEFAULT_STROKE_SIZE);
            mode = typedArray.getInteger( R.styleable.DrawView_mode, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollGesture) {
            return gestureDetector.onTouchEvent(event);
        }

        switch (mode) {
            case SIMPLE_MODE:
                return simpleDrawEvent(event);
            case BOX_MODE:
                return boxDrawEvent(event);
            case LINE_MODE:
                return lineDrawEvent(event);
            case FIGURE_MODE:
                return drawFigureEvent(event);
            default:
                return super.onTouchEvent(event);
        }
    }

    private boolean lineDrawEvent(MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        PointF current = new PointF(x, y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.currentLine = new Line(current, color, strokeWidth);
                allDrawItems.add(currentLine);
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

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (this.currentPath == null) {
                    this.currentPath = new Path();
                } else {
                    this.currentPath.reset();
                }
                currentPath.moveTo(x, y);
                if (currentDrawPath == null) {
                    this.currentDrawPath = new DrawPath(currentPath, strokeWidth);
                }
                allDrawItems.add(currentDrawPath);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (currentDrawPath != null) {
                    currentDrawPath.getPath().lineTo(x, y);
                    currentDrawPath.setColor(color);
                }
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
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.currentBox = new Box(current, color);
                allDrawItems.add(currentBox);
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

    private boolean drawFigureEvent(MotionEvent event) {

        int pointerId = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (currentFigure == null) {
                    currentFigure = new Figure(color, strokeWidth);
                }
                SparseArray<PointF> currentPoints = currentFigure.getPoints();
                currentPoints.put(pointerId, new PointF(event.getX(event.getActionIndex()),
                        event.getY(event.getActionIndex())));
                allDrawItems.add(currentFigure);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerId = event.getPointerId(event.getActionIndex());
                PointF point = currentFigure.getPoints().get(pointerId);
                if (point == null) {
                    point =  new PointF();
                    currentFigure.getPoints().put(pointerId, point);
                }
                point.x = event.getX(event.getActionIndex());
                point.y = event.getY(event.getActionIndex());
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    PointF pointF = currentFigure.getPoints().get(id);
                    pointF.x = event.getX(i);
                    pointF.y = event.getY(i);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentFigure = null;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(bgPaint);
        for (CustomDrawable drawItem : allDrawItems) {
            if (drawItem instanceof DrawPath) {
                drawPath((DrawPath) drawItem, canvas);
            } else if (drawItem instanceof Box) {
                drawRect((Box) drawItem, canvas);
            } else if (drawItem instanceof Line) {
                drawLine((Line) drawItem, canvas);
            } else if (drawItem instanceof Figure) {
                drawFigure((Figure) drawItem, canvas);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private void drawLine(Line line, Canvas canvas) {
        float left = line.getOrigin().x;
        float top = line.getOrigin().y;

        float right = (line.getCurrent().x < left) ? Math.min(line.getCurrent().x, line.getOrigin().x) :
                Math.max(line.getCurrent().x, line.getOrigin().x);
        float bottom = (line.getCurrent().y < top) ? Math.min(line.getCurrent().y, line.getOrigin().y) :
                Math.max(line.getCurrent().y, line.getOrigin().y);
        drawPaint.setColor(line.getColor());
        drawPaint.setStrokeWidth(line.getStroke());
        canvas.drawLine(left, top, right, bottom, drawPaint);

    }

    private void drawPath(DrawPath drawPath, Canvas canvas) {
        Path path = drawPath.getPath();
        drawPaint.setColor(drawPath.getColor());
        drawPaint.setStrokeWidth(drawPath.getStroke());
        canvas.drawPath(path, drawPaint);
    }

    private void drawRect(Box box, Canvas canvas) {
        float left = Math.min(box.getCurrent().x, box.getOrigin().x);
        float right = Math.max(box.getCurrent().x, box.getOrigin().x);
        float top = Math.min(box.getCurrent().y, box.getOrigin().y);
        float bottom = Math.max(box.getCurrent().y, box.getOrigin().y);
        boxPaint.setColor(box.getColor());
        canvas.drawRect(left, top, right, bottom, boxPaint);
    }

    private void drawFigure(Figure figure, Canvas canvas) {

        drawPaint.setColor(figure.getColor());
        drawPaint.setStrokeWidth(figure.getStroke());

        SparseArray<PointF> points = figure.getPoints();
        int pointsSize = points.size();

        if (pointsSize == 0) return;

        if (pointsSize == 1) {
            PointF point = points.get(0);
            if (point == null ) return;
            canvas.drawPoint(point.x, point.y, drawPaint);

        } else {
            for (int i = 1;  i < points.size(); i++) {
                PointF one = points.get(i - 1);
                PointF two = points.get(i);
                canvas.drawLine(one.x, one.y, two.x, two.y, drawPaint);
            }
            if (pointsSize > 2) {
                PointF one = points.get(pointsSize - 1);
                PointF two = points.get(0);
                canvas.drawLine(one.x, one.y, two.x, two.y, drawPaint);
            }
        }
    }

    private void setUpPaint() {
        bgPaint.setColor(Color.WHITE);
        drawPaint.setColor(color);
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setColor(color);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (allDrawItems.isEmpty()) return false;
                CustomDrawable item = allDrawItems.get(allDrawItems.size() - 1);
                if (item instanceof  Figure) {
                    figureScrollHandler(distanceX, distanceY, (Figure) item);
                } else if (item instanceof Box) {
                    boxScrollHandler(distanceX, distanceY, (Box) item);
                } else if (item instanceof Line) {
                    lineScrollHandler(distanceX, distanceY, (Line) item);
                } else {
                    return false;
                }
                invalidate();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    private void lineScrollHandler(float distanceX, float distanceY, Line item) {
        PointF origin = item.getOrigin();
        PointF current = item.getCurrent();
        origin.x -= distanceX;
        origin.y -= distanceY;
        current.x -= distanceX;
        current.y -= distanceY;
    }

    private void boxScrollHandler(float distanceX, float distanceY, Box item) {
        PointF origin = item.getOrigin();
        PointF current = item.getCurrent();
        origin.x -= distanceX;
        origin.y -= distanceY;
        current.x -= distanceX;
        current.y -= distanceY;
    }

    private void figureScrollHandler(float distanceX, float distanceY, Figure item) {
        SparseArray<PointF> points = item.getPoints();
        for (int i = 0; i < points.size(); i++) {
            PointF value = points.valueAt(i);
            value.x -= distanceX;
            value.y -= distanceY;
        }
    }

    public void clear() {
        allDrawItems.clear();
        bgPaint.setColor(Color.WHITE);
        invalidate();
    }

    public void changeBack() {
        int itemsSize = allDrawItems.size();
        if (itemsSize > 0) {
            CustomDrawable item = allDrawItems.get(allDrawItems.size() - 1);
            allDrawItems.remove(item);
            invalidate();
        }
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

    public void setScrollGesture(boolean scrollGesture) {
        this.scrollGesture = scrollGesture;
    }

    public boolean isScrollGesture() {
        return scrollGesture;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }
}
