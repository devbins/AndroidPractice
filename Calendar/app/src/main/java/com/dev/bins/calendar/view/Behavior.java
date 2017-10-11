package com.dev.bins.calendar.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.view.Choreographer;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by bin on 11/10/2017.
 */

public class Behavior extends CoordinatorLayout.Behavior<RecyclerView> {


    public Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private RecycleViewCalendar mCalendarView;




    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        mCalendarView = (RecycleViewCalendar) parent.getChildAt(0);
        int height = mCalendarView.getMeasuredHeight();
        //防止切换月份，引起布局变化
        if (mCalendarView.getState() != RecycleViewCalendar.STATE_COLLAPSE) {
            parent.onLayoutChild(child, layoutDirection);
            child.offsetTopAndBottom(height);
        }
        return true;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dx, int dy, int[] consumed) {

//        RecycleViewCalendar calendarView = (RecycleViewCalendar) coordinatorLayout.getChildAt(0);
//        int top = child.getTop();
//        int height = calendarView.getMeasuredHeight();
//        if (top >= height && dy <0) {
//            consumed[0] = 0;
//            consumed[1] = 0;
//            child.offsetTopAndBottom(height - top);
//            calendarView.offsetTopAndBottom(-calendarView.getTop());
//            return;
//        }
//        if (top<=calendarView.getMinTop() && dy > 0){
//            consumed[0] = 0;
//            consumed[1] = 0;
//            return;
//        }
//        child.offsetTopAndBottom(-dy);
////        calendarView.offsetTopAndBottom((int) (-dy*0.8));
//        calendarView.onScroll(dy);


        if (dy >0){
            scrollUp(child,dy,consumed);
        }else {
            scrollDown(child,dy,consumed);
        }

    }



    public void scrollUp(RecyclerView child, int dy, int[] consumed){


        int top = child.getTop();

        if (top <= mCalendarView.getMinTop()){

            consumed[0] = 0;
            consumed[1] = 0;
            return;
        }

        child.offsetTopAndBottom(-dy);
        mCalendarView.onScroll(dy);
    }


    public void scrollDown(RecyclerView child, int dy, int[] consumed){

        LinearLayoutManager layoutManager= (LinearLayoutManager) child.getLayoutManager();
        int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisiblePosition != 0){
            consumed[0] = 0;
            consumed[1] = 0;
            return;
        }

        if (mCalendarView.getTop() < 0){
            mCalendarView.offsetTopAndBottom(-dy);
            child.offsetTopAndBottom(-dy);
            return;
        }

        int top = child.getTop();
        if (top<mCalendarView.getBottom()){
            child.offsetTopAndBottom(-dy);
        }


    }



    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target) {
        RecycleViewCalendar calendarView = (RecycleViewCalendar) coordinatorLayout.getChildAt(0);
        int top = child.getTop();
        int height = calendarView.getMeasuredHeight();
        if (top > height/2){
            calendarView.open();
            child.offsetTopAndBottom(height - top);
        }else {
            calendarView.collapse();
            // child 距离顶部的距离等于选中view的高度
            child.offsetTopAndBottom(-(top - mCalendarView.getMinTop()));
        }


//        calendarView.onStateChange(top>height/2);

    }


    //    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        System.out.println("dyConsumed:"+dyConsumed);
//        System.out.println("dyUnconsumed:"+dyUnconsumed);
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//        RecycleViewCalendar calendarView = (RecycleViewCalendar) coordinatorLayout.getChildAt(0);
//        calendarView.offsetTopAndBottom(-dyUnconsumed);
//    }
}