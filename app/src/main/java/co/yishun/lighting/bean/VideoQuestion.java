package co.yishun.lighting.bean;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.ui.PlayActivity_;
import co.yishun.lighting.ui.ShootActivity_;
import co.yishun.lighting.ui.view.QuestionView;
import co.yishun.lighting.util.FileUtil;

/**
 * Created by carlos on 3/29/16.
 */
public class VideoQuestion implements QuestionView.IQuestion {
    public static final int REQUEST_VIDEO = 2;
    public final int mOrder;
    public final String mQuestion;
    @Procedure.QuestionType
    public final String mType;
    private Answer mAnswer;

    public VideoQuestion(int mOrder, @Procedure.QuestionType
    String type, String mQuestion, Answer mAnswer) {
        this.mOrder = mOrder;
        mType = type;
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }

    @Override
    public void onRecordAnswer(Context context) {
        ShootActivity_.intent(context).startForResult(mOrder);
    }

    @Override
    public void setAnswer(Answer answer) {
        mAnswer = answer;
    }

    @Override
    public int getQuestionOrder() {
        return mOrder;
    }

    @Override
    public boolean isAnswered() {
        return mAnswer != null;
    }

    @Override
    public String getQuestionName() {
        return mQuestion;
    }

    private File getAnswerFile(Context context) {
        return FileUtil.getVideoStoreFile(context, mType, mOrder);
    }

    private Uri getAnswerUri(Context context) {
        return Uri.fromFile(getAnswerFile(context));
    }

    @Override
    public void onPlayAnswer(Context context) {
        PlayActivity_.intent(context).uri(getAnswerUri(context)).start();
    }

    @Override
    public void onDeleteAnswer(Context context) {
        getAnswerFile(context).delete();
        mAnswer = null;
    }


    public class VideoAnswer implements Answer {
        public File getAnswerFile(Context context) {
            return FileUtil.getVideoStoreFile(context, mType, mOrder);
        }

    }

}
