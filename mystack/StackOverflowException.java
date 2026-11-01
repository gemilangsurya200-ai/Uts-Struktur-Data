package mystack;

/**
 * Checked exception: dilempar saat push() dipanggil pada stack yang sudah penuh.
 * Dinamai "Exception" agar tidak bentrok dengan java.lang.StackOverflowError.
 */
public class StackOverflowException extends Exception {
    private static final long serialVersionUID = 1L;

    public StackOverflowException(String message) {
        super(message);
    }
}