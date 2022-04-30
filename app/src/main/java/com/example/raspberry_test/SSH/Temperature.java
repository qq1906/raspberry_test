package com.example.raspberry_test.SSH;

import android.content.Intent;

import com.example.raspberry_test.data.User;

public class Temperature {

    public static String tem(){
        int Tem=0;
//        RemoteExecuteCommand rec = new RemoteExecuteCommand("zj.powerddns.xyz", "pi", "^K5F9L2!#^Gb%ekJ9&2xtE");//执行命令

        String s = "";
//        s=rec.execute("vcgencmd measure_temp | head -1");
        Exec.ssh("zj.powerddns.xyz", "pi", "vcgencmd measure_temp | head -1");
        System.out.println(s);

        return s;
    }

//    public static void main(String[] args) {
//        tem();
//    }
}
