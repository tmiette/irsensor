package fr.umlv.irsensor.dataserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.data.handler.DataServerHandler;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.common.packets.data.ReqDataPacket;

public class DataServerServer {

  /**
   * Max client that can handle the data server
   */
  private static final int MAX_CLIENT = 5;

  /**
   * Server socket address
   */
  private final InetSocketAddress socketAddress;

  /**
   * Server socket
   */
  private ServerSocketChannel channel;

  /**
   * Executor to handle multiple clients
   */
  private final ExecutorService executer = Executors
      .newFixedThreadPool(MAX_CLIENT);

  private final DataServerHandler handler;

  public DataServerServer(DataServerHandler handler) {
    this.handler = handler;
    socketAddress = new InetSocketAddress(
        IRSensorConfiguration.DATA_SERVER_PORT);
    try {
      channel = ServerSocketChannel.open();
      channel.socket().bind(socketAddress);
    } catch (IOException e) {
      System.err.println("DatagramChannel.open()");
    }
  }

  /**
   * Starts the data server
   */
  public void listen() {
    while (true) {
      try {
        SocketChannel clientChannel = channel.accept();
        executer.submit(handleClient(clientChannel));
        System.out.println("Client accepted");
      } catch (IOException e) {
        System.err.println("accept()");
        close();
      }
    }
  }

  /**
   * Creates a runnable used to handle client request
   * 
   * @param clientChannel
   *            the client(); channel
   * @return the runnable
   */
  private Runnable handleClient(final SocketChannel clientChannel) {
    final ByteBuffer dst = ByteBuffer
        .allocate(IRSensorConfiguration.PACKET_MAX_SIZE);
    return new Runnable() {
      @Override
      public void run() {
        try {
          clientChannel.read(dst);
          dst.flip();
          // Parse request and retrieve catch area
          ReqDataPacket packet = ReqDataPacket.getPacket(dst);
          System.out.println("Received REQDATA from client : " + packet);

          // Prepare response
          Object subData = null;
          ByteBuffer sendBuffer = null;
          try {
            subData = handler.getSubData(packet.getCatchArea());
            byte[] subDataBuffer = handler.dataToByteArray(subData);
            sendBuffer = PacketFactory.createRepData(packet.getId(), handler
                .getMimeType().getId(), subDataBuffer.length, subDataBuffer);
          } catch (IllegalArgumentException e) {
            sendBuffer = PacketFactory.createAck(0, ErrorCode.CONF_ERR);
          }

          clientChannel.write(sendBuffer);
          dst.clear();
        } catch (MalformedPacketException e) {
          System.err.println("Malformed packet " + e.getMessage());
        } catch (IOException e) {
          System.err.println("read()");
          close();
        } finally {
          close(clientChannel);
        }
      }
    };
  }

  /**
   * Closes the data server socket
   */
  public void close() {
    try {
      if (channel != null && channel.isOpen())
        channel.close();
    } catch (IOException e) {
      System.err.println("close()");
    }
  }

  /**
   * Closes the given channel
   */
  public void close(SocketChannel channel) {
    try {
      if (channel != null && channel.isConnected())
        channel.close();
    } catch (IOException e) {
      System.err.println("close()");
    }
  }

}
