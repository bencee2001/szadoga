package util

import org.joda.time.format.DateTimeFormat

const val dateTimePattern = "MM-dd-HH-mm"
val fileNameDateFormater = DateTimeFormat.forPattern(dateTimePattern)