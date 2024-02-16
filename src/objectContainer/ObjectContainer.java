package objectContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer <T> implements Serializable{
    private Node<T> head;
    private SerializablePredicate<T> predicate;

    public ObjectContainer(SerializablePredicate<T> predicate) {
        this.predicate = predicate;
    }

    private static class Node <R> implements Serializable{

        private Node<R> next;

        private R data;
        public Node() {
        }

        public Node<R> getNext() {
            return next;
        }

        public R getData() {
            return data;
        }

        public void setNext(Node<R> next) {
            this.next = next;
        }

        public void setData(R data) {
            this.data = data;
        }

    }
    public void add(T data) {

        if(!predicate.test(data)){
            throw new IllegalArgumentException();
        }

        Node<T> node = new Node<>();
        node.setData(data);
        node.setNext(null);
        if (head == null) {
            head = node;
        } else {
            Node<T> n = head;
            while (n.getNext() != null) {
                n = n.getNext();
            }
            n.setNext(node);
        }
    }
    public List<T> getWithFilter(Predicate<T> predicate){
        List<T> tList = new ArrayList<>();

        Node<T> n = head;
        while (n != null) {
            if (predicate.test(n.getData())) {
                tList.add(n.getData());
            }
            n = n.getNext();
        }

        return tList;
    }

    public void removeIf(Predicate<T> predicate) {
        while (head != null && predicate.test(head.getData())) {
            head = head.getNext();
        }

        Node<T> n = head;
        while (n.getNext() != null) {
            if (predicate.test(n.getNext().getData())) {
                n.setNext(n.getNext().getNext());
            } else {
                n = n.getNext();
            }
        }

    }

    public <R> void storeToFile(String file, Predicate<T> predicate, Function<T, R> function) {

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            getWithFilter(predicate).stream()
                    .map(function)
                    .forEach(e -> {
                        try {
                            bf.write(e.toString());
                            bf.newLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeToFile(String file) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ObjectContainer<T> fromFile(String file) {

        ObjectContainer<T> objectContainer = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            objectContainer = (ObjectContainer<T>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return objectContainer;
    }
}
