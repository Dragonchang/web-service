package com.shenlan.robots.iot.listener;

import com.shenlan.robots.iot.command.Command;
import com.shenlan.robots.iot.command.CommandContainer;
import com.shenlan.robots.iot.command.CommandProcess;
import com.shenlan.robots.iot.model.IotMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class RobotsMqttProcessor implements Processor {
    private static String DEVICE_CONNECTION_TOPIC_TAG = "connected";
    private static String DEVICE_DISCONNECTION_TOPIC_TAG = "disconnected";
    private static int waringCount = 50;

    @Autowired
    CommandContainer commandContainer;

    private ExecutorService executorService;

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory("RobotsMqttProcessor", false));
        }
        return executorService;
    }

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));
    private static final int MAXIMUM_POOL_SIZE = 60;
    private static final int KEEP_ALIVE_SECONDS = 20;

    private final Deque<CommandRunnable> mReadyCommands = new ArrayDeque<>();

    private final Deque<CommandRunnable> mRunningCommands = new ArrayDeque<>();

    @Override
    public void process(IotMessage message) {
        enqueueCommand(new CommandRunnable(this, message));
    }

    @Override
    public void executeMessage(IotMessage message) {
        if(message.isSystemTopic()) {
            if(message.getTopic().endsWith(DEVICE_DISCONNECTION_TOPIC_TAG)) {
                message.setTopic(DEVICE_DISCONNECTION_TOPIC_TAG);
            } else if(message.getTopic().endsWith(DEVICE_CONNECTION_TOPIC_TAG)) {
                message.setTopic(DEVICE_CONNECTION_TOPIC_TAG);
            } else {
                log.warn("unknown system topic "+message.getTopic());
            }
        }
        List<Class> commandEntity = commandContainer.getCommandByTopic(message.getTopic());
        if(commandEntity == null || commandEntity.isEmpty() || commandEntity.size() != 2) {
            log.error("this is not a command entity !");
            return;
        }
        Class<?> commandClass = commandEntity.get(0);
        Class<?> processClass = commandEntity.get(1);
        try {
            Command command = (Command) commandClass.newInstance();
            command.createCommand(message);
            CommandProcess process = (CommandProcess) processClass.newInstance();
            process.process(command);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processEnd(IotMessage message) {
        synchronized (this) {
            if(mReadyCommands.size() > waringCount
                    || mRunningCommands.size() > waringCount) {
                log.warn("processEnd this is warning log for mReadyCommands size : "+mReadyCommands.size()
                        +" mRunningCommands size: "+mRunningCommands.size());
            }
            for (Iterator<CommandRunnable> i = mRunningCommands.iterator(); i.hasNext(); ) {
                CommandRunnable serviceCommand = i.next();
                if (message == serviceCommand.message) {
                    i.remove();
                    break;
                }
            }
        }
    }

    private static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread result = new Thread(r, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    private void enqueueCommand(CommandRunnable command) {
        synchronized (this) {
            if(mReadyCommands.size() > waringCount
                || mRunningCommands.size() > waringCount) {
                    log.warn("enqueueCommand this is warning log for mReadyCommands size : "+mReadyCommands.size()
                        +" mRunningCommands size: "+mRunningCommands.size());
            }
            if(command == null) {
                return;
            }
            if(command.message != null && command.message.isSystemTopic()) {
                mReadyCommands.addFirst(command);
            } else {
                mReadyCommands.add(command);
            }
        }
        promoteAndExecute();
    }

    private void promoteAndExecute() {
        synchronized (this) {
            for (Iterator<CommandRunnable> i = mReadyCommands.iterator(); i.hasNext(); ) {
                CommandRunnable command = i.next();
                if(command == null) {
                    continue;
                }
                if(command.executeOn(executorService())) {
                    //成功加入到线程池队列中
                    i.remove();
                    mRunningCommands.add(command);
                }
            }
        }
    }
}
