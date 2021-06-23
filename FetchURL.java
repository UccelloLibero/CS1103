/*
A program that fetches the information stored at a given URL on the web and saves that data to a file.
**/

import java.io.*;
import java.util.*;
import java.net.*;

class FetchURL {

  private static void copyStream(InputStream in, OutputStream out) throws IOException {
    int oneByte = in.read();
    while(oneByte >= 0) {
      out.write(oneByte);
      oneByte = in.read();
    }
  }

  public static void main(String[] args) {
    // using scanner class form java.util package to read user input in a form of a string
    Scanner sc = new Scanner(System.in);

    // capturing and reading user input for URL as a string
    System.out.println("Please enter a URL: ");
    String link = sc.nextLine();

    // capturing and reading user input for custom file name as a string
    System.out.println("Please enter a filename: ");
    String fileName = sc.nextLine();

    // variable representing input and output stream initialized to null
    InputStream in = null; // data read from Web URL
    FileOutputStream out = null; // file where we save data from Web URL

    // handle exception in case of wrong entry
    try {
      out = new FileOutputStream(fileName);
      URL url = new URL(link);
      in = url.openStream();
      copyStream(in, out);
    }
    catch (Exception e) {
      e.printStackTrace(); // Java's throwable class which prints the throwable along with all details of the error, e.g. will be triggered if the URL is invalid
    }
    finally {
      try {
        // closing both streams
        in.close(); // closing the stream read from URL
        out.close(); // closing the file
      }
      catch (Exception e) {
        e.printStackTrace(); // will be triggered if either stream or file fail to close instead of program crashing
      }
    }
  }
}
