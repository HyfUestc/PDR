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
	//���ڲ���
	private int test_flag; 

	//�洢���ط������ĵ�
	ArrayList<Point> testPoints;
	//��Ŵӷ������õ��ĵ�
	Point testPoint;
	
	//��ȡ��Ļ�����ص��ܶ�
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	
	public MapView(Context context) {
		super(context);
	}


	//test_flag =0 , ����ӱ��ط��������ֵ㼣���͵��������������һ��ȫ����ʾ���е㼣
	public MapView(Context context,String str,List<Point> points){
		super(context);
		if (str == "test1"){
			test_flag = 0;
		}
		
		testPoints = (ArrayList<Point>) points;
	}
	
	//test_flag =1 , ����ӷ������������ݣ��͵��������������ÿ�ν���һ����ͻ�һ����
	public MapView(Context context,String str,Point point){
		super(context);
		if (str == "test2"){
			test_flag = 1;
		}
		
		testPoint = point;
	}
	
	

	//������Ļ��DPI���·�װdrawPoint()������
	//����drawPoint()���������õ����굥λ��px����Ҫת��Ϊdip/dp
	//����Ҫ�������ĵ���ʱ��������points�Ȼ�����������
	private void drawDpPoint(Canvas canvas,List<Point> points,Paint paint){
		//�����������ֻ��������������绪Ϊ��mate2Ϊ2,LG������nexus5Ϊ3�����Ժ�����С��λ�Ŀ��ܻ�������
		int u = 3;
		//�����imageview��ԭ����ƽ��һ��(20,30)
		for(Point p:points){
			x=(p.x+20)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x,y,paint);
		}
	}
	
	private void drawDpPoint(Canvas canvas,Point point,Paint paint){
		//�����������ֻ��������������绪Ϊ��mate2Ϊ2,LG������nexus5Ϊ3�����Ժ�����С��λ�Ŀ��ܻ�������
		int u = 3;
		//�����imageview��ԭ����ƽ��һ��(50,30)
			x=(point.x+20)*u;
			y=(point.y+30)*u;
			canvas.drawPoint(x,y,paint);
	}

	
	/*
	 * ��������չʾ�ĵ�
	 * ��������onDraw()�еģ�������ʾ˵��ò�Ҫ�������棬��Ϊnew�����Ƚ�ռʱ�䣬
	 * stackOverflow���лش�˵��÷���class������߹������У�
	 * Better way could be to declare these object at class level 
	 * and initialize them in constructor, 
	 * and just make drawXxx() calls in onDraw method.
	 * */
	/*Point[] points = new Point[]{
		//���(212,297),��������(x��С��186��y��С��274)
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
		//511����
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
		//ͨ������һֱ�ߵ�504�ſڣ�
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
		//Ȼ��501�ſڣ��ߵ�б�ߣ�
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
		//���ﱱ�ߵ�¥�ݣ�
		new Point(154, 65 ),
	};*/
	
	
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
		paint_Line.setColor(Color.BLUE); // ���ʵ���ɫ

		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(8); // ���û��ʿ��
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // ���ʵ���ɫ
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		
			
		
		switch (test_flag) {
		case 0:          //�ӱ������ݿ��õ��ĵ�
			drawDpPoint(canvas, testPoints, paint_Point);
			break;
		
		case 1:          //�ӷ���������õ���
			drawDpPoint(canvas, testPoint, paint_Point);
			break;
		default:
			break;
		}
	}
}