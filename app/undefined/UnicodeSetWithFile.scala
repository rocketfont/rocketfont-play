package undefined

import undefined.dataClass.UnicodeSet

import scala.reflect.io.File

case class UnicodeSetWithFile(unicodeSet : UnicodeSet, fileWithUnicodes : Seq[(Seq[File], Seq[Int])])
