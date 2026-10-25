package mystack;

/**
 * Checked exception: dilempar saat pop() atau peek() dipanggil pada stack kosong.
 */
public class StackUnderflowException extends Exception {
    private static final long serialVersionUID = 1L;

    public StackUnderflowException(String message) {
        super(message);
    }
}