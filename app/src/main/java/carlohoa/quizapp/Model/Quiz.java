package carlohoa.quizapp.Model;

public class Quiz {

    public int ID;
    public String category;
    public String type;
    public String difficulty;
    public String question;
    public String correctAnswer;

    public Quiz(){

    }

    public Quiz(String category, String type, String difficulty, String question, String correctAnswer){
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public void setID(int ID){ this.ID = ID; }
    public void setCategory(String category){ this.category = category; }
    public void setType(String type){ this.type = type; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setQuestion(String question) { this.question = question; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public int getID() { return ID; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getDifficulty() { return difficulty; }
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
}
