package com.androidjian.wipedelete;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

    private ListView lv_mylist;
    private ArrayList<String> mList;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_mylist = (ListView) findViewById(R.id.lv_mylist);
        
        //准备数据
        mList=new ArrayList<String>();
        for(int i=0;i<30;i++){
        	mList.add("这是第"+i+"条测试数据");
        }
        
        lv_mylist.setAdapter(new MyAdapter());
        lv_mylist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				SwipeManager.getInstance().closeCurrentLayout();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {				
			}
		});
    }

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				convertView=View.inflate(getApplicationContext(), R.layout.item_list, null);
			}
			ViewHolder holder=ViewHolder.getViewHolder(convertView);
			holder.tv_name.setText(mList.get(position));
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		
		TextView tv_name,tv_delete;
		
		public ViewHolder(View convertView){
			tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			tv_delete=(TextView) convertView.findViewById(R.id.tv_delete);
		}
		public static ViewHolder getViewHolder(View convertView){
			ViewHolder holder=(ViewHolder) convertView.getTag();
			if(holder==null){
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}
