/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.core.util.parser;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.statemachine.pattern.AndPatternState;
import org.wso2.siddhi.core.statemachine.pattern.CountPatternState;
import org.wso2.siddhi.core.statemachine.pattern.LogicPatternState;
import org.wso2.siddhi.core.statemachine.pattern.OrPatternState;
import org.wso2.siddhi.core.statemachine.pattern.PatternState;
import org.wso2.siddhi.core.statemachine.sequence.CountSequenceState;
import org.wso2.siddhi.core.statemachine.sequence.OrSequenceState;
import org.wso2.siddhi.core.statemachine.sequence.SequenceState;
import org.wso2.siddhi.core.util.statemachine.AndState;
import org.wso2.siddhi.core.util.statemachine.CountState;
import org.wso2.siddhi.core.util.statemachine.LogicState;
import org.wso2.siddhi.core.util.statemachine.OrState;
import org.wso2.siddhi.core.util.statemachine.State;
import org.wso2.siddhi.core.util.statemachine.StateNumber;
import org.wso2.siddhi.query.api.stream.SingleStream;
import org.wso2.siddhi.query.api.stream.pattern.PatternStream;
import org.wso2.siddhi.query.api.stream.pattern.element.CountElement;
import org.wso2.siddhi.query.api.stream.pattern.element.FollowedByElement;
import org.wso2.siddhi.query.api.stream.pattern.element.LogicalElement;
import org.wso2.siddhi.query.api.stream.pattern.element.PatternElement;
import org.wso2.siddhi.query.api.stream.sequence.element.NextElement;
import org.wso2.siddhi.query.api.stream.sequence.element.OrElement;
import org.wso2.siddhi.query.api.stream.sequence.element.RegexElement;
import org.wso2.siddhi.query.api.stream.sequence.element.SequenceElement;

import java.util.ArrayList;
import java.util.List;

public class StateParser {
    static final Logger log = Logger.getLogger(StateParser.class);

    //Pattern

    public static List<PatternState> convertToPatternStateList(List<State> stateList) {
        List<PatternState> patternStateList = new ArrayList<PatternState>(stateList.size());
        for (int i = 0, stateListSize = stateList.size(); i < stateListSize; i++) {
            State state = stateList.get(i);
            PatternState patternState;
            if (state instanceof AndState) {
                patternState = (new AndPatternState(state.getStateNumber(), state.getSingleStream()));
            } else if (state instanceof OrState) {
                patternState = (new OrPatternState(state.getStateNumber(), state.getSingleStream()));
            } else if (state instanceof CountState) {
                patternState = (new CountPatternState(state.getStateNumber(), state.getSingleStream(), ((CountState) state).getMin(), ((CountState) state).getMax()));
            } else {
                patternState = (new PatternState(state.getStateNumber(), state.getSingleStream()));
            }
            patternState.setStateNumber(i);
            patternState.setFirst(state.isFirst());
            patternState.setLast(state.isLast());
            patternStateList.add(patternState);
        }
        for (int i = 0, stateListSize = stateList.size(); i < stateListSize; i++) {
            State state = stateList.get(i);
            PatternState patternState = patternStateList.get(i);
            if (state.getNext() != -1) {
                patternState.setNextState(patternStateList.get(state.getNext()));
            }
            if (state.getNextEvery() != -1) {
                patternState.setNextEveryState(patternStateList.get(state.getNextEvery()));
            }
            if (state instanceof LogicState) {
                ((LogicPatternState) patternStateList.get(i)).setPartnerState(patternStateList.get(((LogicState) state).getPartnerNumber()));
            }
        }
        return patternStateList;
    }


    public static List<State> identifyStates(PatternElement patternElement) {
        List<State> stateList = identifyStates(patternElement, new ArrayList<State>(), new StateNumber(0), new ArrayList<Integer>(), true);
        //setting first state
        State firstState = stateList.get(0);
        firstState.setFirst(true);
        if (firstState instanceof LogicState) {
            stateList.get(((LogicState) firstState).getPartnerNumber()).setFirst(true);
        }
        //setting last state
        State lastState = stateList.get(stateList.size() - 1);
        lastState.setLast(true);
        if (lastState instanceof LogicState) {
            stateList.get(((LogicState) lastState).getPartnerNumber()).setLast(true);
        }


        return stateList;
    }


    private static List<State> identifyStates(PatternElement patternElement, List<State> stateList,
                                              StateNumber stateNumber, List<Integer> perv,
                                              boolean topLevel) {

        if (patternElement instanceof SingleStream) {
            stateList.add(new State(stateNumber.getNumber(), ((SingleStream) patternElement)));
            addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();
            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
        } else if (patternElement instanceof LogicalElement) {
            if (((LogicalElement) patternElement).getType() == LogicalElement.Type.OR) {
                stateList.add(new OrState(stateNumber.getNumber(), ((LogicalElement) patternElement).getSingleStream1(), stateNumber.getNumber() + 1));
                addStateAsNext(stateList, stateNumber, perv);
                stateNumber.increment();
                stateList.add(new OrState(stateNumber.getNumber(), ((LogicalElement) patternElement).getSingleStream2(), stateNumber.getNumber() - 1));
                //addStateAsNext(stateList, stateNumber, perv);
                stateNumber.increment();
            } else {//AND
                stateList.add(new AndState(stateNumber.getNumber(), ((LogicalElement) patternElement).getSingleStream1(), stateNumber.getNumber() + 1));
                addStateAsNext(stateList, stateNumber, perv);
                stateNumber.increment();
                stateList.add(new AndState(stateNumber.getNumber(), ((LogicalElement) patternElement).getSingleStream2(), stateNumber.getNumber() - 1));
                //addStateAsNext(stateList, stateNumber, perv);
                stateNumber.increment();
            }
            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
            perv.add(stateNumber.getNumber() - 2);
        } else if (patternElement instanceof CountElement) {
            stateList.add(new CountState(stateNumber.getNumber(), ((CountElement) patternElement).getSingleStream(), ((CountElement) patternElement).getMinCount(), ((CountElement) patternElement).getMaxCount()));
            addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();
            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
        } else if (patternElement instanceof FollowedByElement) {
            identifyStates(((FollowedByElement) patternElement).getPatternElement(), stateList, stateNumber, perv, topLevel);
            identifyStates(((FollowedByElement) patternElement).getFollowedByPatternElement(), stateList, stateNumber, perv, topLevel);
        } else if (patternElement instanceof PatternStream) {
            int firstEveryStateNumber = stateNumber.getNumber();
            if (!topLevel) {
                log.error("Every inside Every is not allowed !!");
            }
            identifyStates(((PatternStream) patternElement).getPatternElement(), stateList, stateNumber, perv, false);

            State lastState = stateList.get(stateList.size() - 1);
            State lastStatePartner = null;
            if (lastState instanceof LogicState) {
                lastStatePartner = stateList.get(((LogicState) lastState).getPartnerNumber());
            }
//            if (stateList.get(firstEveryStateNumber) instanceof LogicState) {
//                lastState.setNextEvery(firstEveryStateNumber);
////                lastState.addNextEvery(firstEveryStateNumber + 1);
//                if (lastStatePartner != null) {
//                    lastStatePartner.setNextEvery(firstEveryStateNumber);
////                    lastStatePartner.addNext(firstEveryStateNumber + 1);
//                }
//            } else {
            lastState.setNextEvery(firstEveryStateNumber);
            if (lastStatePartner != null) {
                lastStatePartner.setNextEvery(firstEveryStateNumber);
            }
//            }
        } else {
            log.error("Error! " + patternElement);
        }

        return stateList;
    }

    //Sequence

    public static List<SequenceState> convertToSequenceStateList(List<State> stateList) {
        List<SequenceState> sequenceStateList = new ArrayList<SequenceState>(stateList.size());
        for (int i = 0, stateListSize = stateList.size(); i < stateListSize; i++) {
            State state = stateList.get(i);
            SequenceState sequenceState;
            if (state instanceof OrState) {
                sequenceState = (new OrSequenceState(state.getStateNumber(), state.getSingleStream()));
            } else if (state instanceof CountState) {
                sequenceState = (new CountSequenceState(state.getStateNumber(), state.getSingleStream(), ((CountState) state).getMin(), ((CountState) state).getMax()));
            } else {
                sequenceState = (new SequenceState(state.getStateNumber(), state.getSingleStream()));
            }
            sequenceState.setStateNumber(i);
            sequenceState.setFirst(state.isFirst());
            sequenceState.setLast(state.isLast());
            sequenceStateList.add(sequenceState);
        }
        for (int i = 0, stateListSize = stateList.size(); i < stateListSize; i++) {
            State state = stateList.get(i);
            SequenceState sequenceState = sequenceStateList.get(i);
            if (state.getNext() != -1) {
                sequenceState.setNextState(sequenceStateList.get(state.getNext()));
            }

            if (state instanceof OrState) {
                ((OrSequenceState) sequenceStateList.get(i)).setPartnerState(sequenceStateList.get(((LogicState) state).getPartnerNumber()));
            }
        }
        return sequenceStateList;
    }


    public static List<State> identifyStates(SequenceElement sequenceElement) {
        List<State> stateList = identifyStates(sequenceElement, new ArrayList<State>(), new StateNumber(0), new ArrayList<Integer>());
        //setting first state
        State firstState = stateList.get(0);
        firstState.setFirst(true);
        if (firstState instanceof LogicState) {
            stateList.get(((LogicState) firstState).getPartnerNumber()).setFirst(true);
        }
        //setting last state
        State lastState = stateList.get(stateList.size() - 1);
        lastState.setLast(true);
        if (lastState instanceof LogicState) {
            stateList.get(((LogicState) lastState).getPartnerNumber()).setLast(true);
        }


        return stateList;
    }


    private static List<State> identifyStates(SequenceElement sequenceElement,
                                              List<State> stateList,
                                              StateNumber stateNumber, List<Integer> perv) {

        if (sequenceElement instanceof SingleStream) {
            stateList.add(new State(stateNumber.getNumber(), ((SingleStream) sequenceElement)));
            addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();
            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
        } else if (sequenceElement instanceof OrElement) {
            stateList.add(new OrState(stateNumber.getNumber(), ((OrElement) sequenceElement).getSingleStream1(), stateNumber.getNumber() + 1));
            addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();
            stateList.add(new OrState(stateNumber.getNumber(), ((OrElement) sequenceElement).getSingleStream2(), stateNumber.getNumber() - 1));
            //addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();

            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
            perv.add(stateNumber.getNumber() - 2);
        } else if (sequenceElement instanceof RegexElement) {
            stateList.add(new CountState(stateNumber.getNumber(), ((RegexElement) sequenceElement).getSingleStream(), ((RegexElement) sequenceElement).getMinCount(), ((RegexElement) sequenceElement).getMaxCount()));
            addStateAsNext(stateList, stateNumber, perv);
            stateNumber.increment();
            perv.clear();
            perv.add(stateNumber.getNumber() - 1);
        } else if (sequenceElement instanceof NextElement) {
            identifyStates(((NextElement) sequenceElement).getSequenceElement(), stateList, stateNumber, perv);
            identifyStates(((NextElement) sequenceElement).getNextSequenceElement(), stateList, stateNumber, perv);
        } else {
            log.error("Error! " + sequenceElement);
        }

        return stateList;
    }

    //General

    private static void addStateAsNext(List<State> stateList, StateNumber stateNumber,
                                       List<Integer> perv) {
        for (int prevState : perv) {
            stateList.get(prevState).setNext(stateNumber.getNumber());
        }
    }
}
