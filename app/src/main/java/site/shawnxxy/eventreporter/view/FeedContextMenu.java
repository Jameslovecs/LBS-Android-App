package site.shawnxxy.eventreporter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;
import site.shawnxxy.eventreporter.R;
import site.shawnxxy.eventreporter.utils.Utils;

/**
 * Created by ShawnX on 10/13/17.
 */

public class FeedContextMenu extends LinearLayout {

    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(240);
    public FeedContextMenu(Context context) {
        super(context);
        init();
    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private int feedItem = -1;
    public interface OnFeedContextMenuItemClickListener {
        public void onReportClick(int feedItem);
        public void onRepostClick(int feedItem);
        public void onSharePhotoClick(int feedItem);
        public void onCopyShareUrlClick(int feedItem);
        public void onCancelClick(int feedItem);
    }
    private OnFeedContextMenuItemClickListener onItemClickListener;
    public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @OnClick(R.id.btnSharePhoto)
    public void onSharePhotoClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onSharePhotoClick(feedItem);
        }
    }
    @OnClick(R.id.btnRepost)
    public void onRepostClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onRepostClick(feedItem);
        }
    }
    @OnClick(R.id.btnReport)
    public void onReportClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onReportClick(feedItem);
        }
    }

    @OnClick(R.id.btnCopyShareUrl)
    public void onCopyShareUrlClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCopyShareUrlClick(feedItem);
        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCancelClick(feedItem);
        }
    }

    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(FeedContextMenu.this);
    }
}
