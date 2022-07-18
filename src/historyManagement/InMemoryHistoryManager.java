package historyManagement;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private List<Task> getTask() {
        List<Task> result = new ArrayList<>();
        Node node = head;
        while (node != null) {
            result.add(node.getTask());
            node = node.getNext();
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.getPrev() != null && node.getNext() != null) {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
            if (node.getPrev() == null && node.getNext() != null) {
                head = node.getNext();
                node.getNext().setPrev(null);
            }
            if (node.getPrev() != null && node.getNext() == null) {
                tail = node.getPrev();
                node.getPrev().setNext(null);
            }
            if (node.getPrev() == null && node.getNext() == null) {
                head = node.getNext();
                tail = node.getPrev();
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            final int id = task.getId();
            if (nodeMap.containsKey(id)) {
                removeNode(nodeMap.get(id));
                nodeMap.remove(id);
                linkLast(task);
                nodeMap.put(id, tail);
            } else {
                linkLast(task);
                nodeMap.put(id, tail);
            }
        }
    }

    private void linkLast(Task task) {
        final Node node = new Node(tail, task, null);
        if (head == null) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;
    }

    public static class Node {

        private Node prev;
        private Task task;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "prev=" + prev +
                    ", task=" + task +
                    ", next=" + next +
                    '}';
        }
    }
}
