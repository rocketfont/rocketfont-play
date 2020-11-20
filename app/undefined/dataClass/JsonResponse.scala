package undefined.dataClass

import akka.util.ByteString
import play.api.http.{ContentTypeOf, ContentTypes, MimeTypes, Writeable}
import play.api.libs.json.{JsArray, JsObject, JsValue, Json, OWrites, Writes}
import play.api.mvc.Codec

case class JsonResponse[A](data : A = JsObject.empty, message : String = "")


object JsonResponse{
  implicit val jsonWrites2: OWrites[JsonResponse[JsObject]] = Json.writes[JsonResponse[JsObject]]
  implicit val jsonWritesArr: OWrites[JsonResponse[JsValue]] = Json.writes[JsonResponse[JsValue]]

  implicit def convert[T](implicit tjs: Writes[JsonResponse[T]]): Writeable[JsonResponse[T]] = {
    Writeable(a => ByteString.fromArrayUnsafe(Json.toBytes(Json.toJson(a))), Some(MimeTypes.JSON))
  }
}