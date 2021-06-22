import com.cloudbees.plugins.credentials.Credentials
import jenkins.plugins.openstack.compute.auth.OpenstackCredentialv2
import jenkins.plugins.openstack.compute.auth.OpenstackCredentialv3

Credentials update(creds) {
  /** OpenStack Cloud Plugin */
  if (creds instanceof OpenstackCredentialv2) {
    def decrypted = decrypt(creds.password.plainText)
    if (decrypted != creds.password.plainText) {
      return new OpenstackCredentialv2(creds.scope, creds.id, creds.description, creds.tenant, creds.username, decrypted)
    }
  }
  else if (creds instanceof OpenstackCredentialv3) {
    def decrypted = decrypt(creds.password.plainText)
    if (decrypted != creds.password.plainText) {
      return new OpenstackCredentialv3(creds.scope, creds.id, creds.description, creds.userName, creds.userDomain, creds.projectName, creds.projectDomain, decrypted)
    }
  }

  return creds
}
