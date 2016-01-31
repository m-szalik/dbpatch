all:
		gradle build install
fast:
		gradle build install -x test
test:
		gradle test
clean:
		gradle clean
