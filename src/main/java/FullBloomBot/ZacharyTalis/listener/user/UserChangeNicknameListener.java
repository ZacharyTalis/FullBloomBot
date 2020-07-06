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
package FullBloomBot.ZacharyTalis.listener.user;

import FullBloomBot.ZacharyTalis.DiscordAPI;
import FullBloomBot.ZacharyTalis.entities.Server;
import FullBloomBot.ZacharyTalis.entities.User;
import FullBloomBot.ZacharyTalis.listener.Listener;

/**
 * This listener listens to user nickname changes.
 */
public interface UserChangeNicknameListener extends Listener {

    /**
     * This method is called every time a user changed its nickname.
     *
     * @param api The api.
     * @param server The server.
     * @param user The user with the updated nickname.
     * @param oldNickname The old nickname of the user. May be <code>null</code>.
     */
    public void onUserChangeNickname(DiscordAPI api, Server server, User user, String oldNickname);

}
