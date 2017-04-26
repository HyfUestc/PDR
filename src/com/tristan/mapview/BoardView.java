package com.tristan.mapview;

import android.util.DisplayMetrics;
import android.view.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;

public class BoardView extends View {
	//矩形左上角的x,y坐标
	private float left_up_x=0,left_up_y=0;
	//矩形右下角的x,y坐标
	private float right_bottom_x=0,right_bottom_y=0;
	
	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	
	public BoardView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//根据屏幕的DPI重新封装drawPoint()方法
	private void drawDpRect(Canvas canvas,
			float DpValue_left_up_x,float DpValue_left_up_y,
			float DpValue_right_bottom_x,float DpValue_right_bottom_y,
			Paint paint){
		//现在碰到的手机都还是整数，如华为的mate2为2,LG代工的nexus5为3，但以后碰到小数位的可能会有隐患
//		int u = (int) density;
		int u=3;
		//相对于imageview的原点再平移一个(20,30)
		left_up_x=(DpValue_left_up_x+20)*u;
		left_up_y=(DpValue_left_up_y+30)*u;
		right_bottom_x=(DpValue_right_bottom_x+20)*u;
		right_bottom_y=(DpValue_right_bottom_y+30)*u;
		canvas.drawRect(new RectF(left_up_x,left_up_y,right_bottom_x,right_bottom_y), paint);
	}
	

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
		
		//设置画蒙版的画笔
		Paint paint_Board = new Paint();
		paint_Board.setAntiAlias(true);
		paint_Board.setStyle(Style.FILL);
		paint_Board.setAlpha(3);
		paint_Board.setColor(Color.GREEN);
			
		//画出可行的区域
		//后面可以用path类来改写
		//513和517外面的走廊
		drawDpRect(canvas, 103, 319, 149, 344, paint_Board);
		drawDpRect(canvas, 149, 319, 186, 385, paint_Board);
		drawDpRect(canvas, 186, 368, 223, 385, paint_Board);
		//504到511外面的走廊
		drawDpRect(canvas, 169, 166, 186, 319, paint_Board);
		//505外面的空地
		drawDpRect(canvas, 142, 121, 186, 166, paint_Board);
		//501外面的部分
		drawDpRect(canvas, 142, 72, 165, 121, paint_Board);
		//咖啡台
		drawDpRect(canvas, 122, 72, 142, 104, paint_Board);
		//东边的楼梯
		drawDpRect(canvas, 142, 4, 165, 72, paint_Board);
	}
	
}
