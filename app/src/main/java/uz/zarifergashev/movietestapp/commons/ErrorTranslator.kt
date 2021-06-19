package uz.zarifergashev.movietestapp.commons

object ErrorTranslator {

    //you can translate errors here
    fun translate(exception: Exception): String {
        return exception.message ?: exception.localizedMessage ?: ""
    }
}