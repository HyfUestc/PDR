package com.tristan.sqlhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * ��assets�е�db�ļ�������databases��
 * @author Botision.Huang
 * @Date: 2015-8-18 ����4:11:24
 * @Descp: TODO
 */
public class DatabaseUtil {

    @SuppressLint("SdCardPath")
    public static void packDataBase(Context context){
        // com.kinth.youdian �ǳ���İ�����������Լ��ĳ������
        // /data/data/com.kinth.youdian/databasesĿ¼��׼���� SQLite ���ݿ�ĵط���Ҳ�� Android ����Ĭ�ϵ����ݿ�洢Ŀ¼
        // ���ݿ���Ϊ db_youdian.db 
        String DB_PATH = "/data/data/com.tristan.fivemapdemo/databases/";
        String DB_NAME = "localization.db";

        // ��� SQLite ���ݿ��ļ��Ƿ���� 
        if (!(new File(DB_PATH + DB_NAME)).exists()) {
            // �� SQLite ���ݿ��ļ������ڣ��ټ��һ�� database Ŀ¼�Ƿ����
            File f = new File(DB_PATH);
            // �� database Ŀ¼�����ڣ��½���Ŀ¼
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // �õ� assets Ŀ¼������ʵ��׼���õ� SQLite ���ݿ���Ϊ������
                InputStream is = context.getAssets().open(DB_NAME);
                // �����,��ָ��·��������db�ļ�
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                // �ļ�д��
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                // �ر��ļ���
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
