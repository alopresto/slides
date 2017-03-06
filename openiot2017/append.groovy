import org.apache.commons.io.IOUtils
import java.nio.charset.*

def flowfile = session.get()
if (!flowfile) return

try {
	log.info("Received log message from generator")
	
	def inputStream = session.read(flowfile)
	def logMessage = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
	inputStream.close()

	def logFile = new File(logFilePath as String)
	logFile.append(System.getProperty("line.separator") + logMessage)
	
	log.info("Wrote message to log file (total: ${logFile.text.split("\n").size()} lines)")
	
	session.transfer(flowfile, REL_SUCCESS)
} catch (Exception e) {
	log.error(e)
	session.transfer(flowfile, REL_FAILURE)
}