# import csv
# import requests
# from bs4 import BeautifulSoup
#
# base_url = 'https://github.com/mrdoob'
# options = ['following', 'followers']
# location_count = {}
# locations = []
# for option in options:
#     page = 1
#     while True:
#         url = base_url + '?page=' + str(page) + '&tab=' + option
#         print(url)
#         resp = requests.get(url)
#         html_data = resp.text
#         soup = BeautifulSoup(html_data, 'html.parser')
#
#         if soup.find('p', class_='mt-4'):
#             break
#         else:
#             page += 1
#             for div in soup.find_all('div', class_='d-table-cell col-9 v-align-top pr-3'):
#                 location_tag = div.find('svg', class_='octicon octicon-location')
#                 if location_tag:
#                     location = location_tag.find_next_sibling(string=True).strip()
#                 else:
#                     location = 'N/A'
#                 locations.append(location)
#
#
#     for loc in locations:
#         if loc not in location_count:
#             location_count[loc] = 0
#         location_count[loc] += 1
#     with open(option + '_locations.csv', 'w', newline='', encoding='utf - 8') as csvfile:
#         writer = csv.writer(csvfile)
#         for loc, count in location_count.items():
#             writer.writerow([loc, count])
#

# 优化
import csv
import requests
from bs4 import BeautifulSoup


def update_csv(option, location_count):
    with open(option + '_locations.csv', 'w', newline='', encoding='utf - 8') as csvfile:
        writer = csv.writer(csvfile)
        for loc, count in location_count.items():
            writer.writerow([loc, count])


base_url = 'https://github.com/mrdoob'
options = ['following', 'followers']

for option in options:
    page = 1
    location_count = {}
    while True:
        url = base_url + '?page=' + str(page) + '&tab=' + option
        print(url)
        resp = requests.get(url)
        html_data = resp.text
        soup = BeautifulSoup(html_data, 'html.parser')

        if soup.find('p', class_='mt-4'):
            break
        else:
            for div in soup.find_all('div', class_='d-table-cell col-9 v-align-top pr-3'):
                location_tag = div.find('svg', class_='octicon octicon-location')
                if location_tag:
                    location = location_tag.find_next_sibling(string=True).strip()
                else:
                    location = 'N/A'

                if location not in location_count:
                    location_count[location] = 0
                location_count[location] += 1
            update_csv(option, location_count)
            page += 1





