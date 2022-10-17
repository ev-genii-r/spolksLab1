import java.io.*
import java.net.Socket

class Client{

    var socket:Socket = Socket("127.0.0.1", 8081)

    var input: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
    var output: BufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

    fun testServer(): Boolean{
        var message: String? = input.readLine()
        println("message from server: $message")

        output.write("I hear you" + '\n')
        output.flush()

        if(message != null){
            return true
        }

        return false
    }

    fun listenServer(): String {
        return input.readLine()
    }

    fun answer() {
        var message = readLine()
        output.write(message + "\n")
        output.flush()
        if(message?.get(0) == '3'){
            println(3)
            getFile()
            message = readLine()
            output.write(message + "\n")
            output.flush()
        }
    }

    private fun getFile(){
        println(input.readLine())
        var fileName = readLine()
        output.write(fileName + "\n")
        output.flush()

        val fileCondition = input.readLine()
        println(fileCondition)

        if(fileCondition[5] == 'i'){
            println("file name is incorrect")
            return
        }

        var action = readLine()
        output.write(action + "\n")
        output.flush()

        if(action != "yes"){
            return
        }

        var byteArray = ByteArray(1024)
        try {
            var dataInputStream = DataInputStream(BufferedInputStream(socket.getInputStream()))
            var file = File(fileName)
            var randomAccessFile = RandomAccessFile(file, "rw")
            val startTime = System.currentTimeMillis()
            var num = dataInputStream.read(byteArray)
            while (num != -1 && num != 1){
                randomAccessFile.write(byteArray, 0, num)
                randomAccessFile.skipBytes(num)
                num = dataInputStream.read(byteArray)
            }
            val elapsedTime = System.currentTimeMillis() - startTime
            println("File was successfully transported")
            println("Transporting speed was ${file.length() / elapsedTime * 100} bytes/second")
        }catch (e: Exception){
            println("File transporting error")
        }
    }
}