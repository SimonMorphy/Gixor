# ?q=s&type=users爬取搜索信息为s的用户
import time
import json
import requests
from bs4 import BeautifulSoup
import pymysql as mysql

url = 'https://github.com/search'


def get_page_data(page):
    params = {
        'q': 'z',
        'type': 'users',
        'p': page
    }

    resp = requests.get(url, params=params)
    # print(resp.text)
    soup = BeautifulSoup(resp.text, 'html.parser')
    script_tags = soup.find_all('script', {'data-target': "react-app.embeddedData"})
    # print(script_tags)
    return script_tags
mydb = mysql.connect(
       host="localhost",
       user="root",
       password="1234",
       database="spider"
   )

mycursor = mydb.cursor()
page = 80
while True:
    if page ==101:
        break
    result = get_page_data(page)

    if not result:
        # print(result)
        continue
    else:
        print('正在爬取第%d页' % page)
        result = str(result[0])
        # print(result)
        start_index = result.find('{')
        end_index = result.rfind('}') + 1
        json_str = result[start_index:end_index]
        data = json.loads(json_str)["payload"]["results"]

        for user in data:
            followed_by_current_user = user["followed_by_current_user"] if user["followed_by_current_user"] is not None else False
            is_current_user = user["is_current_user"]
            sponsorable = user["sponsorable"]
            sql = "INSERT INTO users (avatar_url, hl_login, hl_name, hl_profile_bio, followed_by_current_user, followers, id, is_current_user, location, login, display_login, name, profile_bio, sponsorable, repos) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
            val = (user["avatar_url"], user["hl_login"], user["hl_name"], user["hl_profile_bio"], followed_by_current_user,
                   user["followers"], user["id"], is_current_user, user["location"], user["login"], user["display_login"],
                   user["name"], user["profile_bio"], sponsorable, user["repos"])
            mycursor.execute(sql, val)
            mydb.commit()

        page += 1
        print(result)
        print('----------------')
mydb.close()
    # time.sleep(1)
