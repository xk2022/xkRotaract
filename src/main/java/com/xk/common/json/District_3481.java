package com.xk.common.json;

public enum District_3481 {

    District_3481_1(1, "台北西門扶青社"),	//	1984-02-20
    District_3481_2(2, "台北西區扶青社"),	//	1984-12-14
    District_3481_3(3, "中和扶青社"),		//	1985-05-01
    District_3481_4(4, "台北艋舺扶青社"),	//	1988-01-24
    District_3481_5(5, "永和扶青社"),		//	1995-02-16
    District_3481_6(6, "台北扶青社"),		//	1997-08-26
    District_3481_7(7, "台北永福扶青社"),	//	2001-03-05
    District_3481_8(8, "台北錫口扶青社"),	//	2013-10-15
    District_3481_9(9, "台北龍山扶青社"),	//	2014-09-05
    District_3481_10(10, "台北新起扶青社"),	//	2015-07-25
    District_3481_11(11, "台北晨新扶青社"),	//	2018-07-02
    District_3481_12(12, "台北傳世扶青社");	//	2019-07-01

    private int key;
    private String name;

    District_3481(int key, String name) {
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
