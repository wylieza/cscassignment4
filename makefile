BINDIR=./bin
SRCDIR=./src
DOCDIR=./doc

all: 
	javac src/WordApp.java src/Score.java src/WordDictionary.java src/WordPanel.java src/WordRecord.java src/Tracker.java src/Animator.java src/ProcessAction.java -cp bin/ -d bin/

clean:
	rm -f ${BINDIR}/*.class

run:
	@java -cp ./bin WordApp ${args}

docs:
	javadoc  -classpath ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -rf ${DOCDIR}/*