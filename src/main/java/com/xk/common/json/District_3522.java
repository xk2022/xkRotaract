package com.xk.common.json;

/**
 * Enum District_3522
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3522 {

    District_3522_1(1, "東區扶青社_1987/02/16"),
    District_3522_2(2, "忠孝扶青社_1992/03/15"),
    District_3522_3(3, "銘傳扶青社_1995/03/10"),
    District_3522_4(4, "新東扶青社_1996/07/01"),
    District_3522_5(5, "東華扶青社_2000/06/18"),
    District_3522_6(6, "大仁扶青社_2001/04/26"),
    District_3522_7(7, "內湖扶青社_2010/02/01"),
    District_3522_8(8, "士林扶青社_2003/03/27"),
    District_3522_9(9, "北投扶青社_2006/09/23"),
    District_3522_10(10, "東昇扶青社_2007/12/15"),
    District_3522_11(11, "淡海扶青社_2008/03/28"),
    District_3522_12(12, "仁愛扶青社_2010/04/23"),
    District_3522_13(13, "七星扶青社_2011/01/20"),
    District_3522_14(14, "滬尾扶青社_2012/01/17"),
    District_3522_15(15, "文林扶青社_2013/06/12"),
    District_3522_16(16, "松仁扶青社_2014/04/26"),
    District_3522_17(17, "忠誠扶青社_2015/04/19"),
    District_3522_18(18, "草山扶青社_2015/07/01"),
    District_3522_19(19, "台北101扶青社_2015/01/09"),
    District_3522_20(20, "珍愛扶青社_2019/08/13"),
    District_3522_21(21, "同慶扶青社_2017/11/11"),
    District_3522_22(22, "東聖扶青社_2018/10/09"),
    District_3522_23(23, "同領扶青社_2018/07/11"),
    District_3522_24(24, "文湖扶青社_2018/05/22"),
    District_3522_25(25, "海洋扶青社_2018/11/16"),
    District_3522_26(26, "東和扶青社_2018/11/02");

    private int key;
    private String name;

    District_3522(int key, String name) {
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
