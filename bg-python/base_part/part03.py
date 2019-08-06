#!/usr/bin/python3

"""
函数
"""
# =======================================
# 可写函数说明
def printinfo1(name, age):
    "打印任何传入的字符串"
    print("名字: ", name)
    print("年龄: ", age)
    return


# 调用printinfo函数
printinfo1(age=50, name="runoob")  # 指定参数
printinfo1(50, "runoob")  # 按顺序




# =======================================
# 参数是元组
def printinfo2(arg1, *vartuple):
    "打印任何传入的参数"
    print("输出: ")
    print(arg1)
    for var in vartuple:
        print(var)
    return


# 调用printinfo 函数
printinfo2(10)
printinfo2(70, 60, 50)




# =======================================
# 可写函数说明
def printinfo3(arg1, **vardict):
    "打印任何传入的参数"
    print("输出: ")
    print(arg1)
    print(vardict)


# 调用printinfo 函数
printinfo3(1, a=2, b=3)
