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

        msgId = kvList.getValue("MessageType");

        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "711");
        conn.addPair("Sender", "SISServer");
        conn.addPair("Receiver", "InterfaceServer");

        //Create Component
        if (msgId == 21) {

        } else if (msgId == 22) { //Kill component
            System.exit(0);
        } else if (msgId == 23) { //Connect to server

        } else if (msgId == 24) { //Activate Component

        } else if (msgId == 25) { //Deactivate component

        } else if (msgId == 26) { //Acknowledgement
            conn.addPair("MessageType", "Confirm");
        } else if (msgId == 701) { //Cast Vote
            String voter = kvList.getValue("Voter");
            String vote = kvList.getValue("Vote");

            if(checkVoter(voter)) {
                voterTable.put(Voter, vote);
            }
            placeVote(vote);

            //Crafting the message to acknowledge the vote
            conn.addPair("MessageType", "711");
        } else if (msgId == 702) { //Request Report
            conn.addPair("MessageType", "711")
            for(Map.Entry<String, Integer> entry : tallyTable.entrySet()){
                conn.addPair(entry.getKey(), entry.getValue())
            }
        } else if (msgId == 703) { //Initialize Tally Table
            tallyTable = new HashMap<>();
            conn.addPair("MessageType", "Confirm");
        } else if (msgId == 711) { //Acknowledge Vote (this probably doesn't need its own case
            conn.addPair("MessageType", "Confirm");
        } else if (msgId == 712) { //Acknowledge Request Report

        } else {
            conn.addPair("MessageType", "Confirm");
        }

        return conn;


    }

    private boolean checkVoter(String voter){
        if(voterTable.containsKey(voter)){
            return false;
        }
        return true;
    }

    private void placeVote(String vote){
        if(tallyTable.containsKey(vote)){
            tallyTable.put(vote, tallyTable.get(vote)+1);
        } else {
            tallyTable.put(vote, 1);
        }
    }
}