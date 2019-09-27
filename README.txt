=================
RUNNING THE GAME
=================
To run the program there are two options:
make run args="<total words> <words on screen> <dictionary file>"	--> This allows user specifiable params
make run								--> This runs off default dict. and params

Example command for running with user specified params:
make run args="20 5 example_dict.txt"

=================
    COMPILING
=================
Currently there are issue with compiling, so the default 'make' command does not work.
To compile the program issue the command:
make comp

If the above fails (it should not) the following command will compile the program:
javac src/*.java -cp bin/ -d bin/

=================
   ADDITIONAL
=================
Cleaning bin folder 	--> make clean
Generating java docs 	--> make docs
Cleaning docs		--> make cleandocs