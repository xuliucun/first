#!/usr/bin/python3

"""
python3的标准数据类型
Number（数字）
String（字符串）
List（列表）
Tuple（元组）
Set（集合）
Dictionary（字典）
"""
# ----------------------------------------

# a, b, c, d = 1, 2, "runoob", 5+6j  # 赋值
# print(type(a), type(b), type(c), type(d))
#
# a = isinstance(a, int)  # 判断类型
# print(a)
#
# print(5 + 4)  # 加法
# print(4.3 - 2)  # 减法
# print(3 * 7)  # 乘法
# print(2 / 4)  # 除法，得到一个浮点数
# print(3 // 4)  # 除法，得到一个整数 除法取整
# print(17 % 3)  # 取余
# print(2 ** 5)  # 乘方  2的5次方

# ----------------------------------------

# str = 'Runoob'
# print(str)  # 输出字符串
# print(str[0:-1])  # 输出第一个到倒数第二个的所有字符
# print(str[0])  # 输出字符串第一个字符
# print(str[2:5])  # 输出从第三个开始到第五个的字符
# print(str[2:])  # 输出从第三个开始的后的所有字符
# print(str[::-1])  # 倒着读
# print(str * 2)  # 输出字符串两次
# print(str + "TEST")  # 连接字符串
# print('Ru\noob')  # \n 换行
# print(r'Ru\noob')  # r取消转义
# word = 'Python'
# print(word[0], word[5])  # Python中的字符串有两种索引方式，从左往右以0开始，从右往左以-1开始。
# print(word[-1], word[-6])
# adc = '0123456789abcdefghijklmnopqrstuvwsyz'
# print(adc[0:20:3])  # 0369cfi   #从0到20个每隔3个取一个值
# print(adc.find("23"))
# print(adc.find("23", 0, 20))
# print(adc.rfind("23"))
# print(adc.index("23"))
#
# abc = "asdfasgasdfasdfasdfasdfasdfwertfstgsdfgasdf"
# print(abc.find("sd"))
# print(abc.count("sd"))
# print(abc.replace("sd", "xxx", 3))  # 换3个
# print(abc.split("f", 2))  # 按顺序就切2次
# # 获取文件的后缀
# fileName = "第一序列.txt"
# print(fileName[fileName.rindex(".") + 1:])

# ----------------------------------------

list1 = ['abcd', 786, 2.23, 'runoob', 70.2]
# tinylist1 = [123, 'runoob']
# print(list1)  # 输出完整列表
# print(list1[0])  # 输出列表第一个元素
# print(list1[1:3])  # 从第二个开始输出到第三个元素
# print(list1[2:])  # 输出从第三个元素开始的所有元素
# print(tinylist1 * 2)  # 输出两次列表
# print(list1 + tinylist1)  # 连接列表

# list1.append("sadfasd") #增
# list1.insert(0,"sdf")
# for val in list1:
#     print(val)

# print(list1.index('runoob',0,5)) #查
# print('runoob' in list1)

# print(list1)  # 输出完整列表
# list1[1] = "adsfasdfa"  # 改
# del list1[3]  # 按下标删
# list1.pop()  # 删最后一个
# list1.remove(2.23)  # 指定元素删
# print(list1)  # 输出完整列表

# chers=["xlc","fgl","zyb","zbb"]
# room =["主卧","次卧"]

# ----------------------------------------
# # 可以存放一些不能修改的变量，如银行利率，日常常量
# tuple = ('abcd', 786, 2.23, 'runoob', 70.2)
# tinytuple = (123, 'runoob')
# print(tuple)  # 输出完整元组
# print(tuple[0])  # 输出元组的第一个元素
# print(tuple[1:3])  # 输出从第二个元素开始到第三个元素
# print(tuple[2:])  # 输出从第三个元素开始的所有元素
# print(tinytuple * 2)  # 输出两次元组
# print(tuple + tinytuple)  # 连接元组

# ----------------------------------------
# 字典
dictory = {"name1": 'abcd', "name2": 786, "name3": 2.23, "name4": 'runoob', "name5": 70.2}
print(dictory)
dictory["name2"] = 8989898  # 字典里的值可以被重新赋值
print(dictory)
print(dictory.get("nameadf1", 100))  # 如果没有key，可以给一个默认值
print(dictory.get("name1", 100))
# ----------------------------------------


# def reverseWords(input):
#     # 通过空格将字符串分隔符，把各个单词分隔为列表
#     inputWords = input.split(" ")
#     # 翻转字符串
#     # 假设列表 list = [1,2,3,4],
#     # list[0]=1, list[1]=2 ，而 -1 表示最后一个元素 list[-1]=4 ( 与 list[3]=4 一样)
#     # inputWords[-1::-1] 有三个参数
#     # 第一个参数 -1 表示最后一个元素
#     # 第二个参数为空，表示移动到列表末尾
#     # 第三个参数为步长，-1 表示逆向
#     inputWords = inputWords[-1::-1]
#     # 重新组合字符串
#     output = ' '.join(inputWords)
#     return output
#
# if __name__ == "__main__":
#     input = 'I like runoob'
#     rw = reverseWords(input)
#     print(rw)
