#!/bin/sh
set -e

# Railway dynamically injects the PORT environment variable
PORT="${PORT:-8080}"
echo "==> Configuring Apache Tomcat HTTP connector for PORT: $PORT"

# Idempotently update connector port in Tomcat server.xml
sed -i -E "s/<Connector port=\"[0-9]+\"/<Connector port=\"$PORT\"/g" /usr/local/tomcat/conf/server.xml

echo "==> Starting Apache Tomcat 10 on port $PORT..."
exec catalina.sh run
