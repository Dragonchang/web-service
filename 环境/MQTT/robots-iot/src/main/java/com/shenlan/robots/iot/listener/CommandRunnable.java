package com.shenlan.robots.iot.listener;

import com.shenlan.robots.iot.model.IotMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
public class CommandRunnable  extends NamedRunnable{

    Processor processor;
    IotMessage message;

    CommandRunnable(Processor proc, IotMessage msg) {
        super("%s", msg.getTopic(), LocalDateTime.now().toString());
        processor = proc;
        message = msg;
    }

    public boolean executeOn(ExecutorService executorPool) {
        boolean success = false;
        try {
            executorPool.execute(this);
            success = true;
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } finally {
            if (!success) {
                log.warn("executeOn failed for topic: "+message.getTopic());
            }
        }
        return success;
    }

    @Override
    protected void execute() {
        if(processor == null) {
            log.error("processor is null");
            return;
        }
        processor.executeMessage(message);
        processor.processEnd(message);
    }
}
