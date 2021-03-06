package com.example.raspberry_test;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raspberry_test.SSH.Exec;
import com.example.raspberry_test.SSH.HardDrive;
import com.example.raspberry_test.SSH.RemoteExecuteCommand;
import com.example.raspberry_test.SSH.T;
import com.example.raspberry_test.SSH.Temperature;
import com.example.raspberry_test.SSH.Time;
import com.example.raspberry_test.data.User;
import com.example.raspberry_test.plan.DateTimePickDialogUtil;

import java.io.IOError;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Switch Water,T_Mopen,F_Mopen,Led;
    private Spinner mWaterSpinner;
    private Spinner mDianSpinner;
    private Spinner plan_spinner_repeat,plan_spinner_condition;
    private ImageView imageView_d,imageView_w,imageView_led;
    private TextView wendu,harddrive,OStime,ding_water_time,ding_dian_time,water_Gpio,dian_Gpio;
    private Button delet_button,cancel_button;
    private ImageButton imageButton;
    private EditText plan_getTime;
    private ListView plan_list;

    private int state;  //????????? ????????????
    private int stop_time=0;  //????????? ????????????

   private String initStartDateTime;    //??????????????????

    private ArrayList<String> data=new ArrayList<>();

    ArrayAdapter<String> adapter;

    //private ListView listview;
    private MyAdapter myAdapter;
    /**
     * ??????????????????
     */
    private List<String> listData;
    /**
     * ????????????item?????????
     */
    private List<Integer> checkedIndexList;
    /**
     * ????????????item??????checkbox
     */
    private List<CheckBox> checkBoxList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       Log.e("tag","??????ip"+User.getIp());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initListData();
        initView();

        //???????????????????????????  5??????????????????
//        Startthread();
        SimpleDateFormat sdf=new SimpleDateFormat();
        //sdf.applyPattern("yyyy???MM???dd???HH???mm???ss???");
        //2012???07???02??? 16:45
        sdf.applyPattern("yyyy???MM???dd??? HH:mm");
        Date date=new Date();
        initStartDateTime=sdf.format(date);
        // initStartDateTime="2012???07???02??? 16:45";




        /**
         * ???????????????
         * */
        //??????????????????
        String[] mwater = getResources().getStringArray(R.array.water);
        //??????Adapter?????????????????????
        ArrayAdapter<String> adapter_w=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mwater);
        adapter_w.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //?????? Adapter??????????????????
        mWaterSpinner.setAdapter(adapter_w);


        String[] mdian = getResources().getStringArray(R.array.dian);
        ArrayAdapter<String> adapter_d=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mdian);
        adapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDianSpinner.setAdapter(adapter_d);

        String[] spinner_repeat = getResources().getStringArray(R.array.plan_repeat);
        ArrayAdapter<String> adapter_repeat=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinner_repeat);
        adapter_repeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_spinner_repeat.setAdapter(adapter_repeat);

        String[] spinner_condition = getResources().getStringArray(R.array.plan_condition);
        ArrayAdapter<String> adapter_condition=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinner_condition);
        adapter_condition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_spinner_condition.setAdapter(adapter_condition);






        /**
         * ?????????????????????
         * */
        mWaterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                imageView_w.setImageResource(R.drawable.water);
                AnimationDrawable animationDrawable_water=(AnimationDrawable) imageView_w.getDrawable();
                String[] languages = getResources().getStringArray(R.array.water);
                Toast.makeText(MainActivity.this, "?????????????????????:"+languages[pos], 100).show();

                switch (pos){
                    case 0:break;
                    

                    //150ml
                    case 1:
                        mDianSpinner.getParent();
//                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/switch/bumpDelay "+5);
                        Thread thread = new Thread(()->{
                            Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/bumpDelay "+5);
                        });
                        thread.start();
                        CountDownTimer timer=new CountDownTimer(1500,100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_water.start();
//                                ding_water_time.setText(millisUntilFinished/100.0+"s");
                            }

                            @Override
                            public void onFinish() {
//                                ding_water_time.setText("???");
                                animationDrawable_water.stop();
                            }
                        };
                        timer.start();


                        //300ml
                    case 2:break;
                    //1000ml
                    case 3:break;
                    //???????????????
                    case 4:
                        AlertDialog.Builder customizeDialog_water=new AlertDialog.Builder(MainActivity.this);
                        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.diy_water,null);
                        customizeDialog_water.setTitle("???????????????");
                        customizeDialog_water.setView(dialogView);
                        EditText editText_water=dialogView.findViewById(R.id.diy_edit_water);

                        imageView_w.setImageResource(R.drawable.water);
                        AnimationDrawable animationDrawable_diy_water=(AnimationDrawable) imageView_w.getDrawable();

                        customizeDialog_water.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str_water=editText_water.getText().toString();
//                                SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/switch/bumpDelay "+str_water);
                                Thread thread = new Thread(()->{
                                    Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/bumpDelay " +str_water);
                                });
                                thread.start();
                                CountDownTimer timer_zheng=new CountDownTimer((long) (Float.parseFloat(str_water)*1000),1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        animationDrawable_diy_water.start();
//                                        ding_water_time.setText(millisUntilFinished/1000+"ml");
                                    }

                                    @Override
                                    public void onFinish() {
//                                        ding_water_time.setText("???");
                                        animationDrawable_diy_water.stop();
                                        Water.setEnabled(true);
                                    }
                                };
                                timer_zheng.start();
                                Water.setEnabled(false);
                            }
                        });
                        customizeDialog_water.show();
                        break;


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        /**
         * ?????????????????????
         * */
        mDianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                imageView_d.setImageResource(R.drawable.dian_1);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                String[] languages = getResources().getStringArray(R.array.dian);
                Toast.makeText(MainActivity.this, "?????????????????????:"+languages[pos], 100).show();

                switch(pos){

                    //??????  ???
                    case 0:{ //Toast.makeText(MainActivity.this, "?????????????????????:"+languages[pos], 100).show();
                    Log.e("tag","?????????"+languages[pos]);
                    break;
                    }
                    //????????? 5s
                    case 1:{ Toast.makeText(MainActivity.this, "?????????????????????:"+languages[pos], 100).show();
//                        imageButton.setVisibility(View.VISIBLE);
                        mDianSpinner.getParent();
//                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/switch/MotorPositiveDelay "+5);
                        Thread thread = new Thread(()->{
                            Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorPositiveDelay "+5);
                        });
                        thread.start();
                        CountDownTimer timer=new CountDownTimer(30000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_dian.start();
//                                ding_dian_time.setText(millisUntilFinished/1000+"s");
                            }

                            @Override
                            public void onFinish() {
//                                ding_dian_time.setText("???");
                                animationDrawable_dian.stop();
                                T_Mopen.setEnabled(true);
                                F_Mopen.setEnabled(true);
                            }
                        };
                        timer.start();

                        T_Mopen.setEnabled(false);
                        F_Mopen.setEnabled(false);

                    Log.e("tag","?????????5s");

                    break;
                         }

                         //?????????5???
                    case 2:{ Toast.makeText(MainActivity.this, "?????????????????????:"+languages[pos], 100).show();
//                        imageButton.setVisibility(View.VISIBLE);

                        mDianSpinner.getParent();
                        imageView_d.setImageResource(R.drawable.dian_0);
                        AnimationDrawable animationDrawable_dian_fan=(AnimationDrawable) imageView_d.getDrawable();
//                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/switch/MotorReverseDelay "+5);
                        Thread thread = new Thread(()->{
                            Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorReverseDelay "+5);
                        });
                        thread.start();
                        CountDownTimer timer=new CountDownTimer(30000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_dian_fan.start();
//                                ding_dian_time.setText(millisUntilFinished/1000+"s");
                            }

                            @Override
                            public void onFinish() {
//                                ding_dian_time.setText("???");
                                animationDrawable_dian_fan.stop();
                                T_Mopen.setEnabled(true);
                                F_Mopen.setEnabled(true);
                            }
                        };
                        timer.start();
                        T_Mopen.setEnabled(false);
                        F_Mopen.setEnabled(false);
                        Log.e("tag","?????????5s");
                        break;


                    }
                    //?????????
                    case 3:{
                        imageView_d.setImageResource(R.drawable.dian_1);
                        AnimationDrawable animationDrawable_dian_diy_zheng=(AnimationDrawable) imageView_d.getDrawable();

                        AlertDialog.Builder customizeDialog=new AlertDialog.Builder(MainActivity.this);
                        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.diy_dian,null);
                            customizeDialog.setTitle("?????????");
                            customizeDialog.setView(dialogView);

                        RadioGroup radioGroup=(RadioGroup) dialogView.findViewById(R.id.radioGroup);
                        EditText editText=(EditText)dialogView.findViewById(R.id.diy_edit_dian);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                Log.e("tag",""+checkedId);
                                switch (checkedId){
                                    case  R.id.btnZheng:
                                        Log.e("tag","????????????");
                                        state=0;
                                        break;
                                    case R.id.btnFan:
                                        Log.e("tag","????????????");
                                        state=1;
                                        break;
                                }
                            }
                        });


                        customizeDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("tag","???????????????"+editText.getText());
                                    imageButton.setVisibility(View.VISIBLE);
                                    switch (state){
                                        case 0:
                                            String str_zheng=editText.getText().toString();
                                            SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh "+str_zheng);
                                            CountDownTimer timer_zheng=new CountDownTimer((long) (Float.parseFloat(str_zheng)*60000),1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    animationDrawable_dian_diy_zheng.start();
//                                                    ding_dian_time.setText(millisUntilFinished/1000+"s");
                                                }

                                                @Override
                                                public void onFinish() {
//                                                    ding_dian_time.setText("???");
                                                    animationDrawable_dian_diy_zheng.stop();
                                                    T_Mopen.setEnabled(true);
                                                    F_Mopen.setEnabled(true);
                                                }
                                            };
                                            timer_zheng.start();
                                            T_Mopen.setEnabled(false);
                                            F_Mopen.setEnabled(false);
                                            if(timer_zheng!=null)
                                            if(stop_time==1){
                                                timer_zheng.cancel();

//                                                ding_dian_time.setText("???");
                                                stop_time=0;

                                            }
                                            break;

                                        case 1:
                                            String str_fan=editText.getText().toString();
                                            imageView_d.setImageResource(R.drawable.dian_0);
                                            AnimationDrawable animationDrawable_dian_fan=(AnimationDrawable) imageView_d.getDrawable();
                                            SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time_fan.sh "+str_fan);
                                            CountDownTimer timer_fan=new CountDownTimer((long) (Float.parseFloat(str_fan) *60000),1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    animationDrawable_dian_fan.start();
//                                                    ding_dian_time.setText(millisUntilFinished/1000+"s");
                                                }
                                                @Override
                                                public void onFinish() {
//                                                    ding_dian_time.setText("???");
                                                    animationDrawable_dian_fan.stop();
                                                    T_Mopen.setEnabled(true);
                                                    F_Mopen.setEnabled(true);
                                                }
                                            };
                                            timer_fan.start();
                                            T_Mopen.setEnabled(false);
                                            F_Mopen.setEnabled(false);
/*
                                            if(timer_fan!=null)
                                                if(stop_time==1){
                                                    timer_fan.cancel();

//                                                    ding_dian_time.setText("???");
                                                    stop_time=0;

                                                }*/

                                            break;

                                    }



                                }
                            });
                        customizeDialog.show();
                        break;
                    }



                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        /**
         * ???????????????   --->??????
         * */
        plan_spinner_repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] languages=getResources().getStringArray(R.array.plan_repeat);
                Log.i("tag","???????????????????????????"+languages[position]);

                User.setplan_repeat(languages[position]);
                //????????????
                SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
                String currSun = dateFm.format(date);


                //????????????????????? ??????-???-???-???-??????
                String time=plan_getTime.getText().toString();
                String Year=time.substring(0,4);
                String H=time.substring(12,14);     //??????
                String M=time.substring(15,17);     //??????
                String Moon=time.substring(5,7);     //??????
                String Day=time.substring(8,10);     //???
                sumday(currSun);
                User.setPlan_time_year(Year);
                User.setPlan_time_moon(Moon);       //???????????????
                User.setPlan_time_day(Day);         //????????????
                User.setPlan_time_hour(H);          //???????????????
                User.setPlan_time_minute(M);        //???????????????
                //User.setPlan_time_week();    //???????????????




                switch (position){
                        case 0:
                            Log.e("tag","?????????*");
                        break;
                    /**
                     * ????????? ---  ??????-??????-????????????
                     * */
                        case 1:
                            Log.e("tag","????????????-????????????"+"?????????"+position+H+":"+M);
                            User.setPlan_time_hour(H);          //???????????????
                            User.setPlan_time_minute(M);        //???????????????
                            User.setPlan_repeat_num(position);



                            /**
                             * ?????? ---  ??????-??????-????????????
                             * */
                        case 2:

                           // User.setplan_repeat(languages[position]);
                            Log.e("tag","????????????-???????????????"+"?????????"+position+H+":"+M);
                            User.setPlan_time_hour(H);          //???????????????
                            User.setPlan_time_minute(M);        //???????????????
                            User.setPlan_repeat_num(position);

                        break;

                        /**
                         * ???????????????   ???????????????-??????-??????
                         * */
                        case 3:
                            Log.e("tag","????????????-????????????-??????"+"?????????"+position+H+":"+M+""+currSun);
                            User.setPlan_time_hour(H);          //???????????????
                            User.setPlan_time_minute(M);        //???????????????
                           // User.setPlan_time_week(currSun);    //???????????????
                            User.setPlan_repeat_num(position);
                            break;


                    /**
                     * ???????????????   ????????????-???-??????-??????
                     * */
                        case 4:
                            Log.e("tag","????????????-????????????-??????"+"?????????"+position+H+":"+M+"-"+currSun+"-???"+Moon+"-???"+Day);
                            User.setPlan_time_hour(H);          //???????????????
                            User.setPlan_time_minute(M);        //???????????????
                            User.setPlan_time_moon(Moon);       //???????????????
                            User.setPlan_time_day(Day);         //????????????
                            User.setPlan_repeat_num(position);
                        break;



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * ???????????????   --->??????
         * */
        plan_spinner_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] languages=getResources().getStringArray(R.array.plan_condition);
                Log.i("tag","???????????????????????????"+languages[position]);

                User.setplan_condition(languages[position]);

                switch (position){
                    case 0:
                        User.setPlan_conditon_num(position);
                        break;
                    case 1:
                        User.setPlan_conditon_num(position);
                        break;

                        case 2:
                        User.setPlan_conditon_num(position);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    //LED???
       /* Led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MainActivity.this, "??????led", Toast.LENGTH_SHORT).show();
                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Led.sh");

                }else{
                    Toast.makeText(MainActivity.this, "??????led", Toast.LENGTH_SHORT).show();
                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Led_stop.sh");

                }
            }
        });*/



        // Switch Water=(Switch) findViewById(R.id.s);//????????????


//        Water.setEnabled(openBoolean);

        /**
         * ????????????
         * */

        Water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageView_w.setImageResource(R.drawable.water);
                AnimationDrawable animationDrawable_water=(AnimationDrawable) imageView_w.getDrawable();
                if(isChecked){
                    Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    //SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Wopen.sh");

//                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/on.sh");

                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/bumpOn");
                    });
                    thread.start();
                    animationDrawable_water.start();

                    //Gpio??????
                    water_Gpio.setText("1");

                }else{
                    Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    //SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Wstop.sh");
//                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/off.sh");
                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/bumpOff");
                    });
                    thread.start();
                    animationDrawable_water.stop();

                    //Gpio??????
                    water_Gpio.setText("0");
                }
            }
        });

        boolean T=true;
       // Switch T_Mopen=(Switch) findViewById(R.id.dian_01);//??????????????????
        //Switch F_Mopen=(Switch) findViewById(R.id.dian_00);//??????????????????

        //??????????????????????????????????????????????????????????????????
        /*T_Mopen.setEnabled(openBoolean);
        F_Mopen.setEnabled(openBoolean);*/




        T_Mopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("tag","??????"+isChecked);
                imageView_d.setImageResource(R.drawable.dian_1);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                if(T_Mopen.isChecked()&&!F_Mopen.isChecked()){
                    F_Mopen.setEnabled(false);  //??????????????????
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mopen.sh");
                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorPositive");
                    });
                    thread.start();
                    animationDrawable_dian.start();

                    //Gpio??????
                    dian_Gpio.setText("1");
                }else{
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mstop.sh");
                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorOff");
                    });
                    thread.start();
                    animationDrawable_dian.stop();
                    F_Mopen.setEnabled(true);

                    //Gpio??????
                    dian_Gpio.setText("0");

                }
            }
        });


        F_Mopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageView_d.setImageResource(R.drawable.dian_0);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                if(!T_Mopen.isChecked()&&F_Mopen.isChecked()){
                    T_Mopen.setEnabled(false);
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mopen.sh");
                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorReverse");
                    });
                    thread.start();
                    animationDrawable_dian.start();

                    //Gpio??????
                    dian_Gpio.setText("1");
                }else{
                    T_Mopen.setEnabled(true);
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mstop.sh");
                    Thread thread = new Thread(()->{
                        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/switch/MotorOff");
                    });
                    thread.start();
                    animationDrawable_dian.stop();

                    //Gpio??????
                    dian_Gpio.setText("0");
                }
            }
        });







        //???????????????
        plan_getTime.setText(initStartDateTime);
        plan_getTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        MainActivity.this, initStartDateTime);
                dateTimePicKDialog.dateTimePicKDialog(plan_getTime);
                Log.v("tag","??????????????????"+plan_getTime+"??????"+plan_getTime.getText().toString());
                String str=plan_getTime.getText().toString().substring(5);
              Log.e("tag","???????????????"+str);
                User.setPlan_time(str);
            }
        });



        //????????????
        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        //???????????????????????????????????????
        //???radioGroup?????????????????????:setOnCheckedChanged()
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                Toast.makeText(getApplicationContext(), "????????????????????????,?????????" + radbtn.getText(), Toast.LENGTH_LONG).show();
                User.setRadio(radbtn.getText().toString());

                String str=radbtn.getText().toString();
                switch (str){
                    case "???":
                        User.setRadio_num(0);
                        break;
                    case "??????":
                        User.setRadio_num(1);
                        break;
                    case "????????????":
                        User.setRadio_num(2);
                        break;
                    case "????????????":
                        User.setRadio_num(3);
                        break;
                }
            }
        });



    }

    //???????????????
    private void initView() {
      //  btn1 = findViewById(R.id.bt_login);

        Water=findViewById(R.id.s);
        T_Mopen=findViewById(R.id.dian_01);
        F_Mopen=findViewById(R.id.dian_00);
        imageView_d=findViewById(R.id.dian);
        imageView_w=findViewById(R.id.water);
        mWaterSpinner=findViewById(R.id.spin_s);
        mDianSpinner=findViewById(R.id.spin_d);
        wendu=findViewById(R.id.wendu);
      //  harddrive=findViewById(R.id.harddrive);
        OStime=findViewById(R.id.OStime);
       // Led=findViewById(R.id.Led);
       // imageView_led=findViewById(R.id.imageView_led);
       // t=findViewById(R.id.but);


        /*2022-04-18 22:56:15 ??????*/
//        ding_water_time=findViewById(R.id.ding_water_time);
//        ding_dian_time=findViewById(R.id.ding_dian_time);
//        imageButton=findViewById(R.id.dian_cancel); // 2022-04-18 22:57:15
        delet_button=findViewById(R.id.delet_Button);
        cancel_button = findViewById(R.id.cancel_Button);

        //Gpio????????????
        water_Gpio=findViewById(R.id.water_Gpio);
        dian_Gpio=findViewById(R.id.dian_Gpio);


        //????????????
        plan_getTime=findViewById(R.id.plan_getTime);
        plan_spinner_repeat=findViewById(R.id.plan_spinner_repeat);
        plan_spinner_condition=findViewById(R.id.plan_spinner_condition);
        plan_list=findViewById(R.id.plan_list);

        //??????  ??????-????????????
        delet_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);


        myAdapter = new MyAdapter(getApplicationContext(), listData);
        plan_list.setAdapter(myAdapter);
        //??????listview???????????????
        plan_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                //???checkbox???????????????
                for (int i = 0; i < checkBoxList.size(); i++) {
                    checkBoxList.get(i).setVisibility(View.VISIBLE);
                }

                delet_button.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
                return false;
            }
        });

        checkedIndexList = new ArrayList<Integer>();
        checkBoxList = new ArrayList<CheckBox>();
    }
    /**
     ????????????????????????????????????
     * */
    private void  SSH(String ip, String name, String password, String order) {
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand(ip, name, password);
        // List<String> S = new ArrayList<>();
        // S.add(remoteExecuteCommand.execute("./test.sh"));
        remoteExecuteCommand.execute01(order);

    }

/*    //?????????????????????
    public void insert(View v) {
                Intent intent = new Intent(this,activate_login.class);
                startActivity(intent);
    }*/







/*    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Handler mHandler = new Handler();
            mHandler.postDelayed(runnable, 10000);
            Message message = new Message( );
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };*/


    private void Startthread(){
        new Thread(){
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (true);
            }
        }.start();
    }

    //?????????????????????????????????
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                   String Tem=Temperature.tem();
                   String Hard= HardDrive.hard();
                   String Ost= Time.time();

                    wendu.setText(Tem);
//                    harddrive.setText(Hard);
                    OStime.setText(Ost);
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh");
                    Log.e("tag","??????"+Tem+Ost);
                    break;
                case 2:
                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh");
                    break;



            }
        }
    };



    public void disconnect(View view) {
        Intent intent=new Intent(MainActivity.this,activate_login.class);
        startActivity(intent);
        //activate_login.this.setResult(RESULT_OK,intent);
        //?????????Activity
        MainActivity.this.finish();
    }


    public void dian_cancel_off(View view){
        imageView_d.setImageResource(R.drawable.dian_1);
        AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
        animationDrawable_dian.stop();
        F_Mopen.setEnabled(true);
        T_Mopen.setEnabled(true);
//        ding_dian_time.setText("???");
        stop_time=1;
        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mstop.sh");
        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mstop.sh");
    }

    /**
     * ????????????
     * */
    public void plan(View view) {

        Log.e("tag","????????????"+"??????:"+User.getPlan_time()+"????????????"+User.getRadio()+"?????????"+User.getplan_repeat()+"?????????"+User.getplan_condition());
        String str="???????????????"+User.getPlan_time()+","+User.getRadio()+","+User.getplan_repeat()+","+User.getplan_condition();
       // adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);


        if(User.getPlan_time()!=null&&User.getRadio()!=null){

             // data.add(User.getRadio()+"->"+" ???"+User.getplan_repeat()+" ???"+User.getplan_condition()+" : "+User.getPlan_time());
                listData.add(str);

        }else{
            Log.e("tag","????????????");

        }
       //
      //  adapter.notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
        plan_list.setAdapter(myAdapter);

        Log.e("tag","?????????????????????"+"???????????????"+User.getRadio_num()+",????????????"+User.getPlan_repeat_num()+",????????????"+User.getPlan_conditon_num());
        Log.e("tag",User.getPlan_time_year()+"-"+User.getPlan_time_moon()+"-"+User.getPlan_time_day()+"-"+User.getPlan_time_hour()+":"+User.getPlan_time_minute()+"-"+User.getPlan_time_week()+" "+User.getRadio_num()+" "+User.getPlan_repeat_num()+" "+User.getPlan_conditon_num());
        String shell=User.getPlan_time_year()+"-"+User.getPlan_time_moon()+"-"+User.getPlan_time_day()+"-"+User.getPlan_time_hour()+":"+User.getPlan_time_minute()+"-"+User.getPlan_time_week()+" "+User.getRadio_num()+" "+User.getPlan_repeat_num()+" "+User.getPlan_conditon_num();
//        SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/scrip/test2.sh "+shell);
        Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/scrip/test2.sh "+shell);
    }

    /**
     * ???????????????????????????
     */
    public void initListData() {
        //????????????
        listData = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            listData.add("item" + i);
        }
    }
    /**
     * ?????????listview????????????
     */
    class MyAdapter extends BaseAdapter {
        private List<String> listData;
        private LayoutInflater inflater;

        public MyAdapter(Context context, List<String> listData) {
            this.listData = listData;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return listData.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.textview);
                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                //???item??????checkbox??????checkBoxList???
                checkBoxList.add(viewHolder.checkbox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(listData.get(position));
            viewHolder.checkbox.setOnCheckedChangeListener(new CheckBoxListener(position));
            return convertView;
        }
        class ViewHolder {
            TextView tv;
            CheckBox checkbox;
        }
    }

    /**
     * checkbox????????????
     */
    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        /**
         * ??????item???????????????
         */
        int position;

        public CheckBoxListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            if (isChecked) {
                checkedIndexList.add(position);
            } else {
                checkedIndexList.remove((Integer) position);
            }
        }
    }


    /**
     * ???????????????????????????
     */
    public void click_deleteButton(View v) {
        //??????checkedIndexList??????????????????????????????,?????????????????????????????????????????????????????????
        checkedIndexList = sortCheckedIndexList(checkedIndexList);
        for (int i = 0; i < checkedIndexList.size(); i++) {
            //???????????????int,?????????????????????????????????,???????????????????????????????????????????????????
            listData.remove((int) checkedIndexList.get(i));
            checkBoxList.remove(checkedIndexList.get(i));
        }
        for (int i = 0; i < checkBoxList.size(); i++) {
            //?????????????????????????????????
            checkBoxList.get(i).setChecked(false);
            //???checkbox??????????????????
            checkBoxList.get(i).setVisibility(View.INVISIBLE);
        }
        //???????????????
        myAdapter.notifyDataSetChanged();
        //??????checkedIndexList,???????????????????????????
        checkedIndexList.clear();

        delet_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);
    }

    /**
     * ???????????????????????????
     */
    public void click_cancelButton(View v) {
        for (int i = 0; i < checkBoxList.size(); i++) {
            //?????????????????????????????????
            checkBoxList.get(i).setChecked(false);
            //???checkbox??????????????????
            checkBoxList.get(i).setVisibility(View.INVISIBLE);
            delet_button.setVisibility(View.GONE);
            cancel_button.setVisibility(View.GONE);
        }
    }

    /**
     * ???checkedIndexList????????????????????????????????????
     */
    public List<Integer> sortCheckedIndexList(List<Integer> list) {
        int[] ass = new int[list.size()];//????????????
        for (int i = 0; i < list.size(); i++) {
            ass[i] = list.get(i);
        }
        Arrays.sort(ass);
        list.clear();
        for (int i = ass.length - 1; i >= 0; i--) {
            list.add(ass[i]);
        }
        return list;
    }



    public void  sumday(String  str){
        switch (str){
            case "?????????":User.setPlan_time_week("1");break;
            case "?????????":User.setPlan_time_week("2");break;
            case "?????????":User.setPlan_time_week("3");break;
            case "?????????":User.setPlan_time_week("4");break;
            case "?????????":User.setPlan_time_week("5");break;
            case "?????????":User.setPlan_time_week("6");break;
            case "?????????":User.setPlan_time_week("7");break;
        }

    }

}
