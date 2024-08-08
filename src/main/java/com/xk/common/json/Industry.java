package com.xk.common.json;

import java.util.Arrays;
import java.util.Optional;

/**
 * 中華民國統計資訊網
 * https://www.stat.gov.tw/standardindustrialclassification.aspx?n=3144&sms=0&rid=11
 */
public enum Industry {

    Industry_1(1, "農、林、漁、牧業"),
    Industry_2(2, "礦業及土石採取業"),
    Industry_3(3, "製造業"),
    Industry_4(4, "電力及燃氣供應業"),
    Industry_5(5, "用水供應及污染政治業"),
    Industry_6(6, "營建工程業"),
    Industry_7(7, "批發及零售業"),
    Industry_8(8, "運輸及倉儲業"),
    Industry_9(9, "住宿及餐飲業"),
    Industry_10(10, "出版影音及資通訊業"),
    Industry_11(11, "金融及保險業"),
    Industry_12(12, "不動產業"),
    Industry_13(13, "專業、科學及技術服務業"),
    Industry_14(14, "支援服務業"),
    Industry_15(15, "藝術、娛樂及休閒服務業"),
    Industry_16(16, "教育業"),
    Industry_17(17, "公共行政及國防；強制性社會安全"),
    Industry_18(18, "醫療保健及社會工作服務業"),
    Industry_19(19, "其他服務業");

    private Integer key;
    private String name;

    Industry(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    // Optional: You can also have a method to get the name
    public String getName() {
        return name;
    }

    public static Optional<String> getNameByKey(Integer key) {
        return Arrays.stream(Industry.values())
                .filter(industry -> industry.getKey() == key)
                .map(Industry::getName)
                .findFirst();
    }

    @Override
    public String toString() {
        return name;
    }
}
