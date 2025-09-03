package imd.ufrn.data.packages;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import imd.ufrn.data.Reader;
import lombok.Getter;

@Getter
public class PacketReader implements Reader {
  private String content;

  public PacketReader(Packet packet) {
    this.content = new String(
      packet.content().getBytes(),
      StandardCharsets.UTF_8
    );
  };

  @Override
  public String readNextLine() throws IOException, EOFException {
    if(content.trim().isEmpty()) throw new EOFException();
    String[] parts = content.split("\r\n");
    String line = parts[0];

    if(line.length() < content.length())
      content = content.substring(line.length(), content.length());
    else content = "";

    return line;
  };

  @Override
  public byte[] readBytes(int length) throws IOException, EOFException {
    // TODO - Verificar depois se tem erro
    byte[] bytes = content.getBytes();
    try (ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(bytes, length)) {
      return builder.toByteArray();
    } catch(Exception e) {
      throw e;
    }
  };

  @Override
  public void close() throws IOException {};
};
