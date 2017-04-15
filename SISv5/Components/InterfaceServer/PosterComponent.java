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
        } catch(Exception e){
            System.out.println(e);
        }
        poll();
    }

    public static void poll()
    {
      try
      {
        HashMap<String, Poster> posters = new HashMap<String, Poster>();
        HashMap<String, Integer> categories2016 = new HashMap<>();
        HashMap<String, Integer> categories2017 = new HashMap<>();
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
              System.out.println(category);
              if(year.equals("2016"))
              {
                if(!categories2016.containsKey(category))
                  categories2016.put(category, 0);
              }
              else
              {
                if(!categories2017.containsKey(category))
                  categories2017.put(category, 0);
              }

            } catch(NumberFormatException nfe) {
              //Error message?
            }
          }

          else if(code.equalsIgnoreCase("701")) //Vote
          {
            try {
              System.out.println("Got vote");
              String id = vote.getValue("Id");

              Poster p = posters.get(id);
              String category = p.getCategory();
              String year = p.getYear();
              
              System.out.println(id);
              System.out.println(category);
              System.out.println(year);

              if(year.equals("2016"))
                categories2016.put(category, categories2016.get(category) + 1);
              else{
                System.out.println("Found a 2017 entry");
                categories2017.put(category, categories2017.get(category) + 1);
              }
            } catch(NumberFormatException nfe) {
              //Error message?
            }
          }

          else if(code.equalsIgnoreCase("22")) //End voting
          {
            String cats = "";
            for(Map.Entry<String, Integer> entry : categories2016.entrySet())
              cats += entry.getKey() + ',' + entry.getValue() + ",2016;";
            for(Map.Entry<String, Integer> entry : categories2017.entrySet())
              cats += entry.getKey() + ',' + entry.getValue() + ",2017;";

            cats = cats.substring(0, cats.length() - 1);
            vote.addPair("Categories", cats);
            vote.addPair("Receiver", "GUI");
            vote.addPair("Sender", "PosterComponent");
            mEncoder.sendMsg(vote);
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
        } else if(kvList.getValue("MessageCode").equals("711")){
          return "701"; 
        } else if(kvList.getValue("Subject").toLowerCase().equals("request report")){
            return "702";
        } else if(kvList.getValue("Subject").toLowerCase().equals("end voting") || kvList.getValue("Subject").toLowerCase().equals("re: need confirmation") || kvList.getValue("MessageCode").equals("712")){
            return "22";
        } else {
            return kvList.getValue("MessageCode");
        }
    }
}
