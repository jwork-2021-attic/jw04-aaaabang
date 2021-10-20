package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.anish.Monsters.BubbleSorter;
import com.anish.Monsters.Monster;
import com.anish.Monsters.World;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    public Monster[] monsters;
    private Monster[][] matrix;
    String[] sortSteps;

    public static final int MATRIX_WIDTH = 8;
    public static final int MATRIX_HEIGHT = 8;

    public WorldScreen() {
        world = new World();

        final int mycount = 64;

        //这个世界有64个小妖怪
        int monCnt = 64;
        if(monsters == null){
            monsters = new Monster[MATRIX_HEIGHT*MATRIX_WIDTH];
        }
        //从赤道黄
        for(int i = 0;i < 16;i++){
            monsters[monCnt-1] = new Monster(new Color(255-i*5,i*15,0), monCnt, world);
            monCnt -= 1;
        }
        //从黄到绿
        for(int i = 16;i > 0;i--){
            monsters[monCnt-1] = new Monster(new Color(i*15,255-i*5,0), monCnt, world);
            monCnt -= 1;
        }
        //从绿到青
        for(int i = 0;i < 16;i++){
            monsters[monCnt-1] = new Monster(new Color(0,255-i*5,i*10), monCnt, world);
            monCnt -= 1;
        }
        //从青到蓝
        for(int i = 16;i > 0;i--){
            monsters[monCnt-1] = new Monster(new Color(0,i*10,255-i*5), monCnt, world);
            monCnt -= 1;
        }

        if (matrix == null) {
            matrix = new Monster[MATRIX_WIDTH][MATRIX_HEIGHT];
        }

        //将64个小妖怪随机排列入矩阵。
        Random r = new Random();
        for(int i = 0;i < mycount;i++)
        {
            int row = r.nextInt(MATRIX_HEIGHT);
            int col = r.nextInt(MATRIX_WIDTH);
            while(matrix[row][col] != null){
                row = r.nextInt(MATRIX_HEIGHT);
                col = r.nextInt(MATRIX_WIDTH);
            }
            matrix[row][col] = monsters[i];
            world.put(matrix[row][col], 10+col*2, 10+row*2);
        }

        BubbleSorter<Monster> b = new BubbleSorter<>();
        b.load(matrix);
        b.sort();

        sortSteps = this.parsePlan(b.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Monster[][] monsters, String step) {
        String[] couple = step.split("<->");
        getMonByRank(monsters, Integer.parseInt(couple[0])).swap(getMonByRank(monsters, Integer.parseInt(couple[1])));
    }

    private Monster getMonByRank(Monster[][] monsters, int rank) {
        for (Monster[] line : monsters) {
            for(Monster mon : line)
            {
                if (mon.getRank() == rank) {
                    return mon;
                }
            }
            
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (i < this.sortSteps.length) {
            this.execute(matrix, sortSteps[i]);
            i++;
        }

        return this;
    }

}
