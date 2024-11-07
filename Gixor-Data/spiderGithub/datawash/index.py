import pymysql
import requests
from bs4 import BeautifulSoup

headers = {
    "cookie":"_osm_session=035d34b240479b45d48cc72343061391; _osm_totp_token=713891; _pk_id.1.cf09=a1557f9818b2e43b.1730624767.; _pk_ses.1.cf09=1; _osm_welcome=hide; _osm_location=-71.11223|42.37833|14|M",
    "x-csrf-token":"fRERpjPr1kafn4Ipgy44vFJcoZ0TsWmKDJkFxtqsIXSHe6fizUbkf-Rdr8pEpWGnAO4oMZ6Bfqj0RGLwtpzBXw",
    "user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36",
    "x-requested-with":"XMLHttpRequest"
}
def getCountry(location):
    if location.split(', ')[-1]:
        url_location = url + location
        try:
            response = requests.post(url_location, headers=headers)
            # print(response.text)
            soup = BeautifulSoup(response.text, 'html.parser')
            link = soup.find('a')
            data_name = link.get('data-name')
            parts = data_name.split(', ')
            country = parts[-1]
            return country

        except Exception as e:
            try:
                url_location = url + location.split(', ')[-1]
                response = requests.post(url_location, headers=headers)
                # print(response.text)
                soup = BeautifulSoup(response.text, 'html.parser')
                link = soup.find('a')
                data_name = link.get('data-name')
                parts = data_name.split(', ')
                country = parts[-1]
                return country
            except Exception as e:
                return "N/A"
url = "https://www.openstreetmap.org/geocoder/search_osm_nominatim?query="
# getCountry("São Paulo")

def get_top_1000_data(offset):
    connection = pymysql.connect(host='localhost', user='root', password='1234', database='spider')
    cursor = connection.cursor()
    query = "SELECT location FROM no_lcation_followers GROUP BY location LIMIT 2000 offset %s"
    cursor.execute(query, (offset,))
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return data
def update_data(location, nation):
    connection = pymysql.connect(host='localhost', user='root', password='1234', database='spider')
    cursor = connection.cursor()
    query = "UPDATE no_lcation_followers SET nation = %s WHERE location = %s"
    try:
        # 执行更新操作前可以先打印一下要执行的 SQL 语句和参数，方便调试
        print(f"SQL: {query}, Parameters: ({nation}, {location})")
        cursor.execute(query, (nation, location))
        connection.commit()
    except Exception as e:
        print(f"更新数据时出现错误：{e}")
        connection.rollback()
    finally:
        cursor.close()
        connection.close()
while True:
    offset  = 40993
    lists = get_top_1000_data(offset)
    for i in lists:
        if i[0] !='N/A' :
            print(i[0])
            update_data(i[0],getCountry(i[0]))
        continue
    offset += 1000
# getCountry("Europe, Czech Republic, Prague")


