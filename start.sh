#!/bin/bash -e

ARTIFACT=night-watchman
LOCAL_INSTALL_PATH=`dirname $0`
LOCAL_FOLDER=${LOCAL_INSTALL_PATH}/.${ARTIFACT}
JAR_PATH=${LOCAL_FOLDER}/${ARTIFACT}.jar

if [ ! -r ${JAR_PATH} ]; then
    echo "Unable to find ${JAR_PATH}" 1>&2
    exit 1
fi

if [ ! -f ${LOCAL_INSTALL_PATH}/application.properties ]; then
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" 1>&2
    echo "!! ${LOCAL_INSTALL_PATH}/application.properties not found. !!" 1>&2
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" 1>&2
    echo "" 1>&2
    echo "Running application with DEFAULTS. (This will probably not work)" 1>&2
    echo "" 1>&2
fi

if [ ! -f ~/.aws/credentials ]; then
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" 1>&2
    echo "!! ${HOME}/.aws/credentials not found. Please configure. !!" 1>&2
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" 1>&2
    echo "" 1>&2
    exit 2
fi

JAVA_OPTS='-server -Xms512m -Xmx512m'
echo "Executing: java -jar ${JAR_PATH}"
pushd ${LOCAL_INSTALL_PATH}
java ${JAVA_OPTS} -jar ${JAR_PATH}
popd
echo $?

