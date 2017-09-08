package Canny;

import Map.Map;
import sample.Controller;

import static sample.Controller.*;

/**
 * Created by Marcin on 06.09.2017.
 */

public class RobotOrders {
    public static void sendOrder(String message)  {

        Controller controller = new Controller();
       /*try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        switch (message){
            case "turnRight":
                turnRight();
                break;
            case "turnLeft":
                turnLeft();
                break;
            case "goAhead":
                goAhead();
                break;
            case "obrotPrawo90":
                obrotPrawo90();
                break;
            case "obrotLewo90":
                obrotLewo90();
                break;
            case "nextTile":
                nextTile();
                System.out.println("wykryto kafel");
                break;
            case "leftWallDetected":
                leftWallDetected();
                break;
            case "rightWallDetected":
                rightWallDetected();
                break;
            case "frontWallDetected":
                frontWallDetected();
                break;
        }
    }

   public static void frontWallDetected() {
        mapAction.addOneAction("straight", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upDir==true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
        if(downDir == true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
        if(leftDir == true){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(rightDir == true){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
    }

    public static void rightWallDetected() {
        mapAction.addOneAction("right", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upDir==true){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if(downDir == true){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(leftDir == true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
        if(rightDir == true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
    }

    public static void leftWallDetected() {
        mapAction.addOneAction("left", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upDir==true){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(downDir == true){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if(leftDir == true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
        if(rightDir == true){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
    }


    public static void obrotPrawo90() {
        xconYcon.add(Map.getxCon(), Map.getyCon());
        if (upDir==true) {
            downDir = false;
            upDir = false;
            rightDir = true;
            leftDir = false;
            mapAction.addOneAction(null, "rightButtonDir");
        } else if (rightDir == true) {
            downDir = true;
            upDir = false;
            rightDir = false;
            leftDir = false;
            mapAction.addOneAction(null, "downButtonDir");
        } else if (downDir == true) {
            downDir = false;
            upDir = false;
            rightDir = false;
            leftDir = true;
            mapAction.addOneAction(null, "leftButtonDir");
        } else if (leftDir == true) {
            downDir = false;
            upDir = true;
            rightDir = false;
            leftDir = false;
            mapAction.addOneAction(null, "upButtonDir");
        }

    }

    public static void obrotLewo90() {
        xconYcon.add(Map.getxCon(), Map.getyCon());
        if (upDir==true) {
            downDir = false;
            upDir = false;
            rightDir = false;
            leftDir = true;
            mapAction.addOneAction(null, "leftButtonDir");
        } else if (leftDir == true) {
            downDir = true;
            upDir = false;
            rightDir = false;
            leftDir = false;
           // label.setText("Down");
            mapAction.addOneAction(null, "downButtonDir");
        } else if (downDir == true) {
            downDir = false;
            upDir = false;
            rightDir = true;
            leftDir = false;
           // label.setText("Right");
            mapAction.addOneAction(null, "rightButtonDir");
        } else if (rightDir == true) {
            downDir = false;
            upDir = true;
            rightDir = false;
            leftDir = false;
           // label.setText("Up");
            mapAction.addOneAction(null, "upButtonDir");
        }
    }

    public static void goAhead() {
        System.out.println("wyslanie info o jezdzie prosto ");
    }

    public static void nextTile() {
        mapAction.addOneAction("go", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        // sprawdzanie czy punkt jest na mapie i czy nie jest sciana
        if(upDir==true
                && Map.getyCon() -2>=0
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() -1].isWall )
            Map.setyCon(Map.getyCon()-1);
        if(downDir == true
                && Map.getyCon() +2<Map.getyTiles()
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() +1].isWall)
            Map.setyCon(Map.getyCon()+1);
        if(leftDir == true
                && Map.getxCon()-2>=0
                && !Map.getGrid()[Map.getxCon()-1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()-1);
        if(rightDir == true
                && Map.getxCon()+2<Map.getxTiles()
                && !Map.getGrid()[Map.getxCon()+1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()+1);
        //otwieranie nowego kafla
        Map.getGrid()[Map.getxCon()][Map.getyCon() ].open();
    }

    public static void turnRight() {
        System.out.println("wyslanie info o skrecie w prawo ");
    }

    public static void turnLeft() {
        System.out.println("wyslanie info o skrecie w lewo ");
    }

}
