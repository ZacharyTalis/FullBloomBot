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
import FullBloomBot.ZacharyTalis.entities.User;
import FullBloomBot.ZacharyTalis.listener.Listener;

/**
 * This listener listens to user game changes.
 */
public interface UserChangeGameListener extends Listener {

    /**
     * This method is called every time a user changed its game.
     *
     * @param api The api.
     * @param user The user with the updated game.
     * @param oldGame The old game of the user. May be <code>null</code>.
     */
    public void onUserChangeGame(DiscordAPI api, User user, String oldGame);

}
