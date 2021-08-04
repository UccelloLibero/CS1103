import java.io.*;
import java.net.*;
import java.util.*;

/**
The main routine of this program reads requests from a web browser and displays the
request data on the standard output.
This program sets up a listener on port 50505.
This port can be reached via Web browser running on the same machine (your computer)
using URL of the form: http://localhost:50505/path/to/resource.html
The main method does not return any data; it simply reads the request, writes it to
standard output and then closes the connection.
However, the program will continue to run and the server continues to listed for
new connections until the program is terminated.
The termination is done by clicking the red "stop" button in Eclipse or Control-C on the
command line.
*/

public class ReadRequest {

  // The server listens on the port 50505.
  private final static int LISTENING_PORT = 50505;
  protected static Socket client;
  protected static DataInputStream in;
  protected static PrintStream out;
  static String requestedFile;

  // Create a ServerSocket and use it to accept connection requests.
  // Open a serve socket and listen for connection requests.
  // Call handleConnection() method to respond to connection request.
  // This program runs until it is manually terminated or an error occurs.

  public static void main(String[] args) {
    ServerSocket serverSocket;

    try {
      serverSocket = new ServerSocket(LISTENING_PORT);
    }
    catch (Exception e) {
      System.out.println("Failed to create listening socket");
      return;
    }

    System.out.println("Listening on port " + LISTENING_PORT);
    try {
      while(true) {
        Socket connection = serverSocket.accept();
        System.out.println("\n Connection from " + connection.getRemoteSocketAddress());
        ConnectionThread thread = new ConnectionThread(connection);
        thread.start();
      }
    }
    catch (Exception e) {
      System.out.println("Server socket shut down unexpectedly!");
      System.out.println("Error: " + e);
      System.out.println("Exiting.");
    }
  }

  // Handle communication with one client connection.
  // handleConnection() method catches and heldes any exception that occurs so that the exception does not crash the whole server.
  // The socket does get closed at the end of the method.
  // handleConnection() method reads lines of text from the client and prints them to standard output.
  // It continues to read until the client closes the connection or until an error occurs or until
  // a blank line is read.
  // In a connection from a web browser, the first blank line marks the end of the request.
  // This method can run indefinitely, while waiting for the client to send a blank line.
  // This method does not throw any exceptions, instead it catches and handles them so that the
  // server can run continuously.

  private static void handleConnection(Socket connection) {

    String httpRootDir = "/Documents and Settings";
    client = connection;

    try {
      // create input and output streams for conversation with Client
      in = new DataInputStream(client.getInputStream());
      out = new PrintStream(client.getOutputStream());

      // read buffer
      String line = null;
      // first line of request
      String req = null;

      // GET <filename> HTTP/1.x
      req = in.readLine();

      // loop through and discard the rest of the request
      line = req;
      while (line.length() > 0) {
        line = in.readLine();
      }

      // create a token of the input line
      StringTokenizer st = new StringTokenizer(req);

      // check if HTTP request is "GET"
      if (!st.nextToken().equals("GET")) {

        // if it's not GET send HTTP error 501
        sendErrorResponse(501);
        return;
      }

      // parse request -- get filename
      requestedFile = st.nextToken();

      // create a File object
      File f = new File(httpRootDir + requestedFile);
      // check to see if the file exists
      if (!f.canRead()) {
        // if can not read then send HTTP Error 404 -- file not found
        sendErrorResponse(404);
        return;
      }

      // send HTTP response
      sendResponseHeader(getMimeType(requestedFile), (int) f.length());
      // send File to the client
      sendFile(f, client.getOutputStream());
    }

    catch (Exception e) {
      System.out.println("Error whle communicating with client: " + e);
    }

    finally {
      // ensure the connection is closed before returning
      try {
        connection.close();
      }
      catch (Exception e) {}
      System.out.println("Connection closed.");
    }
  }


  // Send a HTTP 200 OK header to the client
  // The third line holds the content-lenght of the document
  // The forth line holds the connection type
  private static void sendResponseHeader(String type, int length) {
    out.println("HTTP/1.1 200 OK");
    out.println("Content-type: " + type);
    out.println("Content-length: " + length);
    out.println("Connection: close " + "\r\n");
  }

  // Send HTTP ERROR header to the Client
  // The first line is status message from the server to the client.
  // The second line holds the connection type.
  // The third line holds the mime type of the document
  // The forth line hold more information of the error.
  private static void sendErrorResponse(int errorCode) {
    switch (errorCode) {
      case 404:
        out.print("HTTP/1.1 404 Not Found");
        out.println("Connection: close");
        out.println("Content-type: text/plain " + "\r\n");
        out.println("<html><head><title>Error</title></head><body><h2>Error: 404 Not Found</h2><p>The resource that you requested does not exist on this server.</p></body></html>");
        break;
      case 501:
        out.print("HTTP/1.1 501 Not Implemented");
        out.println("Connection: close");
        out.println("Content-type: text/plain " + "\r\n");
        break;
    }
  }

  // Extract the MimeType of the file.
  private static String getMimeType(String fileName) {
    int pos = fileName.lastIndexOf(".");

    if (pos < 0) {
      // no file extension in name
      return "x-application/x-unknown";
    }

    String ext = fileName.substring(pos+1).toLowerCase();

    if (ext.equals("txt")) {
      return "text/plain";
    }
    else if (ext.equals("html")) {
      return "text/html";
    }
    else if (ext.equals("htm")) {
      return "text/htm";
    }
    else if (ext.equals("css")) {
      return "text/css";
    }
    else if (ext.equals("js")) {
      return "text/javascript";
    }
    else if (ext.equals("java")) {
      return "text/x-java";
    }
    else if (ext.equals("jpeg")) {
      return "image/jpeg";
    }
    else if (ext.equals("jpg")) {
      return "image/jpg";
    }
    else if (ext.equals("png")) {
      return "image/png";
    }
    else if (ext.equals("gif")) {
      return "image/gif";
    }
    else if (ext.equals("ico")) {
      return "image/x-icon";
    }
    else if (ext.equals("class")) {
      return "application/java-vm";
    }
    else if (ext.equals("jar")) {
      return "application/java-archive";
    }
    else if (ext.equals("zip")) {
      return "application/zip";
    }
    else if (ext.equals("xml")) {
      return "application/xml";
    }
    else if (ext.equals("xhtml")) {
      return "application/xhtml+xml";
    }
    else {
      return "x-application/x-unknown";
      // Note: x-application/x-unknown is something made up;
      // it will probably make the browser offer to save the file.
    }
  }


  // Method to send the file to the output Socket.
  private static void sendFile(File file, OutputStream socketOut) throws IOException {

    InputStream infile = new BufferedInputStream(new FileInputStream(file));
    OutputStream outfile = new BufferedOutputStream(socketOut);

    while (true) {
      // read one byte from file
      int x = infile.read();
      // end of file reached
      if (x < 0) {
        break;
      }
      outfile.write(x);
    }
    outfile.flush();
  }

  // create a thread
  // making this server into multi-threaded server
  private static class ConnectionThread extends Thread {
    Socket connection;
    ConnectionThread(Socket connection) {
      this.connection = connection;
    }

    public void run() {
      handleConnection(connection);
    }
  }
}
