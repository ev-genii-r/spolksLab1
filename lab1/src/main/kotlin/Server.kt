import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.util.*


class Server {

    var serverSocket: ServerSocket = ServerSocket(8081)
    var socket: Socket? = null
    var input: BufferedReader? = null
    var output: BufferedWriter? = null

    var clientAnswer: String? = "?"
    private val fileDirectory = "src/main/resources/fileFolder"

    fun initServer(){
        socket = serverSocket.accept()
        input = BufferedReader(InputStreamReader(socket?.getInputStream()))
        output = BufferedWriter(OutputStreamWriter(socket?.getOutputStream()))
    }

    fun testClient(): Boolean{
        output?.write("Hello, Client" + '\n')
        output?.flush()
        var message: String? = input?.readLine()
        println("client send: $message")
        if(message != null){
            return true
        }
        return false
    }

    fun serverFunctionality(){
        if(clientAnswer == "?"){
            showServerFunctionality()
        }else if(clientAnswer == "1"){
            testConnection()
        }else if(clientAnswer == "2"){
            showAvailableFiles()
        }else if(clientAnswer == "3"){
            sendFile()
        }else if(clientAnswer == "0"){
            closeConnection()
            return
        }else{
            output?.write("I dont understand" + '\n')
            output?.flush()
        }
        listenToClient()
    }

    private fun showServerFunctionality(){
        output?.write("My functionality: ")
        output?.write("1)Test connection ")
        output?.write("2)Show files ")
        output?.write("3)Get file ")
        output?.write("?)Show functionality ")
        output?.write("0)Close connection \n")
        output?.flush()
    }

    private fun testConnection(){
        output?.write("Stable connection \n")
        output?.flush()
    }

    private fun listenToClient(){
        println("listening")
        clientAnswer = input?.readLine()
        println("-$clientAnswer")
    }

    private fun closeConnection(){
        output?.write("Bye \n")
        output?.flush()
        serverSocket.close()
    }

    private fun showAvailableFiles(){
        val dir = File(fileDirectory)
        val arrayFiles: Array<File> = dir.listFiles()
        val listFiles = listOf(*arrayFiles)
        var allNames = ""
        for(f in listFiles){
            allNames += f.name + " "
        }
        output?.write(allNames + "\n")
        output?.flush()
    }

    private fun findFile(name: String?): File? {
        val dir = File(fileDirectory)
        val arrayFiles: Array<File> = dir.listFiles()
        val listFiles = listOf(*arrayFiles)
        var file: File? = null
        for(f in listFiles){
            if(f.name == name){
                file = f
            }
        }
        return file
    }

    private fun sendFile(){
        output?.write("Enter the file name" + '\n')
        output?.flush()
        listenToClient()
        val file = findFile(clientAnswer)
        if(file == null){
            output?.write("File is not exists" + '\n')
            output?.flush()
            return
        }else{
            output?.write("File name: ${file.name}" +
                    " file size: ${file.length()} Bytes" +
                    " do you want to download it? yes/no \n")
            output?.flush()
            listenToClient()
            if(clientAnswer == "yes"){
                transportFile(file)
            }
        }
    }

    private fun transportFile(file: File){
        try {
            var outputBytes: OutputStream = DataOutputStream(BufferedOutputStream(socket?.getOutputStream()))
            var bytes = ByteArray(1024)
            var inputStream: InputStream = FileInputStream(file)
            var num = inputStream.read(bytes)
            while (num != -1) {
                outputBytes.write(bytes)
                outputBytes.flush()
                num = inputStream.read(bytes)
            }
            outputBytes.write(-1)
            outputBytes.flush()
            output = BufferedWriter(OutputStreamWriter(socket?.getOutputStream()))

        }catch (e: Exception){
            println("file transporting error")
            e.printStackTrace()
        }
    }
}