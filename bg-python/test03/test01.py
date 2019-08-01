
# # 学习
# i = 0
# while i < 100:
#     print("****")
#     print("当前是第%d次执行循环"%(i+1))
#     i += 1

# # 练习一：一百内求和
# i=0
# sum=0
# while i<=100:
#     sum=sum+i
#     i+=1
# print("0-100的和为：%d"%sum)
# print("离开单价法来看"+"ajkdfjkal")

# 练习二：九九乘法表
i = 1
while i <= 9:
    j = 1
    while j <= i:
        # %2d占两个空格；%-2d如果只有一个字符则靠左显示
        # 4*2= 8  4*2=8
        # 5*2=10  5*2=10
        print("%d*%d=%-2d  " % (i, j, i * j), end="")
        j += 1
    print("\n")
    i += 1
