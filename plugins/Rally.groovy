import com.cloudbees.plugins.credentials.Credentials
import com.jenkins.plugins.rally.credentials.RallyCredentialsImpl

Credentials update(creds) {
  /** Kubernetes plugin for Jenkins */
  if (creds instanceof RallyCredentialsImpl) {
    def decrypted = decrypt(creds.apiKey.plainText)
    if (decrypted != creds.apiKey.plainText) {
      return new RallyCredentialsImpl(creds.id, creds.name, creds.description, decrypted)
    }
  }

  return creds
}
