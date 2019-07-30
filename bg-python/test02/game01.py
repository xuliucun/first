import random

print()
print(" *******欢迎来到猜拳游戏********")
print(" *******让我们一局定输赢********")
print(" ***********游戏规则************")
print(" *******0-剪刀 1-石头 2-布******")
print(" ***********开始PK吧************\n")

count1, count2, l = 0, 0, 0
f = 0
while l < 5:
    y = int(input(" 请输入你的数字："))
    if y > 2 or y < 0:
        print(" 输入错误，游戏结束！\n")
        break
    r = random.randrange(0, 3)
    print(" 玩家：", y, "  电脑：", r)
    c = y - r

    if c == -2 or c == 1:
        count1 += 1
        print(" 此局玩家胜！\n")
    elif c == 0:
        print(' 此局为平局！\n')
    else:
        count2 += 1
        print(" 此局电脑胜！\n")
    if count1 == 3 and l <= 5:
        print('\n 玩家先胜三局，玩家胜出！')
        f = 1
        break;

    if count2 == 3 and l <= 5:
        print(' \n 电脑先胜三局，电脑胜出！')
        f = 1
        break

    l += 1

if f == 0:
    print('\n 五局结束,', end='')
    print('玩家胜了', count1, '局，', '电脑胜了', count2, '局', '\n 最终结果为：', end='')
    if count1 > count2:
        print(' 玩家胜!')
    elif count1 == count2:
        print(' 平局!')
    else:
        print(' 电脑胜！')
