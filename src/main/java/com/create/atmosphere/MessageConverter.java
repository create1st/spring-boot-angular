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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class MessageConverter implements Encoder<Message, String>, Decoder<String, Message> {
	@Autowired
	private ObjectMapper jackson2ObjectMapper;

	@Override
	public Message decode(final String jsonMessage) {
		final Message message;
		try {
			message = jackson2ObjectMapper.readValue(jsonMessage, Message.class);
		} catch (final IOException e) {
			throw new IllegalArgumentException(e);
		}
		return message;
	}

	@Override
	public String encode(final Message message) {
		final String jsonMessage;
		try {
			jsonMessage = jackson2ObjectMapper.writeValueAsString(message);
		} catch (final JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
		return jsonMessage;
	}

}