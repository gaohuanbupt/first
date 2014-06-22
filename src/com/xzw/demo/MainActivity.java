package com.xzw.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements OnScrollListener {
	
	private static final String TAG = "MainActivity";
	
	private ListView listView;
	private View moreView; //加载更多页面
	
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, String>> listData;
	
	private int lastItem;
    private int count;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = (ListView)findViewById(R.id.listView);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        listData = new ArrayList<HashMap<String,String>>();
        
        prepareData(); //准备数据
        count = listData.size();
     
        adapter = new SimpleAdapter(this, listData,R.layout.item, 
        		new String[]{"itemText"}, new int[]{R.id.itemText});
        
        listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。
        
        listView.setAdapter(adapter); //设置adapter
        listView.setOnScrollListener(this); //设置listview的滚动事件
    }

    private void prepareData(){  //准备数据
    	for(int i=0;i<10;i++){
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("itemText", "测试数据"+i);
    		listData.add(map);
    	}
    	
    }
    
    private void loadMoreData(){ //加载更多数据
    	 count = adapter.getCount(); 
    	for(int i=count;i<count+5;i++){
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("itemText", "测试数据"+i);
    		listData.add(map);
    	}
    	count = listData.size();
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		Log.i(TAG, "firstVisibleItem="+firstVisibleItem+"\nvisibleItemCount="+
				visibleItemCount+"\ntotalItemCount"+totalItemCount);
		
		lastItem = firstVisibleItem + visibleItemCount - 1;  //减1是因为上面加了个addFooterView
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) { 
		Log.i(TAG, "scrollState="+scrollState);
		//下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
		if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){ 
			Log.i(TAG, "拉到最底部");
			moreView.setVisibility(view.VISIBLE);
		 
		    mHandler.sendEmptyMessage(0);
			 
		}
		
	}
	//声明Handler
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
			     
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    loadMoreData();  //加载更多数据，这里可以使用异步加载
			    adapter.notifyDataSetChanged();
			    moreView.setVisibility(View.GONE); 
			    
			    if(count > 30){
			    	Toast.makeText(MainActivity.this, "木有更多数据！", 3000).show();
			          listView.removeFooterView(moreView); //移除底部视图
			    }
				Log.i(TAG, "加载更多数据");
				break;
            case 1:
				
				break;
			default:
				break;
			}
		};
	};
    
}
