package fr.umlv.irsensor.sensor.dispatcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.logging.Level;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.util.IRSensorLogger;

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

          IRSensorLogger.postMessage(Level.INFO,
              "Dispatcher is listening on port " + socketPort);

          while (true) {
            final SocketChannel client = serverSocket.accept();
            new Thread(new Runnable() {
              public void run() {
                final ByteBuffer buffer = ByteBuffer
                    .allocate(IRSensorConfiguration.PACKET_MAX_SIZE);
                try {

                  while (client.read(buffer) != -1) {
                  }
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
                buffer.flip();

                final PacketRegisterable p = packetRegisterables
                    .get(DecodePacket.getId(buffer));

                if (p != null) {
                  IRSensorLogger.postMessage(Level.INFO, name
                      + " : Receive a packet for " + p.getId() + " "
                      + DecodePacket.getOpCode(buffer));
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
      IRSensorLogger.postMessage(Level.INFO, "Want to be registered "
          + packetRegisterable.getId());
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
