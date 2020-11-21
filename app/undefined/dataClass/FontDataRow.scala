package controllers

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.libs.json.{Json, OWrites}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.{DBExecutionContext, DBExecutionContextImpl}
import undefined.slick.Tables
import undefined.slick.Tables.Font

import scala.async.Async.{async, await}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}





case class FontDataRow(fontSrl: Long,
                       fontFamilyName: String,
                       fontStyle: String,
                       fontWeight: Int,
                       fontLicenseSrl: Long,
                       fontCopyrightSrl: Long,
                       fontCreatorSrl: Long,
                      fontUsagePrice : Int
                      )

object FontDataRow {
  implicit val json_writes: OWrites[FontDataRow] = Json.writes[FontDataRow]
}



