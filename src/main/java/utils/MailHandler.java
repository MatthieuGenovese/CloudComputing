package Utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailHandler
{
  public void sendSimpleMail(String user,String mail,String video) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("fezai-ahmed@hotmail.fr", "CLOUD"));
      msg.addRecipient(Message.RecipientType.TO,
        new InternetAddress(mail, user));
      msg.setSubject("the video conversion is done with success");
      msg.setText("your video converted is :  " + video);
      Transport.send(msg);
    } catch (AddressException e) {

      // ...
    } catch (MessagingException e) {
      // ...
    } catch (UnsupportedEncodingException e) {
      // ...
    }
  }



}
