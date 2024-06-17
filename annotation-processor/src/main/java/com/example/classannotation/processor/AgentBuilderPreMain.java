package com.example.classannotation.processor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.modifier.Visibility;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class AgentBuilderPreMain {
    public static void premain(String arguments, Instrumentation instrumentation){
        System.out.println(" ###### ---- ##### ------ I am here");

        for(String className : ByteBuddyProcessor.getClassNames()){
            System.out.println(" ###### ---- ##### ------ Processing class file:" + className );

            new AgentBuilder.Default()
                .type(named(className))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .defineField("id", int.class, Visibility.PUBLIC.ordinal()))
                .installOn(instrumentation);
        }
    }
}
