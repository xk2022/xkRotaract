package com.xk.common.json;

/**
 * Enum District_3510
 * @author yuan
 * Update by yuan on 2024/09/19
 */
public enum District_3510 {

    District_3510_1(1, "高雄東北扶青社_1993/03/11"),
    District_3510_2(2, "鳳山南區扶青社_1997/07/14"),
    District_3510_3(3, "高雄中興扶青社_2006/05/01"),
    District_3510_4(4, "高雄中山扶青社_2005/12/19"),
    District_3510_5(5, "高雄圓山扶青社_2009/10/26"),
    District_3510_6(6, "高雄港都扶青社_2009/12/28"),
    District_3510_7(7, "高雄扶青社_2010/08/03"),
    District_3510_8(8, "高雄北區扶青社_2013/02/22"),
    District_3510_9(9, "高雄拾穗扶青社_2014/04/30"),
    District_3510_10(10, "高雄文山扶青社_2014/02/03"),
    District_3510_11(11, "高雄南區扶青社_2015/08/25"),
    District_3510_12(12, "鳳山扶青社_2016/02/03"),
    District_3510_13(13, "高雄首都扶青社_2016/06/21"),
    District_3510_14(14, "高雄木棉南一扶青社_2017/02/10"),
    District_3510_15(15, "屏東鳳凰扶青社_2017/05/20"),
    District_3510_16(16, "高雄樂心扶青社_2019/04/25"),
    District_3510_17(17, "高科大扶青社_2019/05/08"),
    District_3510_18(18, "大樹扶青社_2019/07/30"),
    District_3510_19(19, "高雄燕之巢扶青社_2019/12/26"),
    District_3510_20(20, "北高雄扶青社_2020/09/13");


    private int key;
    private String name;

    District_3510(int key, String name) {
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
