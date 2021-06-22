import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret
import org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

Credentials update(creds) {
  /** CloudBees AWS Credentials Plugin */
  if (creds instanceof AWSCredentialsImpl) {
    def decrypted = decrypt(creds.secretKey)
    if (decrypted) {
      return new AWSCredentialsImpl(creds.scope, creds.id, creds.accessKey, decrypted, creds.description, creds.iamRoleArn, creds.iamMfaSerialNumber)
    }
  }

  /** Jenkins SSH Credentials Plugin */
  else if (creds instanceof BasicSSHUserPrivateKey) {
    if (creds.passphrase) {
      def decrypted = decrypt(creds.passphrase)
      if (decrypted) {
        return new BasicSSHUserPrivateKey(creds.scope, creds.id, creds.username, creds.privateKeySource, decrypted, creds.description)
      }
    }
  }

  /** Jenkins Credentials Plugin */
  else if (creds instanceof CertificateCredentialsImpl) {
    def decrypted = decrypt(creds.password)
    if (decrypted) {
      return new CertificateCredentialsImpl(creds.scope, creds.id, creds.description, decrypted, creds.keyStoreSource)
    }
  }
  else if (creds instanceof UsernamePasswordCredentialsImpl) {
    def decrypted = decrypt(creds.password)
    if (decrypted) {
      return new UsernamePasswordCredentialsImpl(creds.scope, creds.id, creds.description, creds.username, decrypted)
    }
  }

  /** Jenkins Plain Credentials Plugin */
  else if (creds instanceof StringCredentialsImpl) {
    def decrypted = decrypt(creds.secret)
    if (decrypted) {
      return new StringCredentialsImpl(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }
  else if (creds instanceof FileCredentialsImpl) {
    def decrypted = decrypt(creds.secretBytes)
    if (decrypted) {
      return new FileCredentialsImpl(creds.scope, creds.id, creds.description, creds.fileName, decrypted)
    }
  }

  return null
}
