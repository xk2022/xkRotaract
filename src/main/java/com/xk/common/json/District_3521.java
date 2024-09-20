package com.xk.common.json;

/**
 * Enum District_3521
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3521 {

    District_3521_1(1, "北區扶青社_1989/03/21"),
    District_3521_2(2, "首都扶青社_1991/03/18"),
    District_3521_3(3, "圓山扶青社_1992/03/07"),
    District_3521_4(4, "雙溪扶青社_1995/07/01"),
    District_3521_5(5, "百齡扶青社_1996/07/01"),
    District_3521_6(6, "陽光扶青社_1996/11/20"),
    District_3521_7(7, "陽明扶青社_1998/04/04"),
    District_3521_8(8, "仰德扶青社_1999/04/21"),
    District_3521_9(9, "北安扶青社_2000/07/01"),
    District_3521_10(10, "中原扶青社_2002/05/21"),
    District_3521_11(11, "長安扶青社_2004/02/28"),
    District_3521_12(12, "華朋扶青社_2004/07/03"),
    District_3521_13(13, "劍潭扶青社_2006/12/23"),
    District_3521_14(14, "瑞安扶青社_2014/03/29"),
    District_3521_15(15, "芝山扶青社_2014/08/29"),
    District_3521_16(16, "關渡扶青社_1999/01/01");

    private int key;
    private String name;

    District_3521(int key, String name) {
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
