package com.androidjian.wipedelete;

public class SwipeManager {

	private static SwipeManager manager;
	
	private SwipeManager(){}
	
	public static SwipeManager getInstance(){
		
		if(manager==null){
			synchronized (SwipeManager.class) {
				if(manager==null){
					manager=new SwipeManager();
				}
			}
		}
		return manager;
	}
	
	private SwipeLayout currentLayout;    //用来记录当前打开的swipeLayout
	
	public void setSwipeLayout(SwipeLayout layout){
		this.currentLayout=layout;
	}
	
	/**
	 * 清空当前所记录的已经打开的layout
	 */
	public void clearCurrentLayout(){
		currentLayout=null;
	}
	
	/**
	 * 关闭当前已经打开的SwipeLayout
	 */
	public void closeCurrentLayout(){
		if(currentLayout!=null){
			currentLayout.closeDelete();
		}
	}
	
	/**
	 * 判断当前是否应该能够滑动，如果没有打开的，则可以滑动。
	 * 如果有打开的，则判断打开的layout和当前按下的layout是否是同一个
	 * @param layout
	 * @return
	 */
	public boolean isShouldSwipe(SwipeLayout layout){
		
		if(currentLayout==null){
			return true;
		}else{
			return currentLayout==layout;
		}
	}
}
