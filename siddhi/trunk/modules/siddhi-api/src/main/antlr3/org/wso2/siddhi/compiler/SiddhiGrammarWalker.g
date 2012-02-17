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

 tree grammar SiddhiGrammarWalker;

options {
    tokenVocab=SiddhiGrammar;
    ASTLabelType=CommonTree;
    backtrack=true;
}

@header {
    package org.wso2.siddhi.compiler;

    import org.wso2.siddhi.api.OutputDefinition;
    import org.wso2.siddhi.api.QueryFactory;
    import org.wso2.siddhi.api.condition.Condition;
    import org.wso2.siddhi.api.condition.ExpirableCondition;
    import org.wso2.siddhi.api.condition.pattern.EveryCondition;
    import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
    import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
    import org.wso2.siddhi.api.condition.sequence.SequenceStarCondition;
    import org.wso2.siddhi.api.condition.where.ConditionOperator;
    import org.wso2.siddhi.api.condition.where.WhereCondition;
    import org.wso2.siddhi.api.eventstream.EventStream;
    import org.wso2.siddhi.api.eventstream.InputEventStream;
    import org.wso2.siddhi.api.eventstream.query.Query;
    import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
    import org.wso2.siddhi.api.eventstream.query.inputstream.property.StandardView;
    import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
    import org.wso2.siddhi.api.exception.SiddhiCompilationException;

    import java.util.Date;
    import java.util.LinkedList;

}

@members {
    QueryFactory qf =  QueryFactory.getInstance();
    List<EventStream> streamList =new ArrayList<EventStream>();
    int streamNameIndex=0;
    boolean inHaving=false;

    private String getNewStreamName() {
        return "TempStream"+(streamNameIndex++);
    }

    public String getErrorMessage(RecognitionException e,
                                      String[] tokenNames)
        {
            List stack = getRuleInvocationStack(e, this.getClass().getName());
            String msg = null;
            String inputContext =
                input.LT(-3) == null ? "" : ((Tree)input.LT(-3)).getText()+" "+
                input.LT(-2) == null ? "" : ((Tree)input.LT(-2)).getText()+" "+
                input.LT(-1) == null ? "" : ((Tree)input.LT(-1)).getText()+" >>>"+
                ((Tree)input.LT(1)).getText()+"<<< "+
                ((Tree)input.LT(2)).getText()+" "+
                ((Tree)input.LT(3)).getText();
            if ( e instanceof NoViableAltException ) {
               NoViableAltException nvae = (NoViableAltException)e;
               msg = " no viable alt; token="+e.token+
                  " (decision="+nvae.decisionNumber+
                  " state "+nvae.stateNumber+")"+
                  " decision=<<"+nvae.grammarDecisionDescription+">>";
            }
            else {
               msg = super.getErrorMessage(e, tokenNames);
            }
            msg= stack+" "+msg+" context=..."+inputContext+"...";

            throw new SiddhiCompilationException(msg);
        }
       public String getTokenErrorDisplay(Token t) {
            return t.toString();
        }
}

siddhiGrammar [List<EventStream> inputEventStreamList ]  returns [List<EventStream> eventStreamList ]
    @init{
        if(inputEventStreamList!=null){
            streamList = $inputEventStreamList;
        }
    }
    @after{
        $eventStreamList=streamList;
    }
	: (s=stream[streamList] { streamList.add($s.eventStream);} )+
	;
stream  [List<EventStream> inputEventStreamList] returns [EventStream eventStream]
    scope{
        String name;
    }
    @init{
        if(streamList.size()==0 && inputEventStreamList!=null){
            streamList = $inputEventStreamList;
        }
    }
    : ^( s=stmName {$stream::name=$s.value;} es=streamType {$eventStream=$es.eventStream;})
	;
streamType returns [EventStream eventStream]
    :  es = inputStm[$stream::name] {$eventStream=$es.inputEventStream;}
	|  q = queryStm[$stream::name] {$eventStream=$q.query;}
	;

inputStm [String name] returns [InputEventStream inputEventStream]
    @init{
        List<String> nameList =new ArrayList<String>();
        List<Class> classList =new ArrayList<Class>();
    }
    @after{
        String[] nameArray=new String[nameList.size()];
        Class[] classArray=new Class[classList.size()];
        $inputEventStream = new InputEventStream(
                                $name,
                                nameList.toArray(nameArray),
                                classList.toArray(classArray)
                            );
    }
    : (^(NAME c=type) {nameList.add($NAME.text); classList.add($c.classType);})+
	;

queryStm [String name] returns [Query query]
    scope{
        OutputDefinition outputDef;
        List<QueryInputStream> inputStreamList;
        WhereCondition whereCond;
        FollowedByCondition patternCond;
        SequenceCondition SequenceCond;
        Condition havingCond;
        String gpByCond;
        Map<String,Condition> condDefMap;
        Map<String,String> condOrder;
        String streamName;
        int condOrderNum;

    }
    @init{
        $queryStm::inputStreamList=new LinkedList<QueryInputStream>();
        $queryStm::condOrder=new HashMap<String,String>();
        $queryStm::condOrderNum=-1;;
        $queryStm::condDefMap=new HashMap<String,Condition>();
        List outputList = new LinkedList<String>();
        outputList.add("*");
        $queryStm::outputDef=new OutputDefinition(outputList);
        if(name==null){
            name=getNewStreamName();
        }
        $queryStm::streamName=name;
    }
    @after{
        if($queryStm::patternCond!=null){
            $query = qf.createQuery( $name,$queryStm::outputDef, $queryStm::inputStreamList,$queryStm::patternCond);
        } else if($queryStm::SequenceCond!=null){
            $query = qf.createQuery( $name,$queryStm::outputDef, $queryStm::inputStreamList,$queryStm::SequenceCond);
        } else if($queryStm::inputStreamList.size()==2 &&$queryStm::whereCond!=null){
            $query = qf.createQuery( $name,
                                     $queryStm::outputDef,
                                     qf.innerJoin($queryStm::inputStreamList.get(0),$queryStm::inputStreamList.get(1)),
                                     $queryStm::whereCond);
        } else if($queryStm::inputStreamList.size()==1 &&$queryStm::whereCond!=null){
             $query = qf.createQuery( $name, $queryStm::outputDef,$queryStm::inputStreamList.get(0),$queryStm::whereCond);

             if($queryStm::gpByCond!=null){
                $query.groupBy($queryStm::gpByCond);
             }
             if($queryStm::havingCond!=null){
                $query.having($queryStm::havingCond);
             }
        }else{
            throw new SiddhiCompilationException("Not fall under valid Query type: number of input streams="+$queryStm::inputStreamList.size());
        }
    }
    : queryInput queryCond? queryOutput?
	;
	
queryOutput
    @init{
        List outputList=new LinkedList<String>();
    }
    @after{
        $queryStm::outputDef= new OutputDefinition(outputList);
    }
	: ^('select' '*') { outputList.add("*");}
	| ^('select' ( outputItem {outputList.add($outputItem.value);})+)
	;
outputItem returns [String value]
    :^( NAME  {$value=$NAME.text+"=";}
        (
        aggregate {$value+=$aggregate.value;}
        |defaultOut {$value+=$defaultOut.value;}
        |outAttriName {$value+=$outAttriName.value;}
        )
    )
	|outAttriName {$value=$outAttriName.value;}
    ;
defaultOut returns [String value]
    :^(DEFAULT_OUTPUT  type val)
    {$value="("+$type.classType.getSimpleName()+")"+$val.value;}
	;
	
queryInput
	: ^('from' queryInputStm+)
	;
queryInputStm
    scope{
      QueryInputStream stream;
    }
    @after{
      $queryStm::inputStreamList.add($queryInputStm::stream);
    }
	:  ^(NEW_STREAM  queryStm[null]   {  $queryInputStm::stream=qf.from($queryStm.query); }
         stmProp?)
	|  ^(stmName
	    {
            for(EventStream eventStream :streamList){
                if(null!=eventStream&&eventStream.getStreamId().equals($stmName.value)){
                    $queryInputStm::stream=qf.from(eventStream);
                    break;
                }
            }
            if($queryInputStm::stream==null){
                throw new SiddhiCompilationException($stmName.value+" is not defined, assign the streams before using them");
            }
        }
        stmProp?)
	;
stmProp
	: ^(STREAM_PROP stmStd? stmWin?)
	;
stmWin
    : ^('win' winType NUM )     {$queryInputStm::stream.setWindow($winType.type,Integer.parseInt($NUM.text));}
	;
stmStd
	: ^('std' uniqueType attriName ) {$queryInputStm::stream.setStandardView($uniqueType.type,$attriName.value);}
	;
	
queryCond
	: ^(COND whareCond? gpByCond ? havingCond?)
	| ^('pattern'  ^(COND_DEF condDefs)  ^(COND pattern[false] {$queryStm::patternCond=new FollowedByCondition($pattern.condList);}) )
	| ^('sequence' ^(COND_DEF condDefs) ^(COND seq {$queryStm::SequenceCond=new SequenceCondition($seq.condList);}))
	;
whareCond
	:^('where' cond)
	{$queryStm::whereCond=(WhereCondition)$cond.value;}
	;
havingCond
    @init { inHaving=true; }
    @after { inHaving=false; }
	:^('having' cond)
	{$queryStm::havingCond=$cond.value;}
	;
gpByCond
	:^('group by' attriName)        {String stm=$queryStm::inputStreamList.get(0).getEventStream().getStreamId();$queryStm::gpByCond=stm+"."+$attriName.value;}
	|^('group by' streamAttriName) 	{$queryStm::gpByCond=$streamAttriName.value;}
	;
condDefs
	: condDef+
	;
condDef
  	: ^(NAME cond)
  	{$queryStm::condDefMap.put($NAME.text,$cond.value);}
	;
//pattern  returns [List<Condition> condList]
//  scope{
//      String n;
//  }
//  @init{
//     $pattern::n="";
//  }
//  : ^('!' pat=patternItem ) p=pattern
//  {$condList=$p.condList;Condition c=$condList.remove(0); $condList.add(0,qf.nonOccurrence($pat.cond).followedBy(c));}
//
//  | pat=patternItem       {$condList=new LinkedList<Condition>(); $condList.add($pat.cond);}
//     ( p=pattern {$condList.addAll($p.condList);} )?
//
//  | ^('every' p=pattern {$condList=new LinkedList<Condition>(); $condList.add(new EveryCondition($p.condList));} )
//     ( p=pattern {$condList.addAll($p.condList);} )?
//
//  ;


pattern [boolean  isAfterNullIn] returns [List<Condition> condList, boolean isAfterNullOut]
    scope{
       boolean afterNull;
    }
    @init{
         $pattern::afterNull= $isAfterNullIn;
    }
    @after{
           $isAfterNullOut=$pattern::afterNull;
    }
    : ^('every' nep=nonEveryPattern[$pattern::afterNull] {$condList=new LinkedList<Condition>(); $condList.add(new EveryCondition($nep.condList)); $pattern::afterNull=$nep.isAfterNullOut;})
       ( p=pattern[false] {$condList.addAll($p.condList);$pattern::afterNull=$p.isAfterNullOut;} )?
    |  nep=nonEveryPattern[$pattern::afterNull] {$condList=$nonEveryPattern.condList;$pattern::afterNull=$nep.isAfterNullOut;}
       ( p=pattern[$pattern::afterNull] {$condList.addAll($p.condList);$pattern::afterNull=$p.isAfterNullOut;} )?
    |  ^('!' pat=patternItem[$pattern::afterNull])  {$condList=new LinkedList<Condition>(); $condList.add($pat.cond);} ( p=pattern[true] {$condList.addAll($p.condList); $pattern::afterNull=$p.isAfterNullOut;} )
    ;
nonEveryPattern [boolean isAfterNull] returns [List<Condition> condList,  boolean isAfterNullOut]
    @after{
           $isAfterNullOut=$isAfterNull;
    }
    :   ^('!' pat=patternItem[$isAfterNull]  ) p=nonEveryPattern[true] {$condList=$p.condList;Condition c=$condList.remove(0); $condList.add(0,qf.nonOccurrence($pat.cond).followedBy(c)); $isAfterNull=$p.isAfterNullOut;}
    | pat=patternItem[$isAfterNull]  {$condList=new LinkedList<Condition>(); $condList.add($pat.cond);}
    ( p=nonEveryPattern[false] {$condList.addAll($p.condList);$isAfterNull=$p.isAfterNullOut;})?
    ;


patternItem [boolean isAfterNull] returns [ Condition cond]
	: ^(NAME {$cond= $queryStm::condDefMap.get($NAME.text);
        $queryStm::condOrder.put("this",""+$queryStm::condOrderNum);
        if(!$isAfterNull){$queryStm::condOrderNum++;};
        $cond=((WhereCondition)$cond).getNewInstance($queryStm::condOrder);
        $queryStm::condOrder.put($NAME.text,""+$queryStm::condOrderNum);}
	(withInTime { $cond=((ExpirableCondition)$cond).within($withInTime.value);})?)
	;
withInTime returns [Integer value]
 	: ^('within'  'time'  NUM ) {$value=Integer.parseInt($NUM.text);}
	;
seq returns [List<Condition> condList]
    @init{
        $condList=new LinkedList<Condition>();
    }
    :seqItem {$condList.add($seqItem.cond);} (s=seq {$condList.addAll($s.condList);})?
	;
seqItem returns [Condition cond]
	: ^( NAME {
	 $queryStm::condOrder.put($NAME.text,""+(++$queryStm::condOrderNum));
	 $cond=$queryStm::condDefMap.get($NAME.text);
	 $queryStm::condOrder.put("this",""+$queryStm::condOrderNum);
	 $cond=((WhereCondition)$cond).getNewInstance($queryStm::condOrder);
	   }
	    ('*' {$cond=new SequenceStarCondition($cond);})?
	)
	;
cond returns [Condition value]
    :^('or' r1=cond r2=cond )  {$value=qf.or($r1.value,$r2.value);}
    |^('and' a1=cond a2=cond )  {$value=qf.and($a1.value,$a2.value);}
    |^('not' n=cond)            {$value=qf.not($n.value);}
	|^(numOp m1=mathExpr m2=mathExpr)               {$value=qf.condition($m1.value,$numOp.op,$m2.value);}
    |MATH            {$value=qf.condition($MATH.text.substring(0,$MATH.text.length()));}
	;
mathExpr returns [String value]
    : ^('%' r1=mathExpr r2=mathExpr )   {$value=$r1.value+"\%"+$r2.value;}
    | ^('/' r1=mathExpr r2=mathExpr )   {$value=$r1.value+"/"+$r2.value;}
    | ^('*' r1=mathExpr r2=mathExpr )   {$value=$r1.value+"*"+$r2.value;}
    | ^('-' r1=mathExpr r2=mathExpr )   {$value=$r1.value+"-"+$r2.value;}
    | ^('+' r1=mathExpr r2=mathExpr )   {$value=$r1.value+"+"+$r2.value;}
    |vExpr     {$value=$vExpr.value;}
    ;
vExpr  returns [String value]
    :attriName {if( inHaving==true){$value=$queryStm::streamName+"."+$attriName.value;}else{$value=$queryStm::inputStreamList.get(0).getEventStream().getStreamId()+"."+$attriName.value; }}
    |streamAttriName    {$value=$streamAttriName.value;}
    |'('mathExpr')'     {$value="("+$mathExpr.value+")";}
    |val                {$value=$val.value;}
    |condAttriName      {$value=$condAttriName.value;}
    ;
 condAttriName  returns [String value]
     : '$' {$value="$";} (r=condRefName {$value+=$r.value;} ('[' e= eventNum ']'{$value+='.'+$e.value;})? | 'this' '[' 'prev' ']' {$value+="this.prev";} )
     '.' attriName   {$value+="."+$attriName.value;}
     ;
stmName returns [String value]
    :NAME                           {$value=$NAME.text;}
	;
streamAttriName returns [String value]
	:^(stmName  attriName)          {$value=$stmName.value +"."+$attriName.value;}
	;
attriName returns [String value]
    :NAME	                        {$value=$NAME.text;}
    ;
outAttriName returns [String value]
    @init{
        $value="";
    }
	: attriName                     {$value=$attriName.value; }
	| streamAttriName               {$value=$streamAttriName.value; }
	|^('$' ^(condRefName (eventNum  {$value+="."+$eventNum.value;})? NAME))
	{$value= "$"+$queryStm::condOrder.get($condRefName.value)+ $value+"."+$NAME.text;	}
	;
condRefName returns [String value]
	:NAME	    {$value=$NAME.text;}
	;
eventNum returns [String value]
	:NUM
	{
	    if($NUM.text.equals("0")){
	        $value="start";
	    }else{
	        throw new SiddhiCompilationException("Siddhi not yet support ["+$NUM.text+"] only [0] and [last] is supported for now");
	    }
	}
	|'last'     {$value="last";}
	;
aggregate returns [String value]
    :(a='avg'|a='sum'|a='max'|a='min'|a='count')  '(' outAttriName ')'
    {$value=$a.text+"("+$outAttriName.value+")";}
	;
val returns [String value]
    :unsignNum          {$value=$unsignNum.value;}
    |'+' unsignNum      {$value=$unsignNum.value;}
    |'-' unsignNum      {$value="-"+$unsignNum.value;}
    |t='true'           {$value=$t.text;}
    |f='false'          {$value=$f.text;}
    |string             {$value=$string.value;}
	;
unsignNum returns [String value]
    :NUM                {$value=$NUM.text;}
    |a=NUM '.' b=NUM    {$value=$a.text+"."+$b.text;}
	;
num returns [String value]
    @init{
        $value="";
    }
    : ( p='+'     {$value+=$p.text;}
        |m='-'    {$value+=$m.text;}
      )?
      unsignNum {$value=$unsignNum.value;}
	;
type returns [Class classType]
    :'string' {$classType=String.class;}
    |'int' {$classType=Integer.class;}
    |'long' {$classType=Long.class;}
    |'float' {$classType=Float.class;}
    |'double' {$classType=Double.class;}
    |'bool' {$classType=Boolean.class;}
    |'date' {$classType=Date.class;}
	;
winType returns [WindowType type]
   	:'time'                 {$type=WindowType.TIME;}
   	|'time' '.' 'batch'     {$type=WindowType.TIME_BATCH;}
   	|'length'               {$type=WindowType.LENGTH;}
   	|'length' '.' 'batch'   {$type=WindowType.LENGTH_BATCH;}
	;
uniqueType returns [StandardView type]
    :'firstUnique'  {$type= StandardView.FIRST_UNIQUE;}
    |'unique'       {$type= StandardView.UNIQUE;}
    ;
numOp returns [ConditionOperator op]
    :'=='        {$op= ConditionOperator.EQUAL;}
    |'!='        {$op= ConditionOperator.NOT_EQUAL;}
    |'<'         {$op= ConditionOperator.LESSTHAN;}
    |'>'         {$op= ConditionOperator.GREATERTHAN;}
    |'<='        {$op= ConditionOperator.LESSTHAN_EQUAL;}
    |'>='        {$op= ConditionOperator.GREATERTHAN_EQUAL;}
    |'contains'  {$op= ConditionOperator.CONTAINS;}
	;
string returns [String value]
    :STRING {$value=$STRING.text.substring(1,$STRING.text.length()-1);}
    ;



