package com.panshi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author huangxiaolei
 * @description 工具类
 * @create 2019/07/23
 */
public class Utils {

    /**
     * 获取毫秒数
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    public static long getMillisecond(String time,String format) throws ParseException {
        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        //转成data对象
        Date parse = simpleDateFormat.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        Date date = calendar.getTime();
        return date.getTime();
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("2019-7-23");
        System.out.println(parse.getTime());
        //1563811200000
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        Date date = calendar.getTime();
        System.out.println(calendar.getTime().getTime());
    }

}
