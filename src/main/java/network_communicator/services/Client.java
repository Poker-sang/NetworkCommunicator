package network_communicator.services;

import network_communicator.interfaces.*;

import java.io.*;
import java.net.*;

//客户端
public class Client
{
    private static ReadThread _thread;

    public static void Connect(InetAddress ip, int port, Messager messager, Runnable connectionReset) throws IOException
    {
        _thread = new ReadThread(new Socket(ip, port), messager, connectionReset);
        _thread.start();
    }

    public static void send(String message) throws IOException
    {
        _thread.send(message);
    }

    public static void close()
    {
        if (_thread != null && !_thread.isInterrupted())
            _thread.interrupt();
    }
}