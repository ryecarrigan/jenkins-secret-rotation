import com.cloudbees.plugins.credentials.Credentials
import com.github.kostyasha.yad.credentials.DockerRegistryAuthCredentials

Credentials update(creds) {
  /** Yet Another Docker Plugin */
  if (creds instanceof DockerRegistryAuthCredentials) {
    def decrypted = decrypt(creds.password.plainText)
    if (decrypted != creds.password.plainText) {
      return new DockerRegistryAuthCredentials(creds.scope, creds.id, creds.description, creds.username, decrypted, creds.email)
    }
  }

  return creds
}
