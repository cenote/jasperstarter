#!/bin/bash
# upload release artifacts to sourceforge 

ssh-keyscan -t rsa ${SF_HOST} >>  ~/.ssh/known_hosts
(umask 077; echo $SF_SSH_BASE64 | base64 --decode > ~/.ssh/id_rsa)

# creating the release directory name 
# #[[ "${BITBUCKET_TAG}" =~ .*-([0-9]+.[0-9]+) ]]
# #RELEASE_DIR=${BASH_REMATCH[0]}
#RELEASE_DIR=test
RELEASE_DIR=$(expr "${BITBUCKET_TAG}" : '\(.*-[0-9]*.[0-9]*\)')
echo "upload relese to dir: $RELEASE_DIR"
cd target
mkdir $RELEASE_DIR
scp -r $RELEASE_DIR ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/snapshots/

# uploading...
scp *.zip ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/snapshots/$RELEASE_DIR
