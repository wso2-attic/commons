grammar SiddhiQLGrammar;

options {
  language = Java;
  backtrack=true;
  output    = AST;
  ASTLabelType=CommonTree;
}

tokens {
  COLLECT;
  REGEX;
  HANDLERS;
  CONDITION; 
  OUT_STREAM;
  OUT_ATTRIBUTES;
  OUT_ATTRIBUTE;
  OUT_STREAM;
  SEQUENCE;
  PATTERN;
  JOIN; 
  STREAM;
  DEFINITION;
  QUERY;
  FUNCTION;
  PARAMETERS;
  ATTRIBUTE;
  IN_ATTRIBUTE;
  CONSTANT;
  ANONYMOUS;
  RETURN_QUERY;
  PATTERN_FULL;
  SEQUENCE_FULL;
  WINDOW;
}

@header {
	package org.wso2.siddhi.query.compiler;
	import java.util.LinkedList;
}

@lexer::header { 
	package org.wso2.siddhi.query.compiler;
}


executionPlan
	:(definitionStream|query) (';' (definitionStream|query))* ';'?  ->  (^(DEFINITION definitionStream))*  ( query)*
	; 
   
definitionStream 
	:'define' 'stream' streamId '(' attributeName type (',' attributeName type )* ')'  ->  ^(streamId (^(IN_ATTRIBUTE attributeName type))+)
	;

query
	:inputStream outputStream outputProjection  ->  ^(QUERY outputStream inputStream outputProjection )
	;

outputStream
	:'insert' outputType? 'into' streamId    ->   ^(OUT_STREAM streamId outputType?)
	;

outputType
	: 'expired-events' | 'current-events' | 'all-events'
	;

inputStream
	:'from' ( sequenceFullStream ->^(SEQUENCE_FULL sequenceFullStream) 
		| patternFullStream patternHandler? ->  ^(PATTERN_FULL  patternFullStream patternHandler?)
		| joinStream -> ^(JOIN joinStream) 
		| windowStream -> windowStream
		| stream  -> stream
		)
	;  
	
	 
patternFullStream
	:'(' patternStream ')' ('within' time)? ->  ^(PATTERN  patternStream  time? ) 
	|patternStream  ('within' time)?  ->  ^(PATTERN  patternStream  time? ) 
	;

patternHandler
	:  '['! common ']'!
	;
    
 
stream 
	: basicStream ('as' id)? -> ^(STREAM basicStream id?)
	;
	
windowStream 
	: basicStream'#'windowHandler ('as' id)? -> ^(STREAM basicStream windowHandler id?)
	;
	
basicStream 
	: streamId   handler+ -> ^(streamId ^( HANDLERS  handler+) )
	| streamId    -> streamId 
	|'(' returnQuery ')' handler+   ->  ^( ANONYMOUS returnQuery ^( HANDLERS  handler+) )  
	|'(' returnQuery ')'   ->  ^(ANONYMOUS returnQuery )  
	; 
	 
/**
stream 
	: streamId   handler+ ('as' id)? -> ^(streamId ^( HANDLERS  handler+) id?)
	| streamId   ('as' id)? -> ^(streamId id?)
	|'(' returnQuery ')'  handler+ ('as' id)?  ->  ^( ANONYMOUS returnQuery ^( HANDLERS  handler+) id?)  
	|'(' returnQuery ')'  ('as' id)?  ->  ^(ANONYMOUS returnQuery  id?)  
	; 
**/	
	
joinStream 
	:leftStream join rightStream ('on' condition)? ('within' time)? ->  leftStream  join rightStream condition? time? 
	|leftStream 'unidirectional' join rightStream ('on' condition)? ('within' time)? -> leftStream 'unidirectional'  join  rightStream condition? time?
	|leftStream join rightStream 'unidirectional' ('on' condition)? ('within' time)? -> leftStream  join STREAM rightStream 'unidirectional' condition? time?
	;

leftStream
    :  windowStream
    | stream
    ;

rightStream
    :  windowStream
    |  stream
    ;
 
returnQuery
	: inputStream 'return' outputProjection	->	^(RETURN_QUERY  inputStream outputProjection)
	;

patternStream
	: patternItem ( FOLLOWED_BY patternStream )?  ->   patternItem patternStream?
	| 'every' patternItem ( FOLLOWED_BY patternStream )?  ->  ^( 'every'  patternItem ) patternStream?
	| 'every' '('nonEveryPatternStream')' ( FOLLOWED_BY patternStream )? -> ^( 'every' nonEveryPatternStream )   patternStream?
	;

nonEveryPatternStream
	: patternItem  ( FOLLOWED_BY nonEveryPatternStream )?  ->  patternItem nonEveryPatternStream?
	;

sequenceFullStream
	:sequenceStream ('within' time)? ->  ^(SEQUENCE  sequenceStream time? ) 
	;
	
sequenceStream
	: sequenceItem ',' sequenceItem  (',' sequenceItem )*   ->  sequenceItem+
	; 

FOLLOWED_BY
	: '->'/*|'-['countEnd']>'*/
	;
	
patternItem
	: itemStream 'and'^ itemStream
	| itemStream 'or'^ itemStream
	| itemStream '<'collect '>' -> ^(COLLECT itemStream collect)
	| itemStream
	;

sequenceItem
	: itemStream 'or'^ itemStream
	| itemStream regex -> ^(REGEX itemStream regex)
	| itemStream
	;

itemStream
	: attributeName'='basicStream  ->   ^(STREAM basicStream attributeName?)
	;

regex
	: ('*'|'+'|'?') '?'?
	;

outputProjection
	: externalCall? outputAttributeList groupBy? having? ->  externalCall? ^(OUT_ATTRIBUTES outputAttributeList ) groupBy? having?
	;

outputAttributeList
	:'*'
	| outputItem (',' outputItem)* ->( ^(OUT_ATTRIBUTE outputItem))+
	|-> '*'
	;

outputItem
	: outFuction 'as' id ->  outFuction id
	| constant  'as' id  ->   constant id
	|attributeVariable (-> attributeVariable|'as' id  ->   attributeVariable id)
	;


outFuction
	: ID '(' parameters? ')' -> ^( FUNCTION ID parameters?)
	;

groupBy
	: 'group' 'by' attributeVariable (',' attributeVariable)*  ->   ^('group' attributeVariable+)
	;

having
	: 'having' condition  -> ^('having' condition)
	;

externalCall
	: 'call' ID '(' parameters? ')'  ->  ^( 'call' ^(ID parameters?))
	;

handler
	: '['! (condition  |common  ) ']'!
	;

windowHandler
	: 'window' '.' id  ('(' parameters? ')')?  ->   ^( WINDOW id parameters?)
	;
	
common
	: handlerType '.' id  ('(' parameters? ')')?  ->   ^(handlerType id parameters?)
	;

parameters
	: parameter (',' parameter)*  ->  ^(PARAMETERS parameter+)
	;

time
	: constant
	;
	
parameter
	: modExpression
	;

collect
	: countStart ':' countEnd 
	| countStart ':' 
	| ':' countEnd 
	| countStartAndEnd 
	;

countStart :POSITIVE_INT;

countEnd :POSITIVE_INT;

countStartAndEnd :POSITIVE_INT;

//conditions start

condition 
	:conditionExpression  -> ^(CONDITION conditionExpression)
	;
    
conditionExpression
   	: andCondition ('or'^ conditionExpression )?
	;
	
andCondition
	: compareCondition ('and'^ conditionExpression)?
	;
	
compareCondition
	:modExpression compareOperation^ modExpression
	|boolVal
    |'('conditionExpression ')' -> conditionExpression
    |notCondition
	;
	
modExpression
   	:divisionExpression ('%'^ modExpression)?
   	;
    
divisionExpression
   	:multiplyExpression ('/'^ modExpression)?
   	;
    
multiplyExpression
   	:minusExpression ('*'^ modExpression)?
   	;
    
minusExpression
   	:sumExpression ('-'^ modExpression)?
   	;
    
sumExpression
    :valueExpression ('+'^ modExpression)?
    ;
    
valueExpression
    :constant  | attributeVariable
    ;
    
notCondition
	:'not' '('conditionExpression')' ->  ^('not' conditionExpression) 
	;
	


//conditions end 

constant
	:intVal -> ^( CONSTANT intVal) 
	|longVal -> ^( CONSTANT longVal)
	|floatVal  -> ^( CONSTANT floatVal)
	|doubleVal -> ^( CONSTANT doubleVal)
	|boolVal -> ^( CONSTANT boolVal)
	|stringVal -> ^( CONSTANT stringVal)
	;
	  
streamId: id;

attributeVariable
	:streamPositionAttributeName|streamAttributeName|attributeName;	

streamPositionAttributeName
	:streamId '['POSITIVE_INT']''.' id ->  ^( ATTRIBUTE ^(streamId POSITIVE_INT) id)
	; 	

streamAttributeName 
	: streamId '.' id  ->  ^( ATTRIBUTE streamId id)
	;
	
attributeName 
	: id  ->  ^( ATTRIBUTE id)
	;	
 
join
	: 'left''outer' 'join' ->  ^('join' ^('outer' 'left'))
	| 'right' 'outer' 'join' -> ^('join' ^('outer' 'right'))
	| 'full''outer' 'join' -> ^('join' ^('outer' 'full'))
	| 'outer' 'join'  -> ^('join' ^('outer' 'full'))
	| 'inner' 'join'  -> ^('join' 'inner')
	|  'join' -> ^('join' 'inner')
	;

compareOperation
	:'==' |'!=' |'<='|'>=' |'<' |'>'  |'contains'
	;
	
id: ID|ID_QUOTES ;
	
intVal: POSITIVE_INT| NEGATIVE_INT;

longVal: LONG_VAL; 

floatVal: FLOAT_VAL;

doubleVal: DOUBLE_VAL;

boolVal: BOOL_VAL;

stringVal: STRING_VAL;

type: 'string' |'int' |'long' |'float' |'double' |'bool'; 

handlerType: 'filter';

POSITIVE_INT:  NUM('I'|'i')?;

NEGATIVE_INT: '-' NUM('I'|'i')?;

LONG_VAL: '-'? NUM ('L'|'l');

FLOAT_VAL: '-'? NUM ('.' NUM)? NUM_SCI? ('F'|'f');

DOUBLE_VAL : '-'? NUM ('.' NUM NUM_SCI? ('D'|'d')?| NUM_SCI? ('D'|'d'));

BOOL_VAL: ('true'|'false');

//Need to be in the top to get high priority
ID_QUOTES : '`'('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*'`' {setText(getText().substring(1, getText().length()-1));};

ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

//('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|','|' '|'\t')* 

STRING_VAL
	:'\'' ( ~('\u0000'..'\u001f' | '\\' | '\''| '\"' ) )* '\'' {setText(getText().substring(1, getText().length()-1));}
	|'"' ( ~('\u0000'..'\u001f' | '\\'  |'\"') )* '"'          {setText(getText().substring(1, getText().length()-1));}
	;	

fragment NUM: '0'..'9'+;

fragment NUM_SCI: ('e'|'E') '-'? NUM;

//Hidden channels 

WS  : (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;
COMMENT
    : '/*' .* '*/' {$channel=HIDDEN;}
    ;
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;
