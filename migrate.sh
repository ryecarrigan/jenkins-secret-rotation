#!/usr/bin/env bash
if [[ -z "$CONTROLLER_URL" ]]; then
  echo "CONTROLLER_URL is not set."
  exit 1
fi

if [[ ! -f keys.groovy ]]; then
  echo "Cannot find keys.groovy; run regen.sh first!"
  exit 1
fi

# Re-encode credentials provided by default plugins
echo "Migrating credentials from Credentials, Plain Credentials, SSH Credentials, and AWS Credentials plugins"
curl -sS \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    --data-urlencode "script=$(<keys.groovy)
$(<component-iterate.groovy)
$(<builtin.groovy)
$(<component-decrypt.groovy)
" \
    -X POST \
    "${CONTROLLER_URL}/scriptText"

# Re-encode credentials provided by additional plugins
for plugin in plugins/*; do
  echo "Migrating credentials defined in: $plugin"
  curl -sS \
    -u "${JENKINS_USER_ID}:${JENKINS_API_TOKEN}" \
    --data-urlencode "script=$(<keys.groovy)
$(<component-iterate.groovy)
$(<$plugin)
$(<component-decrypt.groovy)" \
    -X POST \
    "${CONTROLLER_URL}/scriptText"
done
