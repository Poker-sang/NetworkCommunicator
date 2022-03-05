package network_communicator.services;

import network_communicator.interfaces.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;

//读的线程
class ReadThread extends Thread
{
    @Nullable
    private final ServerSocket _serverSocket;
    @Nullable
    private final Messager _callback;
    private Socket _socket;
    private final Messager _newMessage;
    private final Runnable _connectionReset;

    ReadThread(Socket socket, Messager newMessage, Runnable connectionReset)
    {
        _serverSocket = null;
        _callback = null;
        _socket = socket;
        _newMessage = newMessage;
        _connectionReset = connectionReset;
    }

    ReadThread(@NotNull ServerSocket serverSocket, Messager newMessage, @NotNull Messager callback, Runnable connectionReset)
    {
        _serverSocket = serverSocket;
        _newMessage = newMessage;
        _callback = callback;
        _connectionReset = connectionReset;
    }

    void send(String message) throws IOException
    {
        if (_socket.isConnected())
            new PrintStream(_socket.getOutputStream()).println(message);
    }

    @Override
    public void run()
    {
        if (_serverSocket != null && _callback != null)
            try
            {
                _socket = _serverSocket.accept();
                _callback.send(_socket.getInetAddress().getHostAddress() + ":" + _socket.getPort());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        try
        {
            var inputStream = _socket.getInputStream();
            while (true)
                try
                {
                    var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    var readLine = bufferedReader.readLine();
                    if (isInterrupted())
                    {
                        break;
                    }
                    if (readLine == null)
                    {
                        _connectionReset.run();
                        break;
                    }
                    else
                        _newMessage.send(readLine);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            _socket.close();
            if (_serverSocket != null)
                _serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}