import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();
        Queue<Node> openList = new PriorityQueue<>(Comparator.comparing(Node::getF));

        //Map represents openList with ease of checking/comparing
        Map<Point, Node> nodeMap = new HashMap<>();
        Map<Point, Node> closedList = new HashMap<>();

        //Initializes the H value from the start node to the end node
        double ogH = Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2) * 1.0);

        //Starts with the current node
        Node cur = new Node(null, start, 0, ogH, ogH);
        nodeMap.put(cur.curPos, cur);
        openList.add(cur);
        //Looks until the openList is empty
        while(!(withinReach.test(cur.curPos, end) || openList.isEmpty())){

         // while(!(openList.isEmpty())){

            //Creates list of neighbors

            ArrayList<Point> neighbors = (ArrayList<Point>) potentialNeighbors.apply(cur.curPos)
                    .filter(point -> canPassThrough.test(point))
                    //.filter(point -> !point.equals(start) && !point.equals(end))
                    .collect(Collectors.toList());

            for(Point p: neighbors)
            {
                //Checks if already in openList
                if (!(closedList.containsKey(p))) {
                    // Checks if in the open list map
                    if(!(nodeMap.containsKey(p))){

                        Node node = new Node(cur, p, cur.getG() + 1.00, p.hVal(end), (cur.getG() + 1.00) + p.hVal(end));
                        nodeMap.put(p, node);
                        openList.add(node);

                    } else {
                        Node tempNode = nodeMap.get(p);
                        //creates the potential g value for node
                        double gVal = cur.g() + 1;
                        if (tempNode.getG() == 0.0 || tempNode.getG() > gVal)//If oldnode has worst g value, replace
                        {

                        nodeMap.remove(p);
                        Node node = new Node(cur, p, cur.getG() + 1.00, p.hVal(end), (cur.getG() + 1.00) + p.hVal(end));
                        nodeMap.put(p, node);
                        openList.add(node);

                        }
                    }
                }

            }
            closedList.put(cur.curPos, cur);
            //If the path is empty, this is required or else will crash
            if(openList.size() == 0){
                System.out.println("open list empty");
                return null;
            }
            cur = openList.remove();
        }
        //makes path list
        while (cur.getPriorNode() != null){
            if (cur.curPos != end){
                path.add(0, cur.curPos);
                cur = cur.getPriorNode();
            }
        }
        return path;
    }

    private class Node{
        private Node priorNode;
        private Point curPos;
        private double g;
        private double h;
        private double f;

        public Node(Node priorNode, Point curPos, double g, double h, double f){
            this.priorNode = priorNode;
            this.curPos = curPos;
            this.g = g;
            this.h = h;
            this.f = f;
        }

        public Node getPriorNode() { return priorNode;}
        public Point getcurPos() { return curPos; }
        public double getG() { return g; }
        public double getH() { return h; }
        public double getF() { return f; }
        public void setG(double val) { this.g = val; }
        public void setH(double val) { this.h = val; }
        public void setF(double val) { this.f = val; }

        public double g() {
            if (priorNode == null) {
                return 0;
            }
            return priorNode.g + 1.00;
        }

        public double f() { return g + h;}

        public double h(Point end){
            return Math.sqrt(Math.pow(curPos.x - end.x, 2) + Math.pow(curPos.y - end.y, 2) * 1.0);
        }

        public boolean equals(Object o){
            if (o == null) { return false; }
            if (o.getClass() != this.getClass()) { return false; }
            Node n = (Node)o;

            boolean result = true;

            if (priorNode == null){
                result = n.priorNode == null;
            } else {
                result = this.priorNode.equals(n.priorNode);
            }

            if (curPos == null){
                result = result && n.curPos == null;
            } else {
                result = result && this.curPos.equals(n.curPos);
            }

            return result = result && this.g == n.g && this.h == n.h && this.f == n.f;
        }

        public int hashCode(){
            return Objects.hash(priorNode, curPos, g, h, f);
        }

    }

    /*
    Function<Point, Stream<Point>> n = PathingStrategy.CARDINAL_NEIGHBORS;
            Stream<Point> neighbors = n.apply(cur.curPos);

            neighbors.forEach(p -> {

                boolean flag = true;
                for(Node node: openList){
                    if (node.curPos.equals(p)) { flag = false; }
                }
                if (flag) {
                    double g = cur.getG() + 1.00;
                    double h = p.hVal(end);
                    double f = g + h;
                    openList.add(new Node(cur, p, g, h, f));
                }
            });
     */
}
