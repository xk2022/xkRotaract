package com.xk.common.json;

/**
 * Enum District_3490
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3490 {

    District_3490_1(1, "三重扶青社_1984/03/30"),
    District_3490_2(2, "土城扶青社_1984/05/20"),
    District_3490_3(3, "基隆東南扶青社_1990/03/03"),
    District_3490_4(4, "宜蘭扶青社_1993/06/12"),
    District_3490_5(5, "板橋東區扶青社_1997/11/02"),
    District_3490_6(6, "新莊中央扶青社_2007/01/21"),
    District_3490_7(7, "板橋扶青社_2007/03/25"),
    District_3490_8(8, "漢林扶青社_2007/04/29"),
    District_3490_9(9, "五工扶青社_2008/03/23"),
    District_3490_10(10, "花蓮扶青社_2008/03/29"),
    District_3490_11(11, "福爾摩莎扶青社_2008/03/29"),
    District_3490_12(12, "三東湧蓮扶青社_2015/02/23"),
    District_3490_13(13, "基隆扶青社_2016/07/01"),
    District_3490_14(14, "三角湧扶青社_2017/04/01"),
    District_3490_15(15, "新北阿囉哈扶青社_2020/01/16");

    private int key;
    private String name;

    District_3490(int key, String name) {
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
