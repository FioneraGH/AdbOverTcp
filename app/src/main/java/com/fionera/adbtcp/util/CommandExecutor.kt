package com.fionera.adbtcp.util

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * CommandExecutor
 * Created by fionera on 03/01/2018 in AdbOverTCP.
 */
class CommandExecutor {
    companion object {

        fun execCommand(command: String): String {
            try {
                val process = Runtime.getRuntime().exec(command)

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var read = 0
                val buffer = CharArray(4096)
                val output = StringBuffer()
                while ((reader.read(buffer).apply { read = this }) > 0) {
                    output.append(buffer, 0, read)
                }
                reader.close()

                process.waitFor()

                return output.toString()
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }

        fun execCommandWithoutResult(command: String) {
            try {
                Runtime.getRuntime().exec(command)
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }

        fun execCommandsAsSU(commands: Array<String>): Boolean {
            var os: DataOutputStream? = null
            try {
                val process = Runtime.getRuntime().exec("su")
                os = DataOutputStream(process.outputStream)

                for (command in commands) {
                    os.writeBytes(command + "\n")
                    os.flush()
                }

                os.writeBytes("exit\n")
                os.flush()

                process.waitFor()
            } catch (e: IOException) {
                return false
            } catch (e: InterruptedException) {
                return false
            } finally {
                if (os != null) {
                    os.close()
                }
            }

            return true
        }

    }
}