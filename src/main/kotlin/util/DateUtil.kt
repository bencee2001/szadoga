package util

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

const val dateTimeFilePattern = "MM-dd-HH-mm"
val fileNameDateFormatter: DateTimeFormatter = DateTimeFormat.forPattern(dateTimeFilePattern)

const val dateTimePrettyPattern = "yyyy-MM-dd'T'HH:mm:ss"
val prettyDateFormatter: DateTimeFormatter = DateTimeFormat.forPattern(dateTimePrettyPattern)

