/*

Communication Interface for Virtual Classroom Components.
Complied in JDK 1.4.2
Usage:

Step1: Create your component which implemnts Interface
ComponentBase.

Step2: Replace the ComponentMy with your class name in
InterfaceServer::main().

Step3: start up Interface Server by
java InterfaceServer

Step4: Use Virtual remote which is also provided on the
web to send message and get the feedback message.

*/


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
interface ComponentBase:
The interface you have to implement in your component
*/
interface ComponentBase{
   public KeyValueList processMsg(KeyValueList kvList);
}

/*
Class InterfaceServer
Set up a socket server waiting for the remote to connect.
*/

public class CreateInterfaceServer{
	static Socket client;
	static MsgDecoder mDecoder;
	static MsgEncoder mEncoder;

   public static void main(String[] args) throws Exception{
    ComponentBase compMy= new MyComponent();
    client = connect();

    try{
      mDecoder= new MsgDecoder(client.getInputStream());
      mEncoder= new MsgEncoder(client.getOutputStream());

      KeyValueList conn = new KeyValueList();
      conn.addPair("Scope", "SIS.Scope1");
      conn.addPair("MessageType", "Connect");
      conn.addPair("Role", "Basic");
      conn.addPair("Name", "InterfaceServer");
      mEncoder.sendMsg(conn);

      KeyValueList kvInput,kvOutput;
      while(true)
      {
        kvInput=mDecoder.getMsg();
        if (kvInput!=null) {
          System.out.println("Incomming Message:\n");
          System.out.println(kvInput);
          KeyValueList kvResult=compMy.processMsg(kvInput);
          System.out.println("Outgoing Message:\n");
          System.out.println(kvResult);
          mEncoder.sendMsg(kvResult);
          if(kvResult.getValue("Kill").equals("True")){
              System.exit(0);
          }
       }
      }
   }
   catch (Exception e){
   	e.printStackTrace();
  }
 }
static Socket connect() throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 53217);
        return socket;
    }
}




