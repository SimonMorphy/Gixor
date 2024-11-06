import pymysql
import json

# 建立数据库连接
connection = pymysql.connect(
    host='localhost',
    user='root',
    password='1234',
    database='spider'
)

try:
    with connection.cursor() as cursor:
        # 查询数据
        sql = "SELECT * FROM tag"
        cursor.execute(sql)
        results = cursor.fetchall()

        # 获取列名
        columns = [column[0] for column in cursor.description]

        # 将结果转换为 JSON
        json_data = [dict(zip(columns, row)) for row in results]
        json_output = json.dumps(json_data, indent=4)
        columns = [column[0] for column in cursor.description]

        # 将结果转换为 JSON
        json_data = [dict(zip(columns, row)) for row in results]
        json_output = json.dumps(json_data, indent=4)

        # 将 JSON 数据保存到当前文件夹下的文件中
        with open('../tag/data_major.json', 'w') as file:
            file.write(json_output)

finally:
    connection.close()