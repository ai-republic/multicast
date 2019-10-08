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

import java.net.InetAddress;

/**
 * Handle received messages from the {@link MulticastReceiver}.
 *
 * @author Torsten Oltmanns
 *
 */
public interface IMulticastMessageHandler {

	/**
	 * Handle received messages from the {@link MulticastReceiver}.
	 *
	 * @param fromHost the host where the multicast message came from
	 * @param fromPort the port where the multicast message came from
	 * @param data the actual data
	 */
	void handle(InetAddress fromHost, int fromPort, byte[] data);
}