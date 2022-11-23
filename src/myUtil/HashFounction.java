package myUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashFounction {
    public static MessageDigest H_256;

    static {
        try {
            H_256 = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest H_384;

    static {
        try {
            H_384 = MessageDigest.getInstance("sha-384");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest H_512;

    static {
        try {
            H_512 = MessageDigest.getInstance("sha-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * use SHA256 for HMAC
     * @param encryptText   plaintext
     * @param encryptKey    secret key
     * @return  hmac
     * @throws Exception
     */
    public static byte[] HmacSHA256Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data=encryptKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(StandardCharsets.UTF_8);
        return mac.doFinal(text);
    }

    public static byte[] HmacSHA256Encrypt(byte[] encryptTextByte, String encryptKey) throws Exception {
        byte[] data=encryptKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        return mac.doFinal(encryptTextByte);
    }

    /**
     * create string array for secret key
     * @param keylength the size of key array
     * @return  string array for secret key
     */
    public static String[] CreateSecretKey(int keylength) {
        String[] keylist = new String[keylength];

        Random random = new Random();
        int length = 1024;//key length
        for (int j = 0; j < keylist.length; j++) {
            StringBuilder bigstring = new StringBuilder();
            for (int i = 0; i < length; i++) {
                bigstring.append(random.nextInt(10));
            }
            keylist[j] = bigstring.toString();
        }
        return keylist;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String strHex = Integer.toHexString(aByte);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0").append(strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (byte b : byteArray) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
}
