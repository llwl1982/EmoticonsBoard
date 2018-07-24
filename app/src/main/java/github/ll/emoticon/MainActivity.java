package github.ll.emoticon;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import github.ll.emoticon.activity.PackUpdateChatActivity;
import github.ll.emoticon.activity.SimpleChatActivity;
import github.ll.emoticon.activity.SimpleChatCoordinatorLayoutActivity;
import github.ll.emoticon.activity.SimpleCommentActivity;
import github.ll.emoticon.activity.SimpleTranslucentChatActivity;
import github.ll.emoticon.common.GlideImageLoader;
import github.ll.emoticon.qq.QqActivity;
import github.ll.emoticon.userdef.SimpleChatUserDefActivity;
import github.ll.emotionboard.utils.imageloader.ImageLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoader.INSTANCE.setLoader(new GlideImageLoader());

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setMessage("为了在Android M上正常解压和加载表情,需要您的授权.")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showPermissionDialog(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        }
    }

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    public static boolean checkPermission(final Activity activity, final String permission){
        if(Build.VERSION.SDK_INT >= 23) {
            int storagePermission = ActivityCompat.checkSelfPermission(activity, permission);
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void showPermissionDialog(final Activity activity,String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission},REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        ActivityCompat.requestPermissions(activity,new String[]{permission},REQUEST_CODE_ASK_PERMISSIONS);
    }

    @OnClick(R.id.btn_simple_comment) void btn_simple_comment() {
        startActivity(new Intent(MainActivity.this, SimpleCommentActivity.class));
    }

    @OnClick(R.id.btn_simple_chat_fullscreen) void btn_simple_chat_fullscreen() {
        startActivity(new Intent(MainActivity.this, SimpleChatActivity.class));
    }

    @OnClick(R.id.btn_simple_chat_translucent) void btn_simple_chat_translucent() {
        startActivity(new Intent(MainActivity.this, SimpleTranslucentChatActivity.class));
    }

    @OnClick(R.id.btn_simple_chat_coordinatorlayout) void btn_simple_chat_coordinatorlayout() {
        startActivity(new Intent(MainActivity.this, SimpleChatCoordinatorLayoutActivity.class));
    }

    @OnClick(R.id.btn_userdef_ui) void btn_userdef_ui() {
        startActivity(new Intent(MainActivity.this, SimpleChatUserDefActivity.class));
    }

    @OnClick(R.id.btn_qq) void btn_qq() {
        startActivity(new Intent(MainActivity.this, QqActivity.class));
    }

    @OnClick(R.id.btn_packs_update) void btn_packs_update() {
        startActivity(new Intent(MainActivity.this, PackUpdateChatActivity.class));
    }

//    @OnClick(R.id.btn_github) void btn_github() {
//        Uri uri = Uri.parse("http://github.com/w446108264/XhsEmoticonsKeyboard");
//        Intent it = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(it);
//    }
}
