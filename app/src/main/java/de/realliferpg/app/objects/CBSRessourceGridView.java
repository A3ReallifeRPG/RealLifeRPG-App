package de.realliferpg.app.objects;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CBSRessourceGridView extends GridView {

    public CBSRessourceGridView(Context context) {
        super(context);
    }

    public CBSRessourceGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CBSRessourceGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST));
        getLayoutParams().height = getMeasuredHeight();
    }
}