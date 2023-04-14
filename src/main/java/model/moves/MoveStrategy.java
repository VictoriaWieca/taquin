package model.moves;

import model.Agent;
import model.Position;
import model.path.Graph;

public class MoveStrategy {
    private int horizontally, vertically;
    public final static MoveStrategy _up = new MoveStrategy(-1, 0),
                                    _down = new MoveStrategy(1, 0),
                                    _left = new MoveStrategy(0, -1),
                                    _right = new MoveStrategy(0, 1);

    /**
     * Move strategy
     * @param v Vertical delta
     * @param h Horizontal delta
     */
    private MoveStrategy(int v, int h) {
        horizontally = h;
        vertically = v;
    }

    /**
     * Move the agent given the horizontally and vertically deltas
     * @param agent Agent to move
     * @return true if agent has been moved, false else
     */
    public boolean move(Agent agent) {
        Position newPos = getNewPos(agent);
        if(Agent.getPlateau().checkPosition(newPos)
                && Agent.getPlateau().isFree(newPos)) {
            agent.setPosition(newPos);
            return true;
        }
        return false;
    }

    public Position getNewPos(Agent agent) {
        return new Position(agent.getPosition().getX() + vertically,
                agent.getPosition().getY() + horizontally);
    }

    static public Graph.direction getDirection(Position origin, Position target) {
        if(origin.Manhattan(target) == 1) {
            if(origin.getX() > target.getX()) {
                return Graph.direction.up;
            }
            if(origin.getX() < target.getX()) {
                return Graph.direction.down;
            }
            if(origin.getY() > target.getY()) {
                return Graph.direction.left;
            }
            return Graph.direction.right;
        }
        return null;
    }
}
