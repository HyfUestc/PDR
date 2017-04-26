package com.xiaoxuan.map;

import java.util.ArrayList;

import android.graphics.Point;

public class Barrier {
    //��װ�����ݣ���Ϊprivate
	private ArrayList<Point> barrier=new ArrayList<Point>();
    
	//���һ���ϰ���ķ���
	public void addBarrierPoint(Point p){
		this.barrier.add(p);
	}
	
	//���һϵ���ϰ���ķ���
	public void addBarrierPoint(ArrayList<Point> ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	public void addBarrierPoint(Point[] ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	//����ʵ����ķ���
	public ArrayList<Point> getBarrierPoint(){
		return (ArrayList<Point>) this.barrier.clone();
	}
}
