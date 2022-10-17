import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(){

    var client: Client = Client()

    var isServerAnswers = false

    while(true){
        try {
            if (!isServerAnswers) {
                isServerAnswers = client.testServer()
            }

            if (isServerAnswers) {
                println(client.listenServer())
                client.answer()
            }
        }catch (e: Exception){
        }
    }
}