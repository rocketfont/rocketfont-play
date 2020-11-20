package undefined

import javax.inject.Inject
import play.api.Logger
import play.api.mvc.Results.Forbidden
import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class AuthedRequest[T](memberSrl : Long, data : T)
class AuthorizedAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser){
  private val logger = Logger(this.getClass)
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val memberSrlOpt = request.session.get("memberSrl").flatMap(t => t.toLongOption)
    memberSrlOpt match {
      case Some(memberSrl) => block(request)
      case None =>
        logger.info("not auth")
        Future.successful(Forbidden)
    }
  }
}
