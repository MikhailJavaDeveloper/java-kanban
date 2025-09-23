package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    @Test
    void shouldSuccessfullySetValuePrevAndNextAndThanGetValuePrevAndNext() {
        Node node = new Node(null, null, null);
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        Node prevNode = new Node(null, null, null);
        Node nextNode = new Node(null, null, null);

        node.setValue(washDishes);
        node.setPrev(prevNode);
        node.setNext(nextNode);
        Task valueResult = node.getValue();
        Node prevResult = node.getPrev();
        Node nextResult = node.getNext();

        assertEquals(washDishes, valueResult, "Неуспешная установка и получение значения.");
        assertEquals(prevNode, prevResult, "Неуспешная установка и получение предыдущего узла.");
        assertEquals(nextNode, nextResult, "Неуспешная установка и получение следующего узла.");
    }
}
