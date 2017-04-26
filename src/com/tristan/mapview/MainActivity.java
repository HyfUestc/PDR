package com.tristan.mapview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.ivan.pdr.PDRService;
import com.tristan.fivemapdemo.R;
import com.tristan.sqlhelper.DatabaseUtil;
import com.tristan.sqlhelper.PointsData;
import com.xiaoxuan.map.Barrier;
import com.xiaoxuan.map.drawAstarPath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//测试一下github是否关联成功
public class MainActivity extends Activity {
	
	private ToggleButton btn1;					//测试按钮
	private ToggleButton btn2;					//显示蒙版的状态切换按钮
	private ToggleButton btn3;					//用于代码中画图的按钮
	private Button btn4;						//初始化按钮
	private Button btn5;						//用于测试A星算法的按钮
	private drawAstarPath pathView;				//用于画出astar算法给出的路径
	private TextView screenInfo;				//屏幕相关信息展示
	private MapView draw_point;					//用来画测试图的view
	private BoardView draw_board;				//用来画蒙板的view
	private BackgroundView draw_map;			//用来画地图的view
	private ImageView map_bg;					//地图
	private RadioGroup mapset;					//切换地图
	
	private TextView showStepNumber;			//显示步数。
	
    public static int FLAG = 1;
    public static int FLAG1 = 2;
    
    private static final String TAG = "MainActivity";
    
    public static Handler handler2;
    
	//用来处理子线程送过来的消息
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			Point this_point = (Point) msg.obj;
			draw_point = new MapView(MainActivity.this,"test2",this_point);
			FrameLayout fl = (FrameLayout) findViewById(R.id.outside);
			fl.addView(draw_point);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		// 获取帧布局对象，并设置其背景图
		final FrameLayout fl = (FrameLayout) findViewById(R.id.outside);
		
	    //将assets中的外部db文件拷贝到data/data/databases中
		DatabaseUtil.packDataBase(this);
		PointsData locPoint = new PointsData(this);
		final List<Point> points = locPoint.getPointList();
		//draw_point=new MapView(MainActivity.this,"test1",points);
		
		
		Thread getPoints = new Thread(new Runnable(){
			public void run(){
				Socket socket;
				try {
					socket = new Socket("192.168.1.100",8888);
					BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					pw.println("login");
					pw.println("林峰");
					pw.println("666666");
					pw.flush();
					String line = null;
					while((line = bw.readLine())!=null){
						if("success".equals(line)){
							while((line = bw.readLine())!=null){
								String[] str = line.split(" ");
								int x = (int)Float.parseFloat(str[0]);
								int y = (int)Float.parseFloat(str[1]);
								System.out.println("X:"+str[0]+",Y:"+str[1]);
								Point point_get =new Point(x,y); 
								points.add(point_get);
								Message msg = new Message();
								msg.obj = point_get;
//								try {
//									Thread.sleep(500);
//								} catch (InterruptedException e) {
//								}
								handler.sendMessage(msg);
							}
						}
						else if("failed".equals(line)){
						}
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		getPoints.start();


	
		
		
	   //获取屏幕的分辨率
	   DisplayMetrics metric = new DisplayMetrics();
       getWindowManager().getDefaultDisplay().getMetrics(metric);
       
       //华为mate2宽为720px，高度为1208px
       //nexus5宽为1080px,高度为1776px
       int width = metric.widthPixels;  // 屏幕宽度（像素）
       int height = metric.heightPixels;  // 屏幕高度（像素）
       //华为的mate2测试密度为2.0
       //nexus5测试密度为3.0
       float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5/ 2.0）
       int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
       screenInfo = (TextView) findViewById(R.id.screenInfo);
       screenInfo.setText("宽度："+width+"  高度："+height+"  密度："+density+"  DPI："+densityDpi);
		
       showStepNumber = (TextView) findViewById(R.id.step_show);

       
       
		btn1 = (ToggleButton) findViewById(R.id.btn_test);
		btn2 = (ToggleButton) findViewById(R.id.btn_board);
		btn3 = (ToggleButton) findViewById(R.id.btn_map);
		btn4 = (Button) findViewById(R.id.btn_reset);
		btn5 = (Button) findViewById(R.id.btn_astar);
		mapset = (RadioGroup) findViewById(R.id.mapchoice);
		map_bg = (ImageView) findViewById(R.id.map);

		
		//btn1的按键监听
		btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		    		new Thread(new Runnable() {
						public void run() {
							for (Point point : points) {
								Message msg =  new Message();
								msg.obj = point;
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								handler.sendMessage(msg);
//								draw_point=new MapView(MainActivity.this,"test2",point);
//					            fl.addView(draw_point);
							}
						}
					}).start();
		    		
		    	
		        } else {
		            fl.removeView(draw_point);
		        }
		    }
		});
		
		//btn2的按键状态监听，打开则显示蒙版
		btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		    		draw_board=new  BoardView(MainActivity.this);
		            fl.addView(draw_board);
		        } else {
		            fl.removeView(draw_board);
		        }
		    }
		});
		

		//btn3的按键监听,打开则显示已画出来的地图
		btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		    		//开启PedometerService
		    		Intent startService = new Intent(MainActivity.this, PDRService.class);
		    		startService(startService);		    		
		    		//draw_map = new BackgroundView(MainActivity.this);
		    		
                    handler2 = new Handler() {   //使用Handler机制接收service发送过来的步数变化
                        //对步数进行更新。
                    	@Override
                    	public void handleMessage(Message msg) {
                    		super.handleMessage(msg);
                    		if (msg.what==FLAG) {
                    			Log.d(TAG,msg.obj + "" );
                    			//SNTextView.setText(msg.obj + "");
                    			Point this_point = (Point) msg.obj;
                    			draw_point = new MapView(MainActivity.this,"test2",this_point);
                    			FrameLayout fl = (FrameLayout) findViewById(R.id.outside);
                    			fl.addView(draw_point);
                    		} else if (msg.what == FLAG1) {
                    			showStepNumber.setText(msg.obj + "");
                    		}
                    		
                    	}
                    };		    		
		    		//fl.addView(draw_map);
		        } else {
		        	//停止PedometerService
		        	Intent stopService = new Intent(MainActivity.this, PDRService.class);
		        	stopService(stopService);		        	
		        	fl.removeView(draw_map);
		        }
		    }
		});
		
		
		//btn4的按键监听
		btn4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//重启主活动
				Intent intent=getIntent();
				finish();
				startActivity(intent);
				
				//现在直接用动态去除view的方法，而不是重启主活动了
//				ll.removeView(draw_board);
//				ll.removeView(draw_point);
			}
		});
		
		btn5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Barrier test = new Barrier();
				ArrayList<Point> testPoints = new ArrayList<Point>();
				//添加从点(187,280)到(187,344)的障碍物
				for (int i = 280/10; i < 345/10; i++) {
					testPoints.add(new Point(187/10,i));
				}
				test.addBarrierPoint(testPoints);
				Point src = new Point(187/10,280/10);
				Point dst = new Point(187/10,344/10);
				pathView = new drawAstarPath(MainActivity.this, test, src, dst);
				fl.addView(pathView);
			}
		});
		
		mapset.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.btn_5f:
					map_bg.setBackgroundResource(R.drawable.control_new_5f);
					break;
				case R.id.btn_9_526:
					map_bg.setBackgroundResource(R.drawable.map_9_526);
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//程序结束时，关闭PDR服务。
    	Intent stopService = new Intent(MainActivity.this, PDRService.class);
    	stopService(stopService);	
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
	//实现从dip单位到px单位的转换
	public  int dip2px(Context context, float dipValue){ 
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int)(dipValue * scale + 0.5f); 
		} 
    
	
	//封装，从dip->px
	public  int transf(float dipValue){
		return dip2px(MainActivity.this,dipValue);
	}
}



