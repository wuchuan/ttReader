package com.zqgame.yyreader.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class CircleImageView extends ImageView {

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas c) {
		Drawable d = getDrawable();
		Bitmap source = null;
		if(d == null){
			d=getBackground();
			if(d == null){
				return;
			}
		}else{
			source = ((BitmapDrawable) d).getBitmap();
		}
		if (source == null) {
//			super.onDraw(c);
		} else {
			Bitmap temp = Bitmap.createBitmap(getWidth(), getWidth(),
					Config.ARGB_4444);
			
			Paint p = new Paint();
			p.setAntiAlias(true);
			p.setFilterBitmap(true);
			
			Canvas canvas = new Canvas(temp);

			canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, p);

			p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

			// 按比例缩放
			float scale_w = (float) getWidth() / source.getWidth();
			float scale_h = (float) getHeight() / source.getHeight();
			Matrix m = new Matrix();
			m.postScale(scale_w, scale_h);
			canvas.setMatrix(m);
			canvas.drawBitmap(source, 0, 0, p);

			c.drawBitmap(temp, 0, 0, null);
		}

	}


}
