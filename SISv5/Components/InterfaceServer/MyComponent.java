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
        String msgId = getIdFromSubject(kvList);
        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Alert");
        conn.addPair("Sender", "InterfaceServer");

        String messageBody = kvList.getValue("Body");
        String[] lines = messageBody.split("\n");
        String foundPasscode = "";

        foundPasscode = getPasscode(lines);

        //Create Component
        if (msgId.equals("21")) {
            System.out.println("Create Component");
        } else if (msgId.equals("22") || msgId.equals("702")) { //End voting or Get Results
            conn.addPair("MessageCode", "712");
            conn.addPair("Status", "3");
            String values = "";
            for(Map.Entry<String, Integer> entry : tallyTable.entrySet())
              values += entry.getKey() + ',' + entry.getValue() + ';';

            values = values.substring(0, values.length() - 1);
            conn.addPair("Values", values);

            if(msgId.equals("22"))
            {
              conn.addPair("Receiver", "PosterComponent");
              conn.addPair("Kill", "True");
            }
            else
              conn.addPair("Receiver", "GUI");

        } else if (msgId.equals("23")) { //Connect to server (done)
            conn.addPair("MessageCode", "712");
            if(foundPasscode.equals(passcode)){
                KeyValueList conn2 = new KeyValueList();
                conn2.addPair("Scope", "SIS.Scope1");
                conn2.addPair("MessageType", "Connect");
                conn2.addPair("Role", "Basic");
                conn2.addPair("Name", "InputProcessor");
                conn2.addPair("Status", "3");
                conn = conn2;
            } else {
                conn.addPair("Status", "4");
            }
        } else if (msgId.equals("24")) { //Activate Component
            System.out.println("Activate Component");
        } else if (msgId.equals("25")) { //Deactivate component
            System.out.println("Deactivate Component");
        } else if (msgId.equals("26")) { //Acknowledgement
            conn.addPair("MessageType", "Confirm");
        } else if (msgId.equals("701")) { //Cast Vote - COMPLETE
            conn.addPair("Receiver", kvList.getValue("Sender"));
            conn.addPair("MessageCode", "711");
            if(kvList.getValue("Subject").toLowerCase().trim().equals("cs1631 vote")){
                String vote = lines[0].trim();
                System.out.println(vote);
                Integer status = checkPoster(vote) ? (checkVoter(kvList.getValue("From")) ? 3 : 1) : 2;
                if(status == 3) {
                    voterTable.put(kvList.getValue("From"), kvList.getValue("From"));
                    tallyTable.put(vote, tallyTable.get(vote) + 1);
                    conn.addPair("Forward", "True");
                }
                conn.addPair("Status", status.toString());
            } else {
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("703")) { //Add Poster - COMPLETE
            conn.addPair("Receiver", "GUI");
            conn.addPair("Status", "3");
            conn.addPair("MessageCode", msgId);

            String id = kvList.getValue("Id");
            tallyTable.put(id, 0);

            conn.addPair("Forward", "True");
        }
        return conn;
    }

    private boolean checkPoster(String vote){
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

    private String getPasscode(String[] lines){
        for(int i = 0; i< lines.length; i++){
            if(lines[i].toLowerCase().startsWith("passcode")){
                return lines[i].split(":")[1].trim();
            }
        }
        return "";
    }

    private String getIdFromSubject(KeyValueList kvList){
        if(kvList.getValue("Subject").toLowerCase().equals("initialize tally table")){
            return "703";
        } else if(kvList.getValue("Subject").toLowerCase().equals("request report")){
            return "702";
        } else if(kvList.getValue("Subject").toLowerCase().equals("end voting") || kvList.getValue("Subject").toLowerCase().equals("re: need confirmation")){
            return "22";
        } else {
            return kvList.getValue("MessageCode");
        }
    }
}
