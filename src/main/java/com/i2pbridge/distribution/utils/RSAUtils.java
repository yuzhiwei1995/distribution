package com.i2pbridge.distribution.utils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.i2pbridge.distribution.utils.RSATool.MdigestSHA;

public class RSAUtils {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    static {
        // 生成RSA公钥和私钥
//        genKeyPair();
        keyMap.put(0, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHs+FRK+bJnFNT4ZdTGbOD6YKIbRzPtLnEtdiDkeWnqsDhKWf0szhoiJQiAPgsgV9/2IpPTZamtJI4MxdFb/3QtWO5rDTsyNgjVpElbGHAZkOr0feklrPvA3BgVnnzs6EYHviLgKyVRlWg3WAjb45R35FB02F9gKKbCiCbtf6JnQIDAQAB");
        keyMap.put(1, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIez4VEr5smcU1Phl1MZs4PpgohtHM+0ucS12IOR5aeqwOEpZ/SzOGiIlCIA+CyBX3/Yik9Nlqa0kjgzF0Vv/dC1Y7msNOzI2CNWkSVsYcBmQ6vR96SWs+8DcGBWefOzoRge+IuArJVGVaDdYCNvjlHfkUHTYX2AopsKIJu1/omdAgMBAAECgYAMctfsllZpkEmknEFBgk4Rf85VrYzDERUpknT2POAQbX2cKiw7KmW2UTElnWkmzAdytu6ZL8juKgpv1WABIrDp3kdI/KPC4kgI1IfCcM6+MUdZmbfTJV+n+lYse+y4sErMbsVASqBFY2R6Dcj+LCY8kSEmqnGHaVdoXgES/zLwAQJBALwEOEHsTV7XvFaQkWa4VXXN7YBRccO5yXy6jFCp9CgKCUwEjBcEa1aCwaZLuIPXPwsPu54w42ILfPo2/xgPjp0CQQC4xTiwbRe/YYaoFsOwcskg7+o3su+qJl+xXteCnDFoWrtMJ05HZbxud4P+knXxfSHYhUe3I+s+JqEnL5K59HcBAkBvDbcFCURx/s+Z+e5IVBz2ZlogbvrZdwWSDMYQZzJ16+et5QmyFsQn5zfmt9cW2K6+I97YA1xrbwm06YMnfRjpAkBzrsc3bpYXtK/iPZ53suZKQOubw+RZ23y+SGsV00T72RK7M0DHYuL/JsT1di1x7K5sKcYk29BFMVpkdlyfIHgBAkEAkLrMnbvP+tZRfTg+IQIh3mXNzpI+KKsRBxdlMPIxf0Tpf9MaH/Nmdp6uiTJVfZlfuajO4v4044gtWSdny46lEw==");

//        System.out.println("生成的公钥为:" + keyMap.get(0));
//        System.out.println("生成的私钥为:" + keyMap.get(1));
    }

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        // genKeyPair();
        //加密字符串
        String message = "df723820";
//        System.out.println("随机生成的公钥为:" + keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        byte[] source = MdigestSHA(message);
        String messageEn = sign(message);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        System.out.println("长度为：" + messageEn.length());
        System.out.println(vertify(messageEn, message));
//        System.out.println("还原后的字符串为:" + messageDe);
    }


    public static void genKeyPair() {
        KeyPair newKeyPair = creatmyKey();
        if (newKeyPair == null) {
            return;
        }
        if (newKeyPair != null) {
            PrivateKey priv = newKeyPair.getPrivate();
//            byte[] b_priv = priv.getEncoded();
            PublicKey pub = newKeyPair.getPublic();
//            byte[] b_pub = pub.getEncoded();
            String publicKeyString = new String(Base64.encodeBase64(pub.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((priv.getEncoded())));
            // 将公钥和私钥保存到Map
            keyMap.put(0,publicKeyString);  //0表示公钥
            keyMap.put(1,privateKeyString);  //1表示私钥
//            re[0] = b_priv;
//            re[1] = b_pub;
        }
    }

    public static KeyPair creatmyKey() {
        KeyPair myPair;
        long mySeed;
        mySeed = System.currentTimeMillis();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();
        } catch (Exception e1) {
            return null;
        }
        return myPair;
    }

    /**
     * RSA签名
     * @param str 明文
     * @return 签名后的字符串
     * @throws Exception 签名过程中的异常信息
     */
    public static String sign( String str) throws Exception{
        //base64编码的公钥
        byte[] keyInByte = Base64.decodeBase64(keyMap.get(1));

        //RSA签名
        PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyInByte));
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(privKey);
//        sig.update(str.getBytes(StandardCharsets.UTF_8));
        sig.update(Objects.requireNonNull(MdigestSHA(str)));
        return Base64.encodeBase64String(sig.sign());

    }

    /**
     * RSA公钥验证
     *
     * @param str 加密字符串
     * @param source 源字符串
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static boolean vertify(String str, String source) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的公钥
        byte[] keyInByte = Base64.decodeBase64(keyMap.get(0));

        KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
        Signature sig = Signature.getInstance("SHA1withRSA");
        X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
        PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
        sig.initVerify(pubKey);
//        sig.update(Base64.decodeBase64(source));
        sig.update(Objects.requireNonNull(MdigestSHA(source)));
        return sig.verify(inputByte);
    }

}


