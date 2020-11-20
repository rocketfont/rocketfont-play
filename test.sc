import java.net.URL

val rgx = "^https?://[a-z0-9_.-]+/.*?".r

rgx.matches("https://cdn.localhost.rocketfont.net/gomguk/getTestPages?srl=1&fontParams=Nanum+Myeongjo:800:normal&fontFamily=Nanum+Myeongjo")
