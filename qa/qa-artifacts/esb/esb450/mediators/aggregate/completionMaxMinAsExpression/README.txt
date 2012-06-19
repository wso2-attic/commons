The completeCondition messageCount is set as expressions as below

<messageCount min="{get-property('CompMinMsgs')}" max="{get-property('CompMaxMsgs')}"/>

In runtime, the values for the two properties CompMinMsgs & CompMaxMsgs are set through the Property mediator. These values are then extracted using an xpath expression withing the aggregate mediator
