import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

class MyComponent implements ComponentBase {
    static boolean kill = false;
    
    static String passcode = "m1k3+b3n";
    HashMap<String, String> voterTable = new HashMap<>();
    HashMap<String, Integer> tallyTable = new HashMap<>();

    public KeyValueList processMsg(KeyValueList kvList) {
        String msgId = getIdFromSubject(kvList);
        KeyValueList conn = new KeyValueList();

        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Alert");
        conn.addPair("Sender", "InterfaceServer");
        conn.addPair("Receiver", "InputProcessor");


        String messageBody = kvList.getValue("Body");
        String[] lines = messageBody.split("\n");
        String foundPasscode = "";

        foundPasscode = getPasscode(lines);

        //Create Component
        if (msgId.equals("21")) {
            System.out.println("Create Component");
        } else if (msgId.equals("22")) { //Kill component (done)
            conn.addPair("MessageCode", "712");
            conn.addPair("Status", "3");
            System.out.println("HERE");
            if(foundPasscode.equals(passcode)){
                if(!kill){
                    conn.addPair("Kill", "False");
                    kill = true;
                } else{
                    conn.addPair("Kill", "True");
                }
            } else {
                conn.addPair("Status", "4");
            }
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
        } else if (msgId.equals("701")) { //Cast Vote (done)
            conn.addPair("MessageCode", "711");
            if(kvList.getValue("Subject").toLowerCase().trim().equals("cs1631 vote")){
                String vote = lines[0].trim();
                System.out.println(vote);
                Integer status = checkPoster(vote) ? (checkVoter(kvList.getValue("From")) ? 3 : 1) : 2;
                if(status == 3) {
                    voterTable.put(kvList.getValue("From"), kvList.getValue("From"));
                    tallyTable.put(vote, tallyTable.get(vote)+1);
                }
                conn.addPair("Status", status.toString());
            } else {
                conn.addPair("Status", "2");
            }
        } else if (msgId.equals("702")) { //Request Report (needs to be tested)
            conn.addPair("MessageCode", "712");
            if(foundPasscode.equals(passcode)){
                int numWinners = getNumWinners(lines);
                int i = 0;
                String values = "";
                for(Map.Entry<String, Integer> entry : tallyTable.entrySet()){
                    if(i < numWinners){
                        values += entry.getKey() + ',' + entry.getValue().toString() + ';';
                    } else {
                        break;
                    }
                }
                values = values.substring(0, values.length() - 1);
                conn.addPair("Values", values);
                conn.addPair("Status", "3");
            } else {
                conn.addPair("Status", "4");
            }
        } else if (msgId.equals("703")) { //Initialize Tally Table
            conn.addPair("MessageCode", "712");
            if(foundPasscode.equals(passcode)){
                int k = 1;
                while(lines[k] != null && !lines[k].toLowerCase().startsWith("posters") && k < lines.length){
                    k++;
                }
                if(k<lines.length){
                    tallyTable = new HashMap<>();
                    String[] posters = lines[k].split(":")[1].trim().split("[;,]");
                    for(int i = 0; i< posters.length; i++){
                        tallyTable.put(posters[i], 0);
                    }
                    conn.addPair("Status", "3");
                } else{
                    conn.addPair("Status", "5");
                }
            } else {
                conn.addPair("Status", "4");
            }
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

    private Integer getNumWinners(String[] lines){
        for(int i = 0; i<lines.length; i++){
            if(lines[i].toLowerCase().startsWith("winners")){
                try{
                    int result = Integer.parseInt(lines[i].split(":")[1].trim());
                    if(result > lines.length){
                        result = lines.length;
                    }
                    return result;
                } catch(Exception e){
                    return lines.length;
                }
            }
        }
        return lines.length;
    }
}
