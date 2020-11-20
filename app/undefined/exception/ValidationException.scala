package undefined.exception

class ValidationException(msg : String, throwable: Throwable) extends RuntimeException(msg, throwable){
  def this(msg : String) = {
    this(msg, null)
  }

}
