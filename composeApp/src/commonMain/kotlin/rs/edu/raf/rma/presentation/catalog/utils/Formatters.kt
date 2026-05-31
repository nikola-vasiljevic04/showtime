package rs.edu.raf.rma.presentation.catalog.utils

import kotlin.math.round

fun formatVotes(votes: Int?): String = when {
    votes == null -> "0"
    votes >= 1_000_000 -> "${(round((votes / 1_000_000f) * 10f) / 10f).toString().removeSuffix(".0")}M"
    votes >= 1_000 -> "${(round((votes / 1_000f) * 10f) / 10f).toString().removeSuffix(".0")}K"
    else -> votes.toString()
}