import pandas as pd
import pymysql

# 读取 CSV 文件
file_path = 'predictions.csv'  # 替换为你的 CSV 文件路径
df = pd.read_csv(file_path)

def update_data(score, id):
    connection = pymysql.connect(host='localhost', user='root', password='1234', database='spider')
    cursor = connection.cursor()
    query = "UPDATE has_score SET score = %s WHERE id = %s"
    try:
        # 执行更新操作前可以先打印一下要执行的 SQL 语句和参数，方便调试
        print(f"SQL: {query}, Parameters: ({score}, {id})")
        cursor.execute(query, (score, id))
        connection.commit()
    except Exception as e:
        print(f"更新数据时出现错误：{e}")
        connection.rollback()
    finally:
        cursor.close()
        connection.close()


for index, row in df.iterrows():
    SCORE = row['Predicted_Score']
    ID = row['ID']
    update_data(SCORE, ID)
print("数据已成功插入到数据库中。")
