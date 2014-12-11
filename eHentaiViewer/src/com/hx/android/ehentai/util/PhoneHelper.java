package com.hx.android.ehentai.util;

import java.lang.reflect.Field;

import android.os.Build;

public final class PhoneHelper {

	private static PhoneInfo info;

	public static PhoneInfo getPhoneInfo() {

		if (info == null) {
			// 使用反射来收集设备信息.在Build类中包含各种设备信息, // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
			// //返回
			// Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
			Field[] fields = Build.class.getDeclaredFields();
			info = new PhoneInfo();
			for (Field field : fields) {
				try {
					// setAccessible(boolean flag) // 将此对象的 accessible
					// 标志设置为指示的布尔值。
					// //通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
					field.setAccessible(true);
					if ("CPU_ABI".equals(field.getName())) {
						info.setCpuAbi(field.get(null).toString());
					} else if ("CPU_ABI2".equals(field.getName())) {
						info.setCpuAbi2(field.get(null).toString());
					} else if ("DEVICE".equals(field.getName())) {
						info.setDevice(field.get(null).toString());
					} else if ("DISPLAY".equals(field.getName())) {
						info.setDisplay(field.get(null).toString());
					} else if ("FINGERPRINT".equals(field.getName())) {
						info.setFingerprint(field.get(null).toString());
					} else if ("HARDWARE".equals(field.getName())) {
						info.setHardware(field.get(null).toString());
					} else if ("HOST".equals(field.getName())) {
						info.setHost(field.get(null).toString());
					} else if ("ID".equals(field.getName())) {
						info.setId(field.get(null).toString());
					} else if ("MANUFACTURER".equals(field.getName())) {
						info.setManufacturer(field.get(null).toString());
					} else if ("MODEL".equals(field.getName())) {
						info.setPhoneModel(field.get(null).toString());
					} else if ("RADIO".equals(field.getName())) {
						info.setRadio(field.get(null).toString());
					} else if ("SERIAL".equals(field.getName())) {
						info.setSerial(field.get(null).toString());
					} else if ("PRODUCT".equals(field.getName())) {
						info.setProduct(field.get(null).toString());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return info;

	}
}
