package app.shoebill.application

import Hello
import value // this is a bit odd, but should be fine?

val x = Hello(0)
val y = Hello.value.modify(x) { it + 1 }