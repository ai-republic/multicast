/**
   Copyright 2015 Torsten Oltmanns, ai-republic GmbH, Germany

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

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives multicast messages and delegates them to the registered {@link IMulticastMessageHandler}s.
 *
 * @author Torsten Oltmanns
 *
 */
public class MulticastReceiver implements Runnable, Closeable {
	private final InetAddress group;
	private final int port;
	private final int maxMessageLength;
	private boolean running = false;
	private final List<IMulticastMessageHandler> handlers = new ArrayList<>();


	/**
	 * Creates a new {@link MulticastReceiver}.
	 *
	 * @param group the InetAddress to listen on
	 * @param port the port to listen on
	 * @return the {@link MulticastReceiver}
	 */
	public static MulticastReceiver create(final InetAddress group, final int port, final int maxMessageLength) {
		return new MulticastReceiver(group, port, maxMessageLength);
	}


	/**
	 * Constructor.
	 *
	 * @param group the InetAddress to listen on
	 * @param port the port to listen on
	 */
	private MulticastReceiver(final InetAddress group, final int port, final int maxMessageLength) {
		this.group = group;
		this.port = port;
		this.maxMessageLength = maxMessageLength;
	}


	/**
	 * Add a {@link IMulticastMessageHandler} to handle received messages.
	 *
	 * @param handler the {@link IMulticastMessageHandler}
	 */
	public MulticastReceiver addMessageHandler(final IMulticastMessageHandler handler) {
		if (!handlers.contains(handler)) {
			handlers.add(handler);
		}

		return this;
	}


	/**
	 * Remove a {@link IMulticastMessageHandler} to handle received messages.
	 *
	 * @param handler the {@link IMulticastMessageHandler}
	 */
	public void removeMessageHandler(final IMulticastMessageHandler handler) {
		if (handlers.contains(handler)) {
			handlers.remove(handler);
		}
	}


	@Override
	public void run() {
		try {
			final MulticastSocket receiver = new MulticastSocket(port);
			// join the multicast group
			receiver.joinGroup(group);
			// Now the socket is set up and we are ready to receive packets
			// Create a DatagramPacket and do a receive
			final byte buf[] = new byte[maxMessageLength];
			final DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

			running = true;

			while (running) {
				receiver.receive(receivePacket);

				for (final IMulticastMessageHandler handler : handlers) {
					final byte[] data = new byte[receivePacket.getLength()];
					System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, data.length);
					handler.handle(receivePacket.getAddress(), receivePacket.getPort(), data);
				}
			}

			receiver.leaveGroup(group);
			receiver.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Start receiving.
	 */
	public MulticastReceiver start() {
		if (!running) {
			new Thread(this).start();
		}

		return this;
	}


	/**
	 * Stop receiving.
	 */
	public void stop() {
		running = false;
	}


	@Override
	public void close() throws IOException {
		stop();
	}
}