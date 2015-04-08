package com.material.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.daimajia.easing.BaseEasingMethod;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.material.R;
import com.material.ui.widget.DrawView;
import com.material.ui.widget.EasingAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by mojingtian on 15/4/7.
 */
public class EasingFragment extends Fragment{

    private ListView mEasingList;
    private EasingAdapter mAdapter;
    private View mTarget;

    private DrawView mHistory;

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_easing, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEasingList = (ListView)getView().findViewById(R.id.easing_list);
        mAdapter = new EasingAdapter(getActivity());
        mEasingList.setAdapter(mAdapter);
        mTarget = getView().findViewById(R.id.target);
        mHistory = (DrawView)getView().findViewById(R.id.history);
        mEasingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mHistory.clear();
                Skill s = (Skill)view.getTag();
                AnimatorSet set = new AnimatorSet();
                mTarget.setTranslationX(0);
                mTarget.setTranslationY(0);
                set.playTogether(
                        Glider.glide(s, 1200, ObjectAnimator.ofFloat(mTarget, "translationY", 0, dipToPixels(getActivity(), -(160 - 3))), new BaseEasingMethod.EasingListener() {
                            @Override
                            public void on(float time, float value, float start, float end, float duration) {
                                mHistory.drawPoint(time, duration, value - dipToPixels(getActivity(), 60));
                            }
                        })
                );
                set.setDuration(1200);
                set.start();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
