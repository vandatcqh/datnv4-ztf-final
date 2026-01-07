#!/bin/bash
set -e

# Đường dẫn key SSH
SSH_KEY="/home/lap15402/.ssh/id_ed25519"  # đổi lại đúng user bạn

# Đường dẫn remote
REMOTE="root@10.40.30.233:/root/datnv4/"

# 1. Set quyền đọc cho các file
sudo chmod +r schema.sql prometheus.yml nginx.conf loki-config.yml otel-collector-config.yml docker-compose.yml

# 2. Rsync các file cấu hình (dùng -e để chỉ định SSH key)
rsync -avz -e "ssh -i $SSH_KEY -o StrictHostKeyChecking=no" schema.sql "$REMOTE"
rsync -avz -e "ssh -i $SSH_KEY -o StrictHostKeyChecking=no" prometheus.yml "$REMOTE"
rsync -avz -e "ssh -i $SSH_KEY -o StrictHostKeyChecking=no" nginx.conf loki-config.yml otel-collector-config.yml docker-compose.yml "$REMOTE"

# 3. Save image Docker
sudo docker save -o allimages.tar $(sudo docker images --format "{{.Repository}}:{{.Tag}}")
sudo chmod +r allimages.tar

# 4. Rsync image sang server
rsync -avz -e "ssh -i $SSH_KEY -o StrictHostKeyChecking=no" allimages.tar "$REMOTE"
