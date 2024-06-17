package com.example.classannotation.processor;

import com.example.classannotation.annotations.SSTLogger;

import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLoggers")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CustomLoggerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SSTLogger.class)) {
            if (element instanceof TypeElement typeElement) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Class annotated with @SSTLogger: " + typeElement);
                generateLogger(typeElement);
            }
        }
        return true;
    }

//    private void generateLogger(TypeElement typeElement) {
//        String className = typeElement.getSimpleName().toString();
//        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
//
//        // Construct the full class name
//        String fullClassName = packageName + "." + className;
//
//        // Generate code to inject SstLogger instance
//        String loggerCode = "public static final com.example.classannotation.service.SstLogger sstLogger = new com.example.classannotation.service.SstLogger(" + className + ".class.getName());";
//
//        try {
//            // Get the original class source
//            String originalClassSource = processingEnv.getElementUtils().getAllMembers(typeElement).stream()
//                    .filter(e -> e.getKind() == ElementKind.CLASS)
//                    .map(e -> e.toString())
//                    .collect(Collectors.joining("\n"));
//
//            // Replace the class definition with the modified one including the logger
//            String modifiedClassSource = originalClassSource.replaceFirst("\\{", "{\n" + loggerCode);
//
//            // Write the modified class to the same package
//            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fullClassName);
//            try (PrintWriter writer = new PrintWriter(sourceFile.openWriter())) {
//                writer.println("package " + packageName + ";");
//                writer.println(modifiedClassSource);
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "modifiedClassSource annotated with @SSTLogger: " + modifiedClassSource);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    javax.annotation.processing.FilerException: Attempt to recreate a file for type com.example.classannotation.demo.DemoSSTLoggerAnnotation
//    at jdk.compiler/com.sun.tools.javac.processing.JavacFiler.checkNameAndExistence(JavacFiler.java:732)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacFiler.createSourceOrClassFile(JavacFiler.java:498)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacFiler.createSourceFile(JavacFiler.java:435)
//    at com.example.classannotation.processor.CustomLoggerProcessor.generateLogger(CustomLoggerProcessor.java:50)
//    at com.example.classannotation.processor.CustomLoggerProcessor.process(CustomLoggerProcessor.java:23)
//    at org.gradle.api.internal.tasks.compile.processing.DelegatingProcessor.process(DelegatingProcessor.java:62)
//    at org.gradle.api.internal.tasks.compile.processing.NonIncrementalProcessor.process(NonIncrementalProcessor.java:45)
//    at org.gradle.api.internal.tasks.compile.processing.DelegatingProcessor.process(DelegatingProcessor.java:62)
//    at org.gradle.api.internal.tasks.compile.processing.TimeTrackingProcessor.access$401(TimeTrackingProcessor.java:37)
//    at org.gradle.api.internal.tasks.compile.processing.TimeTrackingProcessor$5.create(TimeTrackingProcessor.java:99)
//    at org.gradle.api.internal.tasks.compile.processing.TimeTrackingProcessor$5.create(TimeTrackingProcessor.java:96)
//    at org.gradle.api.internal.tasks.compile.processing.TimeTrackingProcessor.track(TimeTrackingProcessor.java:117)
//    at org.gradle.api.internal.tasks.compile.processing.TimeTrackingProcessor.process(TimeTrackingProcessor.java:96)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacProcessingEnvironment.callProcessor(JavacProcessingEnvironment.java:1023)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacProcessingEnvironment.discoverAndRunProcs(JavacProcessingEnvironment.java:939)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run(JavacProcessingEnvironment.java:1267)
//    at jdk.compiler/com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing(JavacProcessingEnvironment.java:1382)
//    at jdk.compiler/com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1234)
//    at jdk.compiler/com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:916)
//    at jdk.compiler/com.sun.tools.javac.api.JavacTaskImpl.lambda$doCall$0(JavacTaskImpl.java:104)
//    at jdk.compiler/com.sun.tools.javac.api.JavacTaskImpl.invocationHelper(JavacTaskImpl.java:152)
//    at jdk.compiler/com.sun.tools.javac.api.JavacTaskImpl.doCall(JavacTaskImpl.java:100)
//    at jdk.compiler/com.sun.tools.javac.api.JavacTaskImpl.call(JavacTaskImpl.java:94)
//    at org.gradle.internal.compiler.java.IncrementalCompileTask.call(IncrementalCompileTask.java:92)
//    at org.gradle.api.internal.tasks.compile.AnnotationProcessingCompileTask.call(AnnotationProcessingCompileTask.java:94)
//    at org.gradle.api.internal.tasks.compile.ResourceCleaningCompilationTask.call(ResourceCleaningCompilationTask.java:57)
//    at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:59)
//    at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:41)
//    at org.gradle.api.internal.tasks.compile.daemon.AbstractIsolatedCompilerWorkerExecutor$CompilerWorkAction.execute(AbstractIsolatedCompilerWorkerExecutor.java:78)
//    at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
//    at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:54)
//    at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:48)
//    at org.gradle.internal.classloader.ClassLoaderUtils.executeInClassloader(ClassLoaderUtils.java:100)
//    at org.gradle.workers.internal.AbstractClassLoaderWorker.executeInClassLoader(AbstractClassLoaderWorker.java:48)
//    at org.gradle.workers.internal.FlatClassLoaderWorker.run(FlatClassLoaderWorker.java:32)
//    at org.gradle.workers.internal.FlatClassLoaderWorker.run(FlatClassLoaderWorker.java:22)
//    at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:102)
//    at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:71)
//    at org.gradle.process.internal.worker.request.WorkerAction$1.call(WorkerAction.java:146)
//    at org.gradle.process.internal.worker.child.WorkerLogEventListener.withWorkerLoggingProtocol(WorkerLogEventListener.java:41)
//    at org.gradle.process.internal.worker.request.WorkerAction.lambda$run$0(WorkerAction.java:143)
//    at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:80)
//    at org.gradle.process.internal.worker.request.WorkerAction.run(WorkerAction.java:135)
//    at jdk.internal.reflect.GeneratedMethodAccessor12.invoke(Unknown Source)
//    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//    at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
//    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
//    at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
//    at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
//    at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:414)
//    at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
//    at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
//    at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
//    at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
//    at java.base/java.lang.Thread.run(Thread.java:833)

    private void generateLogger(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String loggerFieldName = "sstLogger";

        // Generate code for logger
        String loggerCode = String.format("public static final com.example.classannotation.service.SstLogger %s = new com.example.classannotation.service.SstLogger("+className+".class.getName());", loggerFieldName);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Logger code : " + loggerCode);

        // Write the logger code to a new file
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className + "Logger");
            PrintWriter writer = new PrintWriter(sourceFile.openWriter());
            writer.println("package " + packageName + ";");
            writer.println("public class " + className + "Logger {");
            writer.println(loggerCode);
            writer.println("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
