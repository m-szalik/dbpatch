all:
		gradle build install shadowJar
fast:
		gradle build install -x test
test:
		gradle test
clean:
		gradle clean
standalone:
		gradle clean shadowJar
