package tasks;

import java.util.Objects;

public class Node {
    Task value;
    Node prev;
    Node next;

    public Node(Task value, Node prev, Node next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public Task getValue() {
        return value;
    }

    public void setValue(Task value) {
        this.value = value;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, prev, next);
    }

    @Override
    public String toString() {
        return "Node{" +
                "value.getId=" + value.getId() +
                ", prev.getValue.getId=" + prev.getValue().getId() +
                ", next.getValue.getId=" + next.getValue().getId() +
                '}';
    }
}
