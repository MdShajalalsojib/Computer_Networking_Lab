package POST;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class post {

    public static void main(String[] args) throws MalformedURLException, IOException {

        // opening the connection to a URL webpage where we can post
        URL myUrl = new URL("https://jsonplaceholder.typicode.com/posts/");
        HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

        // setting the method for this connection
        conn.setRequestMethod("POST");

        // now posting anything on the given URL webpage
        conn.setDoOutput(true);                                                 // this is to be done so that we can surely post anything using this connection
        OutputStream out = conn.getOutputStream();                              // this is to take anything into out for posting using the connection
        String postedString = "Hi!!! We have posted something!!! Yay!!!";       // this is the string we wanted to post in the URL
        out.write(postedString.getBytes());                                     // we write/post all the bytes in the string postedString

        // now we test if our posting the postedString string is correct or not
        // first we get the responsecode from the connection, then we test by printing out the code and the responseMessage from URL server
        int responseCode = conn.getResponseCode();
        System.out.print("Value of http created is: " + conn.HTTP_CREATED + "\n");
        if (responseCode == conn.HTTP_CREATED) {
            System.out.print("This is the response Code: " + responseCode + "\n");
            System.out.print("This is the response Message from server: " + conn.getResponseMessage() + "\n");
        } else {
            System.out.print("GO HOME EVERYBODY :( ");
        }

        // Now since we have completed posting, we need to see our posted content
        // this is to be done using our GET method to the connection
        // we DO NOT NEED to use GET method, we just read it using inputstream and strings
        // we need string to get printed. so we need a string-type object, where the new URL content will be saved and later on printed
        InputStreamReader in = new InputStreamReader(conn.getInputStream());    // This is to so that our connection can receive 
        // the new posting from the URL
        BufferedReader buffer = new BufferedReader(in);                         // Now buffer can read from the URL server webpage; used for reading

        StringBuffer fromServer = new StringBuffer();                           // This is the string where each line of the 
        String eachLine = null;                                                   // new postings from the URL will be saved

        while ((eachLine = buffer.readLine()) != null) // While loop continues until we reach end of webpage
        {
            fromServer.append(eachLine);
            fromServer.append(System.lineSeparator());                          // After while loop everything from the webpage
        }                                                                       // is now saved into the stringBuffer fromServer, which can be printed

        buffer.close();                                                         // We close the stream for any further doing

        // now all the webpage content is saved in the fromServer Stringbuffer
        // now we just print this fromServer
        System.out.print("Here is our posted content :\n" + fromServer);
//    }                                                                           
    }
}


//http://webcode.me/
