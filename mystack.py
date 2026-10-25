"""
mystack.py
Library Stack buatan sendiri (Modus A).
Berisi:
  - StackOverflowError / StackUnderflowError : exception khusus
  - StackStatis   : kapasitas tetap (berbasis array/list ukuran tetap)
  - StackDinamis  : kapasitas fleksibel (berbasis linked list / node)
Prinsip: LIFO (Last In, First Out).
"""


class StackOverflowError(Exception):
    """Dilempar saat push ke stack yang sudah penuh."""


class StackUnderflowError(Exception):
    """Dilempar saat pop/peek pada stack yang kosong."""


# =====================================================================
# STACK STATIS: kapasitas tetap, ditentukan saat objek dibuat
# =====================================================================
class StackStatis:
    def __init__(self, kapasitas):
        if not isinstance(kapasitas, int) or kapasitas <= 0:
            raise ValueError("Kapasitas harus berupa bilangan bulat > 0")
        self.kapasitas = kapasitas
        self._data = [None] * kapasitas   # array tetap, dialokasikan di awal
        self._top = -1                    # -1 artinya stack kosong

    def is_empty(self):
        return self._top == -1

    def is_full(self):
        return self._top == self.kapasitas - 1   # indeks terakhir terisi

    def size(self):
        return self._top + 1

    def push(self, item):
        if self.is_full():                        # cek penuh sebelum menambah
            raise StackOverflowError(
                f"Stack Overflow: kapasitas {self.kapasitas} sudah penuh"
            )
        self._top += 1                            # naikkan pointer top
        self._data[self._top] = item

    def pop(self):
        if self.is_empty():                       # cek kosong sebelum mengambil
            raise StackUnderflowError("Stack Underflow: stack kosong")
        item = self._data[self._top]
        self._data[self._top] = None              # kosongkan slot (bersih)
        self._top -= 1                            # turunkan pointer top
        return item

    def peek(self):
        if self.is_empty():
            raise StackUnderflowError("Stack Underflow: stack kosong")
        return self._data[self._top]

    top = peek  # alias: top() sama dengan peek()

    def __len__(self):
        return self.size()

    def __str__(self):
        isi = self._data[: self._top + 1]
        return f"StackStatis(kapasitas={self.kapasitas}, isi={isi} <- top)"


# =====================================================================
# STACK DINAMIS: berbasis linked list, tanpa batas kapasitas kaku
# =====================================================================
class _Node:
    """Satu simpul linked list."""
    __slots__ = ("data", "next")

    def __init__(self, data, next_node=None):
        self.data = data
        self.next = next_node


class StackDinamis:
    def __init__(self):
        self._head = None   # head = elemen paling atas (top)
        self._size = 0

    def is_empty(self):
        return self._head is None

    def is_full(self):
        return False        # stack dinamis tidak pernah penuh (selama memori ada)

    def size(self):
        return self._size

    def push(self, item):
        # node baru menunjuk ke head lama, lalu menjadi head baru (O(1))
        self._head = _Node(item, self._head)
        self._size += 1

    def pop(self):
        if self.is_empty():
            raise StackUnderflowError("Stack Underflow: stack kosong")
        item = self._head.data
        self._head = self._head.next              # geser head ke node di bawahnya
        self._size -= 1
        return item

    def peek(self):
        if self.is_empty():
            raise StackUnderflowError("Stack Underflow: stack kosong")
        return self._head.data

    top = peek  # alias: top() sama dengan peek()

    def __len__(self):
        return self.size()

    def __str__(self):
        isi, node = [], self._head
        while node:                               # telusuri dari top ke bawah
            isi.append(node.data)
            node = node.next
        isi.reverse()                             # tampilkan dasar -> top
        return f"StackDinamis(isi={isi} <- top)"