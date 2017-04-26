package com.ivan.pdr;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
/*
 * 计步类，步长估计类
 */
public class StepDetector implements SensorEventListener{
	private static final String TAG = "StepDetector";
	
	protected SensorManager sensorManager;
	private Context context;
	//移动平均滤波
	private MovingAverage movingAverage = new MovingAverage(10);
	// 加速度计传感器数据
	protected float[] vAcceleration = new float[3];
	// 重力传感器数据
	protected float[] vGravity = new float[3];
	// 线性加速度传感器数据
	protected float[] vLinAcceleration = new float[3];
	//手机y轴与水平面夹角
	private float mPhy = (float) (Math.PI / 2);
	//步伐探测用到的参数
    private int nowStatus = -1;
    private int formerStatus = -1;
    private int stepNumber = 0;
    private long timeOfNow = 0;
    private long timeOfFormer = 0;
    private long intervalOfTime = 0;
    
    /*private float thresholdHigh = 0.5f;
    private float thresholdLow = -0.5f;*/
    private float thresholdHigh = 1f;
    private float thresholdLow = -1f;
    private float maxAcc = 0;
    private float minAcc = 0;
    private float stepLength = 0.3f;
    
	public StepDetector(Context context) {
		this.context = context;
		this.sensorManager = (SensorManager)context.
				getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void onResume() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_FASTEST);
		
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public void onPause() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			
			break;
		case Sensor.TYPE_GRAVITY:
			vGravity = event.values.clone();
			mPhy = (float) Math.atan(Math.abs(vGravity[2]/vGravity[1]));
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
			vLinAcceleration = event.values.clone();
			detectStep(vLinAcceleration, mPhy);
			break;
		default:
			break;
		}
	}
	
	//步伐探测方法。
	public void detectStep (float[] linAccValues, float phy) {
		//1.获取竖直方向加速度。
		float accVertical = (float) (linAccValues[1] * Math.cos(phy) + 
				linAccValues[2] * Math.sin(phy));
		//2.对数据进行移动平均滤波。
		movingAverage.pushValue((float)accVertical);	
		float afterMAValue = movingAverage.getValue();
		DetectorNewStep3(afterMAValue);
	}
	
    public void DetectorNewStep3(float values) {
        if (values > thresholdHigh) {
            nowStatus = 2;
            if (values >= maxAcc) {
                maxAcc = values;
            }
        } else if (values < thresholdLow) {
            nowStatus = 0;
            if (values <= minAcc) {
                minAcc = values;
            }
        } else if (values <= thresholdHigh && values >= thresholdLow) {	//改9.75为-0.5
            nowStatus = 1;
        }
        if ((nowStatus == 1) && (formerStatus == 0)) {
            timeOfNow = System.currentTimeMillis();
            intervalOfTime = timeOfNow - timeOfFormer;
            if ((intervalOfTime >= 200) ) {	//时间域条件判断 && (intervalOfTime <= 2000);
                stepNumber ++;				//删除最大时长限制，最大时长限制存在时会影响，第一步的判别。
                stepLength = stepLengthEstimate1(maxAcc, minAcc);
                {minAcc = 0;
                    maxAcc = 0;}
            }
            timeOfFormer = timeOfNow;
        }
        formerStatus = nowStatus;
    }
    
	public float stepLengthEstimate1(float maxAcc, float minAcc) {
		float steplength = (float) (1.31 * Math.pow((maxAcc - minAcc), 0.25) 
				  - 0.961);
		return steplength;
	}
    
    public int getStepNumber() {
    	return stepNumber;
    }
    
    public float getStepLength() {
    	return stepLength;
    }
}
