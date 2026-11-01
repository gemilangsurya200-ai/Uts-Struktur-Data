package app;

/**
 * Objek immutable yang menyimpan satu kurung buka beserta posisinya di ekspresi.
 */
public final class Kurung {
    private final char simbol;
    private final int posisi;   // posisi 1-based agar mudah dibaca pengguna

    public Kurung(char simbol, int posisi) {
        this.simbol = simbol;
        this.posisi = posisi;
    }

    public char getSimbol() {
        return simbol;
    }

    public int getPosisi() {
        return posisi;
    }

    @Override
    public String toString() {
        return "'" + simbol + "'@" + posisi;
    }
}