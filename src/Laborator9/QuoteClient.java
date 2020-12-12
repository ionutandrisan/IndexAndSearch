package Laborator9;

import java.io.*;
import java.net.*;
import java.util.*;

public class QuoteClient {
    public static void main(String[] args) throws IOException {



        byte[] mesaj = new byte[31];
        byte[] recive = new byte[255];
        Arrays.fill(mesaj, (byte) 0);
        Random x = new Random();
        mesaj[1] = (byte) (0xFF & (x.nextInt(255) + 1));
        mesaj[5] = (byte) 1;

        String domeniu = "www.tuiasi.ro";
        String[] labels = domeniu.split("\\.");
        int idx = 12;

        for(int i = 0; i < labels.length; ++i) {
            int tmp = labels[i].length();
            mesaj[idx++] = (byte) (tmp & 0xFF);
            for(tmp = 0; tmp < labels[i].length(); tmp++) {
                mesaj[idx++] = (byte) labels[i].charAt(tmp);
            }
        }
        mesaj[idx] = 0;
        mesaj[30] = mesaj[28] = (byte) 0x01;

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("81.180.233.1");
        DatagramPacket packet = new DatagramPacket(mesaj, mesaj.length, address, 53);
        socket.send(packet);

        packet = new DatagramPacket(mesaj, mesaj.length);
        socket.receive(packet);

        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);

        socket.close();



    }
}