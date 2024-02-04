package Managers;

public class Node <T>{
    protected T data;
    protected Node next;
    protected Node prev;

    public Node() {
        next = null;
        prev = null;
        data = null;
    }

    public Node(T d, Node n, Node p) {
        data = d;
        next = n;
        prev = p;
    }

    public void setLinkNext(Node n) {
        next = n;
    }

    public void setLinkPrev(Node p) {
        prev = p;
    }

    public Node getLinkNext() {
        return next;
    }

    public Node getLinkPrev() {
        return prev;
    }

    public void setData(T d) {
        data = d;
    }

    public T getData() {
        return data;
    }
}
