package com.hx.android.ehentai.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {

	private final static String DES = "DES";

	/**
	 * Description 嚙賢��蕭獢�蕭擗�嚙踝蕭蹎蕭
	 * 
	 * @param data
	 * @param key
	 *            嚙踐�嚙踝蕭摰描te嚙賢�嚙�
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		String strs = new String(new Base64().encode(bt));
		return strs;
	}

	/**
	 * Description 嚙賢��蕭獢�蕭擗�嚙賡嚙踝蕭
	 * 
	 * @param data
	 * @param key
	 *            嚙踐�嚙踝蕭摰描te嚙賢�嚙�
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) {
		try {
			if (data == null || "".equals(data))
				return null;
			Base64 decoder = new Base64();
			byte[] buf = decoder.decode(data.getBytes());
			byte[] bt = decrypt(buf, key.getBytes());
			return new String(bt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Description 嚙賢��蕭獢�蕭擗�嚙踝蕭蹎蕭
	 * 
	 * @param data
	 * @param key
	 *            嚙踐�嚙踝蕭摰描te嚙賢�嚙�
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 嚙踐�嚙賡�嚙質嚙質蝮�擗蕭嚙踐謓選蕭�喉蕭
		SecureRandom sr = new SecureRandom();

		// ��嚙賣�蕭嚙賭漱�迎蕭獢�蕭�望ESKeySpec�砥��
		DESKeySpec dks = new DESKeySpec(key);

		// 嚙踐����嚙質����鈭蕭���蕭���堆嚙瘩ESKeySpec�抒��蕭�cretKey�砥��
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher�砥�豯剁蕭�堆嚙踝蕭蹎蕭嚙踝�嚙�
		Cipher cipher = Cipher.getInstance(DES);

		// 嚙賢嚙踝蕭鈭蕭�迎�嚙瘠ipher�砥��
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 嚙賢��蕭獢�蕭擗�嚙賡嚙踝蕭
	 * 
	 * @param data
	 * @param key
	 *            嚙踐�嚙踝蕭摰描te嚙賢�嚙�
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 嚙踐�嚙賡�嚙質嚙質蝮�擗蕭嚙踐謓選蕭�喉蕭
		SecureRandom sr = new SecureRandom();

		// ��嚙賣�蕭嚙賭漱�迎蕭獢�蕭�望ESKeySpec�砥��
		DESKeySpec dks = new DESKeySpec(key);

		// 嚙踐����嚙質����鈭蕭���蕭���堆嚙瘩ESKeySpec�抒��蕭�cretKey�砥��
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher�砥�豯剁蕭�堆嚙賡嚙踝蕭嚙踝�嚙�
		Cipher cipher = Cipher.getInstance(DES);

		// 嚙賢嚙踝蕭鈭蕭�迎�嚙瘠ipher�砥��
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
}