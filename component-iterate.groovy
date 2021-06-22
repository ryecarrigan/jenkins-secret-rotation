import com.cloudbees.hudson.plugins.folder.Folder
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider
import com.cloudbees.plugins.credentials.CredentialsStore
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.common.IdCredentials
import jenkins.model.Jenkins

// Get the current Jenkins instance.
def jenkins = Jenkins.get()

// Review all credentials from the system credentials provider.
def systemProvider = jenkins.getExtensionList(SystemCredentialsProvider.class)
if (systemProvider) {
  def store = systemProvider.first().getStore()
  println "Checking ${store.domains.size()} domain(s) in system credentials store"
  updateStore store
}

// Review all credentials in all folders.
def folderExtension = jenkins.getExtensionList(FolderCredentialsProvider.class)
if (folderExtension) {
  def folderProvider = folderExtension.first()
  for (folder in jenkins.getAllItems(Folder.class)) {
    def store = folderProvider.getStore(folder)
    println "Checking ${store.domains.size()} domain(s) in folder store: ${folder.fullName}"
    updateStore store
  }
}

// Iterate over all domains and all credentials in a store and re-encrypt them.
void updateStore(CredentialsStore store) {
  for (domain in store.domains) {
    def credentials = store.getCredentials(domain) as List<IdCredentials>
    println "\t Checking ${credentials.size()} credentials in domain ${domain.name ?: '(global)'}"

    for (creds in credentials) {
      println "\t\tChecking ${creds.id}"
      def updated = update(creds)
      if (updated) {
        println "\t\tValue of ${creds.id} was changed, updating credentials store"
        store.updateCredentials(domain, creds, updated)
      }
    }
  }
}
