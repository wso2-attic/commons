/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

 grammar SiddhiGrammar;

options{
    language  = Java;
    output    = AST;
     backtrack=true;
    }

tokens {
  DEFAULT_OUTPUT;
  NEW_STREAM;
  STREAM_PROP;
  COND_DEF;
  COND;
}  

@lexer::header {
       package org.wso2.siddhi.compiler;
}

@header {
       package org.wso2.siddhi.compiler;

       import org.wso2.siddhi.api.exception.SiddhiCompilationException;
}

@members {
    Stack paraphrases = new Stack();
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        String msg = super.getErrorMessage(e, tokenNames);
        if ( paraphrases.size()>0 ) {
            String paraphrase = (String)paraphrases.peek();
            msg = msg+" "+paraphrase ;
        }
        throw new SiddhiCompilationException(msg);  //passing the exception outside
        // return msg;
    }

    protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow)throws RecognitionException {
        throw e;
    }
 }

 // Alter code generation so catch-clauses get replace with
 // this action.
 @rulecatch {
    catch (RecognitionException e) {
        throw e;
    }
 }

siddhiGrammar
    @init { paraphrases.push("in Siddhi Grammar"); }
    @after { paraphrases.pop(); }
	: stream (';' stream?)*   			-> stream+
	;

stream
    : stmName ':=' streamType 		-> ^(stmName streamType )
	;
streamType
    : inputStm
	| queryStm 
	;	
inputStm
    @init { paraphrases.push("in Input Stream"); }
    @after { paraphrases.pop(); }
    : NAME '[' type ']' (',' inputStm )?		->  ^(NAME type)  inputStm?
	;
queryStm
    @init { paraphrases.push("in Query Stream"); }
    @after { paraphrases.pop(); }
    :queryOutput queryInput queryCond? 	-> queryInput  queryCond? queryOutput
	;
queryOutput
    @init { paraphrases.push("in Query output"); }
    @after { paraphrases.pop(); }
	:'select'
	(
		'*' 			->^('select' '*')
		| outputItem (',' outputItem)* 	->^('select'  outputItem+)
	)	
	;
outputItem
    :NAME '='
	(
		 aggregate			->^( NAME  aggregate )
		| defaultOut 		->^( NAME  defaultOut )
		|outAttriName		->^( NAME  outAttriName )
	) 	
	|outAttriName
                    	;
defaultOut
    :b='(' type ')' val			-> ^(DEFAULT_OUTPUT[$b,"DEFAULT_OUTPUT"]   type val)
	;

queryInput
    @init { paraphrases.push("in Query input"); }
    @after { paraphrases.pop(); }
    :'from' queryInputStm ( ','queryInputStm)*	-> ^('from' queryInputStm+)
	;	
queryInputStm
	: '('  queryStm ')' stmProp?		-> ^(NEW_STREAM queryStm  stmProp?)
	|  stmName   stmProp?			-> ^(stmName stmProp?)
	;
stmProp
    @init { paraphrases.push("in Input Stream Property definition"); }
    @after { paraphrases.pop(); }
    :'[' (stmWin( ',' stmStd)?|stmStd (',' stmWin)?) ']' 	-> ^(STREAM_PROP stmStd? stmWin?)
	;
stmWin
    : 'win' '.' winType '=' NUM 		-> ^('win' winType NUM )
	;
winType
    :'time'|'time' '.' 'batch' |'length' |'length' '.' 'batch'
	;
stmStd
 	:'std' '.'uniqueType '=' attriName		-> ^('std' uniqueType attriName )
	;
	
queryCond
    @init { paraphrases.push("in Query Condition"); }
    @after { paraphrases.pop(); }
	: whereCond? gpByCond? havingCond? -> ^(COND whereCond? gpByCond ? havingCond?)
	| 'pattern' condDefs pattern 		-> ^('pattern'  ^(COND_DEF condDefs)  ^(COND pattern) )
	| 'sequence' condDefs seq gpByCond? -> ^('sequence' ^(COND_DEF condDefs) ^(COND seq)  gpByCond?)
	;
whereCond
    @init { paraphrases.push("in Where Condition"); }
    @after { paraphrases.pop(); }
	:'where' cond			->^('where' cond)
	;
havingCond
    @init { paraphrases.push("in Having Condition"); }
    @after { paraphrases.pop(); }
	:'having' cond			->^('having' cond)
	;
gpByCond
    @init { paraphrases.push("in Group by Condition"); }
    @after { paraphrases.pop(); }
	:'group by' name=attriName			->^('group by' attriName)
	|'group by' streamAttriName			->^('group by' streamAttriName)
	;
condDefs
    @init { paraphrases.push("in Condition definition"); }
    @after { paraphrases.pop(); }
	:'[' condDef (',' condDef)* ']'		->  condDef+
	;
condDef
   	:NAME '=' cond			-> ^(NAME cond)
	;
pattern
    @init { paraphrases.push("in Pattern Condition"); }
    @after { paraphrases.pop(); }
    : 'every' '(' nonEveryPattern ')' ('->' pattern  )?              	-> ^('every' nonEveryPattern) (pattern)?
    | nonEveryPattern  ('->' pattern  )? 			                    ->  nonEveryPattern (pattern)?
    |  '!' patternItem  '->' pattern  			                         ->  ^('!' patternItem) pattern
    ;
nonEveryPattern
    :  '!' patternItem  '->' nonEveryPattern  			                        -> ^('!' patternItem) nonEveryPattern
    |   patternItem     ( '->' nonEveryPattern)?                               -> patternItem ( nonEveryPattern)?
    ;
patternItem
    :  '$'  NAME withInTime?			->^(NAME withInTime?)
	;
withInTime
 	:'[' 'within' '.' 'time' '=' NUM ']'		->^('within'  'time'  NUM )
	;
seq
    @init { paraphrases.push("in Sequence Condition"); }
    @after { paraphrases.pop(); }
    :seqItem seq?
	;
seqItem
	: '$'  NAME '*'?			->^( NAME '*'?)
	;
cond
    :condExpr
    ;
condExpr
    : andCond ('or'^ condExpr )?
	;
andCond
	: numCond  ('and'^ condExpr)?

	;
numCond
	:mathExpr (numOp^ mathExpr)?
     |'('cond ')'                              -> cond
     |notCond
	;
mathExpr
    :dExpr ('%'^ mathExpr)?
    ;
dExpr
    :mExpr ('/'^ mathExpr)?
    ;
mExpr
    :miExpr ('*'^ mathExpr)?
    ;
miExpr
    :sExpr ('-'^ mathExpr)?
    ;
sExpr
    :vExpr ('+'^ mathExpr)?
    ;
vExpr
    :attriName|streamAttriName|val|'('mathExpr')' | condAttriName
    ;
condAttriName
    : '$' (condRefName ('[' eventNum ']')? | 'this' '[' 'prev' ']' )'.' attriName
    ;
notCond
	:'not' '('cond ')'			-> ^('not' cond)
	;
stmName
 	:NAME
	;
streamAttriName
	:stmName '.' attriName			-> ^(stmName  attriName)
	;
attriName
	: NAME
	;
outAttriName
	: attriName
	| streamAttriName
	| '$' condRefName ('[' eventNum ']')? '.' attriName	-> ^('$' ^(condRefName eventNum? attriName))
	;
condRefName
	:NAME
	;
eventNum
	:NUM | 'last'
	;
aggregate
    :('avg'|'sum'|'max'|'min'|'count')  '(' outAttriName ')'
	;
val
    :unsignNum|'+' unsignNum | '-' unsignNum |'true'|'false'|string
	;
unsignNum
	:NUM('.' NUM)?
	;
num
    : unsignNum |'+' unsignNum | '-' unsignNum
	;
type
 	:'string' |'int' |'long' |'float' |'double' |'bool' |'date'
	;
uniqueType
    :'firstUnique' |'unique'
    ;
numOp
	:'==' |'!=' |'<' |'>' |'<='|'>=' |'contains'
	;
string
    :STRING
    ;


NUM
    :'0'..'9'+
	;
NAME
	: ('a'..'z'|'A'..'Z' |'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;
STRING
	:'\'' ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|','|' '|'\t')* '\''
    |'"' ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|','|' '|'\t')* '"'
	;
COMMENT
	: '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    | '/*' ( options{greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;
WS
	:( ' ' | '\t' | '\r'| '\n' )+ {$channel=HIDDEN;}
   	;
