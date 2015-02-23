package com.zk.rippleeffecttheory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SimpleRippleView extends View {
	
	/**
	 * 圆从最小到最大，需要多长时间
	 */
	private int DURATION = 2000;
	/**
	 * 想要画的圆的最大半径
	 */
	private float radiusMax = 0;
	/**
	 * View的最大宽度
	 */
	private int WIDTH;
	/**
	 * View的最大高度
	 */
    private int HEIGHT;
    /**
     * 记录手指点下去的x坐标
     */
	private float x;
	/**
	 * 记录手指点下去的y坐标
	 */
	private float y;
	/**
	 * 对timer的放大系数
	 */
	private int FRAME_RATE = 10;
	/**
	 * 通过timer不停的timer++，达到让圆不断增大的效果
	 */
	private int timer = 0;
	/**
	 * Handler用来更新界面用的
	 */
	private Handler canvasHandler;
	/**
	 * 是否正在播放动画
	 */
	private boolean animationRunning = false;
	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 结合Handler用来刷新当前界面的任务
	 */
	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};
	
	public SimpleRippleView(Context context) {
		super(context);
		init(context);
	}

	public SimpleRippleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SimpleRippleView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		animateRipple(event);
		return super.onTouchEvent(event);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		Log.d("K", "onSizeChanged,w:" + w + ", h: " + h);
		
		WIDTH = w;
		HEIGHT = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(animationRunning){
			if(DURATION <= timer * FRAME_RATE){
				animationRunning = false;
                timer = 0;
                canvas.restore();
                Log.d("K", "animation end.");
                return;
			}else{
				//每隔FRAME_RATE刷新一下
				canvasHandler.postDelayed(runnable, FRAME_RATE);
			}
			
			if(timer == 0){
				canvas.save();
			}
			
			float currentRadius = radiusMax * (((float) timer * FRAME_RATE) / DURATION);
			Log.d("K", "current radius:" + currentRadius);
			canvas.drawCircle(x, y, (currentRadius), paint);
			timer++;
		}
	}
	
	/**
	 * 初始化画笔，Handler
	 * @param context
	 */
	private void init(Context context){
		paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvasHandler = new Handler();
	}
	
	/**
	 * 开始画圆
	 * @param event
	 */
	public void animateRipple(MotionEvent event){
		//当前没有播放动画的时候，执行这些赋值操作
		if(!animationRunning){
			radiusMax = Math.max(WIDTH, HEIGHT);
			Log.d("K","radius:" + radiusMax);
			this.x = event.getX();
            this.y = event.getY();
            animationRunning = true;
            invalidate();
		}
	}
}
