package dev.spruce.mercury;

public class EngineVersion {

    public static final String VERSION = "1.0.0";
    public static final String BUILD = "200723";

    public static String getNameVersion() {
        return String.format("Mercury Engine (%s # %s)", VERSION, BUILD);
    }
}
