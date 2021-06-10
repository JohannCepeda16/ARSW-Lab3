package edu.escuelaing.arsw.url;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Lector de URL para captar informacion
 * @author Johann Cepeda
 */
public class URLReader {

    public static void main(String... args) throws MalformedURLException {
        URL google = new URL("http://www.google.com/");
        System.out.println("---- Leyendo los datos de un objeto URL ----");
        System.out.println("Protocol: " + google.getProtocol());
        System.out.println("Authority: " + google.getAuthority());
        System.out.println("Host: " + google.getHost());
        System.out.println("Port: " + google.getPort());
        System.out.println("Path: " + google.getPath());
        System.out.println("Query: " + google.getQuery());
        System.out.println("File: " + google.getFile());
        System.out.println("Reference: " + google.getRef());
    }
}
