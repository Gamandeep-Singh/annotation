package com.example.classannotation.processor;

import com.example.classannotation.annotations.SSTLogger;
import com.google.auto.common.Visibility;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.named;

@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLogger")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ByteBuddyProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private final static Set<String>  classNames= new HashSet<String>();

    @Override
    public synchronized void init(javax.annotation.processing.ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SSTLogger.class)) {
            if (element instanceof TypeElement typeElement) {
                try {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : process for " + typeElement);
                    classNames.add(typeElement.getQualifiedName().toString());
                    //generateLoggerFieldDirectly(typeElement);
                } catch (Exception e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
                }
            }
        }
        return true;
    }

    public static Set<String> getClassNames(){
        return classNames;
    }
//    private void generateLoggerField(TypeElement typeElement) throws Exception {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz " + typeElement);
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz qualified name " + typeElement.getQualifiedName().toString());
////        Class<?> clazz = Class.forName(typeElement.getQualifiedName().toString());
////        Class<?> clazz = Class.forName("com.example.classannotation.demo.DemoSSTLoggerAnnotation");
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : generating new Buddy for " + typeElement.getQualifiedName());
//        new ByteBuddy()
//                .redefine(typeElement.getQualifiedName().getClass())
//                .defineField("sstLogger", String.class, Visibility.PUBLIC.ordinal())
//                .make()
//                .load(typeElement.getQualifiedName().getClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
//    }

//    private void generateLoggerFieldAgentBuilder(TypeElement typeElement) throws Exception {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz " + typeElement);
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz qualified name " + typeElement.getQualifiedName().toString());
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : generating new Buddy for " + typeElement.getQualifiedName());
//
//        String targetClassName = typeElement.getQualifiedName().toString();
//
//        Instrumentation instrumentation = ByteBuddyAgent.install(ByteBuddyAgent.AttachmentProvider.DEFAULT);
//        new AgentBuilder.Default()
//                .type(named(targetClassName))
//                .transform((builder, typeDescription, classLoader, module, nul) -> builder
//                        .defineField("_id", int.class, Visibility.PUBLIC.ordinal()))
//                .installOn(instrumentation);
//    }

//    private void generateLoggerFieldDirectly(TypeElement typeElement) throws Exception {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz " + typeElement);
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : converting to clazz qualified name " + typeElement.getQualifiedName().toString());
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "ByteBuddyProcessor : generating new Buddy for " + typeElement.getQualifiedName());
//
//        String targetClassName = typeElement.getQualifiedName().toString();
//
//         new ByteBuddy()
//                .redefine(typeElement.getClass(),
//                        ClassFileLocator.ForClassLoader.ofSystemLoader())
//                .defineField("id", int.class)
//                .make()
//                 .load(typeElement.getQualifiedName().getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST);
//
//    }

}
