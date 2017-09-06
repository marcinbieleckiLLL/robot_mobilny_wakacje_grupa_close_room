package Canny;

/**
 * Created by Marcin on 06.09.2017.
 */

public class RobotOrders {

    public static void sendOrder(String message)  {
       /* try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        switch (message){
            case "turnRight":
                System.out.println("PRAWO");
                break;
            case "turnLeft":
                System.out.println("LEWO");
                break;
            case "goAhead":
                System.out.println("PROSTO");
                break;
            case "obrotPrawo90":
                break;
            case "obrotLewo90":
                break;
            case "nextTile":
                System.out.println("wykryto kafel");
                break;
            case "leftWallDetected":
                break;
            case "rightWallDetected":
                break;
            case "frontWallDetected":
                break;
        }
    }

}
