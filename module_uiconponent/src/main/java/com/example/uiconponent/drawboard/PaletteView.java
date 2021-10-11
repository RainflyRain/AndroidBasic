package com.example.uiconponent.drawboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import com.example.uiconponent.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfl
 * 2021-01-15
 **/
public class PaletteView extends View {

    private static final String TAG = "PaletteView";

    private Paint mPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;
    private static final int MAX_CACHE_STEP = 20;
    private List<DrawingInfo> mDrawingList;
    private List<DrawingInfo> mRemovedList;
    private Xfermode mXferModeClear;
    private Xfermode mXferModeDraw;
    private int mDrawSize;
    private int mEraserSize;
    private int mPenAlpha = 255;
    private boolean mCanEraser;
    private Callback mCallback;

    private Bitmap jgBitmap;
    private Paint mJgPaint;

    private boolean isMultipleTouch = false;

    public enum Mode {
        DRAW,
        ERASER
    }

    private Mode mMode = Mode.DRAW;

    public PaletteView(Context context) {
        super(context);
        init();
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface Callback {
        void onUndoRedoStatusChanged();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void init() {
        setDrawingCacheEnabled(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawSize = PaletteDesityUtils.dip2px(getContext(), 3);
        mEraserSize = PaletteDesityUtils.dip2px(getContext(), 30);
        mPaint.setStrokeWidth(mDrawSize);
        mPaint.setColor(0XFF000000);
        mXferModeDraw = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mXferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(mXferModeDraw);

        jgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_jg_point);
    }

    private void initBuffer() {
        try {
            int width = getWidth();
            int height = getHeight();
            mBufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mBufferCanvas = new Canvas(mBufferBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private abstract static class DrawingInfo {
        Paint paint;

        abstract void draw(Canvas canvas);
    }

    private static class PathDrawingInfo extends DrawingInfo {

        Path path;

        @Override
        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            if (mMode == Mode.DRAW) {
                mPaint.setXfermode(mXferModeDraw);
                mPaint.setStrokeWidth(mDrawSize);
            } else {
                mPaint.setXfermode(mXferModeClear);
                mPaint.setStrokeWidth(mEraserSize);
            }
        }
    }

    public void setEraserSize(int size) {
        mEraserSize = size;
    }

    public void setPenRawSize(int size) {
        mDrawSize = size;
        if (mMode == Mode.DRAW) {
            mPaint.setStrokeWidth(mDrawSize);
        }
    }

    public void setPenColor(int color) {
        mPaint.setColor(color);
    }

    private void reDraw() {
        if (mDrawingList != null && mBufferBitmap != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (DrawingInfo drawingInfo : mDrawingList) {
                drawingInfo.draw(mBufferCanvas);
            }
            invalidate();
        }
    }

    public int getPenColor() {
        return mPaint.getColor();
    }

    public int getPenSize() {
        return mDrawSize;
    }

    public int getEraserSize() {
        return mEraserSize;
    }

    public void setPenAlpha(int alpha) {
        mPenAlpha = alpha;
        if (mMode == Mode.DRAW) {
            mPaint.setAlpha(alpha);
        }
    }

    public int getPenAlpha() {
        return mPenAlpha;
    }

    public boolean canRedo() {
        return mRemovedList != null && !mRemovedList.isEmpty();
    }

    public boolean canUndo() {
        return mDrawingList != null && !mDrawingList.isEmpty();
    }

    public void redo() {
        int size = mRemovedList == null ? 0 : mRemovedList.size();
        if (size > 0) {
            DrawingInfo info = mRemovedList.remove(size - 1);
            mDrawingList.add(info);
            mCanEraser = true;
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void undo() {
        int size = mDrawingList == null ? 0 : mDrawingList.size();
        if (size > 0) {
            DrawingInfo info = mDrawingList.remove(size - 1);
            if (mRemovedList == null) {
                mRemovedList = new ArrayList<>(MAX_CACHE_STEP);
            }
            if (size == 1) {
                mCanEraser = false;
            }
            mRemovedList.add(info);
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void clear() {
        if (mBufferBitmap != null) {
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            mCanEraser = false;
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
        mJgPaint = null;
        invalidate();
    }

    public Bitmap buildBitmap() {
        Bitmap bm = getDrawingCache();
        Bitmap result = Bitmap.createBitmap(bm);
        destroyDrawingCache();
        return result;
    }

    private void saveDrawingPath() {
        if (mDrawingList == null) {
            mDrawingList = new ArrayList<>(MAX_CACHE_STEP);
        } else if (mDrawingList.size() == MAX_CACHE_STEP) {
            mDrawingList.remove(0);
        }
        Path cachePath = new Path(mPath);
        Paint cachePaint = new Paint(mPaint);
        PathDrawingInfo info = new PathDrawingInfo();
        info.path = cachePath;
        info.paint = cachePaint;
        if (isNotPoint(info.path)) {
            mDrawingList.add(info);
        }
        mCanEraser = true;
        if (mCallback != null) {
            mCallback.onUndoRedoStatusChanged();
        }
    }

    // 判断是不是在画板画了一个点。
    private boolean isNotPoint(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, true);
        float pathLength = pathMeasure.getLength();
        BigDecimal data1 = BigDecimal.valueOf(pathLength);
        BigDecimal data2 = BigDecimal.valueOf(0.0f);
        // num =0 相等 >0前者大于后者 ，反之 <0 前者小于后者
        int num = data1.compareTo(data2);
        return num != 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pintStyle == JG) {
            // 绘制一个小圆（作为小球）
            // canvas.drawCircle(mX, mY, 15, mJgPaint);
            if (mJgPaint != null) {
                canvas.drawBitmap(jgBitmap, mLastX - jgBitmap.getWidth() / 2, mLastY - jgBitmap.getHeight() / 2, mJgPaint);// canvas画布绘制图片
            }
        } else {
            if (mBufferBitmap != null) {
                canvas.drawBitmap(mBufferBitmap, 0, 0, null);
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        // if (drawChangeListener != null) {
        // drawChangeListener.eventCall(event);
        // }
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
                isMultipleTouch = true;
                if (pintStyle == JG) {
                    mJgPaint = null;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerId(event.getActionIndex()) == 0 && !isMultipleTouch) {
                    // if (controllerView != null && !isControlEvent) {
                    // x = x * getWidth() / controllerView.getWidth();
                    // y = y * getHeight() / controllerView.getHeight();
                    // }
                    mLastX = x;
                    mLastY = y;
                    if (pintStyle == JG && mJgPaint == null) {
                        mJgPaint = new Paint();
                    } else {
                        if (mPath == null) {
                            mPath = new Path();
                        }
                        mPath.moveTo(x, y);
                    }
                } else {
                    if (pintStyle == JG) {
                        mJgPaint = null;
                        invalidate();
                    }
                }
            break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerId(event.getActionIndex()) == 0 && !isMultipleTouch) {
                    // if (controllerView != null && !isControlEvent) {
                    // x = x * getWidth() / controllerView.getWidth();
                    // y = y * getHeight() / controllerView.getHeight();
                    // }
                    // 这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                    if (pintStyle == HB) {
                        if (mMode == Mode.ERASER && !mCanEraser) {
                            break;
                        }
                        mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                        if (mBufferBitmap == null) {
                            initBuffer();
                        }
                        if (mBufferCanvas != null) {
                            if (isNotPoint(mPath)) {
                                mBufferCanvas.drawPath(mPath, mPaint);
                            }
                        }
                    }
                    mLastX = x;
                    mLastY = y;
                    invalidate();
                } else {
                    if (pintStyle == JG && mJgPaint != null) {
                        mJgPaint = null;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerId(event.getActionIndex()) == 0 && !isMultipleTouch) {
                    // if (controllerView != null && !isControlEvent) {
                    // x = x * getWidth() / controllerView.getWidth();
                    // y = y * getHeight() / controllerView.getHeight();
                    // }
                    if (pintStyle == HB) {
                        if (mMode == Mode.DRAW || mCanEraser) {
                            saveDrawingPath();
                        }
                        if (mPath != null) {
                            mPath.reset();
                        }
                    } else {
                        mJgPaint = null;
                        invalidate();
                    }
                }
                if (pintStyle == JG) {
                    mJgPaint = null;
                    invalidate();
                }
                isMultipleTouch = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
                if (pintStyle == JG) {
                    mJgPaint = null;
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setDrawChangeListener(DrawChangeListener drawChangeListener) {
        this.drawChangeListener = drawChangeListener;
    }

    public DrawChangeListener drawChangeListener;

    public static final int JG = 1;

    public static final int HB = 2;

    public int pintStyle = HB;

    public void setPintStyle(int style) {
        pintStyle = style;
    }

    public void setControlEvent(boolean isControlEvent) {
        // 是否反控事件
    }

    public interface DrawChangeListener {

        void eventCall(MotionEvent event);
    }

    public void setEventView(PaletteView view) {
        view.setDrawChangeListener(new DrawChangeListener() {
            @Override
            public void eventCall(MotionEvent event) {
                onTouchEvent(event);
            }
        });
    }
}
