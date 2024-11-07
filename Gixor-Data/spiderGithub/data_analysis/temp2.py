import pymysql as pysql
import os
import csv

# 数据库连接配置
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '1234',
    'db': 'spider'
}

# CSV 文件路径
csv_file_path = 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\has_score_location.csv'

# 临时文件路径
data_file_path = 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\has_score_data_location.csv'

def export_table_with_headers():
    try:
        # 连接到数据库
        conn = pysql.connect(**db_config)
        cursor = conn.cursor()

        # 导出数据并写入临时文件
        data_query = """
        SELECT u.id, u.location, u.login, h.grade, h.Total_Stars_Earned, h.Total_Commits_2024, h.Total_PRs, h.Total_PRs_Merged, h.Merged_PRs_Percentage, h.Total_PRs_Reviewed, h.Total_Issues, h.Total_Discussions_Started, h.Total_Discussions_Answered, h.Contributed_to_last_year, h.score
        FROM users u 
        LEFT JOIN has_score h 
        ON u.id = h.id 
        WHERE u.location IS NOT NULL;
        """
        cursor.execute(data_query)

        # 获取列名
        columns = [desc[0] for desc in cursor.description]

        # 获取查询结果
        results = cursor.fetchall()

        # 将结果写入临时文件
        with open(data_file_path, 'w', newline='', encoding='utf-8') as data_file:
            writer = csv.writer(data_file)
            writer.writerow(columns)  # 写入表头
            writer.writerows(results)  # 写入数据

        # 读取临时文件并写入最终的 CSV 文件
        with open(data_file_path, 'r', newline='', encoding='utf-8') as data_file, \
             open(csv_file_path, 'w', newline='', encoding='utf-8') as csv_file:
            csv_file.write(data_file.read())

        print(f"Data has been successfully exported to {csv_file_path}")
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        # 关闭数据库连接
        conn.close()

        # 删除临时文件
        if os.path.exists(data_file_path):
            os.remove(data_file_path)

if __name__ == "__main__":
    export_table_with_headers()
