package imd.ufrn.data.packages;

import java.net.InetSocketAddress;

public record Packet (
 InetSocketAddress address,
 String content
) {};
