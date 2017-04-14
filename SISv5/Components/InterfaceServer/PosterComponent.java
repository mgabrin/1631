import java.util.*;
import java.io.*;
import java.net.*;
public class PosterComponent 
{
    static Socket socket;
    static MsgEncoder mEncoder;
    static MsgDecoder mDecoder;

    public static void main(String[] args)
    {
        KeyValueList conn = new KeyValueList();
        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Connect");
        conn.addPair("Role", "Basic");
        conn.addPair("Name", "PosterComponent");

        try{
            socket = new Socket("127.0.0.1", 53217);
            mEncoder = new MsgEncoder(socket.getOutputStream());
            mDecoder = new MsgDecoder(socket.getInputStream());
            mEncoder.sendMsg(conn);
            System.out.println(mDecoder.getMsg());
        } catch(Exception e){
            System.out.println(e);
        }
        poll(conn);
    }

    public static void poll(KeyValueList conn)
    {
      try
      {
        HashMap<String, Poster> posters = new HashMap<String, Poster>();
        HashMap<String, Integer> categories = new HashMap<>();
        while(true)
        {
          KeyValueList vote = mDecoder.getMsg();

          String code = getIdFromSubject(vote);
          String status = vote.getValue("Status");

          if(code.equalsIgnoreCase("703")) //AddPoster
          {
            try {
              String id = vote.getValue("Id");
              String year = vote.getValue("Year");
              String category = vote.getValue("Category");
              posters.put(id, new Poster(year, category, 0));

              if(!categories.containsKey(category))
                categories.put(category, 0);
            } catch(NumberFormatException nfe) {
              //Error message?
            }
          }

          else if(code.equalsIgnoreCase("701")) //Vote
          {
            try {
              String id = vote.getValue("Id");

              Poster p = posters.get(id);
              String category = p.getCategory();
              int newId = categories.get(category) + 1;
              categories.put(category, newId);
            } catch(NumberFormatException nfe) {
              //Error message?
            }
          }

          else if(code.equalsIgnoreCase("22")) //End voting
          {
            String cats = "";
            for(Map.Entry<String, Integer> entry : categories.entrySet())
              cats += entry.getKey() + ',' + entry.getValue() + ';';

            cats = cats.substring(0, cats.length() - 1);
            conn.addPair("Categories", cats);
            mEncoder.sendMsg(conn);
            System.exit(0);
          }
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
    }

    private static String getIdFromSubject(KeyValueList kvList){
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
