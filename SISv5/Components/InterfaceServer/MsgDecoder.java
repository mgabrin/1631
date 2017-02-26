import java.io.*;
import java.net.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

        String message = reader.readLine();
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