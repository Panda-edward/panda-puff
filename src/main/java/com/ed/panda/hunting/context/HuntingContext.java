package com.ed.panda.hunting.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Edward
 * @date : 2020/11/2 下午5:33
 */
public class HuntingContext {

    private static ThreadLocal<HunterInfo> context = new ThreadLocal<>();

    private static Map<String, PreyInfo> preys = new HashMap<>();

    public static Map<String, String> strategys = new HashMap<>();

    public static Map<String, String> executors = new HashMap<>();

    public static void setHunterInfo(HunterInfo value) {
        context.set(value);
    }

    public static HunterInfo getHunterInfo() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

    public static PreyInfo getPreyInfo(String bizType) {
        return preys.get(bizType);
    }

    public static void setPreyInfo(String bizType, PreyInfo preyInfo) {
        preys.put(bizType, preyInfo);
    }

}
