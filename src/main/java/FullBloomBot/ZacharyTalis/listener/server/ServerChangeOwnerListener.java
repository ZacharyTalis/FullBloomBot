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
package FullBloomBot.ZacharyTalis.listener.server;

import FullBloomBot.ZacharyTalis.DiscordAPI;
import FullBloomBot.ZacharyTalis.entities.Server;
import FullBloomBot.ZacharyTalis.listener.Listener;

/**
 * This listener listens to server owner changes.
 */
public interface ServerChangeOwnerListener extends Listener {

    /**
     * This method is called every time the owner of a server changed.
     *
     * @param api The api.
     * @param server The server with the new owner.
     * @param oldOwnerId The id of the old owner of the server.
     */
    public void onServerChangeOwner(DiscordAPI api, Server server, String oldOwnerId);

}
