package com.machao.lockview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by cma2 on 16/9/4.
 */
public class LockView extends View {
    private static final int DEFAULT_PAINTINNERPOINT_COLOR = Color.parseColor("#EB5E64");
    private static final int DEFAULT_PAINTOUTERCIRCLE_COLOR = Color.parseColor("#EB5E64");
    private static final int DEFAULT_PAINTINNERPOINTWITHMASKFILTER_COLOR = Color.parseColor("#EB5E64");
    private static final int DEFAULT_PAINTOUTERCIRCLEWITHMASKFILTER_COLOR = Color.parseColor("#EB5E64");
    private static final int DEFAULT_PAINTLINE_COLOR = Color.parseColor("#ED9498");


    private Paint paintInnerPoint;
    private Paint paintOuterCircle;
    private Paint paintInnerPointWithMaskFilter;
    private Paint paintOuterCircleWithMaskFilter;
    private Paint paintLine;


    /**
     * current touch point
     */
    private PointF currentPoint;

    private LockUnit selectedLockUnit;
    private ArrayList<LockUnit> lockUnitArrayList;
    private LinkedHashSet<Integer> selectedLockUnitList;

    /**
     * view width
     */
    private int width;

    /**
     * view height
     */
    private int heigth;

    /**
     * default row and column count
     */
    private int spanCount = 4;

    /**
     * default count to complete
     */
    private int minPointCount = 4;

    /**
     * unit width
     */
    private int widthSegment;

    /**
     * unit height
     */
    private int heigthSegment;

    /**
     * unit shorter Segment
     */
    private int shorterSegment;

    /**
     * inner circle
     */
    private float innerCircleRadius;

    /**
     * trigger circle
     */
    private float innerTriggerCircleRadius;

    /**
     * is trigger circle show
     */
    private boolean isTriggerCircleShow;

    /**
     * outer circle
     */
    private float outerCircleRadius;

    private boolean isLayoutCompleted;

    private OnLockFinishListener onLockFinishListener;

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public LockView(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
		/*
		 * cancel LAYER_TYPE_HARDWARE
		 */
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        this.paintInnerPoint = new Paint();
        this.paintInnerPoint.setAntiAlias(true);
        this.paintInnerPoint.setColor(DEFAULT_PAINTINNERPOINT_COLOR);
        this.paintInnerPoint.setDither(true);
        this.paintInnerPoint.setStyle(Paint.Style.FILL);
        this.paintInnerPoint.setStrokeJoin(Paint.Join.ROUND);
        this.paintInnerPoint.setStrokeWidth(5);

        this.paintOuterCircle = new Paint();
        this.paintOuterCircle.setAntiAlias(true);
        this.paintOuterCircle.setColor(DEFAULT_PAINTOUTERCIRCLE_COLOR);
        this.paintOuterCircle.setDither(true);
        this.paintOuterCircle.setStyle(Paint.Style.STROKE);
        this.paintOuterCircle.setStrokeJoin(Paint.Join.ROUND);
        this.paintOuterCircle.setStrokeWidth(2);

        this.paintInnerPointWithMaskFilter = new Paint();
        this.paintInnerPointWithMaskFilter.setAntiAlias(true);
        this.paintInnerPointWithMaskFilter.setColor(DEFAULT_PAINTINNERPOINTWITHMASKFILTER_COLOR);
        this.paintInnerPointWithMaskFilter.setDither(true);
        this.paintInnerPointWithMaskFilter.setStyle(Paint.Style.FILL);
        this.paintInnerPointWithMaskFilter.setStrokeJoin(Paint.Join.ROUND);
        this.paintInnerPointWithMaskFilter.setStrokeWidth(5);
        this.paintInnerPointWithMaskFilter.setMaskFilter(new BlurMaskFilter(5,BlurMaskFilter.Blur.OUTER));

        this.paintOuterCircleWithMaskFilter = new Paint();
        this.paintOuterCircleWithMaskFilter.setAntiAlias(true);
        this.paintOuterCircleWithMaskFilter.setColor(DEFAULT_PAINTOUTERCIRCLEWITHMASKFILTER_COLOR);
        this.paintOuterCircleWithMaskFilter.setDither(true);
        this.paintOuterCircleWithMaskFilter.setStyle(Paint.Style.STROKE);
        this.paintOuterCircleWithMaskFilter.setStrokeJoin(Paint.Join.ROUND);
        this.paintOuterCircleWithMaskFilter.setStrokeWidth(2);
        this.paintOuterCircleWithMaskFilter.setMaskFilter(new BlurMaskFilter(5,BlurMaskFilter.Blur.OUTER));

        this.paintLine = new Paint();
        this.paintLine.setAntiAlias(true);
        this.paintLine.setColor(DEFAULT_PAINTLINE_COLOR);
        this.paintLine.setDither(true);
        this.paintLine.setStyle(Paint.Style.STROKE);
        this.paintLine.setStrokeJoin(Paint.Join.ROUND);
        this.paintLine.setStrokeWidth(5);

        // init point container
        this.lockUnitArrayList = new ArrayList<LockUnit>();
        this.selectedLockUnitList = new LinkedHashSet<Integer>();
    }

    private void init(Context context, AttributeSet attrs) {
        this.init(context);

        // init attr file
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LockView, 0, 0);
        this.setColorCircle(typeArray.getColor(R.styleable.LockView_colorCircle, DEFAULT_PAINTINNERPOINT_COLOR));
        this.setColorLine(typeArray.getColor(R.styleable.LockView_colorLine, DEFAULT_PAINTLINE_COLOR));
        this.setColorPoint(typeArray.getColor(R.styleable.LockView_colorPoint, DEFAULT_PAINTINNERPOINT_COLOR));
        this.setInnerTriggerCircleRadius(typeArray.getFloat(R.styleable.LockView_innerTriggerCircleRadius, 0.8F));
        this.setOuterCircleRadius(typeArray.getFloat(R.styleable.LockView_outerCircleRadius, 1F));
        this.setInnerCircleRadius(typeArray.getFloat(R.styleable.LockView_outerCircleRadius, 0.2F));
        this.setMinPointCount(typeArray.getInt(R.styleable.LockView_minPointCount, minPointCount));
        this.setSpanCount(typeArray.getInt(R.styleable.LockView_spanCount, spanCount));
        this.setTriggerCircleShow(typeArray.getBoolean(R.styleable.LockView_isTriggerCircleShow, false));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.heigth = MeasureSpec.getSize(heightMeasureSpec);

        this.widthSegment = width/spanCount;
        this.heigthSegment = heigth/spanCount;
        this.shorterSegment = widthSegment > heigthSegment ? heigthSegment : widthSegment;
        this.innerCircleRadius = shorterSegment/20;
        this.innerTriggerCircleRadius = shorterSegment/20*6;
        this.outerCircleRadius = shorterSegment/20*8;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(isLayoutCompleted)	return;
        for(int i=0; i<spanCount; i++) {
            for(int j=0; j<spanCount; j++) {
                LockUnit unit = new LockUnit();
                unit.centerPoint.x = widthSegment*j + widthSegment/2;
                unit.centerPoint.y = heigthSegment*i + heigthSegment/2;
                unit.id = i*spanCount + j;
                unit.row = i;
                unit.column = j;
                this.lockUnitArrayList.add(unit);
            }
        }
        this.isLayoutCompleted = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

		/*
		 * draw line
		 */
        if(selectedLockUnit != null) {
            if(currentPoint != null) {
                canvas.drawLine(selectedLockUnit.centerPoint.x, selectedLockUnit.centerPoint.y, currentPoint.x, currentPoint.y, paintLine);
            } else {
                canvas.drawLine(selectedLockUnit.centerPoint.x, selectedLockUnit.centerPoint.y, selectedLockUnit.centerPoint.x, selectedLockUnit.centerPoint.y, paintLine);
            }
        }

		/*
		 * draw circle
		 */
        for(LockUnit unit : lockUnitArrayList) {
            if(unit.isSelected) {
                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, innerCircleRadius, paintInnerPointWithMaskFilter);
                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, outerCircleRadius, paintOuterCircleWithMaskFilter);

                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, innerCircleRadius, paintInnerPoint);
                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, outerCircleRadius, paintOuterCircle);

                if (isTriggerCircleShow)
                    canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, innerTriggerCircleRadius, paintOuterCircle);
            } else {
                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, innerCircleRadius, paintInnerPoint);
                canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, outerCircleRadius, paintOuterCircle);

                if (isTriggerCircleShow)
                    canvas.drawCircle(unit.centerPoint.x, unit.centerPoint.y, innerTriggerCircleRadius, paintOuterCircle);
            }

            if(unit.linkLockUnit != null) {
                canvas.drawLine(unit.centerPoint.x, unit.centerPoint.y, unit.linkLockUnit.centerPoint.x, unit.linkLockUnit.centerPoint.y, paintLine);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            this.currentPoint = new PointF(event.getX(), event.getY());
            for(int i=0; i<spanCount; i++) {
                for(int j=0; j<spanCount; j++) {
                    LockUnit unit = lockUnitArrayList.get(spanCount*i + j);
                    RectF triggerRect = new RectF(unit.centerPoint.x - innerTriggerCircleRadius,
                            unit.centerPoint.y - innerTriggerCircleRadius,
                            unit.centerPoint.x + innerTriggerCircleRadius,
                            unit.centerPoint.y + innerTriggerCircleRadius);
                    if(triggerRect.contains(currentPoint.x, currentPoint.y)) {
                        if(!unit.isSelected) {
                            unit.isSelected = true;

                            if(selectedLockUnit == null) {
                                this.selectedLockUnit = unit;
                                this.selectedLockUnitList.add(unit.id);
                            } else {
                                if(unit.linkLockUnit == null) {
                                    if(selectedLockUnit.row == unit.row) {
                                        if(selectedLockUnit.column < unit.column) {
                                            for(int k=selectedLockUnit.column; k<unit.column; k++) {
                                                LockUnit centerUnit = lockUnitArrayList.get(spanCount*selectedLockUnit.row + k);
                                                if(centerUnit.linkLockUnit == null) {
                                                    centerUnit.isSelected = true;
                                                    this.selectedLockUnit.linkLockUnit = centerUnit;
                                                    this.selectedLockUnit = centerUnit;
                                                    this.selectedLockUnitList.add(centerUnit.id);
                                                }
                                            }
                                        } else if(selectedLockUnit.column > unit.column) {
                                            for(int k=selectedLockUnit.column; k>unit.column; k--) {
                                                LockUnit centerUnit = lockUnitArrayList.get(spanCount*selectedLockUnit.row + k);
                                                if(centerUnit.linkLockUnit == null) {
                                                    centerUnit.isSelected = true;
                                                    this.selectedLockUnit.linkLockUnit = centerUnit;
                                                    this.selectedLockUnit = centerUnit;
                                                    this.selectedLockUnitList.add(centerUnit.id);
                                                }
                                            }
                                        }
                                    } else if(selectedLockUnit.column == unit.column) {
                                        if(selectedLockUnit.row < unit.row) {
                                            for(int k=selectedLockUnit.row; k<unit.row; k++) {
                                                LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + selectedLockUnit.column);
                                                if(centerUnit.linkLockUnit == null) {
                                                    centerUnit.isSelected = true;
                                                    this.selectedLockUnit.linkLockUnit = centerUnit;
                                                    this.selectedLockUnit = centerUnit;
                                                    this.selectedLockUnitList.add(centerUnit.id);
                                                }
                                            }
                                        } else if(selectedLockUnit.row > unit.row) {
                                            for(int k=selectedLockUnit.row; k>unit.row; k--) {
                                                LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + selectedLockUnit.column);
                                                if(centerUnit.linkLockUnit == null) {
                                                    centerUnit.isSelected = true;
                                                    this.selectedLockUnit.linkLockUnit = centerUnit;
                                                    this.selectedLockUnit = centerUnit;
                                                    this.selectedLockUnitList.add(centerUnit.id);
                                                }
                                            }
                                        }
                                    } else if(Math.abs(selectedLockUnit.column-unit.column) == Math.abs(selectedLockUnit.row-unit.row)) {
                                        if(selectedLockUnit.column < unit.column) {//右边
                                            if(selectedLockUnit.row < unit.row) {//下
                                                for(int k=selectedLockUnit.row, l=selectedLockUnit.column; k<unit.row && l<unit.column; k++, l++) {
                                                    LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + l);
                                                    if(centerUnit.linkLockUnit == null) {
                                                        centerUnit.isSelected = true;
                                                        this.selectedLockUnit.linkLockUnit = centerUnit;
                                                        this.selectedLockUnit = centerUnit;
                                                        this.selectedLockUnitList.add(centerUnit.id);
                                                    }
                                                }
                                            } else if(selectedLockUnit.row > unit.row) {//上
                                                for(int k=selectedLockUnit.row, l=selectedLockUnit.column; k>unit.row && l<unit.column; k--, l++) {
                                                    LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + l);
                                                    if(centerUnit.linkLockUnit == null) {
                                                        centerUnit.isSelected = true;
                                                        this.selectedLockUnit.linkLockUnit = centerUnit;
                                                        this.selectedLockUnit = centerUnit;
                                                        this.selectedLockUnitList.add(centerUnit.id);
                                                    }
                                                }
                                            }
                                        } else if(selectedLockUnit.column > unit.column) {//左边
                                            if(selectedLockUnit.row < unit.row) {//下
                                                for(int k=selectedLockUnit.row, l=selectedLockUnit.column; k<unit.row && l>unit.column; k++, l--) {
                                                    LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + l);
                                                    if(centerUnit.linkLockUnit == null) {
                                                        centerUnit.isSelected = true;
                                                        this.selectedLockUnit.linkLockUnit = centerUnit;
                                                        this.selectedLockUnit = centerUnit;
                                                        this.selectedLockUnitList.add(centerUnit.id);
                                                    }
                                                }
                                            } else if(selectedLockUnit.row > unit.row) {//上
                                                for(int k=selectedLockUnit.row, l=selectedLockUnit.column; k>unit.row && l>unit.column; k--, l--) {
                                                    LockUnit centerUnit = lockUnitArrayList.get(spanCount*k + l);
                                                    if(centerUnit.linkLockUnit == null) {
                                                        centerUnit.isSelected = true;
                                                        this.selectedLockUnit.linkLockUnit = centerUnit;
                                                        this.selectedLockUnit = centerUnit;
                                                        this.selectedLockUnitList.add(centerUnit.id);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    this.selectedLockUnit.linkLockUnit = unit;
                                    this.selectedLockUnit = unit;
                                    this.selectedLockUnitList.add(unit.id);
                                }
                            }
                        }
                    }
                }
            }
        } else if(action == MotionEvent.ACTION_UP) {
            this.selectedLockUnit = null;
            for(LockUnit unit : lockUnitArrayList) {
                unit.linkLockUnit = null;
                unit.isSelected = false;
            }

            if(selectedLockUnitList.size() < minPointCount) {
                this.startPlayErrorAnimation();
            } else {
                if(onLockFinishListener != null) onLockFinishListener.onFinish(convertResult2ArrayList(selectedLockUnitList));
            }
            this.selectedLockUnitList.clear();
        }
        this.invalidate();
        return true;
    }

    /**
     * handle result
     * @param set
     * @return
     */
    private ArrayList<Integer> convertResult2ArrayList(LinkedHashSet<Integer> set) {
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        for(Integer id : set) {
            retVal.add(id);
        }
        return retVal;
    }

    /**
     * set circle color
     * @param paramInt default #EB5E64
     */
    public void setColorCircle(int paramInt) {
        this.paintOuterCircle.setColor(paramInt);
        this.paintOuterCircleWithMaskFilter.setColor(paramInt);
    }

    /**
     * set line color
     * @param paramInt default #EB5E64
     */
    public void setColorLine(int paramInt) {
        this.paintLine.setColor(paramInt);
    }

    /**
     * set point color
     * @param paramInt default #EB5E64
     */
    public void setColorPoint(int paramInt) {
        this.paintInnerPoint.setColor(paramInt);
        this.paintInnerPointWithMaskFilter.setColor(paramInt);
    }

    /**
     * set inner trigger circle radius [0, 1]
     * @param paramFloat default 0.6
     */
    public void setInnerTriggerCircleRadius(float paramFloat) {
        this.innerTriggerCircleRadius = (this.shorterSegment / 2 * paramFloat);
    }

    /**
     * set outer trigger circle radius [0, 1]
     * @param paramFloat default 0.8
     */
    public void setOuterCircleRadius(float paramFloat) {
        this.outerCircleRadius = (this.shorterSegment / 2 * paramFloat);
    }

    /**
     * set inner circle radius [0, 1]
     * @param paramFloat default 0.1
     */
    public void setInnerCircleRadius(float paramFloat) {
        this.innerCircleRadius = (this.shorterSegment / 2 * paramFloat);
    }

    /**
     * set min point count to finish
     * @param paramInt
     */
    public void setMinPointCount(int paramInt) {
        this.minPointCount = paramInt;
    }

    /**
     * @param paramInt row and column count
     */
    public void setSpanCount(int paramInt) {
        this.spanCount = paramInt;
    }

    /**
     * set trigger circle show
     * @param paramBoolean default false
     */
    public void setTriggerCircleShow(boolean paramBoolean) {
        this.isTriggerCircleShow = paramBoolean;
    }


    public void setOnLockFinishListener(OnLockFinishListener onLockFinishListener) {
        this.onLockFinishListener = onLockFinishListener;
    }

    public void startPlayErrorAnimation() {
        ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("translationX", 10, -20, 20, -20, 20, -10)).setDuration(500).start();
    }


    /**
     * data model
     * @author machao
     */
    private class LockUnit {
        /**
         * identifier
         */
        public int id;

        /**
         * row count
         */
        public int row;

        /**
         * column count
         */
        public int column;

        /**
         * center point
         */
        public PointF centerPoint = new PointF();

        /**
         * linked unit
         */
        public LockUnit linkLockUnit;

        /**
         * is selected
         */
        public boolean isSelected = false;
    }

    public interface OnLockFinishListener {
        public void onFinish(List<Integer> result);
    }
}