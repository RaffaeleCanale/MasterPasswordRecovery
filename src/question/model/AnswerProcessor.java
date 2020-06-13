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
public enum AnswerProcessor {
    PLAIN {
        @Override
        public String process(String question) {
            return question;
        }
    },
    STRIPPED {
        @Override
        public String process(String question) {
            return question
                    .replaceAll("\\W", "");
        }
    },
    STRIPPED_LOWERCASE {
        @Override
        public String process(String question) {
            return STRIPPED.process(question.toLowerCase());
        }
    };

    public abstract String process(String question);

}
