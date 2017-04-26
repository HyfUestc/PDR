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
	
	//��ȡ��Ļ�����ص��ܶ�
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	public drawAstarPath(Context context) {
		super(context);
	}
	
	
	//���ﴫ�������Ҳ�ǳ���5֮���
	public drawAstarPath(Context context,Barrier barrier,Point src,Point dst) {
		super(context);
		this.barrierPoints = barrier.getBarrierPoint();
		this.src = src;
		this.dst = dst;
		//�����������src��dst�����Ƿ����㷨Ҫ���
		Pointmap test=new Pointmap(barrier,src,dst);
		this.path = test.getPath();
	}
	
	private void drawDpPoint(Canvas canvas,Point point,Paint paint){
		int u = 3;
		//�����imageview��ԭ����ƽ��һ��(50,30)
		x=(point.x+50)*u;
		y=(point.y+30)*u;
		canvas.drawPoint(x*10,y*10,paint);
	}
	
	private void drawDpPoint(Canvas canvas,Point[] points,Paint paint){
		int u = 3;
		//�����imageview��ԭ����ƽ��һ��(50,30)
		for(Point p:points){
			x=(p.x+50)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x*10,y*10,paint);
		}
	}
	
	private void drawDpPoint(Canvas canvas,ArrayList<Point> points,Paint paint){
		int u = 3;
		//�����imageview��ԭ����ƽ��һ��(50,30)
		for(Point p:points){
			x=(p.x+50)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x*10,y*10,paint);
		}
	}
	
	
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// ����ͼ��ı���ɫ
		canvas.drawColor(Color.TRANSPARENT);
		// ��ӻ���
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(8); // ���û��ʿ��
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // ���ʵ���ɫ
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		Paint paint_special = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(15); // ���û��ʿ��
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.BLUE); // ���ʵ���ɫ
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		//�ýϴֵĵ㻭�������յ�
		drawDpPoint(canvas, src, paint_special);
		drawDpPoint(canvas, dst, paint_special);
		//�ý�ϸ�ĵ㻭��astar�㷨�����Ĺ켣��
		drawDpPoint(canvas, path, paint_Point);
		
	}
	
}
