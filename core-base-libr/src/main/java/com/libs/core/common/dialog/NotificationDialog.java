package com.libs.core.common.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.libs.core.R;
import com.libs.core.common.utils.ViewUtils;

/**
 * @author admin 2019-10-11
 */
public class NotificationDialog extends BaseDialog<NotificationDialog> {

    public NotificationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setCustomHeight() {
        return 796;
    }

    @Override
    protected void initView(Context context) {
        setCancelable(false);
        View layout = View.inflate(context, R.layout.dialog_notification, null);
        View open = layout.findViewById(R.id.dn_open_now);
        ViewUtils.setPressEffect(open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClick != null) {
                    rightClick.onClick(NotificationDialog.this);
                }
            }
        });
        View close = layout.findViewById(R.id.dn_refuse);
        ViewUtils.setPressEffect(close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftClick != null) {
                    leftClick.onClick(NotificationDialog.this);
                }
            }
        });
        addContent(layout);
//        ViewUtils
    }

    @Override
    public NotificationDialog setLeftBtn(String stringRes, @Nullable BtnClickListener<NotificationDialog> listener) {
        leftClick = listener;
        return mDialog;
    }

    @Override
    public NotificationDialog setRightBtn(int stringRes, @Nullable BtnClickListener<NotificationDialog> listener) {
        rightClick = listener;
        return mDialog;
    }
}
