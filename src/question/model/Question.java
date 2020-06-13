/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package question.model;

/**
 *
 * @author canale
 */
public class Question {
    
    private final String question;
    private final AnswerProcessor mode;

    public Question(String question, AnswerProcessor mode) {
        this.question = question;
        this.mode = mode;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerProcessor getMode() {
        return mode;
    }
    
}
