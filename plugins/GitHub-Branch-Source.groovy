import com.cloudbees.plugins.credentials.Credentials
import hudson.util.Secret
import org.jenkinsci.plugins.github_branch_source.GitHubAppCredentials

Credentials update(creds) {
  /** GitHub Branch Source Plugin */
  if (creds instanceof GitHubAppCredentials) {
    def decrypted = decrypt(creds.privateKey.plainText)
    if (decrypted != creds.privateKey.plainText) {
      return new GitHubAppCredentials(creds.scope, creds.id, creds.description, creds.appID, new Secret(decrypted))
    }
  }

  return creds
}
