package app;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

import mystack.Stack;
import mystack.StackDinamis;
import mystack.StackOverflowException;
import mystack.StackStatis;
import mystack.StackUnderflowException;

/**
 * Studi kasus: validasi kurung ekspresi matematika (Modus A: library buatan sendiri).
 * Alur LIFO: kurung buka -> push, kurung tutup -> pop lalu cocokkan, akhir -> stack harus kosong.
 */
public class Main {

    /** Mengembalikan pasangan kurung buka dari sebuah kurung tutup, atau 0 jika bukan kurung tutup. */
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

    /** Validasi memakai stack apa pun yang mengimplementasikan interface Stack (polimorfisme). */
    static HasilValidasi validasiKurung(String ekspresi, Stack<Kurung> stack) {
        for (int i = 0; i < ekspresi.length(); i++) {
            char ch = ekspresi.charAt(i);
            int posisi = i + 1;

            if (isKurungBuka(ch)) {
                try {
                    stack.push(new Kurung(ch, posisi));              // simpan kurung buka
                } catch (StackOverflowException e) {                 // stack statis penuh
                    return HasilValidasi.gagal(e.getMessage() + " (di posisi " + posisi + ")");
                }
            } else if (pasanganBuka(ch) != 0) {                      // ch adalah kurung tutup
                try {
                    Kurung buka = stack.pop();                       // ambil kurung buka terakhir (LIFO)
                    if (buka.getSimbol() != pasanganBuka(ch)) {      // jenis kurung tidak cocok
                        return HasilValidasi.gagal("Tidak cocok: '" + buka.getSimbol()
                                + "' (posisi " + buka.getPosisi() + ") ditutup '" + ch
                                + "' (posisi " + posisi + ")");
                    }
                } catch (StackUnderflowException e) {                // tutup tanpa pasangan
                    return HasilValidasi.gagal("Kurung tutup '" + ch + "' di posisi "
                            + posisi + " tidak punya pasangan");
                }
            }
        }

        if (!stack.isEmpty()) {                                      // ada kurung buka tersisa
            try {
                Kurung sisa = stack.peek();
                return HasilValidasi.gagal("Kurung buka '" + sisa.getSimbol()
                        + "' di posisi " + sisa.getPosisi() + " belum ditutup");
            } catch (StackUnderflowException e) {
                throw new IllegalStateException("Kondisi mustahil: stack tidak kosong tapi peek gagal", e);
            }
        }
        return HasilValidasi.sukses();
    }

    private static void ujiOperasiDasar() {
        System.out.println("=== UJI OPERASI DASAR StackStatis(kapasitas=3) ===");
        StackStatis<Integer> s = new StackStatis<>(3);
        try {
            s.push(10);
            s.push(20);
            s.push(30);
            System.out.println(s + " | size: " + s.size() + " | isFull: " + s.isFull());
            s.push(40);                                              // memicu Overflow
        } catch (StackOverflowException e) {
            System.out.println("Tangkap error  : " + e.getMessage());
        }

        try {
            System.out.println("peek (top)     : " + s.peek());
            System.out.println("pop            : " + s.pop() + " " + s.pop() + " " + s.pop());
            System.out.println("isEmpty        : " + s.isEmpty());
            s.pop();                                                 // memicu Underflow
        } catch (StackUnderflowException e) {
            System.out.println("Tangkap error  : " + e.getMessage());
        }
        System.out.println();
    }

    private static void jalankanPengujian() {
        String[] kasusUji = {
            "(a + b) * c",
            "{[(1 + 2) * 3] - 4} / 5",
            "((a + b)",
            "a + b)",
            "(a + b]",
            "((((((1))))))"        // kedalaman 6: overflow pada stack statis kapasitas 4
        };

        String[] namaStack = {"StackStatis (kapasitas 4)", "StackDinamis (tanpa batas)"};
        // Supplier = pabrik stack baru agar tiap kasus uji mulai dari kondisi kosong
        List<Supplier<Stack<Kurung>>> pabrik = Arrays.asList(
            () -> new StackStatis<Kurung>(4),
            () -> new StackDinamis<Kurung>()
        );

        for (int j = 0; j < namaStack.length; j++) {
            System.out.println("=== VALIDASI KURUNG - " + namaStack[j] + " ===");
            for (String ekspresi : kasusUji) {
                HasilValidasi hasil = validasiKurung(ekspresi, pabrik.get(j).get());
                System.out.printf("[%s] %-26s -> %s%n",
                        hasil.isValid() ? "VALID  " : "INVALID", ekspresi, hasil.getPesan());
            }
            System.out.println();
        }
    }

    private static void modeInteraktif() {
        System.out.println("=== MODE INTERAKTIF (ketik 'q' untuk keluar) ===");
        try (Scanner in = new Scanner(System.in)) {                  // try-with-resources menutup Scanner
            while (true) {
                System.out.print("Masukkan ekspresi: ");
                if (!in.hasNextLine()) {                             // input habis (mis. EOF)
                    break;
                }
                String ekspresi = in.nextLine();
                if (ekspresi.trim().equalsIgnoreCase("q")) {
                    break;
                }
                HasilValidasi hasil = validasiKurung(ekspresi, new StackDinamis<Kurung>());
                System.out.println((hasil.isValid() ? "VALID  : " : "INVALID: ") + hasil.getPesan());
            }
        }
    }

    public static void main(String[] args) {
        ujiOperasiDasar();
        jalankanPengujian();
        modeInteraktif();
    }
}