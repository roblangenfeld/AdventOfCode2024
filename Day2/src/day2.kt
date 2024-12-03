package Day2

import java.io.File

fun main(){
    part1()
    part2()
}

fun part1(){
    val input = File("Day2/src/day2part1.txt")
            .readLines()
            .map { line ->
                line.trim()
                        .split("\\s+".toRegex())
                        .map { it.toInt() }
            }

    var count = 0
    for(list in input){
        val isIncreasing = list.zipWithNext { a, b -> b - a }
                .all { diff -> diff in 1..3 }
        val isDecreasing = list.zipWithNext { a, b -> a - b }
                .all { diff -> diff in 1..3 }
        if(isDecreasing || isIncreasing){
            count++
        }
    }

    print(count)
}

fun part2(){
    val input = File("Day2/src/day2part2.txt")
            .readLines()
            .map { line ->
                line.trim()
                        .split("\\s+".toRegex())
                        .map { it.toInt() }
            }

    var count = 0
    for (list in input) {
        //previous solution works. But let's iterate through each report now and remove
        //one element each time to match the rules of a skip level
        val isReportSafe = list.indices.any { skipIndex ->
            val trimmedReport = list.filterIndexed { index, _ -> index != skipIndex }

            val isIncreasing = trimmedReport.zipWithNext { a, b -> b - a }
                    .all { diff -> diff in 1..3 }

            val isDecreasing = trimmedReport.zipWithNext { a, b -> a - b }
                    .all { diff -> diff in 1..3 }

            isIncreasing || isDecreasing
        }

        if (isReportSafe) {
            count++
        }
    }

    print(count)
}