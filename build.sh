docker build -t de.zaunkoenigweg/lexi4j-maven:latest src/test/docker/

docker run -it --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven de.zaunkoenigweg/lexi4j-maven:latest mvn clean test
