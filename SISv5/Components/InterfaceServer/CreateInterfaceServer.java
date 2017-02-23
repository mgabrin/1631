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
Class KeyValueList:
List of (Key, Value) pair--the basic format of message
Keys: MsgID and Description are required for any messages
MsgID 0-999 is reserved for system use.
You MsgID could start from 1000.
*/
/*
class KeyValueList{
	private Vector Keys;
	private Vector Values;

	public KeyValueList(){
		Keys=new Vector();
		Values=new Vector();
	}


	private int lookupKey(String strKey){
	for(int i=0;i<Keys.size();i++){
	String k=(String) Keys.elementAt(i);
	if (strKey.equals(k)) return i;
	}
	return -1;
	}


	public boolean addPair(String strKey,String strValue){
	return (Keys.add(strKey)&& Values.add(strValue));
	}



	public String getValue(String strKey){
		int index=lookupKey(strKey);
		if (index==-1) return null;
			return (String) Values.elementAt(index);
	}


	public String toString(){
		String result = new String();
		for(int i=0;i<Keys.size();i++){
			result+=(String) Keys.elementAt(i)+":"+(String) Values.elementAt(i)+"\n";
		}
		return result;
	}

	public int size(){ return Keys.size();}

static final String pattern = "\\$+";
public static KeyValueList decodedKV(String message) {
		KeyValueList kvList = new KeyValueList();

		String[] parts = message.split(pattern);
		int validLen = parts.length;
		if (validLen % 2 != 0) {
			--validLen;
		}
		if (validLen < 1) {
			return kvList;
		}

		for (int i = 0; i < validLen; i += 2) {
			kvList.addPair(parts[i], parts[i + 1]);
		}
		return kvList;
	}

private Map<String, String> map;
static final String delim = "$$$";
public String encodedString() {

		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey() + delim + entry.getValue() + delim);
		}
		// X$$$Y$$$, minimum
		builder.append(")");
		return builder.toString();
	}

	public String keyAt(int index){ return (String) Keys.elementAt(index);}
	public String valueAt(int index){ return (String) Values.elementAt(index);}
}*/


class KeyValueList {
	// interal map for the message <property name, property value>, key and
	// value are both in String format
	private Map<String, String> map;

	// delimiter for encoding the message
	static final String delim = "$$$";

	// regex pattern for decoding the message
	static final String pattern = "\\$+";

	/*
	 * Constructor
	 */
	public KeyValueList() {
		map = new HashMap<>();
	}

	/*
	 * Add one property to the map
	 */
	public boolean addPair(String key, String value) {
		key = key.trim();
		value = value.trim();
		if (key == null || key.length() == 0 || value == null
				|| value.length() == 0) {
			return false;
		}
		map.put(key, value);
		return true;
	}

	public String removePair(String key) {
		return map.remove(key);
	}

	// /*
	// * extract a List containing all the input message IDs in Integer format
	// * (specifically designed for message 20)
	// */
	// public List<Integer> InputMessages() {
	// int i = 1;
	// List<Integer> list = new ArrayList<>();
	// String m = map.get("InputMsgID" + i);
	// while (m != null) {
	// list.add(Integer.parseInt(m));
	// ++i;
	// m = map.get("InputMsgID" + i);
	// }
	// return list;
	// }
	//
	// /*
	// * extract a List containing all the output message IDs in Integer format
	// * (specifically designed for message 20)
	// */
	// public List<Integer> OutputMessages() {
	// int i = 1;
	// List<Integer> list = new ArrayList<>();
	// String m = map.get("OutputMsgID" + i);
	// while (m != null) {
	// list.add(Integer.parseInt(m));
	// ++i;
	// m = map.get("OutputMsgID" + i);
	// }
	// return list;
	// }

	/*
	 * encode the KeyValueList into a String
	 */
	/*
	 * encode the KeyValueList into a String
	 */
	public String encodedString() {

		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey() + delim + entry.getValue() + delim);
		}
		// X$$$Y$$$, minimum
		builder.append(")");
		return builder.toString();
	}

	/*
	 * decode a message in String format into a corresponding KeyValueList
	 */
	public static KeyValueList decodedKV(String message) {
		KeyValueList kvList = new KeyValueList();

		String[] parts = message.split(pattern);
		int validLen = parts.length;
		if (validLen % 2 != 0) {
			--validLen;
		}
		if (validLen < 1) {
			return kvList;
		}

		for (int i = 0; i < validLen; i += 2) {
			kvList.addPair(parts[i], parts[i + 1]);
		}
		return kvList;
	}

	/*
	 * get the property value based on property name
	 */
	public String getValue(String key) {
		String value = map.get(key);
		if (value != null) {
			return value;
		}else{
			return "";
		}
	}

	/*
	 * get the number of properties
	 */
	public int size() {
		return map.size();
	}

	/*
	 * toString for printing
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		for (Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
		}
		return builder.toString();
	}
}

/*
Class MsgEncoder:
Serialize the KeyValue List and Send it out to a Stream.
*/
/*
class MsgEncoder{
	private PrintStream printOut;

	private final String delimiter="$$$";

	// used for writing Strings
	public MsgEncoder(OutputStream out){
		printOut= new PrintStream(out);
	}


	public void sendMsg(KeyValueList kvList) throws IOException{
//		PrintStream printOut= new PrintStream(out);
		if (kvList==null) return;
			String outMsg= new String();
		for(int i=0; i<kvList.size();i++){
			if (outMsg.equals(""))
			outMsg=kvList.keyAt(i)+delimiter + kvList.valueAt(i);
			else
			outMsg+=delimiter+kvList.keyAt(i)+delimiter + kvList.valueAt(i);
		}
		System.out.println(outMsg);
		printOut.println(outMsg);
		System.out.println(printOut.checkError());
		printOut.flush();
	}
}
*/
class MsgEncoder {

	// used for writing Strings
	private PrintStream writer;

	/*
	 * Constructor
	 */
	public MsgEncoder(OutputStream out) throws IOException {
		writer = new PrintStream(out);
	}

	/*
	 * encode the KeyValueList that represents a message into a String and send
	 */
	public void sendMsg(KeyValueList kvList) throws IOException {
		if (kvList == null || kvList.size() < 1) {
			return;
		}

		writer.println(kvList.encodedString());
		writer.flush();
	}
}


/*
Class MsgDecoder:
Get String from input Stream and reconstruct it to
a Key Value List.
*/
/*
class MsgDecoder {

	private BufferedReader bufferIn;
	private final String delimiter="$$$";

	public MsgDecoder(InputStream in){
		bufferIn = new BufferedReader(new InputStreamReader(in));
	}


	public KeyValueList getMsg() throws IOException{

		  String n = null;
		  String strMsg = "";
		  while(true) {
		    System.out.println("HEllo");
		    n = bufferIn.readLine();
		    System.out.println(n);
		    if(n == null)
		      break;

		    strMsg += n;
		    //System.out.println(strMsg);
		  }
		//String strMsg= bufferIn.readLine();
		//System.out.println(strMsg);

		if (strMsg==null) return null;

		KeyValueList kvList = new KeyValueList();
		StringTokenizer st = new StringTokenizer(strMsg,delimiter);
		while (st.hasMoreTokens()) {
			kvList.addPair(st.nextToken(),st.nextToken());
		}
		return kvList;
	}

}*/

class MsgDecoder {
	// used for reading Strings
	private BufferedReader reader;

	/*
	 * Constructor
	 */
	public MsgDecoder(InputStream in) throws IOException {
		reader = new BufferedReader(new InputStreamReader(in));
	}

	/*
	 * read and decode the message into KeyValueList
	 */
	public KeyValueList getMsg() throws Exception {
		KeyValueList kvList = new KeyValueList();
		StringBuilder builder = new StringBuilder();

System.out.println("111111");
		String message = reader.readLine();
System.out.println("2222222222");
		if (message != null && message.length() > 2) {

			builder.append(message);

			while (message != null && !message.endsWith(")")) {
				message = reader.readLine();
				builder.append("\n" + message);
				System.out.println("Looping");
			}

			System.out.println("Out of loop");

			kvList = KeyValueList.decodedKV(builder.substring(1, builder.length() - 1));
		}
		return kvList;
	}
}



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
	static MsgDecoder mDecoder;//= new MsgDecoder(client.getInputStream());
	static MsgEncoder mEncoder;//= new MsgEncoder(client.getOutputStream());

   public static final int port=7999;

   public static void main(String[] args) throws Exception{
    System.out.println("in");
    //ServerSocket server = new ServerSocket(port);
    System.out.println("1");
    ComponentBase compMy= new MyComponent();
    System.out.println("2");
    //Socket client = server.accept();
    client = connect();//new Socket("127.0.0.1", 53217);

    System.out.println(client);
    System.out.println("3");
    try{
      mDecoder= new MsgDecoder(client.getInputStream());
      System.out.println("3a");
      mEncoder= new MsgEncoder(client.getOutputStream());

      KeyValueList conn = new KeyValueList();
      conn.addPair("Scope", "SIS.Scope1");
      conn.addPair("MessageType", "Connect");
      conn.addPair("Role", "Basic");
      conn.addPair("Name", "InterfaceServer");
      mEncoder.sendMsg(conn);

      System.out.println("3b");
      KeyValueList kvInput,kvOutput;
      while(true)
      {
        System.out.println("3c");
        kvInput=mDecoder.getMsg();
        System.out.println("4");
        if (kvInput!=null) {
          System.out.println("Incomming Message:\n");
          System.out.println(kvInput);
          KeyValueList kvResult=compMy.processMsg(kvInput);
          System.out.println("Outgoing Message:\n");
          System.out.println(kvResult);
          mEncoder.sendMsg(kvResult);
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



class MyComponent implements ComponentBase{
 public KeyValueList processMsg(KeyValueList kvList) {
	 KeyValueList conn = new KeyValueList();
	 conn.addPair("Scope", "SIS.Scope1");
	 conn.addPair("MessageType", "Confirm");
	 conn.addPair("Sender", "SISServer");
	 conn.addPair("Receiver", "InterfaceServer");
	 return conn;
 }
}
