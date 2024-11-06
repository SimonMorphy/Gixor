import re
import requests
import csv
import pandas as pd


# 3d

class TopicData:
    def __init__(self, url, name, description):
        self.url = url
        self.name = name
        self.description = description


base_url = 'https://github.com'
df = pd.read_csv('github_topics.csv')
first = df.iloc[0].tolist()
print(first)
first_data = TopicData(first[0], first[1], first[2])
print(first_data.url)
print(first_data.name)
print(first_data.description)
url = base_url + first_data.url + '?page=1'
# page = 1
resp = requests.get(url)
html_data = resp.text
print(html_data)
with open('topics_first.html', 'w', encoding='utf - 8') as f:
    f.write(html_data)