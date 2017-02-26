import java.util.Properties;
import java.util.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.concurrent.TimeUnit;
import javax.mail.search.FlagTerm;
import javax.mail.*;
import java.io.*;
import java.net.*;
public class CreateInputProcessor {

    static Socket socket;
    static MsgEncoder mEncoder;

    public static void main(String[] args) {
        String host = "pop.gmail.com";
        String mailStoreType = "pop3";
        String username = "cs1631project@gmail.com";
        String password = "m1k3+b3n";

        KeyValueList conn = new KeyValueList();
        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Connect");
        conn.addPair("Role", "Basic");
        conn.addPair("Name", "InputProcessor");


        try{
            socket = new Socket("127.0.0.1", 53217);
            mEncoder = new MsgEncoder(socket.getOutputStream());
            mEncoder.sendMsg(conn);
        } catch(Exception e){
            System.out.println(e);
        }
        check(host, mailStoreType, username, password);
    }

    public static void check(String host, String storeType, String user, String password) {
        try {


            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator(){
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(user, password);
                }
            });

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");
            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            boolean exit = false;
            while(true) {
                Message[] messages = emailFolder.getMessages();

                for (int i = 0, n = messages.length; i < n; i++) {
                    Message message = messages[i];

                    if(message.getSubject().equals("Test")){
                        System.out.println("Found our guy!");

                        KeyValueList newVote = new KeyValueList();
                        newVote.addPair("Scope", "SIS.Scope1");
                        newVote.addPair("MessageType", "701");
                        newVote.addPair("Receiver", "InterfaceServer");
                        newVote.addPair("Role","Basic");
                        newVote.addPair("EmailAddress", "test@gmail.com");
                        newVote.addPair("Vote", "1");

                        mEncoder.sendMsg(newVote);
                        exit = true;
                    }
                }

                if(exit){
                    break;
                } else {
                    TimeUnit.SECONDS.sleep(3);
                }
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}