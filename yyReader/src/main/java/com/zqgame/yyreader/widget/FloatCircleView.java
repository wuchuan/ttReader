package com.zqgame.yyreader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zqgame.yyreader.GloableParams;

public class FloatCircleView extends ImageView {

	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;

	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	private WindowManager.LayoutParams wmParams = GloableParams.getMywmParams();
	private float mStartX;
	private float mStartY;
	private OnClickListener mClickListener;

	public FloatCircleView(Context context) {
		super(context);
	}

	public FloatCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas c) {
//		Drawable d = getDrawable();
//		Bitmap source = null;
//
//		if (d == null) {
//			return;
//		} else {
//			source = ((BitmapDrawable) d).getBitmap();
//		}
//		if (source == null) {
//			// super.onDraw(c);
//		} else {
//			Bitmap temp = Bitmap.createBitmap(getWidth(), getWidth(),
//					Config.ARGB_4444);
//
//			Paint p = new Paint();
//			p.setAntiAlias(true);
//			p.setFilterBitmap(true);
//			//p.setColor(Color.BLACK); 
//			//p.setStyle(Paint.Style.FILL);
//		//	paint's style is Stroke or StrokeAndFill.
//			//p.setStrokeWidth(2);
//
//
//			Canvas canvas = new Canvas(temp);
//
//			canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, p);
//
//			p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//			float scale_w = (float) getWidth() / source.getWidth();
//			float scale_h = (float) getHeight() / source.getHeight();
//			Matrix m = new Matrix();
//			m.postScale(scale_w, scale_h);
//			canvas.setMatrix(m);
//			canvas.drawBitmap(source, 0, 0, p);
//
//			c.drawBitmap(temp, 0, 0, null);
//		}
		super.onDraw(c);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x = event.getRawX();
		y = event.getRawY() - 25;
		// Log.i("currP", "currX"+x+"====currY"+y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			mStartX = x;
			mStartY = y;

			// Log.i("startP", "startX"+mTouchStartX+"====startY"+mTouchStartY);

			break;
		case MotionEvent.ACTION_MOVE:
			//updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:
			//updateViewPosition();
			mTouchStartX = mTouchStartY = 0;
			if ((x - mStartX) < 2 && (y - mStartY) < 2) {
				if (mClickListener != null) {
					mClickListener.onClick(this);
				}
				break;
			}
		}
		return true;

		// return super.onTouchEvent(event);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}

	private void updateViewPosition() {
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(this, wmParams);

	}
	

}
