BINDIR=./bin
SRCDIR=./src
DOCDIR=./doc


.SUFFIXES: .java .class

${BINDIR}/%.class: ${SRCDIR}/%.java
	javac $< -cp ${BINDIR} -d ${BINDIR} 

all: ${BINDIR}/WordApp.class

${BINDIR}/WordApp.class: ${BINDIR}/Score.class ${BINDIR}/WordDictionary.class ${BINDIR}/WordPanel.class ${BINDIR}/WordRecord.class ${BINDIR}/Tracker.class ${BINDIR}/Animator.class

${BINDIR}/WordPanel.class: ${BINDIR}/WordRecord.class

clean:
	rm -f ${BINDIR}/*.class

run:
	@java -cp ./bin WordApp ${args}

docs:
	javadoc  -classpath ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -rf ${DOCDIR}/*