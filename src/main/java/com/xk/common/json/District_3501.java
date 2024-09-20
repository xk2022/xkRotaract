package com.xk.common.json;

/**
 * Enum District_3501
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3501 {

    District_3501_1(1, "楊梅扶青社_  1994/05/05"),
    District_3501_2(2, "新竹北區扶青社_2010/03/10"),
    District_3501_3(3, "中壢南星扶青社_2000/01/10"),
    District_3501_4(4, "聯合大學扶青社_1999/01/05"),
    District_3501_5(5, "中壢扶青社_  2014/04/05"),
    District_3501_6(6, "新竹西北扶青社_2017/05/17"),
    District_3501_7(7, "新竹中央扶青社_2007/12/25");

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
