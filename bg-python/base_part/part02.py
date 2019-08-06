#!/usr/bin/python3
"""
if
循环
遍历
"""


# 1================================

# num = int(input("输入一个数字："))
# if num % 2 == 0:
#     if num % 3 == 0:
#         print("你输入的数字可以整除 2 和 3")
#     else:
#         print("你输入的数字可以整除 2，但不能整除 3")
# else:
#     if num % 3 == 0:
#         print("你输入的数字可以整除 3，但不能整除 2")
#     else:
#         print("你输入的数字不能整除 2 和 3")

# list1 = ['abcd', 786, 2.23, 'runoob', 70.2]
# i = 0
# for temp in list1:
#     print("第%s个元素为：%s" % (i, temp))
#     i += 1

# count = 0
# while count < 5:
#     print(count, " 小于 5")
#     count = count + 1
# else:
#     print(count, " 大于或等于 5")


# 2===============================

# i = 0
# dictory = {"name1": 'abcd', "name2": 786, "name3": 2.23, "name4": 'runoob', "name5": 70.2}
# for temp in dictory.items():
#     print(temp)
#     print("第%s个元素为：%s" % (i, temp))  # 第i个元素为：('name4', 'runoob')
#     i += 1


# 3================================

# i = 0
# dictory = {"name1": 'abcd', "name2": 786, "name3": 2.23, "name4": 'runoob', "name5": 70.2}
# for x,y in dictory.items():
#     print("第%s个元素的key=%s  value=%s" % (i,x,y))  # 第i个元素为：('name4', 'runoob')
#     i += 1


# 4=============================
# 区别于java的赋值方式

# list1 = ['abcd', 786, 2.23, 'runoob', 70.2]
# x, y, z, q, w = list1
# print(x)

# 5=============================

# trips = ('abcd', 786, 2.23, 'runoob', 70.2)
# for i, x in enumerate(trips):
#     print("第%s个元素的value=%s" % (i, x))  # 第i个元素为：('name4', 'runoob')

# 6=============================

sites = ["Baidu", "Google", "Runoob", "Taobao"]
for site in sites:
    if site == "Runoob":
        print("菜鸟教程!")
        break
    print("循环数据 " + site)
else:
    print("没有循环数据!")
print("完成循环!")
