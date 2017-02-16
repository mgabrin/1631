package com.example.michaelgabrin.cs1631_semester_project;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by michaelgabrin on 2/16/17.
 */
public class InterfaceServer {
    public static final int port=7999;

    public static void main(String[] args) throws Exception
    {
        ServerSocket server = new ServerSocket(port);

    /*
    You need to create your component here
    */

        ComponentBase compMy = new ComponentMy();
        Socket client = server.accept();
        try{
            MsgDecoder mDecoder= new MsgDecoder(client.getInputStream());
            MsgEncoder mEncoder= new MsgEncoder();
            KeyValueList kvInput,kvOutput;
            do
            {
                kvInput=mDecoder.getMsg();
                if (kvInput!=null) {
                    System.out.println("Incomming Message:\n");
                    System.out.println(kvInput);
                    KeyValueList kvResult=compMy.processMsg(kvInput);
                    System.out.println("Outgoing Message:\n");
                    System.out.println(kvResult);
                    mEncoder.sendMsg(kvResult,client.getOutputStream());
                }
            }
            while (kvInput!=null);
        }
        catch (SocketException e){
            System.out.println("Connection was Closed by Client");
        }
    }
}
