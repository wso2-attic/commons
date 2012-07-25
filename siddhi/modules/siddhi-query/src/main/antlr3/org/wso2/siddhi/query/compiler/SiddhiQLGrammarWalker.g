tree grammar SiddhiQLGrammarWalker;

options {
    backtrack=true;
    tokenVocab=SiddhiQLGrammar;
    ASTLabelType=CommonTree;
}
 
@header {
	package org.wso2.siddhi.query.compiler;

    import org.wso2.siddhi.query.api.ExecutionPlan;
    import org.wso2.siddhi.query.api.QueryFactory;
    import org.wso2.siddhi.query.api.condition.Condition;
    import org.wso2.siddhi.query.api.definition.Attribute;
    import org.wso2.siddhi.query.api.definition.StreamDefinition;
    import org.wso2.siddhi.query.api.expression.Expression;
    import org.wso2.siddhi.query.api.expression.Variable;
    import org.wso2.siddhi.query.api.expression.constant.Constant;
    import org.wso2.siddhi.query.api.query.Query;
    import org.wso2.siddhi.query.api.query.projection.Projector;
    import org.wso2.siddhi.query.api.query.projection.attribute.AggregationAttribute;
    import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
    import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;
    import org.wso2.siddhi.query.api.stream.JoinStream;
    import org.wso2.siddhi.query.api.stream.SingleStream;
    import org.wso2.siddhi.query.api.stream.Stream;
    import org.wso2.siddhi.query.api.stream.handler.Handler;
    import org.wso2.siddhi.query.api.stream.pattern.Pattern;
    import org.wso2.siddhi.query.api.stream.pattern.element.LogicalElement;
    import org.wso2.siddhi.query.api.stream.pattern.element.PatternElement;
    import org.wso2.siddhi.query.api.stream.pattern.PatternStream;
    import org.wso2.siddhi.query.api.stream.sequence.Sequence;
    import org.wso2.siddhi.query.api.stream.sequence.SequenceStream;
    import org.wso2.siddhi.query.api.stream.sequence.element.SequenceElement;

} 

executionPlan returns [List<ExecutionPlan> executionPlanList]
    @init{
        $executionPlanList=new ArrayList<ExecutionPlan>();
    }
	: (^(DEFINITION definitionStream {$executionPlanList.add($definitionStream.streamDefinition);}))*  (query)*
	; 
   
definitionStream returns [StreamDefinition streamDefinition]
	@init{
        $streamDefinition = QueryFactory.createStreamDefinition();
    }
	:  ^(streamId {$streamDefinition.name($streamId.value);} (^(IN_ATTRIBUTE attributeName type  {$streamDefinition.attribute($attributeName.value, $type.type);}))+)
	;

query returns [Query query]
	: ^(QUERY outputStream inputStream outputProjection ) {$query = QueryFactory.createQuery().insertInto($outputStream.value).
																						from($inputStream.inStream).
																						project($outputProjection.projector);}
	;

outputStream returns [String value]
	:  ^(OUT_STREAM streamId outputType?) {$value=$streamId.value;}
	;

outputType
	: 'expired-events' | 'current-events' | 'all-events'
	;

inputStream returns [Stream inStream]
	: ^(SEQUENCE_FULL sequenceFullStream) {$inStream=$sequenceFullStream.stream;}
	| ^(PATTERN_FULL  patternFullStream patternHandler?) {$inStream=$patternFullStream.stream;}
	| ^(JOIN joinStream) 	{$inStream=$joinStream.stream;}
	| windowStream          {$inStream=$windowStream.singleStream;}
	| stream  	            {$inStream=$stream.singleStream;}
	;  
 
stream returns [SingleStream singleStream]
	: ^(STREAM basicStream {$singleStream=$basicStream.singleStream;} (id {$singleStream.setStreamReferenceId($id.value);})?)
	;
	
windowStream returns [SingleStream singleStream]
	: ^(STREAM basicStream {$singleStream=$basicStream.singleStream;} (windowHandler {$singleStream.addHandler($windowHandler.handler);}) (id {$singleStream.setStreamReferenceId($id.value);})?)
	;
	
basicStream returns [SingleStream singleStream]
	: ^(streamId {$singleStream=QueryFactory.inputStream($streamId.value);} ^( HANDLERS  (handler {$singleStream.addHandler($handler.handler);})+))
	| streamId {$singleStream=QueryFactory.inputStream($streamId.value);}
	| ^(ANONYMOUS returnQuery {$singleStream=$returnQuery.stream;} ^( HANDLERS  (handler {$singleStream.addHandler($handler.handler);} )+))
	| ^(ANONYMOUS returnQuery {$singleStream=$returnQuery.stream;})
	; 
	 
/**	  
stream returns [SingleStream singleStream]
	: ^(streamId {$singleStream=QueryFactory.inputStream($streamId.value);} ^( HANDLERS  (handler {$singleStream.addHandler($handler.handler);})+) (id {$singleStream.setStreamReferenceId($id.value);})?)
	| ^(streamId {$singleStream=QueryFactory.inputStream($streamId.value);} (id {$singleStream.setStreamReferenceId($id.value);})?)
	| ^(ANONYMOUS returnQuery {$singleStream=$returnQuery.stream;} ^( HANDLERS  (handler {$singleStream.addHandler($handler.handler);} )+) (id {$singleStream.setStreamReferenceId($id.value);})?)
	| ^(ANONYMOUS returnQuery {$singleStream=$returnQuery.stream;} (id {$singleStream.setStreamReferenceId($id.value);})?)
	; 
**/

joinStream  returns [Stream stream]
	scope{
		Condition onCondition;
		Expression within;
	}
	: leftStream join rightStream (condition {$joinStream::onCondition=$condition.condition;})? (time {$joinStream::within=$time.expression;})? 				  { $stream=QueryFactory.joinStream($leftStream.singleStream,$join.type,$rightStream.singleStream,$joinStream::onCondition,(Constant)$joinStream::within);}
	| leftStream 'unidirectional'  join rightStream (condition {$joinStream::onCondition=$condition.condition;})? (time {$joinStream::within=$time.expression;})? { $stream=QueryFactory.joinStream($leftStream.singleStream,$join.type,$rightStream.singleStream,$joinStream::onCondition,(Constant)$joinStream::within,JoinStream.EventTrigger.LEFT);}
	| leftStream  join rightStream 'unidirectional' (condition {$joinStream::onCondition=$condition.condition;})? (time {$joinStream::within=$time.expression;})? { $stream=QueryFactory.joinStream($leftStream.singleStream,$join.type,$rightStream.singleStream,$joinStream::onCondition,(Constant)$joinStream::within,JoinStream.EventTrigger.RIGHT);}
	;

leftStream returns [SingleStream singleStream]
    :  windowStream {$singleStream=$windowStream.singleStream;}
    |  stream       {$singleStream=$stream.singleStream;}
    ;

rightStream returns [SingleStream singleStream]
    :  windowStream {$singleStream=$windowStream.singleStream;}
    |  stream       {$singleStream=$stream.singleStream;}
    ;
 
returnQuery returns [SingleStream stream]
	@init{
		System.err.println("Return Query not yet supported!");
	}
	: ^(RETURN_QUERY  inputStream outputProjection) {$stream=QueryFactory.createQuery().from($inputStream.inStream).project($outputProjection.projector).returnStream();}
	;

patternFullStream returns [PatternStream stream]
	: ^(PATTERN  patternStream {$stream= QueryFactory.patternStream($patternStream.element);} (time {$stream= QueryFactory.patternStream($patternStream.element,(Constant)$time.expression);})?  )
	;

patternHandler returns [Handler handler]
	: common {$handler=new Handler($common.name, $common.type, $common.handlerParameters);} {System.err.println("Pattern Handler not yet supported!");}
	;

patternStream returns [PatternElement element]
	: ^('every'  patternItem) {$element=Pattern.every($patternItem.element);} (p=patternStream {$element=Pattern.followedBy($element,$p.element);})?
	| ^('every' nonEveryPatternStream ) {$element=Pattern.every($nonEveryPatternStream.element);}   (p=patternStream {$element=Pattern.followedBy($element,$p.element);})?
	| patternItem {$element=$patternItem.element;} (p=patternStream {$element=Pattern.followedBy($element,$p.element);})?
	;
    
nonEveryPatternStream returns [PatternElement element]
	: patternItem {$element=$patternItem.element;} (p=nonEveryPatternStream {$element=Pattern.followedBy($element,$p.element);})?
	;

sequenceFullStream returns [SequenceStream stream]
	: ^(SEQUENCE  sequenceStream {$stream= QueryFactory.sequenceStream($sequenceStream.element);} (time {$stream= QueryFactory.sequenceStream($sequenceStream.element,(Constant)$time.expression);})? )
	;
	
sequenceStream returns [SequenceElement element]
	@init{
		List<SequenceElement> elementList=new ArrayList<SequenceElement>();
	}
	@after{
		$element=elementList.get(elementList.size()-1);
		for(int i=elementList.size()-2;i>=0;i--){
			$element=Sequence.next(elementList.get(i),$element);
		}
	}
	:  (sequenceItem {elementList.add($sequenceItem.element);} )+
	;
	
patternItem returns [PatternElement element]
	: ^('and' i1=itemStream i2=itemStream) 	{element=Pattern.logical($i1.singleStream,LogicalElement.Type.AND,$i2.singleStream);}
	| ^('or'  i1=itemStream i2=itemStream)	{element=Pattern.logical($i1.singleStream,LogicalElement.Type.OR,$i2.singleStream);}
	| ^(COLLECT itemStream collect) 	{element=Pattern.count($itemStream.singleStream,$collect.startVal,$collect.endVal);}
	| itemStream						{element=$itemStream.singleStream;}
	;

sequenceItem returns [SequenceElement element]
	: ^('or' i1=itemStream i2=itemStream) 					{element=Sequence.or($i1.singleStream,$i2.singleStream);}
	| ^(REGEX itemStream regex[$itemStream.singleStream])  	{element=$regex.element;}
	| itemStream											{element=$itemStream.singleStream;}
	;   

itemStream returns [SingleStream singleStream]
	: ^(STREAM basicStream {$singleStream=$basicStream.singleStream;}(attributeName {$singleStream.setStreamReferenceId($attributeName.value);})?)
	;
		

regex [SingleStream singleStream] returns [SequenceElement element]
	: ('*' {$element=Sequence.zeroOrMany($singleStream);} |'+' {$element=Sequence.oneOrMany($singleStream);}|'?' {$element=Sequence.zeroOrOne($singleStream);}) ('?' {System.err.println(" ? variation in regex not yet supported!");})?
	;

outputProjection returns [Projector projector]
	: {$projector = QueryFactory.outputProjector();} 
		externalCall? 
		^(OUT_ATTRIBUTES outputAttributeList {$projector.addProjectionList($outputAttributeList.attributeList);} ) 
		(groupBy {$projector.addGroupByList($groupBy.variables);})?
		(having {$projector.having($having.value);})? 
	;
	
outputAttributeList returns [List<OutputAttribute> attributeList]
	@init{
		$attributeList=new ArrayList<OutputAttribute>();
	}
	: '*' 
	| ( ^(OUT_ATTRIBUTE outputItem {$attributeList.add($outputItem.value);}))+
	;
	
outputItem returns [OutputAttribute value]
	: outFuction id			{$value=new AggregationAttribute($id.value, $outFuction.name, $outFuction.expressions);}
	| constant id			{$value=new SimpleAttribute($id.value, $constant.expression);}
	| attributeVariable id 	{$value=new SimpleAttribute($id.value, $attributeVariable.variable);}
	| attributeVariable		{$value=new SimpleAttribute($attributeVariable.variable.getAttributeName(), $attributeVariable.variable);}
	;
	
outFuction returns [String name, Expression[\] expressions]
	:  ^( FUNCTION ID {$name=$ID.text;} (parameters  {$expressions=$parameters.expressions;})?) 
	;

groupBy returns [List<Variable> variables]
	@init{
		$variables=new ArrayList<Variable>();
	}	
	: ^('group' (attributeVariable {$variables.add($attributeVariable.variable);})+)
	;

having returns [Condition value]
	: ^('having' condition) {$value=$condition.condition;}
	;

externalCall
	: ^( 'call' ^(ID parameters?))
	;

handler returns [Handler handler]
	: condition {$handler=new Handler(null, Handler.Type.FILTER, new Object[]{$condition.condition});}
	| common	{$handler=new Handler($common.name, $common.type, $common.handlerParameters);}
	; 
 
windowHandler returns [Handler handler]
	: ^(WINDOW id {$handler=new Handler($id.value, Handler.Type.WIN, null);} (parameters {$handler=new Handler($id.value, Handler.Type.WIN, $parameters.expressions);})? )
	;
	
common returns [Handler.Type type, String name, Expression[\] handlerParameters]
	: ^(handlerType {$type=$handlerType.type;} id {$name=$id.value;} (parameters {$handlerParameters=$parameters.expressions;})? )
	; 
	
parameters returns [Expression[\] expressions]
	scope{
		List<Expression> parameterlist;
	}
	@init{
		$parameters::parameterlist=new ArrayList<Expression>();
	}
	@after{
		$expressions=$parameters::parameterlist.toArray(new Expression[$parameters::parameterlist.size()]);
	}	
	:  ^(PARAMETERS (parameter {$parameters::parameterlist.add($parameter.expression);})+)
	;

time returns [Expression expression]
	: constant {$expression=$constant.expression;}
	;
	
parameter returns [Expression expression]
	: modExpression	{$expression=$modExpression.expression;}
	;

collect returns [int startVal , int endVal]
	@init{
		$startVal=0;
		$endVal=-2;//UNLIMITED
	}
	: countStart ':' countEnd 	{$startVal=$countStart.value; $endVal=$countEnd.value;}
	| countStart ':'			{$startVal=$countStart.value;}
	| ':' countEnd 				{$endVal=$countEnd.value;}
	| countStartAndEnd 		{$startVal=$countStartAndEnd.value; $endVal=$countStartAndEnd.value;}
	
	;	

countStart returns [int value]
	:POSITIVE_INT {$value=Integer.parseInt($POSITIVE_INT.text);}
	;
	
countEnd returns [int value]
	:POSITIVE_INT {$value=Integer.parseInt($POSITIVE_INT.text);}
	;
		
countStartAndEnd returns [int value]
	:POSITIVE_INT {$value=Integer.parseInt($POSITIVE_INT.text);}
	;			

//conditions start

condition returns [Condition condition]
	: ^(CONDITION conditionExpression {$condition=$conditionExpression.condition;})
	;

conditionExpression returns [Condition condition]
    : ^('or' c1=conditionExpression c2=conditionExpression) 	{$condition=Condition.or($c1.condition,$c2.condition);}
    | ^('and' c1=conditionExpression c2=conditionExpression) 	{$condition=Condition.and($c1.condition,$c2.condition);}
    | ^( compareOperation m1=modExpression m2=modExpression)	{$condition=Condition.compare($m1.expression,$compareOperation.operator,$m2.expression);}
    | ^('not' c=conditionExpression)							{$condition=Condition.not($c.condition);}
    |boolVal  													{System.err.println("Boolean Value as conditon not yet supported!");}
	;
	
modExpression returns [Expression expression]
	: ^('%' e1=modExpression e2=modExpression)	{$expression=Expression.mod($e1.expression,$e2.expression);}
    | ^('/' e1=modExpression e2=modExpression)	{$expression=Expression.divide($e1.expression,$e2.expression);}
    | ^('*' e1=modExpression e2=modExpression)	{$expression=Expression.multiply($e1.expression,$e2.expression);}
    | ^('-' e1=modExpression e2=modExpression)	{$expression=Expression.minus($e1.expression,$e2.expression);}
    | ^('+' e1=modExpression e2=modExpression)	{$expression=Expression.add($e1.expression,$e2.expression);}
    |valueExpression 							{$expression=$valueExpression.expression;}
    ;

valueExpression returns [Expression expression]
   	:constant 			{$expression=$constant.expression;}
   	|attributeVariable	{$expression=$attributeVariable.variable;}
   	;


//conditions end 

constant returns [Expression expression]
	: ^( CONSTANT intVal) 		{$expression=Expression.value($intVal.value);}
	| ^( CONSTANT longVal)		{$expression=Expression.value($longVal.value);}
	| ^( CONSTANT floatVal)		{$expression=Expression.value($floatVal.value);}
	| ^( CONSTANT doubleVal)	{$expression=Expression.value($doubleVal.value);}
	| ^( CONSTANT boolVal)		{$expression=Expression.value($boolVal.value);}
	| ^( CONSTANT stringVal)	{$expression=Expression.value($stringVal.value);}
	;
	
streamId returns [String value]
	: id {$value=$id.value;}
	;

attributeVariable  returns [Variable variable]
	:streamPositionAttributeName	{$variable=Expression.variable($streamPositionAttributeName.stream,$streamPositionAttributeName.position,$streamPositionAttributeName.attribute);}
   	|streamAttributeName 			{$variable=Expression.variable($streamAttributeName.stream,$streamAttributeName.attribute);}
   	|attributeName					{$variable=Expression.variable($attributeName.value);}
	;

streamPositionAttributeName returns [String stream, int position, String attribute]
	: ^( ATTRIBUTE ^(streamId {$stream=$streamId.value;} POSITIVE_INT {$position=Integer.parseInt($POSITIVE_INT.text);}) id {$attribute=$id.value;})
	; 	

streamAttributeName returns [String stream, String attribute]
	:^( ATTRIBUTE (streamId {$stream=$streamId.value;}) id {$attribute=$id.value;})
	;
	
attributeName returns [String value]
	:^( ATTRIBUTE id {$value=$id.value;})
	;	
 
join returns [JoinStream.Type type]
	:  ^('join' ^('outer' 'left'))  {$type=JoinStream.Type.LEFT_OUTER_JOIN;}	{System.err.println("Left outer join not yet supported!");}
	|  ^('join' ^('outer' 'right'))	{$type=JoinStream.Type.RIGHT_OUTER_JOIN;} 	{System.err.println("Right outer join not yet supported!");}
	|  ^('join' ^('outer' 'full'))	{$type=JoinStream.Type.FULL_OUTER_JOIN;}	{System.err.println("Full outer join not yet supported!");}
	| ^('join' 'inner')				{$type=JoinStream.Type.INNER_JOIN;}
	;

compareOperation returns [Condition.Operator operator]
	:'=='   	{$operator=Condition.Operator.EQUAL;}
	|'!=' 		{$operator=Condition.Operator.NOT_EQUAL;}
	|'<='		{$operator=Condition.Operator.LESS_THAN_EQUAL;}
	|'>=' 		{$operator=Condition.Operator.GREATER_THAN_EQUAL;}
	|'<' 		{$operator=Condition.Operator.LESS_THAN;}
	|'>'  		{$operator=Condition.Operator.GREATER_THAN;}
	|'contains'	{System.err.println("contains not yet supported!");}
	;
	
id returns [String value]
    : ID {$value=$ID.text;}
    | ID_QUOTES {$value=$ID_QUOTES.text;}
    ;
	
intVal returns [int value]
    : POSITIVE_INT {$value=Integer.parseInt($POSITIVE_INT.text);}
    | NEGATIVE_INT {$value=Integer.parseInt($NEGATIVE_INT.text);}
    ;

longVal returns [long value]
    : LONG_VAL {$value=Long.parseLong($LONG_VAL.text);}
    ;

floatVal returns [float value]
    : FLOAT_VAL {$value=Float.parseFloat($FLOAT_VAL.text);}
    ;

doubleVal returns [double value]
    : DOUBLE_VAL {$value=Double.parseDouble($DOUBLE_VAL.text);}
    ;

boolVal returns [boolean value]
    : BOOL_VAL {$value=Boolean.parseBoolean($BOOL_VAL.text);}
    ;

stringVal returns [String value]
    : STRING_VAL {$value=$STRING_VAL.text;}
    ;
    
type returns [Attribute.Type type]
	:'string' 	{$type=Attribute.Type.STRING;} 
	|'int'  	{$type=Attribute.Type.INT;} 
	|'long' 	{$type=Attribute.Type.LONG;} 
	|'float' 	{$type=Attribute.Type.FLOAT;} 
	|'double' 	{$type=Attribute.Type.DOUBLE;} 
	|'bool' 	{$type=Attribute.Type.BOOL;} 
	; 

handlerType returns [Handler.Type type]
	:'filter'	{$type=Handler.Type.FILTER;}
//	|'expire'	{$type=Handler.Type.EXPIRE;}
//	|'std'		{$type=Handler.Type.STD;}
//	|'timer'	{$type=Handler.Type.TIMER;}
	;
	
	
	
	

