import time

import requests
from bs4 import BeautifulSoup
import pymysql as pysql



def get_top_1000_data(offset):
    connection = pysql.connect(host='localhost', user='root', password='1234', database='spider')
    cursor = connection.cursor()
    query = "SELECT h.id,h.name from has_score h  left join users u ON u.id = h.id where u.location is NOT NULL LIMIT 20000 offset %s"
    cursor.execute(query, (offset,))
    data = cursor.fetchall()
    cursor.close()
    connection.close()
    return data


def insertIntoTag(param, text, param1):
    try:
        # 连接到数据库
        connection = pysql.connect(host='localhost', user='root', password='1234', database='spider')
        cursor = connection.cursor()

        # 定义插入语句
        query = "INSERT INTO tag (id, tag, name) VALUES (%s, %s, %s)"

        # 执行插入语句
        cursor.execute(query, (param, text, param1))

        # 提交事务
        connection.commit()
    except Exception as e:
        # 处理异常
        print(f"Error: {e}")
        connection.rollback()
    finally:
        # 关闭游标和连接
        cursor.close()
        connection.close()


def getPage(page, param, param1):

    url = 'https://github.com/' + param1 + '?page=' + str(page) + '&tab=repositories'
    header = {
        'cookie': '_octo=GH1.1.1421971470.1706466876; _device_id=297c7a6c6a6e2913c1b52528d4418ae4; GHCC=Required:1-Analytics:1-SocialMedia:1-Advertising:1; fs_uid=#o-1FH3DA-na1#6363448799866880:6871966182300377848:::#/1761289055; saved_user_sessions=149983598%3AtxNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; user_session=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; __Host-user_session_same_site=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; logged_in=yes; dotcom_user=smallrh; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; preferred_color_mode=dark; tz=Asia%2FShanghai; _gh_sess=JxJ2pQi%2FC3hs2x%2BV8ym56QkMtlQgAe4Qm%2F6xueiEICIYbFno4KZCFKjrkbM75v%2FO47rSdgYqrRVKi8BOy%2BUIHLB2iFl1zRGcY%2B14reOOfUy1KndOo7V2tDrG1%2F052jIjbOVCGW0Mj1JrxzmRMol%2F7mPJnS%2BI%2B%2BR8gXtrKP3VCNbGS5gzIHI%2BPVLIDwlujOMy79BOdga1FviTcVPzjG6y2TkSSe3KU6PolY38jnW5kkgi54MMu4KHsJRmXNfLySPYFB3TV3h3XoW00ml%2FbqoZdPHRITuM4338xQghAg3TAnP5PU95eHrK28XodAoQ6q2gCMdnBpgfq1wEde%2BLS8%2FaomrXviCmU8yXW5QCHVTCAB6cAs7dKKag8fcfhDXo7qghYs40L4uYQnPdOT3cv8TuC0OZ%2BnDyxJuR78FE9TgcLsHPzE4CRFgTApQtgAbmW6u%2FRiQ2RkidJaaLJ8ZQ--rHolT258uuzInbmh--bXSP5dBZX1ru830ZNWVu0A%3D%3D'
    }
    try:
        response = requests.get(url, headers=header)
        soup = BeautifulSoup(response.text, 'html.parser')
        blankslate_div = soup.find('h2', class_='blankslate-heading')
        if blankslate_div:
            return 1  # 返回状态字段 1
        divs = soup.find_all('div', class_='topics-row-container d-inline-flex flex-wrap flex-items-center f6 my-1')
        # 提取 div 内的所有 a 标签
        for div in divs:
            a_tags = div.find_all('a')
            # 打印每个 a 标签的文本内容
            for a in a_tags:
                print(a.text.strip())
                insertIntoTag(param, a.text, param1)
        return 0
    except Exception as e:
        print(f"Error: {e}")
        return getPage(page, param, param1)

def searchRespo(param, param1):
    status = 0
    page = 1
    while True:
        if status == 1 or page > 50:
            break
        print("当前处理第" + str(page) + "页")
        status = getPage(page, param, param1)
        page += 1


while True:
    offset = 1959
    lists = get_top_1000_data(offset)
    for i in lists:
        print(i[0], i[1])
        if i[0] == '9999440':
            break
        searchRespo(i[0], i[1])
    # time.sleep(2)

# searchRespo('1', 'emilk')
