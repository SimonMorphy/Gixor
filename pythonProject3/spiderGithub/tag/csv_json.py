import csv
import json


def csv_to_json(csv_file_path, json_file_path):
    # 创建一个空列表来存储数据
    data = []

    # 打开CSV文件
    with open(csv_file_path, mode='r', encoding='utf-8') as csv_file:
        # 使用csv.DictReader读取CSV文件
        csv_reader = csv.DictReader(csv_file)

        # 遍历CSV文件中的每一行，并添加到data列表中
        for row in csv_reader:
            data.append(row)

    # 将数据写入JSON文件
    with open(json_file_path, mode='w', encoding='utf-8') as json_file:
        json.dump(data, json_file, indent=4)
csv_file_path = '../data_analysis/has_score_location.csv'
json_file_path = '../data_analysis/has_score_location.json'

csv_to_json(csv_file_path, json_file_path)
