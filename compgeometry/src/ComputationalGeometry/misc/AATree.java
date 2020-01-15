package ComputationalGeometry.misc;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 2/24/13
 * Time: 3:29 PM
 */
public class AATree {

    static {
        nullNode = new AANode(null);
    }

    public AATree() {
        nullNode.level = 0;
        nullNode.left = nullNode;
        nullNode.right = nullNode;
        lastNode = nullNode;
        deletedNode = nullNode;
        this.root = nullNode;
    }

    public void insert(Comparable element) {
        root = insert(element, root);
    }

    public void remove(Comparable element) {
        deletedNode = nullNode;
        root = remove(element, root);
    }

    public boolean isEmpty() {
        return (root == nullNode);
    }

    public Comparable getMinAndRemove() {
        AANode node = root;

        while (node.left != nullNode) {
            node = node.left;
        }
        Comparable result = node.element;
        remove(node.element);

        return result;
    }

    private static class AANode {
        Comparable element;
        AANode left;
        AANode right;
        int level;

        AANode(Comparable element) {
            this.element = element;
            left = nullNode;
            right = nullNode;
            level = 1;
        }
    }

    private static AANode nullNode;
    private AANode lastNode;
    private AANode deletedNode;
    private AANode root;

    private AANode insert(Comparable p, AANode root) {
        if (root == nullNode) {
            root = new AANode(p);
        } else if (p.compareTo(root.element) > 0) {
            root.right = insert(p, root.right);
        } else if (p.compareTo(root.element) < 0){
            root.left = insert(p, root.left);
        } else {
            return root;
        }
        root = skew(root);
        root = split(root);
        return root;
    }

    private AANode remove(Comparable p, AANode root) {
        if (root != nullNode) {
            lastNode = root;
            if (p.compareTo(root.element) < 0) {
                root.left = remove(p, root.left);
            } else {
                deletedNode = root;
                root.right = remove(p, root.right);
            }

            if (root == lastNode) {
                if (deletedNode == nullNode || p.compareTo(deletedNode.element) != 0) {

                } else {
                    deletedNode.element = root.element;
                    root = root.right;
                }
            } else {
               if (root.left.level < root.level - 1 || root.right.level < root.level - 1) {
                   if (root.right.level > --root.level) {
                       root.right.level = root.level;
                   }
                   root = skew(root);
                   root.right = skew(root.right);
                   root.right.right = skew(root.right.right);
                   root = split(root);
                   root.right = split(root.right);
               }
            }

        }
        return root;
    }

    private static AANode skew(AANode node) {
        if (node.left.level == node.level) {
            node = rotateWithLeftChild(node);
        }
        return node;
    }

    private static AANode split(AANode node) {
        if (node.right.right.level == node.level) {
            node = rotateWithRightChild(node);
        }
        return node;
    }

    private static AANode rotateWithLeftChild(AANode node) {
        AANode left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }

    private static AANode rotateWithRightChild(AANode node) {
        AANode right = node.right;
        node.right = right.left;
        right.left = node;
        return right;
    }

    public void print() {
        toString(root);
    }

    public List<Comparable> getElements() {
        if (root == nullNode) {
            return new ArrayList<Comparable>();
        } else {
            return getElements(root);
        }
    }

    private List<Comparable> getElements(AANode node) {
        List<Comparable> result = new ArrayList<Comparable>();
        if (node.left != nullNode) {
            result.addAll(getElements(node.left));
        }
        result.add(node.element);
        if (node.right != nullNode) {
            result.addAll(getElements(node.right));
        }
        return result;
    }

    private String toString(AANode node) {
        if (node != nullNode) {
            return node.toString();
        } else {
            return "null";
        }
    }
}
