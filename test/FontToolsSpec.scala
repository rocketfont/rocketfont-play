import controllers.Assets
import controllers.Assets.Asset
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import undefined.di.component.RocketFontDir
import undefined.routine.FontSubset

import scala.concurrent.ExecutionContext
import scala.reflect.io.File

class FontToolsSpec  extends PlaySpec with GuiceOneAppPerSuite{

  private val ec = app.injector.instanceOf(classOf[ExecutionContext])

  "fonttools can subset font" must {

    "help exit code 0" in {
      val s = new FontSubset(app.injector.instanceOf(classOf[RocketFontDir]), ec)
      s.help() mustBe 0
    }

    val rfd =app.injector.instanceOf(classOf[RocketFontDir])

    "font must be exists" in {
      val url = rfd.getFontResource("assets/fonts/NotoSansKR-Medium.otf")
      val file = File(url.getPath)
      file.exists mustBe true
    }

    "subset font is working" in {
      val s = new FontSubset(app.injector.instanceOf(classOf[RocketFontDir]), ec)
      s.subsetFont("NotoSansKR-Medium.otf", "가나다라")
    }

  }

}
