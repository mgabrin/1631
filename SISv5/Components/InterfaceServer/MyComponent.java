import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

class MyComponent implements ComponentBase {
    HashMap<String, String> voterTable = new HashMap<>();
    HashMap<String, Integer> tallyTable = new HashMap<>();


    public KeyValueList processMsg(KeyValueList kvList) {
        System.out.println("Here is kvList:");
        System.out.println(kvList);

        String msgId = kvList.getValue("MessageCode");

        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Confirm");
        conn.addPair("Sender", "InterfaceServer");
        conn.addPair("Receiver", "Remote");

        //Create Component
        if (msgId.equals("21")) {
            System.out.println("Hello");
        } else if (msgId.equals("22")) { //Kill component (done)
            System.exit(0);
        } else if (msgId.equals("23")) { //Connect to server (done)
            KeyValueList conn2 = new KeyValueList();
            conn2.addPair("Scope", "SIS.Scope1");
            conn2.addPair("MessageType", "Connect");
            conn2.addPair("Role", "Basic");
            conn2.addPair("Name", "InputProcessor");
            conn = conn2;
        } else if (msgId.equals("24")) { //Activate Component
            System.out.println("Hello");
        } else if (msgId.equals("25")) { //Deactivate component
            System.out.println("Hello");
        } else if (msgId.equals("26")) { //Acknowledgement
            conn.addPair("MessageType", "Confirm");
        } else if (msgId.equals("701")) { //Cast Vote (done)
            System.out.println("Casting a vote!");
            String voter = kvList.getValue("Voter");
            String vote = kvList.getValue("Vote");
            if(checkVoter(voter)) {
                voterTable.put(voter, vote);
            }
            int status = placeVote(vote);

            conn.addPair("MessageCode", "711");
            conn.addPair("Status", status.toString())
        } else if (msgId.equals("702")) { //Request Report (needs to be tested)
            conn.addPair("MessageCode", "711");
            for(Map.Entry<String, Integer> entry : tallyTable.entrySet()){
                conn.addPair(entry.getKey(), entry.getValue().toString());
            }
        } else if (msgId.equals("703")) { //Initialize Tally Table (done)
            tallyTable = new HashMap<>();
            conn.addPair("MessageCode", "711");
        } else if (msgId.equals("711")) { //Acknowledge Vote (this probably doesn't need its own case
            System.out.println("Hello");
            //conn.addPair("MessageType", "Confirm");
        } else if (msgId.equals("712")) { //Acknowledge Request Report
            System.out.println("Hello");
        } else {
            System.out.println("Hello");
            //conn.addPair("MessageType", "Confirm");
        }

        return conn;


    }

    private boolean checkVoter(String voter){
        if(voterTable.containsKey(voter)){
            return false;
        }
        return true;
    }

    private int placeVote(String vote){
        if(tallyTable.containsKey(vote)){
            tallyTable.put(vote, tallyTable.get(vote)+1);
            return 0;
        }
        return false;
    }
}