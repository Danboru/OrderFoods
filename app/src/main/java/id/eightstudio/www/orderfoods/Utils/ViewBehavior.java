package id.eightstudio.www.orderfoods.Utils;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.View;

public class ViewBehavior extends CoordinatorLayout.Behavior<CardView> {

    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, CardView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, CardView child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, CardView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    private void slideUp(CardView child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(400);
    }

    private void slideDown(CardView child) {
        child.clearAnimation();
        child.animate().translationY(height).setDuration(400);
    }

}
