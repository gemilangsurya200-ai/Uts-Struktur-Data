from mystack import StackStatis
from mystack import StackOverflowError
from mystack import StackUnderflowError
x = StackStatis(4)
for y in (10,30,50,60):
    x.push(y)
print(x, "| size:", x.size(), "| is_full:", x.is_full())
try:
    x.push(70)
except StackOverflowError as e:
    print(e)
x.pop()
x.pop()
x.pop()
x.pop()
try:
    x.pop()
except StackUnderflowError as u:
    print(u)
    