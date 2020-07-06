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
package FullBloomBot.ZacharyTalis.listener.message;

import FullBloomBot.ZacharyTalis.entities.message.Message;
import FullBloomBot.ZacharyTalis.listener.Listener;
import FullBloomBot.ZacharyTalis.DiscordAPI;

import javax.sound.midi.InvalidMidiDataException;

/**
 * This listener listens to message creations.
 */
public interface MessageCreateListener extends Listener {

    /**
     * This method is called every time a new message is created.
     *
     * @param api The api.
     * @param message The created message.
     */
    public void onMessageCreate(DiscordAPI api, Message message) throws InvalidMidiDataException;

}
