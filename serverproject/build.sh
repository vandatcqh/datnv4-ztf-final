
set -e
set -x


cd authenservice
sudo ./gradlew clean build

cd ../adminservice
sudo ./gradlew clean build

cd ../userservice
sudo ./gradlew clean build

cd ../otpservice
sudo ./gradlew clean
sudo ./gradlew generateProto
sudo ./gradlew build

cd ../tokenservice
sudo ./gradlew clean
sudo ./gradlew generateProto
sudo ./gradlew build

cd ../authzmw
sudo ./gradlew clean
sudo ./gradlew generateProto
sudo ./gradlew build

cd ../relaservice
sudo ./gradlew clean build

cd ../emailservice
sudo ./gradlew clean build

sudo docker compose down -v
sudo docker compose up --build -d
