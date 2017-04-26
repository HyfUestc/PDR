package com.tristan.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.view.View;

public class BackgroundView extends View{
	private int x,y;
	
	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	public BackgroundView(Context context) {
		super(context);
	}
	
	
	//drawDpLine()接收由几个点组成的Point[]数组，然后连线
	private void drawDpLine(Canvas canvas,Point[] points,Paint paint){
		//现在碰到的手机都还是整数，如华为的mate2为2,LG代工的nexus5为3，但以后碰到小数位的可能会有隐患
//		int den = (int) density;
		int den = 3;
		//相对于imageview的原点再平移一个(50,30)
		int len = points.length;
		for(int count=0; count < (len-1) ;count++){
			x=(points[count].x+50)*den;
			y=(points[count].y+30)*den;
			int x_next = (points[count+1].x+50)*den;
			int y_next = (points[count+1].y+30)*den;
			canvas.drawLine(x, y, x_next, y_next, paint);
		}
	}
	
	
	
	Point[] line_point = new Point[]{
			new Point(0,0),
			new Point(200,0),
			new Point(50,50),
	};
	
	
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
		paint_Line.setColor(Color.BLACK); // 画笔的颜色

		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(4); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		drawDpLine(canvas, line_point, paint_Line);
	}
	
}
