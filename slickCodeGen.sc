import java.time.LocalDate
import java.time.temporal.ChronoUnit

val from  = LocalDate.parse("2020-01-15")

val to  = LocalDate.parse("2020-02-14")
val diff = ChronoUnit.MONTHS.between(to, from)

