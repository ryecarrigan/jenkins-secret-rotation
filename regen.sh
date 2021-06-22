#!/usr/bin/env bash
if [[ -z "$CONTROLLER_URL" ]]; then
  echo "CONTROLLER_URL is not set."
  exit 1
fi

# Store the decryption keys
curl -sS \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    --data-urlencode "script=$(<get-keys.groovy)" \
    -X POST \
    "${CONTROLLER_URL}/scriptText" > keys.groovy

# Move the existing keys to backup before regenerating.
RENAMED=$(curl -sS \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    --data-urlencode "script=$(<keys-backup.groovy)" \
    -X POST \
    "${CONTROLLER_URL}/scriptText")
if [[ "$RENAMED" == "Result: true" ]]; then
  echo "Key backups created"
else
  echo "Key backups already exist"
  exit 0
fi

# Restart Jenkins to unload the old keys from memory.
curl -sS \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    -X POST \
    "${CONTROLLER_URL}/restart"
sleep 5

# Wait for Jenkins to be restored after the restart.
while ! curl -f \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    "${CONTROLLER_URL}/whoAmI" > /dev/null 2>&1
do
  echo "Controller is restarting. Waiting 15 seconds..."
  sleep 15
done
echo "Controller is online"
