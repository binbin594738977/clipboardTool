package util;

import core.Config;

public enum RequestUrl {
    ROBOT_GET_DEVICEID_BY_WX("Robot", "getDeviceIdByWx"),//微信号得到设备id
    ;
    public String clazz;
    public String method;

    RequestUrl(String clazz, String method) {
        this.clazz = clazz;
        this.method = method;
    }

    public String getUrl() {
        return Config.getProvideHost() + String.format("m=%s&c=%s", clazz, method);
    }


}
