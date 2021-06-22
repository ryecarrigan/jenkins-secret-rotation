import com.cloudbees.plugins.credentials.Credentials
import com.elasticbox.jenkins.k8s.plugin.auth.TokenCredentialsImpl
import hudson.util.Secret

Credentials update(creds) {
  /** Kubernetes CI plugin */
  if (creds instanceof TokenCredentialsImpl) {
    def decrypted = decrypt(creds.token.plainText)
    if (decrypted != creds.token.plainText) {
      return new TokenCredentialsImpl(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }

  return creds
}
