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
	private View moreView; //���ظ���ҳ��
	
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
        
        prepareData(); //׼������
        count = listData.size();
     
        adapter = new SimpleAdapter(this, listData,R.layout.item, 
        		new String[]{"itemText"}, new int[]{R.id.itemText});
        
        listView.addFooterView(moreView); //���ӵײ�view��һ��Ҫ��setAdapter֮ǰ���ӣ�����ᱨ����
        
        listView.setAdapter(adapter); //����adapter
        listView.setOnScrollListener(this); //����listview�Ĺ����¼�
    }

    private void prepareData(){  //׼������
    	for(int i=0;i<10;i++){
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("itemText", "��������"+i);
    		listData.add(map);
    	}
    	
    }
    
    private void loadMoreData(){ //���ظ�������
    	 count = adapter.getCount(); 
    	for(int i=count;i<count+5;i++){
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("itemText", "��������"+i);
    		listData.add(map);
    	}
    	count = listData.size();
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		Log.i(TAG, "firstVisibleItem="+firstVisibleItem+"\nvisibleItemCount="+
				visibleItemCount+"\ntotalItemCount"+totalItemCount);
		
		lastItem = firstVisibleItem + visibleItemCount - 1;  //��1����Ϊ������˸�addFooterView
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) { 
		Log.i(TAG, "scrollState="+scrollState);
		//�����������ǣ������һ��item�����������ݵ�����ʱ�����и���
		if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){ 
			Log.i(TAG, "������ײ�");
			moreView.setVisibility(view.VISIBLE);
		 
		    mHandler.sendEmptyMessage(0);
			 
		}
		
	}
	//����Handler
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
			    loadMoreData();  //���ظ������ݣ��������ʹ���첽����
			    adapter.notifyDataSetChanged();
			    moreView.setVisibility(View.GONE); 
			    
			    if(count > 30){
			    	Toast.makeText(MainActivity.this, "ľ�и������ݣ�", 3000).show();
			          listView.removeFooterView(moreView); //�Ƴ��ײ���ͼ
			    }
				Log.i(TAG, "���ظ�������");
				break;
            case 1:
				
				break;
			default:
				break;
			}
		};
	};
    
}