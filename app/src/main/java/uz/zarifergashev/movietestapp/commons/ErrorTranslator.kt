package uz.zarifergashev.movietestapp.commons

object ErrorTranslator {
    fun translate(exception: Exception): String {
        return exception.message ?: exception.localizedMessage ?: ""
    }
}