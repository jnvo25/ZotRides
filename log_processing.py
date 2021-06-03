# This is a sample Python script.

# Press ⌃R to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.
import random



# Press the green button in the gutter to run the script.
if __name__ == '__main__':

    readfile = open('test', 'r')

    count = 0
    sum_TS = 0
    sum_TJ = 0
    for line in readfile:
        line_tokens = line.split(" ")
        sum_TS += int(line_tokens[1])
        sum_TJ += int(line_tokens[3])
        count += 1
    print("Average TS: " + str(sum_TS / count))
    print("Average TJ: " + str(sum_TJ / count))

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
