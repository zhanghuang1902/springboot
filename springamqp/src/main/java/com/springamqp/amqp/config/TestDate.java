package com.springamqp.amqp.config;

import java.util.Date;

public class TestDate {

    public static void main(String[] args) {
        Date lastWeekState = DateUtil.getBeginDayOfLastWeek();
        String s = cn.hutool.core.date.DateUtil.dayOfWeekEnum(lastWeekState).toChinese("周");
        System.out.println(s);
        Date endDayOfLastWeek = DateUtil.getEndDayOfLastWeek();
        System.out.println(endDayOfLastWeek);

        Date dayOfWeek = DateUtil.getBeginDayOfWeek();
        System.out.println(dayOfWeek);
        Date ofWeek = DateUtil.getEndDayOfWeek();
        System.out.println(ofWeek);
    }
}
