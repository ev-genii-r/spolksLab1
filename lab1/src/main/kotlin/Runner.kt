import java.io.BufferedReader
import java.io.InputStream

fun main(){

    var server = Server()

    var isServerInit = false
    var isClientAnswers = false
    var mistakeCounter = 0

    while (true){
        try {
            if (server.serverSocket.isBound && !isServerInit) {
                server.initServer()
                isServerInit = true
            }
        }catch (e: Exception) {
            println("server initialisation error")
        }

        try {
            if (server.socket?.isConnected == true && !isClientAnswers) {
                isClientAnswers = server.testClient()
            }
        }catch (e:Exception){
            println("client dont answers")
        }
        try {
            server.serverFunctionality()
        }catch (e:Exception){
            println("server functionality error")
            mistakeCounter ++
            if(mistakeCounter == 10){
                break
            }
        }

    }
}