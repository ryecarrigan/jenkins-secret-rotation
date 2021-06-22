import com.cloudbees.plugins.credentials.Credentials
import hudson.util.Secret
import io.fabric8.jenkins.openshiftsync.OpenShiftToken

Credentials update(creds) {
  /** OpenShift Sync Plugin */
  if (creds instanceof OpenShiftToken) {
    def decrypted = decrypt(creds.secret.plainText)
    if (decrypted != creds.secret.plainText) {
      return new OpenShiftToken(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }

  return creds
}
