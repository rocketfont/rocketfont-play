package controllers.member

import play.api.libs.json.{Json, OWrites, Reads}
import com.github.t3hnar.bcrypt._

case class MemberSignUpLoginJson(email : String, password : String){
  private val emailSplit =email.split('@')
  require(emailSplit.length == 2)
  val emailUserName: String = emailSplit(0)
  val emailHost: String = emailSplit(1)
  val emailNormalized: String = s"$emailUserName@${emailHost.toLowerCase}"
  val isValidEmail: Boolean = """(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".r
    .matches(email)

  def encryptPassword : String = {
    password.boundedBcrypt
  }
  def verifyPassword(hash : String)  : Boolean = {
    password.isBcryptedBounded(hash)
  }
}
object MemberSignUpLoginJson {
  implicit val json_reads: Reads[MemberSignUpLoginJson] = Json.reads[MemberSignUpLoginJson]
  implicit val json_write: OWrites[MemberSignUpLoginJson] = Json.writes[MemberSignUpLoginJson]
}
