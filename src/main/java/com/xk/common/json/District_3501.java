package com.xk.common.json;

public enum District_3501 {

    District_3501_1(1, "楊梅扶青社"),	//	1994-05-05
    District_3501_2(2, "新竹北區扶青社"),	//	2010-03-10
    District_3501_3(3, "中壢南星扶青社"),	//	2000-01-10
    District_3501_4(4, "聯合大學扶青社"),	//	1999-01-05
    District_3501_5(5, "中壢扶青社"),	//	2014-04-05
    District_3501_6(6, "新竹西北扶青社"),	//	2017-05-17
    District_3501_7(7, "新竹中央扶青社");	//	2007-12-25

    private int key;
    private String name;

    District_3501(int key, String name) {
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
}
