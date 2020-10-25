package undefined

import java.math.BigInteger
import java.security.MessageDigest

case object SHA256Hash {
  def hash(str : String): String = String.format("%032x", new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(str.getBytes("UTF-8"))))
}
