/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import asciiPanel.AsciiPanel;

/**
 *
 * @author Aeranythe Echosong
 */
public class StartScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {


        terminal.write("This is the start screen.", 10, 15,AsciiPanel.blue,AsciiPanel.white);
        terminal.write("Press ENTER to continue...", 10, 17,AsciiPanel.red,AsciiPanel.white);
      //  terminal.write("Press A to get a maze runner", 10, 19,AsciiPanel.yellow,AsciiPanel.white);

    }

}
