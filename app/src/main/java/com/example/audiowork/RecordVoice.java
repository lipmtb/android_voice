package com.example.audiowork;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RecordVoice implements Command {
    private String name;
    private MediaRecorder mRecorder;
    private boolean isRecording = false;
    private String recordFilePath;

    public RecordVoice(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    public String getBase64EncodedAudio() {
        String base64EncodedAudio = "";
        byte[] data;
        try {
            Log.d("getBase64EncodedAudio23333:", this.recordFilePath);
            FileInputStream fileInputStream = new FileInputStream(this.recordFilePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            data = byteArrayOutputStream.toByteArray();
            base64EncodedAudio = Base64.encodeToString(data, Base64.DEFAULT);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64EncodedAudio;
    }

    public String getOutputFilePath(Context ctx) {
        // 定义文件名的格式
        String fileNamePattern = "file_at_%s.aac";
        // 获取当前时间并格式化为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        // 创建文件名
        String fileName = String.format(fileNamePattern, formattedDateTime);
        File file = new File(ctx.getExternalFilesDir(""), fileName);
//        File file = new File(Environment., fileName);
        String filepath = file.getAbsolutePath();
        Log.d("getOutputFilePath66:", filepath);
        Log.d("getOutputFilePath，fileName:", fileName);
        return filepath;
    }

    // 开始录音
    private void startRecord(Context ctx) {
        try {
            if (this.mRecorder == null) {
                this.mRecorder = new MediaRecorder();
            }
            this.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);

            this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            String filePath = this.getOutputFilePath(ctx);

            this.mRecorder.setOutputFile(filePath);

            this.mRecorder.prepare();

            this.mRecorder.start();

            this.isRecording = true;
            this.recordFilePath = filePath;
        } catch (IOException e) {
            Log.d("startRecord 错误:", e.toString());
            e.printStackTrace();
        }
    }

    // 结束录音
    private String endRecord() {
        try {
            if (this.mRecorder != null && this.isRecording) {
                this.mRecorder.stop();
                this.mRecorder.release();
                this.isRecording = false;
                return this.getBase64EncodedAudio();
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.d("endRecord 错误:", e.toString());
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void execWithCallback(Context ctx, Map params, CommandCallback callback) {
        Log.d("RecordVoice execWithCallback执行,param is:", params.toString());
        String type = (String) params.get("type");
        if (type.equals("start")) {
            this.startRecord(ctx);
            params.put("recording", "1");
            callback.onResult(params);
        } else if (type.equals("end")) {
            String base64 = this.endRecord();
            this.recordFilePath = "";
            params.put("audioBase64", base64);
            callback.onResult(params);
        } else {
            Log.d("RecordVoice error param is:", params.toString());
        }
    }
}
