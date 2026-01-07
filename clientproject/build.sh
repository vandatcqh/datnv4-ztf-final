sudo docker build -t datnv4-fe .

sudo docker save -o datnv4-fe.tar datnv4-fe

sudo chmod +r datnv4-fe.tar

rsync -avz datnv4-fe.tar root@10.40.30.233:/root/datnv4/
