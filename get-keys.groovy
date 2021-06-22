static def fingerprint(secret) {
  secret.key.encoded.collect { byte b -> Integer.toHexString(b & 0xFF).padLeft(2, '0') }.join(':')
}

println "import groovy.transform.Field"
println "@Field String key1 = '${fingerprint(hudson.util.Secret.KEY)}'"
println "@Field String key2 = '${fingerprint(com.cloudbees.plugins.credentials.SecretBytes.KEY)}'"

return null
