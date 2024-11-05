fun createQueryParams(
    sort: String = "",
    direction: String = "desc",
    perPage: Int = 8,
    page: Int = 1
): Map<String, String> {
    return mapOf(
        "sort" to sort,
        "direction" to direction,
        "perPage" to perPage.toString(),
        "page" to page.toString()
    )
}

fun createPageQueryParams(
    page: Int = 1,
    perPage: Int = 8
): Map<String, String> {
    return mapOf(
        "perPage" to perPage.toString(),
        "page" to page.toString()
    )
}

fun createStateQueryParams(
    state: String = "open",
    page: Int = 1,
    perPage: Int = 8
):Map<String, String> {
    return mapOf(
        "state" to state,
        "perPage" to perPage.toString(),
        "page" to page.toString()
    )
}