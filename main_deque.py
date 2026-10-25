"""
main_deque.py
Studi kasus : Validasi kurung pada ekspresi matematika (Modus B: collections.deque).
Jalankan    : python main_deque.py
Catatan     : deque(maxlen=n) TIDAK melempar overflow, melainkan membuang elemen
              di ujung lain secara diam-diam. Karena itu, batas kapasitas untuk
              stack statis dicek manual agar perilakunya sama dengan mystack.py.
"""

from collections import deque

PASANGAN = {")": "(", "]": "[", "}": "{"}
KURUNG_BUKA = set(PASANGAN.values())


def validasi_kurung(ekspresi, kapasitas=None):
    """
    kapasitas=None -> stack dinamis (tanpa batas)
    kapasitas=int  -> stack statis  (batas dicek manual, overflow dilaporkan)
    Mengembalikan (valid: bool, pesan: str).
    """
    stack = deque()                                          # deque sebagai stack

    for posisi, ch in enumerate(ekspresi, start=1):
        if ch in KURUNG_BUKA:
            if kapasitas is not None and len(stack) >= kapasitas:   # cek overflow manual
                return False, f"Stack Overflow: kapasitas {kapasitas} sudah penuh (di posisi {posisi})"
            stack.append((ch, posisi))                       # push = append (ujung kanan)
        elif ch in PASANGAN:
            if not stack:                                    # underflow: stack kosong
                return False, f"Kurung tutup '{ch}' di posisi {posisi} tidak punya pasangan"
            buka, pos_buka = stack.pop()                     # pop = pop (ujung kanan, LIFO)
            if buka != PASANGAN[ch]:
                return False, (
                    f"Tidak cocok: '{buka}' (posisi {pos_buka}) ditutup '{ch}' (posisi {posisi})"
                )

    if stack:                                                # peek = stack[-1]
        buka, pos_buka = stack[-1]
        return False, f"Kurung buka '{buka}' di posisi {pos_buka} belum ditutup"
    return True, "Valid, semua kurung seimbang"


def jalankan_pengujian():
    kasus_uji = [
        "(a + b) * c",
        "{[(1 + 2) * 3] - 4} / 5",
        "((a + b)",
        "a + b)",
        "(a + b]",
        "((((((1))))))",       # kedalaman 6: overflow pada kapasitas 4
    ]
    jenis_stack = [
        ("deque - Statis (kapasitas 4)", 4),
        ("deque - Dinamis (tanpa batas)", None),
    ]

    for nama, kapasitas in jenis_stack:
        print(f"=== VALIDASI KURUNG - {nama} ===")
        for ekspresi in kasus_uji:
            valid, pesan = validasi_kurung(ekspresi, kapasitas)
            status = "VALID  " if valid else "INVALID"
            print(f"[{status}] {ekspresi:<26} -> {pesan}")
        print()


def mode_interaktif():
    print("=== MODE INTERAKTIF (ketik 'q' untuk keluar) ===")
    while True:
        ekspresi = input("Masukkan ekspresi: ")
        if ekspresi.strip().lower() == "q":
            break
        valid, pesan = validasi_kurung(ekspresi)
        print(("VALID  : " if valid else "INVALID: ") + pesan)


if __name__ == "__main__":
    jalankan_pengujian()
    mode_interaktif()