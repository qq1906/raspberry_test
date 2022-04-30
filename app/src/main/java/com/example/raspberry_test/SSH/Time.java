package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.User;

public class Time {

    public static String time(){
        int Tem=0;
        /*User user=new User();
        user.setIp("zj.powerddns.xyz");
        user.setUserName("pi");
        user.setPassword("^K5F9L2!#^Gb%ekJ9&2xtE");*/
        RemoteExecuteCommand rec = new RemoteExecuteCommand("zj.powerddns.xyz", "pi", "^K5F9L2!#^Gb%ekJ9&2xtE");//执行命令
        //  System.out.println(rec.execute("ls"));//执行脚本
        //rec.execute("ls");//这个方法与上面最大的区别就是，上面的方法，不管执行成功与否都返回，

        //  rec.executeSuccess("cat index.html");//这个方法，如果命令或者脚本执行错误将返回空字符串


        String s = "";

      //  s=rec.execute("cat /proc/uptime| awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf(\"%d天%d时%d分%d秒\",run_days,run_hour,run_minute,run_second)}'");
        Exec.ssh("zj.powerddns.xyz", "pi", "ls");

        return s;
    }
}
