import com.cloudbees.plugins.credentials.Credentials
import org.jenkinsci.plugins.p4.credentials.P4PasswordImpl
import org.jenkinsci.plugins.p4.credentials.P4TicketImpl

Credentials update(creds) {
  /** P4 Plugin */
  if (creds instanceof P4PasswordImpl) {
    def decrypted = decrypt(creds.password.plainText)
    if (decrypted != creds.password.plainText) {
      return new P4PasswordImpl(creds.scope, creds.id, creds.description, creds.p4port, creds.@ssl, creds.username, creds.retry.toString(), creds.timeout.toString(), decrypted)
    }
  } else if (creds instanceof P4TicketImpl) {
    return creds // P4 ticket has no Secret to process.
  }

  return creds
}
