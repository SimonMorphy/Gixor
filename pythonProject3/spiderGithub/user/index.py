import requests
import pymysql
#需要token

url = "https://api.github.com/search/users"
followers_url = "https://api.github.com/users/smallrh/followers"


class GitHubUser:
    def __init__(self, login, id, node_id, avatar_url, gravatar_id, url, html_url, followers_url, following_url,
                 gists_url, starred_url, subscriptions_url, organizations_url, repos_url, events_url,
                 received_events_url, user_type, user_view_type, site_admin, score):
        self.login = login
        self.id = id
        self.node_id = node_id
        self.avatar_url = avatar_url
        self.gravatar_id = gravatar_id
        self.url = url
        self.html_url = html_url
        self.followers_url = followers_url
        self.following_url = following_url
        self.gists_url = gists_url
        self.starred_url = starred_url
        self.subscriptions_url = subscriptions_url
        self.organizations_url = organizations_url
        self.repos_url = repos_url
        self.events_url = events_url
        self.received_events_url = received_events_url
        self.type = user_type
        self.user_view_type = user_view_type
        self.site_admin = site_admin
        self.score = score

    @classmethod
    def from_dict(cls, user_dict):
        return cls(
            user_dict.get('login'),
            user_dict.get('id'),
            user_dict.get('node_id'),
            user_dict.get('avatar_url'),
            user_dict.get('gravatar_id'),
            user_dict.get('url'),
            user_dict.get('html_url'),
            user_dict.get('followers_url'),
            user_dict.get('following_url'),
            user_dict.get('gists_url'),
            user_dict.get('starred_url'),
            user_dict.get('subscriptions_url'),
            user_dict.get('organizations_url'),
            user_dict.get('repos_url'),
            user_dict.get('events_url'),
            user_dict.get('received_events_url'),
            user_dict.get('type'),
            user_dict.get('user_view_type'),
            user_dict.get('site_admin'),
            user_dict.get('score')
        )


db_config = {
    'host': '127.0.0.1',
    'user': 'root',
    'password': '1234',
    'database': 'spider'
}
headers = {
    "Accept": "application/vnd.github+json",
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36'
}

all_users = []
page = 35
final_page = 0

while True:
    params = {
        "q": "s",
        'page': page
    }
    response = requests.get(url, headers=headers, params=params)
    if response.status_code == 200:
        result = response.json()
        print(result)
        users = result["items"]
        if len(users) == 0:
            break
        for user_dict in users:
            user_obj = GitHubUser.from_dict(user_dict)
            all_users.append(user_obj)
        page += 1
    else:
        if final_page == page:
            continue
        else:
            print(f"请求失败，状态码: {response.status_code}")
            conn = pymysql.connect(**db_config)
            try:
                with conn.cursor() as cursor:
                    insert_sql = """
                    INSERT INTO github_users (
                        login, id, node_id, avatar_url, gravatar_id, url, html_url, followers_url, following_url,
                        gists_url, starred_url, subscriptions_url, organizations_url, repos_url, events_url,
                        received_events_url, type, user_view_type, site_admin, score
                    ) VALUES (
                        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                    )
                        """
                    for user in all_users:
                        cursor.execute(insert_sql, (
                            user.login, user.id, user.node_id, user.avatar_url, user.gravatar_id, user.url,
                            user.html_url,
                            user.followers_url, user.following_url, user.gists_url, user.starred_url,
                            user.subscriptions_url,
                            user.organizations_url, user.repos_url, user.events_url, user.received_events_url,
                            user.type,
                            user.user_view_type, user.site_admin, user.score
                        ))
                conn.commit()
            finally:
                final_page = page
                print(f"已写入第 {page} 页数据")
                all_users.clear()
                conn.close()
        continue
