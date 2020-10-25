package undefined

/**
 *
 * @param setAUnicodes 해당 페이지에 바로 직전에 존재하는 글자
 * @param setBUnicodes 해당 페이지에 최근 1시간 내에 존재하는 글자
 * @param setCUnicodes 나무위키, 위키백과에 정보가 존재하는 글자
 * @param setDUnicodes 정보가 없는 글자(매우 드뭄)
 */
case class Font4Tuple(setAUnicodes: Set[Int],
                      setBUnicodes: Set[Int],
                      setCUnicodes: Set[Int],
                      setDUnicodes: Set[Int])
