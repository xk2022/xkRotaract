package com.xk.common.json;

/**
 * Enum District_3502
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3502 {

    District_3502_1(1, "桃園扶青社_1999/01/01"),
    District_3502_2(2, "桃園東南扶青社_1999/01/01"),
    District_3502_3(3, "桃園福宏扶青社_1999/01/01"),
    District_3502_4(4, "桃園西門扶青社_1999/01/01"),
    District_3502_5(5, "航空城扶青社_1999/01/01"),
    District_3502_6(6, "八德陽德扶青社_1999/01/01"),
    District_3502_7(7, "桃園蘆竹扶青社_1999/01/01"),
    District_3502_8(8, "桃園自強扶青社_1999/01/01"),
    District_3502_9(9, "桃園ROTEX扶青社_1999/01/01"),
    District_3502_10(10, "桃園智德開南扶青社_1999/01/01");

    private int key;
    private String name;

    District_3502(int key, String name) {
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
