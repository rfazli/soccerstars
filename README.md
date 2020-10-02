# Soccer Stars Agent

## about 
Write your smart code and run it on the game with this framework.
It is enough to implement only one function.
As the function input, the coordinates of the players and the ball are sent and you only need to return the player number and the degree of shooting and power.
This framework recognizes the coordinates of the players, the ball and the turn of the team and executes the algorithm of the desired team.
You can fight between two algorithms or you can fight our algorithm with a human.

![alt text](https://github.com/rfazli/soccerstars/blob/master/image.jpg?raw=true)


## implement class
It is enough to implement only one function.
```java
public interface TeamLogic {
    Action play(List<Point> myTeam, List<Point> secondTeam, Point ball);
}
```

sample implement, random shoot to ball:
```java
public class MyTeamLogic implements TeamLogic {

    private OpenCvUtils openCvUtils = new OpenCvUtils();

    @Override
    public Action play(List<Point> myTeam, List<Point> secondTeam, Point ball) {
        if (myTeam == null || myTeam.isEmpty() || ball == null)
            return null;

        Action action = new Action();
        int player = new Random().nextInt(myTeam.size());
        action.setPlayer(player);
        action.setPower(100);
        float angle = openCvUtils.getAngle(myTeam.get(player), ball);
        action.setAngle(angle);

        return action;
    }
}
```
