CSCI3465-Final-Exam
===================

> Final Exam (take home part) for CSCI3465 Object-Oriented Programming with Java

---

> Your work will be evaluated based on the following factors:
- Correctness: your program does what the question asks of it (50%)
- Code clarity/ease of understanding (35%)
- Formatting, class/method/field naming, whitespace, and comments
- Effective use of classes to organize data/functionality (15%) 

> This test is worth 50% of the your Final Exam mark (with the other 50% coming from the written final on the 17th) 

> State any assumptions that you make in the comments of your class code. 

---

## Problem 1 (20 pts)

### a.
Design and implement a client-server-style simple calculator program that can handle basic arithmetic.  
A key component for this program will be its **Protocol**:
a Serializable object class that will have two fields:
a **Token** (which will be an enumerated type), and an argument (stored as a String).  
The Token enum should contain a value for each of the basic arithmetic components
(operators `+`, `-`, `*`, `/`, and an `operand`),
as well as for the states `CONNECT`
(the client will send one to the server upon connection),
`READY` (the server sends one to the client when it is ready to accept a calculation),
`QUIT` (for when the client terminates its session).,
and `ERROR` (for when something odd happens).  
Your client and server applications should communicate in a synchronous manner
using Protocol objects transmitted via ObjectInputStream and ObjectOutputStream
stream objects (refer to the Magnet and ChatServer assignments for a refresher on how that works).  
Your client and server should also never assume that the other is sending what it should,
validating the Token of the Protocol object upon receipt
(if it was something other than what was expected,
  an ERROR Protocol object is sent and the connection closed).  
A sample run of the system would look like this:

| Client | Server |
| ------ | ------ |
| connect, send CONNECT |  |
|  | recv CONNECT, send READY, wait for operation |
| recv READY, send PLUS | |
|  |  recv PLUS, it’s a valid operation; wait for two operands |
| send CONST 1 | |
| send CONST 2 | |
| wait for response | |
| | recv both CONSTs, verify they are CONSTs, perform operation |
| | send CONST 2 (result of operation) |
| recv response, wait | |
| for READY before sending again | |
| | send READY, wait for operation |
| send QUIT, wait for resp | |
| | recv QUIT, send QUIT back, close connection |
| recv QUIT, terminate prog. | |


You may design the interface for your client however you like (either command-line or graphical).  
The server can listen on a port of your choice.  No RMI.  Your server doesn’t have to support multiple asynchronous connections.

#### JavaDoc Documentation

See http://glavin001.github.io/CSCI3465-Final-Exam/Problem1/doc/

#### For Grading

- [✓] Command Line Interface
- [✓] `Protocol` class is a Serializable object class that will have two fields:
- [✓] a `Token` (which will be an enumerated type), and 
- [✓] an argument (stored as a String).
- [✓] Communicates in a synchronous manner using Protocol objects transmitted via ObjectInputStream and ObjectOutputStream stream objects
- [✓] if it was something other than what was expected, an ERROR Protocol object is sent and the connection closed
- [✓] Correctness: your program does what the question asks of it (50%)
- [✓] Code clarity/ease of understanding (35%) - Cleanly commented code and generated JavaDoc documentation
- [✓] Formatting, class/method/field naming, whitespace, and comments - Whitespace has been made all spaces, conventional naming scheme, and appropriate parts are commented.
- [✓] Effective use of classes to organize data/functionality (15%)

### b.
Produce a [State UML diagram](http://en.wikipedia.org/wiki/State_diagram_%28UML%29) for your client-server application.  You can use MSWord to cobble this together, or a program of your choice.  Save it as a Word document, a WMF, a PDF, or an image file.

## Problem 2 (15 pts)

### a.
Write two classes EncryptingWriter and DecryptingReader that encrypt and decrypt the characters of the underlying reader or writer.  
Make sure that these classes extend the Writer and Reader classes,
and take a Writer/Reader object as a constructor parameter (storing it in a local Writer/Reader class variable).  
You will have to override all of the Writer/Reader methods (Google for a list),
although some of them will just require a call to the corresponding method of the underlying Writer/Reader object.  
Also, write a short driver that demonstrates that these classes function as intended
(encrypt input to a file and decrypt data from a file, for example).    

- BASIC: Implement the Caesar Cipher (each character is incremented by 3, e.g., A -> D, F -> I)   
- ENHANCED: Design your Reader/Writer in such a way that makes it possible to vary the cipher used.

#### JavaDoc Documentation

See http://glavin001.github.io/CSCI3465-Final-Exam/Problem2/doc/


### b.
Which two design patterns are at work here?
The correct answer to this question will give insight on how to properly structure your classes to complete the ENHANCED portion.
