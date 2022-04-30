package com.example.raspberry_test.SSH;/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate remote exec.
 *   $ CLASSPATH=.:../build javac Exec.java 
 *   $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked 
 * on the remote side and outputs will be printed out.
 *
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import java.io.InputStream;

public class Exec {
  public static void main(String[] arg){
    ssh("zj.powerddns.xyz","pi","ls");
  }

  public static void ssh(String Ip, String UserName, String Command){

    try{
      JSch jsch=new JSch();

      String host=null;

        /*//host=JOptionPane.showInputDialog("Enter username@hostname",System.getProperty("user.name")+"@localhost");
        host="pi@zj.powerddns.xyz";
        System.out.println(host);

      String user=host.substring(0, host.indexOf('@'));
      host=host.substring(host.indexOf('@')+1);*/

      Session session=jsch.getSession(UserName, Ip, 22);

      /*
      String xhost="127.0.0.1";
      int xport=0;
      String display=JOptionPane.showInputDialog("Enter display name",
                                                 xhost+":"+xport);
      xhost=display.substring(0, display.indexOf(':'));
      xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
      session.setX11Host(xhost);
      session.setX11Port(xport+6000);
      */

      // username and password will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo();
      session.setUserInfo(ui);
      session.connect();

      //命令
     /* String command=JOptionPane.showInputDialog("Enter command",
                                                 "set|grep SSH");*/
      String command=Command;

      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);


      channel.setInputStream(null);

      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();

      channel.connect();

      byte[] tmp=new byte[1024];
      while(true){
        while(in.available()>0){
          int i=in.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          if(in.available()>0) continue;
          //System.out.println("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      channel.disconnect();
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }

  }

  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){
      int foo=0;
       return foo==0;
    }
  
    String passwd;
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){
      int result=0;
      if(result==0){
        passwd="^K5F9L2!#^Gb%ekJ9&2xtE";
//        System.out.println(passwd);
        return true;
      }
      else{ 
        return false; 
      }
    }
    public void showMessage(String message){
      //JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String[] promptKeyboardInteractive(String s, String s1, String s2, String[] strings, boolean[] booleans) {
      return new String[0];
    }

  }
}
