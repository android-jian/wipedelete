package com.androidjian.wipedelete;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout {

	private View contentView;   
	private View deleteView;
	private int contentWidth;
	private int contentHeight;
	private int deleteWidth;
	private int deleteHeight;
	private ViewDragHelper dragHelper;      //view��ק������

	public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwipeLayout(Context context) {
		super(context);
		init();
	}

    public void init(){
		dragHelper = ViewDragHelper.create(this, callback);
	}
    
    @Override
    protected void onFinishInflate() {
    	super.onFinishInflate();
    	
    	contentView = getChildAt(0);
    	deleteView = getChildAt(1);
    }
    
    /**
	 * �÷�����onMeasureִ����֮��ִ�У���ô�����ڸ÷����г�ʼ���Լ�����View�Ŀ��
	 */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	contentWidth = contentView.getMeasuredWidth();
    	contentHeight = contentView.getMeasuredHeight();
    	
    	deleteWidth = deleteView.getMeasuredWidth();
    	deleteHeight = deleteView.getMeasuredHeight();
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
    		int bottom) {
    	contentView.layout(0, 0, contentWidth, contentHeight);
    	deleteView.layout(contentView.getRight(), 0, contentView.getRight()+deleteWidth, deleteHeight);
    	//super.onLayout(changed, left, top, right, bottom);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	
    	boolean result=dragHelper.shouldInterceptTouchEvent(ev);
    	//�����ǰ�д򿪵ģ�����Ҫֱ�����أ�����onTouch����
    	if(!SwipeManager.getInstance().isShouldSwipe(this)){
    		//�ȹر��Ѿ��򿪵�layout
    		SwipeManager.getInstance().closeCurrentLayout();      
    		
    		result= true;
    	}
    	return result;
    }
    
    private float downX;
	private float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//�����ǰ�д򿪵ģ���������߼�����ִ��
    	if(!SwipeManager.getInstance().isShouldSwipe(this)){
    		requestDisallowInterceptTouchEvent(true);
    		return true;
    	}
    	
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			
			break;
        case MotionEvent.ACTION_MOVE:
			float moveX=event.getX();
			float moveY=event.getY();
			
			//��ȡx�����y�����ƶ��ľ���
			float scaleX=moveX-downX;
			float scaleY=moveY-downY;
			if(Math.abs(scaleX)>Math.abs(scaleY)){
				//��ʾ�ƶ���ƫ����ˮƽ������ôӦ��SwipeLayoutӦ�ô�������listview��Ҫ����
				requestDisallowInterceptTouchEvent(true);
			}
			
			downX=moveX;
			downY=moveY;
			break;
        case MotionEvent.ACTION_UP:
	        break;
		}
    	dragHelper.processTouchEvent(event);     //�����¼�����
    	return true;
    }
    
    enum SwipeState{
    	open,close;
    }
    public SwipeState curState=SwipeState.close;      //Ĭ��״̬Ϊ�ر�״̬
    
    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
		
		@Override
		public boolean tryCaptureView(View child, int pointId) {
			return child==contentView || child==deleteView;
		}

		public int getViewHorizontalDragRange(View child) {
			return deleteWidth;
		};
		
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			
			if(child==contentView){
				if(left<-deleteWidth) left=-deleteWidth;
				if(left>0) left=0;
			}else if(child==deleteView){
				if(left<getMeasuredWidth()-deleteWidth) left=getMeasuredWidth()-deleteWidth;
				if(left>getMeasuredWidth()) left=getMeasuredWidth();
			}
			return left;
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if(changedView==contentView){
				//�ֶ��ƶ�deleteView
				deleteView.layout(deleteView.getLeft()+dx, deleteView.getTop(), deleteView.getRight()+dx, deleteView.getBottom());
			}else if(changedView==deleteView){
				//�ֶ��ƶ�contentView
				contentView.layout(contentView.getLeft()+dx, contentView.getTop(), contentView.getRight()+dx, contentView.getBottom());
			}
			
			//���µ�ǰ״̬    �жϿ��͹رյ��߼�
			if(contentView.getLeft()==-deleteWidth && curState!=SwipeState.open){
				curState=SwipeState.open;
				//��ǰ��Swipelayout�Ѿ��򿪣���Ҫ��Manager��¼һ����
				SwipeManager.getInstance().setSwipeLayout(SwipeLayout.this);
				
			}else if(contentView.getLeft()==0 && curState!=SwipeState.close){
				curState=SwipeState.close;
				//˵����ǰ��SwipeLayout�Ѿ��رգ���Ҫ��Manager���һ��
				SwipeManager.getInstance().clearCurrentLayout();
			}
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			
			if(contentView.getLeft()<-deleteWidth/2){
				//��deleteView
				openDelete();
			}else{
				//�ر�deleteView
				closeDelete();
			}
		}
		
	};

	/**
	 * ��deleteView
	 */
	public void openDelete() {
		dragHelper.smoothSlideViewTo(deleteView, getMeasuredWidth()-deleteWidth, deleteView.getTop());
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}

	/**
	 * deleteView�ر�
	 */
	public void closeDelete() {
		dragHelper.smoothSlideViewTo(deleteView, getMeasuredWidth(), deleteView.getTop());
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}
	
	@Override
	public void computeScroll() {
		if(dragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
		}
	}
	
}
