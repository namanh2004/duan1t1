package com.example.duan1t1.adapter;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import com.example.duan1t1.R;

import java.util.Date;

public class MyNotification extends Application {
    public static final String CHANNEL_ID = "CHANNEL_NOTIFI";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sendNotifi();
    }
    private void sendNotifi() {
        Notification notification = new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setContentTitle("Thông báo")
                .setContentText("Có đơn hàng mới cần xác định")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getColor(R.color.xanhla))
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), notification);
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}
//        CHANNEL_ID: Một hằng số định danh kênh thông báo.
//
//        onCreate(): Phương thức này được gọi khi ứng dụng được khởi tạo. Trong phương thức này, lớp tạo một kênh thông báo bằng cách gọi phương thức createNotificationChannel().
//
//        createNotificationChannel(): Phương thức này được sử dụng để tạo một kênh thông báo. Nó kiểm tra phiên bản của thiết bị và tạo kênh thông báo chỉ trên các thiết bị chạy Android 8.0 (API 26) trở lên. Đối với các phiên bản Android cũ hơn, không cần tạo kênh thông báo. Kênh thông báo này có tên, mô tả và mức độ quan trọng được định nghĩa bởi các hằng số và được đăng ký với hệ thống thông qua NotificationManager.
//
//        onTerminate(): Phương thức này được gọi khi ứng dụng kết thúc. Trong phương thức này, lớp gửi một thông báo để thông báo rằng có một đơn hàng mới cần được xác định.
//
//        sendNotifi(): Phương thức này được sử dụng để gửi một thông báo. Nó tạo ra một đối tượng NotificationCompat.Builder và thiết lập các thuộc tính của thông báo như tiêu đề, nội dung, biểu tượng và màu sắc. Sau đó, thông báo được xây dựng từ builder và gửi thông qua NotificationManager.
//
//        getNotificationId(): Phương thức này được sử dụng để lấy một ID duy nhất cho thông báo, dựa trên thời gian hiện tại.