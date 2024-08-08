package com.xk.common.json;

public enum District_3461 {

    District_3461_1(1, "台中東南扶青社"),	//	1987-06-30
    District_3461_2(2, "台中東興扶青社"),	//	2015-03-18
    District_3461_3(3, "台中市政扶青社"),	//	2016-11-11
    District_3461_4(4, "台中扶青社"),		//	1985-09-28
    District_3461_5(5, "台中中興扶青社"),	//	2016-03-12
    District_3461_6(6, "台中省都扶青社"),	//	2017-10-02
    District_3461_7(7, "台中南區扶青社"),	//	2015-06-13
    District_3461_8(8, "台中惠來扶青社"),	//	2019-01-09
    District_3461_9(9, "大甲扶青社"),		//	1988-07-14
    District_3461_10(10, "台中中山扶青社"),	//	2006-06-18
    District_3461_11(11, "台中台美扶青社"),	//	2015-03-11
    District_3461_12(12, "台中港扶青社"),		//1994-05-29
    District_3461_13(13, "東區菁英扶青社"),	//	2015-08-26
    District_3461_14(14, "台中大屯扶青社"),	//	2013-06-20
    District_3461_15(15, "台中中區扶青社");	//	2014-09-29

    private int key;
    private String name;

    District_3461(int key, String name) {
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
