grammar WRB;

prog:   stat+; 

stat:   expr (NL|SEMI|EOF)									#calculation            
    |   ID nmbr? '=' expr (NL|SEMI|EOF)			    		#assignment  
    ;

expr:	func												#funct
	|	expr (('^'|'**') powtypes)+							#power
	|	expr op=(MUL|DIV) expr   							#multiplication
    |   expr op=(ADD|SUB) expr   							#addition
    |   nmbr				        						#double
    |   '(' expr ')'         								#brackets
    |   SUB (func|'(' expr ')'|nmbr)						#sub
    ;

nmbr:	(INT|DBL)											#int
	|	(INT|DBL) ('e+'|'E+') (INT|DBL)						#pos_tenspotency
	|	(INT|DBL) ('e-'|'E-') (INT|DBL)						#neg_tenspotency
	|	ID nmbr?											#id
	;	
	
powtypes:	(nmbr|'('expr')');

func:	('f\''|'F') '(' ID ',' expr (',' expr)? ')'			#diffint
	|	ID nmbr? '(' expr (',' expr)* ')'					#function
	|	ID '(' ID (',' ID)* ')' '=' expr					#function_def
	;	

ID  :   [a-zA-Z]+;    
INT :   [0-9]+;      
DBL :	[0-9]*[.]?[0-9]+;   	
WS  :   [ \t]+ -> skip; 							// skip whitespaces
NL	:	'\r'? '\n';
SEMI:	';';


MUL:	'*';
DIV:	'/';
ADD:	'+';
SUB:	'-';
POW:	'^';

PT:		'.';
