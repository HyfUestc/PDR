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
	
	//��ȡ��Ļ�����ص��ܶ�
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	public BackgroundView(Context context) {
		super(context);
	}
	
	
	//drawDpLine()�����ɼ�������ɵ�Point[]���飬Ȼ������
	private void drawDpLine(Canvas canvas,Point[] points,Paint paint){
		//�����������ֻ��������������绪Ϊ��mate2Ϊ2,LG������nexus5Ϊ3�����Ժ�����С��λ�Ŀ��ܻ�������
//		int den = (int) density;
		int den = 3;
		//�����imageview��ԭ����ƽ��һ��(50,30)
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

		// ����ͼ��ı���ɫ
		canvas.drawColor(Color.TRANSPARENT);
		// ��ӻ���
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true); // �����
		paint_Line.setStrokeWidth(2); // ���û��ʿ��
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setColor(Color.BLACK); // ���ʵ���ɫ

		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(4); // ���û��ʿ��
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // ���ʵ���ɫ
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		drawDpLine(canvas, line_point, paint_Line);
	}
	
}
