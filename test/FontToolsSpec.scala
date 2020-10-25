import controllers.Assets
import controllers.Assets.Asset
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Configuration, Play}
import undefined.FontSubset

import scala.concurrent.ExecutionContext
import scala.reflect.io.File

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

}
