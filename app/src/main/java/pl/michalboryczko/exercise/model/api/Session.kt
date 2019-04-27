package pl.michalboryczko.exercise.model.api


data class Session(
        val sessionId: String,
        val name: String,
        val password: String
)


data class Story(
        val storyId: String,
        val sessionId: String,
        val story: String,
        val description: String,
        val estimations: HashMap<String, Int>?
)

data class Estimation(
        val storyId: String,
        val points: Int,
        val userId: String
)