package network_communicator.services;

import network_communicator.interfaces.*;

import java.io.*;
import java.net.*;

/**
 * 服务器
 */
public class Server
{
    private static ReadWriteThread _thread;

    public static void start(int port, Messager messager, Messager callback, Runnable connectionReset) throws IOException
    {
        _thread = new ReadWriteThread(new ServerSocket(port), messager, callback, connectionReset);
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