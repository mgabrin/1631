package com.example.michaelgabrin.cs1631_semester_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by michaelgabrin on 2/16/17.
 */
public class MsgDecoder {
    private BufferedReader bufferIn;
    private final String delimiter="$$$";

    public MsgDecoder(InputStream in){
        bufferIn = new BufferedReader(new InputStreamReader(in));
    }

/*
get String and output KeyValueList
*/

    public KeyValueList getMsg() throws IOException {
        String strMsg= bufferIn.readLine();

        if (strMsg==null) return null;

        KeyValueList kvList = new KeyValueList();
        StringTokenizer st = new StringTokenizer(strMsg,delimiter);
        while (st.hasMoreTokens()) {
            kvList.addPair(st.nextToken(),st.nextToken());
        }
        return kvList;
    }

}
