package client;

import builders.CommandShaper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class RequestSender {
    private String host;
    private int port;

    public RequestSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static ByteBuffer serializeRequest(CommandShaper commandShaper) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytes);
        oos.writeObject(commandShaper);
        oos.flush();
        ByteBuffer bufferToSend = ByteBuffer.wrap(bytes.toByteArray());
        oos.close();
        bytes.close();
        return bufferToSend;
    }

    public void sendRequest(CommandShaper commandShaper) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(host);
        ByteBuffer byteBuffer = serializeRequest(commandShaper);
        byte[] bufferToSend = byteBuffer.array();
        DatagramPacket datagramPacket = new DatagramPacket(bufferToSend, bufferToSend.length, address, port);
        datagramSocket.send(datagramPacket);
    }
}
