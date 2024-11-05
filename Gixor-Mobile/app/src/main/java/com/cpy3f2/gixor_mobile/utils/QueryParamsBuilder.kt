fun createQueryParams(
    sort: String = "created",
    direction: String = "desc",
    perPage: Int = 30,
    page: Int = 1
): Map<String, String> {
    return mapOf(
        "sort" to sort,
        "direction" to direction,
        "per_page" to perPage.toString(),
        "page" to page.toString()
    )
}