package main;

import Graphics.Assets;
import Tiles.Tile;
import database.Database;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//mă voi folosi de containerul JPanel pentru butoane, câmpuri text, imagini, tile-uri etc.
public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 32;   //32x32

    public final int maxScreenCol = 38;                       //numărul de coloane
    public final int maxScreenRow = 20;                       //numărul de linii
    public final int screenWidth = tileSize * maxScreenCol;        //1248px
    public final int screenHeight = tileSize * maxScreenRow;       //624px




    int FPS = 60;
    TileManager tileM = new TileManager(this);
    public UI ui = new UI(this);
    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    Sound sound = new Sound();   //introducere sunet joc
    Sound se = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyHandler);
    public SuperObject[] obj = new SuperObject[10];
    public Entity[] npc = new Entity[10];
    public AssetSetter assetSetter = new AssetSetter(this);
    //GAME STATE
    public int gameState;
    public final int titleState=0;
    public final int playState=1;
    public final int pauseState = 2;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));    //setarea dimensiunii ferestrei jocului
        this.setBackground(Color.black);                                   //culoarea de fundal a jocului
        this.setDoubleBuffered(true);                                //pentru o performanță mai bună a randării jocului
        this.addKeyListener(keyHandler);
        this.setFocusable(true);                                     //pentru citirea tastelor
        Assets.Init();
    }

    public void setupGame()
    {
        assetSetter.setObject();
        assetSetter.setNPC();
        playMusic(0);
        gameState = titleState;

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic()
    {
        sound.stop();
    }
    public void playSE(int i) {

        se.setFile(i);
        se.play();

    }
    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {

            player.update();
            for (int i=0; i< npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //TITLE SCREEN
        if (gameState == titleState) {
            Tile.backgroundTile.DrawBackground(g,0,0);
            try {
                ui.draw(g2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else {
            tileM.draw(g2);                          //Încărcare matrice de tile-uri

            for (int i = 1; i <= 29; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 13 * Tile.TILE_WIDTH);
            for (int i = 1; i <= 16; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 18 * Tile.TILE_WIDTH);
            for (int i = 17; i >= 2; i--)
                Tile.streetTile3.DrawStreet(g, 30 * Tile.TILE_WIDTH, i * Tile.TILE_WIDTH);
            Tile.streetTile2.DrawStreet(g, 15 * Tile.TILE_WIDTH, 13 * Tile.TILE_WIDTH);
            for (int i = 18; i >= 4; i--)
                Tile.streetTile3.DrawStreet(g, Tile.TILE_WIDTH, i * Tile.TILE_WIDTH);
            for (int i = 17; i >= 1; i--)
                Tile.streetTile3.DrawStreet(g, 15 * Tile.TILE_WIDTH, i * Tile.TILE_WIDTH);
            for (int i = 14; i >= 6; i--)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            for (int i = 4; i >= 2; i--)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 6 * Tile.TILE_WIDTH);
            for (int i = 6; i <= 8; i++)
                Tile.streetTile3.DrawStreet(g, 5 * Tile.TILE_WIDTH, i * Tile.TILE_WIDTH);
            for (int i = 6; i <= 20; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 8 * Tile.TILE_WIDTH);
            for (int i = 13; i >= 3; i--)
                Tile.streetTile3.DrawStreet(g, 21 * Tile.TILE_WIDTH, i * Tile.TILE_WIDTH);
            for (int i = 21; i <= 30; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 3 * Tile.TILE_WIDTH);
            Tile.streetTile5.DrawStreet2(g, 21 * Tile.TILE_WIDTH, 3 * Tile.TILE_WIDTH);
            Tile.streetTile5.DrawStreet2(g, 30 * Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            for (int i = 31; i <= 33; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            for (int i = 16; i <= 30; i++)
                Tile.streetTile.DrawStreet(g, i * Tile.TILE_WIDTH, 18 * Tile.TILE_WIDTH);  //Străzi

            Tile.treeTile.DrawTree(g, 6 * Tile.TILE_WIDTH, 5 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 6 * Tile.TILE_WIDTH, 6 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 7 * Tile.TILE_WIDTH, 5 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 7 * Tile.TILE_WIDTH, 6 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 6 * Tile.TILE_WIDTH, 11 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 13 * Tile.TILE_WIDTH, 15 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 13 * Tile.TILE_WIDTH, 13 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 11 * Tile.TILE_WIDTH, 9 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 12 * Tile.TILE_WIDTH, 9 * Tile.TILE_WIDTH);
            for (int i = 8; i <= 12; i++)
                Tile.treeTile.DrawTree(g, i * Tile.TILE_WIDTH, 2 * Tile.TILE_WIDTH);
            Tile.treeTile.DrawTree(g, 17 * Tile.TILE_WIDTH, 6 * Tile.TILE_WIDTH);            //Copaci

            Tile.hotelTile.DrawHotel(g2, 34 * Tile.TILE_HEIGHT, 0);
            Tile.dormTile.DrawDorm(g2, 32 * Tile.TILE_WIDTH, 6 * Tile.TILE_HEIGHT);
            Tile.mcdonaldTile.DrawMcDonalds(g2, 16 * Tile.TILE_WIDTH, 9 * Tile.TILE_WIDTH);  //Clădiri
            Tile.kfcTile.DrawKFC(g2, 3 * Tile.TILE_WIDTH, 15 * Tile.TILE_WIDTH);
            Tile.ACTile.Draw2(g2, 0, 0);

            Tile.carTile.DrawOpelCar(g2, 32 * Tile.TILE_WIDTH, 2 * Tile.TILE_HEIGHT);
            Tile.BMWCarTile.DrawBMWCar(g2, 32 * Tile.TILE_WIDTH, 3 * Tile.TILE_HEIGHT);      //Mașini
            Tile.dusterTile.DrawDuster(g2, 32 * Tile.TILE_WIDTH, 4 * Tile.TILE_HEIGHT);

            //OBJECT
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2, this);
                }
            }

            //NPC
            for (int i=0; i<npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            player.draw(g2);

            try {
                ui.draw(g2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            g2.dispose();

        }


    }
}