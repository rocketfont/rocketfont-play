package undefined.di

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

class DBExecutionContextImpl @Inject()(system: ActorSystem)
  extends CustomExecutionContext(system, "db-context")
    with DBExecutionContext
