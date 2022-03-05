package network_communicator.services;

import network_communicator.interfaces.*;

import java.io.*;
import java.net.*;

public class Server
{
    private static ReadThread _thread;

    public static void start(int port, Messager messager, Messager callback, Runnable connectionReset) throws IOException
    {
        _thread = new ReadThread(new ServerSocket(port), messager, callback, connectionReset);
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