#!/bin/bash
# upload release artifacts to sourceforge 

ssh-keyscan -t rsa ${SF_HOST} >>  ~/.ssh/known_hosts
(umask 077; echo $SF_SSH_BASE64 | base64 --decode > ~/.ssh/id_rsa)

# creating the release directory name 
# #[[ "${BITBUCKET_TAG}" =~ .*-([0-9]+.[0-9]+) ]]
# #RELEASE_DIR=${BASH_REMATCH[0]}
#RELEASE_DIR=test
RELEASE_DIR=$(expr "${BITBUCKET_TAG}" : '\([^0-9]*-[0-9]*.[0-9]*\)')
echo "upload relese to dir: $RELEASE_DIR"
cd target
mkdir $RELEASE_DIR
scp -r $RELEASE_DIR ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/

# uploading...
scp jasperstarter-*.zip ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/$RELEASE_DIR
scp jasperstarter-*.bz2 ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/$RELEASE_DIR
#scp jasperstarter-*.exe ${SF_USER}@${SF_HOST}:${SF_FILEBASE}/$RELEASE_DIR
