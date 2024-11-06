import requests
from bs4 import BeautifulSoup
import json
import redis
import re



def getHtmlText(url, header):
    response = requests.get(url,headers=header)
    if response.status_code != 200:
        print(f"Failed to retrieve the page. Status code: {response.status_code}")
        exit()
    else:
        return response.text
def get_topics(html):
    soup = BeautifulSoup(html, 'html.parser')
    results = soup.find_all('article', class_='Box-row')
    return results
headers = {
    "cookie":"_octo=GH1.1.1421971470.1706466876; _device_id=297c7a6c6a6e2913c1b52528d4418ae4; GHCC=Required:1-Analytics:1-SocialMedia:1-Advertising:1; fs_uid=#o-1FH3DA-na1#6363448799866880:6871966182300377848:::#/1761289055; saved_user_sessions=149983598%3AtxNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; user_session=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; __Host-user_session_same_site=txNWokiS4_Z6KKe_8128eVgoKc7hwXEIaOkwkGwUbJtPxad-; logged_in=yes; dotcom_user=smallrh; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; preferred_color_mode=dark; tz=Asia%2FShanghai; _gh_sess=NHKCDS1LY1s0Hi%2FC9IP23j06lxuGqTJucAri2OGN%2FZT5uQYvsPYMgLa9Rba8mEq6wG5btKSmvEPf%2F3TVY1JSiFcmuNyYtGcD9VPgLq6N7N2VoCow23qti56Wg69LSd1WF4LH5QGGxOj5IJtz3S70Cg3W5oLdLUob953mLsQEXIy1tJN7tj8LFJbLdixVeczdyY8OOP3ik9viOhd2wletrMIngnShznNWn71mAxfyJjg6xLWavJUtuhP4zDa6mq9ysuh7Bku4BHUbdgiHNmC25TDbRU0Nvqeq%2BeCY3meJa2LlSuK5CNybHxw4gLrtmgIr0z0dIxuUzKHZQtl%2Bl6U3jSFIi%2F4s9TkN19PnOUwmjruZg1aL5K9lAOTpAy%2BIz7GNpCAvPBwhOL74Ew%2BkzwckpZLPFgumzs5uBmEXlDIN%2BoQpJR%2FW%2FEhXe6GxQue%2BF3Nt%2FcoyyMiOcBvEzi%2BIZfz2gGBdkywJ9eLunBLdVuxZipW0yrGcgiZblYF3ZLgenDACjBPUER7tOCNuk%2FY7GLcgLTvMYM4gbbh34e9AA0IBZE0h%2FVoACy0ZBDtoJX0%3D--nXIWqADqRSkbXdxZ--ZfPgGNX8kk2YOzFxxcEr9Q%3D%3D"
}
URL = 'https://github.com/trending'
HTML = getHtmlText(URL, headers)
topics = get_topics(HTML)
repos = []

# 遍历每个仓库卡片
for card in topics:
    # 获取仓库名称和所有者
    builtBy = []
    repo_info = card.find('h2', class_='h3 lh-condensed').find('a')
    repo_url = repo_info['href']
    repo_owner = repo_url.split('/')[1]
    repo_name = repo_url.split('/')[-1]
    # 获取仓库描述
    repo_description = card.find('p', class_='col-9 color-fg-muted my-1 pr-4').text.strip() if card.find('p',
                                                                                                         class_='col-9 color-fg-muted my-1 pr-4') else 'No description'
    # 获取仓库语言
    repo_language = card.find('span', itemprop='programmingLanguage').text.strip() if card.find('span',
                                                                                                itemprop='programmingLanguage') else 'Unknown'
    # < span class ="repo-language-color" style="background-color: #3572A5" > < / span >
    repo_language_color = card.find('span', class_='repo-language-color').get('style')
    repo_language_color = repo_language_color.split(':')[-1].strip()
    # print(repo_language_color)
    # 获取仓库收藏数
    repo_stars = card.find('a', href=f"/{repo_owner}/{repo_name}/stargazers").text.strip().replace(',', '')

    # 获取仓库Fork数
    repo_forks = card.find('a', href=f"/{repo_owner}/{repo_name}/forks").text.strip().replace(',', '')

    # 获取仓库今天所收星数
    stars_today = card.find('span', class_='d-inline-block float-sm-right').text.strip()
    stars_today = ''.join(filter(str.isdigit, stars_today))
    # <span data-view-component="true" class="d-inline-block mr-3">
    contributor = card.find('span', class_='d-inline-block mr-3')
    a_tags = contributor.find_all('a', class_='d-inline-block')

    for a in a_tags:
        # print(a)
        username = a.find('img')['alt'][1:]  # 去掉@符号
        href = a['href']
        avatar_url = a.find('img')['src']
        avatar_url =re.sub(r'\?s=\d+&v=\d+', '', avatar_url)

        contributor_info = {
            'username': username,
            'href': 'https://github.com'+href,
            'avatar':avatar_url
        }
        builtBy.append(contributor_info)
    repo = {
        'author': repo_owner,
        'name': repo_name,
        'avatar': 'https://github.com/'+repo_owner+'.png',
        'url':'https://github.com/'+repo_owner+'/'+repo_name,
        'description': repo_description,
        'language': repo_language,
        'languageColor':repo_language_color,
        'stars': repo_stars,
        'forks': repo_forks,
        'currentPeriodStars': stars_today,
        'builtBy':builtBy
    }
    repos.append(repo)

repos_json = json.dumps(repos, indent=4, ensure_ascii=False)

# 输出JSON字符串
print(repos_json)

redis_client = redis.StrictRedis(host='39.105.98.60', port=6379, db=0,password='agrinavi')

# 将JSON字符串存储到Redis中
redis_key = 'trendy-repos'
redis_client.set(redis_key, repos_json)

print(f"Data stored in Redis with key: {redis_key}")

# 将JSON字符串保存到文件
with open('repos.json', 'w', encoding='utf-8') as f:
    f.write(repos_json)


