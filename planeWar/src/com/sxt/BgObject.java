package com.sxt;

import java.awt.*;

public class BgObject extends GameObject{

    //得分
    int score = 0;

    /**
     * 背景的实体类
     */
    public BgObject() {
        super();
    }

    public BgObject(String img, GameWin frame) {
        super(img, frame);
    }

    public BgObject(String img, int x, int y, double speed, GameWin frame) {
        super(img, x, y, speed, frame);
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img,x,y,null);
        //背景图循环
        if(y >= 0){
            y = -360;
        }
        //背景图移动
        y += speed;

        //积分面板
        g.setFont(new Font("楷体",Font.BOLD,40));
        g.setColor(Color.GREEN);
        g.drawString("score:" + score,50,100);
    }

    @Override
    public Rectangle getRec() {
        return null;
    }
}
