/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.atmosphere;

import com.create.model.Message;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ManagedService(path = LiveUpdate.LIVE_UPDATE)
public class LiveUpdate {
	private static final Logger LOG = LoggerFactory.getLogger(LiveUpdate.class);
	public static final String LIVE_UPDATE = "/live/update";

	@Ready
	public final void onReady(final AtmosphereResource resource) {
		LOG.debug("onReady : {} ", resource);
	}

	@Disconnect
	public final void onDisconnect(final AtmosphereResourceEvent event) {
		LOG.debug("onDisconnect : {} ", event);
	}

	@org.atmosphere.config.service.Message(encoders = { MessageConverter.class }, decoders = { MessageConverter.class })
	public final Message onMessage(final Message message) throws IOException {
		LOG.debug("onMessage : {}", message);
		return message;
	}

}
