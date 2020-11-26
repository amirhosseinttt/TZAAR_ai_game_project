import com.sun.rowset.internal.Row;
import models.*;

import java.util.ArrayList;

public class OptimizedAI extends Player {

    protected int doneActions = 0;
    protected int maxDepth = 2;

    public OptimizedAI(PlayerType type) {
        super(type);
    }

    @Override
    public Action forceAttack(Game game) {
        if (doneActions == 0 && getType() == PlayerType.white) {
            int maxValue = Integer.MIN_VALUE;
            Action bestAction = null;
            ArrayList<Action> actions = getAllActions(game.getBoard());
            for (Action action : actions) {
                if (action.getType() == Action.ActionType.attack) {
                    Game copyGame = game.copy();
                    if (copyGame.applyActionTwo(this, action, true)) {
                        continue;
                    }
                    Player winner = copyGame.getWinner();
                    if (winner != null) {
                        if (winner.getType() == getType()) {
                            return action;
                        }
                    } else {
                        int temp = Math.max(maxValue, minForceAttack(game, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));
                        if (temp > maxValue) {
                            maxValue = temp;
                            bestAction = action;
                        }
                    }
                }
            }
            doneActions++;
            return bestAction;
        } else {
            int maxValue = Integer.MIN_VALUE;
            Action bestAction = null;
            ArrayList<Action> actions = getAllActions(game.getBoard());
            for (Action action : actions) {
                if (action.getType() == Action.ActionType.attack) {
                    Game copyGame = game.copy();
                    if (copyGame.applyActionTwo(this, action, true)) {
                        continue;
                    }
                    Player winner = copyGame.getWinner();
                    if (winner != null) {
                        if (winner.getType() == getType()) {
                            return action;
                        }
                    } else {
                        int temp = Math.max(maxValue, maxSecondMove(game, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));
                        if (temp > maxValue) {
                            maxValue = temp;
                            bestAction = action;
                        }
                    }
                }
            }
            doneActions++;
            return bestAction;
        }
    }

    int eval(Game game) {
        int maxCount = 0;
        int minCount = 0;
        for (Board.BoardRow boardRow : game.getBoard().getRows()) {
            for (Board.BoardCell cell : boardRow.boardCells) {
                if (cell.bead != null) {
                    if (cell.bead.getPlayer().getType() == this.getType()) {
                        maxCount += cell.bead.getHeight();
                    } else {
                        minCount += cell.bead.getHeight();
                    }
                }
            }
        }

        return maxCount - minCount;
    }

    @Override
    public Action secondAction(Game game) {
        int maxValue = Integer.MIN_VALUE;
        Action bestAction = null;
        ArrayList<Action> actions = getAllActions(game.getBoard());
        for (Action action : actions) {
            Game copyGame = game.copy();
            if (copyGame.applyActionTwo(this, action, false)) {
                continue;
            }
            Player winner = copyGame.getWinner();
            if (winner != null) {
                if (winner.getType() == getType()) {
                    return action;
                }
            } else {
                int temp = Math.max(maxValue, minForceAttack(game, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));
                if (temp > maxValue) {
                    maxValue = temp;
                    bestAction = action;
                }
            }
        }
        doneActions++;
        return bestAction;
    }

    protected int maxForceAttack(Game game, int depth, int alpha, int beta) { //useless alpha beta
        if (depth == maxDepth) {
            return eval(game);
        }

        int maxValue = Integer.MIN_VALUE;
        ArrayList<Action> actions = getAllActions(game.getBoard());
        for (Action action : actions) {
            if (action.getType() == Action.ActionType.attack) {
                Game copyGame = game.copy();
                if (copyGame.applyActionTwo(this, action, true)) {
                    continue;
                }
                Player winner = copyGame.getWinner();
                if (winner != null) {
                    if (winner.getType() == getType()) {
                        return Integer.MAX_VALUE;
                    }
                } else {
                    maxValue = Math.max(maxValue, maxSecondMove(copyGame, depth + 1, alpha, beta));
                }
            }
        }
        return maxValue;
    }

    protected int maxSecondMove(Game game, int depth, int alpha, int beta) {
        if (depth == maxDepth) {
            return eval(game);
        }

        int maxValue = Integer.MIN_VALUE;
        ArrayList<Action> actions = getAllActions(game.getBoard());
        for (Action action : actions) {
            Game copyGame = game.copy();
            if (copyGame.applyActionTwo(this, action, false)) {
                continue;
            }
            Player winner = copyGame.getWinner();
            if (winner != null) {
                if (winner.getType() == getType()) {
                    return Integer.MAX_VALUE;
                }
            } else {
                int eval = minForceAttack(game, depth + 1, alpha, beta);
                maxValue = Math.max(maxValue, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
        }
        return maxValue;
    }

    protected int minForceAttack(Game game, int depth, int alpha, int beta) { //useless alpha beta
        if (depth == maxDepth) {
            return eval(game);
        }

        int minValue = Integer.MAX_VALUE;
        ArrayList<Action> actions = getAllActions(game.getBoard());
        for (Action action : actions) {
            if (action.getType() == Action.ActionType.attack) {
                Game copyGame = game.copy();
                if (copyGame.applyActionTwo(this, action, true)) {
                    continue;
                }
                Player winner = copyGame.getWinner();
                if (winner != null) {
                    if (winner.getType() == getType().reverse()) {
                        return Integer.MIN_VALUE;
                    }
                } else {
                    minValue = Math.min(minValue, minSecondMove(game, depth + 1, alpha, beta));
                }
            }
        }
        return minValue;

    }

    protected int minSecondMove(Game game, int depth, int alpha, int beta) {
        if (depth == maxDepth) {
            return eval(game);
        }

        int minValue = Integer.MAX_VALUE;
        ArrayList<Action> actions = getAllActions(game.getBoard());
        for (Action action : actions) {
            Game copyGame = game.copy();
            if (copyGame.applyActionTwo(this, action, false)) {
                continue;
            }
            Player winner = copyGame.getWinner();
            if (winner != null) {
                if (winner.getType() == getType().reverse()) {
                    return Integer.MIN_VALUE;
                }
            } else {
                int eval = maxForceAttack(game, depth + 1, alpha, beta);
                minValue = Math.min(minValue, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
        }

        return minValue;
    }
}
