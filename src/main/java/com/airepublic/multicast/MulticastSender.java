/**
   

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.airepublic.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Sends messages via multicast.
 *
 * @author Torsten Oltmanns
 *
 */
public class MulticastSender {

    /**
     * Sends messages via multicast.
     *
     * @param group the {@link InetAddress} to send to
     * @param port the port
     * @param data the data
     * @throws IOException
     */
    public static void send(final InetAddress group, final int port, final byte[] data) throws IOException {
        // Create the socket but we don't bind it as we are only going to send data
        final MulticastSocket s = new MulticastSocket();
        // Create a DatagramPacket
        final DatagramPacket pack = new DatagramPacket(data, data.length, group, port);
        s.send(pack);
        s.close();
    }
}