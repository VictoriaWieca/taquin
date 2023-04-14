package model.path;

import javafx.application.Platform;
import model.Position;

import java.util.*;

public class Graph {
    static private Node nodes[][];
    public enum direction{none, up, down, left, right}

    static public void init(int height, int width) {
        nodes = new Node[height][width];
        for(int i = 0; i < nodes.length; i++) {
            for(int j = 0; j < nodes[i].length; j++) {
                nodes[i][j] = new Node(new Position(i, j));
            }
        }

        for(int i = 0; i < nodes.length; i++) {
            for(int j = 0; j < nodes[i].length; j++) {
                List<Edge> edges = new ArrayList<>();
                if(i > 0) {
                    edges.add(new Edge(nodes[i-1][j]));
                }
                if(i < (nodes.length - 1)) {
                    edges.add(new Edge(nodes[i+1][j]));
                }
                if(j > 0) {
                    edges.add(new Edge(nodes[i][j-1]));
                }
                if(j < (nodes[i].length - 1)) {
                    edges.add(new Edge(nodes[i][j+1]));
                }
                //Copy
                Edge tab[] = new Edge[edges.size()];
                for(int k = 0; k < edges.size(); k++) {
                    tab[k] = edges.get(k);
                }
                nodes[i][j].adjacencies = tab;
            }
        }
    }

    public static synchronized void setPrio(Position pos, int prio) {
        Platform.runLater(() -> {
            nodes[pos.getX()][pos.getY()].setPrio((nodes.length * nodes[0].length + 1) - prio);
        });
    }

    private static int computeDist(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    public static synchronized List<direction> AstarSearch(Position src, Position end, int agentId) {
        Node source = nodes[src.getX()][src.getY()];
        Node goal = nodes[end.getX()][end.getY()];
        source.setHVal(computeDist(src, end), agentId);

        Set<Node> explored = new HashSet<>();

        PriorityQueue<Node> queue = new PriorityQueue<>(
                20,
                Comparator.comparingDouble(i -> i.f_scores)
        );

        //cost from start
        source.g_scores = 0;

        queue.add(source);

        boolean found = false;

        while((!queue.isEmpty()) && (!found)){

            //the node in having the lowest f_score value
            Node current = queue.poll();

            explored.add(current);

            //goal found
            if (current.equals(goal)) {
                found = true;
            }

            //check every child of current node
            for (Edge e : current.adjacencies) {
                boolean testDirect = true;
                Node child = e.target;
                if(current.equals(source)
                        && !child.isFree()
                        && child.equals(goal)) {
                    testDirect = (Math.random() > 0.5);
                }
                if(testDirect) {
                    child.setHVal(computeDist(child.pos, end), agentId);
                    int cost = e.cost + (2 * child.prio) + child.free + (int)(Math.random() * 40);
                    if((child.blocked != 0) && (Math.random() > 0.5)) {
                        cost *= child.blocked;
                    }
                    int temp_g_scores = current.g_scores + cost;
                    int temp_f_scores = temp_g_scores + child.h_scores.get(agentId);

                    /* If it's the first time we explore the child
                     * or we have a better score than previously
                     */
                    if ((!explored.contains(child)) ||
                            (temp_f_scores < child.f_scores)) {

                        child.parent = current;
                        child.g_scores = temp_g_scores;
                        child.f_scores = temp_f_scores;

                        queue.remove(child);
                        queue.add(child);

                    }
                }
            }

        }

        if(found) {
            //Reconstruct path
            Stack<Node> order = new Stack<>();
            Node current = goal;
            do {
                order.push(current);
                current = current.parent;
            } while (!current.pos.equals(src));

            List<direction> dirs = new ArrayList<>();
            while (!order.empty()) {
                Node tmp = order.pop();
                dirs.add(getDir(current.pos, tmp.pos));
                current = tmp;
            }

            return dirs;
        } else {
            return null;
        }
    }

    static public synchronized void setFree(Position p, boolean free) {
        nodes[p.getX()][p.getY()].setFree(free);
    }

    static public synchronized void block(Position p) {
        nodes[p.getX()][p.getY()].block();
    }

    static public synchronized void ghostUp(Position p) {
        Platform.runLater(() -> {
            nodes[p.getX()][p.getY()].ghost += 5;
        });
    }

    static public synchronized void ghostUp(Position p, int modif) {
        Platform.runLater(() -> {
            nodes[p.getX()][p.getY()].ghost += modif;
        });
    }

    static private direction getDir(Position src, Position dest) {
        int dx = src.getX() - dest.getX();
        if(dx == 1) {
            return direction.up;
        }
        if(dx == -1) {
            return direction.down;
        }
        int dy = src.getY() - dest.getY();
        if(dy == 1) {
            return direction.left;
        }
        if(dy == -1) {
            return direction.right;
        }
        return direction.none;
    }

    static class Node {

        final Position pos;
        int g_scores;
        HashMap<Integer, Integer> h_scores;
        int f_scores = 0;
        int ghost = 0;
        int prio = 0;
        Edge[] adjacencies;
        Node parent;
        int free, blocked;

        Node(Position p) {
            pos = p;
            h_scores = new HashMap<>();
            free = 0;
            blocked = 0;
        }

        public void setFree(boolean fr) {
            free = fr ? 0 : 15;
            blocked = 0;
        }

        public void block() {
            blocked = 3;
        }

        public boolean isFree() {
            return free == 0;
        }

        void setHVal(int dist, int agentId) {
            h_scores.put(agentId, dist);
        }

        public String toString() {
            return pos.toString();
        }

        public void setPrio(int p) {
            prio = p;
        }


        @Override
        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }
            if(o instanceof Node) {
                return ((Node) o).pos.equals(pos);
            }
            return false;
        }

    }

    static class Edge {
        final int cost;
        final Node target;

        Edge(Node targetNode){
            target = targetNode;
            cost = 1;
        }
    }
}
