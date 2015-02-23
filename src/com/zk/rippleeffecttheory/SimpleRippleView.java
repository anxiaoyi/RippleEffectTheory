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
	 * Բ����С�������Ҫ�೤ʱ��
	 */
	private int DURATION = 2000;
	/**
	 * ��Ҫ����Բ�����뾶
	 */
	private float radiusMax = 0;
	/**
	 * View�������
	 */
	private int WIDTH;
	/**
	 * View�����߶�
	 */
    private int HEIGHT;
    /**
     * ��¼��ָ����ȥ��x����
     */
	private float x;
	/**
	 * ��¼��ָ����ȥ��y����
	 */
	private float y;
	/**
	 * ��timer�ķŴ�ϵ��
	 */
	private int FRAME_RATE = 10;
	/**
	 * ͨ��timer��ͣ��timer++���ﵽ��Բ���������Ч��
	 */
	private int timer = 0;
	/**
	 * Handler�������½����õ�
	 */
	private Handler canvasHandler;
	/**
	 * �Ƿ����ڲ��Ŷ���
	 */
	private boolean animationRunning = false;
	/**
	 * ����
	 */
	private Paint paint;
	/**
	 * ���Handler����ˢ�µ�ǰ���������
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
				//ÿ��FRAME_RATEˢ��һ��
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
	 * ��ʼ�����ʣ�Handler
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
	 * ��ʼ��Բ
	 * @param event
	 */
	public void animateRipple(MotionEvent event){
		//��ǰû�в��Ŷ�����ʱ��ִ����Щ��ֵ����
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
