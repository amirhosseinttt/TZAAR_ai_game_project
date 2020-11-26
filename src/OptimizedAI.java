import models.Action;
import models.Game;
import models.Player;
import models.PlayerType;

import java.util.ArrayList;

public class OptimizedAI extends Ai {

    public OptimizedAI(PlayerType type) {
        super(type);
        this.maxDepth = 2;
    }


}
