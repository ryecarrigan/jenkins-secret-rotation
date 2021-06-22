import com.cloudbees.plugins.credentials.Credentials
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentials

Credentials update(creds) {
  /** Docker Commons Plugin */
  if (creds instanceof DockerServerCredentials) {
    def decrypted = decrypt(creds.clientKey)
    if (decrypted != creds.clientKey) {
      return new DockerServerCredentials(creds.scope, creds.id, creds.description, decrypted, creds.clientCertificate, creds.serverCaCertificate)
    } else {
      println("${creds.id}: ${decrypted}")
    }
  }

  return creds
}
