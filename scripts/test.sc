import org.xbill.DNS.{Lookup, SimpleResolver, TXTRecord, Type}

val lookup = new Lookup("naver.com", Type.TXT)
lookup.setResolver(new SimpleResolver("8.8.8.8"))
lookup.run()(0).asInstanceOf[TXTRecord].getStrings.size()