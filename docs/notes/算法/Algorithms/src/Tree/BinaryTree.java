package Tree;

import java.util.LinkedList;

public class BinaryTree
{
    // 根节点
    private BinaryNode root;
    
    public BinaryTree() { }
    
    public BinaryTree(BinaryNode root)
    {
        this.root = root;
    }
    
    public BinaryNode getRoot()
    {
        return root;
    }
    
    public void setRoot(BinaryNode root)
    {
        this.root = root;
    }
    
    /**
     * 节点类
     */
    public static class BinaryNode
    {
        private String data;
        private BinaryNode left;
        private BinaryNode right;
        
        public BinaryNode(String data, BinaryNode left, BinaryNode right)
        {
            this.data = data;
            this.left = left;
            this.right = right;
        }
        
        public String getData()
        {
            return data;
        }
        
        public BinaryNode getLeft()
        {
            return left;
        }
        
        public BinaryNode getRight()
        {
            return right;
        }
        
        public void setData(String data)
        {
            this.data = data;
        }
        
        public void setLeft(BinaryNode left)
        {
            this.left = left;
        }
        
        public void setRight(BinaryNode right)
        {
            this.right = right;
        }
    }
    
    /**
     *  二叉树的遍历有递归和非递归两种实现方法
     */
    /**
     * 递归 先序遍历
     */
    public void preOrder(BinaryNode node)
    {
        if (node != null)
        {
            System.out.println(node.getData());
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }
    
    /**
     * 递归 中序遍历
     */
    public void inOrder(BinaryNode node)
    {
        if (node != null)
        {
            inOrder(node.getLeft());
            System.out.println(node.getData());
            inOrder(node.getRight());
        }
    }
    
    /**
     * 递归 后序遍历
     */
    public void postOrder(BinaryNode node)
    {
        if (node != null)
        {
            postOrder(node.getLeft());
            postOrder(node.getRight());
            System.out.println(node.getData());
        }
    }
    
    /**
     * 非递归 先序遍历
     */
    public void preOrderNoRecursion()
    {
        // 利用 LinkedList 实现栈
        LinkedList<BinaryNode> stack = new LinkedList<>();
        // 将根节点入栈
        stack.push(getRoot());
        BinaryNode current = null;
        while (!stack.isEmpty())
        {
            // 节点出栈
            current = stack.pop();
            System.out.println(current.getData());
            // 将当前节点的右节点入栈
            if (current.getRight() != null) stack.push(current.getRight());
            // 将当前节点的左节点入栈
            if (current.getLeft() != null) stack.push(current.getLeft());
        }
    }
    
    /**
     * 非递归 中序遍历
     */
    public void inorderNoRecursion()
    {
        LinkedList<BinaryNode> stack = new LinkedList<>();
        BinaryNode current = getRoot();
        while (current != null || !stack.isEmpty())
        {
            while (current != null)
            {
                stack.push(current);
                current = current.getLeft();
            }
            current = stack.pop();
            System.out.println(current.getData());
            current = current.getRight();
        }
    }
    
    /**
     * 非递归 后序遍历
     */
    public void postorderNoRecursion()
    {
        LinkedList<BinaryNode> stack = new LinkedList<>();
        BinaryNode current = getRoot();
        while (current != null || !stack.isEmpty())
        {
            // 将左子结点都入栈
            while (current != null)
            {
                stack.push(current);
                current = current.getLeft();
            }
            // 栈顶结点出栈
            current = stack.pop();
            
        }
    }
    
    
    public static void main(String[] args)
    {
        BinaryNode nodeG = new BinaryNode("G", null, null);
        BinaryNode nodeF = new BinaryNode("F", null, null);
        BinaryNode nodeE = new BinaryNode("E", null, null);
        BinaryNode nodeD = new BinaryNode("D", null, null);
        BinaryNode nodeC = new BinaryNode("C", nodeF, nodeG);
        BinaryNode nodeB = new BinaryNode("B", nodeD, nodeE);
        BinaryNode nodeA = new BinaryNode("A", nodeB, nodeC);
        BinaryTree tree = new BinaryTree(nodeA);
        tree.inorderNoRecursion();
    }
}
