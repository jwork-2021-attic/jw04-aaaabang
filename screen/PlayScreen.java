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

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    
    private int maze_dim;
    int[][] maze;
  
    // int[][] visit;
    // int[][] dis;

    public PlayScreen() {
        
        this.screenHeight = 60;
        this.maze_dim = 40;
        this.screenWidth = maze_dim;
        // this.runner = runner;
        // visit = new int[maze_dim][maze_dim];

        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);

        // if(runner == true){
        //     dfs(0, 0);
        // }
    }

    /* private void next(int a,int b,int x, int y)
    // {
        
    //     if (visit[x][y] == 0) { dfs(x, y); }
    // }
    // // 深度优先
    // private void dfs(int x, int y)
    // {
    //     visit[x][y] = 1;
    //     if(x == maze_dim-1 && y == maze_dim-1)
    //     {
    //         return;
    //     }
             
    //     if (x + 1 < maze_dim && maze[x + 1][y] == 1) //右
    //     { 
    //         player.moveBy(1, 0);
    //         next(x, y, x + 1, y);
    //         player.moveBy(-1, 0);
    //     }
    //     if (x - 1 >=0 && maze[x - 1][y] == 1) //左
    //     {
    //         player.moveBy(-1, 0);
    //         next(x, y, x - 1, y);
    //         player.moveBy(1, 0);
    //     }
    //     if (y + 1 < maze_dim && maze[x][y + 1] == 1) //下
    //     {
    //         player.moveBy(0, 1);
    //         next(x, y, x, y+1);
    //         player.moveBy(0, -1);
    //     }
    //     if (y - 1 >=0 && maze[x][y - 1] == 1) //上
    //     {
    //         player.moveBy(0, -1);
    //         next(x, y, x, y-1);
    //         player.moveBy(0, 1);
    //     }
        
    // }*/


    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages);

        // for (int i = 0; i < 8; i++) {
        //     creatureFactory.newFungus();
        // }
    }

    private void createWorld() {
        WorldBuilder worldBuilder = new WorldBuilder(maze_dim, maze_dim);
        world = worldBuilder.generateMaze().build();
        maze = worldBuilder.getMaze();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < maze_dim; x++) {
            for (int y = 0; y < maze_dim; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.gray);
                }
            }
        }

        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                //if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                //}
            }
        }
        if(player.x() == maze_dim-1 && player.y() == maze_dim-1)
        {
            chanToWinScreen(terminal);
            return;
        }

        //show destination
        terminal.write((char)3,maze_dim-1,maze_dim-1,Color.PINK);

        // Creatures can choose their next action now
        world.update();
    }

    private void chanToWinScreen(AsciiPanel terminal){
        WinScreen win = new WinScreen();
        win.displayOutput(terminal);

    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // // Stats
        // String stats = String.format("%3d/%3d hp", player.hp(), player.maxHP());
        // terminal.write(stats, 1, 23);
        // Messages
        displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.moveBy(0, 1);
                break;
            default:
                break;
        }
        return this;
    }



    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
