# jenkins-secret-rotation
This project is intended to help rotate encryption keys used by `Secret` and `SecretBytes` as stored in Jenkins Credentials.

## Usage
Although this process creates backups of the initial keys, it is still recommended for you to create manual backups first.

1. Set the Jenkins URL and REST API credentials in `.env` by copying `.env.example` and providing correct values.
2. Run `./regen.sh` to back up the initial keys and restart Jenkins to unload those keys. This also stores the decryption bytes in `keys.groovy`.
3. Run `./migrate.sh` to perform the re-encryption using the decryption bytes from `keys.groovy` to decrypt the stored secrets and allow them to be re-encoded using new encryption keys.
