package org.louiswilliams.getlost.getlost.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Louis on 6/16/2014.
 */
public class CompassView extends View{

    private static final int RADIUS = 50;
    private static final int SCALE = 50;
    private Point center;
    private Path path = new Path();
    private Paint paint = new Paint();
    private float rotate = 0.0f;

    public CompassView(Context context) {
        super(context);
        init();
    }

    public CompassView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void init() {
        paint.setColor(Color.RED);
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    @Override
    public void onDraw(Canvas canvas) {
        center = new Point(this.getWidth() / 2, this.getHeight() / 2);

        canvas.rotate(rotate, center.x, center.y);

        path.moveTo(center.x - SCALE, center.y);
        path.lineTo(center.x, center.y - SCALE * 5);
        path.lineTo(center.x + SCALE, center.y);
        path.lineTo(center.x - SCALE, center.y);

        canvas.drawPath(path, paint);
    }
}
