package com.ivan.pdr;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.tristan.mapview.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/*
 * �Ʋ�����
 */

public class PDRService extends Service {
	private static final String TAG = "PDRService";
	
	private boolean doWrite = true;
	private int mStepNumber = 0;
	private int mFormerStepNumber = 0;
	private float mStepLength = 0;
    private float[] vOrientation = new float[3];
    private float[] mPosition = new float[2];
    private float[] mIndoorPosition = new float[2];
    private String fileName;

    protected Runnable mRunable;
    protected Handler mHandler;
    
    private Orientation mOrientation;
    private StepDetector mStepDetector;
    private PositionEstimate mPositionEstimate;
    private OutputSdCard mOutputSdCard;
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG,"PDRService onCreate");
		super.onCreate();
		init();
		mHandler = new Handler();
		mRunable = new Runnable()
		{
			@Override
			public void run()
			{
				mHandler.postDelayed(this, 100);//ÿ0.1sִ��һ�Ρ�
				vOrientation = mOrientation.getOrientation();
				mStepNumber = mStepDetector.getStepNumber();
				mStepLength = mStepDetector.getStepLength();
				//�����������仯����������²�����
				//mIndoorPosition:���Ƶ�λ����Ϣ��mStepNumber���ƵĲ���
				//vOrientation���Ƶķ���
				if (mStepNumber != mFormerStepNumber) {
					//1.����λ�ã�����ȡλ����Ϣ��
					mPositionEstimate.estimatePosition(mStepLength, vOrientation[0]);
					mPosition = mPositionEstimate.getCurrentPosition();
					mIndoorPosition = mPositionEstimate.getCurrentIndoorPosition();
					Log.d(TAG, "mPosition " + mStepNumber + ", x: " 
					+ mPosition[0] + "y: " + mPosition[1] + "mIndoorPosition, " + 
							"x: " + mIndoorPosition[0] + "y: " + mIndoorPosition[1]);
					//2.����Ϣ����MainActivity�У���ͨ������MapView�࣬�ڵ�ͼ�Ͻ�����ʾ��
					Point currentPoint = new Point((int)(mIndoorPosition[0] * 250 / 6), 
							(int)(mIndoorPosition[1] * 500 / 10.5));
                    Message msg = new Message();
                    msg.what = MainActivity.FLAG;
                    msg.obj = currentPoint;
                    MainActivity.handler2.sendMessage(msg);
                    
                    /*Message msg1 = new Message();
                    msg1.what = MainActivity.FLAG1;
                    msg.obj = mStepNumber;
                    MainActivity.handler2.sendMessage(msg1);*/
                    //3.��λ�����ꡢ���Ƶķ��򡢹��ƵĲ���������SD���ļ��С�
					if (doWrite == true) {
						String message = new String();
						DecimalFormat df = new DecimalFormat("#,##0.000");
						message += df.format(mStepNumber) + " ";
						message += df.format(vOrientation[0]) + " ";
						message += df.format(mIndoorPosition[0]) + " ";
						message += df.format(mIndoorPosition[1]) + "\r\n";	
						mOutputSdCard.method1(fileName, message);	
					}
				}
				mFormerStepNumber = mStepNumber;
			}
		};
        mHandler.post(mRunable);
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"PDRService onStartCommand");
        mOrientation.onResume();
        mStepDetector.onResume();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"PDRService onDestroy");
        super.onDestroy();
        mOrientation.onPause();
        mStepDetector.onPause();
    }
    
    public void init() {
    	//��ʼ��������ͼƲ���
    	mOrientation = new GyroscopeOrientation(this);
    	mStepDetector = new StepDetector(this);
    	//���ó�ʼ���λ����Ϣ��
    	mPosition[0] = 3;
    	mPosition[1] = 5;
    	mPositionEstimate = new PositionEstimate(mPosition, 0);
    	//���ü�¼txt�ļ���fileName��
    	SimpleDateFormat sdf = new SimpleDateFormat("MMdd.HHmm.ss",Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		fileName = mOutputSdCard.getSDPath() + "/"  + timeStr + ".txt";
    }
    
}
/*public class PDRService extends Service implements SensorEventListener{
    private static final String TAG = "PedometerService";
    private final int FREQUENCY = 200;

    private int mStepNumber = 0;
    private int formerStepNumber = 0;
    private double phy = Math.PI / 2;
    private double accVertical = 0;
    
    private float[] accelerationValues;
    private float[] magneticValues;
    
    private SensorManager mSensorManager;
    private Sensor mAccl,mGravity,mLinearAccel,mRotationVector;

    private Pedometer mSimpleStepDetector = new Pedometer();
    private MovingAverage mMovingAverage = new MovingAverage(10);
    
    private Orientation mOrientation;

    protected Handler mHandler;
    
    private float[] vOrientation = new float[3];
    
    protected Runnable runable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"PedometerService onCreate");
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccl = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mLinearAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //mSensorManager.registerListener(this,mAccl,FREQUENCY);
        mSensorManager.registerListener(this,mGravity,FREQUENCY);
        mSensorManager.registerListener(this,mLinearAccel,FREQUENCY);
        mSensorManager.registerListener(this, mRotationVector,FREQUENCY);
        mOrientation = new GyroscopeOrientation(this);
		mHandler = new Handler();

		runable = new Runnable()
		{
			@Override
			public void run()
			{
				mHandler.postDelayed(this, 100);
				Log.d(TAG, "Hello");
				vOrientation = mOrientation.getOrientation();	//��ȡ�ںϺ�ĽǶ�ֵ��Ϣ

				dataReady = true;

				updateText();		//����UI����ʾ�ĽǶ�ֵ
				updateGauges();
				Log.d(TAG, "z: " + Math.toDegrees(vOrientation[0]));
			}
		};
        mHandler.post(runable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"PedometerService onStartCommand");
        mOrientation.onResume();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"PedometerService onDestroy");
        mOrientation.onPause();
        mHandler.removeCallbacks(runable);	
        //mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    	float[] rotationMatrix;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mStepNumber ++;

                //����������PedometerFragment
                Message msg = new Message();
                msg.what = PedometerFragment.FLAG;
                msg.obj = mStepNumber;
                PedometerFragment.handler.sendMessage(msg);

                Log.d(TAG,mStepNumber + " ");
                break;
            case Sensor.TYPE_GRAVITY:
                float[] graValues = new float[3];
                graValues = event.values.clone();
                phy = Math.atan(Math.abs(graValues[2]/graValues[1]));	//����н�phy��2017.1.18�ո�[2]\[1]����
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                float[] linAccValues = event.values.clone();

                accVertical = linAccValues[1] * Math.cos(phy) + linAccValues[2] * Math.sin(phy);
                mMovingAverage.pushValue((float) accVertical);
                float afterMAValue = mMovingAverage.getValue();

                //Log.d(TAG,"AfterMAValue: " + afterMAValue);
                mSimpleStepDetector.DetectorNewStep3(afterMAValue);
                mStepNumber = mSimpleStepDetector.getFootNumber();
                
                //���������仯����ı�Point����ֵ,��������MainActivity
                if (formerStepNumber != mStepNumber) {
                	Log.d(TAG,"mStepNumber: " + mStepNumber);
                	Point currentPoint = new Point(150,30+10*mStepNumber);
                    Message msg = new Message();
                    msg.what = MainActivity.FLAG;
                    //msg.obj = mStepNumber;
                    msg.obj = currentPoint;
                    MainActivity.handler2.sendMessage(msg); 
				}
                
                formerStepNumber = mStepNumber;

                //����������PedometerFragment
                Point currentPoint = new Point(10,10*mStepNumber);
                Message msg = new Message();
                msg.what = PedometerFragment.FLAG;
                //msg.obj = mStepNumber;
                msg.obj = currentPoint;
                PedometerFragment.handler.sendMessage(msg);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
            	//Log.d(TAG, "This is rotation vector !!!!");
            	rotationMatrix = new float[16];
            	mSensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            	determineOrientation(rotationMatrix);
            	break;
            case Sensor.TYPE_GYROSCOPE:
            	break;
            case Sensor.TYPE_MAGNETIC_FIELD:
            	break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    
    
     * ������ټƺʹų����ݣ�ͨ�����ټƺʹų����ݼ�����ת����
     * accelerationValues:���ټ����ݣ�magneticValues:���������ݡ�
     * rotationMatrix����ȡ����ת����
     
    private float[] generateRotationMatrix()
    {
        float[] rotationMatrix = null;
        
        if (accelerationValues != null && magneticValues != null)
        {
            rotationMatrix = new float[16];
            boolean rotationMatrixGenerated;
            rotationMatrixGenerated =
                    SensorManager.getRotationMatrix(rotationMatrix,
                    null,
                    accelerationValues,
                    magneticValues);
            
            if (!rotationMatrixGenerated)
            {
                //Log.w(TAG, getString(R.string.rotationMatrixGenFailureMessage));

                rotationMatrix = null;
            }
        }
            
        return rotationMatrix;
    }
    
    private void determineOrientation(float[] rotationMatrix) {
    	float[] orientationValues = new float[3];
    	SensorManager.getOrientation(rotationMatrix, orientationValues);
    	
    	//double azimuth = Math.toDegrees(orientationValues[0]);
    	double azimuth = orientationValues[0];
    	double pitch = Math.toDegrees(orientationValues[1]);
    	double roll = Math.toDegrees(orientationValues[2]);
    	
    	Log.d(TAG, "azimuth: " + azimuth + "\n");
    	Log.d(TAG, "pitch: " + pitch + "\n");
    	Log.d(TAG, "roll: " + roll + "\n");
    }
}*/
