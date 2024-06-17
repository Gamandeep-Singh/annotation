package com.example.classannotation.processor;

import com.example.classannotation.annotations.SSTLogger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLoggers")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CommonLoggerProcessor extends AbstractProcessor {

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

    private void generateLogger(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String loggerFieldName = "sstLogger";

        // Generate code for logger
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "generating Logger code ");

        // Write the logger code to a new file
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className+"Logger");
            PrintWriter writer = new PrintWriter(sourceFile.openWriter());
            writer.println("package " + packageName + ";");
            writer.println("public class " + className + "Logger{");
            writer.println("private com.example.classannotation.service.SstLogger logger = new com.example.classannotation.service.SstLogger(this.getClass().getName());");
            writer.println("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private void generateLogger(TypeElement typeElement) {
//        String className = typeElement.getSimpleName().toString();
//        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
//
//        // Generate code for logger
//        String loggerCode = " extends com.example.classannotation.service.SstLogger ";
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Logger code : " + loggerCode);
//
//        // Write the logger code to a new file
//        try {
//            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + ".sstLogger");
//            PrintWriter writer = new PrintWriter(sourceFile.openWriter());
//            writer.println("package " + packageName + ";");
//            writer.println("public class sstLogger "+loggerCode+" {");
//            writer.println("public sstLogger() { ");
//            writer.println("    super("+className+".class.getName());");
//            writer.println("}");
//            writer.println("public static final com.example.classannotation.service.SstLogger log = new com.example.classannotation.service.SstLogger("+className+".class.getName());");
//            writer.println("}");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void generateLogger(TypeElement typeElement) {
//        String className = typeElement.getSimpleName().toString();
//        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
//        String loggerFieldName = "sstLogger";
//
//        // Generate code for logger
//        String loggerCode = " extends com.example.classannotation.service.SstLogger ";
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Logger code : " + loggerCode);
//
//        // Write the logger code to a new file
//        try {
//            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className + "Logger");
//            PrintWriter writer = new PrintWriter(sourceFile.openWriter());
//            writer.println("package " + packageName + ";");
//            writer.println("public class " + className + "Logger "+loggerCode+" {");
//            writer.println("public "+ className + "Logger() { ");
//            writer.println("    super("+className+".class.getName());");
//            writer.println("}");
//            writer.println("}");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
