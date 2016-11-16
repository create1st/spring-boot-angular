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

package com.create.controller;

import com.create.atmosphere.LiveUpdate;
import com.create.model.Message;
import com.create.model.MessageBuilder;
import com.create.model.MessageType;
import org.atmosphere.cpr.AtmosphereFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/global/status")
public class GlobalController {
    private final AtmosphereFramework atmosphereFramework;

    @Autowired
    public GlobalController(final AtmosphereFramework atmosphereFramework) {
        this.atmosphereFramework = atmosphereFramework;
    }

    @GetMapping(value = "get")
    @ResponseBody
    public Message get() {
        return MessageBuilder
                .aMessage()
                .withType(MessageType.STATUS)
                .withValue(true)
                .build();
    }

    @PostMapping(value = "update")
    public void update(@RequestBody final Message updateMessage) {
        try {
            final Message statusMessage = getStatusMessage(updateMessage);
            this.atmosphereFramework.getBroadcasterFactory()
                    .lookup(LiveUpdate.LIVE_UPDATE)
                    .broadcast(statusMessage);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Message getStatusMessage(final Message message) {
        return MessageBuilder
                .aMessage()
                .withType(MessageType.STATUS)
                .withValue(message.getValue())
                .build();
    }
}