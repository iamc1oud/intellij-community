// IMPORT: dependency.util4
package p

import dependency.util1
import dependency.util2
import dependency.util3
import dependency.Date
import java.util.*

val aMap: AbstractMap<Int, Int>? = null
val calendar: Calendar? = null

fun klass(): Date {
    util1()
    util2()
    util3()
    return Date()
}