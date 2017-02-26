import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

class MyComponent implements ComponentBase{
    HashMap<String, String> voterTable = new HashMap<>();
    HashMap<String, String> tallyTable = new HashMap<>();


    public KeyValueList processMsg(KeyValueList kvList) {
        System.out.println("Here is kvList:");
        System.out.println(kvList);

        KeyValueList conn = new KeyValueList();

        if(kvList.getValue("Head") != ""){
//            System.out.println(kvList.getValue("Head"));
//
//            //Assume that we get the msgId at some point above this
//            if(msgId == 21){
//
//            } else if(msgId == 22){
//
//            } else if(msgId == 23){
//
//            } else if(msgId == 24){
//
//            } else if(msgId == 25){
//
//            } else if(msgId == 26){
//
//            } else if(msgId == 701){
//
//            } else if(msgId == 702){
//
//            } else if(msgId == 703){
//
//            } else if(msgId == 711){
//
//            } else if(msgId == 712){
//
//            }
        } else{
            System.out.println(kvList.getValue("Sender"));
            conn.addPair("Scope", "SIS.Scope1");
            conn.addPair("MessageType", "Confirm");
            conn.addPair("Sender", "SISServer");
            conn.addPair("Receiver", "InterfaceServer");
        }

        return conn;

    }
}