package mystack;

/**
 * Stack dinamis: berbasis linked list (node), tanpa batas kapasitas kaku.
 *
 * @param <T> tipe elemen yang disimpan
 */
public class StackDinamis<T> implements Stack<T> {

    /** Satu simpul linked list (nested class, hanya dipakai internal). */
    private static class Node<E> {
        private final E data;
        private final Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<T> head;    // head = elemen puncak stack
    private int jumlah;

    public StackDinamis() {
        this.head = null;
        this.jumlah = 0;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public boolean isFull() {
        return false;        // tidak pernah penuh (selama memori JVM tersedia)
    }

    @Override
    public int size() {
        return jumlah;
    }

    @Override
    public void push(T item) {
        head = new Node<>(item, head);   // node baru menunjuk head lama, jadi head baru (O(1))
        jumlah++;
    }

    @Override
    public T pop() throws StackUnderflowException {
        if (isEmpty()) {
            throw new StackUnderflowException("Stack Underflow: stack kosong");
        }
        T item = head.data;
        head = head.next;                // geser head ke node di bawahnya
        jumlah--;
        return item;
    }

    @Override
    public T peek() throws StackUnderflowException {
        if (isEmpty()) {
            throw new StackUnderflowException("Stack Underflow: stack kosong");
        }
        return head.data;
    }

    @Override
    public String toString() {
        // telusuri dari puncak ke dasar, lalu tampilkan terbalik (dasar -> puncak)
        StringBuilder sb = new StringBuilder();
        for (Node<T> n = head; n != null; n = n.next) {
            sb.insert(0, n.data + (n == head ? "" : ", "));
        }
        return "StackDinamis(isi=[" + sb + "] <- top)";
    }
}