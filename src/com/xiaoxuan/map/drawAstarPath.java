package com.xiaoxuan.map;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.view.View;

public class drawAstarPath extends View{
	private int x,y;
	private Point src;
	private Point dst;
	private ArrayList<Point> barrierPoints;
	private ArrayList<Point> path;
	
	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	public drawAstarPath(Context context) {
		super(context);
	}
	
	
	//这里传入的数据也是除以5之后的
	public drawAstarPath(Context context,Barrier barrier,Point src,Point dst) {
		super(context);
		this.barrierPoints = barrier.getBarrierPoint();
		this.src = src;
		this.dst = dst;
		//这里面输入的src和dst必须是符合算法要求的
		Pointmap test=new Pointmap(barrier,src,dst);
		this.path = test.getPath();
	}
	
	private void drawDpPoint(Canvas canvas,Point point,Paint paint){
		int u = 3;
		//相对于imageview的原点再平移一个(50,30)
		x=(point.x+50)*u;
		y=(point.y+30)*u;
		canvas.drawPoint(x*10,y*10,paint);
	}
	
	private void drawDpPoint(Canvas canvas,Point[] points,Paint paint){
		int u = 3;
		//相对于imageview的原点再平移一个(50,30)
		for(Point p:points){
			x=(p.x+50)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x*10,y*10,paint);
		}
	}
	
	private void drawDpPoint(Canvas canvas,ArrayList<Point> points,Paint paint){
		int u = 3;
		//相对于imageview的原点再平移一个(50,30)
		for(Point p:points){
			x=(p.x+50)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x*10,y*10,paint);
		}
	}
	
	
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(8); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		Paint paint_special = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(15); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.BLUE); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		//用较粗的点画出起点和终点
		drawDpPoint(canvas, src, paint_special);
		drawDpPoint(canvas, dst, paint_special);
		//用较细的点画出astar算法给出的轨迹点
		drawDpPoint(canvas, path, paint_Point);
		
	}
	
}
