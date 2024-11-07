# import requests
# import re
#
# url = 'https://github.com/topics?page='
# page = 1
#
# from urllib.request import urlopen
#
# url = url + str(page)
# resp = urlopen(url)
# html_data = resp.read().decode('utf - 8')
# # print(html_data)
# href_pattern = re.compile(r'<a href="([^"]+)" class="no-underline flex-1 d-flex flex-column">')
# # 匹配主题名称（第一个p标签内容）的正则表达式
# topic_pattern = re.compile(r'<p class="f3 lh-condensed mb-0 mt-1 Link--primary">([^<]+)</p>')
# # 匹配主题描述（第二个p标签内容）的正则表达式
# description_pattern = re.compile(r'<p class="f5 color-fg-muted mb-0 mt-1">([^<]+)</p>')
#
# hrefs = href_pattern.findall(html_data)
# topics = topic_pattern.findall(html_data)
# descriptions = description_pattern.findall(html_data)
#
# for href, topic, description in zip(hrefs, topics, descriptions):
#     print(f"href: {href}")
#     clean_description = description.strip()
#     print(f"内容: {topic} - {clean_description}")
#     print("-" * 20)

import requests
import re
import csv




def get_page_data(page):
    url = 'https://github.com/topics?page=' + str(page)
    try:
        resp = requests.get(url)
        html_data = resp.text
        # 检查是否满足结束条件
        end_condition = re.search(r'<h2 class="h2">All featured topics</h2>.*?<div>(.*?)</div>', html_data, re.DOTALL)
        if end_condition and end_condition.group(1).strip() == "":
            return False
        href_pattern = re.compile(r'<a href="([^"]+)" class="no-underline flex-1 d-flex flex-column">')
        topic_pattern = re.compile(r'<p class="f3 lh-condensed mb-0 mt-1 Link--primary">([^<]+)</p>')
        description_pattern = re.compile(r'<p class="f5 color-fg-muted mb-0 mt-1">([^<]+)</p>')
        hrefs = href_pattern.findall(html_data)
        topics = topic_pattern.findall(html_data)
        descriptions = description_pattern.findall(html_data)
        return hrefs, topics, descriptions
    except Exception as e:
        print(e)
        return False


page = 1
with open('github_topics.csv', 'w', newline='', encoding='utf-8') as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(['href', 'topic', 'description'])
    while True:
        data = get_page_data(page)
        if not data:
            break
        hrefs, topics, descriptions = data
        for href, topic, description in zip(hrefs, topics, descriptions):
            clean_description = description.strip()
            writer.writerow([href, topic, clean_description])
        page += 1