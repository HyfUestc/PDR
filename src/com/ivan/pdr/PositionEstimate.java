package com.ivan.pdr;

import org.apache.commons.math3.analysis.function.Sin;

/*
 * ��ʾ����λ����Ϣ���࣬�����������㡣
 * PositionEstimate�࣬����java
 */
public class PositionEstimate {
	private float[] position = new float[2];
	private float[] indoorPosition = new float[2];
	private float theta = 0;	//thetaΪ��������ϵ֮��ļнǣ�N����x�ᣩ��
	
	//Constructor,������������λ�ã�����������ϵ���������ϵ֮��ļнǡ�
	public PositionEstimate (float[] indoorPosition, float theta) {
		this.indoorPosition = indoorPosition.clone();
		this.theta = theta;
		//����������ϵת��Ϊ��������ϵ��
		position[0] = (float) (indoorPosition[0] * Math.cos(theta) 
				- indoorPosition[1] * Math.sin(theta));
		position[1] = (float) (indoorPosition[0] * Math.sin(theta) 
				+ indoorPosition[1] * Math.cos(theta));
	}
	
	//PDR���к�λ���㡣
	public void estimatePosition(float stepLength, float orientation) {
		position[0] = (float) (position[0] + stepLength * Math.cos(orientation));
		position[1] = (float) (position[1] + stepLength * Math.sin(orientation));
		//����ϵת��������������ϵת��Ϊ����
		indoorPosition[0] = (float) (position[0] * Math.cos(-theta) -
				position[1] * Math.sin(-theta));
		indoorPosition[1] = (float) (position[0] * Math.sin(-theta) + 
				position[1] * Math.cos(-theta));
	}
	
	//��õ�ǰλ�ã���������ϵ�µ�λ�ã���
	public float[] getCurrentPosition() {
		return position;
	}
	
	//���õ�ǰλ�ã���������ϵ�µ�λ�ã���
	public void setCurrentPosition(float[] position) {
		this.position = position.clone();
	}
	
	//��õ�ǰλ�ã���������ϵ�µ�λ�ã�
	public float[] getCurrentIndoorPosition() {
		return indoorPosition;
	}
}
