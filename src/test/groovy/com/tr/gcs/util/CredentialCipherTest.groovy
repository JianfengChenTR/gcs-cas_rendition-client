package com.tr.gcs.util

import org.apache.commons.codec.binary.Base64
import org.apache.log4j.Logger
import org.junit.jupiter.api.Test

class CredentialCipherTest
{
    private final static Logger LOG = Logger.getLogger(this.class)

    @Test
    void testEncode()
    {
        byte[] encoded = CredentialCipher.encode('tr1234'.bytes)
        String encodedString = new String(Base64.encodeBase64(encoded))
        LOG.info("encoded text: " + encodedString)
    }

    @Test
    void testDecode()
    {
        byte[] encoded = CredentialCipher.decode('nikQpKdtm64=', Base64.decodeBase64('QBOx+fk+e9I='.bytes))
        assert new String(encoded) == 'tr1234'
    }
}
