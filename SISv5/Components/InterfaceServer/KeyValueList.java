import java.io.*;
import java.net.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class KeyValueList {
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