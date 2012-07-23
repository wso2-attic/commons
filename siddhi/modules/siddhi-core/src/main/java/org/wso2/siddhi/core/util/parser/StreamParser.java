/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.query.projector.QueryProjector;
import org.wso2.siddhi.core.statemachine.pattern.AndPatternState;
import org.wso2.siddhi.core.statemachine.pattern.CountPatternState;
import org.wso2.siddhi.core.statemachine.pattern.OrPatternState;
import org.wso2.siddhi.core.statemachine.pattern.PatternState;
import org.wso2.siddhi.core.statemachine.sequence.CountSequenceState;
import org.wso2.siddhi.core.statemachine.sequence.OrSequenceState;
import org.wso2.siddhi.core.statemachine.sequence.SequenceState;
import org.wso2.siddhi.core.query.stream.StreamProcessor;
import org.wso2.siddhi.core.query.stream.handler.RunnableHandler;
import org.wso2.siddhi.core.query.stream.handler.StreamHandler;
import org.wso2.siddhi.core.query.stream.handler.filter.FilterHandler;
import org.wso2.siddhi.core.query.stream.handler.window.WindowHandler;
import org.wso2.siddhi.core.query.stream.packer.SingleStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.join.JoinStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.join.LeftJoinInStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.join.LeftJoinRemoveStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.join.RightJoinInStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.join.RightJoinRemoveStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.pattern.AndPatternStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.pattern.CountPatternStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.pattern.OrPatternStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.pattern.PatternStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.sequence.CountSequenceStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.sequence.OrSequenceStreamPacker;
import org.wso2.siddhi.core.query.stream.packer.sequence.SequenceStreamPacker;
import org.wso2.siddhi.core.query.stream.recevier.SingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.StreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.pattern.AndPatternSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.pattern.CountPatternSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.pattern.OrPatternSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.pattern.PatternSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.pattern.PatternStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.sequence.CountSequenceSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.sequence.OrSequenceSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.sequence.SequenceSingleStreamReceiver;
import org.wso2.siddhi.core.query.stream.recevier.sequence.SequenceStreamReceiver;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.stream.JoinStream;
import org.wso2.siddhi.query.api.stream.SingleStream;
import org.wso2.siddhi.query.api.stream.Stream;
import org.wso2.siddhi.query.api.stream.handler.Handler;
import org.wso2.siddhi.query.api.stream.pattern.PatternStream;
import org.wso2.siddhi.query.api.stream.sequence.SequenceStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class StreamParser {


    public static List<StreamReceiver> parseStream(Stream queryStream,
                                                   List<QueryEventStream> queryEventStreamList,
                                                   QueryProjector queryProjector,
                                                   ThreadPoolExecutor threadPoolExecutor,
                                                   SiddhiContext siddhiContext) {
        List<StreamReceiver> streamReceiverList = new ArrayList<StreamReceiver>();

        if (queryStream instanceof SingleStream) {
            List<StreamProcessor> simpleStreamProcessorList = parseStreamHandler((SingleStream) queryStream, queryEventStreamList, new SingleStreamPacker(), siddhiContext);

            SingleStreamReceiver receiver = new SingleStreamReceiver((SingleStream) queryStream, simpleStreamProcessorList.get(0), threadPoolExecutor, siddhiContext);

            SingleStreamPacker singleStreamPacker = (SingleStreamPacker) simpleStreamProcessorList.get(simpleStreamProcessorList.size() - 1);

            //singleStreamPacker next
            singleStreamPacker.setNext(queryProjector);

            streamReceiverList.add(receiver);
            return streamReceiverList;

        } else if (queryStream instanceof JoinStream) {
            ConditionExecutor onConditionExecutor;
            if (((JoinStream) queryStream).getOnCompare() != null) {
                onConditionExecutor = ExecutorParser.parseCondition(((JoinStream) queryStream).getOnCompare(), queryEventStreamList, null);
            } else {
                onConditionExecutor = ExecutorParser.parseCondition(Condition.bool(Expression.value(true)), queryEventStreamList, null);
            }
            JoinStreamPacker leftJoinInStreamPacker;
            JoinStreamPacker rightJoinInStreamPacker;
            JoinStreamPacker leftJoinRemoveStreamPacker;
            JoinStreamPacker rightJoinRemoveStreamPacker;
            ReentrantLock lock = new ReentrantLock();
            switch (((JoinStream) queryStream).getTrigger()) {
                case LEFT:
                    leftJoinInStreamPacker = new LeftJoinInStreamPacker(onConditionExecutor, true, lock);
                    rightJoinInStreamPacker = new RightJoinInStreamPacker(onConditionExecutor, false, lock);
                    leftJoinRemoveStreamPacker = new LeftJoinRemoveStreamPacker(onConditionExecutor, true, lock);
                    rightJoinRemoveStreamPacker = new RightJoinRemoveStreamPacker(onConditionExecutor, false, lock);
                    break;
                case RIGHT:
                    leftJoinInStreamPacker = new LeftJoinInStreamPacker(onConditionExecutor, false, lock);
                    rightJoinInStreamPacker = new RightJoinInStreamPacker(onConditionExecutor, true, lock);
                    leftJoinRemoveStreamPacker = new LeftJoinRemoveStreamPacker(onConditionExecutor, false, lock);
                    rightJoinRemoveStreamPacker = new RightJoinRemoveStreamPacker(onConditionExecutor, true, lock);
                    break;
                default:
                    leftJoinInStreamPacker = new LeftJoinInStreamPacker(onConditionExecutor, true, lock);
                    rightJoinInStreamPacker = new RightJoinInStreamPacker(onConditionExecutor, true, lock);
                    leftJoinRemoveStreamPacker = new LeftJoinRemoveStreamPacker(onConditionExecutor, true, lock);
                    rightJoinRemoveStreamPacker = new RightJoinRemoveStreamPacker(onConditionExecutor, true, lock);
                    break;
            }

            SingleStream leftStream = (SingleStream) ((JoinStream) queryStream).getLeftStream();
            SingleStream rightStream = (SingleStream) ((JoinStream) queryStream).getRightStream();
            WindowHandler leftWindowHandler = generateWindowHandler(detachWindow(leftStream),siddhiContext);
            if (leftWindowHandler instanceof RunnableHandler) {
                siddhiContext.addRunnableHandler((RunnableHandler) leftWindowHandler);
            }
            WindowHandler rightWindowHandler = generateWindowHandler(detachWindow(rightStream),siddhiContext);
            if (rightWindowHandler instanceof RunnableHandler) {
                siddhiContext.addRunnableHandler((RunnableHandler) rightWindowHandler);
            }
            List<StreamProcessor> leftSimpleStreamProcessorList = parseStreamHandler(leftStream, queryEventStreamList, leftJoinInStreamPacker, siddhiContext);
            List<StreamProcessor> rightSimpleStreamProcessorList = parseStreamHandler(rightStream, queryEventStreamList, rightJoinInStreamPacker, siddhiContext);

            SingleStreamReceiver leftReceiver = new SingleStreamReceiver(leftStream, leftSimpleStreamProcessorList.get(0), threadPoolExecutor, siddhiContext);
            SingleStreamReceiver rightReceiver = new SingleStreamReceiver(rightStream, rightSimpleStreamProcessorList.get(0), threadPoolExecutor, siddhiContext);

            //joinStreamPacker next
            leftJoinInStreamPacker.setNext(queryProjector);
            rightJoinInStreamPacker.setNext(queryProjector);
            leftJoinRemoveStreamPacker.setNext(queryProjector);
            rightJoinRemoveStreamPacker.setNext(queryProjector);

            //WindowHandler joinStreamPacker relation settings
            leftJoinInStreamPacker.setWindowHandler(leftWindowHandler);
            leftWindowHandler.setNext(leftJoinRemoveStreamPacker);

            rightJoinInStreamPacker.setWindowHandler(rightWindowHandler);
            rightWindowHandler.setNext(rightJoinRemoveStreamPacker);


            //joinStreamPacker prev
            JoinStreamPacker leftSingleStreamPacker = (JoinStreamPacker) leftSimpleStreamProcessorList.get(leftSimpleStreamProcessorList.size() - 1);
            JoinStreamPacker rightSingleStreamPacker = (JoinStreamPacker) rightSimpleStreamProcessorList.get(rightSimpleStreamProcessorList.size() - 1);
            rightJoinInStreamPacker.setOppositeWindowHandler(leftSingleStreamPacker.getWindowHandler());
            leftJoinInStreamPacker.setOppositeWindowHandler(rightSingleStreamPacker.getWindowHandler());

            rightJoinRemoveStreamPacker.setOppositeWindowHandler(leftSingleStreamPacker.getWindowHandler());
            leftJoinRemoveStreamPacker.setOppositeWindowHandler(rightSingleStreamPacker.getWindowHandler());

            streamReceiverList.add(leftReceiver);
            streamReceiverList.add(rightReceiver);
            return streamReceiverList;

        } else if (queryStream instanceof PatternStream) {

            List<PatternState> patternStateList = StateParser.convertToPatternStateList(StateParser.identifyStates(((PatternStream) queryStream).getPatternElement()));
            //    queryEventStreamList ;
            // PatternStreamPacker patternStreamPacker = new PatternStreamPacker(stateList);
            // PatternSingleStreamReceiver[] patternSingleStreamReceiverArray = new PatternSingleStreamReceiver[stateList.size()];
            for (String streamId : queryStream.getStreamIds()) {

                //    List<SingleStream> streamList = new ArrayList<SingleStream>();
                List<PatternSingleStreamReceiver> patternSingleStreamReceiverList = new ArrayList<PatternSingleStreamReceiver>();
                for (PatternState state : patternStateList) {
                    if (state.getSingleStream().getStreamId().equals(streamId)) {
                        //           streamList.add(state.getSingleStream());
                        PatternStreamPacker patternStreamPacker;
                        if (state instanceof OrPatternState) {
                            patternStreamPacker = new OrPatternStreamPacker(((OrPatternState) state));
                        } else if (state instanceof AndPatternState) {
                            patternStreamPacker = new AndPatternStreamPacker(((AndPatternState) state));
                        } else if (state instanceof CountPatternState) {
                            patternStreamPacker = new CountPatternStreamPacker((CountPatternState) state);
                        } else {
                            patternStreamPacker = new PatternStreamPacker(state);
                        }
                        List<StreamProcessor> simpleStreamProcessorList = parseStreamHandler((SingleStream) state.getSingleStream(), queryEventStreamList, patternStreamPacker, siddhiContext);

                        PatternSingleStreamReceiver patternSingleStreamReceiver;

                        if (state instanceof OrPatternState) {
                            patternSingleStreamReceiver = new OrPatternSingleStreamReceiver(((OrPatternState) state), simpleStreamProcessorList.get(0), patternStateList.size());
                        } else if (state instanceof AndPatternState) {
                            patternSingleStreamReceiver = new AndPatternSingleStreamReceiver(((AndPatternState) state), simpleStreamProcessorList.get(0), patternStateList.size());
                        } else if (state instanceof CountPatternState) {
                            patternSingleStreamReceiver = new CountPatternSingleStreamReceiver(((CountPatternState) state), simpleStreamProcessorList.get(0), patternStateList.size());
                        } else {
                            patternSingleStreamReceiver = new PatternSingleStreamReceiver(state, simpleStreamProcessorList.get(0), patternStateList.size());
                        }


                        state.setPatternSingleStreamReceiver(patternSingleStreamReceiver);
                        patternSingleStreamReceiverList.add(patternSingleStreamReceiver);
                        //  patternSingleStreamReceiverArray[state.getStateNumber()] = patternSingleStreamReceiver;

//                        PatternStreamPacker patternStreamPacker = (PatternStreamPacker) simpleStreamProcessorList.get(simpleStreamProcessorList.size() - 1);
                        patternStreamPacker.setStreamReceiver(patternSingleStreamReceiver);
                        patternStreamPacker.setNext(queryProjector);
                        state.setPatternStreamPacker(patternStreamPacker);

                        //patternStreamPacker.setPrevious(singleStreamPacker); since not needed not set
                    }
                }

                PatternStreamReceiver receiver = new PatternStreamReceiver(streamId, patternSingleStreamReceiverList, threadPoolExecutor, siddhiContext);
                streamReceiverList.add(receiver);

                //for persistence
                for (PatternSingleStreamReceiver streamReceiver : patternSingleStreamReceiverList) {
                    siddhiContext.getPersistenceService().addPersister(streamReceiver);
                }

            }


            //   patternStreamPacker.setPatternSingleStreamReceiverArray(patternSingleStreamReceiverArray);
            //patternStreamPacker next
            //  patternStreamPacker.setNext(queryProjector, 0);

            for (PatternState state : patternStateList) {
                state.getPatternSingleStreamReceiver().init();
            }
            return streamReceiverList;

        } else if (queryStream instanceof SequenceStream) {


            List<SequenceState> sequenceStateList = StateParser.convertToSequenceStateList(StateParser.identifyStates(((SequenceStream) queryStream).getSequenceElement()));
            //    queryEventStreamList ;
            // PatternStreamPacker patternStreamPacker = new PatternStreamPacker(stateList);
            // PatternSingleStreamReceiver[] patternSingleStreamReceiverArray = new PatternSingleStreamReceiver[stateList.size()];
            for (String streamId : queryStream.getStreamIds()) {

                //    List<SingleStream> streamList = new ArrayList<SingleStream>();
                List<SequenceSingleStreamReceiver> sequenceSingleStreamReceiverList = new ArrayList<SequenceSingleStreamReceiver>();
                for (SequenceState state : sequenceStateList) {
                    if (state.getSingleStream().getStreamId().equals(streamId)) {
                        //           streamList.add(state.getSingleStream());
                        SequenceStreamPacker sequenceStreamPacker;
                        if (state instanceof OrSequenceState) {
                            sequenceStreamPacker = new OrSequenceStreamPacker(((OrSequenceState) state));
                        } else if (state instanceof CountSequenceState) {
                            sequenceStreamPacker = new CountSequenceStreamPacker((CountSequenceState) state);
                        } else {
                            sequenceStreamPacker = new SequenceStreamPacker(state);
                        }
                        List<StreamProcessor> simpleStreamProcessorList = parseStreamHandler((SingleStream) state.getSingleStream(), queryEventStreamList, sequenceStreamPacker, siddhiContext);

                        SequenceSingleStreamReceiver sequenceSingleStreamReceiver;

                        if (state instanceof OrSequenceState) {
                            sequenceSingleStreamReceiver = new OrSequenceSingleStreamReceiver(((OrSequenceState) state), simpleStreamProcessorList.get(0), sequenceStateList.size());
                        } else if (state instanceof CountSequenceState) {
                            sequenceSingleStreamReceiver = new CountSequenceSingleStreamReceiver(((CountSequenceState) state), simpleStreamProcessorList.get(0), sequenceStateList.size());
                        } else {
                            sequenceSingleStreamReceiver = new SequenceSingleStreamReceiver(state, simpleStreamProcessorList.get(0), sequenceStateList.size());
                        }


                        state.setSequenceSingleStreamReceiver(sequenceSingleStreamReceiver);
                        sequenceSingleStreamReceiverList.add(sequenceSingleStreamReceiver);
                        //  patternSingleStreamReceiverArray[state.getStateNumber()] = patternSingleStreamReceiver;

//                        PatternStreamPacker patternStreamPacker = (PatternStreamPacker) simpleStreamProcessorList.get(simpleStreamProcessorList.size() - 1);
                        sequenceStreamPacker.setStreamReceiver(sequenceSingleStreamReceiver);
                        sequenceStreamPacker.setNext(queryProjector);
                        state.setSequenceStreamPacker(sequenceStreamPacker);

                        //patternStreamPacker.setPrevious(singleStreamPacker); since not needed not set
                    }
                }

                SequenceStreamReceiver receiver = new SequenceStreamReceiver(streamId, sequenceSingleStreamReceiverList, threadPoolExecutor, siddhiContext);
                streamReceiverList.add(receiver);

                //for persistence
                for (SequenceSingleStreamReceiver streamReceiver : sequenceSingleStreamReceiverList) {
                    siddhiContext.getPersistenceService().addPersister(streamReceiver);
                }
            }

            //   patternStreamPacker.setPatternSingleStreamReceiverArray(patternSingleStreamReceiverArray);
            //patternStreamPacker next
            //  patternStreamPacker.setNext(queryProjector, 0);

            for (SequenceState state : sequenceStateList) {
                state.getSequenceSingleStreamReceiver().init();
            }

            for (StreamReceiver streamReceiver : streamReceiverList) {
                List<SequenceSingleStreamReceiver> otherStreamReceiverList = new ArrayList<SequenceSingleStreamReceiver>();
                for (StreamReceiver otherStreamReceiver : streamReceiverList) {
                    if (otherStreamReceiver != streamReceiver) {
                        otherStreamReceiverList.addAll(((SequenceStreamReceiver) otherStreamReceiver).getSequenceSingleStreamReceiverList());
                    }
                }
                ((SequenceStreamReceiver) streamReceiver).setOtherStreamReceivers(otherStreamReceiverList);
            }
            return streamReceiverList;
        }
        return streamReceiverList;

    }

    private static Handler detachWindow(SingleStream singleStream) {
        Handler windowHandler = new Handler("length", Handler.Type.WIN, new Object[]{Integer.MAX_VALUE});
        for (Iterator<Handler> iterator = singleStream.getHandlerList().iterator(); iterator.hasNext(); ) {
            Handler handler = iterator.next();
            if (handler.getType() == Handler.Type.WIN) {
                windowHandler = handler;
                iterator.remove();
            }
        }
        return windowHandler;

    }

    private static List<StreamProcessor> parseStreamHandler(SingleStream inputStream,
                                                            List<QueryEventStream> queryEventStreamList,
                                                            SingleStreamPacker singleStreamPacker,
                                                            SiddhiContext context) {
        List<StreamProcessor> streamProcessorList = new ArrayList<StreamProcessor>();
        List<Handler> handlerList = inputStream.getHandlerList();
        for (int i = 0; i < handlerList.size(); i++) {
            Handler handler = handlerList.get(i);
            StreamHandler streamHandler = null;
            if (handler.getType() == Handler.Type.FILTER) {
                if (handler.getName() == null) {   //default filter
                    Condition condition = (Condition) handler.getParameters()[0];
                    streamHandler = new FilterHandler(ExecutorParser.parseCondition(condition, queryEventStreamList, inputStream.getStreamReferenceId()));

                }
            } else if (handler.getType() == Handler.Type.WIN) {
                streamHandler = generateWindowHandler(handler,context);
            }
            if (streamHandler instanceof RunnableHandler) {
                context.addRunnableHandler((RunnableHandler) streamHandler);
            }
            if (streamProcessorList.size() > 0) {
                StreamHandler prevStreamHandler = (StreamHandler) streamProcessorList.get(i - 1);
                prevStreamHandler.setNext(streamHandler);
            }
            streamProcessorList.add(streamHandler);

        }
        if (streamProcessorList.size() > 0) {
            StreamHandler lastStreamHandler = (StreamHandler) streamProcessorList.get(streamProcessorList.size() - 1);
            lastStreamHandler.setNext(singleStreamPacker);

        }
        streamProcessorList.add(singleStreamPacker);
        return streamProcessorList;
    }

    private static WindowHandler generateWindowHandler(Handler handler,
                                                       SiddhiContext siddhiContext) {
        WindowHandler windowHandler = (WindowHandler) org.wso2.siddhi.core.util.ClassLoader.loadClass(WindowHandler.class.getPackage().getName() + "." + handler.getName().substring(0, 1).toUpperCase() + handler.getName().substring(1) + "WindowHandler");
//                    WindowHandler windowHandler = new TimeWindowHandler();
        windowHandler.setParameters(handler.getParameters());

        //for persistence
        siddhiContext.getPersistenceService().addPersister(windowHandler);

        return windowHandler;
    }

}
