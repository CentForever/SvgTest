package jp.shts.android.trianglelabelview;

import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

class Test {

    /**
     * 获取TextView某一个字符的坐标
     *
     * @parms tv
     * @parms index 字符下标
     * @return 返回的是相对坐标
     */
    private int getTextViewSelectionBottomY(TextView tv, int index) {
        Layout layout = tv.getLayout();
        Rect bound = new Rect();
        int line = layout.getLineForOffset(index);
        layout.getLineBounds(line, bound);
        int yAxisBottom = bound.bottom;//字符底部y坐标
        // int yAxisTop = bound.top;//字符顶部y坐标
        // float xAxisLeft = layout.getPrimaryHorizontal(index);//字符左边x坐标
        // float xAxisRight = layout.getSecondaryHorizontal(index);//字符右边x坐标
        return yAxisBottom;
    }
}


