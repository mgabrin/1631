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
        String msgId = "";
        if(kvList.getValue("Subject").equals("Initialize Tally Table")){
            msgId = "703";
        } else if(kvList.getValue("Subject").equals("Request Report")){
            msgId = "702";
        } else {
            msgId = kvList.getValue("MessageCode");
        }
        
        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Alert");
        conn.addPair("Sender", "InterfaceServer");
        conn.addPair("Receiver", "InputProcessor");
        conn.addPair("MessageCode", "711");

        String messageBody = kvList.getValue("Body");
        String[] lines = messageBody.split("\n");
        String foundPasscode = "";
        if (lines[0].startsWith("Passcode")){
            foundPasscode = lines[0].split(":")[1].trim();
        }

        //Create Component
        if (msgId.equals("21")) {
            System.out.println("Create Component");
        } else if (msgId.equals("22")) { //Kill component (done)
            if(foundPasscode.equals(passcode)){
                System.exit(0);
            } else {
                conn.addPair("MessageCode", "711");
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("23")) { //Connect to server (done)
            if(foundPasscode.equals(passcode)){
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
            if(foundPasscode.equals(passcode)){
                String values = "";
                for(Map.Entry<String, Integer> entry : tallyTable.entrySet()){
                    values += entry.getKey() + ',' + entry.getValue().toString() + ';';
                }
                values = values.substring(0, values.length() - 1);
                conn.addPair("Values", values);
            } else {
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("703")) { //Initialize Tally Table 
            if(foundPasscode.equals(passcode) && lines[1] != null){
                String[] posters = lines[1].split(";");
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