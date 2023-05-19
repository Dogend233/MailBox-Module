package com.tripleying.dogend.mailbox.module.datemail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    private static final SimpleDateFormat sdf;
    
    static{
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public static String getTimeFromString(String str){
        String result = null;
        String[] st = str.split(" ");
        if(st.length==3 || st.length==6){
            try{
                int[] tt = new int[st.length];
                for(int i=0;i<st.length;i++){
                    tt[i] = Integer.parseInt(st[i]);
                }
                if(tt[0]<10000 && tt[0]>999 && tt[1]>0 && tt[1]<13 && tt[2]>0){
                    Calendar c = Calendar.getInstance();
                    c.set(tt[0], tt[1], 0);
                    int dm = c.get(Calendar.DAY_OF_MONTH);
                    if(tt[2]<=dm){
                        StringBuilder sb = new StringBuilder();
                        sb.append(tt[0]).append('-').append(tt[1]).append('-').append(tt[2]).append(' ');
                        if(tt.length==3){
                            sb.append("00:00:00");
                        }else{
                            if(tt[3]>=0 && tt[3]<24 && tt[4]>=0 && tt[4]<60 && tt[5]>=0 && tt[5]<60){
                                sb.append(tt[3]).append(':').append(tt[4]).append(':').append(tt[5]);
                            }
                        }
                        String ts = sb.toString();
                        Date date = sdf.parse(ts);
                        String rt = sdf.format(date);
                        if(rt!=null) result = rt;
                    }
                }
            }catch(Exception ex){}
        }
        return result;
    }
    
    public static boolean isStart(String start){
        try {
            long time = sdf.parse(start).getTime();
            return System.currentTimeMillis()>=time;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return false; 
        }
    }
    
    public static boolean isEnd(String end){
        try {
            long time = sdf.parse(end).getTime();
            return System.currentTimeMillis()>time;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return false; 
        }
    }
    
}
