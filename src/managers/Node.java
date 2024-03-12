package managers;

public class Node<T> {
    protected T data;
    protected Node<T> next;
    protected Node<T> prev;

    public Node() {
        next = null;
        prev = null;
        data = null;
    }

    public Node(T d, Node<T> n, Node<T> p) {
        data = d;
        next = n;
        prev = p;
    }

    public void setLinkNext(Node<T> n) {
        next = n;
    }

    public void setLinkPrev(Node<T> p) {
        prev = p;
    }

    public Node<T> getLinkNext() {
        return next;
    }

    public Node<T> getLinkPrev() {
        return prev;
    }

    public void setData(T d) {
        data = d;
    }

    public T getData() {
        return data;
    }
}
