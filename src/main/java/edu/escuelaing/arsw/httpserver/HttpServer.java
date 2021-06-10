package edu.escuelaing.arsw.httpserver;

import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Servidor Http SINGLETON
 * 
 * @author Johann Cepeda
 */
public class HttpServer {

    private static HttpServer _instance = new HttpServer();

    private HttpServer() {
    }

    /**
     * Devuelve la unica instancia de la clase
     * 
     * @return
     */
    public static HttpServer getInstance() {
        return _instance;
    }

    public static void main(String[] args) throws IOException {
        HttpServer.getInstance().startServer(args);
    }

    /**
     * Inicializar el servidor
     * 
     * @param args
     * @throws IOException
     */
    public void startServer(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while(running){
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
    
            processRequest(clientSocket);
        }

        serverSocket.close();
    }

    /**
     * Procesar el Request
     * 
     * @param clientSocket
     * @throws IOException
     */
    public void processRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        String method = "", path = "", version = "";
        List<String> headers = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null) {
            if(method.isEmpty()){
                String[] requestInfo = inputLine.split(" ");
                method = requestInfo[0];
                path = requestInfo[1];
                version = requestInfo[2];
                System.out.println("Request: " + method + " " + path + " " + version);
            }else{
                System.out.println("Header: " + inputLine);
                headers.add(inputLine);
            }

            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }

        outputLine = getResponse(path);
        out.println(outputLine);
        out.close();
        in.close();

        clientSocket.close();
    }

    /**
     * Retorna codigo html para la pagina
     * @return
     */
    public String getResponse(String path) {
        String type = "text/html";
        if(path.endsWith(".css")){
            type = "text/css";
        }else if(path.endsWith(".js")){
            type = "text/javascript";
        }

        Path file = Paths.get("./www" + path);
        Charset charset = Charset.forName("UTF-8");
        String outMsg = "";
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                outMsg += "\r\n" + line;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return "HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n" + "\r\n" 
                + "<!DOCTYPE html>" 
                + outMsg;
    }
}