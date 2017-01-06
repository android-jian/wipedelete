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
	
	private SwipeLayout currentLayout;    //������¼��ǰ�򿪵�swipeLayout
	
	public void setSwipeLayout(SwipeLayout layout){
		this.currentLayout=layout;
	}
	
	/**
	 * ��յ�ǰ����¼���Ѿ��򿪵�layout
	 */
	public void clearCurrentLayout(){
		currentLayout=null;
	}
	
	/**
	 * �رյ�ǰ�Ѿ��򿪵�SwipeLayout
	 */
	public void closeCurrentLayout(){
		if(currentLayout!=null){
			currentLayout.closeDelete();
		}
	}
	
	/**
	 * �жϵ�ǰ�Ƿ�Ӧ���ܹ����������û�д򿪵ģ�����Ի�����
	 * ����д򿪵ģ����жϴ򿪵�layout�͵�ǰ���µ�layout�Ƿ���ͬһ��
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
