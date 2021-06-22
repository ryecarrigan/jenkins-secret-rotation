import com.cloudbees.plugins.credentials.Credentials
import hudson.util.Secret
import org.csanchez.jenkins.plugins.kubernetes.OpenShiftTokenCredentialImpl

Credentials update(creds) {
  /** Kubernetes plugin for Jenkins */
  if (creds instanceof OpenShiftTokenCredentialImpl) {
    def decrypted = decrypt(creds.secret)
    if (decrypted) {
      return new OpenShiftTokenCredentialImpl(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }

  return creds
}
