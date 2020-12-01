package undefined.email

import java.util.Properties

import javax.inject.Inject
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient
import javax.mail.{Message, Session}
import javax.mail.internet.{InternetAddress, MimeMessage}
import play.api.Configuration
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart
import software.amazon.awssdk.services.ses.model.RawMessage
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

import javax.mail.internet.MimeMultipart
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.auth.credentials.{AwsCredentials, AwsSessionCredentials, StaticCredentialsProvider}

class SendEmailImpl @Inject()(val config: Configuration) extends SendEmail {
  def apply(to: String, subject: String, content: String): Unit = {

    val cred =  new AwsCredentials{
      override def accessKeyId(): String = config.get[String]("rocketFont.email.aws.iam.accessKeyId")
      override def secretAccessKey(): String = config.get[String]("rocketFont.email.aws.iam.accessKeySecret")
    }

    val region = Region.AP_NORTHEAST_2
    val sesClient = SesClient.builder()
      .credentialsProvider(StaticCredentialsProvider.create(cred))
      .region(region)
      .build()

    val mailFrom = config.get[String]("rocketFont.email.from")

    val session = Session.getDefaultInstance(new Properties())
    val message = new MimeMessage(session)
    message.setSubject(subject, "UTF-8")
    message.setFrom(mailFrom)
    message.getFrom
     val rt: Message.RecipientType = Message.RecipientType.TO
    message.setRecipients(rt, to)

    // Create a multipart/alternative child container.
    val msgBody = new MimeMultipart("alternative")

    // Create a wrapper for the HTML and text parts.
    val wrap = new MimeBodyPart

    // Define the text part.
    val textPart = new MimeBodyPart
    textPart.setContent(content, "text/plain; charset=UTF-8")

    // Define the HTML part.
    // Add the text and HTML parts to the child container.
    msgBody.addBodyPart(textPart)

    // Add the child container to the wrapper object.
    wrap.setContent(msgBody)

    val msg = new MimeMultipart("mixed")
    message.setContent(msg)
    msg.addBodyPart(wrap);


    val outputStream = new ByteArrayOutputStream
    message.writeTo(outputStream)
    val buf = ByteBuffer.wrap(outputStream.toByteArray)
    val arr: Array[Byte] = new Array[Byte](buf.remaining)
     buf.get(arr)
    val data = SdkBytes.fromByteArray(arr)

    val rawMessage = RawMessage.builder()
      .data(data)
      .build()

    val rawEmailRequest = SendRawEmailRequest.builder.rawMessage(rawMessage).build
    sesClient.sendRawEmail(rawEmailRequest)
  }


}
