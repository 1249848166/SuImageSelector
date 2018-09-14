package su.com.imageselectortest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import su.com.suimageselector.IconImagePosition;
import su.com.suimageselector.MyImageImageLoader;
import su.com.suimageselector.SelectPicturePanelActivity;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE=110;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        setContentView(parent);

        Intent intent=new Intent(this,SelectPicturePanelActivity.class);
        intent.putExtra(SelectPicturePanelActivity.bottomBarColor,"#ffffff");//底部横条颜色
        intent.putExtra(SelectPicturePanelActivity.bottomBarLeftTextColor,"#000000");//底部文件夹名称字体颜色
        intent.putExtra(SelectPicturePanelActivity.bottomBarLeftTextSize,18);//底部文件夹名称字体大小
        intent.putExtra(SelectPicturePanelActivity.bottomBarRightTextColor,"#000000");//底部图片数目字体颜色
        intent.putExtra(SelectPicturePanelActivity.bottomBarRightTextSize,18);//底部图片数目字体大小
        intent.putExtra(SelectPicturePanelActivity.topBarColor,"#ffffff");//顶部横条颜色
        intent.putExtra(SelectPicturePanelActivity.topBarFinishButtonDisableColor,"#aaaaaa");//完成选择按钮失效颜色
        intent.putExtra(SelectPicturePanelActivity.topBarFinishButtonEnableColor,"#00ff00");//完成选择按钮启用颜色
        intent.putExtra(SelectPicturePanelActivity.topBarFinishButtonTextColor,"#ffffff");//完成选择按钮字体颜色
        intent.putExtra(SelectPicturePanelActivity.topBarFinishButtonTextSize,18);//完成选择按钮字体大小
        intent.putExtra(SelectPicturePanelActivity.topBarTitle,"请选择图片");//顶部横条标题
        intent.putExtra(SelectPicturePanelActivity.topBarTitleColor,"#000000");//顶部横条标题字体颜色
        intent.putExtra(SelectPicturePanelActivity.topBarTitleSize,18);//顶部横条标题字体大小
        intent.putExtra(SelectPicturePanelActivity.maxSelectNum,5);//最大可选择图片数量
        intent.putExtra(SelectPicturePanelActivity.colSpan,4);//图片墙列数
        intent.putExtra(SelectPicturePanelActivity.colSpace,1);//图片墙图片间隔（像素）
        intent.putExtra(SelectPicturePanelActivity.checkIconPosition, IconImagePosition.LEFT.toString());//图片被选中后标记的位置
        startActivityForResult(intent,REQUEST_CODE);//启动选择图片界面
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//选择结果在这里获取
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK){
            List<String> paths=data.getStringArrayListExtra("paths");
            for(String path:paths){
                ImageView iv=new ImageView(MainActivity.this);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5,5,5,5);
                iv.setLayoutParams(params);
                iv.setBackgroundColor(Color.BLACK);
                MyImageImageLoader.getInstance(MainActivity.this).loadImage(iv,path,100,100);
                parent.addView(iv);
            }
        }
    }
}
