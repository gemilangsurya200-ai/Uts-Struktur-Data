package mystack;

/**
 * Kontrak (abstraksi) untuk semua jenis stack LIFO.
 * StackStatis dan StackDinamis sama-sama mengimplementasikan interface ini
 * sehingga program utama bisa memakai keduanya secara polimorfik.
 *
 * @param <T> tipe elemen yang disimpan
 */
public interface Stack<T> {

    /** Menambah elemen ke puncak stack. */
    void push(T item) throws StackOverflowException;

    /** Mengambil sekaligus menghapus elemen puncak. */
    T pop() throws StackUnderflowException;

    /** Melihat elemen puncak tanpa menghapusnya. */
    T peek() throws StackUnderflowException;

    /** Alias dari peek(). */
    default T top() throws StackUnderflowException {
        return peek();
    }

    boolean isEmpty();

    boolean isFull();

    int size();
}