package app;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * Studi kasus yang sama dengan Main, tetapi memakai java.util.ArrayDeque (Modus B).
 *
 * Catatan:
 *  - ArrayDeque tidak punya batas kapasitas (angka di konstruktor hanya ukuran awal),
 *    sehingga batas "stack statis" dicek manual agar perilakunya setara dengan StackStatis.
 *  - ArrayDeque.pop() melempar NoSuchElementException jika kosong, sedangkan peek() mengembalikan
 *    null; karena itu kita cek isEmpty() terlebih dahulu.
 *  - Java menyarankan Deque (bukan kelas legacy java.util.Stack) untuk implementasi stack.
 */
public class MainDeque {

    private static char pasanganBuka(char tutup) {
        switch (tutup) {
            case ')': return '(';
            case ']': return '[';
            case '}': return '{';
            default:  return 0;
        }
    }

    private static boolean isKurungBuka(char c) {
        return c == '(' || c == '[' || c == '{';
    }

    /**
     * @param kapasitas batas maksimum untuk mode statis; null berarti dinamis (tanpa batas)
     */
    static HasilValidasi validasiKurung(String ekspresi, Integer kapasitas) {
        Deque<Kurung> stack = new ArrayDeque<>();                    // ArrayDeque sebagai stack

        for (int i = 0; i < ekspresi.length(); i++) {
            char ch = ekspresi.charAt(i);
            int posisi = i + 1;

            if (isKurungBuka(ch)) {
                if (kapasitas != null && stack.size() >= kapasitas) {        // cek overflow manual
                    return HasilValidasi.gagal("Stack Overflow: kapasitas " + kapasitas
                            + " sudah penuh (di posisi " + posisi + ")");
                }
                stack.push(new Kurung(ch, posisi));                  // push = addFirst
            } else if (pasanganBuka(ch) != 0) {
                if (stack.isEmpty()) {                               // underflow: cek manual
                    return HasilValidasi.gagal("Kurung tutup '" + ch + "' di posisi "
                            + posisi + " tidak punya pasangan");
                }
                Kurung buka = stack.pop();                           // pop = removeFirst (LIFO)
                if (buka.getSimbol() != pasanganBuka(ch)) {
                    return HasilValidasi.gagal("Tidak cocok: '" + buka.getSimbol()
                            + "' (posisi " + buka.getPosisi() + ") ditutup '" + ch
                            + "' (posisi " + posisi + ")");
                }
            }
        }

        if (!stack.isEmpty()) {
            Kurung sisa = stack.peek();                              // peek = elemen puncak
            return HasilValidasi.gagal("Kurung buka '" + sisa.getSimbol()
                    + "' di posisi " + sisa.getPosisi() + " belum ditutup");
        }
        return HasilValidasi.sukses();
    }

    private static void jalankanPengujian() {
        String[] kasusUji = {
            "(a + b) * c",
            "{[(1 + 2) * 3] - 4} / 5",
            "((a + b)",
            "a + b)",
            "(a + b]",
            "((((((1))))))"        // kedalaman 6: overflow pada kapasitas 4
        };
        String[] namaStack = {"ArrayDeque - Statis (kapasitas 4)", "ArrayDeque - Dinamis (tanpa batas)"};
        Integer[] kapasitas = {4, null};

        for (int j = 0; j < namaStack.length; j++) {
            System.out.println("=== VALIDASI KURUNG - " + namaStack[j] + " ===");
            for (String ekspresi : kasusUji) {
                HasilValidasi hasil = validasiKurung(ekspresi, kapasitas[j]);
                System.out.printf("[%s] %-26s -> %s%n",
                        hasil.isValid() ? "VALID  " : "INVALID", ekspresi, hasil.getPesan());
            }
            System.out.println();
        }
    }

    private static void modeInteraktif() {
        System.out.println("=== MODE INTERAKTIF (ketik 'q' untuk keluar) ===");
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("Masukkan ekspresi: ");
                if (!in.hasNextLine()) {
                    break;
                }
                String ekspresi = in.nextLine();
                if (ekspresi.trim().equalsIgnoreCase("q")) {
                    break;
                }
                HasilValidasi hasil = validasiKurung(ekspresi, null);
                System.out.println((hasil.isValid() ? "VALID  : " : "INVALID: ") + hasil.getPesan());
            }
        }
    }

    public static void main(String[] args) {
        jalankanPengujian();
        modeInteraktif();
    }
}