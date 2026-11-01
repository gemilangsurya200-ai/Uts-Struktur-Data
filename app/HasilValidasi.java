package app;

/**
 * Hasil validasi: status valid/tidak beserta pesan penjelasannya.
 */
public final class HasilValidasi {
    private final boolean valid;
    private final String pesan;

    private HasilValidasi(boolean valid, String pesan) {
        this.valid = valid;
        this.pesan = pesan;
    }

    public static HasilValidasi sukses() {
        return new HasilValidasi(true, "Valid, semua kurung seimbang");
    }

    public static HasilValidasi gagal(String pesan) {
        return new HasilValidasi(false, pesan);
    }

    public boolean isValid() {
        return valid;
    }

    public String getPesan() {
        return pesan;
    }
}