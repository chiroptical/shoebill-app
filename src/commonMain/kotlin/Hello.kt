import arrow.optics.optics

@optics
data class Hello(val value: Int) {
    companion object
}

val hello = Hello(0)
val world = Hello.value.modify(hello) { it + 1 }