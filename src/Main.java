import models.Game;
import models.Player;
import models.PlayerType;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        RandomPlayer whitePlayer = new RandomPlayer(PlayerType.white);
        OptimizedAI blackPlayer = new OptimizedAI(PlayerType.black);
//        Ai blackPlayer = new Ai(PlayerType.black);
//        ThreadedAI blackPlayer = new ThreadedAI(PlayerType.black);
        Game game = new Game(whitePlayer, blackPlayer);
        Player player = game.play();
        System.out.println(player.getType());
        System.out.println("the game last for " + (System.currentTimeMillis() - time) / 1000 + " seconds");
    }

}
