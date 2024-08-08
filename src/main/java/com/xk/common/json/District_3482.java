package com.xk.common.json;

public enum District_3482 {

    District_3482_1(1, "台北延平扶青社"),	//	1985-04-30
    District_3482_2(2, "台北城中扶青社"),	//	1996-03-18
    District_3482_3(3, "台北城東扶青社"),	//	1997-10-31
    District_3482_4(4, "台北稻江扶青社"),	//	1997-02-25
    District_3482_5(5, "台北大稻埕扶青社"),	//	2002-03-12
    District_3482_6(6, "台北北海扶青社"),	//	2003-05-10
    District_3482_7(7, "台北圓環扶青社"),	//	2005-03-12
    District_3482_8(8, "台北大安扶青社"),	//	2006-05-09
    District_3482_9(9, "台北大龍峒扶青社"),	//	2006-07-05
    District_3482_10(10, "台北西北區扶青社"),	//	2007-04-01
    District_3482_11(11, "台北百城扶青社"),	//	2008-10-19
    District_3482_12(12, "台北東海扶青社"),	//	2009-12-19
    District_3482_13(13, "台北新世紀扶青社"),	//	2014-01-18
    District_3482_14(14, "台北圓滿扶青社"),	//	2013-11-28
    District_3482_15(15, "台北上城扶青社"),	//	2014-09-03
    District_3482_16(16, "台北保安扶青社"),	//	2015-02-24
    District_3482_17(17, "台北邑德扶青社"),	//	2015-09-15
    District_3482_18(18, "台北百合扶青社"),	//	2015-10-09
    District_3482_19(19, "台北怡東扶青社"),	//	2016-05-13
    District_3482_20(20, "台北城中北醫大扶青社");	//	2020-09-15

    private int key;
    private String name;

    District_3482(int key, String name) {
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
