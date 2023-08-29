package server;

import builders.ResponseShaper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ResponseSender {
    private String host;
    private int port;

    public ResponseSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static ByteBuffer serializeResponse(ResponseShaper responseShaper) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytes);
        oos.writeObject(responseShaper);
        oos.flush();
        ByteBuffer bufferToSend = ByteBuffer.wrap(bytes.toByteArray());
        oos.close();
        bytes.close();
        return bufferToSend;
    }

    public void sendResponse(ResponseShaper responseShaper) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(host);
        ByteBuffer byteBuffer = serializeResponse(responseShaper);
        byte[] bufferToSend = byteBuffer.array();
        DatagramPacket datagramPacket = new DatagramPacket(bufferToSend, bufferToSend.length, address, port);
        datagramSocket.send(datagramPacket);
    }
}
