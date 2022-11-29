package com.xc.utils.stock;

import com.xc.utils.DateTimeUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xc.utils.HolidayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetStayDays {
    private static final Logger log = LoggerFactory.getLogger(GetStayDays.class);


    public static Date getBeginDate(Date beginDate) {
        String date = DateTimeUtil.dateToStr(beginDate, "yyyy-MM-dd");
        return DateTimeUtil.strToDate(date, "yyyy-MM-dd");
    }


    public static int getWorkDays(Date StartDate) {
        //开始時间到現在有几天是節假日
        int holiday=0;
        Long begin_time=StartDate.getTime()/1000;
        long end_time=new Date().getTime()/1000;
        while(begin_time<end_time){
            begin_time+=3600*24;
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
            String httpArg = f.format(begin_time*1000);
            String jsonResult = HolidayUtil.request(httpArg);
            if(jsonResult.equals("2")){
                holiday++;
            }
        }
        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        Date begin_date = getBeginDate(StartDate);
        Date end_date = getBeginDate(new Date());

        try {
            cl1.setTime(begin_date);
            cl2.setTime(end_date);
        } catch (Exception e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }
        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            if (cl1.get(7) != 7 && cl1.get(7) != 1)
                count++;
            cl1.add(5, 1);
        }
        return count - 1 - holiday;
    }


    public static int getDays(Date StartDate) {
        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(StartDate);
            cl2.setTime(new Date());
        } catch (Exception e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }
        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            count++;
            cl1.add(5, 1);
        }
        return count - 1;
    }


    public static int testWorkDays(Date beginDate, Date endDate) {
        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(beginDate);
            cl2.setTime(endDate);
        } catch (Exception e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }
        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            if (cl1.get(7) != 7 && cl1.get(7) != 1)
                count++;
            cl1.add(5, 1);
        }
        return count - 1;
    }


    public static void main(String[] args) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date start = df.parse("2021-6-11");

        System.out.println("排除週末：" + getWorkDays(start));
        System.out.println("不排除週末： " + getDays(start));

        System.out.println(" -------- ");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begindate = df2.parse("2019-03-10 11:39:09");
        Date enddate = df2.parse("2019-03-11 07:15:00");
        log.info("测试的天数 = {}", Integer.valueOf(testWorkDays(begindate, enddate)));
        log.info("测试的天数 = {}", Integer.valueOf(getDays(begindate)));


        System.out.println(DateTimeUtil.dateToStr(getBeginDate(begindate)));
        System.out.println(DateTimeUtil.dateToStr(getBeginDate(enddate)));
    }
}
