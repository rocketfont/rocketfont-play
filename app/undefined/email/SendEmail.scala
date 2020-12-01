package undefined.email

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[SendEmailImpl])
trait SendEmail {
  def apply(to: String, title : String, content : String): ()
}
