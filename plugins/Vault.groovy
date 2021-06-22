import com.cloudbees.plugins.credentials.Credentials
import com.datapipe.jenkins.vault.credentials.VaultAppRoleCredential
import com.datapipe.jenkins.vault.credentials.VaultGithubTokenCredential
import com.datapipe.jenkins.vault.credentials.VaultTokenCredential
import hudson.util.Secret

Credentials update(creds) {
  /** HashiCorp Vault Plugin */
  if (creds instanceof VaultAppRoleCredential) {
    def decrypted = decrypt(creds.secretId.plainText)
    if (decrypted != creds.secretId.plainText) {
      return new VaultAppRoleCredential(creds.scope, creds.id, creds.description, creds.roleId, new Secret(decrypted), creds.path)
    }
  } else if (creds instanceof VaultGithubTokenCredential) {
    def decrypted = decrypt(creds.accessToken.plainText)
    if (decrypted != creds.accessToken.plainText) {
      return new VaultGithubTokenCredential(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  } else if (creds instanceof VaultTokenCredential) {
    def decrypted = decrypt(creds.token.plainText)
    if (decrypted != creds.token.plainText) {
      return new VaultTokenCredential(creds.scope, creds.id, creds.description, new Secret(decrypted))
    }
  }

  return creds
}
