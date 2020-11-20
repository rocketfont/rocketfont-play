package controllers

import javax.inject._
import org.jsoup.Jsoup
import play.api.mvc._
import scalikejdbc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GomGukWebPagePrintController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(srl : Long, fontParams : String, fontFamily : String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>

    val text = DB readOnly { implicit session =>

      sql"""
             |SELECT text as TEXT
             |FROM rocket_font_test
             |WHERE 1=1
             |AND srl = $srl""".stripMargin
     .map(rs => rs.string("TEXT")).single().apply().getOrElse("ERROR NOT EXISTS")
    }

    val textOnly = Jsoup.parse(text).text()
    Ok(views.html.gomgukTest(textOnly, fontParams, fontFamily))
  }
}
