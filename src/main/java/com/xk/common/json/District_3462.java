package com.xk.common.json;

/**
 * Enum District_3462
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3462 {

    District_3462_1(1, "員林扶青社_1989/04/30"),
    District_3462_2(2, "彰化松柏扶青社_2003/03/18"),
    District_3462_3(3, "台中東海扶青社_2010/01/08"),
    District_3462_4(4, "台中西屯扶青社_2013/03/08"),
    District_3462_5(5, "大里國光扶青社_2014/08/14"),
    District_3462_6(6, "大南投扶青社_2014/10/01"),
    District_3462_7(7, "台中惠文扶青社_2015/01/11"),
    District_3462_8(8, "台中西北扶青社_2015/05/27"),
    District_3462_9(9, "台中惠民扶青社_2015/12/21"),
    District_3462_10(10, "彰化半線扶青社_2016/03/16"),
    District_3462_11(11, "彰化磺溪扶青社_2017/06/19"),
    District_3462_12(12, "台中中科扶青社_2019/01/11"),
    District_3462_13(13, "台中草悟道扶青社_2019/05/26"),
    District_3462_14(14, "台中國美扶青社_2019/12/27");

    private int key;
    private String name;

    District_3462(int key, String name) {
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
