package dev.spruce.mercury.utils;

public class Time {

    public static long timeStart = System.currentTimeMillis();

    public static long getTime() {
        return System.currentTimeMillis() - timeStart;
    }
}
