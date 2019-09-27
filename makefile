BINDIR=./bin
SRCDIR=./src
DOCDIR=./doc


.SUFFIXES: .java .class

${BINDIR}/$*.class: ${SRCDIR}/$*.java
	javac $< -cp ${BINDIR} -d ${BINDIR} 

all: javac ${SRCDIR}/*.java -cp ${BINDIR}  -d ${BINDIR} 

${BINDIR}/WordApp.class: ${BINDIR}/Score.class ${BINDIR}/WordDictionary.class ${BINDIR}/WordPanel.class ${BINDIR}/WordRecord.class ${BINDIR}/Tracker.class ${BINDIR}/Animator.class

comp:
	javac src/WordApp.java src/Score.java src/WordDictionary.java src/WordPanel.java src/WordRecord.java src/Tracker.java src/Animator.java src/ProcessAction.java -cp bin/ -d bin/

clean:
	rm -f ${BINDIR}/*.class

run:
	@java -cp ./bin WordApp ${args}

docs:
	javadoc  -classpath ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -rf ${DOCDIR}/*