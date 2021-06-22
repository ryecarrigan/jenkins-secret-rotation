import com.cloudbees.plugins.credentials.Credentials
import com.dabsquared.gitlabjenkins.connection.GitLabApiTokenImpl
import hudson.util.Secret

Credentials update(creds) {
  /** GitLab Plugin */
  if (creds instanceof GitLabApiTokenImpl) {
    def decrypted = decrypt(creds.apiToken.plainText)
    if (decrypted != creds.apiToken.plainText) {
      return new GitLabApiTokenImpl(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }

  return creds
}
