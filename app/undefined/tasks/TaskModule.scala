package undefined.tasks

import play.api.inject.SimpleModule
import play.api.inject._

class TaskModule extends SimpleModule(bind[WebPageCrawlingTask].toSelf.eagerly()){

}
