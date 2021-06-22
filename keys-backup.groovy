import com.cloudbees.plugins.credentials.SecretBytes
import hudson.util.Secret
import jenkins.model.Jenkins
import jenkins.security.ConfidentialKey

def var0 = renameKey(Secret.KEY)
def var1 = renameKey(SecretBytes.KEY)

return var0 || var1

static boolean renameKey(ConfidentialKey key) {
  def oldFile = new File(Jenkins.get().getRootDir(), "secrets/${key.id}")
  def newFile = new File(Jenkins.get().getRootDir(), "secrets/${key.id}.backup")
  if (newFile.exists()) {
    println "Backup key already exists"
    return false
  }

  if (oldFile.exists()) {
    oldFile.renameTo(newFile)
    if (newFile.exists()) {
      println "Key ${key.id} renamed to ${key.id}.backup"
      return true
    }
  } else {
    println "Initial key ${key.id} does not exist"
  }

  return false
}
