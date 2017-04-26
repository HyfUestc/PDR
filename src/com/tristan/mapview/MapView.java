package com.tristan.mapview;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;





public class MapView extends View {
	private float x=0, y=0;
	//用于测试
	private int test_flag; 

	//存储本地服务器的点
	ArrayList<Point> testPoints;
	//存放从服务器拿到的点
	Point testPoint;
	
	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	
	public MapView(Context context) {
		super(context);
	}


	//test_flag =0 , 如果从本地服务器复现点迹，就调用这个构造器，一次全部显示所有点迹
	public MapView(Context context,String str,List<Point> points){
		super(context);
		if (str == "test1"){
			test_flag = 0;
		}
		
		testPoints = (ArrayList<Point>) points;
	}
	
	//test_flag =1 , 如果从服务器接受数据，就调用这个构造器，每次接受一个点就画一个点
	public MapView(Context context,String str,Point point){
		super(context);
		if (str == "test2"){
			test_flag = 1;
		}
		
		testPoint = point;
	}
	
	

	//根据屏幕的DPI重新封装drawPoint()方法，
	//这里drawPoint()方法里所用的坐标单位是px，需要转化为dip/dp
	//把需要画出来的点暂时放在数组points里，然后遍历画出来
	private void drawDpPoint(Canvas canvas,List<Point> points,Paint paint){
		//现在碰到的手机都还是整数，如华为的mate2为2,LG代工的nexus5为3，但以后碰到小数位的可能会有隐患
		int u = 3;
		//相对于imageview的原点再平移一个(20,30)
		for(Point p:points){
			x=(p.x+20)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x,y,paint);
		}
	}
	
	private void drawDpPoint(Canvas canvas,Point point,Paint paint){
		//现在碰到的手机都还是整数，如华为的mate2为2,LG代工的nexus5为3，但以后碰到小数位的可能会有隐患
		int u = 3;
		//相对于imageview的原点再平移一个(50,30)
			x=(point.x+20)*u;
			y=(point.y+30)*u;
			canvas.drawPoint(x,y,paint);
	}

	
	/*
	 * 所有用于展示的点
	 * 本来放在onDraw()中的，但是提示说最好不要放在里面，因为new操作比较占时间，
	 * stackOverflow上有回答说最好放在class层面或者构造器中：
	 * Better way could be to declare these object at class level 
	 * and initialize them in constructor, 
	 * and just make drawXxx() calls in onDraw method.
	 * */
	/*Point[] points = new Point[]{
		//起点(212,297),向左向上(x减小到186，y减小到274)
		new Point(212, 297),
		new Point(209, 296),
		new Point(207, 295),
		new Point(207, 294),
		new Point(206, 290),
		new Point(205, 285),
		new Point(203, 283),
		new Point(200, 282),
		new Point(197, 280),
		new Point(195, 278),
		new Point(192, 281),
		new Point(190, 280),
		new Point(188, 277),
		//511的门
		new Point(186, 274),
		new Point(185, 272),
		new Point(182, 270),
		new Point(182, 268),
		new Point(181, 265),
		new Point(181, 260),
		new Point(180, 255),
		new Point(180, 250),
		new Point(180, 245),
		new Point(179, 240),
		new Point(178, 237),
		new Point(178, 234),
		new Point(179, 230),
		new Point(178, 225),
		new Point(178, 220),
		new Point(177, 215),
		new Point(176, 209),
		new Point(177, 205),
		new Point(177, 200),
		new Point(176, 196),
		new Point(176, 190),
		new Point(176, 185),
		new Point(175, 180),
		new Point(175, 170),
		new Point(174, 166),
		//通过走廊一直走到504门口：
		new Point(174, 165),
		new Point(170, 164),
		new Point(167, 162),
		new Point(164, 161),
		new Point(160, 160),
		new Point(157, 155),
		new Point(157, 150),
		new Point(156, 146),
		new Point(155, 140),
		new Point(155, 135),
		new Point(156, 131),
		new Point(154, 130),
		new Point(154, 126),
		new Point(153, 124),
		//然后到501门口，走的斜线：
		new Point(152, 123),
		new Point(152, 121),
		new Point(152, 117),
		new Point(152, 112),
		new Point(152, 108),
		new Point(152, 104),
		new Point(152, 101),
		new Point(152,  98),
		new Point(153,  95),
		new Point(153,  91),
		new Point(153,  88),
		new Point(153,  84),
		new Point(153,  79),
		new Point(153,  73),
		new Point(154,  70),
		new Point(154,  67),
		//到达北边的楼梯：
		new Point(154, 65 ),
	};*/
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true); // 抗锯齿
		paint_Line.setStrokeWidth(2); // 设置画笔宽度
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setColor(Color.BLUE); // 画笔的颜色

		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(8); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		
			
		
		switch (test_flag) {
		case 0:          //从本地数据库拿到的点
			drawDpPoint(canvas, testPoints, paint_Point);
			break;
		
		case 1:          //从服务器请求得到点
			drawDpPoint(canvas, testPoint, paint_Point);
			break;
		default:
			break;
		}
	}
}