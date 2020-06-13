package question.model;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.EnumCaster;
import com.wx.util.representables.string.PairRepr;
import com.wx.util.representables.string.StringRepr;

/**
 * @author canale
 */
public class QuestionRepr implements TypeCaster<String, Question> {

    private static final String SEPARATOR = " - ";

    private final PairRepr<String, AnswerProcessor> pairRepr =
            new PairRepr<>(new StringRepr(), new EnumCaster<>(AnswerProcessor.values()),
                    SEPARATOR);

    @Override
    public String castIn(Question value) throws ClassCastException {
        return pairRepr.castIn(value.getQuestion(), value.getMode());
    }

    @Override
    public Question castOut(String value) throws ClassCastException {
        return pairRepr.castOut(value, Question::new);
    }

}
