import akka.actor.ActorSystem
import controllers.Assets
import controllers.Assets.Asset
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Configuration, Play}
import undefined.dataClass.{FontInfo, FontTuple}
import undefined.{FontSubset, UnicodeRangePrinter}

import java.util
import java.util.Collections
import java.util.stream.Collectors
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.reflect.io.File

//noinspection ScalaStyle
class FontToolsSpec  extends PlaySpec with GuiceOneAppPerSuite{

  private val actorSystem = app.injector.instanceOf(classOf[ActorSystem])
  private val config = app.injector.instanceOf(classOf[Configuration])

  private val fontSubset = new FontSubset(config, actorSystem)

  "system can parse font Request Get Param" in {
    FontTuple("Noto Sans KR:400:normal").getOrElse(FontTuple("",400,"")).mustBe(FontTuple("Noto Sans KR", 400, "normal"))
  }

  "fonttools can subset font" must {
    "help exit code 0" in {
      fontSubset.help mustBe 0
    }

    "subset font is working" in {
      fontSubset.subsetFont("NotoSansKR-Medium.otf",
        "가나다라"
        .codePoints()
        .boxed()
        .iterator()
        .asScala
        .map(_.toInt)
        .toSeq)
    }
  }

  "Unicode Ranes can range Test Case A" in {
    val a = Seq(1,2,3, 5, 7,8, 9, 12)
    UnicodeRangePrinter.printUnicode(a) .mustBe("U+1-3,U+5,U+7-9,U+c")
    val b = Seq(1, 3,4,5,6, 8,9)
    UnicodeRangePrinter.printUnicode(b) .mustBe("U+1,U+3-6,U+8-9")
  }

}
