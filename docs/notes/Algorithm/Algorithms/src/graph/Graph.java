package graph;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;

/**
 * 使用邻接链表实现图
 */
public class Graph {

    // 存储顶点的数组
    private VertextNode[] vertextNodes;

    // 图中顶点的个数
    private int vertextSize;

    /**
     * 初始化底层顶点数组
     *
     * @param numOfVertext 图时顶点个数
     */
    public Graph(int numOfVertext) {
        vertextNodes = new VertextNode[numOfVertext];
    }

    /**
     * 插入顶点
     *
     * @param data 顶点信息
     */
    public void insertVertext(int data) {
        VertextNode node = new VertextNode(data);
        vertextNodes[vertextSize++] = node;
    }

    /**
     * 插入边
     *
     * @param vertext1 顶点在数组中对应的索引
     * @param vertext2
     * @return
     */
    public boolean insertEdge(int vertext1, int vertext2) {
        if (vertext1 < 0 || vertext2 < 0 || vertext1 >= vertextSize || vertext2 >= vertextSize) {
            return false;
        }
        AdjacencyNode node1 = new AdjacencyNode(vertext1);
        node1.setNext(vertextNodes[vertext2].getNode());
        vertextNodes[vertext2].setNode(node1);

        AdjacencyNode node2 = new AdjacencyNode(vertext2);
        node2.setNext(vertextNodes[vertext1].getNode());
        vertextNodes[vertext1].setNode(node2);

        return true;
    }


    /**
     * 顶点定义
     */
    class VertextNode {

        // 顶点信息
        int data;
        // 当前顶点的所有邻接顶点构成的单链表的头结点
        AdjacencyNode node;

        public VertextNode(int data) {
            this.data = data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public void setNode(AdjacencyNode node) {
            this.node = node;
        }

        public AdjacencyNode getNode() {
            return node;
        }
    }

    /**
     * 相邻结点定义
     */
    class AdjacencyNode {

        // 邻接顶点编号，即数组中该顶点的下标
        int adjvex;
        // 边的权重值
        int weight;
        // 下一个邻接顶点
        AdjacencyNode next;

        public AdjacencyNode(int adjvex) {
            this.adjvex = adjvex;
        }

        public void setNext(AdjacencyNode next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph(10);
        graph.insertVertext(1);
        graph.insertVertext(2);
        graph.insertVertext(3);
        graph.insertVertext(4);
        graph.insertVertext(5);
        graph.insertVertext(6);

        graph.insertEdge(0, 1);
        graph.insertEdge(0, 2);
        graph.insertEdge(1, 3);
        graph.insertEdge(2, 3);
        graph.insertEdge(2, 4);
        graph.insertEdge(3, 4);
        graph.insertEdge(3, 5);
        graph.insertEdge(4, 5);


    }

}
