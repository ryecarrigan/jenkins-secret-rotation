import com.cloudbees.plugins.credentials.CredentialsConfidentialKey
import com.cloudbees.plugins.credentials.SecretBytes
import hudson.util.Secret
import jenkins.security.CryptoConfidentialKey

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

String decrypt(Secret secret) {
  try {
    String var0 = secret.plainText
    String var1 = var0[1..var0.length() - 2].getBytes(StandardCharsets.UTF_8)
    byte[] var2 = new String(var1).decodeBase64()
    byte[] var3 = var2[9..var2.size() - 1]
    byte[] var4 = var3[0..15]
    byte[] var5 = var3[16..var3.size() - 1]
    byte[] var6 = key1.split(':').collect { Integer.parseInt(it, 16) }

    Cipher cipher = Secret.getCipher(CryptoConfidentialKey.ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(var6, 0, 16, CryptoConfidentialKey.KEY_ALGORITHM), new IvParameterSpec(var4))

    return new String(cipher.doFinal(var5), StandardCharsets.UTF_8)
  } catch (ignored) {}

  return null
}

SecretBytes decrypt(SecretBytes secret) {
  try {
    byte[] var0 = secret.plainData
    byte[] var1 = var0[0..7]
    byte[] var2 = var0[9..var0.length - 1 - (var0[8] & 0xFF)]
    byte[] var3 = this.key2.split(':').collect { Integer.parseInt(it, 16) }

    def key1 = new SecretKeySpec(var3, 0, 16, CredentialsConfidentialKey.KEY_ALG)
    def digest = MessageDigest.getInstance(CredentialsConfidentialKey.DIGEST_ALG)
    digest.update(key1.encoded)

    byte[] var4 = digest.digest(var1)
    byte[] var5 = var4[0..15]
    byte[] var6 = var4[16..31]
    def key2 = new SecretKeySpec(var5, 0, 16, CredentialsConfidentialKey.KEY_ALG)

    Cipher cipher = Secret.getCipher(CredentialsConfidentialKey.CIPHER_ALG)
    cipher.init(Cipher.DECRYPT_MODE, key2, new IvParameterSpec(var6))

    return new SecretBytes(false, cipher.doFinal(var2))
  } catch (ignored) {}

  return null
}
