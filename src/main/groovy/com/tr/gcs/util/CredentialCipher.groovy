package com.tr.gcs.util

import org.apache.commons.codec.binary.Base64
import org.apache.log4j.Logger

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@SuppressWarnings("DuplicateStringLiteral")
class CredentialCipher
{
    final static Logger LOG = Logger.getLogger(this.class)

    static byte[] encode(byte[] text)
    {
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey()
        LOG.info("key: " + new String(Base64.encodeBase64(secretKey.encoded)))
        Cipher cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        cipher.doFinal(text)
    }

    static byte[] decode(String key, byte[] text)
    {
        byte[] decodedKey = Base64.decodeBase64(key.bytes)
        SecretKey secretKey = new SecretKeySpec(decodedKey, "DES")
        Cipher cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        cipher.doFinal(text)
    }
}
