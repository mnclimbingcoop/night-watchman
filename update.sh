#!/bin/bash -e

LOCAL_INSTALL_PATH=`dirname $0`

ARTIFACT=night-watchman
GROUP=com.mnclimbingcoop

DL_URL=https://bintray.com/artifact/download/mnclimbingcoop/maven
URL=https://bintray.com/mnclimbingcoop/maven/lakitu/_latestVersion
LATEST_VERSION=`curl -is $URL |grep Location: |sed -e 's#.*https://bintray.com/##' |cut -d \/ -f 4`

if [ "$LATEST_VERSION" == "" ]; then
    echo "Error fetching latest version!"
    exit 1
else

    VERSION=${LATEST_VERSION}

    GROUP_PATH=${GROUP/\./\/}
    JAR=${ARTIFACT}-${VERSION}.jar

    LOCAL_FOLDER=${LOCAL_INSTALL_PATH}/.${ARTIFACT}
    JAR_PATH=${LOCAL_FOLDER}/${JAR}

    JAR_SYMLINK=${LOCAL_FOLDER}/${ARTIFACT}.jar

    if [ ! -d ${LOCAL_FOLDER} ]; then
        mkdir -p ${LOCAL_FOLDER}
    fi

    if [ ! -r ${JAR_PATH} ]; then
        echo "Downloading latest version of ${DL_URL}/${GROUP_PATH}/${ARTIFACT}/${VERSION}/${JAR} to ${JAR_PATH}"
        curl -L ${DL_URL}/${GROUP_PATH}/${ARTIFACT}/${VERSION}/${JAR} > ${JAR_PATH}

        # If download succeeded
        if (unzip -tqq ${JAR_PATH}) ; then
            echo "Download succeded."
            pushd ${LOCAL_FOLDER}
            ln -sf ${JAR} ${ARTIFACT}.jar
            popd

            # Restart service
            echo "Restarting service..."
            sudo service ${ARTIFACT} restart
        else
            echo "Download failed. Removing ${JAR_PATH}"
            rm -f ${JAR_PATH}
        fi
    fi

    # Write the symlink if it's not there
    if [ -r ${JAR_PATH} ] && [ ! -r ${LOCAL_FOLDER}/${ARTIFACT}.jar ] ; then
        pushd ${LOCAL_FOLDER}
        ln -sf ${JAR} ${ARTIFACT}.jar
        popd
    fi
fi
