package rs.edu.raf.rma.domain.model

sealed class QuizQuestion(
    val title: String,
    val questionText: String,
    val imageUrl: String,
    val options: List<String>,
    val correctOptionIndex: Int
) {
    class GuessMovie(imageUrl: String, options: List<String>, correctOptionIndex: Int) :
        QuizQuestion("Guess the Movie", "What movie is this?", imageUrl, options, correctOptionIndex)

    class GuessYear(movieTitle: String, imageUrl: String, options: List<String>, correctOptionIndex: Int) :
        QuizQuestion("Guess the Year", "In what year was '$movieTitle' released?", imageUrl, options, correctOptionIndex)

    class GuessActor(movieTitle: String, imageUrl: String, options: List<String>, correctOptionIndex: Int) :
        QuizQuestion("Guess the Actor", "Which actor played in '$movieTitle'?", imageUrl, options, correctOptionIndex)
}