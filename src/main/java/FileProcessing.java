import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessing {
    private static final int FILENAME_MAX_LENGTH = 64;
    private static final String FILENAME_PATTERN = "[^A-Za-z0-9/]";
    private static final String ROOT_PATH = "/var/www/uploads/";

    private static boolean isSafeFileName(String fileName) {
        System.out.println("incoming file name: " + fileName);
        if (fileName.length() > FILENAME_MAX_LENGTH) {
            System.out.println("error, max file name");
            return false;
        }
        Pattern pattern = Pattern.compile(FILENAME_PATTERN);
        Matcher matcher = pattern.matcher(fileName);
        boolean matchFound = matcher.find();
        if (matchFound) {
            System.out.println("error, bad filename");
            return false;
        } else {
            System.out.println("file name passed");
            return true;
        }
    }

    private static void function3() throws Throwable {
        String data;
        data = "";

        Socket socket = null;
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;

        try {
            // for Man-In-the-Middle protection, you should connect the target via encrypted session
            // socket = new Socket("host.example.org", 39544);


            socket = new Socket("127.0.0.1", 39544);
            readerInputStream = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            readerBuffered = new BufferedReader(readerInputStream);
            data = readerBuffered.readLine();
        } catch (IOException exceptIO) {
            System.out.println("Error with stream reading" + exceptIO);
        } finally {
            System.out.println("snipped");
        }


        int lastSlashIndex = data.lastIndexOf("/");
        String extractFileName = data.substring(lastSlashIndex + 1);

        if (data != null && isSafeFileName(data.replace(extractFileName, ""))) {
            System.out.println(data);
            File file = new File(ROOT_PATH + data);

            FileInputStream streamFileInputSink = null;
            InputStreamReader readerInputStreamSink = null;
            BufferedReader readerBufferedSink = null;
            if (file.exists() && file.isFile()) {
                try {
                    streamFileInputSink = new FileInputStream(file);
                    readerInputStreamSink = new InputStreamReader(streamFileInputSink,
                            StandardCharsets.UTF_8);
                    readerBufferedSink = new BufferedReader(readerInputStreamSink);
                    System.out.println(readerBufferedSink.readLine());
                } catch (Throwable t) {
                    System.out.println("snipped");
                }
            }
        } else {
            System.out.println(" do action ...");
        }
    }


    public static void main(String[] args) throws Throwable {
        function3();
    }
}
