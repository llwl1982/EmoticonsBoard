package github.ll.emoticon.common.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.ll.emoticon.R;
import github.ll.emoticon.common.SimpleCommonUtils;
import github.ll.emoticon.common.data.ImMsgBean;
import github.ll.emotionboard.utils.imageloader.ImageLoader;

public class ChattingListAdapter extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 8;
    private final int VIEW_TYPE_LEFT_TEXT = 0;
    private final int VIEW_TYPE_LEFT_IMAGE = 1;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ImMsgBean> mData;

    public ChattingListAdapter(Activity activity) {
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void addData(List<ImMsgBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        for (ImMsgBean bean : data) {
            addData(bean, false, false);
        }
        this.notifyDataSetChanged();
    }

    public void addData(ImMsgBean bean, boolean isNotifyDataSetChanged, boolean isFromHead) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }

        if (isFromHead) {
            mData.add(0, bean);
        } else {
            mData.add(bean);
        }

        if (isNotifyDataSetChanged) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) == null) {
            return -1;
        }
        return mData.get(position).getMsgType() == ImMsgBean.CHAT_MSGTYPE_TEXT ? VIEW_TYPE_LEFT_TEXT : VIEW_TYPE_LEFT_IMAGE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImMsgBean bean = mData.get(position);
        int type = getItemViewType(position);

        View holderView;
        switch (type) {
            case VIEW_TYPE_LEFT_TEXT:
                ViewHolderLeftText holder;
                if (convertView == null) {
                    holder = new ViewHolderLeftText();
                    holderView = mInflater.inflate(R.layout.listitem_cha_left_text, null);
                    holderView.setFocusable(true);
                    holder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    holder.tv_content = (TextView) holderView.findViewById(R.id.tv_content);
                    holderView.setTag(holder);
                    convertView = holderView;
                } else {
                    holder = (ViewHolderLeftText) convertView.getTag();
                }

                disPlayLeftTextView(holder, bean);
                break;

            case VIEW_TYPE_LEFT_IMAGE:
                ViewHolderLeftImage imageHolder;
                if (convertView == null) {
                    imageHolder = new ViewHolderLeftImage();
                    holderView = mInflater.inflate(R.layout.listitem_chat_left_image, null);
                    holderView.setFocusable(true);
                    imageHolder.iv_avatar = (ImageView) holderView.findViewById(R.id.iv_avatar);
                    imageHolder.iv_image = (ImageView) holderView.findViewById(R.id.iv_image);
                    holderView.setTag(imageHolder);
                    convertView = holderView;
                } else {
                    imageHolder = (ViewHolderLeftImage) convertView.getTag();
                }

                disPlayLeftImageView(imageHolder, bean);
                break;

            default:
                convertView = new View(mActivity);
                break;
        }
        return convertView;
    }

    public void disPlayLeftTextView(ViewHolderLeftText holder, ImMsgBean bean) {
        setContent(holder.tv_content, bean.getContent());
    }

    public void setContent(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }

    public void disPlayLeftImageView(ViewHolderLeftImage holder, ImMsgBean bean) {
        ImageLoader.INSTANCE.displayImage(bean.getImage(), holder.iv_image);
    }

    public final class ViewHolderLeftText {
        public ImageView iv_avatar;
        public TextView tv_content;
    }

    public final class ViewHolderLeftImage {
        public ImageView iv_avatar;
        public ImageView iv_image;
    }
}