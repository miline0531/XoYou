package kr.co.genericit.mybase.xoyou2.network.action;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;

/**
 * Retrofit 공통 Action Class
 */
public class ActionRuler {
    private final LinkedList<Runnable> mRunnableList = new LinkedList<Runnable>();
    private static final ActionRuler ruler = new ActionRuler();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private ActionRuler() {

    }

    public static ActionRuler getInstance() {
        return ruler;
    }

    public synchronized void runNext() {
        if (!isListEmpty(mRunnableList)) {
            if (mRunnableList.get(0) != null) {
                Runnable runnable = mRunnableList.get(0);
                mRunnableList.remove(0);
                mHandler.post(runnable);
            } else {
                mRunnableList.remove(0);
            }
        } else {
            runFinishAction();
        }
    }

    public void finish() {
        clear();
        runFinishAction();
    }

    public void skip() {
        if (!isListEmpty(mRunnableList))
            mRunnableList.remove(0);
    }

    public boolean hasAction(Runnable runnable) {
        if (mRunnableList.contains(runnable)) {
            return true;
        }
        return false;
    }

    public void remove(Runnable runnable) {
        if (mRunnableList.contains(runnable))
            mRunnableList.remove(runnable);
    }

    public void addRunnable(Runnable runnable) {
        mRunnableList.add(runnable);
    }

    public void addAction(Action action) {
        mRunnableList.add(action);
    }

    public void addActionAtFirst(Action action) {
        mRunnableList.add(0, action);
    }

    public void addRunnableAtFirst(Runnable runnable) {
        mRunnableList.add(0, runnable);
    }

    public int getCount() {
        if (isListEmpty(mRunnableList)) {
            return 0;
        }
        return mRunnableList.size();
    }

    private void clear() {
        mRunnableList.clear();
    }

    private Action mFinishAction = null;

    public void setOnFinishAction(Action action) {
        mFinishAction = action;
    }

    private void runFinishAction() {
        if (mFinishAction != null) {
            Action temp = mFinishAction;
            mFinishAction = null;
            temp.run();
        }
    }

    private boolean isListEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
