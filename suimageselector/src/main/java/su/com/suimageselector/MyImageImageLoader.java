package su.com.suimageselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MyImageImageLoader {

    private static MyImageImageLoader instance;
    LruCache<String,Bitmap> lruCache;
    ExecutorService executorService;
    final int THREAD_COUNT=10;
    Handler looperHandler;
    Handler uiHandler;
    Semaphore semaphore;
    int screenWidth;

    @SuppressLint("HandlerLeak")
    private MyImageImageLoader(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth=metrics.widthPixels;
        semaphore=new Semaphore(0);
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);
        lruCache=new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight()*value.getRowBytes();
            }
        };
        executorService= Executors.newFixedThreadPool(THREAD_COUNT);
        uiHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    ImageObj imageObj = (ImageObj) msg.obj;
                    ImageView imageView = imageObj.imageView;
                    Bitmap bitmap = imageObj.bitmap;
                    imageView.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        HandlerThread handlerThread=new HandlerThread("looperThread");
        handlerThread.start();
        looperHandler =new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                Message message=uiHandler.obtainMessage();
                message.obj=msg.obj;
                uiHandler.sendMessage(message);
            }
        };
    }

    public static MyImageImageLoader getInstance(Context context){
        if(instance==null){
            synchronized (MyImageImageLoader.class){
                if(instance==null)
                    instance=new MyImageImageLoader(context);
            }
        }
        return instance;
    }

    @SuppressLint("NewApi")
    public void loadImage(final ImageView imageView, final String path, final int width,final int height){
        try {
            Bitmap bitmap = lruCache.get(path);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, options);
                        int outWidth = options.outWidth;
                        int outHeight = options.outHeight;
                        int widthRatio = outWidth / width;
                        int heightRatio = outHeight / height;
                        options.inSampleSize = Math.min(widthRatio, heightRatio);
                        options.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
                        lruCache.put(path, bitmap);
                        if(looperHandler ==null) {
                            try {
                                semaphore.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message msg= looperHandler.obtainMessage();
                        msg.obj= new ImageObj(imageView,bitmap,path);
                        looperHandler.sendMessage(msg);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class ImageObj{
        ImageView imageView;
        Bitmap bitmap;
        String path;
        public ImageObj(ImageView imageView, Bitmap bitmap, String path) {
            this.imageView = imageView;
            this.bitmap = bitmap;
            this.path = path;
        }
    }
}
