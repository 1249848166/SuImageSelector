package su.com.suimageselector;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectPicturePanelActivity extends AppCompatActivity implements OnImageFolderListener {

    View parent;

    RecyclerView recyclerView;
    MyImageAdapter imageAdapter;
    MyFolderAdapter folderAdapter;
    List<String> urls;
    int span = 3;
    int space = 1;
    ProgressDialog progressDialog;
    List<MyImageFolder> images;
    List<MyImageFolder> vedios;
    MyImageFolder selectedFolder = null;

    Toolbar toolbar;
    Button selectedNum;
    TextView title;
    int buttonDisableColor = Color.parseColor("#aaaaaa");
    int buttonEnableColor = Color.parseColor("#00ff00");

    RelativeLayout bottombar;
    TextView directory;
    TextView num;

    int maxSize = 5;

    CheckBoxImageViewGroup group;

    final int MSG_SHOW_PROGRESS_DIALOG = 1;
    final int MSG_UPDATE_RECYCLERVIEW = 2;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS_DIALOG:
                    if (progressDialog != null && !progressDialog.isShowing())
                        progressDialog.show();
                    break;
                case MSG_UPDATE_RECYCLERVIEW:
                    imageAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    directory.setText(selectedFolder.getName());
                    num.setText(selectedFolder.getFileNum()+"张");
                    break;
            }
        }
    };

    public static String topBarTitle = "topBarTitle";
    public static String topBarTitleSize = "topBarTitleSize";
    public static String topBarTitleColor = "topBarTitleColor";
    public static String topBarFinishButtonEnableColor = "topBarFinishButtonEnableColor";
    public static String topBarFinishButtonDisableColor = "topBarFinishButtonDisableColor";
    public static String topBarFinishButtonTextColor = "topBarFinishButtonTextColor";
    public static String topBarFinishButtonTextSize = "topBarFinishButtonTextSize";
    public static String topBarColor = "topBarColor";
    public static String bottomBarColor = "bottomBarColor";
    public static String bottomBarLeftTextColor = "bottomBarLeftTextColor";
    public static String bottomBarRightTextColor = "bottomBarRightTextColor";
    public static String bottomBarRightTextSize = "bottomBarRightTextSize";
    public static String bottomBarLeftTextSize = "bottomBarLeftTextSize";
    public static String maxSelectNum = "maxSelectNum";
    public static String colSpan = "colSpan";
    public static String colSpace = "colSpace";
    public static String checkIconPosition = "checkIconPosition";
    public static String checkIconDrawable = "checkIconDrawable";

    IconImagePosition iconImagePosition = IconImagePosition.CENTER;
    int drawable = R.mipmap.check;

    PopupWindow popupWindow;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent=LayoutInflater.from(this).inflate(R.layout.activity_select_picture_panel,null);
        setContentView(parent);
        try {
            //接受自定义属性
            Intent intent = getIntent();
            String topBarTitle = intent.getStringExtra("topBarTitle");
            int topBarTitleSize = intent.getIntExtra("topBarTitleSize", 0);
            String topBarTitleColor = intent.getStringExtra("topBarTitleColor");
            String topBarFinishButtonEnableColor = intent.getStringExtra("topBarFinishButtonEnableColor");
            String topBarFinishButtonDisableColor = intent.getStringExtra("topBarFinishButtonDisableColor");
            String topBarFinishButtonTextColor = intent.getStringExtra("topBarFinishButtonTextColor");
            String topBarColor = intent.getStringExtra("topBarColor");
            String bottomBarColor = intent.getStringExtra("bottomBarColor");
            int topBarFinishButtonTextSize = intent.getIntExtra("topBarFinishButtonTextSize", 0);
            String bottomBarLeftTextColor = intent.getStringExtra("bottomBarLeftTextColor");
            String bottomBarRightTextColor = intent.getStringExtra("bottomBarRightTextColor");
            int bottomBarRightTextSize = intent.getIntExtra("bottomBarRightTextSize", 0);
            int bottomBarLeftTextSize = intent.getIntExtra("bottomBarLeftTextSize", 0);
            int maxSelectNum = intent.getIntExtra("maxSelectNum", 0);
            int colSpan = intent.getIntExtra("colSpan", 0);
            int colSpace = intent.getIntExtra("colSpace", 0);
            int iconDrawable = intent.getIntExtra("checkIconDrawable", 0);
            //0：中心，1：左，2：上，3：右，4：下，5：左上，6：右上，7：右下，8：左下
            String checkIconPosition = intent.getStringExtra("checkIconPosition");
            if (checkIconPosition != null) {
                iconImagePosition = IconImagePosition.valueOf(checkIconPosition);
            }
            if (iconDrawable != 0) {
                drawable = iconDrawable;
            }

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (topBarColor != null) {
                toolbar.setBackgroundColor(Color.parseColor(topBarColor));
            }
            title = toolbar.findViewById(R.id.title);
            if (topBarTitle != null) {
                title.setText(topBarTitle);
            }
            if (topBarTitleSize != 0) {
                title.setTextSize(topBarTitleSize);
            }
            if (topBarTitleColor != null) {
                title.setTextColor(Color.parseColor(topBarTitleColor));
            }
            selectedNum = toolbar.findViewById(R.id.selectedNum);
            if (topBarFinishButtonDisableColor != null) {
                selectedNum.setBackgroundColor(Color.parseColor(topBarFinishButtonDisableColor));
                buttonDisableColor = Color.parseColor(topBarFinishButtonDisableColor);
            }
            if (topBarFinishButtonEnableColor != null) {
                buttonEnableColor = Color.parseColor(topBarFinishButtonEnableColor);
            }
            if (topBarFinishButtonTextColor != null) {
                selectedNum.setTextColor(Color.parseColor(topBarFinishButtonTextColor));
            }
            if (topBarFinishButtonTextSize != 0) {
                selectedNum.setTextSize(topBarFinishButtonTextSize);
            }
            selectedNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //完成选择
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectPicturePanelActivity.this);
                    AlertDialog askDialog = builder.create();
                    askDialog.setMessage("是否完成选择？");
                    askDialog.setCancelable(false);
                    askDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    askDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("paths", (ArrayList<String>) group.getCheckedItems());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    askDialog.show();
                }
            });

            bottombar = findViewById(R.id.bottombar);
            if (bottomBarColor != null) {
                bottombar.setBackgroundColor(Color.parseColor(bottomBarColor));
            }
            directory = findViewById(R.id.directory);
            if (bottomBarLeftTextColor != null) {
                directory.setTextColor(Color.parseColor(bottomBarLeftTextColor));
            }
            if (bottomBarLeftTextSize != 0) {
                directory.setTextSize(bottomBarLeftTextSize);
            }
            num = findViewById(R.id.num);
            if (bottomBarRightTextColor != null) {
                num.setTextColor(Color.parseColor(bottomBarRightTextColor));
            }
            if (bottomBarRightTextSize != 0) {
                num.setTextSize(bottomBarRightTextSize);
            }

            recyclerView = findViewById(R.id.recycler);
            urls = new ArrayList<>();
            if (maxSelectNum != 0) {
                maxSize = maxSelectNum;
            }
            group = new CheckBoxImageViewGroup(new ArrayList<String>(), maxSize, new ImageCheckCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onCheckResult(CheckBoxImageViewGroup group, CheckBoxImageView item) {
                    selectedNum.setText(group.size() + "/" + group.getMaxSize());
                    if (group.size() >= 1) {
                        selectedNum.setEnabled(true);
                        selectedNum.setBackgroundResource(R.drawable.round_corner_green);
                    } else {
                        selectedNum.setEnabled(false);
                        selectedNum.setBackgroundResource(R.drawable.round_corner_gray);
                    }
                    if (group.size() >= group.getMaxSize()) {
                        Toast.makeText(SelectPicturePanelActivity.this, "最多选择" + group.getMaxSize() + "张图片", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (colSpan != 0) {
                span = colSpan;
            }
            if (colSpace != 0) {
                space = colSpace;
            }
            imageAdapter = new MyImageAdapter(urls, SelectPicturePanelActivity.this, span, space, group, iconImagePosition, drawable);
            RecyclerView.LayoutManager manager = new GridLayoutManager(SelectPicturePanelActivity.this, span);
            recyclerView.setLayoutManager(manager);
            MyImageDecoration decoration = new MyImageDecoration(MyImageDecoration.Decoration.Grid, span, space);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(imageAdapter);

            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("正在加载...");

            selectedFolder=new MyImageFolder();
            selectedFolder.setName("未选择相册");
            selectedFolder.setPath("");

            new Thread() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    message.what = MSG_SHOW_PROGRESS_DIALOG;
                    handler.sendMessage(message);
                    try {
                        initFolders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        setFileImages(selectedFolder.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            DisplayMetrics metrics = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            View content = LayoutInflater.from(this).inflate(R.layout.image_layout_list, null);
            popupWindow = new PopupWindow(content, width, (int) (height * 0.8));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_popupwindow));
            popupWindow.setAnimationStyle(R.style.PopupWindowAnim);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    try {
                        backgroundAlpha(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            RecyclerView list = content.findViewById(R.id.list);
            folderAdapter = new MyFolderAdapter(images, this, this);
            list.setAdapter(folderAdapter);
            RecyclerView.LayoutManager linearmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            list.setLayoutManager(linearmanager);
            MyImageDecoration lineardecoration = new MyImageDecoration(MyImageDecoration.Decoration.Linear, 1, 2);
            list.addItemDecoration(lineardecoration);

            bottombar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        showPopupWindow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initFolders(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        initImageFolders();
    }

    void initImageFolders(){
         images = new ArrayList<>();
         Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
         ContentResolver cr = SelectPicturePanelActivity.this.getContentResolver();
         Cursor cs = cr.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                 + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/png", "image/jpeg"}, MediaStore.Images.Media.DATE_MODIFIED);
         Set<String> loopSet = new HashSet<>();
         assert cs != null;
         while (cs.moveToNext()) {
             String path = cs.getString(cs.getColumnIndex(MediaStore.Images.Media.DATA));
             File parentFile = new File(path).getParentFile();
             if (parentFile == null) {
                 continue;
             }
             String dirPath = parentFile.getAbsolutePath();
             if (loopSet.contains(dirPath)) {
                 continue;
             }
             loopSet.add(dirPath);
             MyImageFolder folder = new MyImageFolder();
             folder.setName(dirPath.substring(dirPath.lastIndexOf("/"), dirPath.length()));
             folder.setPath(dirPath);
             folder.setFirstImagePath(path);
             if (parentFile.list() == null) {
                 continue;
             }
             int num = parentFile.list(new FilenameFilter() {
                 @Override
                 public boolean accept(File file, String s) {
                     if (s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png")) {
                         return true;
                     }
                     return false;
                 }
             }).length;
             folder.setFileNum(num);
             images.add(folder);
         }
         cs.close();
     }

    void setFileImages(String directory){
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    if (s.endsWith(".jpeg") || s.endsWith(".jpg") || s.endsWith(".png")) {
                        return true;
                    }
                    return false;
                }
            });
            urls.clear();
            for (File f : files) {
                urls.add(f.getAbsolutePath());
            }
        }
        Message msg = handler.obtainMessage();
        msg.what = MSG_UPDATE_RECYCLERVIEW;
        handler.sendMessage(msg);
    }

    void showPopupWindow(){
        try {
            if (!popupWindow.isShowing()) {
                backgroundAlpha(0.3f);
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            } else {
                popupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else
            super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onFolderSelect(MyImageFolder folder) throws Exception {
        setFileImages(folder.getPath());
        selectedFolder=folder;
        popupWindow.dismiss();
        directory.setText(folder.getName());
        num.setText(folder.getFileNum()+"张");
    }

    void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
