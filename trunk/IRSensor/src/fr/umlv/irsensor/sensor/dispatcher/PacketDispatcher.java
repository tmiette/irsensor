package fr.umlv.irsensor.sensor.dispatcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;

/**
 * <code>PacketDispatcher</code> implements a TCP server socket on a given
 * listening port. Then <code>PacketRegisterable</code> can register itself to
 * the dispatcher to be notified for packet entrance.
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class PacketDispatcher {

  private final int socketPort;

  private final HashMap<Integer, PacketRegisterable> packetRegisterables = new HashMap<Integer, PacketRegisterable>();

  private boolean isRunning;

  private String name;

  /**
   * A constructor with the server socket port The socket is bound on the local
   * host
   * 
   * @param port
   */
  public PacketDispatcher(int port, String name) {
    this.socketPort = port;
    this.name = name;
  }

  public void startDispatcher() {
    // this.isRunning = true;
    Thread dispatcherThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          final ServerSocketChannel serverSocket = ServerSocketChannel.open();

          serverSocket.socket().bind(new InetSocketAddress(socketPort));

          System.out.println("Dispatcher is listening on port " + socketPort);
          final ByteBuffer buffer = ByteBuffer
              .allocate(IRSensorConfiguration.PACKET_MAX_SIZE);
          while (true) {
            final SocketChannel client = serverSocket.accept();
            new Thread(new Runnable() {
              public void run() {
                try {
                  while( client.read(buffer) != -1);
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
                buffer.flip();

                final PacketRegisterable p = packetRegisterables
                    .get(DecodePacket.getId(buffer));

                if (p != null) {
                  System.out.println(name + " : Receive a packet for "
                      + p.getId() + " " + DecodePacket.getOpCode(buffer));
                  final ByteBuffer packet = buffer.duplicate();
                  p.setPacket(packet, client);
                }
                buffer.clear();
              };
            }).start();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    dispatcherThread.setName("packet dispatcher");
    dispatcherThread.start();
  }

  /**
   * Register a new <code>PacketRegisterable</code>
   * 
   * @param packetRegisterable
   * @throws IdAlreadyUsedException
   */
  public void register(PacketRegisterable packetRegisterable)
      throws IdAlreadyUsedException {
    if (!this.isRunning) {
      if (this.packetRegisterables.containsKey(packetRegisterable.getId()))
        throw new IdAlreadyUsedException();
      System.out.println("Want to be registered " + packetRegisterable.getId());
      this.packetRegisterables.put(packetRegisterable.getId(),
          packetRegisterable);
    }
  }

  /**
   * Unregister a <code>PacketRegisterable</code> from its id
   * 
   * @param packetRegisterable
   */
  public void unregister(int id) {
    this.packetRegisterables.remove(id);
  }
}
