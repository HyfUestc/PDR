package com.tristan.sqlhelper;


import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

public class PointsData {

    private LocDataHelper helper;
    private SQLiteDatabase db = null;

    public PointsData(Context mContext){
        helper = new LocDataHelper(mContext);
    }

    /**
     * 从本地数据库获取坐标点
     * @return
     */
    public List<Point> getPointList(){
        List<Point> points = new ArrayList<Point>();
        try{
            db = helper.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from points", null);
            if(null != cursor){
                while(cursor.moveToNext()){
                	int x = cursor.getInt(cursor.getColumnIndex("val_x"));
                	int y = cursor.getInt(cursor.getColumnIndex("val_y"));
                    Point point = new Point(x,y);
                    points.add(point);
                }
            }
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null != db){
                db.close();
            }
        }
        return points;
    }

}