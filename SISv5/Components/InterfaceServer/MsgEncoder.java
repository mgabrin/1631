import java.io.*;
import java.net.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


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