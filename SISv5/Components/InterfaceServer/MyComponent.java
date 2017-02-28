import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

class MyComponent implements ComponentBase {

    static String passcode = "m1k3+b3n";
    HashMap<String, String> voterTable = new HashMap<>();
    HashMap<String, Integer> tallyTable = new HashMap<>();


    public KeyValueList processMsg(KeyValueList kvList) {
        String msgId = kvList.getValue("Subject").equals("Initialize Tally Table") ? "703" : kvList.getValue("MessageCode");
        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Alert");
        conn.addPair("Sender", "InterfaceServer");
        conn.addPair("Receiver", "InputProcessor");
        conn.addPair("MessageCode", "711");

        //Create Component
        if (msgId.equals("21")) {
            System.out.println("Create Component");
        } else if (msgId.equals("22")) { //Kill component (done)
            if(kvList.getValue("Passcode").equals(passcode)){
                System.exit(0);
            } else {
                conn.addPair("MessageCode", "711");
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("23")) { //Connect to server (done)
            if(kvList.getValue("Passcode").equals(passcode)){
                KeyValueList conn2 = new KeyValueList();
                conn2.addPair("Scope", "SIS.Scope1");
                conn2.addPair("MessageType", "Connect");
                conn2.addPair("Role", "Basic");
                conn2.addPair("Name", "InputProcessor");
                conn = conn2;
            } else {
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("24")) { //Activate Component
            System.out.println("Activate Component");
        } else if (msgId.equals("25")) { //Deactivate component
            System.out.println("Deactivate Component");
        } else if (msgId.equals("26")) { //Acknowledgement
            conn.addPair("MessageType", "Confirm");
        } else if (msgId.equals("701")) { //Cast Vote (done)
            //if(kvList.getValue("Subject").equals("CS1631 Vote")){
                if(checkEmail(kvList.getValue("Vote")) && checkVoter(kvList.getValue("Voter"))) {
                    voterTable.put(kvList.getValue("Voter"), kvList.getValue("Voter"));
                    tallyTable.put(kvList.getValue("Vote"), tallyTable.get(kvList.getValue("Vote"))+1);
                }
                Integer status = checkEmail(kvList.getValue("Vote")) ? (checkVoter(kvList.getValue("Voter")) ? 3 : 1) : 2;
                conn.addPair("Status", status.toString());
           // } else {
                //return null;
           // }
        } else if (msgId.equals("702")) { //Request Report (needs to be tested)
            if(kvList.getValue("Passcode").equals(passcode)){
                for(Map.Entry<String, Integer> entry : tallyTable.entrySet()){
                    conn.addPair(entry.getKey(), entry.getValue().toString());
                }
            } else {
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("703")) { //Initialize Tally Table 
            if(kvList.getValue("Passcode").equals(passcode)){
                String[] posters = kvList.getValue("Posters").split(";");
                tallyTable = new HashMap<>();
                for(String poster : posters){
                    tallyTable.put(poster, 0);
                }
            } else {
                conn.addPair("Status", "2");
            }
        }

        return conn;
    }

    private boolean checkEmail(String vote){
        if(tallyTable.containsKey(vote)){
            return true;
        }
        return false;
    }

    private boolean checkVoter(String voter){
        if(voterTable.containsKey(voter)){
            return false;
        }
        return true;
    }
}