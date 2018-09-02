# SuImageSelector(一个简单的图片选择器，简单使用，不卡顿)

## 效果图
![效果图](https://github.com/1249848166/SuImageSelector/blob/master/SuImageSelector.gif "效果图") 

## 1.配置
添加jit仓库
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
添加网络引用
```java
dependencies {
	        implementation 'com.github.1249848166:SuImageSelector:1.0'
	}

```
manifest文件添加访问文件权限
```java
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
由于api23（安卓5.0）开始一些重要权限失效，所以简单的做法是将目标api设置在23以下。或者写动态分配权限的代码。
```java
targetSdkVersion 22
```
这个控件要求最低api为19，因为用到沉浸式状态栏的一些设置
```java
minSdkVersion 19
```

## 2.简单使用
在需要跳转到图片选择的地方
```java
Intent intent=new Intent(this,SelectPanelActivity.class);
startActivityForResult(intent,REQUEST_CODE);//启动选择图片界面
```
在回调中取得图片
```java
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
                MyImageLoader.getInstance(MainActivity.this).loadImage(iv,path,100,100);
                parent.addView(iv);
            }
        }
    }
```
## 2.复杂使用
有可选的属性设置。完整代码如下（也可以打开app文件夹（案例项目）查看）
```java
package su.com.trymycompile;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;
import su.com.suimageselector.IconPosition;
import su.com.suimageselector.MyImageLoader;
import su.com.suimageselector.SelectPanelActivity;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE=110;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        setContentView(parent);

        Intent intent=new Intent(this,SelectPanelActivity.class);
        intent.putExtra(SelectPanelActivity.bottomBarColor,"#ffffff");//底部横条颜色
        intent.putExtra(SelectPanelActivity.bottomBarLeftTextColor,"#000000");//底部文件夹名称字体颜色
        intent.putExtra(SelectPanelActivity.bottomBarLeftTextSize,18);//底部文件夹名称字体大小
        intent.putExtra(SelectPanelActivity.bottomBarRightTextColor,"#000000");//底部图片数目字体颜色
        intent.putExtra(SelectPanelActivity.bottomBarRightTextSize,18);//底部图片数目字体大小
        intent.putExtra(SelectPanelActivity.topBarColor,"#ffffff");//顶部横条颜色
        intent.putExtra(SelectPanelActivity.topBarFinishButtonDisableColor,"#aaaaaa");//完成选择按钮失效颜色
        intent.putExtra(SelectPanelActivity.topBarFinishButtonEnableColor,"#00ff00");//完成选择按钮启用颜色
        intent.putExtra(SelectPanelActivity.topBarFinishButtonTextColor,"#ffffff");//完成选择按钮字体颜色
        intent.putExtra(SelectPanelActivity.topBarFinishButtonTextSize,18);//完成选择按钮字体大小
        intent.putExtra(SelectPanelActivity.topBarTitle,"请选择图片");//顶部横条标题
        intent.putExtra(SelectPanelActivity.topBarTitleColor,"#000000");//顶部横条标题字体颜色
        intent.putExtra(SelectPanelActivity.topBarTitleSize,18);//顶部横条标题字体大小
        intent.putExtra(SelectPanelActivity.maxSelectNum,5);//最大可选择图片数量
        intent.putExtra(SelectPanelActivity.colSpan,4);//图片墙列数
        intent.putExtra(SelectPanelActivity.colSpace,1);//图片墙图片间隔（像素）
        intent.putExtra(SelectPanelActivity.checkIconPosition, IconPosition.LEFT.toString());//图片被选中后标记的位置
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
                MyImageLoader.getInstance(MainActivity.this).loadImage(iv,path,100,100);
                parent.addView(iv);
            }
        }
    }
}

```
