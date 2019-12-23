package com.friean.javabase.designpatterns;

import java.util.HashMap;
import java.util.Map;

public class Reform {

    public static void main(String[] args) {
        // Initializing a Dictionary:
        // { ‘A’: 1, ‘B.A’: 2, ‘B.B’: 3, ‘CC.D.E’: 4, ‘CC.D.F’: 5}
        Map<String,Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B.A", 2);
        map.put("B.B", 3);
        map.put("CC.D.E", 4);
        map.put("CC.D.F", 5);

        System.out.println(map.toString());

        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> temMap = null;
        Map<String,Object> curElement = null;
        for (String key : map.keySet()) {
            if (key.contains(".")){
                String[] list =  key.split("\\.");
                curElement = resultMap;
                for (int i = 0; i < list.length; i++) {
                    if (i < list.length-1){
                        if (!curElement.containsKey(list[i])){
                            temMap = new HashMap<>();
                            curElement.put(list[i],temMap);
                            curElement = temMap;
                        }else {
                            curElement = (Map<String, Object>) curElement.get(list[i]);
                        }
                    }else{
                        curElement.put(list[i],map.get(key));
                    }
                }
            }
            else {
                resultMap.put(key,map.get(key));
            }
        }

        System.out.println(resultMap.toString());

    }
}
