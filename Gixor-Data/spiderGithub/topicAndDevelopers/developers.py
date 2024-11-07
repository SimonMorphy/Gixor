import requests
from bs4 import BeautifulSoup
import json
import redis

def getHtmlText(url, header):
    response = requests.get(url,headers=header)
    if response.status_code != 200:
        print(f"Failed to retrieve the page. Status code: {response.status_code}")
        exit()
    else:
        return response.text

def get_developers(html):
    soup = BeautifulSoup(html, 'html.parser')
    results = soup.find_all('article', class_='Box-row d-flex')
    return results

headers = {
    "cookie":"_octo=GH1.1.1421971470.1706466876; _device_id=297c7a6c6a6e2913c1b52528d4418ae4; GHCC=Required:1-Analytics:1-SocialMedia:1-Advertising:1; fs_uid=#o-1FH3DA-na1#6363448799866880:6871966182300377848:::#/1761289055; saved_user_sessions=149983598%3AtxNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; user_session=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; __Host-user_session_same_site=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; logged_in=yes; dotcom_user=smallrh; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; preferred_color_mode=dark; tz=Asia%2FShanghai; _gh_sess=AgcH%2FQ6AoEGv8JIdaNJc8LO3K0n2oxGC194epb6A2thMfPHnLZFDyUsIFp6%2BpFhpEzwa41IovlqX5ICh5O5spLYRjFq%2F5h01GXhjHPooDOM4s4rNj%2BiZEjc%2F10rOgK%2BdTpOxLiqUFUbmxMRhtSaQvA23skfPko5UdOlv%2BGEBJT9n6TKGDSLVlpm5JCg8gGdUtpHulYxnrPVKnM%2FtCmwaP2DhGLsOnyQc1XD2ixbyjgKhaYVnrLAHcHFz1D5OaZEKINhXofpCWaOKYm2HrB6PGxdMDbNNBsmwTWVI4Tz%2FsLDVLyJ%2Fvur5PLUdnqrfDCcp1C13I76QEfUMwdbUXS4ugE3fdqpWSWroCeYF%2BVoyOvQrj0MNV2fKzfqY1bRmAZuYMjjUngVIIwvKiKs1OB3cc1hYEZt2HZVJYX5gotjoi44Cb%2BztCCkDlgi6Dskrw92foQYXjQDVRFQ7WgdVpXus63XdhJJi%2FBDRxve5dZaGwMIimPZ%2BH5Kx1C%2Bl0u%2BUHn3W%2BhpCJ2%2F2ZhTc8%2B1105zyLRLUT0m6O%2FkayYrhdldLhnw2Jf0IULCbW1v8%2Fbo%3D--NegMvbl%2BDHI%2FeNeF--LN1RN7UGp47K5oWQYsBtPA%3D%3D"
}
URL = 'https://github.com/trending/developers'
HTML = getHtmlText(URL, headers)
topics = get_developers(HTML)

repos = []
# 遍历每个仓库卡片
for card in topics:
    repo_info = card.find('h1', class_='h3 lh-condensed').find('a')
    try:
        repo_info2 = card.find('h1', class_='h4 lh-condensed').find('a')
        repo_name = repo_info2.text.strip().split('/')[-1]
    except Exception as e:
        repo_name = 'Unknown'
    name = repo_info.text.strip()
    github_name = repo_info['href'].strip('/')

    repo_description = card.find('div', class_='f6 color-fg-muted mt-1').text.strip() if card.find('div',class_='f6 color-fg-muted mt-1') else 'No description'

    # 创建仓库对象
    repo = {
        'username': github_name,
        'displayName':name ,
        'avatarUrl': 'https://github.com/'+github_name+'.png',
        'repoName':'https://github.com/'+github_name+'/'+repo_name,
        'description': repo_description
    }
    repos.append(repo)
    # 将仓库对象转换成JSON字符串
repos_json = json.dumps(repos, indent=4, ensure_ascii=False)

# # 输出JSON字符串
print(repos_json)
redis_client = redis.StrictRedis(host='39.105.98.60', port=6379, db=0, password='agrinavi')

# 将JSON字符串存储到Redis中
redis_key = 'trendy-developers'
redis_client.set(redis_key, repos_json)

print(f"Data stored in Redis with key: {redis_key}")
