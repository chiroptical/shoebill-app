import arrow.optics.optics

@optics
data class Hello(val value: Int) {
    companion object
}