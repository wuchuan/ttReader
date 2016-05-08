//TO DO:
//
// - improve timer performance (especially on Eee Pad)
// - improve child rearranging

package com.zqgame.yyreader.widget;

import java.util.Collections;
import java.util.ArrayList;

import com.zqgame.yyreader.util.LogUtil;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class DraggableGridView extends ViewGroup implements View.OnTouchListener, View.OnClickListener, View.OnLongClickListener {
	//layout vars
	public static float bookpwRatio = .625f;//childRatio=书之间间距/书的宽度
	public static float bookhwRatio = 1.5f;//childRatio=书的高度/书的宽度
	
	
    protected int colCount=3, childWidthSize,childHeightSize, wPadding,hPadding, dpi, scroll = 0;
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    //dragging vars
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true, touching = false;
    //anim vars
    public static int animT = 150;
    protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
    //listeners
    protected OnRearrangeListener onRearrangeListener;
    protected OnClickListener secondaryOnClickListener;
    private OnItemClickListener onItemClickListener;
    private Bitmap backGroundDraw=null;
    
    //CONSTRUCTOR AND HELPERS
    public DraggableGridView (Context context, AttributeSet attrs) {
        super(context, attrs);
        setListeners();
        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		dpi = metrics.densityDpi;
    }
    protected void setListeners()
    {
    	setOnTouchListener(this);
    	super.setOnClickListener(this);
        setOnLongClickListener(this);
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
    	secondaryOnClickListener = l;
    }
    protected Runnable updateTask = new Runnable() {
        public void run()
        {
            if (dragged != -1)
            {
            	if (lastY < hPadding * 3 && scroll > 0)
            		scroll -= 20;
            	else if (lastY > getBottom() - getTop() - (hPadding * 3) && scroll < getMaxScroll())
            		scroll += 20;
            }
            else if (lastDelta != 0 && !touching)
            {
            	scroll += lastDelta;
            	lastDelta *= .9;
            	if (Math.abs(lastDelta) < .25)
            		lastDelta = 0;
            }
            clampScroll();
            onLayout(true, getLeft(), getTop(), getRight(), getBottom());
        
            handler.postDelayed(this, 25);
        }
    };
    
    //OVERRIDES
    @Override
    public void addView(View child) {
    	super.addView(child);
    	newPositions.add(-1);
    };
    @Override
    public void removeViewAt(int index) {
    	super.removeViewAt(index);
    	newPositions.remove(index);
    };
    
    //LAYOUT
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	
    	//compute width of view, in dp 分配空间的宽度
//        float w = (r - l) / (dpi / 160f);
        
        //determine number of columns, at least 2 至少两列
//        colCount = 2;
//        int sub = 240;
//        w -= 280;
//        while (w > 0)
//        {
//        	colCount++;
//        	w -= sub;
//        	sub += 40;
//        }
        
        //determine childSize and padding, in px
        childWidthSize = (r - l) / colCount;
        childWidthSize = Math.round(childWidthSize * bookpwRatio);
        childHeightSize=Math.round(childWidthSize*bookhwRatio);
        wPadding = ((r - l) - (childWidthSize * colCount)) / (colCount + 1);//padding：每个子控件之间的间距
    	hPadding=Math.round(wPadding*bookhwRatio);
        for (int i = 0; i < getChildCount(); i++)
        	if (i != dragged)
        	{
	            Point xy = getCoorFromIndex(i);
	            getChildAt(i).layout(xy.x, xy.y, xy.x + childWidthSize, xy.y + childHeightSize);
        	}
    }
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
    	if (dragged == -1)
    		return i;
    	else if (i == childCount - 1)
    		return dragged;
    	else if (i >= dragged)
    		return i + 1;
    	return i;
    }
    /**根据xy得到从1开始的位置*/
    public int getIndexFromCoor(int x, int y)
    {
        int col = getColFromCoor(x), row = getRowFromCoor(y + scroll); 
        if (col == -1 || row == -1) //touch is between columns or rows
            return -1;
        int index = row * colCount + col;
        if (index >= getChildCount())
            return -1;
        return index;
    }
    /**根据点击的x位置得到在那一列*/
    protected int getColFromCoor(int x)
    {
        x -= wPadding;
        for (int i = 0; x > 0; i++)
        {
            if (x < childWidthSize)
                return i;
            x -= (childWidthSize + wPadding);
        }
        return -1;
    }
    /**根据点击的Y位置（包含滑动的距离）得到在那一行*/
    protected int getRowFromCoor(int coor)
    {
        coor -= hPadding;
        for (int i = 0; coor > 0; i++)
        {
            if (coor < childHeightSize)
                return i;
            coor -= (childWidthSize + hPadding);
        }
        return -1;
    }
    /**根据坐标得到位置 -1:位置不在子控件区域*/
    protected int getTargetFromCoor(int x, int y)
    {
        if (getRowFromCoor(y + scroll) == -1) //touch is between rows
            return -1;
        //if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
            //return -1;
        
        int leftPos = getIndexFromCoor(x - (childWidthSize / 4), y);
        int rightPos = getIndexFromCoor(x + (childWidthSize / 4), y);
        if (leftPos == -1 && rightPos == -1) //touch is in the middle of nowhere
            return -1;
        if (leftPos == rightPos) //touch is in the middle of a visual
        	return -1;
        
        int target = -1;
        if (rightPos > -1)
            target = rightPos;
        else if (leftPos > -1)
            target = leftPos + 1;
        if (dragged < target)
            return target - 1;
        
        //Toast.makeText(getContext(), "Target: " + target + ".", Toast.LENGTH_SHORT).show();
        return target;
    }
    protected Point getCoorFromIndex(int index)
    {
        int col = index % colCount;
        int row = index / colCount;
        return new Point(wPadding + (childWidthSize + wPadding) * col,
                         hPadding + (childHeightSize + hPadding) * row - scroll);
    }
    public int getIndexOf(View child)
    {
    	for (int i = 0; i < getChildCount(); i++)
    		if (getChildAt(i) == child)
    			return i;
    	return -1;
    }
    
    //EVENT HANDLERS
    public void onClick(View view) {
    	if (enabled)
    	{
    		if (secondaryOnClickListener != null)
    			secondaryOnClickListener.onClick(view);
    		if (onItemClickListener != null && getLastIndex() != -1)
    			onItemClickListener.onItemClick(null, getChildAt(getLastIndex()), getLastIndex(), getLastIndex() / colCount);
    	}
    }
    public boolean onLongClick(View view)
    {
    	if (!enabled)
    		return false;
        int index = getLastIndex();
        if (index != -1)
        {
            dragged = index;
            animateDragged();
            return true;
        }
        return false;
    }
    public boolean onTouch(View view, MotionEvent event)
    {
        int action = event.getAction();
           switch (action & MotionEvent.ACTION_MASK) {
               case MotionEvent.ACTION_DOWN:
            	   enabled = true;
                   lastX = (int) event.getX();
                   lastY = (int) event.getY();
                   touching = true;
                   break;
               case MotionEvent.ACTION_MOVE:
            	   int delta = lastY - (int)event.getY();
                   if (dragged != -1)
                   {
                       //change draw location of dragged visual
                       int x = (int)event.getX(), y = (int)event.getY();
                       int l = x - (3 * childWidthSize / 4), t = y - (3 * childHeightSize / 4);
                       getChildAt(dragged).layout(l, t, l + (childWidthSize * 3 / 2), t + (childHeightSize * 3 / 2));
                       
                       //check for new target hover
                       int target = getTargetFromCoor(x, y);
                       if (lastTarget != target)
                       {
                           if (target != -1)
                           {
                               animateGap(target);
                               lastTarget = target;
                           }
                       }
                   }
                   else
                   {
                	   scroll += delta;
                	   clampScroll();
                	   if (Math.abs(delta) > 2)
                    	   enabled = false;
                	   onLayout(true, getLeft(), getTop(), getRight(), getBottom());
                   }
                   lastX = (int) event.getX();
                   lastY = (int) event.getY();
                   lastDelta = delta;
                   break;
               case MotionEvent.ACTION_UP:
                   if (dragged != -1)
                   {
                	   View v = getChildAt(dragged);
                       if (lastTarget != -1)
                           reorderChildren();
                       else
                       {
                           Point xy = getCoorFromIndex(dragged);
                           v.layout(xy.x, xy.y, xy.x + childWidthSize, xy.y + childHeightSize);
                       }
                       v.clearAnimation();
                       if (v instanceof ImageView)
                    	   ((ImageView)v).setAlpha(255);
                       lastTarget = -1;
                       dragged = -1;
                   }
                   touching = false;
                   break;
           }
        if (dragged != -1)
        	return true;
        return false;
    }
    
    //EVENT HELPERS
    protected void animateDragged()
    {
    	View v = getChildAt(dragged);
    	int x = getCoorFromIndex(dragged).x + childWidthSize / 2, y = getCoorFromIndex(dragged).y + childHeightSize / 2;
        int l = x - (3 * childWidthSize / 4), t = y - (3 * childHeightSize / 4);
    	v.layout(l, t, l + (childWidthSize * 3 / 2), t + ( childHeightSize* 3 / 2));
    	AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1, childWidthSize * 3 / 4, childHeightSize * 3 / 4);
		scale.setDuration(animT);
		AlphaAnimation alpha = new AlphaAnimation(1, .5f);
		alpha.setDuration(animT);

		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		
		v.clearAnimation();
		v.startAnimation(animSet);
    }
    
    
    @Override
	protected void onDraw(Canvas canvas) {
    	  int count = getChildCount();
    	  if(count!=0&&backGroundDraw!=null){
        	  int row = (count-1) / 3;
//        	  int h= padding + (childSize + padding) * (row+1);
//        	    int top = count>0 ? getChildAt(0).getTop() : 0; 
        	    
        	   
//        	    int backgroundWidth = background.getWidth();  
//        	    int backgroundHeight = background.getHeight();
        	    
//        	    int width = getWidth();  
//        	    int height = getHeight(); 
//        	    BitmapFactory.decodeResource(getResources(), backGroundDrawId);
        	  if(backGroundDraw.getWidth()!=getWidth()&&getWidth()>0&&(childHeightSize + hPadding)>0){
        	    	backGroundDraw = Bitmap.createScaledBitmap(backGroundDraw, (int)getWidth(), (int)childHeightSize + hPadding, true);
        	    	}
        	    for(int i=0;!(i>row);i++){
        	    	canvas.drawBitmap(backGroundDraw, 0, hPadding + (childHeightSize + hPadding) * (i)-scroll, null);  
        	    }

        	   
//        	    for (int y = top; y<height; y += backgroundHeight){  
//        	        for (int x = 0; x<width; x += backgroundWidth){  
//        	            canvas.drawBitmap(background, x, y, null);  
//        	        }  
//        	    }  
    		  
    	  }

		super.onDraw(canvas);
	}
    public void setBackGroundDraw(int id){
    	backGroundDraw=BitmapFactory.decodeResource(getResources(), id);
    	}
	protected void animateGap(int target)
    {
    	for (int i = 0; i < getChildCount(); i++)
    	{
    		View v = getChildAt(i);
    		if (i == dragged)
	    		continue;
    		int newPos = i;
    		if (dragged < target && i >= dragged + 1 && i <= target)
    			newPos--;
    		else if (target < dragged && i >= target && i < dragged)
    			newPos++;
    		
    		//animate
    		int oldPos = i;
    		if (newPositions.get(i) != -1)
    			oldPos = newPositions.get(i);
    		if (oldPos == newPos)
    			continue;
    		
    		Point oldXY = getCoorFromIndex(oldPos);
    		Point newXY = getCoorFromIndex(newPos);
    		Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
    		Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());
    		
    		TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x,
																  Animation.ABSOLUTE, newOffset.x,
																  Animation.ABSOLUTE, oldOffset.y,
																  Animation.ABSOLUTE, newOffset.y);
			translate.setDuration(animT);
			translate.setFillEnabled(true);
			translate.setFillAfter(true);
			v.clearAnimation();
			v.startAnimation(translate);
    		
			newPositions.set(i, newPos);
    	}
    }
    protected void reorderChildren()
    {
        //FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND RECONSTRUCTING THE LIST!!!
    	if (onRearrangeListener != null)
    		onRearrangeListener.onRearrange(dragged, lastTarget);
        ArrayList<View> children = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++)
        {
        	getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }
        removeAllViews();
        while (dragged != lastTarget)
            if (lastTarget == children.size()) // dragged and dropped to the right of the last element
            {
                children.add(children.remove(dragged));
                dragged = lastTarget;
            }
            else if (dragged < lastTarget) // shift to the right
            {
                Collections.swap(children, dragged, dragged + 1);
                dragged++;
            }
            else if (dragged > lastTarget) // shift to the left
            {
                Collections.swap(children, dragged, dragged - 1);
                dragged--;
            }
        for (int i = 0; i < children.size(); i++)
        {
        	newPositions.set(i, -1);
            addView(children.get(i));
        }
        onLayout(true, getLeft(), getTop(), getRight(), getBottom());
    }
    public void scrollToTop()
    {
    	scroll = 0;
    }
    public void scrollToBottom()
    {
    	scroll = Integer.MAX_VALUE;
    	clampScroll();
    }
    protected void clampScroll()
    {
    	int stretch = 3, overreach = getHeight() / 2;
    	int max = getMaxScroll();
    	max = Math.max(max, 0);
    	
    	if (scroll < -overreach)
    	{
    		scroll = -overreach;
    		lastDelta = 0;
    	}
    	else if (scroll > max + overreach)
    	{
    		scroll = max + overreach;
    		lastDelta = 0;
    	}
    	else if (scroll < 0)
    	{
	    	if (scroll >= -stretch)
	    		scroll = 0;
	    	else if (!touching)
	    		scroll -= scroll / stretch;
    	}
    	else if (scroll > max)
    	{
    		if (scroll <= max + stretch)
    			scroll = max;
    		else if (!touching)
    			scroll += (max - scroll) / stretch;
    	}
    }
    protected int getMaxScroll()
    {
    	int rowCount = (int)Math.ceil((double)getChildCount()/colCount), max = rowCount * childHeightSize + (rowCount + 1) * hPadding - getHeight();
    	return max;
    }
    public int getLastIndex()
    {
    	return getIndexFromCoor(lastX, lastY);
    }
    
    //OTHER METHODS
    public void setOnRearrangeListener(OnRearrangeListener l)
    {
    	this.onRearrangeListener = l;
    }
    public void setOnItemClickListener(OnItemClickListener l)
    {
    	this.onItemClickListener = l;
    }
    
    
    
}