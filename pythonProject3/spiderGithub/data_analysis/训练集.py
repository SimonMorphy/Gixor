import math

import pymysql


def exponential_cdf(x):
    return 1 - 2 ** (-x)


def log_normal_cdf(x):
    return x / (1 + x)


def calculateRank(commits, prs, issues, reviews, stars, followers):
    variables = [commits, prs, issues, reviews, stars, followers]
    for i in range(len(variables)):
        var = variables[i]
        if 'k' in var:
            variables[i] = int(float(var.replace('k', '')) * 1000)
        else:
            variables[i] = int(var)
    commits, prs, issues, reviews, stars, followers = variables
    COMMITS_MEDIAN = 250
    COMMITS_WEIGHT = 2
    PRS_MEDIAN = 50
    PRS_WEIGHT = 3
    ISSUES_MEDIAN = 25
    ISSUES_WEIGHT = 1
    REVIEWS_MEDIAN = 2
    REVIEWS_WEIGHT = 1
    STARS_MEDIAN = 50
    STARS_WEIGHT = 4
    FOLLOWERS_MEDIAN = 10
    FOLLOWERS_WEIGHT = 1

    TOTAL_WEIGHT = (
        COMMITS_WEIGHT +
        PRS_WEIGHT +
        ISSUES_WEIGHT +
        REVIEWS_WEIGHT +
        STARS_WEIGHT +
        FOLLOWERS_WEIGHT
    )

    rank = 1 - (
        COMMITS_WEIGHT * exponential_cdf(commits / COMMITS_MEDIAN) +
        PRS_WEIGHT * exponential_cdf(prs / PRS_MEDIAN) +
        ISSUES_WEIGHT * exponential_cdf(issues / ISSUES_MEDIAN) +
        REVIEWS_WEIGHT * exponential_cdf(reviews / REVIEWS_MEDIAN) +
        STARS_WEIGHT * log_normal_cdf(stars / STARS_MEDIAN) +
        FOLLOWERS_WEIGHT * log_normal_cdf(followers / FOLLOWERS_MEDIAN)
    ) / TOTAL_WEIGHT

    THRESHOLDS = [1, 12.5, 25, 37.5, 50, 62.5, 75, 87.5, 100]
    LEVELS = ["S", "A+", "A", "A-", "B+", "B", "B-", "C+", "C"]
    level = LEVELS[next((index for index, t in enumerate(THRESHOLDS) if rank * 100 <= t), len(LEVELS) - 1)]

    return level, rank * 100
def connect_to_database():
    return pymysql.connect(
        host='localhost',
        user='root',
        password='1234',
        database='spider'
    )
def get_users_from_database():
    conn = connect_to_database()
    try:
        with conn.cursor() as cursor:
            query = "SELECT * FROM has_score limit 10000 offset 2000"
            cursor.execute(query)
            columns = [col[0] for col in cursor.description]
            users = []
            for row in cursor.fetchall():
                user = dict(zip(columns, row))
                users.append(user)
        return users
    finally:
        conn.close()
def update_database():
    conn = connect_to_database()
    try:
        with conn.cursor() as cursor:
            users = get_users_from_database()
            for user in users:
                level, percentile = calculateRank(
                    user["Total_Commits_2024"],
                    user["Total_PRs"],
                    user["Total_Issues"],
                    user["Total_PRs_Reviewed"],
                    user["Total_Stars_Earned"],
                    user["Contributed_to_last_year"]
                )
                score = (1 - percentile / 100) * 100
                update_query = "UPDATE has_score SET score = %s WHERE id = %s"
                cursor.execute(update_query, (score, user["id"]))
            conn.commit()
    except Exception as e:
        print(e)
    finally:
        conn.close()
users = get_users_from_database()
update_database()