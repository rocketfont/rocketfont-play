import controllers.Assets
import controllers.Assets.Asset
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Configuration, Play}
import undefined.{FontSubset, UnicodeRangePrinter}

import scala.concurrent.ExecutionContext
import scala.reflect.io.File

//noinspection ScalaStyle
class FontToolsSpec  extends PlaySpec with GuiceOneAppPerSuite{

  private val ec = app.injector.instanceOf(classOf[ExecutionContext])
  private val config = app.injector.instanceOf(classOf[Configuration])

  private val s = new FontSubset(config, ec)

  "fonttools can subset font" must {
    "help exit code 0" in {
      s.help mustBe 0
    }


    "subset font is working" in {
      s.subsetFont("NotoSansKR-Medium.otf", "가나다라")
    }
  }

  "Unicode Ranes can range Test Case A" in {
    val a = Seq(1,2,3, 5, 7,8, 9, 12)
    UnicodeRangePrinter.print(a) .mustBe("U+1-3,U+5,U+7-9,U+C")
    val b = Seq(1, 3,4,5,6, 8,9)
    UnicodeRangePrinter.print(b) .mustBe("U+1,U+3-6,U+8-9")
  }

}
