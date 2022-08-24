package com.sxt;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GameWin extends JFrame {

    /** 定义双缓存图片 */
    Image offScreenImage = null;

    //游戏重绘次数
    int count = 1;
    //记录敌方小飞机数量
    int enemyCount = 0;

    //游戏状态 0未开始 1运行中 2暂停 3失败 4成功
    int state = 0;
    int MY_WIDTH = 600;
    int MY_HEIGHT = 600;

    //爆炸效果图
    Image explode = Toolkit.getDefaultToolkit().getImage("imgs/explode/boom.png");
    Image bg = Toolkit.getDefaultToolkit().getImage("imgs/explode/bg.png");
    //定义一个大的集合
    List<GameObject> objectList = new ArrayList<>();
    //被删除物体的集合
    List<GameObject> removeList = new ArrayList<>();
    //我方子弹的集合
    List<ShellObj> shellObjList = new ArrayList<>();
    //敌方子弹的集合
    List<BulletObj> bulletObjList = new ArrayList<>();
    //敌方战斗机集合
    List<EnemyObj> enemyObjList = new ArrayList<>();

    //背景的实体类
    BgObject bgObj = new BgObject("imgs/explode/bg.png",0,-360,1,this);
    //我方战斗机
    PlaneObj planeObj = new PlaneObj("imgs/explode/plane.png",290,550,0,this);
    //敌方Boss
    BossObj bossObj = null;

    //窗口的启动方法
    public void launch(){
        //窗口可视
        setVisible(true);
        //窗口尺寸
        setSize(MY_WIDTH,MY_HEIGHT);
        //窗口位置
        setLocationRelativeTo(null);
        //窗口标题
        setTitle("飞机大战");

        //将游戏物体添加到大集合中
        objectList.add(bgObj);
        objectList.add(planeObj);

        //为窗口添加开始鼠标事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键代号是1
                if(e.getButton() == 1){
                    state = 1;
                    repaint();
                }
            }
        });
        while (true){
            if (state == 1){
                creatObj();
                repaint();
            }

            try {
                //线程休眠
                Thread.sleep(25);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(@NotNull Graphics g) {
        /** 创建和容器一样大小的Image图片 */
        if(offScreenImage == null){
            offScreenImage = this.createImage(MY_WIDTH,MY_HEIGHT);
        }
        /** 获得该图片的画布 */
        Graphics gImage = offScreenImage.getGraphics();
        /** 填充整个画布 */
        gImage.fillRect(0,0,MY_WIDTH,MY_HEIGHT);

        //游戏未开始
        if (state == 0) {
            gImage.drawImage(bg, 0, 0, null);
            //改变画笔颜色
            gImage.setColor(Color.BLUE);
            //改变文字大小和样式
            gImage.setFont(new Font("仿宋", Font.BOLD, 50));
            //添加文字
            gImage.drawString("点击开始游戏", 150, 300);
        }


        //游戏开始
        if (state == 1) {
            objectList.removeAll(removeList);
            //绘制所有游戏物体
            for (GameObject object : objectList){
                object.paintSelf(gImage);
            }
        }

        //游戏暂停
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                //esc被按下
                if(e.getKeyCode() == 27){
                    switch (state){
                        //运行改为暂停
                        case 1:
                            state = 2;
                            break;
                        //暂停改为运行
                        case 2:
                            state = 1;
                            break;
                    }
                }
            }
        });

        //游戏失败
        if (state == 3) {
            //改变画笔颜色
            gImage.setColor(Color.RED);
            //改变文字大小和样式
            gImage.setFont(new Font("仿宋", Font.BOLD, 50));
            //添加文字
            gImage.drawString("GAME OVER", 180, 300);
        }

        //游戏通关
        if (state == 4) {
            //改变画笔颜色
            gImage.setColor(Color.GREEN);
            //改变文字大小和样式
            gImage.setFont(new Font("仿宋", Font.BOLD, 50));
            //添加文字
            gImage.drawString("通关！", 200, 300);
        }
        gImage.drawImage(explode,200,200,null);

        /** 将缓冲区绘制好的图形整个绘制到容器的画布中 */
        g.drawImage(offScreenImage,0,0,null);
        //count自增
        count ++;
    }

    //添加子弹或敌机
    public void creatObj(){
        //我方子弹生成
        //控制我方子弹生成速度
        if (count % 10 == 0){
            shellObjList.add(new ShellObj("imgs/explode/bulletGreen.png",planeObj.x + 3,planeObj.y - 10,10,this));
            objectList.add(shellObjList.get(shellObjList.size() - 1));
        }
        //敌方子弹生成
        //敌方子弹生成速度
        if (count % 10 == 0 && bossObj != null){
            bulletObjList.add(new BulletObj("imgs/explode/bulletYellow.png",bossObj.x + 60,bossObj.y + 60,10,this));
            objectList.add(bulletObjList.get(bulletObjList.size()-1));
        }
        //生成敌方小飞机
        if (count % 20 == 0) {
            enemyObjList.add(new EnemyObj("imgs/explode/enemy.png",this));
            objectList.add(enemyObjList.get(enemyObjList.size() - 1));
            enemyCount ++;
        }
        if(enemyCount >= 50){
           if(bossObj == null){
               bossObj = new BossObj("imgs/explode/boss.png",240,30,5,this);
               objectList.add(bossObj);
           }
        }
    }

    public static void main(String[] args) {
        GameWin gameWin = new GameWin();
        gameWin.launch();
    }
}
