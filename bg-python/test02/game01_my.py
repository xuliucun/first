import random
i = 0
while i<5:
    playerInput = input("请输入（0剪刀  1石头  2布）：")
    # 玩家
    player = int(playerInput)
    # 电脑
    mac = random.randint(0, 2)

    # 判断

    if (player == 0 and mac == 2) or (player == 1 and mac == 0) or (player == 2 and mac == 1):
        print("玩家胜")
    elif player == mac:
        print("平局")
    else:
        print("电脑胜")
    i+=1