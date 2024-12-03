fun asHeading(title: String, width: Int = 80): String {
    if (title.length > width)
        throw IllegalArgumentException("Heading '$title' is longer than width $width")

    return title
        .padStart((width - title.length) / 2 + title.length, '=')
        .padEnd(width, '=')
}

fun main() {
    println(asHeading("Day 01"))
    day01.main()
    println(asHeading("Day 02"))
    day02.main()
    println(asHeading("Day 03"))
    day03.main()
}
