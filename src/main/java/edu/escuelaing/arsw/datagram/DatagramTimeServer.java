package edu.escuelaing.arsw.datagram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatagramTimeServer {

    DatagramSocket socket;
    boolean active = false;

    public DatagramTimeServer() {
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException ex) {
            Logger.getLogger(DatagramTimeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startServer() throws InterruptedException {
        byte[] buf = new byte[256];
        active = true;
        try {
            while(active){
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String dString = new Date().toString();
                buf = dString.getBytes();
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            }

        } catch (

        IOException ex) {
            Logger.getLogger(DatagramTimeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        DatagramTimeServer ds = new DatagramTimeServer();
        try {
            ds.startServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}