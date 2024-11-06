import logging
from urllib.parse import urlsplit
import requests
from bs4 import BeautifulSoup
import pymysql as pysql

# 配置日志记录
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

headers = {
    'Cookie': '_octo=GH1.1.1421971470.1706466876; _device_id=297c7a6c6a6e2913c1b52528d4418ae4; user_session=6oKJmc0uhqYdEaPM7P_hJRpiJg2aNeL_ELg9cZO47VGkG_LL; __Host-user_session_same_site=6oKJmc0uhqYdEaPM7P_hJRpiJg2aNeL_ELg9cZO47VGkG_LL; logged_in=yes; dotcom_user=smallrh; GHCC=Required:1-Analytics:1-SocialMedia:1-Advertising:1; fs_uid=#o-1FH3DA-na1#6363448799866880:6871966182300377848:::#/1761289055; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; preferred_color_mode=dark; tz=Asia%2FShanghai; _gh_sess=g4EU0HLlZ9ZdwN47Inj22bk898T9gQ6Ymmtt%2Birao0lxG82rIe55dmu5vGSmY%2FBfUGkkRoQdXjSTuZz9xo7LBzqZditDAChOujLN2FT7hBY1tolxZ7kX7K32nRayh08ZG%2B00E2%2BuAYTV6QzBEHnuVEb82nydhyvwzxrmkLdB%2FEjUF5FK09VRJkOF38YvneNjPgrVNrgkEKU64NViKBRPwzItsSxis1LMLjRwdH5X8lSUCJEawxKMpWt4iaBJW07jc8eJNC2XO2IRZxrKjgdBhCEZztvjWn%2BAx%2BQTB6jAnspUWahhGBS%2F9APN4M87r8PMxN24sjZwIMmlBY009Mvlb5jooDap16LTBQwlsoZ1ymoxpSPjaL1mM2rMt2ZIQhaF--NYxmQsRMu7EnutBQ--yGnUsoCaa8No%2FdVAtcR3ig%3D%3D'}

def requests_retry_session(
    retries=3,
    backoff_factor=0.3,
    status_forcelist=(500, 502, 504),
    session=None,
):
    session = session or requests.Session()
    retry = requests.packages.urllib3.util.retry.Retry(
        total=retries,
        read=retries,
        connect=retries,
        backoff_factor=backoff_factor,
        status_forcelist=status_forcelist,
    )
    adapter = requests.adapters.HTTPAdapter(max_retries=retry)
    session.mount('http://', adapter)
    session.mount('https://', adapter)
    return session

def insert_no_location_follower(user):
    with pysql.connect(host='localhost', user='root', password='1234', db='spider') as conn:
        with conn.cursor() as cursor:
            sql = "INSERT INTO no_lcation_followers (id, following_id, name, github_name, location) VALUES (%s, %s, %s, %s, %s)"
            cursor.execute(sql, (
                user['Avatar ID'], user['following_id'], user['Username'], user['GitHub Username'], user['City Info']
            ))
            conn.commit()

def find_followers_location(name, userid):
    page = 1
    while True:
        url = f'https://github.com/{name}?page={page}&tab=followers'
        try:
            response = requests_retry_session().get(url, headers=headers)
            response.raise_for_status()
            soup = BeautifulSoup(response.text, 'html.parser')
        except requests.exceptions.RequestException as e:
            logging.error(f"Request failed for {url}: {e}")
            break

        user_blocks = soup.find_all('div', class_='d-table table-fixed col-12 width-full py-4 border-bottom color-border-muted')
        user_data = []

        if len(user_blocks) == 0 or page > 90:
            break

        for block in user_blocks:
            username = block.find('span', class_='f4 Link--primary').text.strip() if block.find('span', class_='f4 Link--primary') else 'N/A'
            github_username = block.find('span', class_='Link--secondary').text.strip() if block.find('span', class_='Link--secondary') else 'N/A'
            avatar_img = block.find('img', class_='avatar avatar-user')
            avatar_url = avatar_img['src'] if avatar_img else 'N/A'
            avatar_id = urlsplit(avatar_url).path.split('/')[-1].split('?')[0] if avatar_img else 'N/A'
            p_element = block.find('p', class_='color-fg-muted text-small mb-0')
            city_info = p_element.find('svg', class_='octicon octicon-location').next_sibling.strip() if p_element and p_element.find('svg', class_='octicon octicon-location') else 'N/A'

            user_data.append({
                'Avatar ID': avatar_id,
                'following_id': userid,
                'Username': username,
                'GitHub Username': github_username,
                'City Info': city_info
            })

        logging.info(f'Page: {page}')
        page += 1

        for user in user_data:
            insert_no_location_follower(user)

def insert_no_location_followings(user):
    with pysql.connect(host='localhost', user='root', password='1234', db='spider') as conn:
        with conn.cursor() as cursor:
            sql = "INSERT INTO no_lcation_followings (id, follower_id, name, github_name, location) VALUES (%s, %s, %s, %s, %s)"
            cursor.execute(sql, (
                user['Avatar ID'], user['follower_id'], user['Username'], user['GitHub Username'], user['City Info']
            ))
            conn.commit()

def find_following_location(name, userid):
    page = 1
    while True:
        url = f'https://github.com/{name}?page={page}&tab=following'
        try:
            response = requests_retry_session().get(url, headers=headers)
            response.raise_for_status()
            soup = BeautifulSoup(response.text, 'html.parser')
        except requests.exceptions.RequestException as e:
            logging.error(f"Request failed for {url}: {e}")
            break

        user_blocks = soup.find_all('div', class_='d-table table-fixed col-12 width-full py-4 border-bottom color-border-muted')
        user_data = []

        if len(user_blocks) == 0 or page > 90:
            break

        for block in user_blocks:
            username = block.find('span', class_='f4 Link--primary').text.strip() if block.find('span', class_='f4 Link--primary') else 'N/A'
            github_username = block.find('span', class_='Link--secondary').text.strip() if block.find('span', class_='Link--secondary') else 'N/A'
            avatar_img = block.find('img', class_='avatar avatar-user')
            avatar_url = avatar_img['src'] if avatar_img else 'N/A'
            avatar_id = urlsplit(avatar_url).path.split('/')[-1].split('?')[0] if avatar_img else 'N/A'
            p_element = block.find('p', class_='color-fg-muted text-small mb-0')
            city_info = p_element.find('svg', class_='octicon octicon-location').next_sibling.strip() if p_element and p_element.find('svg', class_='octicon octicon-location') else 'N/A'

            user_data.append({
                'Avatar ID': avatar_id,
                'follower_id': userid,
                'Username': username,
                'GitHub Username': github_username,
                'City Info': city_info
            })

        logging.info(f'Page: {page}')
        page += 1

        for user in user_data:
            insert_no_location_followings(user)

with pysql.connect(host='localhost', user='root', password='1234', db='spider') as conn:
    with conn.cursor() as cursor:
        cursor.execute("SELECT id, login FROM users WHERE ISNULL(location) LIMIT 6500 OFFSET 6449")
        results = cursor.fetchall()

for row in results:
    user_id, user_name = row
    logging.info(user_name)
    find_followers_location(user_name, user_id)
    logging.info('-------------------------------------------------------------')
    find_following_location(user_name, user_id)
