package com.da.qlnhahang.utils;

public class OrderStatus {
    public static String getStatus(int status) {
        switch (status) {
            case -1:
                return "Đã từ chối";
            case 0:
                return "Đã đặt";
            case 1:
                return "Đang chế biến";
            case 2:
                return "Đã chế biến";
            default:
                return "Đã lên bàn";
        }
    }
}
