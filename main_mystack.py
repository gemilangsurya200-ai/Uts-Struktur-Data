"""
main_mystack.py
Studi kasus : Validasi kurung pada ekspresi matematika (Modus A: library buatan sendiri).
Jalankan    : python main_mystack.py   (pastikan mystack.py satu folder)
"""

from mystack import (
    StackStatis,
    StackDinamis,
    StackOverflowError,
    StackUnderflowError,
)

# Kurung tutup -> pasangan kurung buka yang benar
PASANGAN = {")": "(", "]": "[", "}": "{"}
KURUNG_BUKA = set(PASANGAN.values())


def validasi_kurung(ekspresi, stack):
    """
    Mengembalikan (valid: bool, pesan: str).
    Alur LIFO:
      - kurung buka  -> push (beserta posisinya)
      - kurung tutup -> pop, lalu cocokkan dengan pasangannya
      - akhir ekspresi, stack harus kosong
    """
    for posisi, ch in enumerate(ekspresi, start=1):
        if ch in KURUNG_BUKA:
            try:
                stack.push((ch, posisi))                     # simpan kurung buka
            except StackOverflowError as e:                  # stack statis penuh
                return False, f"{e} (di posisi {posisi})"
        elif ch in PASANGAN:
            try:
                buka, pos_buka = stack.pop()                 # ambil kurung buka terakhir
            except StackUnderflowError:                      # tutup tanpa pasangan
                return False, f"Kurung tutup '{ch}' di posisi {posisi} tidak punya pasangan"
            if buka != PASANGAN[ch]:                         # jenis kurung tidak cocok
                return False, (
                    f"Tidak cocok: '{buka}' (posisi {pos_buka}) ditutup '{ch}' (posisi {posisi})"
                )

    if not stack.is_empty():                                 # sisa kurung buka
        buka, pos_buka = stack.peek()
        return False, f"Kurung buka '{buka}' di posisi {pos_buka} belum ditutup"
    return True, "Valid, semua kurung seimbang"


def uji_operasi_dasar():
    """Demo singkat push/pop/peek + Overflow & Underflow pada StackStatis."""
    print("=== UJI OPERASI DASAR StackStatis(kapasitas=3) ===")
    s = StackStatis(3)
    for x in (10, 20, 30):
        s.push(x)
    print(s, "| size:", s.size(), "| is_full:", s.is_full())
    try:
        s.push(40)                                           # memicu Overflow
    except StackOverflowError as e:
        print("Tangkap error  :", e)
    print("peek (top)     :", s.peek())
    print("pop            :", s.pop(), s.pop(), s.pop())
    print("is_empty       :", s.is_empty())
    try:
        s.pop()                                              # memicu Underflow
    except StackUnderflowError as e:
        print("Tangkap error  :", e)
    print()


def jalankan_pengujian():
    kasus_uji = [
        "(a + b) * c",
        "{[(1 + 2) * 3] - 4} / 5",
        "((a + b)",
        "a + b)",
        "(a + b]",
        "((((((1))))))",       # kedalaman 6: overflow di stack statis (kapasitas 4)
    ]
    # (nama, pabrik pembuat stack baru agar tiap kasus mulai dari kosong)
    jenis_stack = [
        ("StackStatis (kapasitas 4)", lambda: StackStatis(4)),
        ("StackDinamis (tanpa batas)", lambda: StackDinamis()),
    ]

    for nama, buat in jenis_stack:
        print(f"=== VALIDASI KURUNG - {nama} ===")
        for ekspresi in kasus_uji:
            valid, pesan = validasi_kurung(ekspresi, buat())
            status = "VALID  " if valid else "INVALID"
            print(f"[{status}] {ekspresi:<26} -> {pesan}")
        print()


def mode_interaktif():
    print("=== MODE INTERAKTIF (ketik 'q' untuk keluar) ===")
    while True:
        ekspresi = input("Masukkan ekspresi: ")
        if ekspresi.strip().lower() == "q":
            break
        valid, pesan = validasi_kurung(ekspresi, StackDinamis())
        print(("VALID  : " if valid else "INVALID: ") + pesan)


if __name__ == "__main__":
    uji_operasi_dasar()
    jalankan_pengujian()
    mode_interaktif()