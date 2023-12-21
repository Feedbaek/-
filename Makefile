NAME = DriveTestSystem

all : $(NAME)

$(NAME) :
	docker compose up --build -d

start :
	docker start mariadb
	docker start spring

stop :
	docker stop spring
	docker stop mariadb

restart: stop start

clean :
	docker compose down --remove-orphans --rmi all --volumes
	cd backend-spring && ./gradlew clean

fclean : clean
	docker network prune --force
	docker volume prune --force
	docker system prune --all --force --volumes

re : fclean all
