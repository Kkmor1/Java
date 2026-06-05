package com.thealgorithms.datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author jack870131
 */
public class RedBlackBST {

    private static final int RED = 0;
    private static final int BLACK = 1;

    private class Node {

        int key = -1;
        int color = BLACK;
        Node left = nil;
        Node right = nil;
        Node p = nil;

        Node(int key) {
            this.key = key;
        }
    }

    private final Node nil = new Node(-1);
    private Node root = nil;

    /**
     * Inserts a key into the red-black tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        Node node = new Node(key);
        insert(node);
    }

    /**
     * Deletes a key from the red-black tree.
     *
     * @param key the key to delete
     * @return true if the key was found and deleted, false otherwise
     */
    public boolean delete(int key) {
        Node node = new Node(key);
        return delete(node);
    }

    /**
     * Searches for a key in the red-black tree.
     *
     * @param key the key to search for
     * @return true if the key is found, false otherwise
     */
    public boolean search(int key) {
        Node node = new Node(key);
        return findNode(node, root) != null;
    }

    /**
     * Returns the minimum key in the tree.
     *
     * @return the minimum key, or null if the tree is empty
     */
    public Integer treeMinimum() {
        if (root == nil) {
            return null;
        }
        return treeMinimum(root).key;
    }

    /**
     * Returns the maximum key in the tree.
     *
     * @return the maximum key, or null if the tree is empty
     */
    public Integer treeMaximum() {
        if (root == nil) {
            return null;
        }
        Node node = root;
        while (node.right != nil) {
            node = node.right;
        }
        return node.key;
    }

    /**
     * Finds the successor of a given key.
     *
     * @param key the key whose successor to find
     * @return the successor key, or null if no successor exists
     */
    public Integer findSuccessor(int key) {
        Node node = new Node(key);
        Node foundNode = findNode(node, root);
        if (foundNode == null) {
            return null;
        }
        if (foundNode.right != nil) {
            return treeMinimum(foundNode.right).key;
        }
        Node p = foundNode.p;
        while (p != nil && foundNode == p.right) {
            foundNode = p;
            p = p.p;
        }
        return p == nil ? null : p.key;
    }

    /**
     * Finds the predecessor of a given key.
     *
     * @param key the key whose predecessor to find
     * @return the predecessor key, or null if no predecessor exists
     */
    public Integer findPredecessor(int key) {
        Node node = new Node(key);
        Node foundNode = findNode(node, root);
        if (foundNode == null) {
            return null;
        }
        if (foundNode.left != nil) {
            Node temp = foundNode.left;
            while (temp.right != nil) {
                temp = temp.right;
            }
            return temp.key;
        }
        Node p = foundNode.p;
        while (p != nil && foundNode == p.left) {
            foundNode = p;
            p = p.p;
        }
        return p == nil ? null : p.key;
    }

    /**
     * Checks if the red-black tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == nil;
    }

    /**
     * Validates all red-black tree invariants.
     *
     * @return true if all invariants hold, false otherwise
     */
    public boolean validateRedBlackInvariants() {
        if (root == nil) {
            return true;
        }
        
        if (root.color != BLACK) {
            return false;
        }

        return validateRedNodeChildren(root) && validateBlackHeight(root) != -1;
    }

    private boolean validateRedNodeChildren(Node node) {
        if (node == nil) {
            return true;
        }
        if (node.color == RED) {
            if (node.left.color != BLACK || node.right.color != BLACK) {
                return false;
            }
        }
        return validateRedNodeChildren(node.left) && validateRedNodeChildren(node.right);
    }

    private int validateBlackHeight(Node node) {
        if (node == nil) {
            return 1;
        }
        int leftBlackHeight = validateBlackHeight(node.left);
        int rightBlackHeight = validateBlackHeight(node.right);
        
        if (leftBlackHeight == -1 || rightBlackHeight == -1 || leftBlackHeight != rightBlackHeight) {
            return -1;
        }
        
        return leftBlackHeight + (node.color == BLACK ? 1 : 0);
    }

    /**
     * Returns an in-order traversal of the tree keys.
     *
     * @return a list of keys in in-order traversal
     */
    public List<Integer> inOrderTraversal() {
        List<Integer> result = new ArrayList<>();
        inOrderTraversal(root, result);
        return result;
    }

    private void inOrderTraversal(Node node, List<Integer> result) {
        if (node == nil) {
            return;
        }
        inOrderTraversal(node.left, result);
        result.add(node.key);
        inOrderTraversal(node.right, result);
    }

    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color == RED) ? " R " : " B ") + "Key: " + node.key + " Parent: " + node.p.key + "\n");
        printTree(node.right);
    }

    public void printTreepre(Node node) {
        if (node == nil) {
            return;
        }
        System.out.print(((node.color == RED) ? " R " : " B ") + "Key: " + node.key + " Parent: " + node.p.key + "\n");
        printTreepre(node.left);
        printTreepre(node.right);
    }

    private Node findNode(Node findNode, Node node) {
        if (root == nil) {
            return null;
        }
        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    private void insert(Node node) {
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.p = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.p = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.p = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }

    private void fixTree(Node node) {
        while (node.p.color == RED) {
            Node y = nil;
            if (node.p == node.p.p.left) {
                y = node.p.p.right;

                if (y != nil && y.color == RED) {
                    node.p.color = BLACK;
                    y.color = BLACK;
                    node.p.p.color = RED;
                    node = node.p.p;
                    continue;
                }
                if (node == node.p.right) {
                    node = node.p;
                    rotateLeft(node);
                }
                node.p.color = BLACK;
                node.p.p.color = RED;
                rotateRight(node.p.p);
            } else {
                y = node.p.p.left;
                if (y != nil && y.color == RED) {
                    node.p.color = BLACK;
                    y.color = BLACK;
                    node.p.p.color = RED;
                    node = node.p.p;
                    continue;
                }
                if (node == node.p.left) {
                    node = node.p;
                    rotateRight(node);
                }
                node.p.color = BLACK;
                node.p.p.color = RED;
                rotateLeft(node.p.p);
            }
        }
        root.color = BLACK;
    }

    void rotateLeft(Node node) {
        if (node.p != nil) {
            if (node == node.p.left) {
                node.p.left = node.right;
            } else {
                node.p.right = node.right;
            }
            node.right.p = node.p;
            node.p = node.right;
            if (node.right.left != nil) {
                node.right.left.p = node;
            }
            node.right = node.right.left;
            node.p.left = node;
        } else {
            Node right = root.right;
            root.right = right.left;
            right.left.p = root;
            root.p = right;
            right.left = root;
            right.p = nil;
            root = right;
        }
    }

    void rotateRight(Node node) {
        if (node.p != nil) {
            if (node == node.p.left) {
                node.p.left = node.left;
            } else {
                node.p.right = node.left;
            }

            node.left.p = node.p;
            node.p = node.left;
            if (node.left.right != nil) {
                node.left.right.p = node;
            }
            node.left = node.left.right;
            node.p.right = node;
        } else {
            Node left = root.left;
            root.left = root.left.right;
            left.right.p = root;
            root.p = left;
            left.right = root;
            left.p = nil;
            root = left;
        }
    }

    void transplant(Node target, Node with) {
        if (target.p == nil) {
            root = with;
        } else if (target == target.p.left) {
            target.p.left = with;
        } else {
            target.p.right = with;
        }
        with.p = target.p;
    }

    Node treeMinimum(Node subTreeRoot) {
        while (subTreeRoot.left != nil) {
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    boolean delete(Node z) {
        Node result = findNode(z, root);
        if (result == null) {
            return false;
        }
        Node x;
        Node y = z;
        int yorigcolor = y.color;

        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = treeMinimum(z.right);
            yorigcolor = y.color;
            x = y.right;
            if (y.p == z) {
                x.p = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.p = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.p = y;
            y.color = z.color;
        }
        if (yorigcolor == BLACK) {
            deleteFixup(x);
        }
        return true;
    }

    void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.p.left) {
                Node w = x.p.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.p.color = RED;
                    rotateLeft(x.p);
                    w = x.p.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.p;
                    continue;
                } else if (w.right.color == BLACK) {
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.p.right;
                }
                if (w.right.color == RED) {
                    w.color = x.p.color;
                    x.p.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.p);
                    x = root;
                }
            } else {
                Node w = x.p.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.p.color = RED;
                    rotateRight(x.p);
                    w = x.p.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.p;
                    continue;
                } else if (w.left.color == BLACK) {
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.p.left;
                }
                if (w.left.color == RED) {
                    w.color = x.p.color;
                    x.p.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.p);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    public void insertDemo() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Add items");

        int item;
        Node node;

        item = scan.nextInt();
        while (item != -999) {
            node = new Node(item);
            insert(node);
            item = scan.nextInt();
        }
        printTree(root);
        System.out.println("Pre order");
        printTreepre(root);
        scan.close();
    }

    public void deleteDemo() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Delete items");
        int item;
        Node node;
        item = scan.nextInt();
        node = new Node(item);
        System.out.print("Deleting item " + item);
        if (delete(node)) {
            System.out.print(": deleted!");
        } else {
            System.out.print(": does not exist!");
        }

        System.out.println();
        printTree(root);
        System.out.println("Pre order");
        printTreepre(root);
        scan.close();
    }
}
