package Tree;

public class BinarySearchTree
{
    // 根节点
    private BSTNode Root;
    
    public BinarySearchTree() {}
    
    public BinarySearchTree(BSTNode root)
    {
        this.Root = root;
    }
    
    public void setRoot(BSTNode root)
    {
        this.Root = root;
    }
    
    public BSTNode getRoot()
    {
        return Root;
    }
    
    /**
     * 二叉查找树查找
     */
    public boolean search(int value)
    {
        return search(Root, value);
    }
    
    private boolean search(BSTNode node, int value)
    {
        if (node == null) return false;
        if (value == node.value) return true;
        else if (value < node.value) return search(node.left, value);
        else return search(node.right, value);
    }
    
    /**
     * 二叉查找树的插入
     * 算法步骤：
     * 1. 若二叉查找树为空，将待插入结点作为根结点插入到空树中；
     * 2. 若二叉查找树非空，将 key 与根结点进行比较：
     * 若 key < node.value, 将 key 插入到左子树；
     * 若 key > node.value, 将 key 插入到右子树；
     * 若 key = node.value, 树中已包含该结点，不需要插入
     */
    public boolean insert(int value)
    {
        // 若二叉查找树为空
        if (Root == null)
        {
            Root = new BSTNode(value);
            return true;
        }
        return insert(Root, value);
    }
    
    private boolean insert(BSTNode node, int value)
    {
        if (value == node.value) return false;
        else if (value < node.value)
        {
            if (node.left == null)
            {
                node.left = new BSTNode(value);
                return true;
            } else
            {
                return insert(node.left, value);
            }
        }
        else
        {
            if (node.right == null)
            {
                node.right = new BSTNode(value);
                return true;
            } else
            {
                return insert(node.right, value);
            }
        }
    }
    
    /**
     * 二叉查找树的删除
     * 算法步骤：
     * 1. 从根结点开始查找待删结点，如果树中不存在此结点，返回false；
     * 2. 如果存在待删结点，需要分为多种情况讨论：
     *      1) 若待删结点为叶子结点，只需修改其父结点指向该结点的指针为 null；
     *      2) 若待删结点只有左子树或者右子树时，
     *          待删结点为其父结点的左孩子，待删结点的左子树后者右子树直接成为其父结点的左子树即可；
     *          待删结点为其父结点的右孩子，待删结点的左子树后者右子树直接成为其父结点的右子树即可；
     *      3) 待删结点既有左子树也有右子树，有两种处理方法：
     *          令待删结点的左子树为其父结点的左子树，待删结点的右子树为二叉树中序遍历序列中待删结点的前驱结点的右子树；
     *          令中序遍历序列中待删结点的前驱结点(后继结点)替换待删结点，然后再从二叉查找树中删去直接前驱(后继结点)；
     */
    
    /**
     * 根据先序遍历序列和中序遍历序列构建二叉树
     */
    public static BSTNode constructBST(int[] preOrder, int[] inOrder)
    {
        return constructBST(preOrder, 0, preOrder.length - 1, inOrder, 0, inOrder.length - 1);
    }
    
    private static BSTNode constructBST(int[] preOrder, int preStart, int preEnd, int[] inOrder, int inStart, int inEnd)
    {
        if (preEnd < preStart) return null;
        
        // 确定根结点：先序遍历序列的第一个结点
        BSTNode root = new BSTNode(preOrder[preStart]);
        // 在中序遍历序列中找到根结点位置，将中序遍历序列分为两个子序列：分别对应根结点的左右子树的中序遍历序列
        for (int i = inStart; i <= inEnd; i++)
        {
            if (root.value == inOrder[i])
            {
                root.left = constructBST(preOrder, preStart + 1, preStart + (i - inStart), inOrder, inStart, i - 1);
                root.right = constructBST(preOrder, preStart + (i - inStart) + 1, preEnd, inOrder, i + 1, inEnd);
            }
        }
        return root;
    }
    
    /**
     * 先序遍历 递归实现
     */
    public void preOrder(BSTNode node)
    {
        if (node != null)
        {
            System.out.print(node.value + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }
    
    /**
     * 中序遍历 递归实现
     */
    public void inOrder(BSTNode node)
    {
        if (node != null)
        {
            inOrder(node.left);
            System.out.print(node.value + " ");
            inOrder(node.right);
        }
    }
    
    /**
     * 后序遍历 递归实现
     */
    public void postOrder(BSTNode node)
    {
        if (node != null)
        {
            postOrder(node.left);
            postOrder(node.right);
            System.out.print(node.value + " ");
        }
    }
    
    
    /**
     * 二叉查找树节点定义
     */
    public static class BSTNode
    {
        private int value;
        private BSTNode left;
        private BSTNode right;
        
        public BSTNode(int value)
        {
            this(value, null, null);
        }
        
        public BSTNode(int value, BSTNode left, BSTNode right)
        {
            this.value = value;
            this.left = left;
            this.right = right;
        }
        
        public int getvalue()
        {
            return value;
        }
        
        public BSTNode getLeft()
        {
            return left;
        }
        
        public BSTNode getRight()
        {
            return right;
        }
        
        public void setvalue(int value)
        {
            this.value = value;
        }
        
        public void setLeft(BSTNode left)
        {
            this.left = left;
        }
        
        public void setRight(BSTNode right)
        {
            this.right = right;
        }
    }
    
    public static void main(String[] args)
    {
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(45);
        tree.insert(24);
        tree.insert(53);
        tree.insert(45);
        tree.insert(12);
        tree.insert(24);
        tree.insert(90);
        tree.preOrder(tree.getRoot());

        
    }
}
