package edu.escuelaing.arsw.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Calculadora de seno, coseno y tangente
 * @author Johann Cepeda
 */
public class Server {

    /**
     * Metodo principal de la clase
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        String currentOp = "cos";

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("fun")) {
                String[] op = inputLine.split(":");
                if (!op[1].equals("sen") && !op[1].equals("cos") && !op[1].equals("tan")) {
                    out.println("Operacion no valida");
                } else {
                    currentOp = op[1];
                    System.out.println("Cambio de operacion a: " + currentOp);
                    out.println("Cambio de operacion a: " + currentOp);
                }
            } else {
                try {
                    if (currentOp.equals("cos"))
                        outputLine = "Coseno de " + inputLine + " = " + Math.cos(Double.parseDouble(inputLine));
                    else if (currentOp.equals("sen"))
                        outputLine = "Seno de " + inputLine + " = " + Math.sin(Double.parseDouble(inputLine));
                    else if (currentOp.equals("tan"))
                        outputLine = "Tangente de " + inputLine + " = " + Math.tan(Double.parseDouble(inputLine));
                    else
                        outputLine = "Operacion invalida";

                    // Resultados
                    out.println(outputLine);
                } catch (Exception e) {
                    throw new Exception("Formato invalido");
                }
            }
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
