package multicast;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airepublic.multicast.MulticastReceiver;
import com.airepublic.multicast.MulticastSender;

public class MulticastTest {
	private InetAddress group;
	private final int port = 5000;
	private MulticastReceiver receiver;
	private final Queue<byte[]> queue = new LinkedList<>();


	@Before
	public void setup() throws UnknownHostException {
		group = InetAddress.getByName("224.0.0.0");
		receiver = MulticastReceiver.create(group, port, 10).addMessageHandler((fromHost, fromPort, data) -> {
			System.out.println("Received data from: " + fromHost.toString() + ":" + fromPort + ": " + data);

			queue.add(data);
		});
	}


	@After
	public void tearDown() {
		receiver.stop();
	}


	public void startReceiver() throws IOException {
		new Thread(receiver).start();
	}


	@Test
	public void test() throws Exception {
		receiver.start();

		Thread.sleep(100);

		MulticastSender.send(group, port, "REQUEST".getBytes());

		Thread.sleep(100);

		assertThat(queue).isNotEmpty();
		assertThat(queue.poll()).isEqualTo("REQUEST".getBytes());
	}
}
