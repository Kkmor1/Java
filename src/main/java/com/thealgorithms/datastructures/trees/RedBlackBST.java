package com.thealgorithms.datastructures.trees;

import java.util.Scanner;

/**
 * @author jack870131
 */
public class RedBlackBST {

    static final int RED = 0;
    static final int BLACK = 1;

    class Node {

        int key = -1;
        int color = BLACK;
        Node left = nil;
        Node right = nil;
        Node p = nil;

        Node(int key) {
            this.key = key;
        }
    }

    final Node nil = new Node(-1);
    Node root = nil;

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

    public void insert(int key) {
        Node node = new Node(key);
        insert(node);
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

    public boolean search(int key) {
        Node n = new Node(key);
        return findNode(n, root) != null;
    }

    public boolean delete(int key) {
        Node n = new Node(key);
        return delete(n);
    }

    public Integer getMinimum() {
        if (root == nil) {
            return null;
        }
        return treeMinimum(root).key;
    }

    public Integer getMaximum() {
        if (root == nil) {
            return null;
        }
        return treeMaximum(root).key;
    }

    public Integer getSuccessor(int key) {
        Node n = findNode(new Node(key), root);
        if (n == null) {
            return null;
        }
        Node succ = treeSuccessor(n);
        return succ != nil ? succ.key : null;
    }

    public Integer getPredecessor(int key) {
        Node n = findNode(new Node(key), root);
        if (n == null) {
            return null;
        }
        Node pred = treePredecessor(n);
        return pred != nil ? pred.key : null;
    }

    Node treeMaximum(Node subTreeRoot) {
        while (subTreeRoot.right != nil) {
            subTreeRoot = subTreeRoot.right;
        }
        return subTreeRoot;
    }

    Node treeSuccessor(Node x) {
        if (x.right != nil) {
            return treeMinimum(x.right);
        }
        Node y = x.p;
        while (y != nil && x == y.right) {
            x = y;
            y = y.p;
        }
        return y;
    }

    Node treePredecessor(Node x) {
        if (x.left != nil) {
            return treeMaximum(x.left);
        }
        Node y = x.p;
        while (y != nil && x == y.left) {
            x = y;
            y = y.p;
        }
        return y;
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
        z = result; // USE THE FOUND NODE
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
