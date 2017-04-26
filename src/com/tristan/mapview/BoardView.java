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
	//�������Ͻǵ�x,y����
	private float left_up_x=0,left_up_y=0;
	//�������½ǵ�x,y����
	private float right_bottom_x=0,right_bottom_y=0;
	
	//��ȡ��Ļ�����ص��ܶ�
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	
	public BoardView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//������Ļ��DPI���·�װdrawPoint()����
	private void drawDpRect(Canvas canvas,
			float DpValue_left_up_x,float DpValue_left_up_y,
			float DpValue_right_bottom_x,float DpValue_right_bottom_y,
			Paint paint){
		//�����������ֻ��������������绪Ϊ��mate2Ϊ2,LG������nexus5Ϊ3�����Ժ�����С��λ�Ŀ��ܻ�������
//		int u = (int) density;
		int u=3;
		//�����imageview��ԭ����ƽ��һ��(20,30)
		left_up_x=(DpValue_left_up_x+20)*u;
		left_up_y=(DpValue_left_up_y+30)*u;
		right_bottom_x=(DpValue_right_bottom_x+20)*u;
		right_bottom_y=(DpValue_right_bottom_y+30)*u;
		canvas.drawRect(new RectF(left_up_x,left_up_y,right_bottom_x,right_bottom_y), paint);
	}
	

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
		
		//���û��ɰ�Ļ���
		Paint paint_Board = new Paint();
		paint_Board.setAntiAlias(true);
		paint_Board.setStyle(Style.FILL);
		paint_Board.setAlpha(3);
		paint_Board.setColor(Color.GREEN);
			
		//�������е�����
		//���������path������д
		//513��517���������
		drawDpRect(canvas, 103, 319, 149, 344, paint_Board);
		drawDpRect(canvas, 149, 319, 186, 385, paint_Board);
		drawDpRect(canvas, 186, 368, 223, 385, paint_Board);
		//504��511���������
		drawDpRect(canvas, 169, 166, 186, 319, paint_Board);
		//505����Ŀյ�
		drawDpRect(canvas, 142, 121, 186, 166, paint_Board);
		//501����Ĳ���
		drawDpRect(canvas, 142, 72, 165, 121, paint_Board);
		//����̨
		drawDpRect(canvas, 122, 72, 142, 104, paint_Board);
		//���ߵ�¥��
		drawDpRect(canvas, 142, 4, 165, 72, paint_Board);
	}
	
}
