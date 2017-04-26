package com.ivan.pdr;

import org.apache.commons.math3.analysis.function.Sin;

/*
 * 表示行人位置信息的类，包括航迹推算。
 * PositionEstimate类，输入java
 */
public class PositionEstimate {
	private float[] position = new float[2];
	private float[] indoorPosition = new float[2];
	private float theta = 0;	//theta为两个坐标系之间的夹角（N轴与x轴）。
	
	//Constructor,输入室内坐标位置，和室内坐标系与地理坐标系之间的夹角。
	public PositionEstimate (float[] indoorPosition, float theta) {
		this.indoorPosition = indoorPosition.clone();
		this.theta = theta;
		//将室内坐标系转换为地理坐标系。
		position[0] = (float) (indoorPosition[0] * Math.cos(theta) 
				- indoorPosition[1] * Math.sin(theta));
		position[1] = (float) (indoorPosition[0] * Math.sin(theta) 
				+ indoorPosition[1] * Math.cos(theta));
	}
	
	//PDR进行航位推算。
	public void estimatePosition(float stepLength, float orientation) {
		position[0] = (float) (position[0] + stepLength * Math.cos(orientation));
		position[1] = (float) (position[1] + stepLength * Math.sin(orientation));
		//坐标系转换，将地理坐标系转换为室内
		indoorPosition[0] = (float) (position[0] * Math.cos(-theta) -
				position[1] * Math.sin(-theta));
		indoorPosition[1] = (float) (position[0] * Math.sin(-theta) + 
				position[1] * Math.cos(-theta));
	}
	
	//获得当前位置（地理坐标系下的位置）。
	public float[] getCurrentPosition() {
		return position;
	}
	
	//设置当前位置（地理坐标系下的位置）。
	public void setCurrentPosition(float[] position) {
		this.position = position.clone();
	}
	
	//获得当前位置（室内坐标系下的位置）
	public float[] getCurrentIndoorPosition() {
		return indoorPosition;
	}
}
