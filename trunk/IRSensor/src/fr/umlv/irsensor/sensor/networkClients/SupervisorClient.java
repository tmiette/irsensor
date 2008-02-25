package fr.umlv.irsensor.sensor.networkClients;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.ErrorCode;
import fr.umlv.irsensor.common.fields.OpCode;
import fr.umlv.irsensor.common.packets.DecodePacket;
import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.sensor.Sensor;

public class SupervisorClient {

  private static final byte[] serverAddress = new byte[] { (byte) 127,
      (byte) 0, (byte) 0, (byte) 1 };

  private static final int SERVER_PORT = IRSensorConfiguration.SUPERVISOR_SERVER_PORT;

  private static final int BUFFER_SIZE = 512;

  private final Sensor sensor;

  public SupervisorClient(Sensor sensor) {
    this.sensor = sensor;
  }

  public void registrySensor() throws IOException, MalformedPacketException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress(InetAddress
        .getByAddress(serverAddress), SERVER_PORT));

    ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    ByteBuffer reqPacket = PacketFactory.createReqConPacket();

    // send REQCON packet to supervision server
    channel.write(reqPacket);

    // wait for REPCON packet
    channel.read(readBuffer);
    readBuffer.flip();

    if (DecodePacket.getOpCode(readBuffer) != OpCode.REPCON)
      throw new MalformedPacketException();
    int id = DecodePacket.getId(readBuffer);
    this.sensor.setId(id);
    channel.write(PacketFactory.createAck(id, ErrorCode.OK));
  }

  public void sendRepData(int id, int mimeType, int dataLen, byte[] data) {
    SocketChannel socketClient = null;
    try {
      socketClient = SocketChannel.open();
      socketClient.connect(new InetSocketAddress(InetAddress
          .getByAddress(serverAddress), SERVER_PORT));
      
      
      System.out.println("###################### I'm here ######################");
      final JFrame frame = new JFrame("Result");
      frame.setSize(new Dimension(800, 600));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new JLabel(new ImageIcon(data)));
      frame.setVisible(true);
      
      
      ByteBuffer b = PacketFactory.createRepData(id, mimeType, data.length,
          data);
      socketClient.write(b);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        socketClient.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
