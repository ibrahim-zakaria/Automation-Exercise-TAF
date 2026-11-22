package com.AutomationExercise.media;

import com.automation.remarks.video.recorder.IVideoRecorder;

public class ScreenRecordManager {
    public final static String RECORDINGS_PATH = "test-out/recordings";
    private static final ThreadLocal<IVideoRecorder> recorder = new ThreadLocal<>();
    //function for start recording
    public static void setRecording(){}

    public static void stopRecording(String name) {

    }
    //function for stop recording
    //function for encoding
}
