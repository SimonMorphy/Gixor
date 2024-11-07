import time

import requests
from bs4 import BeautifulSoup
import json
import pymysql


def get_user_stat(name, max_retries=3, retry_delay=2):
    for attempt in range(max_retries + 1):
        try:
            url = f'https://github-readme-stats.vercel.app/api?username={name}&show=reviews,discussions_started,discussions_answered,prs_merged,prs_merged_percentage'
            resp = requests.get(url)
            resp.raise_for_status()  # 检查请求是否成功
            soup = BeautifulSoup(resp.text, 'html.parser')

            user_stats = {}

            # 提取等级
            rank_text = soup.find('text', {'data-testid': 'level-rank-icon'})
            user_stats['Rank'] = rank_text.text.strip() if rank_text else 'N/A'

            # 提取Total Stars Earned
            stars_text = soup.find('text', {'data-testid': 'stars'})
            user_stats['Total Stars Earned'] = stars_text.text.strip() if stars_text else 'N/A'

            # 提取Total Commits (2024)
            commits_text = soup.find('text', {'data-testid': 'commits'})
            user_stats['Total Commits (2024)'] = commits_text.text.strip() if commits_text else 'N/A'

            # 提取Total PRs
            prs_text = soup.find('text', {'data-testid': 'prs'})
            user_stats['Total PRs'] = prs_text.text.strip() if prs_text else 'N/A'

            # 提取Total PRs Merged
            prs_merged_text = soup.find('text', {'data-testid': 'prs_merged'})
            user_stats['Total PRs Merged'] = prs_merged_text.text.strip() if prs_merged_text else 'N/A'

            # 提取Merged PRs Percentage
            prs_merged_percentage_text = soup.find('text', {'data-testid': 'prs_merged_percentage'})
            user_stats['Merged PRs Percentage'] = prs_merged_percentage_text.text.strip() if prs_merged_percentage_text else 'N/A'

            # 提取Total PRs Reviewed
            reviews_text = soup.find('text', {'data-testid': 'reviews'})
            user_stats['Total PRs Reviewed'] = reviews_text.text.strip() if reviews_text else 'N/A'

            # 提取Total Issues
            issues_text = soup.find('text', {'data-testid': 'issues'})
            user_stats['Total Issues'] = issues_text.text.strip() if issues_text else 'N/A'

            # 提取Total Discussions Started
            discussions_started_text = soup.find('text', {'data-testid': 'discussions_started'})
            user_stats['Total Discussions Started'] = discussions_started_text.text.strip() if discussions_started_text else 'N/A'

            # 提取Total Discussions Answered
            discussions_answered_text = soup.find('text', {'data-testid': 'discussions_answered'})
            user_stats['Total Discussions Answered'] = discussions_answered_text.text.strip() if discussions_answered_text else 'N/A'

            # 提取Contributed to (last year)
            contribs_text = soup.find('text', {'data-testid': 'contribs'})
            user_stats['Contributed to (last year)'] = contribs_text.text.strip() if contribs_text else 'N/A'

            return user_stats

        except (requests.RequestException, ValueError, AttributeError) as e:
            if attempt < max_retries:
                print(f"Attempt {attempt + 1} failed. Retrying in {retry_delay} seconds...")
                time.sleep(retry_delay)
            else:
                print(f"Attempt {attempt + 1} failed. No more retries.")
                raise e

def get_user(src, params):
    response = requests.get(src)
    if response.status_code == 429:
        print("达到请求限制，等待一段时间后重试...")
        time.sleep(60)  # 等待60秒
        return get_user(src, params)
    return response


def get_user_skills(src):
    resp = requests.get(src)
    soup = BeautifulSoup(resp.text, 'html.parser')
    lang_items = soup.find_all('g', {'class': 'stagger'})
    for item in lang_items:
        lang_name = item.find('text', {'data-testid': 'lang-name'})
        lang_percentage = item.find('text', x='215', y='34')

        if lang_name:
            print("Language:", lang_name.text)
        if lang_percentage:
            print("Percentage:", lang_percentage.text)


def get_user_info(name):
    count_star = 0
    count_fork = 0
    count_contributions = 0

    url = 'https://github.com/' + name
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    target_divs = soup.find_all('p', dir='auto')

    print(target_divs)
    img_tag = target_divs[1].find('img')
    img_tag2 = target_divs[2].find('img')
    if img_tag is not None and img_tag2 is not None:
        src_value = img_tag.get('src')
        print(src_value)
        get_user_stat(src_value)

        src_value2 = img_tag2.get('src')
        print(src_value2)
        get_user_skills(src_value2)
    else:
        page = 1
        while True:
            url = 'https://github.com/' + name + '?sort=name&tab=repositories&page=' + str(page)
            result = requests.get(url)
            # print(result.text)
            soup = BeautifulSoup(result.text, 'html.parser')
            li_elements = soup.find_all('li',
                                        class_='col-12 d-flex flex-justify-between width-full py-4 border-bottom color-border-muted public source')
            if not li_elements:
                break
            for li in li_elements:
                # project_name_a = li.find('a', itemprop="name codeRepository")
                # project_name = project_name_a.text.strip() if project_name_a else 'Unknown'
                star_a = li.find('a', class_='Link--muted mr-3', href=lambda x: x and 'stargazers' in x)
                fork_a = li.find('a', class_='Link--muted mr-3', href=lambda x: x and 'forks' in x)
                star_count = star_a.find('svg', class_='octicon octicon-star').parent.text.strip() if star_a else '0'
                fork_count = fork_a.find('svg',
                                         class_='octicon octicon-repo-forked').parent.text.strip() if fork_a else '0'
                count_star += int(str(star_count).replace(',', ''))
                count_fork += int(str(fork_count).replace(',', ''))
                # print(f"Project name: {project_name},Star count: {star_count}, Fork count: {fork_count}")
            page += 1
        url = 'https://github.com/search'
        params = {'q': 'author:' + name + ' is:issue', 'type': 'issues'}
        response = get_user(url, params)
        soup = BeautifulSoup(response.text, 'html.parser')
        issue_result = soup.find('script', {'data-target': "react-app.embeddedData"}).string
        # if issue_result:
        #     print(issue_result.string)
        data = json.loads(issue_result)
        print('issue_result', data['payload']['result_count'])
        print('-----------------------------')
        url = 'https://github.com/search'
        params = {'q': 'author:' + name + ' is:issue', 'type': 'pullrequests'}
        response = get_user(url, params)
        # print(response.text)
        soup = BeautifulSoup(response.text, 'html.parser')
        prs_result = soup.find('script', {'data-target': "react-app.embeddedData"}).string
        # if prs_result:
        #     print(prs_result.string)
        data = json.loads(prs_result)
        # print(data)
        print('prs_result', data['payload']['result_count'])
        print("Total Star Count:", count_star)
        print("Total Fork Count:", count_fork)


# get_user_info('merveenoyan')
connection = pymysql.connect(host='localhost', user='root', password='1234', db='spider')
with connection:
    cursor = connection.cursor()
    cursor.execute("SELECT id, login FROM users LIMIT 22629 OFFSET 18129")
    results = cursor.fetchall()
    for row in results:
        user_id, user_name = row
        user_stats = get_user_stat(user_name)
        sql = (
            'INSERT INTO has_score(id,name,grade,Total_Stars_Earned,Total_Commits_2024,Total_PRs,Total_PRs_Merged,Merged_PRs_Percentage,Total_PRs_Reviewed,Total_Issues,Total_Discussions_Started,Total_Discussions_Answered,Contributed_to_last_year) '
            'value( %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)')
        try:
            cursor.execute(sql, (user_id, user_name, user_stats['Rank'], user_stats['Total Stars Earned'],
                                  user_stats['Total Commits (2024)'], user_stats['Total PRs'],
                                  user_stats['Total PRs Merged'], user_stats['Merged PRs Percentage'],
                                  user_stats['Total PRs Reviewed'], user_stats['Total Issues'],
                                  user_stats['Total Discussions Started'], user_stats['Total Discussions Answered'],
                                  user_stats['Contributed to (last year)']
            ))
            connection.commit()
        except pymysql.err.IntegrityError as e:
            print(f"Error: {e}")
            connection.rollback()
    cursor.close()
    connection.close()




