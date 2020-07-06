/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package FullBloomBot.ZacharyTalis.utils.handler.server;

import FullBloomBot.ZacharyTalis.entities.Server;
import FullBloomBot.ZacharyTalis.entities.impl.ImplServer;
import FullBloomBot.ZacharyTalis.ImplDiscordAPI;
import FullBloomBot.ZacharyTalis.utils.LoggerUtil;
import FullBloomBot.ZacharyTalis.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * Handles the guild members chunk packet.
 */
public class GuildMembersChunkHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GuildMembersChunkHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMembersChunkHandler(ImplDiscordAPI api) {
        super(api, true, "GUILD_MEMBERS_CHUNK");
    }

    @Override
    public void handle(JSONObject packet) {
        final Server server = api.getServerById(packet.getString("guild_id"));
        if (server != null) {
            ((ImplServer) server).addMembers(packet.getJSONArray("members"));
        }
    }

}
