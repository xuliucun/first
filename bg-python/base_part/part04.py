#!/usr/bin/python3

"""
变量
内存
引用
"""

# ========================
# 全局变量
# num = 100
# def test():
#     global num  # 想使用全局变量只能加global
#     num += 200
#     print(num)


# ========================
# id(xx)可查看xx的地址
num = 100
print(id(num))

num = 102
print(id(num))

num = 103
tt = num
print(id(tt))


# test()
