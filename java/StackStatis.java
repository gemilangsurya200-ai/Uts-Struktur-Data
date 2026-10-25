package mystack;

/**
 * Stack statis: kapasitas tetap, berbasis array yang dialokasikan sekali di awal.
 *
 * @param <T> tipe elemen yang disimpan
 */
public class StackStatis<T> implements Stack<T> {

    private final T[] data;      // array tetap penampung elemen
    private final int kapasitas;
    private int top;             // indeks elemen puncak; -1 berarti kosong

    @SuppressWarnings("unchecked")
    public StackStatis(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas harus > 0");
        }
        this.kapasitas = kapasitas;
        this.data = (T[]) new Object[kapasitas];   // generic array harus lewat cast
        this.top = -1;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public boolean isFull() {
        return top == kapasitas - 1;               // slot terakhir sudah terisi
    }

    @Override
    public int size() {
        return top + 1;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    @Override
    public void push(T item) throws StackOverflowException {
        if (isFull()) {                            // cek penuh sebelum menambah
            throw new StackOverflowException(
                    "Stack Overflow: kapasitas " + kapasitas + " sudah penuh");
        }
        data[++top] = item;                        // naikkan top, lalu simpan
    }

    @Override
    public T pop() throws StackUnderflowException {
        if (isEmpty()) {                           // cek kosong sebelum mengambil
            throw new StackUnderflowException("Stack Underflow: stack kosong");
        }
        T item = data[top];
        data[top--] = null;                        // bersihkan slot agar tidak menahan referensi
        return item;
    }

    @Override
    public T peek() throws StackUnderflowException {
        if (isEmpty()) {
            throw new StackUnderflowException("Stack Underflow: stack kosong");
        }
        return data[top];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StackStatis(kapasitas=" + kapasitas + ", isi=[");
        for (int i = 0; i <= top; i++) {
            sb.append(data[i]);
            if (i < top) {
                sb.append(", ");
            }
        }
        return sb.append("] <- top)").toString();
    }
}