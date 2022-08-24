package com.sxt;

import java.awt.*;

public class BossObj extends GameObject{

    int HP = 10;

    public BossObj() {
        super();
    }

    public BossObj(String img, GameWin frame) {
        super(img, frame);
    }

    public BossObj(String img, int x, int y, double speed, GameWin frame) {
        super(img, x, y, speed, frame);
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img,x,y,null);
        //控制飞机运行方向
        if(x > 480){
            speed = -5;
        }
        if(x < 0){
            speed = 5;
        }
        x += speed;

        //boss与我方子弹进行碰撞检测
        for(ShellObj shellObj : this.frame.shellObjList){
            if(this.getRec().intersects(shellObj.getRec())){
                //我方子弹击中boss
                shellObj.x = -100;
                shellObj.y = 100;
                this.frame.removeList.add(shellObj);
                HP --;
            }
            if(HP < 0){
                this.frame.state = 4;
            }
        }
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x,y,100,100);
    }
}
