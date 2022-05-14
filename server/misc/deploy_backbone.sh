#!/bin/bash

# Deploy new Backbone
rm -rf /tmp/backbone_backup
mkdir -p /tmp/backbone_backup
service backbone stop
cp /backbone/backbone/secret_key.txt /tmp/backbone_backup/
cp /backbone/backbone/project_id.txt /tmp/backbone_backup/
cp /backbone/backbone/notif_api_key.txt /tmp/backbone_backup/
cp /backbone/backbone/db.sqlite /tmp/backbone_backup/
rm -rf /backbone/backbone
cp -r /home/mark/Software/cs446-project/server/backbone /backbone/
cp /tmp/backbone_backup/notif_api_key.txt /backbone/backbone
cp /tmp/backbone_backup/secret_key.txt /backbone/backbone
cp /tmp/backbone_backup/project_id.txt /backbone/backbone
cp /tmp/backbone_backup/db.sqlite /backbone/backbone
service backbone start

# Deploy new docs
source /backbone/venv/bin/activate
cd /backbone/backbone
make clean html
rm -rf /backbone/docs/*
cp -r /backbone/backbone/_build/html/* /backbone/docs/

chown -R mark:backbone /backbone/
