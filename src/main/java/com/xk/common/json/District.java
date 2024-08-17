package com.xk.common.json;

import java.util.HashMap;
import java.util.Map;

public enum District {

    District_3461(3461, "3461地區"),
    District_3462(3462, "3462地區"),
    District_3470(3470, "3470地區"),
    District_3481(3481, "3481地區"),
    District_3482(3482, "3482地區"),
    District_3490(3490, "3490地區"),
    District_3501(3501, "3501地區"),
    District_3502(3502, "3502地區"),
    District_3510(3510, "3510地區"),
    District_3521(3521, "3521地區"),
    District_3522(3522, "3522地區"),
    District_3523(3523, "3523地區");

    private int key;
    private String name;
    private static final Map<Integer, District> lookup = new HashMap<>();

    District(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    // Optional: You can also have a method to get the name
    public String getName() {
        return name;
    }

    public static String getNameByKey(int key) {
        District district = lookup.get(key);
        return (district != null) ? district.getName() : null;
    }
}
