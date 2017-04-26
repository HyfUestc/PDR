package com.xiaoxuan.map;

import java.util.ArrayList;

import android.graphics.Point;

public class Barrier {
    //封装好数据，设为private
	private ArrayList<Point> barrier=new ArrayList<Point>();
    
	//添加一个障碍点的方法
	public void addBarrierPoint(Point p){
		this.barrier.add(p);
	}
	
	//添加一系列障碍点的方法
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
	
	//返回实例域的方法
	public ArrayList<Point> getBarrierPoint(){
		return (ArrayList<Point>) this.barrier.clone();
	}
}
