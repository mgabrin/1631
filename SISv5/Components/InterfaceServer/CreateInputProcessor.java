import java.util.*;
import javax.mail.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
public class CreateInputProcessor {

    static Socket socket;
    static MsgEncoder mEncoder;
    static MsgDecoder mDecoder;

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
        try
        {
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

          while(true)
          {
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            for (int i = 0, n = messages.length; i < n; i++)
            {
              Message message = messages[i];
              String subject = message.getSubject();

              System.out.println(subject);

              Address[] fromAddresses = message.getFrom();
              String from = fromAddresses[0].toString();

              System.out.println(from);

              String body = null;
              if(message.getContentType().startsWith("multipart"))
              {
                Multipart mp = (Multipart)message.getContent();
                Part p;
                for (int k = 0; k < mp.getCount(); k++)
                {
                  p = mp.getBodyPart(k);
                  if(p.getContentType().startsWith("text/plain"))
                  {
                    body = (String)p.getContent();
                    break;
                  }
                }
              }

              else if(message.getContentType().startsWith("text/plain"))
                body = (String)message.getContent();

              else
                continue;

              System.out.println("Body:" + body);

              KeyValueList newVote = new KeyValueList();
              newVote.addPair("Scope", "SIS.Scope1");
              newVote.addPair("MessageType", "Alert");
              newVote.addPair("Sender", "InputProcessor");
              newVote.addPair("Receiver", "InterfaceServer");
              newVote.addPair("MessageCode", "701");
              newVote.addPair("From", from);
              newVote.addPair("Subject", subject);
              newVote.addPair("Body", body);

              mEncoder.sendMsg(newVote);

              //KeyValueList response = null;
              //response = mDecoder.getMsg();
            }
            emailFolder.close(false);
            TimeUnit.SECONDS.sleep(3);
          }
          //close the store and folder objects

          //store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
