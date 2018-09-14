package su.com.suimageselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CheckBoxImageView extends ImageView {

    boolean checked=false;
    int filterColor= Color.parseColor("#aa000000");
    int drawable=R.mipmap.check;
    Bitmap bitmap;
    Paint paint;
    CheckBoxImageViewGroup group;
    IconImagePosition iconImagePosition = IconImagePosition.CENTER;

    public void judge(String path){
        if(group!=null&&group.getCheckedItems()!=null&&group.getCheckedItems().contains(path)){
            setColorFilter(filterColor);
            checked=true;
        }else{
            setColorFilter(null);
            checked=false;
        }
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        judge((String) tag);//关键，检查是否勾选过，防止复用视图优化带来的位置错乱
    }

    public CheckBoxImageView(Context context) {
        this(context,null);
    }

    public CheckBoxImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CheckBoxImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
        initBitmap();
    }

    public void check(CheckBoxImageViewGroup group, boolean checked, IconImagePosition iconImagePosition, int drawable){
        this.group=group;
        this.iconImagePosition = iconImagePosition;
        this.drawable=drawable;
        initBitmap();
        group.check(this,checked);
    }

    void initBitmap(){
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), drawable);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public CheckBoxImageViewGroup getGroup() {
        return group;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(checked) {
            setColorFilter(filterColor);
            if(iconImagePosition.equals(IconImagePosition.CENTER)){
                canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2, paint);
            }else if(iconImagePosition.equals(IconImagePosition.LEFT)){
                canvas.drawBitmap(bitmap, 0, getHeight() / 2 - bitmap.getHeight() / 2, paint);
            }else if(iconImagePosition.equals(IconImagePosition.TOP)){
                canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, 0, paint);
            }else if(iconImagePosition.equals(IconImagePosition.RIGHT)){
                canvas.drawBitmap(bitmap, getWidth() - bitmap.getWidth(), getHeight() / 2 - bitmap.getHeight() / 2, paint);
            }else if(iconImagePosition.equals(IconImagePosition.BOTTOM)){
                canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getHeight() - bitmap.getHeight(), paint);
            }else if(iconImagePosition.equals(IconImagePosition.LEFT_TOP)){
                canvas.drawBitmap(bitmap, 0,0, paint);
            }else if(iconImagePosition.equals(IconImagePosition.RIGHT_TOP)){
                canvas.drawBitmap(bitmap, getWidth() - bitmap.getWidth(), 0, paint);
            }else if(iconImagePosition.equals(IconImagePosition.RIGHT_BOTTOM)){
                canvas.drawBitmap(bitmap, getWidth() - bitmap.getWidth(), getHeight() - bitmap.getHeight(), paint);
            }else if(iconImagePosition.equals(IconImagePosition.LEFT_BOTTOM)){
                canvas.drawBitmap(bitmap, 0, getHeight() - bitmap.getHeight(), paint);
            }
        }else{
            setColorFilter(null);
        }
    }

}
