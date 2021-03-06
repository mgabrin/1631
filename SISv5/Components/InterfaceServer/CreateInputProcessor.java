import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
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
        String username = "cs1631voting@gmail.com";
        String password = "m1k3+b3n";
        String sendHost = "smtp.gmail.com";

        KeyValueList conn = new KeyValueList();
        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Connect");
        conn.addPair("Role", "Basic");
        conn.addPair("Name", "InputProcessor");


        try{
            socket = new Socket("127.0.0.1", 53217);
            mEncoder = new MsgEncoder(socket.getOutputStream());
            mDecoder = new MsgDecoder(socket.getInputStream());
            mEncoder.sendMsg(conn);
            System.out.println(mDecoder.getMsg());
        } catch(Exception e){
            System.out.println(e);
        }
        check(host, mailStoreType, username, password, sendHost);
    }

    public static void check(String host, String storeType, String user, String password, String sendHost) {
        try
        {
          //create properties field
          Properties properties = new Properties();

          properties.put("mail.pop3.host", host);
          properties.put("mail.pop3.port", "995");
          properties.put("mail.pop3.starttls.enable", "true");
          properties.put("mail.pop3.ssl.enable", "true");

          properties.put("mail.smtp.host", sendHost);
          properties.put("mail.smtp.port", "587");
          properties.put("mail.smtp.auth", "true");
          properties.put("mail.smtp.starttls.enable", "true");

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

              KeyValueList response = mDecoder.getMsg();

              String code = response.getValue("MessageCode");
              String status = response.getValue("Status");
              String responseSubject = null;
              String responseText = null;

              if(code.equals("711") && !response.getValue("Kill").equals("True"))
              {
                if(status.equals("3"))
                {
                  responseSubject = "Vote Recorded";
                  responseText = "Your vote was successfully recorded.\nThanks for voting!";
                }
                else
                {
                  responseSubject = "Vote Error";
                  if(status.equals("1"))
                    responseText = "Sorry, you have already voted.\nYour second vote was not recorded.";
                  else
                    responseText = "Sorry, you voted for an invalid candidate.\nPlease vote again for a valid candidate.";
                }
              }

              /*else if(code.equals("712"))
              {
                if(status.equals("3"))
                {
                  if(response.getValue("Kill").equals("False"))
                  {
                    responseSubject = "Need Confirmation";
                    responseText = "Reply with Admin passcode to close voting.";
                  }

                  else
                  {
                    responseSubject = "Success";
                    responseText = "Your administrative action was successful.\n";
                    if(!response.getValue("Values").equals(""))
                    {
                      String[] allCands = response.getValue("Values").split(";");
                      String[] indCand = null;
                      for(int k = 0; k < allCands.length; k++)
                      {
                        indCand = allCands[k].split(",");
                        responseText += "\nCandidate: " + indCand[0];
                        responseText += "\nVotes: " + indCand[1] + "\n";
                      }
                    }
                  }
                }

                else
                {
                  responseSubject = "Error";
                  if(status.equals("4"))
                    responseText = "Your password was invalid.\nPlease try again.";
                  else
                    responseText = "The candidate list was invalid.\nPlease provide a valid list delimeted by semicolons.";
                }
              }*/

              else
                continue;

              MimeMessage msg = new MimeMessage(emailSession);
              msg.setFrom(new InternetAddress(user));
              msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));

              if(response.getValue("Kill").equals("True")){
                  msg.setSubject("Voting is Over");
                  msg.setText("Sorry, voting has ended.");
                  System.out.println("Sending");
                  Transport.send(msg);
                  emailFolder.close(false);
                  store.close();
                  System.exit(0);
              }

              msg.setSubject(responseSubject);
              msg.setText(responseText);
              System.out.println("Sending");
              Transport.send(msg);
              System.out.println("Sent");


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
