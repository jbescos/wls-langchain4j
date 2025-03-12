#!/bin/bash

# Configuration Variables
ORACLE_HOME="/home/jbescos/workspace/weblogic/depot/src151100_build/Oracle_Home"
DOMAIN_HOME="/home/jbescos/workspace/weblogic/depot/src151100_build/Oracle_Home/servers/myserver"
ADMIN_SERVER_URL="t3://localhost:7001"
ADMIN_USERNAME="weblogic"
ADMIN_PASSWORD="weblogic123"
WAR_FILE="target/integration.war"
APP_NAME="integration"
target="myserver"

# Generate the WLST script
echo "
connect('${ADMIN_USERNAME}', '${ADMIN_PASSWORD}', '${ADMIN_SERVER_URL}')
deploy('${APP_NAME}', '${WAR_FILE}', targets='${target}')
disconnect()
exit()
" > deploy.py

# Run the WLST script
${ORACLE_HOME}/oracle_common/common/bin/wlst.sh deploy.py

# Clean up WLST script
rm -f deploy.py

echo "Deployment completed successfully!"